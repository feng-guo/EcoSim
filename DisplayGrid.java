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
  
  DisplayGrid(Organism[][] w) { 
    this.world = w;
    
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
  
  
  public void refresh() { 
    frame.repaint();
  }
  
  
  
  class GridAreaPanel extends JPanel {
    public void paintComponent(Graphics g) {        
      super.repaint();
      
      setDoubleBuffered(true); 
      //g.setColor(Color.WHITE);
      Image grass = Toolkit.getDefaultToolkit().getImage("GrassTexture.png");
      Image sheep = Toolkit.getDefaultToolkit().getImage("Sheep.png");
      Image eatingSheep = Toolkit.getDefaultToolkit().getImage("EatingSheep.png");
      Image wolf = Toolkit.getDefaultToolkit().getImage("Wolf.png");
      Image eatingWolf = Toolkit.getDefaultToolkit().getImage("EatingWolf.png");
      Image dirt = Toolkit.getDefaultToolkit().getImage("Dirt.jpg");
      Image plant = Toolkit.getDefaultToolkit().getImage("Plant.jpg");
      Image villager = Toolkit.getDefaultToolkit().getImage("Aaron.png");
      
      for(int i = 0; i<world[0].length;i=i+1) { 
        for(int j = 0; j<world.length;j=j+1) { 
          if (world[i][j] == null) {
            g.drawImage(dirt,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          } else if (world[i][j] instanceof Plant) {
            g.drawImage(grass,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
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
            g.drawImage(villager,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          }
          //g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
          //g.setColor(Color.BLACK);
          //g.drawRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
        }
      }
    }
  }//end of GridAreaPanel
  
} //end of DisplayGrid
