package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import nz.ac.vuw.ecs.swen225.gp20.maze.Keys;

/**
 * Information Panel, used for displaying information on the GUI.
 * @author Nick Mazey 300473270
 * @param <E> - The type of object being displayed in the info panel.
 *
 */
public class InfoPanel<E> extends JPanel {

  private static final long serialVersionUID = -9015101074934321877L;

  private E value; // The value stored by the info panel.
  private String title; // The title of the value stored by the info panel.
  private JLabel titleText; // The title of the info panel
  private JLabel valueText; // The text which displays the value.
  private int textHeight; // The height of the info panel text for scaling.
  private JPanel inventory; // The JPanel used if it is showing the inventory
  private HashMap<Character, ImageIcon> images; // The images used for drawing the keys
  private int scale; // Integer value for scaling.

  /**
   * Initialises the info panel.
   * 
   * @param value      The value to be displayed by the info panel
   * @param title      The label to give to the value stored in the info panel
   * @param textHeight The height of the text in the info panel
   */
  public InfoPanel(E value, String title, int textHeight) {

    this.value = value;
    this.title = title;
    this.textHeight = textHeight;

    setLayout(new GridLayout(0, 1));

    titleText = new JLabel(this.title, JLabel.CENTER);
    valueText = new JLabel(value.toString(), JLabel.CENTER);

    titleText.setFont(new Font("", Font.PLAIN, this.textHeight));
    valueText.setFont(new Font("", Font.PLAIN, this.textHeight));

    add(titleText, BorderLayout.NORTH);
    add(valueText, BorderLayout.SOUTH);

  }

  /**
   * Initialises the info panel with colour.
   * 
   * @param value      The value to be displayed by the info panel
   * @param title      The label to give to the value stored in the info panel
   * @param height     The height of the text in the info panel
   * @param color     The colour of the info panel
   * @param border     The colour of the border of the panel
   * @param textColor The colour of the text of the panel
   */
  public InfoPanel(E value, String title, int height, Color color, Color border, Color textColor) {

    this(value, title, height);

    Border windowBorder = BorderFactory.createLineBorder(border);
    setBorder(windowBorder);
    setBackground(color);

    titleText.setForeground(textColor);
    valueText.setForeground(textColor);

    if (value instanceof List) {
      scale = 150;
      remove(valueText);
      inventory = new JPanel();
      inventory.setLayout(new GridLayout(2, 10));
      add(inventory, BorderLayout.SOUTH);
      inventory.setBackground(null);
      loadImages();
    }
  }

  /**
   * Caches all the images for loading inventory items if it is an inventory panel.
   */
  private void loadImages() {
    char[] chars = { 'R', 'G', 'B', 'Y', 'N' };
    images = new HashMap<Character, ImageIcon>();
    BufferedImage image;
    for (char c : chars) {
      String fileLocation = "";
      switch (c) {
        case 'Y':
          fileLocation = "assets/yellow_key.png";
          break;
        case 'R':
          fileLocation = "assets/red_key.png";
          break;
        case 'G':
          fileLocation = "assets/green_key.png";
          break;
        case 'B':
          fileLocation = "assets/blue_key.png";
          break;
        case 'N':
          fileLocation = "assets/null_key.png";
          break;
        default:
          break;
      }

      try {
        image = ImageIO.read(new File(fileLocation));
        Image resize = image.getScaledInstance(scale, scale, java.awt.Image.SCALE_FAST);
        images.put(c, new ImageIcon(resize));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Draws the images of inventory items on the JPanel.
   */
  @SuppressWarnings("rawtypes")
  private void drawImages() {
    inventory.removeAll();
    Border border = BorderFactory.createLineBorder(Color.WHITE);
    for (Object k : ((List) value)) {
      if (inventory.getComponents().length < 20) {
        JLabel item = new JLabel();
        item.setPreferredSize(new Dimension(inventory.getWidth() / 10, inventory.getHeight() / 2));
        item.setIcon(images.get(Character.toUpperCase(((Keys) k).getColor().charAt(0))));
        item.setBorder(border);
        inventory.add(item);
      } 
    }
    while (inventory.getComponents().length < 20) {
      JLabel filler = new JLabel();
      filler.setPreferredSize(new Dimension(inventory.getWidth() / 10, inventory.getHeight() / 2));
      filler.setBorder(border);
      filler.setIcon(images.get('N'));
      inventory.add(filler);
    }
    if (scale != inventory.getWidth() / 10 && scale != inventory.getHeight() / 2) {
      scale = inventory.getWidth() / 10 < inventory.getHeight() / 2 ? inventory.getWidth() / 10
          : inventory.getHeight() / 2;
      if (scale != 0) {
        loadImages();
      }
    }
  }

  /**
   * Changes the value stored in the info panel.
   * 
   * @param newValue The new value to be returned.
   */
  public void update(E newValue) {
    value = newValue;
    if (value instanceof List) {
      drawImages();
    } else {
      valueText.setText(value.toString());
    }
  }

  /**
   * Returns the value stored in the info panel.
   * 
   * @return the value stored in the info panel.
   */
  public E getValue() {
    return value;
  }
}
