class MapBoard {
  private Organism[][] board;
  private int length;
  private int width;
  private int plantNutrition;
  private boolean intelligence;
  
  MapBoard() {
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
  
  public void age(Organism o) {
    o.changeAge(1);
    if (o instanceof Villager) {
      int healthLost = (int)Math.ceil(o.getAge()*0.001); //Piecewise aging
      o.changeHealth(-healthLost);
    } else if (o instanceof Animal) {
      int healthLost = (int)Math.ceil(o.getAge()*0.01); //Piecewise aging
      o.changeHealth(-healthLost);
    } else if (o instanceof Plant) {
      int newHealth = (int) Math.floor(plantNutrition*(-1*o.getAge()*o.getAge() + 20*o.getAge() + o.getInitialHealth())); //Parabolic piecewise aging
      o.setHealth(newHealth);
    }
    if (o.getHealth() < 1) {
      board[o.getY()][o.getX()] = null;
    }
  }
  
  public void decideMove(Animal a) {
    int decision;
    decision = (int) Math.floor(Math.random() * 10);
    if (decision < 8) {
      directionMove(a);
    }
  }
  
  public void directionMove(Animal a) {
    int direction;
    boolean triedMoving = false;
    //Smart moving for wolves
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
        int highestNutrition = 0;
        direction= 5; //Should be changed later on
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
      direction = (int) Math.floor(Math.random() * 4);
      moveAnimal(a, direction);
    }
  }
  
  public void changeMoved(int y, int x) {
    if (board[y][x] == null) {
    } else if (board[y][x] instanceof Animal) {
      board[y][x].setMoved(false);
    }
  }
  
  public void changeHasEaten(int y, int x) {
    if (board[y][x] == null) {
    } else if (board[y][x] instanceof Animal) {
      board[y][x].setHasEaten(false);
    }
  }
  
