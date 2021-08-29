package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelManager;
import nz.ac.vuw.ecs.swen225.gp20.render.MazeView;
/**
 * Class represents board for the game
 * And most logic for the game to work. 
 * @author Paula
 *
 */

public class Board implements Cloneable {

  //------------------------
  // MEMBER VARIABLES 
  //------------------------ 

  private Tile[][] baseBoard; //initiate array of Tiles.

  private static int rows; //number of rows for the board.
  private static int cols; //number of cols for the board.
  String boardS = "";  //board String
  int level; //to keep track of what level is being played.
  boolean level2 = false;  //used to load the correct level in loadBoard. 
  int treasureCount; //keeps track of how many treasures have been collected.
  int time = 1; //created to keep track of the time it takes to run the game.
  int storedTime; //Stored time in game.
  Tile nextMove; //Tile that the player wants to move to.
  private char dir; //direction of chap.
  Boolean gameOver = false; 
  Boolean level1Status = true; //tells us if level one is finished or not.
  Boolean trapCase = false; //tells us if chap fell into a trap
  String message = ""; //Message according to how player died.
  String infoPanel = ""; // Info message according to what level is being played.
  String moveImpString = ""; //String used to intake the imposter's movements.
  Preconditions precon; //precondition used for checking treasure count.

  /**
   * Variable for the board itself. This will help
   * stringbuilder read the board into the game.
   * drawB = level 1.
   * drawB2 = level 2.
   */
  String drawB =
      "FFFFFFFFFFFFFFF"
         + "FFFWWWWFWWWWFFF"
         + "FFFWFFWWWFFWFFF"
         + "FFFWTFWEWFTWFFF"
         + "FWWWWrWOWrWWWWF"
         + "FWFBgFFFFFyBFWF"
         + "FWFFWGFIFYWFFWF"
         + "FWWWWTFCFTWWWWF"
         + "FWFTWGFFFYWTFWF"
         + "FWFFgFFFFFyFFWF"
         + "FWWWWWbWbWWWWWF"
         + "FFFWRTFWFTRWFFF"
         + "FFFWFFFWFFFWFFF"
         + "FFFWWWWWWWWWFFF"
         + "FFFFFFFFFFFFFFF";


  String drawB2 =
      "FFFFFFFFFFFFFFFFFFF"
          + "FFFFWWWWWWWWWWWFFFF"
          + "FFFFWWFFFTQFFWWFFFF"
          + "FFFFWWQFFRFFFWWFFFF"
          + "FWWWWWWWWgWWWWWWWWF"
          + "FWFTFWFFFFFFFWFFFWF"
          + "FWFBFrFFFXFFFWTQGWF"
          + "FWQFFWYFFFFFTWFFFWF"
          + "FWWWWWFFFFFFFWWbWWF"
          + "FFFFFWFFFIFFFWWFWWF"
          + "FWWWWWFFFCFFFWFFTWF"
          + "FWFFFbFFFFFFFyFFFWF"
          + "FWFGFWTFFFQFBWQFFWF"
          + "FWFFFWFFFFFFFWWWWWF"
          + "FWWFWWWgWWFFFWFEFWF"
          + "FWWrWWFFRWWWWWFQFWF"
          + "FWFTYWTFFyFFFOFFFWF"
          + "FWWWWWWWWWWWWWWWWWF"
          + "FFFFFFFFFFFFFFFFFFF";
  /**
   * meaning of each individual char from the board.
   */
  char wall = 'W';
  char floor = 'F';
  char greenK = 'G';
  char redK = 'R';
  char blueK = 'B';
  char yellowK = 'Y';

  char glock = 'g';
  char rlock = 'r';
  char block = 'b';
  char ylock = 'y';

  char info = 'I';
  char treasure = 'T';
  char exitL = 'O';
  char exit = 'E';
  char chap = 'C';

  char imposter = 'X';
  char trap = 'Q';


  /**
   *Created every key from the board according to its color.
   */
  Keys gk1 = new Keys("green");
  Keys gk2 = new Keys("green");
  Keys rk1 = new Keys("red");
  Keys rk2 = new Keys("red");
  Keys bk1 = new Keys("blue");
  Keys bk2 = new Keys("blue");
  Keys yk1 = new Keys("yellow");
  Keys yk2 = new Keys("yellow");

  private List<Keys> keys = new ArrayList<>(); //list of the keys player collects.
  private List<Integer> treasures = new ArrayList<>(); //list of treasures how many were collected.

