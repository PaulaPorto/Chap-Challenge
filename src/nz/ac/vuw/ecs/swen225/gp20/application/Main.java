package nz.ac.vuw.ecs.swen225.gp20.application;

/**
 * The main class that runs the whole program.
 * 
 * @author Nick Mazey 300473270
 *
 */
public class Main {
  /**
   * The main method used to run the program.
   * @param args - The arguments parsed to the program (Takes none).
   */
  public static void main(String[] args) {
    boolean checkingFrameRate = false;
    GameUserInterface g = new GameUserInterface();
    long time1 = System.currentTimeMillis();
    int i = 0;
    while (g != null) {
      if (checkingFrameRate) { 
        if (System.currentTimeMillis() - time1 >= 1000) {
          System.out.println(i + " Frames Per Second");
          i = 0;
        }
      }
      time1 = System.currentTimeMillis();
      g.tick();
      while (!g.doneFrame()) {
        try {
          Thread.sleep(g.timeToSleep(), 0);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        if (time1 - System.currentTimeMillis() > 10000) { // Game not responding for 10 seconds
          g = null;
          break;
        }
      }
      if (checkingFrameRate) {
        i++;
      }
    }
  }
}