  public void addOrganism(Organism o) {
    if (board[o.getY()][o.getX()] == null) {
      board[o.getY()][o.getX()] = o;
    } else if (board[o.getY()][o.getX()] instanceof Plant && !(o instanceof Plant)) {
      if (o instanceof Sheep || o instanceof Villager) {
        //If a baby spawns on food, it consumes it
        o.changeHealth(board[o.getY()][o.getX()].getHealth());
        board[o.getY()][o.getX()] = o;
        o.setHasEaten(true);
      } else {
        //Wolves aren't vegan
        board[o.getY()][o.getX()] = o;
      }
    } else if (!(o instanceof Plant)) {
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
        randomizeCoordinates(o);
      } else if (boardCounter != area) {
        for (int i=0; i<length; i++) {
          for (int j=0; j<width; j++) {
            if (board[i][j] == null) {
              o.setX(j);
              o.setY(i);
              addOrganism(o);
            }
          }
        }
      }
    }
  }
  
  public void spawnPlant() {
    Plant e;
    int plantType = (int)Math.ceil(Math.random() * 100);
    if (plantType > 95) {
      e = new SugarCane();
      randomizeCoordinates(e);
    } else if (plantType > 90) {
      e = new Corn();
      randomizeCoordinates(e);
    } else if (plantType > 85) {
      e = new Wheat();
      randomizeCoordinates(e);
    } else if (plantType > 80) {
      e = new Potato();
      randomizeCoordinates(e);
    } else if (plantType > 75) {
      e = new Carrot();
      randomizeCoordinates(e);
    } else if (plantType > 70) {
      e = new Apple();
      randomizeCoordinates(e);
    } else if (plantType > 65) {
      e = new Seeds();
      randomizeCoordinates(e);
    } else if (plantType > 30) {
      e = new Grass();
      randomizeCoordinates(e);
    } else {
      e = new Weed();
      randomizeCoordinates(e);
    }
  }
  
  public void randomizeCoordinates(Organism organism) {
    organism.setY((int)(Math.floor(Math.random() * length)));
    organism.setX((int)(Math.floor(Math.random() * width)));
    addOrganism(organism);
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
      return false;
    }
  }
  
  public void decidePlant(Villager farmer) {
    int decision;
    decision = (int) Math.floor(Math.random() * 10);
    if (decision >= 7) {
      villagerRampage(farmer);
    }
  }
  
  public void villagerRampage (Villager farmer) {
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
  
  public void moveAnimal(Animal organism, int direction) {
    //This method executes the actions associated with moving to a specific area
    boolean wolvesAround = checkWolvesAround(organism); //This boolean is necessary to avoid stackoverflow
    
    boolean canMove = false; //Allows the organism to move in the direction desired
    int spaceX, spaceY; //
    
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
      if(organism instanceof Villager) {
        if (board[spaceY][spaceX] == null) {
          board[organism.getY()][organism.getX()] = null;
          changeOrganismCoordinates(organism, direction);
          board[spaceY][spaceX] = organism;
          decidePlant((Villager)organism);
        } else if (board[spaceY][spaceX] instanceof Sheep) {
          board[spaceY][spaceX].changeHealth(10);
          decidePlant((Villager)organism);
        } else if (board[spaceY][spaceX] instanceof Villager && !(board[spaceY][spaceX].getGender() == organism.getGender())) {
          Animal baby;
          boolean spawned;
          do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            board[spaceY][spaceX].changeHealth(-10);
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned);
        } else {
          //Villagers eat everything but sheep
          board[organism.getY()][organism.getX()] = null;
          changeOrganismCoordinates(organism, direction);
          organism.changeHealth(board[organism.getY()][organism.getX()].getHealth());
          board[spaceY][spaceX] = organism;
          organism.setHasEaten(true);
          decidePlant((Villager)organism);
        }
      } else if (board[spaceY][spaceX] instanceof Villager) {
        if (organism instanceof Sheep) {
          organism.changeHealth(10);
        } else if (organism instanceof Wolf) {
          board[organism.getY()][organism.getX()] = null;
          changeOrganismCoordinates(organism, direction);
          organism.changeHealth(board[organism.getY()][organism.getX()].getHealth());
          organism.setHasEaten(true);
          board[spaceY][spaceX] = organism;
        }
      } else if (board[spaceY][spaceX] == null) {
        board[organism.getY()][organism.getX()] = null;
        changeOrganismCoordinates(organism, direction);
        board[spaceY][spaceX] = organism;
      } else if (board[spaceY][spaceX] instanceof Plant) {
        board[organism.getY()][organism.getX()] = null;
        changeOrganismCoordinates(organism, direction);
        if (organism instanceof Sheep) {
          organism.changeHealth(board[organism.getY()][organism.getX()].getHealth());
          board[spaceY][spaceX] = organism;
          organism.setHasEaten(true);
        } else {
          //Wolves aren't vegan
          board[spaceY][spaceX] = organism;
        }
      } else if (board[spaceY][spaceX] instanceof Wolf && organism instanceof Sheep && !wolvesAround) {
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
      } else if (board[spaceY][spaceX] instanceof Sheep && organism instanceof Wolf) {
        board[organism.getY()][organism.getX()] = null;
        changeOrganismCoordinates(organism, direction);
        organism.changeHealth(board[organism.getY()][organism.getX()].getHealth());
        organism.setHasEaten(true);
        board[spaceY][spaceX] = organism;
      } else if (board[spaceY][spaceX] instanceof Animal && organism instanceof Animal && (organism.getGender() != board[spaceY][spaceX].getGender())) {
        if (organism instanceof Sheep) {
          boolean spawned; //max 6 sheep babies or else the board becomes too wild
           do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            board[spaceY][spaceX].changeHealth(-10);
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned);
        } else if (organism instanceof Wolf) {
          boolean spawned; //max 6 wolf babies or else the simulation ends too quickly
          do {
            spawned = spawnChildren((Animal)organism, spaceX, spaceY);
            board[spaceY][spaceX].changeHealth(-10);
            organism.changeHealth(-10);
          } while (board[spaceY][spaceX].getHealth() > 20 && organism.getHealth() > 20 && spawned);
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

