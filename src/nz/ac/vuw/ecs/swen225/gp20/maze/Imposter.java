package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.maze.Board.direction;

/**
 * class represents the Imposter used for level two. 
 * @author Paula
 *
 */
public class Imposter  implements Cloneable { 

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //boolean move; //checks if imposter is moving
  private int row; //Imposter's row
  private int col; //Imposter's col
  boolean isValid = true; //checks if the imposter's move is valid or not.
  static boolean killChap = false; //checks if the imposter killed chap.
  private static char dirI;
  //private Board b;
  //private Tile[][] baseBoard;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  /**
   * Sets the position of the imposter in level 2.
   * @param r row imposter is in
   * @param c col imposter is in
   */
  public void setPos(int r, int c) {
    //this.move = m;
    this.row = r;
    this.col = c;
  }

  /**
   * Clone method for the Imposter.
   * @return  the cloned Imposter.
   */
  public Imposter clone() {
    Imposter cloned = new Imposter();
    cloned.setCol(col);
    cloned.setRow(row);
    cloned.setDirI(dirI);
    cloned.setIsValid(isValid);
    cloned.setkillChap(killChap);

    return cloned;
  }

  /**
   *Specifies what moves are done for the imposter throughout the entire game for level 2.
   *@param baseBoard of the game
   *@param direction Imposter is moving in
   */
  public void move(Tile[][] baseBoard, String direction) {
    Tile currentImpT = findCurrentImp(baseBoard);
    Tile newPosI = null;
    int currentIRow = currentImpT.getRow();
    int currentICol = currentImpT.getCol();
    int dx = 0;
    int dy = 0;

    switch (direction) {
      case "UP": 
        setDirI('/');
        dy = 1;
        break;

      case "DOWN":
        setDirI('-');
        dy = -1;
        break;

      case "LEFT": 
        setDirI('*');
        dx = -1;
        break;

      case "RIGHT": 
        setDirI('^');
        dx = +1;
        break;

      default: break;
    }
    if (currentICol > 0 && currentICol < 19 && currentIRow > 0 && currentIRow < 19) {
      newPosI = baseBoard[currentIRow + dy][currentICol + dx];

      if (newPosI.getChar() == 'C') { 
        moveI(currentImpT, newPosI);
        currentImpT = newPosI; 
        killChap = true;
      }
      
      //move chap from the old pos to the new pos according to direction.
      moveI(currentImpT, newPosI); 
      if (isValid == true) {
        currentImpT = newPosI;
      } 

    }
  }

  /**
   *Checks the valid moves imposter can make throughout the game.
   *Sets the new position to the current position.
   *Makes the oldTile a floor tile.
   *@param oldTile imposter was in
   *@param newTile imposter is moving to
   */
  public void moveI(Tile oldTile, Tile newTile) {
    Imposter i = oldTile.getImp();
    char t = newTile.getChar();
    if (t == 'T' || t == 'Q' || t == 'W' || t == 'O' 
        || t == 'y' || t == 'b' || t == 'g' || t == 'r' 
        || t == 'Y' || t == 'B' || t == 'G' || t == 'R') {
      isValid = false;
      Board b = new Board();
      b.tick();
    } else {
      isValid = true;
    }
    if (isValid == true) {
      oldTile.setImp(null);
      if (oldTile.getRow() == 9 && oldTile.getCol() == 9) {
        oldTile.setChar('I');
      } else {
        oldTile.setChar('F');
      }

      newTile.setImp(i);
      newTile.setChar('X');
    }
  }

  /**
   * Finds the exact Tile that the char 'X' is currently in.
   * @param baseBoard of the game
   * @return Tile that contains 'X'
   */
  public static Tile findCurrentImp(Tile [][] baseBoard) {
    for (int row = 0; row < baseBoard.length; row++) {
      for (int col = 0; col < baseBoard.length; col++) {
        if (baseBoard[row][col] != null) {
          if (baseBoard[row][col].getChar() == 'X') {
            //System.out.println(row+1);
            //System.out.println(col+1);
            return baseBoard[row][col];

          }
        }
      }
    }
    return null;
  }

  //------------------------
  // INTERFACE
  //------------------------

  /**
   * gets the row of Imposter's position.
   * @return row where Imposter is
   */
  public int getRow() {
    return row;
  }

  /**
   * sets the row of Imposter's position.
   * @param row of the imposter
   */
  public void setRow(int row) {
    this.row = row;
  }

  /**
   * gets the col of Imposter's position.
   * @return col where Imposter is
   */
  public int getCol() {
    return col;
  }

  /**
   * sets the col of Imposter's position.
   * @param col of the imposter
   */
  public void setCol(int col) {
    this.col = col;
  }

  /**
   * sets the isValid to the boolean passed in.
   * @param isValid boolean if the move is valid
   */
  public void setIsValid(boolean isValid) {
    this.isValid = isValid;
  }

  /**
   * sets the killChap to the boolean passed in.
   * @param killChap boolean chap was killed by the imposter
   */
  public static void setkillChap(boolean killChap) {
    Imposter.killChap = killChap;
  }

  /**
   * gets the boolean status of killchap. Id its tru chap is dead. If false chap was not killed.
   * @return killchap boolean
   */
  public static boolean getKillStatus() {
    return killChap;
  }

  /**
   * sets the direction of Imposter.
   * @param dirI direction of the imposter
   */
  public static void setDirI(char dirI) {
    Imposter.dirI = dirI;
  }

  /**
   * gets the direction that the imposter is moving.
   * @return direction of the imposter.
   */
  public static char getDirI() {
    return dirI;
  }


}
