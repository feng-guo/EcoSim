/* [DisplayGrid.java]
 * A Small program for Display a 2D String Array graphically
 * @author Mangat
 */

// Graphics Imports
//import javax.swing.*;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.BorderLayout;


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
      
      //Ground textures
      Image grass = Toolkit.getDefaultToolkit().getImage("images/Grass.png");
      Image dirt = Toolkit.getDefaultToolkit().getImage("images/Dirt.jpg");
      
      //Cool animal sprites
      Image sheep = Toolkit.getDefaultToolkit().getImage("images/Sheep.png");
      Image babySheep = Toolkit.getDefaultToolkit().getImage("images/BabySheep.png");
      Image eatingSheep = Toolkit.getDefaultToolkit().getImage("images/EatingSheep.png");
      Image wolf = Toolkit.getDefaultToolkit().getImage("images/Wolf.png");
      Image babyWolf = Toolkit.getDefaultToolkit().getImage("images/BabyWolf.png");
      Image eatingWolf = Toolkit.getDefaultToolkit().getImage("images/EatingWolf.png");
      
      //Dynamic animal sprites (Changes based on terrain)
      Image rabbit = Toolkit.getDefaultToolkit().getImage("images/Rabbit.png");
      Image villager = Toolkit.getDefaultToolkit().getImage("images/Villager.png");
      Image babyVillager = Toolkit.getDefaultToolkit().getImage("images/BabyVillager.png");
      
      //Lame animal sprites
      Image cow = Toolkit.getDefaultToolkit().getImage("images/Cow.png");
      Image pig = Toolkit.getDefaultToolkit().getImage("images/Pig.png");
      
      //Dynamic animal sprites
      if (terrain.equals("desert")) {
        dirt = Toolkit.getDefaultToolkit().getImage("images/Sand.jpg");
        grass = Toolkit.getDefaultToolkit().getImage("images/DesertGrass.png");
        rabbit = Toolkit.getDefaultToolkit().getImage("images/DesertRabbit.png");
        villager = Toolkit.getDefaultToolkit().getImage("images/DesertVillager.png");
        babyVillager = Toolkit.getDefaultToolkit().getImage("images/DesertBabyVillager.png");
      } else if (terrain.equals("snow")) {
        dirt = Toolkit.getDefaultToolkit().getImage("images/Snow.jpg");
        grass = Toolkit.getDefaultToolkit().getImage("images/SnowGrass.png");
        rabbit = Toolkit.getDefaultToolkit().getImage("images/SnowRabbit.png");
        villager = Toolkit.getDefaultToolkit().getImage("images/SnowVillager.png");
        babyVillager = Toolkit.getDefaultToolkit().getImage("images/SnowBabyVillager.png");
      } else if (terrain.equals("city")) {
        dirt = Toolkit.getDefaultToolkit().getImage("images/Stone.png");
        grass = Toolkit.getDefaultToolkit().getImage("images/CityGrass.png");
        rabbit = Toolkit.getDefaultToolkit().getImage("images/CityRabbit.png");
        villager = Toolkit.getDefaultToolkit().getImage("images/CityVillager.png");
        babyVillager = Toolkit.getDefaultToolkit().getImage("images/CityVillager.png");
      }
      
      //Boring plant pictures
      Image sugarCane = Toolkit.getDefaultToolkit().getImage("images/SugarCane.png");;
      Image corn = Toolkit.getDefaultToolkit().getImage("images/Corn.png");
      Image wheat = Toolkit.getDefaultToolkit().getImage("images/Wheat.png");
      Image potato = Toolkit.getDefaultToolkit().getImage("images/Potato.png");
      Image carrot = Toolkit.getDefaultToolkit().getImage("images/Carrot.png");
      Image apple = Toolkit.getDefaultToolkit().getImage("images/Apple.png");
      Image seeds = Toolkit.getDefaultToolkit().getImage("images/Seeds.png");
      Image weed = Toolkit.getDefaultToolkit().getImage("images/Weed.png");
      Image creeper = Toolkit.getDefaultToolkit().getImage("images/Creeper.png");
      
      
      
      for(int i = 0; i<world[0].length; i++) { 
        for(int j = 0; j<world.length; j++) { 
          if (world[i][j] == null) {
            g.drawImage(dirt,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          } else if (world[i][j] instanceof Plant) {
            g.drawImage(grass,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
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
            } else if (world[i][j] instanceof Creeper) {
              g.drawImage(creeper,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            }
          } else if (world[i][j] instanceof Wolf) {
            g.drawImage(dirt,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            if (world[i][j].getHasEaten()) {
              g.drawImage(eatingWolf,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else {
              if (world[i][j].getAge() < 10) {
                g.drawImage(babyWolf,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
              } else {
                g.drawImage(wolf,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
              }
            }
          } else if (world[i][j] instanceof Sheep) {
            g.drawImage(dirt,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            if (world[i][j].getHasEaten()) {
              g.drawImage(eatingSheep,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else {
              if (world[i][j].getAge() < 10) {
                g.drawImage(babySheep,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
              } else {
                g.drawImage(sheep,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
              }
            }
          } else if (world[i][j] instanceof Villager) {
            g.drawImage(dirt,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            if (world[i][j].getAge() < 10) {
              g.drawImage(babyVillager,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            } else {
              g.drawImage(villager,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            }
          } else if (world[i][j] instanceof Rabbit) {
            g.drawImage(dirt,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            g.drawImage(rabbit,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          } else if (world[i][j] instanceof Pig) {
            g.drawImage(dirt,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            g.drawImage(pig,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          } else if (world[i][j] instanceof Cow) {
            g.drawImage(dirt,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
            g.drawImage(cow,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          }
          //g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
          //g.setColor(Color.BLACK);
          //g.drawRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
        }
      }
    }
  }//end of GridAreaPanel
  
} //end of DisplayGrid
