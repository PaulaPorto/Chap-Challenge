package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Class represents the Keys collected in the game. 
 * @author Paula
 *
 */

public class Keys implements Cloneable {
  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //the color of the key
  private String color;


  //------------------------
  // CONSTRUCTOR
  //------------------------

  /**
   * Constructor for keys.
   * @param keyColor String color of the key
   */
  public Keys(String keyColor) {
    this.color = keyColor;
  }

  /**
   * Clone method for Keys.
   * @return  the cloned keys.
   */
  public Keys clone() {
    Keys cloned = new Keys(this.color);
    return cloned;
  }


  //------------------------
  // INTERFACE
  //------------------------

  /**
   * Gets the color of the key.
   * @return  key color
   */
  public String getColor() {
    return color; 
  }

  /**
   * Equals method for keys.
   * @param o Object
   * @return key color
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Keys keys = (Keys) o;
    return this.color.equals(keys.getColor());

  }
  
  /**
   * Hashcode method for equals method.
   */
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42; // any arbitrary constant will do
}

}
