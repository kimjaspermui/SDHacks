import Acme.*;
import objectdraw.*;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CarDetectorGUI extends WindowController implements ActionListener { 

  public static final int APPLET_WIDTH = 500;
  public static final int APPLET_HEIGHT = 600;

  private JButton gilmanTitle = new JButton("Gilman Parking Structure");
  private JButton hopkinsTitle = new JButton("Hopkins Parking Structure");
  private JButton pangeaTitle = new JButton("Pangea Parking Structure");

  private boolean gilman = false;
  private boolean hopkins = false;
  private boolean pangea = false;

  private JButton floorOne = new JButton("Floor 1");
  private JButton floorTwo = new JButton("Floor 2");
  private JButton floorThird = new JButton("Floor 3");
  private JButton floorFourth = new JButton("Floor 4");

  private JPanel northPanel = new JPanel(new GridLayout(3,1));
  private JPanel southPanel = new JPanel(new GridLayout(2,2));

  private FilledRect background;

  private Scanner scanner;

  private Text floorInfo;
  private Text spotInfo1;
  private Text spotInfo2;
  private Text carInfo;

  private static final int INFO_X = 20;
  private static final int INFO_Y = 20;
  private static final int GAP = 30;
  private static final int INFO_SIZE = 40;
  private static final Color INFO_COLOR = Color.WHITE;

  private static final String FLOOR_STRING = "Floor ";
  private static final String SPOT_STRING1 = "Spots left in parking";
  private static final String SPOT_STRING2 = "structure: ";
  private static final String CAR_STRING = "Number of cars: ";

  private int floor = 0;

  public void begin() {

    background = new FilledRect(0, 0, APPLET_WIDTH, APPLET_HEIGHT, canvas);
    floorInfo = new Text("", INFO_X, INFO_Y, canvas);
    floorInfo.setColor(INFO_COLOR);
    floorInfo.setFontSize(INFO_SIZE);
    spotInfo1 = new Text("", INFO_X, floorInfo.getY() + INFO_SIZE + GAP,
    canvas);
    spotInfo1.setColor(INFO_COLOR);
    spotInfo1.setFontSize(INFO_SIZE);
    spotInfo2 = new Text("", INFO_X, spotInfo1.getY() + INFO_SIZE +
    GAP, canvas);
    spotInfo2.setColor(INFO_COLOR);
    spotInfo2.setFontSize(INFO_SIZE);
    carInfo = new Text("", INFO_X, spotInfo2.getY() + INFO_SIZE + GAP,
    canvas);
    carInfo.setColor(INFO_COLOR);
    carInfo.setFontSize(INFO_SIZE);

    gilmanTitle.addActionListener(this);
    northPanel.add(gilmanTitle);

    hopkinsTitle.addActionListener(this);
    northPanel.add(hopkinsTitle);

    pangeaTitle.addActionListener(this);
    northPanel.add(pangeaTitle);

    floorOne.addActionListener(this);
    southPanel.add(floorOne);
    floorTwo.addActionListener(this);
    southPanel.add(floorTwo);
    floorThird.addActionListener(this);
    southPanel.add(floorThird);
    floorFourth.addActionListener(this);
    southPanel.add(floorFourth);

    this.add(northPanel, BorderLayout.NORTH);
    this.add(southPanel, BorderLayout.SOUTH);
  }

  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == gilmanTitle) {
      try {

        scanner = new Scanner(new File("gilman1.txt"), "UTF-8");
      }

      catch (FileNotFoundException exp) {

        System.out.println("Can't read file");

        return;
      }

      floor = 1;

      showInfo();
    }
  }

  private void showInfo() {

    floorInfo.setText(FLOOR_STRING + floor);

    if (scanner.hasNext()) {

      spotInfo1.setText(SPOT_STRING1);
      spotInfo2.setText(SPOT_STRING2 + scanner.nextInt());
    }
    
    if (scanner.hasNext()) {

      carInfo.setText(CAR_STRING + scanner.nextInt());
    }
  }

  public static void main(String[] args) {

    new Acme.MainFrame(new CarDetectorGUI(), args, APPLET_WIDTH, APPLET_HEIGHT);
  }
}
