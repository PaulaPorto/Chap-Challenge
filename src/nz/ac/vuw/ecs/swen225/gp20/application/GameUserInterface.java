package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicMenuBarUI;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board.direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Keys;
import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelManager;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.RecordGame;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.ReplayGame;
import nz.ac.vuw.ecs.swen225.gp20.render.MazeView;

/**
 * Class to handle the GUI, its generation and its methods.
 *
 * @author Nick Mazey 300473270
 *
 */
public class GameUserInterface extends JFrame {

  private static final long serialVersionUID = 4493155820799495705L;
  private boolean paused; // Whether or not the game is paused.
  private JPanel drawBoard; // JPanel for drawing the board.
  private Board activeLevel; // The board the game is based on.
  private JPanel levelInfo; // The level info panel.
  private MazeView boardView; // In order to keep a view and update it
  private Board[] boards; // The list of boards that can be loaded.
  private Board oldBoard; // The board being used before replaying.
  private int maxObjHeight; // The max height an object can be.
  private RecordGame activeRecord; // The recording being used.
  private ReplayGame activeReplay; // The replay being played.
  private boolean recording; // Whether or not the game is recording.
  private boolean replaying; // Whether or not the game is replaying.
  @SuppressWarnings("rawtypes")
  private HashMap<Character, InfoPanel> infoPanels; // The info panels used by the game info.
  private long evalTime; // The time used for evaluating whether or not a second has passed
  private long offset; // Time used to prevent getting infinite time from spam-pausing
  private boolean doneFrame; // Whether or not the current frame is finished.
  private boolean capFrameRate; // Whether or not the fps is capped.
  private boolean wasd; // Whether or not WASD control is enabled.

