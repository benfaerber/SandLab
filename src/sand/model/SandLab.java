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

  public Color modColor(int r, int g, int b)
  {
	  if (r != 0)
		  r += (int) (Math.random()*(5)) - 3;
	  if (g != 0)
		  g += (int) (Math.random()*(5)) - 3;
	  if (b != 0)
		  b += (int) (Math.random()*(5)) - 3;
	  return new Color(r, g, b);
  }
  //copies each element of grid into the display
  public void updateDisplay()
  {
      //Step 3
	  for (int i = 0; i < grid.length; i++)
	  {
		  for (int j = 0; j < grid[i].length; j++)
		  {
			  int point = grid[i][j];
			  Color c = null;
			  switch (point)
			  {
			  case EMPTY:
				  c = new Color(186, 249, 252);
				  break;
			  case METAL:
				  c = modColor(130, 130, 130);
				  break;
			  case SAND:
				  c = modColor(147, 86, 0);
				  break;
			  case WATER:
				  c = modColor(0, 121, 173);
				  break;
			  case TREE:
				  c = modColor(91, 52, 0);
				  break;
			  case LEAF:
				  c = modColor(9, 130, 25);
				  break;
			  case BEEHIVE:
				  c =  modColor(204, 193, 0);
				  break;
			  case BEE:
				  c = modColor(204, 193, 100);
				  break;
			  }
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
    	if (grid[y+1][x] == EMPTY || grid[y+1][x] == WATER)
    	{
    		int temp = grid[y+1][x];
    		grid[y+1][x] = SAND;
    		grid[y][x] = temp;
    	}
    }
    
    if (point == WATER && y <= rows-2)
    {
    	int direction = (int) (Math.random()*(3)) - 1;
    	if (x+direction >= 0 && x+direction < cols)
    	{
        	int check = grid[y+1][x+direction];
        	if (check == EMPTY)
        	{
        		grid[y][x] = EMPTY;
        		grid[y+1][x+direction] = WATER;
        	}
        	else if (grid[y][x+direction] == EMPTY)
        	{
        		grid[y][x] = EMPTY;
        		grid[y][x+direction] = WATER;
        	}
    	}
    	
    }
    
    if (point == TREE && y <= rows-2 && y != 0)
    {
    	if (treeHeight(x, y) > 3)
    	{
    		if (x != 0 && (int) (Math.random()*(10)) == 5)
    			grid[y][x-1] = LEAF;
    		if (x < grid.length-2 && (int) (Math.random()*(10)) == 5)
    			grid[y][x+1] = LEAF;
    	}
    	
    	if (grid[y+1][x] == SAND || grid[y+1][x] == TREE)
    	{
    		if ((int) (Math.random()*(10)) == 5 && grid[y-1][x] == EMPTY)
    		{
    			if (treeHeight(x, y) < 7)
    				grid[y-1][x] = TREE;
    			else if (treeHeight(x, y) == 7)
    				grid[y-1][x] = LEAF;
    		}
    	}
    	else
    	{
    		grid[y][x] = EMPTY;
    	}
    }
    else if (point == TREE)
    {
    	grid[y][x] = EMPTY;
    }
    
    if (point == LEAF && x > 0 && x < grid.length && y > 0 && y < grid.length-2)
    {
    	if (grid[y][x-1] == EMPTY && (int) (Math.random()*(100)) == 5)
		{
    		if (blockCount(BEEHIVE) == 0)
    			grid[y][x-1] = BEEHIVE;
		}
    }
    
    if (point == BEEHIVE && x > 0 && x < grid.length && y > 0 && y < grid.length-2)
    {
    	if (grid[y][x-1] == LEAF || grid[y][x+1] == LEAF)
    	{
    		if ((int) (Math.random()*(100)) == 5 && blockCount(BEE) < 10 && grid[y][x-1] == EMPTY)
    		{
    			grid[y][x-1] = BEE;
    		}
	    		
    	}
    	else
    	{
    		grid[y][x] = EMPTY;
    	}
    }
    
    if (point == BEE)
    {
    	int dire = (int) (Math.random()*(5));
    	System.out.println(dire);
    	if (y > 0 && dire == 0)
    	{
    		if (grid[y-1][x] == EMPTY)
    		{
    			grid[y-1][x] = BEE;
    			grid[y][x] = EMPTY;
    		}
    	}
    		
    	if (y < grid.length-2 && dire == 1)
    	{
    		if (grid[y+1][x] == EMPTY)
    		{
    			grid[y+1][x] = BEE;
    			grid[y][x] = EMPTY;
    		}
    	}
    	
    	if (x > 0 && dire == 2)
    	{
    		if (grid[y][x-1] == EMPTY)
    		{
    			grid[y][x-1] = BEE;
    			grid[y][x] = EMPTY;
    		}
    	}
    	
    	if (x < grid.length-2 && dire == 3)
    	{
    		if (grid[y][x+1] == EMPTY)
    		{
    			grid[y][x+1] = BEE;
    			grid[y][x] = EMPTY;
    		}
    	}
    		
    }
  }
  
  public int treeHeight(int x, int y)
  {
	  int treeCount = 0;
	  for (int i = y; y < grid.length-1; i++)
	  {
		  if (grid[i][x] == TREE)
			  treeCount++;
		  else
			  break;
	  }
	  return treeCount;
  }
  
  public int blockCount(int block)
  {
	  int count = 0;
	  for (int i = 0; i < grid.length; i++)
	  {
		  for (int j = 0; j < grid[i].length; j++)
		  {
			  if (grid[i][j] == block)
				  count++;
		  }
	  }
	  return count;
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
