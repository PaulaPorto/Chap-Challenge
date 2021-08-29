package test.nz.ac.vuw.ecs.swen225.gp20.persistence;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board.direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Imposter;
import nz.ac.vuw.ecs.swen225.gp20.maze.Tile;
import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelManager;
import org.junit.jupiter.api.Test;


/**
 * Class that has test cases for the Persistence package.
 * 
 * @author Nicholo Antigo 300410130
 *
 */
public class TestCases {
  
  /**
   * Testing to see if level 1 loads properly.
   */
  @Test
  public void test_LoadLevel1() {
    LevelManager lm = new LevelManager();
    File level1 = new File("levels/level1.json");
    Board level1Board = lm.loadGame(level1);
    
    //check that it is not null
    assertTrue(level1Board != null);
    
    Tile chapTile = level1Board.findCurrentPos();
    
    //check that chap is in the right position for level 1 (properly loaded)
    assertTrue(chapTile.getRow() == 7 && chapTile.getCol() == 7);
    
  }
  
  /**
   * Testing to see if level 2 loads properly.
   */
  @Test
  public void test_LoadLevel2() {
    LevelManager lm = new LevelManager();
    File level2 = new File("levels/level2.json");
    Board level2Board = lm.loadGame(level2);
    
    //check that it is not null
    assertTrue(level2Board != null);
    
    Tile chapTile = level2Board.findCurrentPos();
    
    //check that chap is in the right position for level 2 (properly loaded)
    assertTrue(chapTile.getRow() == 8 && chapTile.getCol() == 9);
    
    Imposter imp = Imposter.findCurrentImp(level2Board.getBaseBoard()).getImp();
    
    //imposter is not null and exists in level 2
    assertTrue(imp != null);
  }
  
  /**
   * Testing to see if saving a game works.
   */
  @Test
  public void test_SaveGame() {
    LevelManager lm = new LevelManager();
    File level1 = new File("levels/level1.json");
    Board level1Board = lm.loadGame(level1);
    
    //moving Chap to collect a treasure and a key
    level1Board.Move(direction.LEFT);
    level1Board.Move(direction.LEFT);
    level1Board.Move(direction.UP);
    
    lm.saveGame(level1Board);
    
    
  }
  
  /**
   * Testing to see if loading a save file works.
   */
  @Test
  public void test_LoadingTestSave() {
    LevelManager lm = new LevelManager();
    
    //PLEASE LOAD THE SAVE FILE
    //choosing a file (the test file) and loading that test file
    Board loadedBoard = lm.chooseFileAndLoad();
    
    assertTrue(loadedBoard != null);
  }
  
  /**
   * Testing to see if loading multiple levels work.
   */
  @Test
  public void test_LoadLevelPack() {
    LevelManager lm = new LevelManager();
    
    //NEED TO CHOOSE levelpack.txt in the levels folder
    //Loading the level pack that is in the levels folder
    List<Board> levelPackBoards = lm.loadLevelPack();
    
    //checking that the list is not empty
    assertTrue(levelPackBoards.size() > 0);
    
    //checking that the boards are not null
    assertTrue(levelPackBoards.get(0) != null);
    assertTrue(levelPackBoards.get(1) != null);
    
    //checking that the 0th element is the first level and the 1st element is the second level
    assertTrue(levelPackBoards.get(0).getLevel() == 1);
    assertTrue(levelPackBoards.get(1).getLevel() == 2);
  }
  
  /**
   * Testing to see if loading a record work.
   */
  @Test
  public void test_LoadRecord() {
    LevelManager lm = new LevelManager();
    
    //NEED TO CHOOSE the a record file
    lm.loadRecord();
    
    List<String> loadedMoves = lm.getMoveList();
    Board loadedBoard = lm.getInitialBoard();
    
    //checking if the moves list (for record and replay) is not empty and has been loaded properly
    assertTrue(loadedMoves.size() > 0);
    
    //checking if the board
    assertTrue(loadedBoard != null);
  }
  
}