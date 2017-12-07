/* Ecosystem Demo
 * 
 */

import java.util.Scanner;
import javax.swing.*;
//To do:
//Add pictures for desert biome (sheep, eatingsheep, wolf, eatingwolf, villager, baby villager, rabbit)

class Ecosystem {
  public static void main(String[] args) {
    //Variable declaration
    int initialSheep, initialRabbits, initialWolves, initialVillagers, initialHealth, plantNutrition, plantSpawnRate, plantSpawnAttempts;
    int length, width; //Pictures can't be scaled normally if length != width. Too much effort to replace width
    Scanner input = new Scanner(System.in);
    Organism organism; //Dummy variable name
    int wolfCount, sheepCount, rabbitCount, villagerCount, animalCount, turnCount;
    String answer, terrain; //Answer for yes or no
    int option; //Answers for option panes
    boolean intelligence;
    Object[] options = {"Field", "Snow", "Desert", "City"}; //Options for the terrain
    MapBoard map; //Map object
    DisplayGrid grid; //Graphics grid
    
    //Start program
    answer = JOptionPane.showInputDialog("How long do you want the field to be?");
    length = Integer.parseInt(answer); //Convert string to int
    width = length;
    option = JOptionPane.showConfirmDialog(null, "Would you like to run a simulation demo (yes/no)", "Confirm", JOptionPane.YES_NO_OPTION);
    if (option == 0) {
      option = JOptionPane.showOptionDialog(null, "What ecosystem would you like?", "Select an option", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
      if (option == 0) {
        JOptionPane.showMessageDialog(null, "The plains has very high plant growth and nutrition. Fit for any organism.");
        plantNutrition = 2; //Setting this to 3 is insanity
        plantSpawnRate = 3;
        initialSheep = (length*width*1)/10;
        initialRabbits = initialSheep;
        initialWolves = initialSheep/10;
        initialVillagers = 10;
        initialHealth = 40; //Pretty fat 
        terrain = "field";
        map = new MapBoard(length, width, plantNutrition, true);
      } else if (option == 1) {
        JOptionPane.showMessageDialog(null, "The tundra is very cold and very few plants grow. Survival is a struggle.");
        plantNutrition = 1; //Plants grow less and are less nutritious
        plantSpawnRate = 1;
        initialSheep = (length*width*1)/10; //Sheep don't live in the arctic
        initialRabbits = initialSheep * 2; //Bunnies actually live in the arctic
        initialWolves = initialSheep/5; //Wolves actually live in the arctic
        initialVillagers = 5; //It's too cold for villagers
        initialHealth = 20; //They're starving in the snow
        terrain = "snow";
        map = new MapBoard(length, width, plantNutrition, true);
      } else if (option == 2) {
        JOptionPane.showMessageDialog(null, "The scorching desert has few, but nutritious plants for all organisms.");
        plantNutrition = 2; //Plants grow better in the desert than the arctic
        plantSpawnRate = 1;
        initialSheep = (length*width)/5; //Sheep don't really live in the desert
        initialRabbits = initialSheep*3 / 2; 
        initialWolves = initialSheep/20; //Less wolves
        initialVillagers = 15; //People like deserts more than the arctic
        initialHealth = 20;
        terrain = "desert";
        map = new MapBoard(length, width, plantNutrition, true);
      } else {
        JOptionPane.showMessageDialog(null, "Modern India. A lot of people and animals.");
        plantNutrition = 2; //Plants can live in the city but hard to reproduce
        plantSpawnRate = 1;
        initialSheep = length*width/20; //Sheep don't really live in the city
        initialRabbits = initialSheep*5/3; //Rabbits live better in the city than sheep
        initialWolves = initialSheep/10;  
        initialVillagers = 30; //Watch the population boom
        initialHealth = 50; //City has a better standard of living than the meadows
        terrain = "field";
        map = new MapBoard(length, width, plantNutrition, true);
      }
    } else {
      //Manual entry
      do {
        answer = JOptionPane.showInputDialog("How nutritious do you want your plants to be (scale of 1-3)?");
        plantNutrition = Integer.parseInt(answer); //Determines the a value of the parabolic growth of plants
      } while(plantNutrition>3 || plantNutrition<1);
      option = JOptionPane.showConfirmDialog(null, "Would you like to have smart animals", "Confirm", JOptionPane.YES_NO_OPTION);
      if (option == 0) {
        intelligence = true;
      } else {
        intelligence = false;
      }
      map = new MapBoard(length, width, plantNutrition, intelligence);
      
      option = JOptionPane.showOptionDialog(null, "What ecosystem would you like?", "Select an option", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
      if (option == 0) {
        terrain = "field";
      } else if (option == 1) {
        terrain = "snow";
      } else if (option == 2) {
        terrain = "desert";
      } else {
        terrain = "field";
      }
      
      answer = JOptionPane.showInputDialog("How many sheep would you like to start out with?");
      initialSheep = Integer.parseInt(answer);
      
      answer = JOptionPane.showInputDialog("How many wolves would you like to start out with?");
      initialWolves = Integer.parseInt(answer);
      
      answer = JOptionPane.showInputDialog("How many rabbits would you like to start out with?");
      initialRabbits = Integer.parseInt(answer);
      
      answer = JOptionPane.showInputDialog("How many villagers would you like to start out with?");
      initialVillagers = Integer.parseInt(answer);
      
      answer = JOptionPane.showInputDialog("What is the initial health of all animals?");
      initialHealth = Integer.parseInt(answer);
      
      JOptionPane.showMessageDialog(null, "The spawnrate is on a scale based on exponents of 10. Please choose wisely");
      do {
         answer = JOptionPane.showInputDialog("What is the spawn rate of plants (1-3)");
         plantSpawnRate = Integer.parseInt(answer);
      } while(plantSpawnRate>3 || plantSpawnRate<1);
    }
    
    //Out of the if statement
    //Determining the number of attempted plant spawns per turn
    int multiplier = (int)Math.pow(10, (4-plantSpawnRate));
    plantSpawnAttempts = plantSpawnRate * (int)Math.ceil((double)(length*width)/(multiplier)); //Weird spawn attempts but it will try to spawn at least once
    
    //Initial spawning of all animals and plants
    for (int i=0; i<initialSheep; i++) {
      organism = new Sheep(initialHealth);
      organism.setMoved(false);
      map.randomizeGender((Animal)organism);
      map.randomizeCoordinates(organism);
    }
    for (int i=0; i<initialWolves; i++) {
      organism = new Wolf(initialHealth);
      organism.setMoved(false);
      map.randomizeGender((Animal)organism);
      map.randomizeCoordinates(organism);
    }
    for (int i=0; i<initialRabbits; i++) {
      organism = new Rabbit(initialHealth*3/4); //Rabbits are small
      organism.setMoved(false);
      map.randomizeGender((Animal)organism);
      map.randomizeCoordinates(organism);
    }
    for (int i=0; i<initialVillagers; i++) {
      organism = new Villager(initialHealth*5); //Villagers live longer than other animals
      organism.setMoved(false);
      map.randomizeGender((Animal)organism);
      map.randomizeCoordinates(organism);
    }
    for (int i=0; i<=plantSpawnAttempts; i++) {
      map.spawnPlant();
    }
    
    //Initializing the display
    grid = new DisplayGrid(map.getBoard());
    grid.setTerrain(terrain);
    
    //Initializing the counters
    turnCount = 0;
    sheepCount = 0;
    wolfCount = 0;
    rabbitCount = 0;
    villagerCount= 0;
    for (int i=0; i<length; i++) {
      for (int j=0; j<width; j++) {
        map.changeMoved(i, j);
        organism = map.getOrganism(i, j);
        if (organism == null) {
        } else if (organism instanceof Sheep) {
          sheepCount++;
        } else if (organism instanceof Wolf) {
          wolfCount++;
        } else if (organism instanceof Rabbit) {
          rabbitCount++;
        } else if (organism instanceof Villager) {
          villagerCount++;
        }
      }
    }
    System.out.println("The simulation started with " + sheepCount + " sheep.");
    System.out.println("The simulation started with " + wolfCount + " wolves.");
    System.out.println("The simulation started with " + rabbitCount + " rabbits.");
    System.out.println("The simulation started with " + villagerCount + " villagers.");
    System.out.println("");
    
    boolean triggeredEnd=false, noMoreAnimals=false, endLoop=false;
    //Actual simulation
    do {
      try{ Thread.sleep(500); }catch(Exception c) {}; //Delay of the board changing
      turnCount++; //Increases the turn count
      
      //Counter reset
      wolfCount = 0;
      sheepCount = 0;
      rabbitCount = 0;
      villagerCount = 0;
      animalCount = 0;
      
      //Changes the display of the animal
      for (int i=0; i<length; i++) {
        for (int j=0; j<width; j++) {
          map.changeHasEaten(i, j);
        }
      }
      //Spawns plants
      for (int i=0; i<plantSpawnAttempts; i++) {
        map.spawnPlant();
      }
      //Allows the organism to move and age
      for (int i=0; i<length; i++) {
        for (int j=0; j<width; j++) {
          organism = map.getOrganism(i, j);
          if (organism != null) {
            if (organism instanceof Plant) {
              map.age(organism);
            } else if (organism instanceof Animal && !organism.getMoved()) {
              map.decideMove((Animal)organism);
              organism.setMoved(true);
              map.age(organism);
            } 
          }
        }
      }
      
      //Counters
      for (int i=0; i<length; i++) {
        for (int j=0; j<width; j++) {
          map.changeMoved(i, j);
          organism = map.getOrganism(i, j);
          if (organism == null) {
          } else if (organism instanceof Sheep) {
            sheepCount++;
            animalCount++;
          } else if (organism instanceof Wolf) {
            wolfCount++;
            animalCount++;
          } else if (organism instanceof Villager) {
            villagerCount++;
            animalCount++;
          } else if (organism instanceof Rabbit) {
            rabbitCount++;
            animalCount++;
          }
        }
      }
      //Passes the updated board to the grid
      grid.changeGrid(map.getBoard());
      grid.refresh();
      //Outputs counters
      System.out.println("This simulation has taken " + turnCount + " turns.");
      System.out.println("There are " + sheepCount + " sheep left.");
      System.out.println("There are " + wolfCount + " wolves left.");
      System.out.println("There are " + rabbitCount + " rabbits left.");
      System.out.println("There are " + villagerCount + " villagers left.");
      System.out.println("");
      if (!triggeredEnd && (wolfCount == 0 || sheepCount == 0)) {
        option = JOptionPane.showConfirmDialog(null, "Sheep or wolves have gone extinct. Would you like to end the program?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
          endLoop = true;
        } else {
          triggeredEnd = true;
        }
      }
      if (animalCount == 0 && !noMoreAnimals) {
        option = JOptionPane.showConfirmDialog(null, "There is nothing but plants left. Would you like to end the program?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
          endLoop = true;
        } else {
          noMoreAnimals = true;
        }
      }
    } while(!endLoop); 
    System.out.println("This simulation took " + turnCount + " turns.");
    System.out.println("There are " + sheepCount + " sheep left.");
    System.out.println("There are " + wolfCount + " wolves left.");
    System.out.println("There are " + rabbitCount + " rabbits left.");
    System.out.println("There are " + villagerCount + " villagers left.");
  }
}