  /**
   * Checks what level is being played and passes in the correct 
   * String board and the value for row and columns.
   */
  public void setLevel() {
    if (level2 == false) { //level one is being played.
      //rows = 15;
      //cols = 15;
      setRows(15);
      setCols(15);
      this.baseBoard = new Tile[rows][cols];
      storedTime = 60;
      //b = drawB;
    }
    if (level2 == true) { //level two is being played.
      //rows = 19;
      //cols = 19;
      setRows(19);
      setCols(19);
      this.baseBoard = new Tile[rows][cols];
      storedTime = 120;
      //b = drawB2;
    }
  }
  
  /**
   * Creates the board according to the 2d array.
   */
  public Board() {
    setLevel(); 
  }

  /**
   * Loads the board. Reads through the array setting each tile to the equivalent char.
   */
  public void loadBoard() {
    setLevel();
    if (level2 == false) {
      boardS = drawB;
    } else {
      boardS = drawB2; 
    }

    BufferedReader buffy = new BufferedReader(new StringReader(boardS));
    for (int row = rows - 1; row >  - 1; row--) {
      //System.out.println("rowb = "+row );
      for (int col = 0; col < cols; col++) {
        //System.out.println("colb = "+col);
        //System.out.println("rows cols = "+rows +cols);
        //System.out.println("board " + row + col);
        try { 
          this.baseBoard[row][col] = new Tile((char) buffy.read(), row, col); 
        } catch (IOException e) {
          e.printStackTrace(); 
        }
      }
    }
    if (level2 == true) {
      loadImposter();
      //tick();
    }
    loadChap();
  }

  /**
   * Resets the level and clears the list of treasures and keys.
   */
  public void resetLevel() {
    if (time == 0) {
      MazeView.playSound("sounds/Death.wav");
    }
    setLevel();
    if (level == 1) {
      loadBoard();
    } else {
      level2 = true; 
      restart();
    }
    keys.clear();
    loadBoard();
    treasures.clear();
    time = storedTime;
    gameOver = true;
    level1Status = true;
  }

  /**
   * Restarts the level for level 2.
   */
  public void restart() {
    //MazeView.playSound("sounds/Death.wav");
    keys.clear();
    treasures.clear();
    gameOver = true;
    level2 = true;
    if (Imposter.killChap == true) { 
      message = "Awn. You got killed :( "
        + "Try again and look out for the Imposter...";
    }
    if (Imposter.killChap == true || trapCase == true) {
      MazeView.playSound("sounds/Death.wav");
      JFrame frame = new JFrame();
      JTextArea loss = new JTextArea(message);
      loss.setLineWrap(true);
      loss.setWrapStyleWord(true);
      JOptionPane.showMessageDialog(frame, loss); 
    }
    loadBoard();
    loadImposter();
    time = storedTime;
    Imposter.killChap = false;
    trapCase = false;
  }

  /**
   * Adds each key that the player caught to the list. 
   * Distinguishes if its the first or second key of the specific color
   * so that the player can only open a second door if they have acquired
   * the second key of that color.
   * @param k char of the key that was collected
   */
  public void addKey(char k) {
    if (k == 'G') {
      if (!keys.contains(gk1)) {
        keys.add(gk1);
      } else {
        keys.add(gk2);
      }
    }
    if (k == 'B') {
      if (!keys.contains(bk1)) {
        keys.add(bk1);
      } else {
        keys.add(bk2);
      }
    }
    if (k == 'R') {
      if (!keys.contains(rk1)) {
        keys.add(rk1);
      } else {
        keys.add(rk2);
      }
    }
    if (k == 'Y') {
      if (!keys.contains(yk1)) {
        keys.add(yk1);
      } else {
        keys.add(yk2);
      }
      
    }

  }

  /**
   * Adds a count of one to the list "treasures" every time a treasure is caught
   * by the player. 
   */
  public void addTreasure() {
    treasures.add(1);
    setTreasureCount(9 - treasures.size());
    treasureCount = getTreasureCount() - 1;
    if  (treasures.size() > 8) { //makes sure the count is within the correct amount.
      return;
    }
    //System.out.println("treasure size" + treasures.size())
  }