  /**
   * Initialises the GUI.
   */
  public GameUserInterface() {

    // Initialising variables
    doneFrame = false;
    wasd = false;
    paused = false;
    recording = false;
    capFrameRate = true;
    // Setting up the window
    setTitle("Chap's Challenge");
    if (isWindows()) {
      setPreferredSize(new Dimension(716, 562));
    } else {
      setPreferredSize(new Dimension(700, 520));
    }
    setMinimumSize(getPreferredSize());
    setLayout(new GridBagLayout());
    setResizable(true);
    setBackground(Color.BLACK);

    loadLevels();

    File[] saves = new File("saves").listFiles();
    if (saves != null && saves.length > 0) {
      File latest = null;
      for (File f : saves) {
        if (latest == null) {
          latest = f;
        } else {
          if (f.lastModified() > latest.lastModified()) {
            latest = f;
          }
        }
      }
      activeLevel = handleLoadSave(false, false, activeLevel, latest.toString());
    } else {
      activeLevel = boards[0];
    }

    // Adding stuff to it
    setJMenuBar(setupMenu());
    setScaler();

    levelInfo = setupLevelInfo();
    // Constraints for the level info.
    GridBagConstraints g = new GridBagConstraints();
    g.fill = GridBagConstraints.BOTH;
    g.weightx = 0.3;
    g.weighty = 1;
    levelInfo.setMinimumSize(new Dimension((int) (getMinimumSize().getWidth() * 0.3),
        (int) getMinimumSize().getHeight()));
    add(levelInfo, g);

    // Constraints for the drawBoard to allow scaling.
    GridBagConstraints dbCons = new GridBagConstraints();
    dbCons.fill = GridBagConstraints.BOTH;
    dbCons.weightx = 0.7;
    dbCons.weighty = 1;
    drawBoard = new JPanel();
    drawBoard.setBackground(Color.BLACK);
    drawBoard.setBorder(null);
    drawBoard.setMinimumSize(new Dimension((int) (getMinimumSize().getWidth() * 0.7),
        (int) getMinimumSize().getHeight()));
    drawBoard.setLayout(new GridBagLayout());
    add(drawBoard, dbCons);

    boardView = new MazeView(activeLevel);

    // Finalising the window
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        confirmExit();
      }
    });
    addKeyListener(setupKeys());
    pack();
    setVisible(true);
    evalTime = System.currentTimeMillis();
  }

  /**
   * A method to determine if the computer running the application is Windows or
   * not.
   * 
   * @return - True if running on windows, false if not.
   */
  private boolean isWindows() {
    return System.getProperty("os.name").contains("Windows");
  }

  /**
   * Loads all the levels into the game.
   */
  public void loadLevels() {
    boards = new Board[2];
    boards[0] = handleLoadSave(false, false, boards[0], "levels/level1.json");
    boards[1] = handleLoadSave(false, false, boards[1], "levels/level2.json");
  }

  /**
   * A method to create the menu bar so it is easier to modify.
   *
   * @return returns the menu bar for the window.
   */
  private JMenuBar setupMenu() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(setupGameMenu());
    menuBar.add(setupReplayMenu());
    menuBar.add(setupExitMenu());
    menuBar.add(setupHelpMenu());

    // Adding dark mode to the menu bar
    menuBar.setUI(new BasicMenuBarUI() {
      @Override
      public void paint(Graphics g, JComponent c) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
      }
    });
    menuBar.setBackground(Color.BLACK);

    for (MenuElement menuElement : menuBar.getSubElements()) {
      JMenu menu = (JMenu) menuElement.getComponent();
      menu.setOpaque(true);
      menu.setForeground(Color.WHITE);
      menu.setBackground(Color.BLACK);

      for (MenuElement e : menu.getSubElements()) {
        JPopupMenu popup = (JPopupMenu) e.getComponent();
        popup.setBorder(null);
        for (MenuElement m : popup.getSubElements()) {
          JMenuItem lastItem = (JMenuItem) m.getComponent();
          lastItem.setOpaque(true);
          lastItem.setBackground(Color.BLACK);
          lastItem.setForeground(Color.WHITE);
        }

      }

    }

    return menuBar;
  }

  /**
   * Method to setup the game menu and sub menus so it is easier to modify.
   *
   * @return The Game sub-menu.
   */
  private JMenu setupGameMenu() {

    JMenuItem pauseGame = new JMenuItem("Pause");
    pauseGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!paused) {
          togglePaused();
        }
      }
    });
    KeyStroke pause = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
    pauseGame.setAccelerator(pause);

    JMenuItem resumeGame = new JMenuItem("Resume");
    resumeGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (paused) {
          togglePaused();
        }
      }
    });
    KeyStroke resume = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    resumeGame.setAccelerator(resume);

    JMenuItem resetLevel = new JMenuItem("Reset Level");
    resetLevel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        activeLevel.resetLevel();
      }
    });
    KeyStroke reset = KeyStroke.getKeyStroke(KeyEvent.VK_P, 2);
    resetLevel.setAccelerator(reset);

    JMenuItem restartGame = new JMenuItem("Restart Game");
    KeyStroke restart = KeyStroke.getKeyStroke(KeyEvent.VK_1, 2);
    restartGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        activeLevel = boards[0];
        activeLevel.resetLevel();
      }
    });
    restartGame.setAccelerator(restart);

    JMenuItem reloadLastUnfinished = new JMenuItem("Restart Last Unfinished Level");
    KeyStroke reloadUnfinished = KeyStroke.getKeyStroke(KeyEvent.VK_D, 2);
    reloadLastUnfinished.setAccelerator(reloadUnfinished);
    reloadLastUnfinished.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        File[] saves = new File("saves").listFiles();
        if (saves != null && saves.length > 0) {
          File latest = null;
          for (File f : saves) {
            if (latest == null) {
              latest = f;
            } else {
              if (f.lastModified() > latest.lastModified()) {
                latest = f;
              }
            }
          }
          if (latest != null) {
            activeLevel = handleLoadSave(false, false, activeLevel, latest.toString());
          }
        }
      }
    });

    JMenuItem loadSave = new JMenuItem("Load Save");
    KeyStroke load = KeyStroke.getKeyStroke(KeyEvent.VK_R, 2);
    loadSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        activeLevel = handleLoadSave(false, true, activeLevel, "");

      }
    });
    loadSave.setAccelerator(load);

    JMenuItem toggleFrameRateCap = new JMenuItem("Toggle FPS Cap (ON)");
    toggleFrameRateCap.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        capFrameRate = !capFrameRate;
        if (capFrameRate) {
          toggleFrameRateCap.setText("Toggle FPS Cap (ON)");
        } else {
          toggleFrameRateCap.setText("Toggle FPS Cap (OFF)");
        }
      }
    });

    JMenuItem wasdToggle = new JMenuItem("Toggle WASD Movement (OFF)");
    wasdToggle.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        wasd = !wasd;
        if (wasd) {
          wasdToggle.setText("Toggle WASD Movement (ON)");
        } else {
          wasdToggle.setText("Toggle WASD Movement (OFF)");
        }
      }
    });

    JMenu game = new JMenu("Game");
    game.add(pauseGame);
    game.add(resumeGame);
    game.add(reloadLastUnfinished);
    game.add(resetLevel);
    game.add(restartGame);
    game.add(loadSave);
    game.add(toggleFrameRateCap);
    game.add(wasdToggle);
    return game;
  }

  /**
   * Method to setup the replay menu and sub menus so it is easier to modify.
   *
   * @return The Replay sub-menu
   */
  private JMenu setupReplayMenu() {
    JMenuItem startReplaying = new JMenuItem("Start Replaying");
    startReplaying.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        startReplaying();
      }
    });

    JMenuItem stopReplaying = new JMenuItem("Stop Replaying");
    stopReplaying.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopReplaying();
      }
    });

    JMenuItem startRecording = new JMenuItem("Start Recording");
    startRecording.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        startRecording();
      }
    });

    JMenuItem stopRecording = new JMenuItem("Stop Recording");
    stopRecording.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopRecording();
      }
    });

    JMenuItem autoReplay = new JMenuItem("Auto");
    autoReplay.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (replaying) {
          activeReplay.toggleAutoReplay();
        }
      }
    });

    JMenuItem stepBy = new JMenuItem("Next Step");
    stepBy.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (replaying) {
          if (!activeReplay.isFinished()) {
            activeReplay.nextStep();
            evalTime = System.currentTimeMillis();
          } else {
            stopReplaying();
          }
        }
      }
    });

    JMenuItem speedUp = new JMenuItem("Increase replay speed");
    speedUp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (replaying) {
          activeReplay.decreaseWaitTime();
        }
      }
    });

    JMenuItem slowDown = new JMenuItem("Decrease replay speed");
    slowDown.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (replaying) {
          activeReplay.increaseWaitTime();
        }
      }
    });

    JMenu replay = new JMenu("Replay");
    replay.add(startReplaying);
    replay.add(stopReplaying);
    replay.add(startRecording);
    replay.add(stopRecording);
    replay.add(autoReplay);
    replay.add(stepBy);
    replay.add(speedUp);
    replay.add(slowDown);
    return replay;
  }

  /**
   * Method to setup the Help menu.
   *
   * @return The Help Menu
   */
  private JMenuItem setupHelpMenu() {
    JMenuItem help = new JMenu("Help");
    help.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        new HelpWindow();
        if (!paused) {
          togglePaused();
        }
      }
    });
    return help;
  }

  /**
   * Method to setup the Exit menu and sub menus so it's easier to modify.
   *
   * @return The Exit menu and sub-menus
   */
  private JMenu setupExitMenu() {
    JMenuItem saveExit = new JMenuItem("Save and Exit");
    saveExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handleLoadSave(true, false, activeLevel, "");
        confirmExit();
      }
    });
    KeyStroke save = KeyStroke.getKeyStroke(KeyEvent.VK_S, 2);
    saveExit.setAccelerator(save);

    JMenuItem exitNoSave = new JMenuItem("Exit Without Saving");
    exitNoSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        confirmExit();
      }
    });
    KeyStroke noSave = KeyStroke.getKeyStroke(KeyEvent.VK_X, 2);
    exitNoSave.setAccelerator(noSave);

    JMenu exit = new JMenu("Exit");
    exit.add(saveExit);
    exit.add(exitNoSave);
    return exit;
  }

  /**
   * A method to handle loading and saving in one place.
   * 
   * @param saving   - Whether or not the caller is saving.
   * @param choosing - Whether or not the caller is choosing where to load / save
   *                 from / to.
   * @param loadSave - The board to load to or save from.
   * @param location - If the caller is not choosing, where to load / save from /
   *                 to.
   */
  private Board handleLoadSave(boolean saving, boolean choosing, Board loadSave, String location) {
    LevelManager l = new LevelManager();
    if (recording) {
      stopRecording();
    }
    if (saving) {
      l.saveGame(loadSave);
    } else {
      Board tempBoard = null;
      if (choosing) {
        tempBoard = l.chooseFileAndLoad();
      } else {
        File toLoad = new File(location);
        tempBoard = l.loadGame(toLoad);
      }
      if (tempBoard != null) {
        loadSave = tempBoard;
      }
    }
    return loadSave;
  }

  /**
   * Creates a dialog window for confirming the user wants to exit the program.
   */
  private void confirmExit() {
    // Setup window
    JDialog exit = new JDialog(this, "Confirm Quit");
    exit.setPreferredSize(new Dimension(200, 100));
    exit.setLocationRelativeTo(this);
    exit.setLayout(new FlowLayout(FlowLayout.CENTER));

    // Adding Text
    JLabel confirmExit = new JLabel("Are you sure you want to quit?", JLabel.CENTER);
    if (!isWindows()) {
      confirmExit.setFont(new Font("", Font.PLAIN, 12));
    }

    confirmExit.setForeground(Color.WHITE);

    // Handle Buttons
    JButton confirm = new JButton("Exit");
    confirm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    confirm.setBackground(Color.BLACK);
    confirm.setForeground(Color.WHITE);

    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        exit.dispose();
      }
    });
    cancel.setBackground(Color.BLACK);
    cancel.setForeground(Color.WHITE);

    // Finish window
    exit.add(confirmExit, BorderLayout.NORTH);
    exit.add(confirm, BorderLayout.SOUTH);
    exit.add(cancel, BorderLayout.SOUTH);
    exit.pack();
    exit.getContentPane().setBackground(Color.BLACK);
    exit.setVisible(true);
  }

  /**
   * Creates the side window for viewing information about the level.
   *
   * @return The Level Info panel.
   */
  @SuppressWarnings("rawtypes")
  private JPanel setupLevelInfo() {
    JPanel info = new JPanel();
    info.setLayout(new GridLayout(4, 0));

    int textHeight = (maxObjHeight / 26);

    Color panelColor = Color.BLACK;
    Color textColor = Color.WHITE;

    infoPanels = new HashMap<Character, InfoPanel>();

    // Creating the info panels
    InfoPanel<Integer> levelNum = new InfoPanel<Integer>(99, "Level No.:", textHeight, panelColor,
        textColor, textColor);
    InfoPanel<Integer> treasureLeft = new InfoPanel<Integer>(99, "Treasure Left:", textHeight,
        panelColor, textColor, textColor);
    InfoPanel<Integer> timeLeft = new InfoPanel<Integer>(99, "Time Left:", textHeight, panelColor,
        textColor, textColor);
    InfoPanel<List<Keys>> inventory = new InfoPanel<List<Keys>>(activeLevel.getKeys(), "Inventory:",
        textHeight, panelColor, textColor, textColor);

    infoPanels.put('n', levelNum);
    infoPanels.put('l', treasureLeft);
    infoPanels.put('t', timeLeft);
    infoPanels.put('i', inventory);

    // Finalising the window
    info.add(levelNum);
    info.add(treasureLeft);
    info.add(timeLeft);
    info.add(inventory);
    info.setBackground(Color.GRAY);
    info.setVisible(true);
    return info;
  }

  /**
   * Updates all level info.
   */
  @SuppressWarnings("unchecked")
  private void updateInfo() {
    if (activeLevel != null) {
      infoPanels.get('n').update(activeLevel.getLevel());
      infoPanels.get('l').update(activeLevel.getTreasureCount());
      infoPanels.get('t').update(activeLevel.getTime());
      infoPanels.get('i').update(activeLevel.getKeys());
    }
  }

  /**
   * Toggles whether or not the game is paused.
   */
  private void togglePaused() {
    offset = System.currentTimeMillis() - evalTime;
    paused = !paused;
  }

  /**
   * Redraws all elements of the GUI.
   */
  private void redraw() {
    drawBoard.removeAll();
    GridBagConstraints g = new GridBagConstraints();
    g.weightx = 1;
    g.weighty = 1;
    g.fill = GridBagConstraints.BOTH;
    if (paused && !replaying) {
      JLabel paused = new JLabel("Paused", JLabel.CENTER);
      paused.setPreferredSize(drawBoard.getSize());
      paused.setForeground(Color.WHITE);
      paused.setBackground(Color.BLACK);
      drawBoard.add(paused, g);
    } else {
      boardView.update(activeLevel);
      if (drawBoard.getHeight() > drawBoard.getWidth()) {
        g.fill = GridBagConstraints.HORIZONTAL;
      } else {
        g.fill = GridBagConstraints.VERTICAL;
      }
      g.anchor = GridBagConstraints.WEST;
      drawBoard.add(boardView, g);
      boardView.rescale();
    }
    updateInfo();
    invalidate();
    revalidate();
    repaint();
    doneFrame = true;
  }

  /**
   * Whether or not the GUI is ready to draw another frame.
   * 
   * @return - Whether or not the GUI has finished the current frame.
   */
  public boolean doneFrame() {
    return doneFrame;
  }

  /**
   * Sets up the keys for key handling.
   * 
   * @return - The key listener to be used by the program.
   */
  private KeyListener setupKeys() {
    return new KeyListener() {
      /**
       * Keys used outside of shortcuts.
       */
      public void keyPressed(KeyEvent e) {
        if (!paused && !replaying) {
          direction moveDir = null;
          // Up Key
          if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W && wasd) {
            moveDir = direction.UP;
          }
          // Down Key
          if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S && wasd) {
            moveDir = direction.DOWN;
          }
          // Left Key
          if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A && wasd) {
            moveDir = direction.LEFT;
          }

          // Right Key
          if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D && wasd) {
            moveDir = direction.RIGHT;
          }

          if (moveDir != null) {
            activeLevel.Move(moveDir);
            if (recording) {
              activeRecord.addAnAction(moveDir);
            }
          }
        }
      }

      /**
       * These are two overflow input methods.
       */
      public void keyReleased(KeyEvent e) {

      }

      public void keyTyped(KeyEvent e) {

      }

    };
  }

  /**
   * Sets up variables for UI scaling.
   */
  private void setScaler() {
    if (isWindows()) {
      maxObjHeight = (int) (getPreferredSize().getHeight() - 62);
    } else {
      maxObjHeight = (int) (getPreferredSize().getHeight() - 22);
    }
  }

  /**
   * Starts recording the game.
   */
  private void startRecording() {
    if (!recording && !replaying) {
      recording = true;
      activeRecord = new RecordGame(activeLevel);
    }
  }

  /**
   * Starts replaying the game and stops the active recording.
   */
  private void startReplaying() {
    if (!replaying) {
      if (recording) {
        stopRecording();
      }
      oldBoard = activeLevel;
      ReplayGame replay = new ReplayGame();
      replay.loadGameSave();
      activeReplay = replay;
      activeLevel = replay.getBoard();
      replaying = true;
    }
  }

  /**
   * Stops replaying the game.
   */
  private void stopReplaying() {
    if (replaying) {
      activeLevel = oldBoard;
      replaying = false;
    }
  }

  /**
   * Stops recording the game.
   */
  private void stopRecording() {
    if (recording) {
      recording = false;
      activeRecord.finalise();
    }
  }

  /**
   * Creates JDialog when the player runs out of time.
   */
  private void runOutOfTime() {
    JDialog timeOut = new JDialog(this, "Time Out");
    timeOut.setPreferredSize(new Dimension(200, 100));
    timeOut.setLocationRelativeTo(this);
    timeOut.setLayout(new FlowLayout(FlowLayout.CENTER));
    timeOut.getContentPane().setBackground(Color.BLACK);

    // Adding Text
    JLabel timeLeft = new JLabel("You ran out of time", JLabel.CENTER);
    timeLeft.setForeground(Color.WHITE);
    timeLeft.setBackground(Color.BLACK);

    // Finish window
    timeOut.add(timeLeft, BorderLayout.NORTH);
    timeOut.pack();
    timeOut.setVisible(true);
  }

  /**
   * The game clock.
   */
  public void tick() {
    doneFrame = false;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (!paused && !replaying) {
          if (System.currentTimeMillis() - evalTime >= 1000) {
            activeLevel.tick();
            if (recording && activeLevel.getLevel() == 2) { // Patch to make it work, temporary
              activeRecord.addAnImposterAction(Board.direction.valueOf(activeLevel.getImpMove()));
            }
            if (activeLevel.getTime() > 0) {
              activeLevel.setTime(activeLevel.getTime() - 1);
            } else {
              if (recording) {
                stopRecording();
              }
              activeLevel.resetLevel();
              runOutOfTime();
              togglePaused();
            }
            evalTime = System.currentTimeMillis();
          }
          if (!activeLevel.getLevel1Status() && activeLevel.getLevel() == 1) {
            if (recording) {
              stopRecording();
            }
            evalTime = System.currentTimeMillis();
            activeLevel = boards[1];
            togglePaused();
            activeLevel.resetLevel();
            boardView.update(activeLevel);
          } else if (!activeLevel.getLevel1Status()) {
            loadLevels();
            // Used to stopgap an issue after completing level 2
            activeLevel = boards[0];
            activeLevel.resetLevel();
            togglePaused();
          }
        } else if (replaying && activeReplay != null && activeReplay.isAutoReplay()) {
          if (System.currentTimeMillis() - evalTime > activeReplay.getWaitTime()) {
            activeReplay.nextStep();
            evalTime = System.currentTimeMillis();
          }
        } else {
          evalTime = System.currentTimeMillis() - offset;
        }
        redraw();
      }
    });
  }

  /**
   * How long the system should wait between frames.
   * 
   * @return The time between frames.
   */
  public long timeToSleep() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    int refreshRate = ge.getDefaultScreenDevice().getDisplayMode().getRefreshRate();
    if (!paused) {
      if (capFrameRate) {
        return (1000 / refreshRate);
      }
      if (isWindows()) { // Returning 0 on linux causes system hanging
        return 0;
      }
      return 1;
    }
    return (10000 / refreshRate);
  }

}
