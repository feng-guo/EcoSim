import java.util.Scanner;
//To do:
//Add pictures
//Pic
//Add villagers

class Ecosystem {
  public static void main(String[] args) {
    int initialSheep, initialWolves, initialVillagers, initialHealth, plantNutrition, plantSpawnRate, plantSpawnAttempts;
    int length, width;
    Scanner input = new Scanner(System.in);
    Organism e;
    int wolfCount, sheepCount, villagerCount, turnCount;
    String answer;
    boolean intelligence;
    System.out.println("How long do you want the field to be?");
    length = input.nextInt();
    width = length;
    do {
      System.out.println("How nutritious do you want your plants to be (scale of 1-3)?");
      plantNutrition = input.nextInt();
    } while(plantNutrition>3 || plantNutrition<1);
    System.out.println("Do you want the animals to be smart?");
    input.nextLine();
    answer = input.nextLine();
    if (answer.equalsIgnoreCase("yes")) {
      intelligence = true;
    } else {
      intelligence = false;
    }
    MapBoard m = new MapBoard(length, width, plantNutrition, intelligence);
    System.out.println("How many sheep would you like to start out with?");
    initialSheep = input.nextInt();
    System.out.println("How many wolves would you like to start out with?");
    initialWolves = input.nextInt();
    System.out.println("How many villagers would you like to start out with?");
    initialVillagers = input.nextInt();
    System.out.println("What is the initial health of all animals?");
    initialHealth = input.nextInt();
    do {
      System.out.println("What is the rate of which you want plants to spawn (scale 1-3)?");
      plantSpawnRate = input.nextInt();
    } while(plantSpawnRate>3 || plantSpawnRate<1);
    int multiplier = (int)Math.pow(10, (4-plantSpawnRate));
    plantSpawnAttempts = plantSpawnRate * (int)(length*width)/(multiplier);
    
    for (int i=0; i<initialSheep; i++) {
      e = new Sheep(initialHealth);
      e.setMoved(false);
      m.randomizeGender((Animal)e);
      m.randomizeCoordinates(e);
    }
    for (int i=0; i<initialWolves; i++) {
      e = new Wolf(initialHealth);
      e.setMoved(false);
      m.randomizeGender((Animal)e);
      m.randomizeCoordinates(e);
    }
    for (int i=0; i<initialVillagers; i++) {
      e = new Villager(initialHealth*5); //Villagers live longer than animals
      e.setMoved(false);
      m.randomizeGender((Animal)e);
      m.randomizeCoordinates(e);
    }
    for (int i=0; i<=plantSpawnAttempts; i++) {
      m.spawnPlant();
    }
    DisplayGrid grid = new DisplayGrid(m.getBoard());
    turnCount = 0;
    sheepCount = 0;
    wolfCount = 0;
    villagerCount= 0;
    for (int i=0; i<length; i++) {
      for (int j=0; j<width; j++) {
        m.changeMoved(i, j);
        e = m.getOrganism(i, j);
        if (e == null) {
        } else if (e instanceof Sheep) {
          sheepCount++;
        } else if (e instanceof Wolf) {
          wolfCount++;
        } else if (e instanceof Villager) {
          villagerCount++;
        }
      }
    }
    System.out.println("The simulation started with " + sheepCount + " sheep.");
    System.out.println("The simulation started with " + wolfCount + " wolves.");
    System.out.println("The simulation started with " + villagerCount + " villagers.");
    System.out.println("");
    do {
      try{ Thread.sleep(1000); }catch(Exception c) {};
      turnCount++;
      wolfCount = 0;
      sheepCount = 0;
      villagerCount = 0;
      for (int i=0; i<length; i++) {
        for (int j=0; j<width; j++) {
          m.changeHasEaten(i, j);
        }
      }
      for (int i=0; i<plantSpawnAttempts; i++) {
        m.spawnPlant();
      }
      for (int i=0; i<length; i++) {
        for (int j=0; j<width; j++) {
          e = m.getOrganism(i, j);
          if (e != null) {
            if (e instanceof Plant) {
              m.age(e);
            } else if (e instanceof Animal && !e.getMoved()) {
              m.decideMove((Animal)e);
              e.setMoved(true);
              m.age(e);
            } 
          }
        }
      }
      for (int i=0; i<length; i++) {
        for (int j=0; j<width; j++) {
          m.changeMoved(i, j);
          e = m.getOrganism(i, j);
          if (e == null) {
          } else if (e instanceof Sheep) {
            sheepCount++;
          } else if (e instanceof Wolf) {
            wolfCount++;
          } else if (e instanceof Villager) {
            villagerCount++;
          }
        }
      }
      grid.changeGrid(m.getBoard());
      grid.refresh();
      System.out.println("This simulation has taken " + turnCount + " turns.");
      System.out.println("There are " + sheepCount + " sheep left.");
      System.out.println("There are " + wolfCount + " wolves left.");
      System.out.println("There are " + villagerCount + " villagers left.");
      System.out.println("");
    } while(wolfCount != 0 && sheepCount !=0); //I don't care when villagers die out
    System.out.println("This simulation took " + turnCount + " turns.");
    System.out.println("There are " + sheepCount + " sheep left.");
    System.out.println("There are " + wolfCount + " wolves left.");
    System.out.println("There are " + villagerCount + " villagers left.");
  }
}
