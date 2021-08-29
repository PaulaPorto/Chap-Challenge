package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * class to test guava and the preconditions. 
 * @author Paula
 *
 */
public class GuavaTester {


  /**
   * Run this main to check the precondition of treasure count.
   * @param args of string being passed in
   */
  public static void main(String[] args) {
    Board b = new Board();
    int treasures = b.getTreasureCount();
    //Preconditions precon = new Preconditions(1);
    //Preconditions precon2 = new Preconditions(10);
  }  

} 
