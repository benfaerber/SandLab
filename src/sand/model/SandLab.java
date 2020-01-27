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
  public static final int GRASS = 8;
  public static final int CLOUD = 9;
  public static final int NUKE = 10;
  public static final int FIRE = 11;
  
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
    names = new String[12];
    // Each value needs a name for the button
    names[EMPTY] = "Empty";
    names[METAL] = "Metal";
    names[SAND] = "Dirt";
    names[WATER] = "Water";
    names[TREE] = "Tree";
    names[LEAF] = "Leaf";
    names[BEEHIVE] = "Beehive";
    names[BEE] = "Bee";
    names[GRASS] = "Grass";
    names[CLOUD] = "Cloud";
    names[NUKE] = "Nuke";
    names[FIRE] = "Fire";
    
    //1. Add code to initialize the data member grid with same dimensions
    this.grid = new int[numRows][numCols];
    
    // Propagates the floor with sand
    for (int i = 0; i < grid.length; i++)
	{
		for (int j = 1; j < 10; j++)
		{
			grid[grid.length-j][i] = SAND;
		}
	}
    
    display = new SandDisplay("Falling Sand", numRows, numCols, names);
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool)
  {
    //2. Assign the values associated with the parameters to the grid
   grid[row][col] = tool;
  }

  private Color modColor(int r, int g, int b)
  {
	  int mr = r;
	  int mg = g;
	  int mb = b;
	  if (r != 0)
		  mr += random(4) - 2;
	  if (g != 0)
		  mg += random(4) - 2;
	  if (b != 0)
		  mb += random(4) - 2;
	  
	  if (mr > 255 || mr < 0)
		  mr = r;
	  if (mg > 255 || mg < 0)
		  mg = g;
	  if (mb > 255 || mb < 0)
		  mb = r;
	  
	  return new Color(mr, mg, mb);
  }
  
  private Color gradient(int r, int g, int b, int y)
  {
	  r -= (y*4f)-10;
	  return new Color(r, g, b);
  }
  
  private Color randomCloudColor()
  {
	  return new Color(255, random(155), 0);
  }
  
  private int random(int range)
  {
	  return (int) (Math.random()*(range));
  }
  
  private void swap(int x1, int y1, int x2, int y2)
  {
	  int temp = grid[y1][x1];
	  grid[y1][x1] = grid[y2][x2];
	  grid[y2][x2] = temp;
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
			  Color[] colors = new Color[]
			  {
				  gradient(186, 249, 252, i), //EMPTY 
				  modColor(130, 130, 130),  //METAL
				  modColor(147, 86, 0),		//SAND
				  modColor(0, 121, 173),	//WATER
				  modColor(91, 52, 0),		//TREE
				  modColor(9, 130, 25),		//LEAF
				  modColor(204, 193, 0),	//BEEHIVE
				  modColor(204, 193, 100),	//BEE
				  modColor(9, 135, 0),		//GRASS
				  modColor(235, 235, 235),	//CLOUD
				  modColor(0, 43, 0),	//NUKE
				  modColor(255, 0, 0)	//FIRE
			  };
			  
			  if (point != FIRE)
				  display.setColor(i, j, colors[point]);
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
    int y = random(rows);
    int x = random(cols);
    
    int point = grid[y][x];
    if (y <= rows-2)
    {
    	if (point == SAND)
        {
        	if (grid[y+1][x] == EMPTY || grid[y+1][x] == WATER)
        		swap(x, y, x, y+1);
        	
        	if (grid[y+1][x] == SAND && (grid[y-1][x] == EMPTY || grid[y-1][x] == TREE))
        		grid[y][x] = GRASS;
        }
    	
    	if (point == GRASS)
        {
        	if (grid[y+1][x] != SAND || (grid[y-1][x] != EMPTY && grid[y-1][x] != TREE))
        		grid[y][x] = SAND;
        }
    	
    	if (point == WATER)
        {
        	int direction = random(3) - 1;
        	if (x+direction >= 0 && x+direction < cols)
        	{
            	if (grid[y+1][x+direction] == EMPTY)
            		swap(x, y, x+direction, y+1);
            	else if (grid[y][x+direction] == EMPTY)
            		swap(x, y, x+direction, y);
        	}
        	
        }
        
        if (point == TREE && y != 0)
        {
        	if (treeHeight(x, y) > 3)
        	{
        		if (x != 0 && random(10) == 5)
        			grid[y][x-1] = LEAF;
        		if (x < grid.length-2 && random(10) == 5)
        			grid[y][x+1] = LEAF;
        	}
        	
        	if (grid[y+1][x] == GRASS || grid[y+1][x] == TREE)
        	{
        		if (random(10) == 5 && grid[y-1][x] == EMPTY)
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
        
        if (point == LEAF && x > 0 && x < grid.length && y > 0)
        {
        	if (grid[y][x-1] == EMPTY && random(100) == 5)
    		{
        		if (blockCount(BEEHIVE) == 0)
        			grid[y][x-1] = BEEHIVE;
    		}
        }
        
        if (point == BEEHIVE && x > 0 && x < grid.length && y > 0)
        {
        	if (grid[y][x-1] == LEAF || grid[y][x+1] == LEAF)
        	{
        		if (random(100) == 5 && blockCount(BEE) < 10 && grid[y][x-1] == EMPTY)
        			grid[y][x-1] = BEE;    	    		
        	}
        	else
        	{
        		grid[y][x] = EMPTY;
        	}
        }
        
        if (point == BEE)
        {
        	int dire = random(5);
        	int xm = 0;
        	int ym = 0;
        	if (dire == 0 && y > 0)
        		ym = -1;
        	else if (dire == 1 && y < grid.length-2)
        		ym = 1;
        	else if (dire == 2 && x > 0)
        		xm = -1;
        	else if (dire == 3 && x < grid.length-2)
        		xm = 1;
        	
    		if (grid[y+ym][x+xm] == EMPTY)
    			swap(x, y, x+xm, y+ym);
        }
        
        if (point == CLOUD)
        {
        	if (x > grid.length-3)
        	{
        		grid[y][x] = EMPTY;
        	}
        	else
        	{
        		if (blockCount(CLOUD) == 1 && y < 40 & x < 25)
            	{
            		for (int j = 0; j < 5; j++)
            		{
            			for (int i = 0; i < 10; i++)
                		{
            				grid[y-j][x+i] = CLOUD;
                		}
            		}
            	}
            	else
            	{
            		if (grid[y][x+1] == EMPTY)
            			swap(x, y, x+1, y);
                	
                	if (grid[y+1][x] == EMPTY && random(10) == 2)
                		grid[y+1][x] = WATER;
            	}
        	}
        }
        
        if (point == NUKE)
        {
        	if (grid[y+1][x] == EMPTY)
        	{
        		swap(x, y, x, y+1);
        	}
        	else
        	{
        		explosion(x, y);
        		mushroomCloud(x,y);
        	}
        }
        
        if (point == FIRE)
        {
        	if (y < 1)
        		grid[y][x] = EMPTY;
        	
        	if (y > 0 && grid[y-1][x] == EMPTY)
        		swap(x, y, x, y-1);
        }
    }  
  }
  
  public void explosion(int x, int y)
  {
	  int[][] map = new int[][]
	  {
		  new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0},
		  new int[] {0, 0, 1, 1, 1, 1, 1, 0, 0},
		  new int[] {0, 1, 1, 1, 1, 1, 1, 1, 0},
		  new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1},
		  new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1},
		  new int[] {1, 1, 0, 1, 1, 1, 1, 1, 1},
		  new int[] {0, 1, 0, 1, 1, 1, 1, 1, 0},
		  new int[] {0, 0, 1, 1, 1, 1, 1, 0, 0},
		  new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0}
	  };
	  
	  for (int i = map.length-1; i >= 0; i--)
	  {
		  for (int j = 0; j < map[i].length; j++)
		  {
			  if (map[i][j] == 1)
			  {
				  grid[y-(i-(map.length/2))][x+(j-(map.length/2))] = EMPTY;
			  }
		  }
	  }
	  
	  
  }
  
  public void mushroomCloud(int x, int y)
  {
	  int[][] map = new int[][]
	  {
		  new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0},
		  new int[] {0, 0, 1, 1, 1, 1, 1, 0, 0},
		  new int[] {0, 1, 1, 1, 1, 1, 1, 1, 0},
		  new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1},
		  new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1},
		  new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0},
		  new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0},
		  new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0},
		  new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0},
		  new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0},
		  new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0}
	  };
	  
	  for (int i = map.length-1; i >= 0; i--)
	  {
		  for (int j = 0; j < map[i].length; j++)
		  {
			  if (map[i][j] == 1)
			  {
				  int foundX = x+(j-(map.length/2))+1;
				  int foundY = y-(map.length-i)+5;
				  grid[foundY][foundX] = FIRE;
				  display.setColor(foundY, foundX, randomCloudColor());
			  }
		  }
		  updateDisplay();
		  display.repaint();
		  display.pause(100);
	  }
	  
	  display.pause(1000);
	  
	  for (int i = map.length-1; i >= 0; i--)
	  {
		  for (int j = 0; j < map[i].length; j++)
		  {
			  if (map[i][j] == 1)
			  {
				  int foundX = x+(j-(map.length/2))+1;
				  int foundY = y-(map.length-i)+5;
				  grid[foundX][foundY] = EMPTY;
			  }
		  }
	  }
	  updateDisplay();
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