  /**
   * Checks if the Tile is valid for the player to move to according to
   * the char.
   * @param tile being checked if its valid
   * @return false when the tile is invalid. True when it's valid.
   */
  public boolean isValid(Tile tile) {
    //System.out.println(tile.getChar());
    char c = tile.getChar();

    if (c == 'F' || c == 'T' || c == 'G' || c == 'B' || c == 'R' || c == 'Y') {
      return true;
    }
    if (c == 'I') {
      return true;
    }
    if (c == 'O') { //can only go here if all the treasures have been collected.
      if (treasures.size() == 8) {
        MazeView.playSound("sounds/swipe.wav");
        return true;
      }
    }
    if (c == 'E') {
      endLevel();
      //level2 = true;
      return true;
    }
    if (c == 'Q') {
      //resetLevel();
      return true;
    }
    if (c == 'X') {
      return true;
    }

    //only valid when it has the right key color. 
    //Checks that the first key has been used so it needs the other key to open the other door.
    if (c == 'g') { 
      if (keys.contains(gk1)) {
        keys.remove(gk1);
        MazeView.playSound("sounds/swipe.wav");
        return true;
      }
    }
    if (c == 'r') {
      if (keys.contains(rk1)) {
        keys.remove(rk1);
        MazeView.playSound("sounds/swipe.wav");
        return true;
      }
    }
    if (c == 'b') {
      if (keys.contains(bk1)) {
        keys.remove(bk1);
        MazeView.playSound("sounds/swipe.wav");
        return true;
      }
    }
    if (c == 'y') {
      if (keys.contains(yk1)) {
        keys.remove(yk1);
        MazeView.playSound("sounds/swipe.wav");
        return true;
      }
    }
    return false; //the Tile is invalid.
  }


  /**
   * Moves chap according to the direction pressed by the player.
   * @param move direction chap is going to
   */
  public void Move(direction move) {
    if (!Imposter.killChap) {
      Tile currentTile = findCurrentPos(); //finds the tile where chap is.
      Tile newPos = null;
      int currentRow = currentTile.getRow();
      int currentCol = currentTile.getCol();
      int dx = 0;
      int dy = 0;

      switch (move) {
        case UP: 
          setDir('U');
          //System.out.println("Direction:" + getDir());
          dy = 1;
          break;

        case DOWN:
          setDir('D');
          //System.out.println("Direction:" + getDir());
          dy = -1;
          break;

        case LEFT: 
          setDir('L');
          //System.out.println("Direction:" + getDir());
          dx = -1;
          break;

        case RIGHT: 
          setDir('S');
          //System.out.println("Direction:" + getDir());
          dx = +1;
          break;

        default: break;
      }

      if (currentCol > 0 && currentCol < baseBoard.length && currentRow > 0 
          && currentRow < baseBoard.length) {
        newPos = baseBoard[currentRow + dy][currentCol + dx];
        setNextLoc(newPos);
        if (isValid(newPos) == true) { //if the new position is valid...
          if (newPos.getChar() == 'I') { //makes sure the info tile remains the same.
            moveC(currentTile, newPos);  
            currentTile = newPos; 
            if (level == 1) {
              infoPanel = "Collect all treasure items by unlocking the doors with the "
                  + "colored keys, and enter the final lock to win";
            }
            if (level == 2) {
              infoPanel = "Look out for traps and the Imposter! He's trying to kill you. "
                  + "If there is no way out, restart the level :)";
            }
            getInfo();

          }
          if (newPos.getChar() == 'Q') { 
            moveC(currentTile, newPos); 
            currentTile = newPos; 
            //resetLevel();
            //MazeView.playSound("sounds/Death.wav");
            message = "OOPS! You fell into a trap, try again.";
            trapCase = true;
            restart();
          }
          if (newPos.getChar() == 'X') { 
            moveC(currentTile, newPos); 
            currentTile = newPos; 
            message = "Awn. You got killed :( Try again and look out for the Imposter...";
            Imposter.killChap = true;
            restart();
          } else {
            moveC(currentTile, newPos); //move chap from old pos to the new pos according to dir.
            currentTile = newPos; 
          }
        }
      }
    }
  }

