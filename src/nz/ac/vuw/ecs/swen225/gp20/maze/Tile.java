package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Tile class for the board. 
 * @author Paula
 *
 */

public class Tile implements Cloneable {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  private int row; //row of Tile
  private int col; //col of Tile 
  private char charC; //char on Tile
  private Chap chap; //Chap object
  private Imposter imp; //Imposter object



  //------------------------
  // CONSTRUCTOR
  //------------------------
  
  /**
   * Sets the char row and col for a tile.
   * @param ch char on the tile
   * @param row Tile row
   * @param col Tile col
   */
  public Tile(char ch, int row, int col) {
    this.charC = ch;
    this.row = row;
    this.col = col;
  }

  /**
   * Clone method for every tile.
   * @return  the cloned tile.
   */
  public Tile clone() {
    Tile cloned = new Tile(this.charC, this.row, this.col);

    if (getChap() != null) {
      cloned.setChap(this.chap.clone());
    }
    if (getImp() != null) {
      cloned.setImp(this.imp.clone());
    }

    return cloned;

  }

  /**
   * Returns the row position of the tile.
   * @return  the row position
   */
  public int getRow() {
    return this.row;
  }

  /**
   * Returns the col position of the tile.
   * @return  the col position
   */
  public int getCol() {
    return this.col;
  }

  /**
   * gets the char in the Tile.
   * @return  the char on Tile
   */
  public Character getChar() {
    return this.charC;
  }


  /**
   * get the location of chap (row and col).
   * @return  chap object
   */
  public Chap getChap() {
    return chap;
  }

  /**
   * sets chap to the chap in the parameter.
   * @param chapp being passed in
   */
  public void setChap(Chap chapp) {
    this.chap = chapp;
  }

  /**
   * get the location of imposter (row and col).
   * @return  imposter object
   */
  public Imposter getImp() {
    return imp;
  }

  /**
   * sets Imposter to the imposter in the field.
   * @param imposter being passed in
   */
  public void setImp(Imposter imposter) {
    this.imp = imposter;
  }

  /**
   * sets the char to the char in the parameter.
   * @param ch char being passed in
   */
  public void setChar(Character ch) {
    this.charC = ch;
  }

  /**
   * toString method. Prints out row col and char.
   * @return  row col and char as a string
   */
  @Override
  public String toString() {
    return row + "," + col + "," + charC;
  }

}
