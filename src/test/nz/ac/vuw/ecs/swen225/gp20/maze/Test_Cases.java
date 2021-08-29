package test.nz.ac.vuw.ecs.swen225.gp20.maze;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import org.junit.jupiter.api.Test;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board.direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Imposter;
import nz.ac.vuw.ecs.swen225.gp20.maze.Tile;
import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelManager;

/**
 * Class represents Test Cases for maze package 
 * @author Paula
 */

public class Test_Cases {
  //Level one chap r = 7 c = 7
  //Level two chap r = 8 c = 9

  /**
   * Level One Test
   * Checks if Chap moved to the right tile when he moves left.
   */
  @Test
  public void test_1() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.LEFT);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    //System.out.println("r = " +r);
    //System.out.println("c = " +c);
    assertTrue(r == 7 && c == 6);
  }

  /**
   * Level One Test
   * Checks if Chap moved to the right tile when he moves right.
   */
  @Test
  public void test_2() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.RIGHT);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 7 && c == 8);
  }

  /**
   * Level One Test
   * Checks if Chap moved to the right tile when he moves up.
   * Then leaves that tile and checks if the char is still 'I'.
   */
  @Test
  public void test_3() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.UP);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 8 && c == 7);
    b.Move(direction.RIGHT); 
    assertTrue(chapTileAfter.getChar() == 'I');
  }

  /**
   * Level One Test
   * Checks if Chap moved to the right tile when he moves down.
   */
  @Test
  public void test_4() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.DOWN);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 6 && c == 7);
  }

  /**
   * Level One Test
   * Checks if the treasure is added to the list when collected.
   */
  @Test
  public void test_5() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    b.Move(direction.LEFT);
    b.Move(direction.LEFT);
    assertTrue(b.getTreasures().size() == 1);
  }

  /**
   * Level One Test
   * Checks that the board is not null when loaded.
   */
  @Test
  public void test_6() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    b.loadBoard();
    assertTrue(b != null);
  }

  /**
   * Level One Test
   * Checks that the level resets correctly.
   * Treasure and Keys lists are emptied and the gameover Bool is true.
   */
  @Test
  public void test_7() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    b.resetLevel();
    assertTrue(b.getTreasures().size() == 0);
    assertTrue(b.getKeys().size() == 0);
    assertTrue(b.getGoState() == true);
  }

  /**
   * Level One Test
   * Checks that chap cannot walk through the final lock if the treasure list is not 8.
   */
  @Test
  public void test_8() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.LEFT);
    b.Move(direction.UP);
    b.Move(direction.UP);
    b.Move(direction.RIGHT);
    b.Move(direction.UP);

    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 9 && c == 7);
  }

  /**
   * Level One Test
   * Checks that Chap can go through the final lock if all the treasures were collected.
   */
  @Test
  public void test_9() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    Tile chapTileBefore = b.findCurrentPos();

    b.addTreasure();
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();

    b.Move(direction.LEFT);
    b.Move(direction.UP);
    b.Move(direction.UP);
    b.Move(direction.RIGHT);
    b.Move(direction.UP);
    b.Move(direction.UP);

    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 11 && c == 7);
  }

  /**
   * Level One Test
   * Checks that that when a treasure is collected it is added to the treasure list.
   * Check that when the keys are collected they are added to the key list.
   */
  @Test
  public void test_10() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.DOWN);
    b.Move(direction.RIGHT);
    b.Move(direction.RIGHT);
    b.Move(direction.UP);
    b.Move(direction.UP);
    b.Move(direction.UP);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 9 && c == 9);
    assertTrue(b.getTreasures().size() == 1);
    assertTrue(b.getKeys().size() == 2);
  }

  /**
   * Level One Test
   * Checks the size of the list when all keys are collected.
   * Checks the size of the list when all treasures are collected.
   */
  @Test
  public void test_11() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();
    b.addTreasure();

    b.addKey('Y');
    b.addKey('Y');
    b.addKey('R');
    b.addKey('R');
    b.addKey('B');
    b.addKey('B');
    b.addKey('G');
    b.addKey('G');

    assertTrue(b.getKeys().size() == 8);
    assertTrue(b.getTreasures().size() == 8);
    b.addTreasure();
    assertTrue(b.getTreasures().size() > 8);
  }

  /**
   * Level One Test
   * Checks that the  yellow locks work when the player has collected the correct key.
   */
  @Test
  public void test_12() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));

    b.Move(direction.RIGHT);
    b.Move(direction.RIGHT);
    b.Move(direction.UP);
    b.Move(direction.DOWN);
    b.Move(direction.DOWN);
    b.Move(direction.DOWN);
    b.Move(direction.RIGHT);

    assertTrue(b.getKeys().size() == 1);

    b.Move(direction.LEFT);
    b.Move(direction.UP);
    b.Move(direction.UP);
    b.Move(direction.UP);
    b.Move(direction.UP);
    b.Move(direction.RIGHT);

    assertTrue(b.getKeys().size() == 0);

  }  

  /**
   * Level One Test
   * Checks that the green locks work when the player has collected the correct key.
   */
  @Test
  public void test_13() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));

    b.Move(direction.LEFT);
    b.Move(direction.LEFT);
    b.Move(direction.UP);
    b.Move(direction.DOWN);
    b.Move(direction.DOWN);
    b.Move(direction.DOWN);
    b.Move(direction.LEFT);

    assertTrue(b.getKeys().size() == 1);
  }  

  /**
   * Level One Test
   * Checks that the locks work when the player has not collected key.
   * Does not let Chap through.
   */
  @Test
  public void test_14() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    b.Move(direction.DOWN);
    b.Move(direction.DOWN);
    b.Move(direction.RIGHT);
    b.Move(direction.DOWN);

    Tile chapTile = b.findCurrentPos();

    int r = chapTile.getRow();
    int c = chapTile.getCol();
    //System.out.println("row = " + r + "col = " + c);
    assertTrue(r == 5 && c == 8); 
  } 

  /**
   * Level One Test
   * Checks if the time in the level is correct according to the json file loaded in.
   */
  @Test
  public void test_15() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    b.getTime();
    assertTrue(b.getTime() == 60);
  }

  /**
   * Level One Test
   * Checks if the cols are correct according to the json file loaded in.
   */
  @Test
  public void test_16() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    b.getCols();
    assertTrue(b.getCols() == 15);
  }
  
  /**
   * Level One Test
   * Checks if the clone method works.
   */
  @Test
  public void test_17() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level1.json"));
    b.clone();
    assertTrue(b.getLevel() == 1);
  }

  /**
   * Level Two Test
   * Checks if Chap moved to the right tile when he moves left.
   */
  @Test
  public void test_2_1() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.LEFT);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 8 && c == 8);
  }

  /**
   * Level Two Test
   * Checks if Chap moved to the right tile when he moves right.
   */
  @Test
  public void test_2_2() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.RIGHT);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 8 && c == 10);
  }

  /**
   * Level Two Test
   * Checks if Chap moved to the right tile when he moves up.
   * Then leaves that tile and checks if the char is still 'I'.
   */
  @Test
  public void test_2_3() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.UP);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 9 && c == 9);
    b.Move(direction.RIGHT);
    assertTrue(chapTileAfter.getChar() == 'I');
  }

  /**
   * Level Two Test
   * Checks if Chap moved to the right tile when he moves down.
   */
  @Test
  public void test_2_4() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.DOWN);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 7 && c == 9);
  }

  /**
   * Level Two Test
   * Checks that when chap goes onto the imposter the game restarts 
   * And Chap goes back to his intial position.
   */
  @Test
  public void test_2_5() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.UP);
    b.Move(direction.UP);
    b.Move(direction.UP);
    b.Move(direction.UP);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 8 && c == 9);
  }

  /**
   * Level Two Test
   * Checks if Chap can collect a treasure in level 2.
   */
  @Test
  public void test_2_6() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    b.Move(direction.DOWN);
    b.Move(direction.DOWN);
    b.Move(direction.LEFT);
    b.Move(direction.LEFT);
    b.Move(direction.LEFT);
    assertTrue(b.getTreasures().size() == 1);
  }

  /**
   * Level Two Test
   * Checks if the board is not null when level 2 is loaded.
   */
  @Test
  public void test_2_7() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    b.loadBoard();
    assertTrue(b != null);
  }

  /**
   * Level Two Test
   * Checks if Chap fell into a trap.
   * And if the game restarts correctly.
   */
  @Test
  public void test_2_8() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    Tile chapTileBefore = b.findCurrentPos();
    b.Move(direction.DOWN);
    b.Move(direction.DOWN);
    b.Move(direction.RIGHT);
    Tile chapTileAfter = b.findCurrentPos();
    int r = chapTileAfter.getRow();
    int c = chapTileAfter.getCol();
    assertTrue(r == 8 && c == 9);
    assertTrue(b.getTreasures().size() == 0);
    assertTrue(b.getKeys().size() == 0);
  }

  /**
   * Level Two Test
   * Checks if the Imposter is loading.
   */
  @Test
  public void test_2_9() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    b.setLevel2True();
    b.loadBoard();
    b.loadImposter();
    assertTrue(b != null);
  }

  /**
   * Level Two Test
   * Checks if the Imposter is moving correctly.
   */
  @Test
  public void test_2_10() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    Tile[][] baseB = b.getBaseBoard();
    Imposter imp = Imposter.findCurrentImp(baseB).getImp();

    imp.move(baseB, "UP");
    imp.move(baseB, "LEFT");
    imp.move(baseB, "DOWN");
    imp.move(baseB, "RIGHT");
    imp.move(baseB, "RIGHT");

    Tile impTile = Imposter.findCurrentImp(baseB);

    int r = impTile.getRow();
    int c = impTile.getCol();

    assertTrue(r == 12 && c == 10);
  }

  /**
   * Level Two Test
   * Checks if the Imposter can go through the info tile.
   */
  @Test
  public void test_2_11() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    Tile[][] baseB = b.getBaseBoard();
    Imposter imp = Imposter.findCurrentImp(baseB).getImp();

    imp.move(baseB, "DOWN");
    imp.move(baseB, "DOWN");
    imp.move(baseB, "DOWN");
    imp.move(baseB, "LEFT");

    Tile impTile = Imposter.findCurrentImp(baseB);

    int r = impTile.getRow();
    int c = impTile.getCol();
    assertTrue(r == 9 && c == 8);  
  }

  /**
   * Level Two Test
   * Checks if the Imposter is in the correct tile when he tries to walk through a wall.
   * Does not let him go to that wall tile.
   */
  @Test
  public void test_2_12() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    Tile[][] baseB = b.getBaseBoard();
    Imposter imp = Imposter.findCurrentImp(baseB).getImp();

    imp.move(baseB, "RIGHT");
    imp.move(baseB, "RIGHT");
    imp.move(baseB, "RIGHT");
    imp.move(baseB, "RIGHT");
    Tile impTile = Imposter.findCurrentImp(baseB);

    int r = impTile.getRow();
    int c = impTile.getCol();
    //System.out.println("row = " + r + "col = " + c);
    assertTrue(r == 12 && c == 12);  
  }

  /**
   * Level Two Test
   * Checks that the level is being set to the correct one according to the json file.
   */
  @Test
  public void test_2_13() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    b.getLevel();
    assertTrue(b.getLevel() == 2);
  }

  /**
   * Level Two Test
   * Checks if the rows are correct according to the json file loaded in.
   */
  @Test
  public void test_2_14() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    b.getRows();
    assertTrue(b.getRows() == 19);
  }

  /**
   * Level Two Test
   * Checks if the pop up saying the player has won the game works (End Level).
   */
  @Test
  public void test_2_15() {
    LevelManager lm = new LevelManager();
    Board b =  lm.loadGame(new File("levels/level2.json"));
    b.setLevelNum(2);
    b.endLevel();
    assertTrue(b.getLevel() == 2);
  }

}
