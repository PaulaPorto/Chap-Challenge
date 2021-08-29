package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Color;
import java.awt.Dimension; 
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * Class for the help window so it can be more easily modified.
 * 
 * @author Nick Mazey 300473270
 *
 */
public class HelpWindow extends JFrame {

  private static final long serialVersionUID = 8921704604915818340L;

  /**
   * Initialises and draws the help window.
   */
  public HelpWindow() {
    setTitle("Chap's Challenge Help");
    setPreferredSize(new Dimension(500, 500));
    add(genTextPane());
    pack();
    setVisible(true);

  }

  /**
   * Generates the text pane for the help window so it is easier to modify.
   * 
   * @return The Help text pane
   */
  private JScrollPane genTextPane() {
    JTextPane helpPane = new JTextPane();
    helpPane.setBackground(Color.BLACK);
    helpPane.setForeground(Color.WHITE);
    // introduction
    StringBuilder helpText = new StringBuilder(
        "Introduction: \n\nChap's Challenge is a puzzle game in which you play as chap and your go"
        + "al is to collect all the treasure in a level and exit through the locked door. \n\n");
    // Rules
    helpText.append(
        "Rules: \n\nChap is capable of moving up, down, left or right with the corresponding arrow"
        + " keys. Chap is capable of picking up keys, which will be displayed on the GUI. With the"
        + "se keys, Chap can open the corresponding door. Upon opening a door, its corresponding k"
        + "ey is consumed.\n\n Every level has a pre-set timer which counts down so long as the le"
        + "vel isn't paused. Once the timer reaches 0, the game will pause and the level will reset"
        + ", displaying a dialog saying that you ran out of time.");
    helpPane.setEditable(false);
    helpPane.setText(helpText.toString());
    JScrollPane helpPaneScroll = new JScrollPane(helpPane);
    return helpPaneScroll;
  }
}
