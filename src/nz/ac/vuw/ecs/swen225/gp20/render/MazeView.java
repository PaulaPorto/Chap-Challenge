package nz.ac.vuw.ecs.swen225.gp20.render;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board;
import nz.ac.vuw.ecs.swen225.gp20.maze.Imposter;
import nz.ac.vuw.ecs.swen225.gp20.maze.Tile;


/**
 * Class represents the rendering of the 2D maze while the player moves by 
 * reading the board and passing it through to the GUI.
 * The sounds used for picking up treasure and opening doors are from Youtube from among us game:
 * The rest of the sounds were completely original and made by us.
 * Images were inspired by among us but were drawn by us using Inkscape 
 * 
 *  @author Sanjeeta Singh 300400467 and Nick Mazey 300473270
 */

//Link - https://www.youtube.com/watch?v=lnfhXhLXmns&ab_channel=DroopyBear

public class MazeView extends JPanel { 
  //Default serial version
  private static final long serialVersionUID = 1L;

  //HashMap to store all the images as objects to speed up loading
  private HashMap<Character, ImageIcon> images;

  //A test frame to run the mazeView class without affecting the GUI
  private JFrame frame = new JFrame("GUI TEST");

  //The file path for the images 
  private String fileLocation = "";

  //The scaling of the board
  private int scaling = 150;
  
  //Storing the old images before they get resized
  private int oldImage;
  
  //The starting point of the first tile for the row
  int firstRow;
  
  //The starting point of the first time for the column
  int firstCol;


  /**
   * The constructor that sets size of the board and 
   * fixes the grid for the board. Calls methods 
   * from the board class and call the method in MazeView.
   * @param b - method takes in a board to read to print it out
   */
  public MazeView(Board b) { 
    //makes the board into a 5x5 grid for zoom up rendering
    setLayout(new GridLayout(5, 5));
    b.loadChap();
    setup();
    update(b);
    addComponentListener(new ComponentListener() {
      @Override
      public void componentResized(ComponentEvent e) {
        //calls the re-scale method
        rescale();
      }

      @Override
      public void componentMoved(ComponentEvent e) {
        //Default method
      }

      @Override
      public void componentShown(ComponentEvent e) { 
      //Default method
      }
   
      @Override
      public void componentHidden(ComponentEvent e) { 
      //Default method
      }    
    });
  }
  /**
   * Updates the view with a new board by drawing the board as the player moves.
   * This method also places a tile under each objects on 
   * the board such as treasure, key etc to make it cleaner. 
   * The images changing according to the players movement are set in this method.
   *  @param b - Passing through the board 
   */
  
