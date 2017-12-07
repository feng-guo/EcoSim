class MapBoard {
  private Organism[][] board;
  private int length;
  private int width;
  private int plantNutrition;
  private boolean intelligence;
  
  MapBoard() {
    //Default constructor. Never called
    board = new Organism[25][25];
    this.plantNutrition = 1;
  }
  
  MapBoard(int length, int width, int plantNutrition, boolean intelligence) {
    board = new Organism[length][width];
    this.length = length;
    this.width = width;
    this.plantNutrition = plantNutrition;
    this.intelligence = intelligence;
  }
  
  public Organism[][] getBoard() {
    return board;
  }
  
  public Organism getOrganism(int y, int x) {
    return board[y][x];
  }
  
  public void age(Organism organism) {
    organism.changeAge(1); //organism ages a day
    if (organism instanceof Villager) {
      int healthLost = (int)Math.ceil(organism.getAge()*0.001); //Piecewise aging, except villagers die slower
      organism.changeHealth(-healthLost);
    } else if (organism instanceof Animal) {
      int healthLost = (int)Math.ceil(organism.getAge()*0.01); //Piecewise aging
      organism.changeHealth(-healthLost);
    } else if (organism instanceof Plant) {
      int newHealth = (int) Math.floor(plantNutrition*(-1*organism.getAge()*organism.getAge() + 20*organism.getAge() + organism.getInitialHealth())); //Parabolic piecewise aging
      organism.setHealth(newHealth);
    }
    if (organism.getHealth() < 1) {
      board[organism.getY()][organism.getX()] = null; //Organism has died
    }
  }
  
  public void decideMove(Animal a) {
    //Method decides whether it moves or not
    int decision;
    decision = (int) Math.floor(Math.random() * 10);
    if (decision < 8) {
      directionMove(a); //Actually moves the animal
    }
  }
  
  public void directionMove(Animal a) {
    //Method for deciding where the animal should move
    int direction;
    boolean triedMoving = false;
    //Smart moving for wolves
    //Wolves prefer sheep over rabbits and generally will not chase rabbits because they are hard to spot in the terrain
    if (intelligence) {
      if (a instanceof Wolf) {
        if (a.getX() != 0) {
          if (board[a.getY()][a.getX()-1] != null) {
            if (board[a.getY()][a.getX()-1] instanceof Sheep) {
              moveAnimal(a, 0);
              triedMoving = true;
            }
          }
        }
        if (a.getX() != width-1 && !triedMoving) {
          if (board[a.getY()][a.getX()+1] != null) {
            if (board[a.getY()][a.getX()+1] instanceof Sheep) {
              moveAnimal(a, 1);
              triedMoving = true;
            }
          }
        }
        if (a.getY() != 0 && !triedMoving) {
          if (board[a.getY()-1][a.getX()] != null) {
            if (board[a.getY()-1][a.getX()] instanceof Sheep) {
              moveAnimal(a, 2);
              triedMoving = true;
            }
          }
        }
        if (a.getY() != length-1 && !triedMoving) {
          if (board[a.getY()+1][a.getX()] != null) {
            if (board[a.getY()+1][a.getX()] instanceof Sheep) {
              moveAnimal(a, 3);
              triedMoving = true;
            }
          }
        }
      }
      //Makes sheep want to eat if they are next to food and are hungry
      if (a instanceof Sheep && !triedMoving) {
        int highestNutrition = 0; //Decides what plant to eat, as sheep determines what has the best nutrition
        direction= 5; //Should be changed later on if they can
        if (a.getX() != 0) {
          if (board[a.getY()][a.getX()-1] != null) {
            if (board[a.getY()][a.getX()-1] instanceof Plant && board[a.getY()][a.getX()-1].getHealth() > highestNutrition) {
              highestNutrition = board[a.getY()][a.getX()-1].getHealth();
              direction = 0;
            }
          }
        }
        if (a.getX() != width-1) {
          if (board[a.getY()][a.getX()+1] != null) {
            if (board[a.getY()][a.getX()+1] instanceof Plant && board[a.getY()][a.getX()+1].getHealth() > highestNutrition) {
              highestNutrition = board[a.getY()][a.getX()+1].getHealth();
              direction = 1;
            }
          }
        }
        if (a.getY() != 0) {
          if (board[a.getY()-1][a.getX()] != null) {
            if (board[a.getY()-1][a.getX()] instanceof Plant && board[a.getY()-1][a.getX()].getHealth() > highestNutrition) {
              highestNutrition = board[a.getY()-1][a.getX()].getHealth();
              direction = 2;
            }
          }
        }
        if (a.getY() != length-1) {
          if (board[a.getY()+1][a.getX()] != null) {
            if (board[a.getY()+1][a.getX()] instanceof Plant && board[a.getY()+1][a.getX()].getHealth() > highestNutrition) {
              highestNutrition = board[a.getY()+1][a.getX()].getHealth();
              direction = 3;
            }
          }
        }
        if (direction == 5) {
          //If the direction has not changed, then direction would remain as 5, and therefore must move randomly
          triedMoving = false;
        } else {
          //If direction != 5, then it was changed and then must move in the direction desired
          moveAnimal(a, direction);
          triedMoving = true;
        }
      }
      //Makes sheep want to mate if they have a stable life
      if (a instanceof Sheep && a.getHealth() > 40) {
        if (a.getX() != 0) {
          if (board[a.getY()][a.getX()-1] != null) {
            if (board[a.getY()][a.getX()-1] instanceof Sheep && board[a.getY()][a.getX()-1].getHealth() > 40) {
              moveAnimal(a, 0);
              triedMoving = true;
            }
          }
        }
        if (a.getX() != width-1 && !triedMoving) {
          if (board[a.getY()][a.getX()+1] != null) {
            if (board[a.getY()][a.getX()+1] instanceof Sheep && board[a.getY()][a.getX()+1].getHealth() > 40) {
              moveAnimal(a, 1);
              triedMoving = true;
            }
          }
        }
        if (a.getY() != 0 && !triedMoving) {
          if (board[a.getY()-1][a.getX()] != null) {
            if (board[a.getY()-1][a.getX()] instanceof Sheep && board[a.getY()-1][a.getX()].getHealth() > 40) {
              moveAnimal(a, 2);
              triedMoving = true;
            }
          }
        }
        if (a.getY() != length-1 && !triedMoving) {
          if (board[a.getY()+1][a.getX()] != null) {
            if (board[a.getY()+1][a.getX()] instanceof Sheep && board[a.getY()+1][a.getX()].getHealth() > 40) {
              moveAnimal(a, 3);
              triedMoving = true;
            }
          }
        }
      }
    }
    if (!triedMoving) {
      //Randomly moves in a direction
      //Villagers move randomly because they're dumb in minecraft
      //Rabbits also move randomly because they're even dumber
      direction = (int) Math.floor(Math.random() * 4);
      moveAnimal(a, direction);
    }
  }
  
  public void changeMoved(int y, int x) {
    //Organism has not moved this turn
    if (board[y][x] == null) {
    } else if (board[y][x] instanceof Animal) {
      board[y][x].setMoved(false);
    }
  }
  
  public void changeHasEaten(int y, int x) {
    //Changes the display of the animal
    if (board[y][x] == null) {
    } else if (board[y][x] instanceof Animal) {
      board[y][x].setHasEaten(false);
    }
  }
  
  public void addOrganism(Organism organism) {
    if (board[organism.getY()][organism.getX()] == null) {
      board[organism.getY()][organism.getX()] = organism;
/* This following code was commented out because animals spawn beside their parent
 * However, since rabbits mate like crazy, it really doesn't matter where rabbits spawn */
      
    } else if (board[organism.getY()][organism.getX()] instanceof Plant && !(organism instanceof Plant)) {
      if (organism instanceof Rabbit || organism instanceof Sheep || organism instanceof Villager) {
        //If a baby spawns on food, it consumes it
        organism.changeHealth(board[organism.getY()][organism.getX()].getHealth());
        board[organism.getY()][organism.getX()] = organism;
        organism.setHasEaten(true);
      } else {
        //Wolves aren't vegan
        board[organism.getY()][organism.getX()] = organism;
      }
     
    } else if (!(organism instanceof Plant)) {
      //If a plant tries to spawn on an occupied space, it doesn't spawn
      double boardCounter = 0;
      double area = length*width;
      for (int i=0; i<length; i++) {
        for (int j=0; j<width; j++) {
          if (board[i][j] == null) {
            boardCounter++;
          }
        }
      }
      if ((boardCounter/area) > 0.3) {
        //Allows random spawning until a certain point when it gets tedious
        randomizeCoordinates(organism);
      } else if (boardCounter != area) {
        for (int i=0; i<length; i++) {
          for (int j=0; j<width; j++) {
            if (board[i][j] == null) {
              organism.setX(j);
              organism.setY(i);
              addOrganism(organism);
            }
          }
        }
      }
    }
  }
  
  public void spawnPlant() {
    //Randomizes what plant spawns
    Plant plant;
    int plantType = (int)Math.ceil(Math.random() * 100);
    if (plantType > 95) {
      plant = new SugarCane();
      randomizeCoordinates(plant);
    } else if (plantType > 90) {
      plant = new Corn();
      randomizeCoordinates(plant);
    } else if (plantType > 85) {
      plant = new Wheat();
      randomizeCoordinates(plant);
    } else if (plantType > 80) {
      plant = new Potato();
      randomizeCoordinates(plant);
    } else if (plantType > 75) {
      plant = new Carrot();
      randomizeCoordinates(plant);
    } else if (plantType > 70) {
      plant = new Apple();
      randomizeCoordinates(plant);
    } else if (plantType > 65) {
      plant = new Seeds();
      randomizeCoordinates(plant);
    } else if (plantType > 30) {
      plant = new Grass();
      randomizeCoordinates(plant);
    } else {
      plant = new Weed();
      randomizeCoordinates(plant);
    }
  }
  
  public void randomizeCoordinates(Organism organism) {
    //Randomizes coordinates of an organism trying to spawn
    organism.setY((int)(Math.floor(Math.random() * length)));
    organism.setX((int)(Math.floor(Math.random() * width)));
    addOrganism(organism); //Passes to spawning organism to attempt a spawn
  }
  
  public void randomizeGender(Animal animal) {
    int random = (int)(Math.floor(Math.random() * 2));
    if (random == 0) {
      animal.setGender(true);
    } else {
      animal.setGender(false);
    }
  }
  
  public void changeOrganismCoordinates(Animal organism, int direction) {
    if (direction == 0) {
      organism.moveLeft();
    } else if (direction == 1) {
      organism.moveRight();
    } else if (direction == 2) {
      organism.moveUp();
    } else if (direction == 3) {
      organism.moveDown();
    }
  }
  
  public boolean checkWolvesAround (Animal organism) {
    //This method avoids stackoverflow by seeing if a sheep is somehow surrounded by wolves
    boolean wolvesAround = true;
    try {
      if (organism.getX() > 0) {
        if (!(board[organism.getY()][organism.getX()-1] instanceof Wolf)) {
          return false;
        }
      }
      if (organism.getX() < width-1) {
        if (!(board[organism.getY()][organism.getX()+1] instanceof Wolf)) {
          return false;
        }
      }
      if (organism.getY() > 0) {
        if (!(board[organism.getY()-1][organism.getX()] instanceof Wolf)) {
          return false;
        }
      }
      if (organism.getY () < length-1) {
        if (!(board[organism.getY()+1][organism.getX()] instanceof Wolf)) {
          return false;
        }
      }
      return wolvesAround;
    } catch (NullPointerException e) {
      //If NullPointerException is caught, their is a free space 
      return false;
    }
  }
  
  public void decidePlant(Villager farmer) {
    //Decides whether or not a villager decides to plant
    int decision;
    decision = (int) Math.floor(Math.random() * 10);
    if (decision >= 7) {
      villagerRampage(farmer);
    }
  }
  
  public void villagerRampage (Villager farmer) {
    //Villager goes ham, murders whatever isn't a fellow villager, tills the soil and plants seeds
    Plant plantedSeed;
    if (farmer.getX() > 0) {
      if (board[farmer.getY()][farmer.getX()-1] == null) {
        plantedSeed = new Seeds(farmer.getX()-1, farmer.getY());
        board[farmer.getY()][farmer.getX()-1] = plantedSeed;
      } else if (!(board[farmer.getY()][farmer.getX()-1] instanceof Villager)) {
        plantedSeed = new Seeds(farmer.getX()-1, farmer.getY());
        board[farmer.getY()][farmer.getX()-1] = plantedSeed;
      }
    }
    if (farmer.getX() < width-1) {
      if (board[farmer.getY()][farmer.getX()+1] == null) {
        plantedSeed = new Seeds(farmer.getX()+1, farmer.getY());
        board[farmer.getY()][farmer.getX()+1] = plantedSeed;
      } else if (!(board[farmer.getY()][farmer.getX()+1] instanceof Villager)) {
        plantedSeed = new Seeds(farmer.getX()+1, farmer.getY());
        board[farmer.getY()][farmer.getX()+1] = plantedSeed;
      }
    }
    if (farmer.getY() > 0) {
      if (board[farmer.getY()-1][farmer.getX()] == null) {
        plantedSeed = new Seeds(farmer.getX(), farmer.getY()-1);
        board[farmer.getY()-1][farmer.getX()] = plantedSeed;
      } else if (!(board[farmer.getY()-1][farmer.getX()] instanceof Villager)) {
        plantedSeed = new Seeds(farmer.getX(), farmer.getY()-1);
        board[farmer.getY()-1][farmer.getX()] = plantedSeed;
      }
    } 
    if (farmer.getY() < length-1) {
      if (board[farmer.getY()+1][farmer.getX()] == null) {
        plantedSeed = new Seeds(farmer.getX(), farmer.getY()+1);
        board[farmer.getY()+1][farmer.getX()] = plantedSeed;
      } else if (!(board[farmer.getY()+1][farmer.getX()] instanceof Villager)) {
        plantedSeed = new Seeds(farmer.getX(), farmer.getY()+1);
        board[farmer.getY()+1][farmer.getX()] = plantedSeed;
      }
    } 
  }
  
  public boolean spawnChildren (Animal parent, int spaceX, int spaceY) {
    int parentX = parent.getX();
    int parentY = parent.getY();
    Animal child;
    int childX=0, childY=0;
    try {
      if (parentX > 0) {
        childX = parentX-1;
        childY = parentY;
        if (!(board[childY][childX] instanceof Animal)) {
          if (parent instanceof Sheep) {
            child = new Sheep(20);
          } else if (parent instanceof Wolf) {
            child = new Wolf(20);
          } else if (parent instanceof Rabbit) {
            child = new Rabbit(10);
          } else {
            child = new Villager(20);
          }
          child.setX(childX);
          child.setY(childY);
          randomizeGender(child);
          board[childY][childX] = child;
          return true;
        }
      }
      if (parentX < width-1) {
        childX = parentX+1;
        childY = parentY;
        if (!(board[childY][childX] instanceof Animal)) {
          if (parent instanceof Sheep) {
            child = new Sheep(20);
          } else if (parent instanceof Wolf) {
            child = new Wolf(20);
          } else if (parent instanceof Rabbit) {
            child = new Rabbit(10);
          } else {
            child = new Villager(20);
          }
          child.setX(childX);
          child.setY(childY);
          randomizeGender(child);
          board[childY][childX] = child;
          return true;
        }
      }
      if (parentY > 0) {
        childX = parentX;
        childY = parentY-1;
        if (!(board[childY][childX] instanceof Animal)) {
          if (parent instanceof Sheep) {
            child = new Sheep(20);
          } else if (parent instanceof Wolf) {
            child = new Wolf(20);
          } else if (parent instanceof Rabbit) {
            child = new Rabbit(10);
          } else {
            child = new Villager(20);
          }
          child.setX(childX);
          child.setY(childY);
          randomizeGender(child);
          board[childY][childX] = child;
          return true;
        }
      }
      if (parentY < length-1) {
        childX = parentX;
        childY = parentY+1;
        if (!(board[childY][childX] instanceof Animal)) {
          if (parent instanceof Sheep) {
            child = new Sheep(20);
          } else if (parent instanceof Wolf) {
            child = new Wolf(20);
          } else if (parent instanceof Rabbit) {
            child = new Rabbit(10);
          } else {
            child = new Villager(20);
          }
          child.setX(childX);
          child.setY(childY);
          randomizeGender(child);
          board[childY][childX] = child;
          return true;
        }
      } 
      if (spaceX > 0) {
        childX = spaceX-1;
        childY = spaceY;
        if (!(board[childY][childX] instanceof Animal)) {
          if (parent instanceof Sheep) {
            child = new Sheep(20);
          } else if (parent instanceof Wolf) {
            child = new Wolf(20);
          } else if (parent instanceof Rabbit) {
            child = new Rabbit(10);
          } else {
            child = new Villager(20);
          }
          child.setX(childX);
          child.setY(childY);
          randomizeGender(child);
          board[childY][childX] = child;
          return true;
        }
      }
      if (spaceX < width-1) {
        childX = spaceX+1;
        childY = spaceY;
        if (!(board[childY][childX] instanceof Animal)) {
          if (parent instanceof Sheep) {
            child = new Sheep(20);
          } else if (parent instanceof Wolf) {
            child = new Wolf(20);
          } else if (parent instanceof Rabbit) {
            child = new Rabbit(10);
          } else {
            child = new Villager(20);
          }
          child.setX(childX);
          child.setY(childY);
          randomizeGender(child);
          board[childY][childX] = child;
          return true;
        }
      }
      if (spaceY > 0) {
        childX = spaceX;
        childY = spaceY-1;
        if (!(board[childY][childX] instanceof Animal)) {
          if (parent instanceof Sheep) {
            child = new Sheep(20);
          } else if (parent instanceof Wolf) {
            child = new Wolf(20);
          } else if (parent instanceof Rabbit) {
            child = new Rabbit(10);
          } else {
            child = new Villager(20);
          }
          child.setX(childX);
          child.setY(childY);
          randomizeGender(child);
          board[childY][childX] = child;
          return true;
        }
      }
      if (spaceY < length-1) {
        childX = spaceX;
        childY = spaceY+1;
        if (!(board[childY][childX] instanceof Animal)) {
          if (parent instanceof Sheep) {
            child = new Sheep(20);
          } else if (parent instanceof Wolf) {
            child = new Wolf(20);
          } else if (parent instanceof Rabbit) {
            child = new Rabbit(10);
          } else {
            child = new Villager(20);
          }
          child.setX(childX);
          child.setY(childY);
          randomizeGender(child);
          board[childY][childX] = child;
          return true;
        }
      }
      return false;
    } catch (NullPointerException e) {
      if (parent instanceof Sheep) {
        child = new Sheep(20);
      } else if (parent instanceof Wolf) {
        child = new Wolf(20);
      } else if (parent instanceof Rabbit) {
        child = new Rabbit(10);
      } else {
        child = new Villager(20);
      }
      child.setX(childX);
      child.setY(childY);
      randomizeGender(child);
      board[childY][childX] = child;
      return true;
    }
  }
  
  public boolean rabbitControl() {
    int rabbitCount = 0;
    Organism organism;
    for (int i=0; i<length; i++) {
      for (int j=0; j<width; j++) {
        if (board[i][j] == null) {
        } else if (board[i][j] instanceof Rabbit) {
          rabbitCount++;
        } 
      }
    }
    if (((double)rabbitCount/length*width) < 0.3) {
      return true;
    } else {
      return false;
    }
  }
  
  public void moveAnimal(Animal organism, int direction) {
    //This method executes the actions associated with moving to a specific area
    boolean wolvesAround = checkWolvesAround(organism); //This boolean is necessary to avoid stackoverflow
    
    boolean canMove = false; //Allows the organism to move in the direction desired. Can be changed
    int spaceX, spaceY; //The coordinates of the spot of which an organism wants to move in
    
    //For reference of direction
    //0 = left
    //1 = right
    //2 = up
    //3 = down
    
    if (direction == 0 && organism.getX() != 0) {
      spaceX = organism.getX()-1;
      spaceY = organism.getY();
      canMove = true;
    } else if (direction == 1 && organism.getX() != width-1) {
      spaceX = organism.getX()+1;
      spaceY = organism.getY();
      canMove = true;
    } else if (direction == 2 && organism.getY() != 0) {
      spaceX = organism.getX();
      spaceY = organism.getY()-1;
      canMove = true;
    } else if (direction == 3 && organism.getY() != length-1) {
      spaceX = organism.getX();
      spaceY = organism.getY()+1;
      canMove = true;
    } else {
      //Integers have to be initialized
      //This only triggers when it tries moving out of the border, but canMove is still false
      //-1 because it should break the program, letting me know the program is broken
      spaceX = -1;
      spaceY = -1;
    }
    
    if (canMove) {
      if (organism instanceof Villager) {
        if (board[spaceY][spaceX] == null) {
          board[organism.getY()][organism.getX()] = null;
          changeOrganismCoordinates(organism, direction);
          board[spaceY][spaceX] = organism;
          decidePlant((Villager)organism);
        } else if ((board[spaceY][spaceX] instanceof Sheep || board[spaceY][spaceX] instanceof Rabbit) && organism.getHealth() > 10) {
          board[spaceY][spaceX].changeHealth(10); //Feeds the animal if they're not starving to death
          decidePlant((Villager)organism);
        } else if (board[spaceY][spaceX] instanceof Villager && !(board[spaceY][spaceX].getGender() == organism.getGender())) {
          Animal baby;
          boolean spawned; //States whether or not a baby was spawned successfully
          do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            board[spaceY][spaceX].changeHealth(-10); //Both parties lose health
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned);
        } else if (board[spaceY][spaceX] instanceof Villager) {
          organism.changeHealth(5);
          board[spaceY][spaceX].changeHealth(5);
        } else {
          //Villagers eat everything, including each other
          board[organism.getY()][organism.getX()] = null; //Moves off the space
          changeOrganismCoordinates(organism, direction); //Moves onto the space
          organism.changeHealth(board[organism.getY()][organism.getX()].getHealth()); //Eats the other organism
          board[spaceY][spaceX] = organism; //Moves onto the space
          organism.setHasEaten(true); //Villager has eaten something
          decidePlant((Villager)organism); //Then decides if it wants to go farming
        }
      } else if (board[spaceY][spaceX] instanceof Villager) {
        if (organism instanceof Sheep || organism instanceof Rabbit) {
          organism.changeHealth(10); //Sheep/rabbit gets fed
        } else if (organism instanceof Wolf) {
          //Wolf eats the villager (rip)
          board[organism.getY()][organism.getX()] = null;
          changeOrganismCoordinates(organism, direction);
          organism.changeHealth(board[organism.getY()][organism.getX()].getHealth());
          organism.setHasEaten(true);
          board[spaceY][spaceX] = organism;
        }
      } else if (board[spaceY][spaceX] == null) {
        //Moves onto the empty space
        board[organism.getY()][organism.getX()] = null;
        changeOrganismCoordinates(organism, direction);
        board[spaceY][spaceX] = organism;
      } else if (board[spaceY][spaceX] instanceof Plant) {
        board[organism.getY()][organism.getX()] = null;
        changeOrganismCoordinates(organism, direction);
        if (organism instanceof Sheep || organism instanceof Rabbit) {
          //They eat the plant
          organism.changeHealth(board[organism.getY()][organism.getX()].getHealth());
          board[spaceY][spaceX] = organism;
          organism.setHasEaten(true);
        } else {
          //Wolves aren't vegan
          board[spaceY][spaceX] = organism;
        }
      } else if (board[spaceY][spaceX] instanceof Wolf && organism instanceof Sheep && !wolvesAround) {
        //Sheep avoid wolves
        boolean tried = false;
        if (organism.getX() != 0 && direction != 0) {
          try {
            if (!(board[organism.getY()][organism.getX()-1] instanceof Wolf)) {
              moveAnimal(organism, 0); //Tries to move to the left
              tried = true;
            }
          } catch(NullPointerException e) {
            //If the exception is caught, that would mean the space is free to move in
            moveAnimal(organism, 0);
            tried = true;
          }
        } 
        if (organism.getX() != width-1 && direction!= 1 && !tried) {
          try {
            if (!(board[organism.getY()][organism.getX()+1] instanceof Wolf)) {
              moveAnimal(organism, 1); //Moves right
              tried = true;
            }
          } catch(NullPointerException e) {
            moveAnimal(organism, 1);
            tried = true;
          }
        }
        if (organism.getY() != 0 && direction!= 2 && !tried) {
          try {
            if (!(board[organism.getY()-1][organism.getX()] instanceof Wolf)) {
              moveAnimal(organism, 2); //Moves up
              tried = true;
            }
          } catch(NullPointerException e) {
            moveAnimal(organism, 2);
            tried = true;
          }
        }
        if (organism.getY() != length-1 && direction!= 3 && !tried) {
          try {
            if (!(board[organism.getY()+1][organism.getX()] instanceof Wolf)) {
              moveAnimal(organism, 3); //Moves down
              tried = true;
            }
          } catch(NullPointerException e) {
            moveAnimal(organism, 3);
            tried = true;
          }
        }
      } else if (!(board[spaceY][spaceX] instanceof Wolf) && organism instanceof Wolf) {
        //The board can't be a plant at this point
        //The board has to be an animal so it eats them
        board[organism.getY()][organism.getX()] = null;
        changeOrganismCoordinates(organism, direction);
        organism.changeHealth(board[organism.getY()][organism.getX()].getHealth());
        organism.setHasEaten(true);
        board[spaceY][spaceX] = organism;
      } else if (organism instanceof Rabbit && board[spaceY][spaceX] instanceof Wolf) {
        //Rabbit suicide
        board[organism.getY()][organism.getX()] = null;
        board[spaceY][spaceX].changeHealth(organism.getHealth());
        board[spaceY][spaceX].setHasEaten(true);
      } else if (board[spaceY][spaceX] instanceof Animal && organism instanceof Animal && (organism.getGender() != board[spaceY][spaceX].getGender())) {
        if (organism instanceof Sheep) {
          boolean spawned; //max 6 sheep babies or else the board becomes too wild
           do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            board[spaceY][spaceX].changeHealth(-10);
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned);
        } else if (organism instanceof Wolf) {
          boolean spawned; 
          int counter = 0; //max 4 wolf babies or else the simulation ends too quickly
          do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            counter++;
            board[spaceY][spaceX].changeHealth(-10);
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned && counter<5);
        } else if (organism instanceof Rabbit) {
          boolean rabbitControl = rabbitControl(); //Maximum 5 rabbits
          int counter = 0;
          while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && rabbitControl & counter < 5) {
            Animal baby = new Rabbit(10);
            randomizeGender(baby);
            randomizeCoordinates(baby);
            board[spaceY][spaceX].changeHealth(-5);
            organism.changeHealth(-5);
            rabbitControl = rabbitControl(); //Ensures that rabbits don't crowd out the board
          }
        }
      } else if (board[spaceY][spaceX] instanceof Wolf && organism instanceof Wolf) {
        if (board[spaceY][spaceX].getHealth() > organism.getHealth()) {
          organism.changeHealth(-10);
        } else if (board[spaceY][spaceX].getHealth() < organism.getHealth()) {
          board[spaceY][spaceX].changeHealth(-10);
          if (board[spaceY][spaceX].getHealth() < 1) {
            board[organism.getY()][organism.getX()] = null;
            changeOrganismCoordinates(organism, direction);
            board[organism.getY()][organism.getX()] = organism;
          }
        } else {
          int random = (int)(Math.floor(Math.random() * 2));
          if (random == 0) {
            organism.changeHealth(-10);
          } else {
            board[spaceY][spaceX].changeHealth(-10);
            if (board[spaceY][spaceX].getHealth() < 1) {
              board[organism.getY()][organism.getX()] = null;
              changeOrganismCoordinates(organism, direction);
              board[organism.getY()][organism.getX()] = organism;
            }
          }
        }
      }             
    }
  }
}