  /**
   * Changes Chap position from the old tile to the new tile.
   * @param oldTile where chap is
   * @param newTile where chap is moving to
   */
  public void moveC(Tile oldTile, Tile newTile) {
    final Chap chapp = oldTile.getChap();
    char t = newTile.getChar();
    oldTile.setChap(null);
    if (t == 'G' || t == 'R' || t == 'B' || t == 'Y') { //if its a key, add to the key list.
      addKey(t);
      MazeView.playSound("sounds/pickingKey.wav");
    }
    if (t == 'T') { //if its a treasure, add to the treasures list.
      addTreasure();
      Preconditions precon = new Preconditions(treasures.size());
      this.precon = precon;
      MazeView.playSound("sounds/pickUp.wav");
    }
    if (level == 1) {
      if (oldTile.getRow() == 8 && oldTile.getCol() == 7) { //if its the info tile, display info.
        oldTile.setChar('I');
      } else {
        oldTile.setChar('F');
      } //set old position to a floor tile.
    }
    if (level == 2) {
      if (oldTile.getRow() == 9 && oldTile.getCol() == 9) { //if its the info tile, display info.
        oldTile.setChar('I');
      } else {
        oldTile.setChar('F');
      } //set old position to a floor tile.

    }

    newTile.setChap(chapp);
    newTile.setChar('C');
  }

