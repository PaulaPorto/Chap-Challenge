package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.google.common.annotations.GwtCompatible;
import static com.google.common.base.Preconditions.checkArgument;
//import static com.google.common.base.Preconditions.checkNotNull;



/**
 * class to create the Preconditions. 
 * @author Paula
 *
 */
@GwtCompatible
public class Preconditions { 

  public int treasures; //count of treasures

  /**
   * Precondition: checks if the treasure is greater than or equal to 0.
   * And less than 9.
   * @param treasures Integer of treasures
   */
  public Preconditions(int treasures) {
    checkArgument(treasures > -1 && treasures < 9, "treasure count should be greater than -1");
    this.treasures = treasures; 
  } 

  /**
   * gets the treasure count.
   * @return treasure count
  */
  public int getTreasures() {
    return treasures;
  }

  /**
   * Sets the treasure count to the int passed in.
   * Checks if the treasure is greater than 0.
   * @param treasures Integer of treasures.
   */
  public void setTreasures(int treasures) {
    checkArgument(treasures > 0, "age should be positive number");
    this.treasures = treasures;
  }

}
