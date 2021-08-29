package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board.direction;

/**
 * This class handles the record logic for the Chap's Challenge.
 * This class starts a recording while the game is running and
 * once the player presses the "Start Recording" button from
 * the JMenuBar. It captures each move the player makes.
 * Once the game is done recording, the player can
 * press the "Stop Recording" buttons from the JMenuBar.
 * The file gets stored inside a folder called "Recorded Files".
 *
 * @author Abraham Alfred 300509598
 */
public class RecordGame {
  private Queue<direction> actionQueue = new ArrayDeque<>();
  private Queue<direction> imposterActionQueue = new ArrayDeque<>();
  private Board board;

  /**
   * This Constructor handles the initialization of the record.
   * This also makes a clone of the board.
   *
   * @param board     the current state of the board
   */
  public RecordGame(Board board) {
    this.board = board.clone();
  }

  /**
   * This method adds the action movement generated
   * by the Move class to the actionQueue.
   *
   * @param direc     the direction that is being added
   */
  public void addAnAction(direction direc) {
    actionQueue.add(direc);
  }

  /**
   * This method adds the imposter action movement to the imposterActionQueue.
   *
   * @param impDirec     the imposter dir that is being added
   */
  public void addAnImposterAction(direction impDirec) {
    imposterActionQueue.add(impDirec);
  }

  /**
   * This method saves the record as a JSON format file.
   * Also creates a folder in the Chap's Challenge directory
   * and stores the save file inside the Recorded Files folder accordingly.
   */
  public void finalise() {
    File recordDirectory = new File("Recorded Files/");
    try {
      if (!(recordDirectory.exists())) {
        recordDirectory.mkdir();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    ObjectMapper mapper = new ObjectMapper();
    String json = null;
    try {
      // So basically, this fixed the BeanSerializer Error
      mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
      
      // Pretty printer will print the output file nicely
      json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    if (json != null) {
      try {
        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(
                 "Recorded Files/" + dateHelper()));
        bufferedWriter.write(json);
        bufferedWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Helper Method for date formatting.
   * This gets passed to the finalise()
   * method above, and writes the name of
   * the .json file as a time and date stamp.
   *
   * @return formatDate     the formatted name of the record file
   */
  public String dateHelper() {
    SimpleDateFormat formatDate = new SimpleDateFormat("'Record-'dd-MM-yyyy-HH-mm-ss'.json'");
    Date date = new Date(System.currentTimeMillis());
    return formatDate.format(date);
  }

  /**
   * Getter method for board.
   *
   * @return board     the board that was saved
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Setter method for board.
   *
   * @param board      the new board that is being set
   */
  public void setBoard(Board board) {
    this.board = board;
  }
}