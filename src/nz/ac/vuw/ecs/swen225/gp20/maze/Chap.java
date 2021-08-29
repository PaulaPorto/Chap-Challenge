package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * class represents the Chap object. 
 * @author Paula
 *
 */

public class Chap implements Cloneable {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //the row and column of Chap's location
  private int row;
  private int col;


  //------------------------
  // CONSTRUCTOR
  //------------------------

  /**
   * Sets the row and col for Chap.
   * @param r Integer row where chap is
   * @param c Integer col where chap is
   */
  public void setPosition(int r, int c) {
    this.row = r;
    this.col = c;
  }

  /**
   * Clone method for Chap.
   * @return  the cloned Chap.
   */
  public Chap clone() {
    Chap cloned = new Chap();
    cloned.setRow(row);
    cloned.setCol(col);

    return cloned;
  }

  //------------------------
  // INTERFACE
  //------------------------

  /**
   * gets the row of Chap's position.
   * @return row where chap is
   */
  public int getRow() {
    return row;
  }

  /**
   * sets the row of Chap's position.
   * @param row Integer where chap is being set to
   */
  public void setRow(int row) {
    this.row = row;
  }

  /**
   * gets the col of Chap's position.
   * @return col where chap is
   */
  public int getCol() {
    return col;
  }

  /**
   * sets the col of Chap's position.
   * @param col Integer where chap is being set to
   */
  public void setCol(int col) {
    this.col = col;
  }


}