package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import nz.ac.vuw.ecs.swen225.gp20.maze.Board;
import nz.ac.vuw.ecs.swen225.gp20.maze.Imposter;
import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelManager;

/**
 * This class handles the replay logic for the Chap's Challenge.
 * Once a save is made by the player, this class gets a save
 * with the help of LevelManager. The game save gets loaded
 * into the game accordingly and the player can do an
 * auto-replay or a step-by-step replay by selecting
 * the "Auto" and "Next Step" buttons from the JMenuBar.
 * The player also has the abilities to increase and
 * decrease replay speed for the recorded game.
 * These can be done by selecting the "Increase Replay Speed"
 * and "Decrease replay speed" from the JMenuBar.
 *
 * @author Abraham Alfred 300509598
 */
public class ReplayGame {
  private Queue<Board.direction> actionQueue = new ArrayDeque<>();
  private Queue<Board.direction> imposterActionQueue = new ArrayDeque<>();
  private List<String> moveList = new ArrayList<>();
  private List<String> impMoveList = new ArrayList<>();
  private LevelManager levelManager = new LevelManager();
  private boolean isFinished = false;
  private boolean autoReplay = false;
  private Board board;
  private long waitTime;
  private Board levelTwoState;

  /**
   * This Constructor handles the initialization
   * of the waitTime which is used as a delay function
   * once the game is replaying.
   */
  public ReplayGame() {
    waitTime = 200;
  }

  /**
   * This method handles the next step functionality
   * of the ReplayGame function.
   */
  public void nextStep() {
    if (actionQueue.size() > 0) {
      Board.direction direction = actionQueue.poll();
      if (imposterActionQueue.size() > 0) {
        board.moveImp(imposterActionQueue.poll().toString(),
                        Imposter.findCurrentImp(board.getBaseBoard()).getImp());
      }
      if (actionQueue.size() == 0) {
        isFinished = true;
      }
      if (direction == null) {
        board = levelTwoState;
      } else {
        board.Move(direction);
      }
    } else {
      isFinished = true;
    }
  }

  /**
   * This method gets the moves from the moveList
   * and adds them accordingly to the actionQueue.
   */
  public void addDirectionToQueue() {
    for (int i = 0; i < moveList.size(); i++) {
      switch (moveList.get(i)) {
        default:
          actionQueue.add(Board.direction.DOWN);
          break;
        case "UP":
          actionQueue.add(Board.direction.UP);
          break;
        case "LEFT":
          actionQueue.add(Board.direction.LEFT);
          break;
        case "RIGHT":
          actionQueue.add(Board.direction.RIGHT);
          break;
      }
    }
  }

  /**
   * This method gets the moves from the impMoveList
   * and adds them accordingly to the imposterActionQueue.
   */
  public void addImpDirectionToQueue() {
    for (int i = 0; i < impMoveList.size(); i++) {
      switch (moveList.get(i)) {
        default:
          imposterActionQueue.add(Board.direction.DOWN);
          break;
        case "UP":
          imposterActionQueue.add(Board.direction.UP);
          break;
        case "LEFT":
          imposterActionQueue.add(Board.direction.LEFT);
          break;
        case "RIGHT":
          imposterActionQueue.add(Board.direction.RIGHT);
          break;
      }
    }
  }

  /**
   * This method loads the JSON file
   * with the help of the LevelManager class.
   * The LevelManager creates a loader for this method,
   * then the method loadRecord() gets called.
   * A moveList for Chap, an impMoveList for
   * the enemy also get called accordingly.
   * These lists then get the moves from the
   * LevelManager and the directions then can
   * be added to both to chap and enemies
   * actionQueue.
   */
  public void loadGameSave() {
    if (levelManager == null) {
      return;
    }
    levelManager.loadRecord();
    moveList = levelManager.getMoveList();
    impMoveList = levelManager.getImpMoveList();
    board = levelManager.getInitialBoard();
    addDirectionToQueue();
    addImpDirectionToQueue();
  }

  /**
   * Boolean method which returns the autoReplay boolean.
   *
   * @return autoReplay   true if autoreplay, false otherwise
   */
  public boolean autoReplay() {
    return autoReplay;
  }

  /**
   * Long method which returns the waitTime long.
   *
   * @return waitTime     the wait time
   */
  public long getWaitTime() {
    return waitTime;
  }

  /**
   * This method increases the wait time by 100.
   */
  public void increaseWaitTime() {
    if (waitTime < 1000) {
      waitTime += 100;
    }
  }

  /**
   * This method decreases the wait time by 100.
   */
  public void decreaseWaitTime() {
    if (waitTime > 200) {
      waitTime -= 100;
    }
  }

  /**
   * This method is a toggle for switching between
   * auto-replay and step-by-step.
   */
  public void toggleAutoReplay() {
    autoReplay = !autoReplay;
  }

  /**
   * Getter method for Board.
   *
   * @return board      the board that is being replayed
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Getter method for isFinished.
   *
   * @return isFinished     true if the replay is finished, false otherwise
   */
  public boolean isFinished() {
    return isFinished;
  }

  /**
   * Getter method for isAutoReplay.
   *
   * @return autoReplay    the autoReplay field
   */
  public boolean isAutoReplay() {
    return autoReplay;
  }

  /**
   * Getter method for the actionQueue.
   *
   * @return actionQueue     the list of chap's actions
   */
  public Queue getActionQueue() {
    return actionQueue;
  }

  /**
   * Getter method for imposterActionQueue.
   *
   * @return imposterActionQueue     the list of imposter's actions
   */
  public Queue getImposterActionQueue() {
    return imposterActionQueue;
  }
}