  /**
   * Identifies what direction should be called on (to move chap) 
   * according to the keyEvent (arrow keys).
   * @param e KeyEvent being called to move
   */
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      Move(direction.RIGHT);
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      Move(direction.DOWN);
    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      Move(direction.LEFT);
    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
      Move(direction.UP);
    }
  }

  /**
   * Enums. Directions used to move Chap.
   */
  public enum direction {
    UP, DOWN, LEFT, RIGHT
  }

  /**
   * Loads Chap object and sets his initial direction to facing up. 
   */
  public void loadChap() {
    Tile c = findCurrentPos();
    Chap chap = new Chap();
    chap.setPosition(c.getRow(), c.getCol());
    c.setChap(chap);
    setDir('U');
  }

  /**
   * Loads Imposter object for level 2. 
   */
  public void loadImposter() {
    Imposter imp;
    if (Imposter.findCurrentImp(baseBoard).getImp() != null) {
      imp = Imposter.findCurrentImp(baseBoard).getImp();
    } else {
      imp = new Imposter();
    }
    Tile t = baseBoard[12][9];
    //Tile t = Imposter.findCurrentImp(baseBoard);
    imp.setPos(t.getRow(), t.getCol());  
    t.setImp(imp);
    imp.setDirI('U');
    //moveImp(imp);
    //imp.moveImp(imp);
  }

  /**
   * Moves the imposter from one tile to another. 
   * @param  move String that the imposter is going to do
   * @param i Imposter being moved
   */
  public void moveImp(String move, Imposter i) {
    moveImpString = move;
    i.move(baseBoard, move);
  }

  /**
   * Tick method to move the imposter in a different direction every second. 
   */
  public void tick() {
    if (level == 2) {
      if (findCurrentPos() == null) {
        restart();
      }
      if (Imposter.findCurrentImp(baseBoard) != null 
          && Imposter.findCurrentImp(baseBoard).getImp() != null 
          && Imposter.getKillStatus() == false) {
        String[] random = {"UP", "DOWN", "LEFT", "RIGHT"};
        int pos = (int) (Math.random() * 4);
        //System.out.println(pos);
        String move = random[pos];
        //System.out.println(move);
        moveImp(move, Imposter.findCurrentImp(baseBoard).getImp());
      }
    }

  }


  /**
   * Displays a help message when the player goes over the question mark Tile.
   */
  public void getInfo() {
    JFrame frame = new JFrame();
    JTextArea info = new JTextArea(infoPanel);
    info.setLineWrap(true);
    info.setWrapStyleWord(true);
    JOptionPane.showMessageDialog(frame, info); 
  }

  /**
   * Displays a message when the player reaches the exit Tile.
   */
  public void endLevel() {
    MazeView.playSound("sounds/gameWon.wav");
    gameOver = true;
    if (level == 2) {
      JDialog gameover = new JDialog();
      JLabel g = new JLabel("YOU WON. GOOD JOB :)");
      gameover.setLayout(new FlowLayout());
      gameover.setSize(200, 100); 
      gameover.add(g);
      gameover.setLocationRelativeTo(null);
      gameover.setVisible(true);
    }
    level1Status = false;
  }

  /**
   * Method used to move imposter in recnplay.
   * @param dir direction imposter is moving
   */
  public void applyImpMove(direction dir) {
    switch (dir) {
      case UP: 
        moveImpString = "UP";
        setImpMove(moveImpString);
        break;

      case DOWN:
        moveImpString = "DOWN";
        setImpMove(moveImpString);
        break;

      case LEFT: 
        moveImpString = "LEFT";
        setImpMove(moveImpString);
        break;

      case RIGHT: 
        moveImpString = "RIGHT";
        setImpMove(moveImpString);
        break;

      default: break;
    }

    Board b = new Board();
    Tile[][] baseB = b.getBaseBoard();
    Imposter imp = Imposter.findCurrentImp(baseB).getImp();
    imp.move(baseB, moveImpString);
  }
  
  /**
   * Method that clones the entire board for recnplay.
   */
  public Board clone() {
    Board cloned = new Board();
    cloned.setCols(cols);
    cloned.setRows(rows);
    cloned.setStringB(boardS);
    cloned.setLevelNum(level);
    cloned.setLevel2(level2);
    cloned.setTreasureCount(treasureCount);
    cloned.setTime(time); 
    cloned.setStoredTime(storedTime);
    if (nextMove != null) {
      cloned.setNextMove(nextMove.clone()); 
    }
    cloned.setDir(dir);
    cloned.setGoStatus(gameOver); 
    cloned.setLevel1Status(level1Status);
    cloned.setTrapCase(trapCase);
    cloned.setMessage(message);
    cloned.setInfoPanel(infoPanel);
    cloned.setImpMove(moveImpString);
    List<Integer> clonedTreasures = new ArrayList<Integer>();
    for (int i : treasures) {
      clonedTreasures.add(i);
    }
    cloned.setTreasures(clonedTreasures);
    
    List<Keys> clonedKeys = new ArrayList<Keys>();
    for (Keys i : keys) {
      clonedKeys.add(i.clone());
    }
    cloned.setKeys(clonedKeys);
    
    cloned.baseBoard = new Tile[baseBoard.length][baseBoard[0].length];
    for (int i = 0; i < baseBoard.length; i++) {
      for (int j = 0; j < baseBoard[0].length; j++) {
        cloned.baseBoard[i][j] = this.baseBoard[i][j].clone();
      }
    }
    cloned.drawB = this.drawB;
    return cloned;
  }

  /**
   * Used for testing the Board functionality. Chap's movements etc.
   */
  public void test() {
  }

  /**
   * Finds the exact Tile that the char 'C' is currently in.
   * @return Tile that contains 'C'
   */
  public Tile findCurrentPos() {
    for (int row = 0; row < baseBoard.length; row++) {
      for (int col = 0; col < baseBoard.length; col++) {
        if (baseBoard[row][col] != null) {
          if (baseBoard[row][col].getChar() == 'C') {
            //System.out.println(row+1);
            //System.out.println(col+1);
            return baseBoard[row][col];

          }
        }
      }
    }
    return null; 
  }

  /**
   * Main used for testing Board functionality.
   * @param arguments for what is being tested
   */
  public static void main(String[] arguments) {
    Board b = new Board();
    b.test();

  }

  //------------------------
  // INTERFACE
  //------------------------

  /**
   * Sets the status of level2 the boolean passed in.
   * @param level2 boolean of level 2
   */
  public void setLevel2(boolean level2) {
    this.level2 = level2;
  }


  /**
   * Gets the state level 2. 
   * @return level2 state
   */
  public boolean getLevel2State() {
    return level2;
  }
  
  /**
   * Sets the status of level2 to true.
   */
  public void setLevel2True() {
    this.level2 = true;
  }

  /**
   * Sets the board string in the field to the String passed in.
   * @param level1Status boolean of level1status
   */
  public void setLevel1Status(boolean level1Status) {
    this.level1Status = level1Status;
  }

  /**
   * Gets the state of level 1. If its true level one is still being played. If level 1 is over.
   * @return gameOver state
   */
  public boolean getLevel1Status() {
    return level1Status;
  }
  
  /**
   * Sets the level of the game.
   * @param level Integer of the level being played
   */
  public void setLevelNum(int level) {
    this.level = level;
  }

  /**
   * Gets the level of the game.
   * @return game level
   */
  public int getLevel() {
    return level;
  }

  /**
   * Sets the treasure count to how many treasures have been collected.
   * @param treasureCount Integer of the treasures collected
   */
  public void setTreasureCount(int treasureCount) {
    this.treasureCount = treasureCount;
  }

  /**
   * Gets the count of how many treasures have been collected..
   * @return number of treasures.
   */
  public int getTreasureCount() {
    return treasureCount;
  }

  /**
   * Sets the time to how long the game is being played.
   * @param time Integer for the level
   */
  public void setTime(int time) {
    this.time = time;
  }

  /**
   * Gets the time of game. 
   * @return time game is being played.
   */
  public int getTime() {
    return time;
  }

  /**
   * Sets the board to the 2d Array.
   * @param baseBoard 2d array of tiles
   */
  public void setBaseBoard(Tile[][] baseBoard) {
    this.baseBoard = baseBoard;
  }

  /**
   * Gets the array of Tiles that make the board.
   * @return 2d array of Tiles.
   */
  public Tile[][] getBaseBoard() {
    return baseBoard;
  }

  /**
   * Gets the amount of rows in board.
   * @return value of rows.
   */
  public int getRows() {
    return rows;
  }

  /**
   * Sets the amount of rows in board.
   * @param rows Integer of rows in the board
   */
  public static void setRows(int rows) {
    Board.rows = rows;
  }

  /**
   * Gets the amount of cols in board.
   * @return value of cols.
   */
  public int getCols() {
    return cols;
  }

  /**
   * Sets the amount of cols in board.
   * @param cols Integer of columns in the board
   */
  public static void setCols(int cols) {
    Board.cols = cols;
  }

  /**
   * Sets the list "keys" to a keys list passed in.
   * @param k List of keys
   */
  public void setKeys(List<Keys> k) {
    this.keys = k;
  }
  
  /**
   * Getter method for keys collected by Chap.
   * @return list of keys
   */
  
  public List<Keys> getKeys() {
    return keys;
  }

  /**
   * Sets the "treasures" list to a list passed in (count of treasures collected).
   * @param t List of treasures collected
   */
  public void setTreasures(List<Integer> t) {
    this.treasures = t;
  }
  
  /**
   * Getter method for treasures collected by Chap.
   * @return how many treasures have been collected.
   */
  
  public List<Integer> getTreasures() {
    return treasures;
  }

  /**
   * Sets the Tile to the Tile chap is moving to next.
   * @param nextMove Tile that chap is moving to
   */
  public void setNextLoc(Tile nextMove) {
    this.nextMove = nextMove;
  }

  /**
   * Gets the tile that Chap is moving to.
   * @return the next tile chap is going to.
   */
  public Tile getNextLoc() {
    return nextMove;
  }

  /**
   * Sets the dir (direction) in the field to the char passed in.
   * @param dir char of  the direction imposter is moving to
   */
  public void setDir(char dir) {
    this.dir = dir;
  }

  /**
   * Gets the direction as a char.
   * @return dir of Chap's movement.
   */
  public char getDir() {
    return dir;
  }

  /**
   * Sets the storedTime in the field to the int passed in.
   * @param storedTime Integer time stored in game
   */
  public void setStoredTime(int storedTime) {
    this.storedTime = storedTime;
  }

  /**
   * Sets the nextmove in the field to the Tile passed in.
   * @param nextMove Tile of the next move
   */
  public void setNextMove(Tile nextMove) {
    this.nextMove = nextMove;
  }

  /**
   * Sets the gameOver in the field to the boolean passed in.
   * @param gameOver boolean of gameover state
   */
  
  public void setGoStatus(boolean gameOver) {
    this.gameOver = gameOver;
  }

  /**
   * Gets the state of the game. If its true its still running. If false game is over.
   * @return gameOver state
   */
  
  public boolean getGoState() {
    return gameOver;
  }

  /**
   * Sets the trapCase in the field to the boolean passed in.
   * @param trapCase boolean of trap state
   */
  public void setTrapCase(boolean trapCase) {
    this.trapCase = trapCase;
  }

  /**
   * Sets the message in the field to the String passed in.
   * @param message String of the message shown
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Sets the infoPanel in the field to the String passed in.
   * @param infoPanel String of the info panel message
   */
  public void setInfoPanel(String infoPanel) {
    this.infoPanel = infoPanel;
  }

  /**
   * Sets the board string in the field to the String passed in.
   * @param b String of the board
   */
  public void setStringB(String b) {
    this.boardS = b;
  }

  /**
   * Sets the dir (direction) in the field to the String passed in for the Imposters move.
   * @param moveImpString String of the movement the imposter is doing
   */
  public void setImpMove(String moveImpString) {
    this.moveImpString = moveImpString;
  }

  /**
   * Gets the direction of the Imposter as a string.
   * @return dir of Imposter's movement.
   */
  public String getImpMove() {
    return moveImpString;
  }

  /**
   * Sets the board to the String input.
   * @param drawB Board for level 1
   */
  public void setDrawB(String drawB) {
    this.drawB = drawB;
  }

  /**
   * Gets the String version of the board .
   * @return the board made of Strings
   */
  public String getDrawB() {
    return drawB;
  }

}
