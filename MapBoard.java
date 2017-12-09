/* [MapBoard.java]
 * The 2D area that can be called and displayed
 */


class MapBoard {
  private Organism[][] board;
  private int length;
  private int width;
  private int plantNutrition;
  private boolean intelligence;
  private boolean oldAge;
  
  MapBoard() {
    //Default constructor. Never called by the Ecosystem class
    board = new Organism[25][25];
    this.length = 25;
    this.width = 25;
    this.plantNutrition = 1;
    this.intelligence = false;
  }
  
  MapBoard(int length, int width, int plantNutrition, boolean intelligence) {
    //Constructor that I call in Ecosystem
    board = new Organism[length][width];
    this.length = length;
    this.width = width;
    this.plantNutrition = plantNutrition;
    this.intelligence = intelligence;
  }
  
  public void setOldAge(boolean oldAge) {
    this.oldAge = oldAge;
  }
  
  public Organism[][] getBoard() {
    //Returns the 2D array to be used in DisplayGrid
    return board;
  }
  
  public Organism getOrganism(int y, int x) {
    //Returns the organism on a specific space for use in Ecosystem
    return board[y][x];
  }
  
  public void age(Organism organism) {
    organism.changeAge(1); //organism ages a specific unit of time (year, day, whatever)
    if (organism instanceof Villager) {
      int healthLost = (int)Math.ceil(organism.getAge()*0.001); //Piecewise aging, except villagers die slower
      organism.changeHealth(-healthLost);
    } else if (organism instanceof Animal) {
      int healthLost = (int)Math.ceil(organism.getAge()*0.01); //Piecewise starving + aging for other animals
      organism.changeHealth(-healthLost);
    } else if (organism instanceof Plant && !(organism instanceof Creeper)) {
      int newHealth = (int) Math.floor(plantNutrition*(-1*organism.getAge()*organism.getAge() + 20*organism.getAge() + organism.getInitialHealth())); //Parabolic piecewise aging
      organism.setHealth(newHealth); //Plants can only live up to 10 days
    }
    if (organism.getHealth() < 1) {
      board[organism.getY()][organism.getX()] = null; //Organism has died from having no health or just too old
    } else if (organism.getAge() == 3 && organism instanceof Creeper) {
      explodeCreeper((Creeper)organism);
    }
    if (oldAge && organism.getAge() > 100) {
      board[organism.getY()][organism.getX()] = null;
    }
  }
  
  public void decideMove(Animal a) {
    //Method decides whether it moves or not randomly
    int decision;
    decision = (int) Math.floor(Math.random() * 10);
    if (decision < 8) {
      directionMove(a); //Passes it to a method that decides what direction the animal moves
    }
  }
  
