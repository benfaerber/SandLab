package sand.model;
import java.awt.*;
import java.util.*;

import sand.view.SandDisplay;

public class SandLab
{
  //Step 4,6
  //add constants for particle types here
  public static final int EMPTY = 0;
  public static final int METAL = 1;
  public static final int SAND = 2;
  public static final int WATER = 3;
  public static final int TREE = 4;
  public static final int LEAF = 5;
  public static final int BEEHIVE = 6;
  public static final int BEE = 7;
  
  //do not add any more fields below
  private int[][] grid;
  private SandDisplay display;
  
  
  /**
   * Constructor for SandLab
   * @param numRows The number of rows to start with
   * @param numCols The number of columns to start with;
   */
  public SandLab(int numRows, int numCols)
  {
    String[] names;
    // Change this value to add more buttons
    //Step 4,6
    names = new String[8];
    // Each value needs a name for the button
    names[EMPTY] = "Empty";
    names[METAL] = "Metal";
    names[SAND] = "Sand";
    names[WATER] = "Water";
    names[TREE] = "Tree";
    
    //1. Add code to initialize the data member grid with same dimensions
    this.grid = new int[numRows][numCols];
    
    display = new SandDisplay("Falling Sand", numRows, numCols, names);
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool)
  {
    //2. Assign the values associated with the parameters to the grid
   grid[row][col] = tool;
  }

  //copies each element of grid into the display
  public void updateDisplay()
  {
      //Step 3
	  for (int i = 0; i < grid.length; i++)
	  {
		  for (int j = 0; j < grid[i].length; j++)
		  {
			  Color c = null;
			  if (grid[i][j] == EMPTY)
				  c = Color.black;
			  else if (grid[i][j] == METAL)
				  c = Color.GRAY;
			  else if (grid[i][j] == SAND)
				  c = Color.yellow;
			  display.setColor(i, j, c);
		  }
	  }
  }

  //Step 5,7
  //called repeatedly.
  //causes one random particle in grid to maybe do something.
  public void step()
  {
    //Remember, you need to access both row and column to specify a spot in the array
    //The scalar refers to how big the value could be
    //int someRandom = (int) (Math.random() * scalar)
    //remember that you need to watch for the edges of the array
    int rows = grid.length;
    int cols = grid[0].length;
    int y = (int) (Math.random()*(rows));
    int x = (int) (Math.random()*(cols));
    
    int point = grid[y][x];
    if (point == SAND && y <= rows-2)
    {
    	if (grid[y+1][x] == EMPTY)
    	{
    		grid[y+1][x] = SAND;
    		grid[y][x] = EMPTY;
    	}
    }
  }
  
  //do not modify this method!
  public void run()
  {
    while (true) // infinite loop
    {
      for (int i = 0; i < display.getSpeed(); i++)
      {
        step();
      }
      updateDisplay();
      display.repaint();
      display.pause(1);  //wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)  //test if mouse clicked
      {
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
      }
    }
  }
}