  public void update(Board b) {
    if (b != null) {
      removeAll();
      //Find the players current position x and y
      Tile pos = b.findCurrentPos();
      //if chap is dead then set the positions
      if (pos == null) {
        pos = Imposter.findCurrentImp(b.getBaseBoard());
      }
      firstRow = pos.getRow() + 2;
      firstCol = pos.getCol() - 2;
      //Boundary check so board doesn't render out of bounds when chap moves
      if (firstRow - 4 < 0) {
        firstRow = 4;
      }
      for (int i = firstRow; i >= firstRow - 4; i--) {
        for (int j = firstCol; j <= firstCol + 4; j++) {    
          char type = b.getBaseBoard()[i][j].getChar();     
          JLabel item = new JLabel();
          item.setIcon(images.get(b.getBaseBoard()[i][j].getChar()));
          //A tile is added to the images to make it look cleaner
          if (b.getBaseBoard()[i][j].getChar().equals('C') 
              || b.getBaseBoard()[i][j].getChar().equals('T')
              || b.getBaseBoard()[i][j].getChar().equals('Y') 
              || b.getBaseBoard()[i][j].getChar().equals('B')
              || b.getBaseBoard()[i][j].getChar().equals('R') 
              || b.getBaseBoard()[i][j].getChar().equals('G')
              || b.getBaseBoard()[i][j].getChar().equals('O') 
              || b.getBaseBoard()[i][j].getChar().equals('X')) {
            item = new JLabel(images.get('F')) {
              public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (type == 'C') {
                    //Calling the method from the board to get the direction of the player
                    char directions = b.getDir();  
                    //According the directions the image is drawn 
                    if (directions == 'U' || directions ==  'S' 
                        ||  directions == 'L' || directions == 'D') {
                        g.drawImage(images.get(directions).getImage(), 0, 0, null);
                    } else {
                    //If there is no directions made the up image is drawn
                    g.drawImage(images.get('U').getImage(), 0, 0, null);
                    }
                    //To get the imposter to move according to the way it needs to move
                    } else if (type == 'X') { 
                    //Getting the direction of the imposter from the imposter class
                    char dirI = Imposter.getDirI(); 
                    //According to the symbol the correct image is drawn
                    if (dirI == '*' || dirI == '/' 
                        || dirI == '^' || dirI == '-') {
                        g.drawImage(images.get(dirI).getImage(), 0, 0, null);
                      } else {
                        //If its none of the above then draw the default which is the front view
                        g.drawImage(images.get('-').getImage(), 0, 0, null);
                    } 
                    } else {
                      //if its any char another then draw it according to the char 
                      g.drawImage(images.get(type).getImage(), 0, 0, null);
                  }
                }
              };
          }
          //adding the JLabel to the JPanel 
          this.add(item);
        }
      }
    }
  }

  /**
   * This method does the math for the rescaling of the 
   * board when the window size increases and/or decreases.
   * When the window size decreases then the it is replaced with the 
   * un-scaled image to stop the images from getting pixelated. 
   */
  public void rescale() {
    if (scaling != getParent().getWidth() / 5 
        && getParent().getWidth() < getParent().getHeight() 
        || scaling != getParent().getHeight() / 5 
        && getParent().getHeight() < getParent().getWidth()) {
      if (getParent().getSize().getWidth() < getParent().getSize().getHeight()) {
        scaling = getParent().getWidth() / 5;
      } else {
        scaling = getParent().getHeight() / 5;
      }
      
      //If it increases to scale set to the original image to avoid resolution issues
      if (oldImage < scaling) {
        setup();
      }
      
      //The resizing of the images
      for (ImageIcon i : images.values()) {
        i.setImage(i.getImage().getScaledInstance(scaling, scaling, java.awt.Image.SCALE_FAST));
      }
      
      //Setting the image to the scaled image when window size increases 
      oldImage = scaling;
    }
  }
  
  
  /**
  * Method loads the file so sound effects can be inserted this 
  * method is called in the many methods in the game whenever 
  * sound needs to be inserted. eg. when picking up keys, opening locks etc.
  * @param soundFilePath - the sound file that is chosen
  */
  public static synchronized void playSound(String soundFilePath) {
    new Thread(new Runnable() {
      public void run() {
        try {
          Clip clip = AudioSystem.getClip();
          AudioInputStream sounds = AudioSystem.getAudioInputStream(new File(soundFilePath));
          clip.open(sounds);
          clip.start();
        } catch (Exception e) {
          System.out.println("Error No File");
        }
      }
      
    }).start();
  }
  

  /**
   * Pre-loads all images to a hashmap to load the images on 
   * the labels faster. According to the letters passed through 
   * from the board an image is assigned to it and passed 
   * to the update method to draw the images.
   */
  public void setup() {
    images = new HashMap<Character, ImageIcon>();
    //The available chars 
    char [] availableChars = {'F', 'W', 'T', 'b', 'B', 'y', 'Y', 
                              'r', 'R', 'g', 'G', 'E', 'O', 'I', 
                              'C', 'U', 'L', 'D', 'S', 'X', 'Q', 
                              '*', '^', '-', '/' };
    BufferedImage image;
    for (int i = 0; i < availableChars.length; i++) {
      switch (availableChars[i]) {
        case 'F':
          //Floor
          fileLocation = "assets/tile.png";
          break;
        case 'W':
          //Wall
          fileLocation = "assets/wall.png";
          break;
        case 'T':
          //Treasure
          fileLocation = "assets/treasure.png";
          break;
        case 'b':
          //Blue lock
          fileLocation = "assets/blue_lock.png";
          break;
        case 'B':
          //Blue key
          fileLocation = "assets/blue_key.png";
          break;
        case 'y':
          //Yellow lock
          fileLocation = "assets/yellow_lock.png";
          break;
        case 'Y':
          //Yellow Key
          fileLocation = "assets/yellow_key.png";
          break;
        case 'r':
          //Red lock
          fileLocation = "assets/red_lock.png";
          break;
        case 'R':
          //Red key
          fileLocation = "assets/red_key.png";
          break;
        case 'g':
          //Green lock
          fileLocation = "assets/green_lock.png";
          break;
        case 'G':
          //Green key
          fileLocation = "assets/green_key.png";
          break;
        case 'E':
          //Exit
          fileLocation = "assets/exit_door.png";
          break;
        case 'O':
          //Final lock
          fileLocation = "assets/final_door.png";
          break;
        case 'I':
          //Information
          fileLocation = "assets/information_tile.png";
          break;
        case 'C':
          //Chap the player (default image when reading the board)
          fileLocation = "assets/right_view.png";
          break;
        case 'U':
          //Chap the player going up
          fileLocation = "assets/back_view.png";
          break;
        case 'L':
          //Chap the player going left
          fileLocation = "assets/left_view.png";
          break;
        case 'S':
          //Chap the player going right
          fileLocation = "assets/right_view.png";
          break;
        case 'D':
          //Chap the player going down
          fileLocation = "assets/front_view.png";
          break;
        case 'X':
          //The enemy on the board (default image when reading the board)
          fileLocation = "assets/front_view_imposter.png";
          break;
        case '*':
          //The left view of enemy on the board 
          fileLocation = "assets/left_view_imposter.png";
          break;
        case '^':
          //The right view of enemy on the board 
          fileLocation = "assets/right_view_imposter.png";
          break;
        case '/':
          //The back view of enemy on the board 
          fileLocation = "assets/back_view_imposter.png";
          break;
        case '-':
          //The front view of enemy on the board 
          fileLocation = "assets/front_view_imposter.png";
          break;
        case 'Q':
          //The trap tile 
          fileLocation = "assets/trap.png";
          break;
        default:
          fileLocation = "assets/tile.png";
          break;
      }
      ImageIO.setUseCache(true);
      try {
        image = ImageIO.read(new File(fileLocation));
        Image resize = image.getScaledInstance(scaling, scaling, java.awt.Image.SCALE_SMOOTH); 
        images.put(availableChars[i], new ImageIcon(resize));
        oldImage = 150;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Method to make a basic window to test the images layout.
   */
  
  public void guitest() {
    frame.setVisible(true);
    frame.setSize(450, 450);
    frame.setLayout(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(this);
    this.setBounds(0, 0, 450, 450);
  }

  /**
   * A main to test the image layout before making changes to the GUI.
   * @param arguments - the arguments being past.
   * @throws LineUnavailableException - throws an error if line doesn't exist.
   * @throws IOException -  throws errors.
   * @throws UnsupportedAudioFileException - Throws error when file isn't supported.
  */
  
  public static void main(String[] arguments) throws UnsupportedAudioFileException, 
                                              IOException, LineUnavailableException {
    Board b = new Board();
    b.loadBoard();
    MazeView m = new MazeView(b);
    m.guitest();
  }
}