  public void directionMove(Animal animal) {
    //Method for deciding where the animal should move
    int direction; //Parameter for left, right, up, down
    boolean triedMoving = false; //Doesn't trigger future if statements if it is true
    //Smart moving for wolves
    //Wolves prefer sheep over rabbits and generally will not chase rabbits because they are hard to spot in the terrain
    if (intelligence) {
      //Checks whether or not I want this to run since I ask the user if they want to make the animals smart
      if (animal instanceof Wolf) {
        //Checks the object class
        if (animal.getX() != 0) {
          //Prevents ArrayIndexOutOfBounds
          if (board[animal.getY()][animal.getX()-1] != null) {
            //Prevents NullPointerException
            if (board[animal.getY()][animal.getX()-1] instanceof Sheep) { //Checks to see if an adjacent board is a sheep
              moveAnimal(animal, 0); //Checks what interaction should take place
              triedMoving = true; //Prevents following if statements from triggering
            }
          }
        }
        if (animal.getX() != width-1 && !triedMoving) {
          if (board[animal.getY()][animal.getX()+1] != null) {
            if (board[animal.getY()][animal.getX()+1] instanceof Sheep) {
              moveAnimal(animal, 1);
              triedMoving = true;
            }
          }
        }
        if (animal.getY() != 0 && !triedMoving) {
          if (board[animal.getY()-1][animal.getX()] != null) {
            if (board[animal.getY()-1][animal.getX()] instanceof Sheep) {
              moveAnimal(animal, 2);
              triedMoving = true;
            }
          }
        }
        if (animal.getY() != length-1 && !triedMoving) {
          if (board[animal.getY()+1][animal.getX()] != null) {
            if (board[animal.getY()+1][animal.getX()] instanceof Sheep) {
              moveAnimal(animal, 3);
              triedMoving = true;
            }
          }
        }
      }
      //Makes sheep want to eat if they are next to food and are hungry
      if (animal instanceof Sheep || animal instanceof Cow || animal instanceof Pig && !triedMoving) {
        int highestNutrition = 0; //Decides what plant to eat, as sheep/pigs/cows determines what has the best nutrition
        direction = 5; //Should be changed later on if they actually decide where to move
        if (animal.getX() != 0) {
          if (board[animal.getY()][animal.getX()-1] != null) {
            if (board[animal.getY()][animal.getX()-1] instanceof Plant && board[animal.getY()][animal.getX()-1].getHealth() > highestNutrition) {
              //Checks if it's a plant and if it's the most nutritous one
              highestNutrition = board[animal.getY()][animal.getX()-1].getHealth();
              direction = 0; //Sets the direction left
            }
          }
        }
        if (animal.getX() != width-1) {
          if (board[animal.getY()][animal.getX()+1] != null) {
            if (board[animal.getY()][animal.getX()+1] instanceof Plant && board[animal.getY()][animal.getX()+1].getHealth() > highestNutrition) {
              highestNutrition = board[animal.getY()][animal.getX()+1].getHealth();
              direction = 1; //Sets the direction right
            }
          }
        }
        if (animal.getY() != 0) {
          if (board[animal.getY()-1][animal.getX()] != null) {
            if (board[animal.getY()-1][animal.getX()] instanceof Plant && board[animal.getY()-1][animal.getX()].getHealth() > highestNutrition) {
              highestNutrition = board[animal.getY()-1][animal.getX()].getHealth();
              direction = 2;
            }
          }
        }
        if (animal.getY() != length-1) {
          if (board[animal.getY()+1][animal.getX()] != null) {
            if (board[animal.getY()+1][animal.getX()] instanceof Plant && board[animal.getY()+1][animal.getX()].getHealth() > highestNutrition) {
              highestNutrition = board[animal.getY()+1][animal.getX()].getHealth();
              direction = 3;
            }
          }
        }
        if (direction == 5) {
          //If the direction has not changed, then direction would remain as 5, and therefore must move randomly
          triedMoving = false;
        } else {
          //If direction != 5, then it was changed and then must move in the direction desired
          moveAnimal(animal, direction);
          triedMoving = true;
        }
      }
      //Makes sheep want to mate if they have a stable life
      if (animal instanceof Sheep && animal.getHealth() > 40 && !triedMoving) {
        if (animal.getX() != 0) {
          if (board[animal.getY()][animal.getX()-1] != null) {
            if (board[animal.getY()][animal.getX()-1] instanceof Sheep && board[animal.getY()][animal.getX()-1].getHealth() > 40 && board[animal.getY()][animal.getX()-1].getGender() != animal.getGender()) {
              //Checks if the sheep are healthy and of the opposite sex
              moveAnimal(animal, 0);
              triedMoving = true;
            }
          }
        }
        if (animal.getX() != width-1 && !triedMoving) {
          if (board[animal.getY()][animal.getX()+1] != null) {
            if (board[animal.getY()][animal.getX()+1] instanceof Sheep && board[animal.getY()][animal.getX()+1].getHealth() > 40 && board[animal.getY()][animal.getX()+1].getGender() != animal.getGender()) {
              moveAnimal(animal, 1);
              triedMoving = true;
            }
          }
        }
        if (animal.getY() != 0 && !triedMoving) {
          if (board[animal.getY()-1][animal.getX()] != null) {
            if (board[animal.getY()-1][animal.getX()] instanceof Sheep && board[animal.getY()-1][animal.getX()].getHealth() > 40 && board[animal.getY()-1][animal.getX()].getGender() != animal.getGender()) {
              moveAnimal(animal, 2);
              triedMoving = true;
            }
          }
        }
        if (animal.getY() != length-1 && !triedMoving) {
          if (board[animal.getY()+1][animal.getX()] != null) {
            if (board[animal.getY()+1][animal.getX()] instanceof Sheep && board[animal.getY()+1][animal.getX()].getHealth() > 40 && board[animal.getY()+1][animal.getX()].getGender() != animal.getGender()) {
              moveAnimal(animal, 3);
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
      moveAnimal(animal, direction);
    }
  }
  
  public void changeMoved(int y, int x) {
    //Organism has not moved this turn
    //Resets the value of moved to false
    if (board[y][x] == null) {
    } else if (board[y][x] instanceof Animal) {
      board[y][x].setMoved(false);
    }
  }
  
  public void changeHasEaten(int y, int x) {
    //Changes the display of the animal if it just ate something (wolf and sheep only)
    if (board[y][x] == null) {
      //Prevents NullPointer in case something wrong is passed in
    } else if (board[y][x] instanceof Animal) {
      board[y][x].setHasEaten(false);
    }
  }
  
  public void addOrganism(Organism organism) {
    if (board[organism.getY()][organism.getX()] == null) {
      board[organism.getY()][organism.getX()] = organism;
      /* This following code was commented out because animals spawn beside their parent
       * However, since rabbits mate like crazy, it really doesn't matter where rabbits spawn 
       * Except rabbits now don't spawn like crazy because it just flooded the map*/
      
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
      double boardCounter = 0; //Counts the number of empty spaces
      double area = length*width; //Both variables are doubles so there are decimals
      for (int i=0; i<length; i++) {
        for (int j=0; j<width; j++) {
          if (board[i][j] == null) {
            boardCounter++; //Adds to the counter if the board space is empty
          }
        }
      }
      if ((boardCounter/area) > 0.3) {
        //Allows random spawning until a certain point when it gets tedious
        randomizeCoordinates(organism); //Randomizes a spawn to try again
      } else if (boardCounter != area) {
        //Triggers when only 0.3 of the board is empty
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
    } else if (plantType == 21) {
      plant = new Creeper();
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
    //Since there are only two genders in this world, I can use a boolean for gender
    //Randomizes a gender
    int random = (int)(Math.floor(Math.random() * 2));
    if (random == 0) {
      animal.setGender(true);
    } else {
      animal.setGender(false);
    }
  }
  
  public void changeOrganismCoordinates(Animal organism, int direction) {
    //Changes the coordinates of an organism using methods implemented by moveable
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
          //Whenever it's not a wolf, the sheep is fine
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
      //If NullPointerException is caught, there is a free space 
      return false;
    }
  }
  
  public void explodeCreeper(Creeper creeper) {
    //Makes everything in a small radius disappear
    int creeperX = creeper.getX();
    int creeperY = creeper.getY();
    board[creeper.getY()][creeper.getX()] = null;
    if (creeperX != 0) {
      board[creeperX-1][creeperY] = null;
    }
    if (creeperX != width-1) {
      board[creeperX+1][creeperY] = null;
    }
    if (creeperY != 0) {
      board[creeperX][creeperY-1] = null;
    }
    if (creeperY != length-1) {
      board[creeperX][creeperY+1] = null;
    }
    if (creeperX != 0 && creeperY != 0) {
      board[creeperX-1][creeperY-1] = null;
    }
    if (creeperX != 0 && creeperY != length-1) {
      board[creeperX-1][creeperY+1] = null;
    }
    if (creeperX != width-1 && creeperY != 0) {
      board[creeperX+1][creeperY-1] = null;
    }
    if (creeperX != width-1 && creeperY != length-1) {
      board[creeperX+1][creeperY+1] = null;
    }
  }
  
  public void decidePlant(Villager farmer) {
    //Decides whether or not a villager decides to murder the plants/animals of an adjacent square and till the soil
    int decision;
    decision = (int) Math.floor(Math.random() * 10);
    if (decision >= 7 && farmer.getHealth() > 50) {
      //Villagers must be healthy enough in order to plant seeds
      villagerRampage(farmer); //Calls the method when it wants to start planning
    }
  }
  
  public void villagerRampage (Villager farmer) {
    //Villager goes ham, murders whatever isn't a fellow villager, tills the soil and plants seeds
    Plant plantedSeed;
    if (farmer.getX() > 0) {
      //Prevents ArrayIndexOutOfBounds
      if (board[farmer.getY()][farmer.getX()-1] == null) {
        //Prevents NullPointer
        plantedSeed = new Seeds(farmer.getX()-1, farmer.getY());
        board[farmer.getY()][farmer.getX()-1] = plantedSeed;
      } else if (!(board[farmer.getY()][farmer.getX()-1] instanceof Villager)) {
        //Doesn't kill fellow villagers
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
    //spaceX, spaceY are the coordinates of the second parent
    //This method spawns the children next to its parents. However, rabbits never use this method. If they do somehow use it, I keep the code in there anyway
    int parentX = parent.getX(); //Set the coordinates of the parents instead of using getX() and getY() over and over again
    int parentY = parent.getY();
    Animal child; //Declare the animal class
    int childX=0, childY=0; 
    try {
      if (parentX > 0) {
        childX = parentX-1;
        childY = parentY;
        if (!(board[childY][childX] instanceof Animal)) {
          //Crushes the plant of the 
          if (parent instanceof Sheep) {
            child = new Sheep(20);
          } else if (parent instanceof Wolf) {
            child = new Wolf(20);
          } else if (parent instanceof Rabbit) {
            child = new Rabbit(10);
          } else if (parent instanceof Pig) {
            child = new Pig(20);
          } else if (parent instanceof Cow) {
            child = new Cow(20);
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
          } else if (parent instanceof Pig) {
            child = new Pig(20);
          } else if (parent instanceof Cow) {
            child = new Cow(20);
          } else if (parent instanceof Pig) {
            child = new Pig(20);
          } else if (parent instanceof Cow) {
            child = new Cow(20);
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
          } else if (parent instanceof Pig) {
            child = new Pig(20);
          } else if (parent instanceof Cow) {
            child = new Cow(20);
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
          } else if (parent instanceof Pig) {
            child = new Pig(20);
          } else if (parent instanceof Cow) {
            child = new Cow(20);
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
          } else if (parent instanceof Pig) {
            child = new Pig(20);
          } else if (parent instanceof Cow) {
            child = new Cow(20);
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
          } else if (parent instanceof Pig) {
            child = new Pig(20);
          } else if (parent instanceof Cow) {
            child = new Cow(20);
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
          } else if (parent instanceof Pig) {
            child = new Pig(20);
          } else if (parent instanceof Cow) {
            child = new Cow(20);
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
          } else if (parent instanceof Pig) {
            child = new Pig(20);
          } else if (parent instanceof Cow) {
            child = new Cow(20);
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
    //Spawns rabbits if it is less than 30% of the overall board space
    double rabbitCount = 0;
    Organism organism;
    for (int i=0; i<length; i++) {
      for (int j=0; j<width; j++) {
        if (board[i][j] == null) {
        } else if (board[i][j] instanceof Rabbit) {
          rabbitCount++;
        } 
      }
    }
    if ((rabbitCount/length*width) < 0.3) {
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
        } else if (board[spaceY][spaceX] instanceof Animal && !(board[spaceY][spaceX] instanceof Wolf) && organism.getHealth() > 10) {
          board[spaceY][spaceX].changeHealth(10); //Feeds the animal if the villager is not starving to death
          decidePlant((Villager)organism);
        } else if (board[spaceY][spaceX] instanceof Villager && !(board[spaceY][spaceX].getGender() == organism.getGender()) && organism.getAge() > 10 && board[spaceY][spaceX].getAge() > 10) {
          //Reproducing if they're different genders and both over the age of 10
          Animal baby;
          boolean spawned; //States whether or not a baby was spawned successfully
          do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            board[spaceY][spaceX].changeHealth(-10); //Both parties lose health
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned);
        } else if (board[spaceY][spaceX] instanceof Villager) {
          //Villagers feed children or same gender
          organism.changeHealth(5);
          board[spaceY][spaceX].changeHealth(5);
        } else {
          //Villagers eat everything, excluding each other
          board[organism.getY()][organism.getX()] = null; //Moves off the space
          changeOrganismCoordinates(organism, direction); //Moves onto the space
          organism.changeHealth((board[organism.getY()][organism.getX()].getHealth()*4)/5); //Eats the other organism
          board[spaceY][spaceX] = organism; //Moves onto the space
          organism.setHasEaten(true); //Villager has eaten something
          decidePlant((Villager)organism); //Then decides if it wants to go farming
        }
      } else if (board[spaceY][spaceX] instanceof Villager) {
        if (!(organism instanceof Wolf)) {
          organism.changeHealth(10); //Animal gets fed
        } else if (organism instanceof Wolf) {
          //Wolf eats the villager (rip)
          board[organism.getY()][organism.getX()] = null;
          changeOrganismCoordinates(organism, direction);
          organism.changeHealth((board[organism.getY()][organism.getX()].getHealth()*3)/5);
          organism.setHasEaten(true);
          board[spaceY][spaceX] = organism;
        }
      } else if (board[spaceY][spaceX] == null) {
        //Moves onto the empty space
        board[organism.getY()][organism.getX()] = null;
        changeOrganismCoordinates(organism, direction);
        board[spaceY][spaceX] = organism;
      } else if (board[spaceY][spaceX] instanceof Plant) {
        if (!(board[spaceY][spaceX] instanceof Creeper)) {
          board[organism.getY()][organism.getX()] = null;
          changeOrganismCoordinates(organism, direction);
          if (!(organism instanceof Wolf)) {
            //They eat the plant
            organism.changeHealth((board[organism.getY()][organism.getX()].getHealth()*4)/5); //They don't get all the nutrition
            board[spaceY][spaceX] = organism;
            organism.setHasEaten(true);
          } else {
            //Wolves aren't vegan
            board[spaceY][spaceX] = organism;
          }
        }
      } else if (board[spaceY][spaceX] instanceof Wolf && (organism instanceof Sheep || organism instanceof Cow || organism instanceof Pig) && !wolvesAround) {
        //Sheep/Cows/Pigs avoid wolves
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
        organism.changeHealth((board[organism.getY()][organism.getX()].getHealth()*3)/5);
        organism.setHasEaten(true);
        board[spaceY][spaceX] = organism;
      } else if (organism instanceof Rabbit && board[spaceY][spaceX] instanceof Wolf) {
        //Rabbit suicide
        board[organism.getY()][organism.getX()] = null;
        board[spaceY][spaceX].changeHealth((organism.getHealth()*3)/5);
        board[spaceY][spaceX].setHasEaten(true);
      } else if (board[spaceY][spaceX] instanceof Animal && organism instanceof Animal && (organism.getGender() != board[spaceY][spaceX].getGender())) {
        if (organism instanceof Sheep && board[spaceY][spaceX] instanceof Sheep) {
          boolean spawned; //max 6 sheep babies or else the board becomes too wild
          do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            board[spaceY][spaceX].changeHealth(-10);
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned);
        } else if (organism instanceof Wolf && board[spaceY][spaceX] instanceof Wolf) {
          boolean spawned; 
          int counter = 0; //max 4 wolf babies or else the simulation ends too quickly
          do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            counter++;
            board[spaceY][spaceX].changeHealth(-10);
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned && counter<5);
        } else if (organism instanceof Rabbit && board[spaceY][spaceX] instanceof Rabbit) {
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
        } else if (organism instanceof Cow && board[spaceY][spaceX] instanceof Cow) {
          boolean spawned; 
          do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            board[spaceY][spaceX].changeHealth(-10);
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned);
        } else if (organism instanceof Pig && board[spaceY][spaceX] instanceof Pig) {
          boolean spawned; 
          do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            board[spaceY][spaceX].changeHealth(-10);
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned);
        } 
      } else if (board[spaceY][spaceX] instanceof Wolf && organism instanceof Wolf) {
        int winner = organism.compareTo((Animal)board[spaceY][spaceX]);
        if (winner == 1) {
          board[spaceY][spaceX].changeHealth(-10);
          if (board[spaceY][spaceX].getHealth() < 1) {
            board[organism.getY()][organism.getX()] = null;
            changeOrganismCoordinates(organism, direction);
            board[organism.getY()][organism.getX()] = organism;
          }
        } else if (winner == -1) {
          organism.changeHealth(-10);
          if (organism.getHealth() < 1) {
            board[organism.getY()][organism.getX()] = null;
          }
        } else {
          int random = (int)(Math.floor(Math.random() * 2));
          if (random == 0) {
            organism.changeHealth(-10);
            if (organism.getHealth() < 1) {
              board[organism.getY()][organism.getX()] = null;
            }
          } else {
            board[spaceY][spaceX].changeHealth(-10);
            if (board[spaceY][spaceX].getHealth() < 1) {
              board[organism.getY()][organism.getX()] = null;
              changeOrganismCoordinates(organism, direction);
              board[organism.getY()][organism.getX()] = organism;
            } //End of organism wolf moving
          } //End of board wolf losing
        } //End of random wolf fight
      } //End of long if statement
    } //End of canMove if statement
  } //End of method
} //End of file

