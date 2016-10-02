import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.LinkedList;
import java.io.File;
import java.io.PrintWriter;
import java.lang.Math;
import java.awt.Color;

import java.io.IOException;
import java.lang.InterruptedException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class CarDetector2 {

  public static void main(String[] args) {

    // the incremented value
    final int JASPER = 10;

    // the amount of pixels of a car (in image)
    //final int CAR_SIZE = 750 / JASPER; // For bar
    final int CAR_SIZE = 30; // For android robot

    // the total amount of parking spots in the parking structure
    final int PARKING_SPOTS = 100;

    // red, green, and blue values that will be stored for prevous and current
    int redValue1 = 0;
    int greenValue1 = 0;
    int blueValue1 = 0;

    int redValue2 = 0;
    int greenValue2 = 0;
    int blueValue2 = 0;

    Color c = null;

    // the values for the two pixels being compared
    double distance1 = 0.0;
    double distance2 = 0.0;

    // maximum difference of the color pixel allowed
    final int DIFFERENCE_THRESHOLD = 50;

    // variable to count the number of car
    int count = 0;

    // number of pixels that are different
    int failCount = 0;

    // variable to hold the difference
    double maxDifference = 0.0;

    // boolean to track if the car passed the top or bottom
    boolean passedTop = false;
    boolean passedBottom = false;

    // boolean to check what direction is the car moving, in or out
    boolean in = false;
    boolean out = false;

    // LinkedList for the original top & bottom row, updating top and bottom row
    LinkedList<Integer> topMaster = new LinkedList<>();
    LinkedList<Integer> bottomMaster = new LinkedList<>();
    LinkedList<Integer> topRow = new LinkedList<>();
    LinkedList<Integer> bottomRow = new LinkedList<>();

    // BufferedImage that process images
    BufferedImage image = null;

    // PrintWriter to write out data
    PrintWriter print = null;

    // open very first image
    try {

      image = ImageIO.read(new File("test.jpg"));
    }

    catch (IOException e) {

      System.out.println("Can't open file");
    }

    // filling in original top row, updating top and bottom rows
    for (int i = 0; i < image.getWidth(); i += JASPER) {

      topMaster.add(image.getRGB(i,0));
      bottomMaster.add(image.getRGB(i,image.getHeight() - 1));
      topRow.add(image.getRGB(i, 0));
      bottomRow.add(image.getRGB(i, image.getHeight()-1));
    }

    // infinite loop to process images
    while (true) {

      // open the image over and over again
      try {

        image = ImageIO.read(new File("test.jpg"));
      }

      catch (IOException e) {

        System.out.println("Can't read file");
      }

      // int to store the current pixel's RGB value
      int rgbValue = 0;

      // for loop to test bottom row for passing
      for (int i = 0; i < image.getWidth() && !passedBottom; i += JASPER) {

        // obtain the rgb value of the current pixel
        rgbValue = image.getRGB(i, image.getHeight() - 1);

        // gets the color and stores it so tha each color can be extracted indv
        c = new Color(image.getRGB(i,image.getHeight() - 1));
        redValue1 = c.getRed();
        greenValue1 = c.getGreen();
        blueValue1 = c.getBlue();

        // gets the color and stores it so that each color can be extractd indiv
        c = new Color(bottomRow.get(i / JASPER));
        redValue2 = c.getRed();
        greenValue2 = c.getGreen();
        blueValue2 = c.getBlue();

        // gets distance from each pixel master and recent picture's pixel
        distance1 = Math.sqrt(Math.pow(redValue1, 2) + Math.pow(greenValue1, 2) +
                    Math.pow(blueValue1, 2));
        distance2 = Math.sqrt(Math.pow(redValue2, 2) + Math.pow(greenValue2, 2) +
                    Math.pow(blueValue2, 2));

        // gets the distance of the two values
        maxDifference = Math.abs(distance1 - distance2);

        // if the distance is more than 50 it counts as fail
        if(maxDifference > DIFFERENCE_THRESHOLD) {

          failCount++;
        }
      }

      // bottom row check if a car passed
      if (failCount > CAR_SIZE && !passedBottom) {

        passedBottom = true;

        // if car didn't pass top row, then it's coming in
        if (!out) {

          in = true;
        }
      }

      failCount = 0;

      // for loop to test top row for passing
      for (int i = 0; i < image.getWidth() && !passedTop; i += JASPER) {

        // obtain the current rgb value
        rgbValue = image.getRGB(i, 0);

        // convert the current pixel's rgb to Color object
        c = new Color(image.getRGB(i,0));
        redValue1 = c.getRed();
        greenValue1 = c.getGreen();
        blueValue1 = c.getBlue();

        // convert the previous pixel's rgb to Color object
        c = new Color(topRow.get(i / JASPER));
        redValue2 = c.getRed();
        greenValue2 = c.getGreen();
        blueValue2 = c.getBlue();

        // gets the distance from each pixel master and recent pictures' pixel
        distance1 = Math.sqrt(Math.pow(redValue1, 2) + Math.pow(greenValue1, 2) +
                    Math.pow(blueValue1, 2));
        distance2 = Math.sqrt(Math.pow(redValue2, 2) + Math.pow(greenValue2, 2) + 
                    Math.pow(blueValue2, 2));

        // gets the distance of the two values
        maxDifference = Math.abs(distance1 - distance2);

        if (maxDifference > DIFFERENCE_THRESHOLD) {

          failCount++;
        }
      }

      // top row check if a car passed
      if (failCount > CAR_SIZE && !passedTop) {

        passedTop = true;

        // if car didn't pass bottom, then it's going out
        if (!in) {

          out = true;
        }
      }

      failCount = 0;

      if (in) {

        System.out.println("It's coming in!");
      }

      else if (out) {

        System.out.println("It's going out!");
      }

      // both rows passed, reset everything
      if (passedBottom && passedTop && in) {

        for (int i = 0; i < image.getWidth(); i += JASPER) {

          // obtain the current pixel's rgb value
          rgbValue = image.getRGB(i, 0);

          // gets the color values of the pixel being scanned
          c = new Color(image.getRGB(i, 0));
          redValue1 = c.getRed();
          greenValue1 = c.getGreen();
          blueValue1 = c.getBlue();

          // gets the color values of the pixel being scanned
          c = new Color(topMaster.get(i / JASPER));
          redValue2 = c.getRed();
          greenValue2 = c.getGreen();
          blueValue2 = c.getBlue();

          // getst he distance from the master and recent picture's pixel
          distance1 = Math.sqrt(Math.pow(redValue1, 2) + Math.pow(greenValue1, 2) +
                      Math.pow(blueValue1, 2));
          distance2 = Math.sqrt(Math.pow(redValue2, 2) + Math.pow(greenValue2, 2) + 
                      Math.pow(blueValue2, 2));

          // calculates the absolute value of the two distances
          maxDifference = Math.abs(distance1 - distance2);

          // if the differnce is more than 50 failcount is increasec
          if (maxDifference > DIFFERENCE_THRESHOLD) {

            failCount++;
          }
        }

        if (failCount < CAR_SIZE) {

          // one car passed
          count++;

          passedBottom = passedTop = false;
          in = out = false;

          // store a new original top row
          topMaster.clear();
          bottomMaster.clear();

          for (int i = 0; i < image.getWidth(); i += JASPER) {

            topMaster.add(image.getRGB(i,0));
            bottomMaster.add(image.getRGB(i,image.getHeight() - 1));
          }

          // print to file
          try {

            print = new PrintWriter("gilman1.txt", "UTF-8");
            print.println(PARKING_SPOTS - count);
            print.println(count);
            print.close();
          }

          catch (FileNotFoundException e) {

            System.out.println("Can't find file");
          }

          catch (UnsupportedEncodingException e) {

            System.out.println("Can't encode file");
          }
        }
      }

      if (passedBottom && passedTop && out) {

        for (int i = 0; i < image.getWidth(); i += JASPER) {

          // obtain the current pixel's rgb value
          rgbValue = image.getRGB(i, 0);

          // gets the color values of the pixel being scanned
          c = new Color(image.getRGB(i, 0));
          redValue1 = c.getRed();
          greenValue1 = c.getGreen();
          blueValue1 = c.getBlue();

          // gets the color values of the pixel being scanned
          c = new Color(bottomMaster.get(i / JASPER));
          redValue2 = c.getRed();
          greenValue2 = c.getGreen();
          blueValue2 = c.getBlue();

          // getst he distance from the master and recent picture's pixel
          distance1 = Math.sqrt(Math.pow(redValue1, 2) + Math.pow(greenValue1, 2) +
                      Math.pow(blueValue1, 2));
          distance2 = Math.sqrt(Math.pow(redValue2, 2) + Math.pow(greenValue2, 2) + 
                      Math.pow(blueValue2, 2));

          // calculates the absolute value of the two distances
          maxDifference = Math.abs(distance1 - distance2);

          // if the differnce is more than 50 failcount is increasec
          if (maxDifference > DIFFERENCE_THRESHOLD) {

            failCount++;
          }
        }

        if (failCount < CAR_SIZE) {

          // one car left
          count--;

          passedBottom = passedTop = false;
          in = out = false;

          // store a new original top row
          topMaster.clear();
          bottomMaster.clear();

          for (int i = 0; i < image.getWidth(); i += JASPER) {

            topMaster.add(image.getRGB(i,0));
            bottomMaster.add(image.getRGB(i,image.getHeight() - 1));
          }

          // print to file
          try {

            print = new PrintWriter("gilman1.txt", "UTF-8");
            print.println(PARKING_SPOTS - count);
            print.println(count);
            print.close();
          }

          catch (FileNotFoundException e) {

            System.out.println("Can't find file");
          }

          catch (UnsupportedEncodingException e) {

            System.out.println("Can't encode file");
          }
        }
      }

      failCount = 0;

      // reset top and bottom rows as preview pixels
      topRow.clear();
      bottomRow.clear();

      for (int i = 0; i < image.getWidth(); i += JASPER) {

        topRow.add(image.getRGB(i, 0));
        bottomRow.add(image.getRGB(i, image.getHeight() - 1));
      }

      // info prints
      System.out.println("count: " + count);
      System.out.println("passes top: " + passedTop);
      System.out.println("passes bottom: " + passedBottom);

      try {

        Thread.sleep(20);
      }

      catch (InterruptedException e) {

        System.out.println("Can't sleep");
      }
    }
  }
}
