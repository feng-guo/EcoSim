/* [DisplayGrid.java]
 * A Small program for Display a 2D String Array graphically
 * @author Mangat
 */

// Graphics Imports
import javax.swing.*;
import java.awt.*;
import java.io.*;


class DisplayGrid { 

  private JFrame frame;
  private int maxX,maxY, GridToScreenRatio;
  private Organism[][] world;
  private String terrain;
  
  DisplayGrid(Organism[][] w) { 
    this.world = w;
    this.terrain = "Field";
    
    maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
    maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
    GridToScreenRatio = maxY / (world.length+1);  //ratio to fit in screen as square map
    
    System.out.println("Map size: "+world.length+" by "+world[0].length + "\nScreen size: "+ maxX +"x"+maxY+ " Ratio: " + GridToScreenRatio);
    
    this.frame = new JFrame("Map of World");
    
    GridAreaPanel worldPanel = new GridAreaPanel();
    
    frame.getContentPane().add(BorderLayout.CENTER, worldPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    frame.setVisible(true);
  }
  
  public void changeGrid(Organism[][] w) {
    this.world = w;
  }
  
  public void setTerrain(String terrain) {
    this.terrain = terrain;
  }
  
  public void refresh() { 
    frame.repaint();
  }
  
  
  
  class GridAreaPanel extends JPanel {
    public void paintComponent(Graphics g) {        
      super.repaint();
      
      setDoubleBuffered(true); 
      //g.setColor(Color.WHITE);
      Image grass = Toolkit.getDefaultToolkit().getImage("GrassTexture.png");
      
      Image sheep;
      Image eatingSheep;
      Image wolf;
      Image eatingWolf;
      Image dirt;
      if (terrain.equals("field")) {
        sheep = Toolkit.getDefaultToolkit().getImage("Sheep.png");
        eatingSheep = Toolkit.getDefaultToolkit().getImage("EatingSheep.png");
        wolf = Toolkit.getDefaultToolkit().getImage("Wolf.png");
        eatingWolf = Toolkit.getDefaultToolkit().getImage("EatingWolf.png");
        dirt = Toolkit.getDefaultToolkit().getImage("Dirt.jpg");
      } else {
        sheep = Toolkit.getDefaultToolkit().getImage("SnowSheep.png");
        eatingSheep = Toolkit.getDefaultToolkit().getImage("SnowEatingSheep.jpg");
        wolf = Toolkit.getDefaultToolkit().getImage("SnowWolf.png");
        eatingWolf = Toolkit.getDefaultToolkit().getImage("SnowEatingWolf.jpg");
        dirt = Toolkit.getDefaultToolkit().getImage("Snow.jpg");
      }
      
      
      Image plant = Toolkit.getDefaultToolkit().getImage("Plant.jpg");
      Image villager = Toolkit.getDefaultToolkit().getImage("Villager.png");
      Image babyVillager = Toolkit.getDefaultToolkit().getImage("BabyVillager.png");
      Image sugarCane = Toolkit.getDefaultToolkit().getImage("SugarCane.png");;
      Image corn = Toolkit.getDefaultToolkit().getImage("Corn.png");
      Image wheat = Toolkit.getDefaultToolkit().getImage("Wheat.png");
      Image potato = Toolkit.getDefaultToolkit().getImage("Potato.png");
      Image carrot = Toolkit.getDefaultToolkit().getImage("Carrot.png");
      Image apple = Toolkit.getDefaultToolkit().getImage("Apple.png");
      Image seeds = Toolkit.getDefaultToolkit().getImage("Seeds.png");
      Image weed = Toolkit.getDefaultToolkit().getImage("Weed.png");
      
      
      
      for(int i = 0; i<world[0].length;i=i+1) { 
        for(int j = 0; j<world.length;j=j+1) { 
          if (world[i][j] == null) {
            g.drawImage(dirt,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          } else if (world[i][j] instanceof Plant) {
            if (world[i][j] instanceof SugarCane) {
              g.drawImage(sugarCane,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else if (world[i][j] instanceof Corn) {
              g.drawImage(corn,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else if (world[i][j] instanceof Wheat) {
              g.drawImage(wheat,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else if (world[i][j] instanceof Potato) {
              g.drawImage(potato,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else if (world[i][j] instanceof Carrot) {
              g.drawImage(carrot,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else if (world[i][j] instanceof Apple) {
              g.drawImage(apple,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else if (world[i][j] instanceof Seeds) {
              g.drawImage(seeds,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else if (world[i][j] instanceof Weed) {
              g.drawImage(weed,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else {
              g.drawImage(grass,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            }
          } else if (world[i][j] instanceof Wolf) {
            if (world[i][j].getHasEaten()) {
              g.drawImage(eatingWolf,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else {
              g.drawImage(wolf,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            }
          } else if (world[i][j] instanceof Sheep) {
            if (world[i][j].getHasEaten()) {
              g.drawImage(eatingSheep,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else {
              g.drawImage(sheep,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            }
          } else if (world[i][j] instanceof Villager) {
            if (world[i][j].getAge() < 20) {
              g.drawImage(babyVillager,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else {
              g.drawImage(villager,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            }
          }
          //g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
          //g.setColor(Color.BLACK);
          //g.drawRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
        }
      }
    }
  }//end of GridAreaPanel
  
} //end of DisplayGrid
