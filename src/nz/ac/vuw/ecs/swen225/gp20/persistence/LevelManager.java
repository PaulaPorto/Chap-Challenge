package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board;
import nz.ac.vuw.ecs.swen225.gp20.maze.Chap;
import nz.ac.vuw.ecs.swen225.gp20.maze.Imposter;
import nz.ac.vuw.ecs.swen225.gp20.maze.Keys;
import nz.ac.vuw.ecs.swen225.gp20.maze.Tile;

/**
 * Class that handles the saving and loading of the game/board.
 * 
 * @author Nicholo Antigo 300410130
 *
 */
public class LevelManager {

  //the variables needed for the RecordAndReplay Module
  private List<String> moveList = new ArrayList<>();
  private List<String> impMoveList = new ArrayList<>();
  private Board initialBoard;

  /**
   * Saves the game and writes the maze into a JSON format.
   * @param currentBoard     the board that is being saved
   */
  public void saveGame(Board currentBoard) {

    Board currentStateBoard = currentBoard;
    int levelNum = currentStateBoard.getLevel();
    File savesDir = new File("saves/");
    BufferedWriter writer = null;
    try {
      
      //creates the directory if it doesn't exist
      if (!(savesDir.exists())) {
        savesDir.mkdir();
      }
      
      writer = Files.newBufferedWriter(Paths.get("saves/" + fileNameHelper(levelNum)));

      if (writer != null) {

        //creating the map for the board properties
        Map<Object, Object> boardProp = new HashMap<Object, Object>();

        //adding different properties that the board has to the map with corresponding variable name
        boardProp.put("level", currentStateBoard.getLevel());
        boardProp.put("rows", currentStateBoard.getRows());
        boardProp.put("columns", currentStateBoard.getCols());

        boardProp.put("treasureCount", currentStateBoard.getTreasureCount());
        boardProp.put("time", currentStateBoard.getTime());

        boardProp.put("baseBoard", currentStateBoard.getBaseBoard());

        boardProp.put("keys", currentStateBoard.getKeys());
        boardProp.put("treasures", currentStateBoard.getTreasures());

        ObjectMapper mapper = new ObjectMapper();

        //turning the map into JSON string and writing it to a file
        writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(boardProp));
        writer.close();

      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the game from the JSON file that is chosen by the player.
   * @param f  file that contains the JSON information
   * @return   a board object that contains the new information loaded from the JSON file
   */
  public Board loadGame(File f) {

    Board newBoard = null;
    try {
      Reader reader = Files.newBufferedReader(Paths.get(f.toString()));

      ObjectMapper mapper = new ObjectMapper();

      //reading the JSON file into a tree
      JsonNode parser = mapper.readTree(reader);

      //the properties in the JSON file
      int level = parser.path("level").asInt();
      int rows = parser.path("rows").asInt();
      int columns = parser.path("columns").asInt();

      //the new board that is going to be returned
      newBoard = new Board();

      //the new 2D array that contains the board
      Tile[][] newBaseBoard = new Tile[rows][columns];

      //lists for the keys and the treasures
      List<Keys> newKeys = new ArrayList<>();
      List<Integer> newTreasures = new ArrayList<>();

      //adding the properties from the JSON into the board
      newBoard.setLevelNum(level);
      newBoard.setRows(rows);
      newBoard.setCols(columns);

      //iterates through the baseBoard 2D array and gets the info of the Tile objects in JSON format
      for (JsonNode node : parser.path("baseBoard")) {
        for (JsonNode node2 : node) {
          int tileRow = node2.path("row").asInt();
          int tileCol = node2.path("col").asInt();
          char tileChar = node2.path("char").asText().charAt(0);

          //if chap is not null, create a chap object and set the corresponding values
          if (!(node2.path("chap").asText().equals("null"))) {
            JsonNode chapNode = node2.path("chap");
            int chapRow = chapNode.path("row").asInt();
            int chapCol = chapNode.path("col").asInt();
            Chap chapObj = new Chap();
            chapObj.setPosition(chapRow, chapCol);
            Tile tileObj = new Tile(tileChar, tileRow, tileCol);
            tileObj.setChap(chapObj);
            newBaseBoard[tileRow][tileCol] = tileObj;
          } else {
            Tile tileObj = new Tile(tileChar, tileRow, tileCol);
            newBaseBoard[tileRow][tileCol] = tileObj;
          }
          
          //if imposter is not null, create imp object and set corresponding values
          if (!(node2.path("imp").asText().equals("null"))) {
            JsonNode impNode = node2.path("imp");
            int impRow = impNode.path("row").asInt();
            int impCol = impNode.path("col").asInt();
            Imposter impObj = new Imposter();
            impObj.setPos(impRow, impCol);
            Tile tileObj = new Tile(tileChar, tileRow, tileCol);
            tileObj.setImp(impObj);
            newBaseBoard[tileRow][tileCol] = tileObj;
          } else {
            Tile tileObj = new Tile(tileChar, tileRow, tileCol);
            newBaseBoard[tileRow][tileCol] = tileObj;
          }

        }
      }

      //loop to get all the keys
      for (JsonNode keyNode : parser.path("keys")) {
        String keyColour = keyNode.path("color").asText();
        Keys keyObj = new Keys(keyColour);
        newKeys.add(keyObj);
      }

      //loop to get all the treasures
      for (JsonNode treasureNode : parser.path("treasures")) {
        int treasure = treasureNode.asInt();
        newTreasures.add(treasure);
      }

      //setting the different properties in the board object
      newBoard.setKeys(newKeys);
      newBoard.setTreasures(newTreasures);
      newBoard.setBaseBoard(newBaseBoard);
      newBoard.setTreasureCount(parser.path("treasureCount").asInt());
      newBoard.setTime(parser.path("time").asInt());

      reader.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return newBoard;
  }

  /**
   * Open the JFileChooser and make user pick a file, pass the returned file into loadGame.
   * @return   a board object that contains the information loaded from the JSON file
   */
  public Board chooseFileAndLoad() {

    File chosenFile = chooseFile();
    
    if (chosenFile == null) {
      return null;
    }
    
    return loadGame(chosenFile);
  }

  /**
   * Loads a level pack which contains different level names and loads them ahead of time.
   * @return     a list of Board objects that pertains to the different levels loaded
   */
  public List<Board> loadLevelPack() {

    BufferedReader reader = null;
    List<Board> levels = new ArrayList<>();
    List<File> files = new ArrayList<>();
    File levelPack = chooseFile();

    try {

      reader = new BufferedReader(new FileReader(levelPack));
      String currentLine;

      //while the currentLine contains a file name that's in the levelpack, load and add to the list
      while ((currentLine = reader.readLine()) != null) {
        File currentFile = new File("levels/" + currentLine);
        files.add(currentFile);
      }

      reader.close();
      
      //add the made boards into the board list
      for (File file : files) {
        levels.add(loadGame(file));
      }


    } catch (Exception e) {
      e.printStackTrace();
    }

    if (levels.size() == 0) {
      return null;
    }

    return levels;
  }

  /**
   * Loads a record.json file and creates a Board object for each of the moves that Chap has done
   * Also keeps track of the treasures and the keys Chap has picked up.
   */
  public void loadRecord() {

    File chosenFile = chooseFile();
    Board boardObj = new Board();
    
    if (chosenFile != null) {
      try {
        Reader read = Files.newBufferedReader(Paths.get(chosenFile.toString()));

        ObjectMapper mapper = new ObjectMapper();

        JsonNode parser = mapper.readTree(read);

        //getting the actionQueue and placing it in moveList
        for (JsonNode moveNode : parser.path("actionQueue")) {
          moveList.add(moveNode.asText());
        }
        
        //getting the imposter moves and placing it in the list
        for (JsonNode impMoveNode : parser.path("imposterActionQueue")) {
          impMoveList.add(impMoveNode.asText());
        }

        JsonNode boardNode = parser.path("board");
        
        //setting the level, the rows and columns
        int level = boardNode.path("level").asInt();
        
        int rows;
        int columns;
        if (level == 1) {
          rows = 15;
          columns = 15;
        } else {
          rows = 19;
          columns = 19;
        }

        //collections for the baseBoard, keys and treasures
        Tile[][] newBaseBoard = new Tile[rows][columns];
        List<Keys> newKeys = new ArrayList<>();
        List<Integer> newTreasures = new ArrayList<>();

        //adding the levels, rows, columns into the new Board
        boardObj.setLevelNum(level);
        boardObj.setRows(rows);
        boardObj.setCols(columns);

        //iterating through the tile objects
        for (JsonNode node : boardNode.path("baseBoard")) {
          for (JsonNode node2 : node) {
            int tileRow = node2.path("row").asInt();
            int tileCol = node2.path("col").asInt();
            char tileChar = node2.path("c").asText().charAt(0);

            //if chap is not null make a chap object
            if (!(node2.path("chap").asText().equals("null"))) {
              JsonNode chapNode = node2.path("chap");
              int chapRow = chapNode.path("row").asInt();
              int chapCol = chapNode.path("col").asInt();
              Chap chapObj = new Chap();
              chapObj.setPosition(chapRow, chapCol);
            } else {
              Tile tileObj = new Tile(tileChar, tileRow, tileCol);
              newBaseBoard[tileRow][tileCol] = tileObj;
            }
            
            //if imp is not null make the imp obj and set the values
            if (!(node2.findPath("imp").asText().equals("null"))) {
              JsonNode impNode = node2.path("imp");
              int impRow = impNode.path("row").asInt();
              int impCol = impNode.path("col").asInt();
              Imposter impObj = new Imposter();
              impObj.setPos(impRow, impCol);
              Tile tileObj = new Tile(tileChar, tileRow, tileCol);
              tileObj.setImp(impObj);
              newBaseBoard[tileRow][tileCol] = tileObj;
            } else {
              Tile tileObj = new Tile(tileChar, tileRow, tileCol);
              newBaseBoard[tileRow][tileCol] = tileObj;
            }
          }

          //looping to get the keys
          for (JsonNode keyNode : boardNode.path("keys")) {
            String keyColour = keyNode.path("color").asText();
            Keys keyObj = new Keys(keyColour);
            newKeys.add(keyObj);
          }

          //loop to get the treasures
          for (JsonNode treasureNode : boardNode.path("treasures")) {
            int treasure = treasureNode.asInt();
            newTreasures.add(treasure);
          }

          //setting other properties in the board
          boardObj.setTreasureCount(boardNode.path("treasureCount").asInt());
          boardObj.setKeys(newKeys);
          boardObj.setTreasures(newTreasures);
          boardObj.setBaseBoard(newBaseBoard);
          boardObj.setTime(boardNode.path("time").asInt());

          setInitialBoard(boardObj);

          read.close();

        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Gets the moveList needed for RecordAndReplay.
   * @return     the list of moves done by the player
   */
  public List<String> getMoveList() {
    return moveList;
  }

  /**
   * Gets the impMoveList needed for RecordAndReplay.
   * @return     the list of moves done by the imposter
   */
  public List<String> getImpMoveList() {
    return impMoveList;
  }

  /**
   * Gets the initialBoard which is needed for RecordAndReplay.
   * @return     the initial board when the "record" button was clicked
   */
  public Board getInitialBoard() {
    return initialBoard;
  }

  /**
   * Sets the initialBoard to a populated Board object.
   * @param initialBoard     the populated board 
   */
  public void setInitialBoard(Board initialBoard) {
    this.initialBoard = initialBoard;
  }

  /**
   * Helps with the filename when saving games.
   * @param level    the level number of the current level getting saved
   * @return         the name of the file that is going to be saved
   */
  private String fileNameHelper(int level) {
    SimpleDateFormat formatDate = new SimpleDateFormat("'Level'" + level 
        + "'_'dd-MM-yyyy-HH-mm-ss'.json'");
    Date date = new Date(System.currentTimeMillis());
    return formatDate.format(date);
  }

  /**
   * Prompts the user to choose a file and returns the file chosen.
   * @return   the file that is chosen by the user
   */
  private File chooseFile() {
    File chosenFile = null;
    try {
      JFileChooser choose = new JFileChooser("saves");
      choose.setDialogTitle("Pick a file to load:");

      //showing the file chooser
      int value = choose.showSaveDialog(null);
      if (value == JFileChooser.APPROVE_OPTION) {
        chosenFile = choose.getSelectedFile();
      }

      //if the chosenFile is null 
      if (chosenFile == null) {
        return null;
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }

    return chosenFile;
  }


}
