/*
 * [Organism.java]
 * The super class for all things living
 * Feng Guo
 * 11/23/2017
 */ 


abstract class Organism implements Moveable{
  private int health;
  private int initialHealth; //This is used for plants
  private int x;
  private int y;
  private int age;
  
  Organism (int x, int y, int health) {
    this.health = health; 
    this.initialHealth = health; //Removes a lot of if statements in the MapBoard class
    this.x = x;
    this.y = y;
    this.age = 0;
  }  
  Organism(int health) {
    this.health = health;
    this.age = 0;
  }
  public int getHealth() {
    return health;
  }
  public int getX() {
    return x;
  }
  public int getY() {
    return y;
  }
  public int getAge() {
    return age;
  }
  public int getInitialHealth() {
    return initialHealth;
  }
  public void changeHealth(int health) {
    this.health += health;
  }
  public void setHealth(int health) {
    this.health = health;
  }
  public void setX(int x) {
    this.x = x;
  }
  public void setY(int y) {
    this.y = y;
  }
  public void changeAge(int age) {
    this.age += age;
  }
  public void moveLeft() {
    x--;
  }
  public void moveRight() {
    x++;
  }
  public void moveUp() {
    y--;
  }
  public void moveDown() {
    y++;
  }
  
  abstract boolean getGender();
  abstract boolean getMoved();
  abstract boolean getHasEaten();
  abstract void setGender(boolean gender);
  abstract void setMoved(boolean moved);
  abstract void setHasEaten(boolean hasEaten);
}

abstract class Plant extends Organism {
  Plant(int health) {
    super(health);
  }
  
  Plant(int x, int y, int health) {
    super(x, y, health);
  }
  
  public boolean getGender() {
    //This should never be called for a plant
    return true;
  }
  
  public boolean getMoved() {
    //This should never be called for a plant
    return true;
  }
   public boolean getHasEaten() {
    //This should never be called for a plant
    return true;
  }
  public void setGender(boolean gender){};
  public void setMoved(boolean moved){};
  public void setHasEaten(boolean hasEaten){};
}

class SugarCane extends Plant {
  SugarCane() {
    super(100);
  }
  SugarCane(int x, int y) {
    super(x, y, 100);
  }
}
class Corn extends Plant {  
  Corn() {
    super(75);
  }
  Corn(int x, int y) {
    super(x, y, 75);
  }
}
class Wheat extends Plant {
  Wheat() {
    super(50);
  }
  Wheat(int x, int y) {
    super(x, y, 50);
  }
}
class Potato extends Plant {
  Potato() {
    super(40);
  }
  Potato(int x, int y) {
    super(x, y, 40);
  }
}
class Carrot extends Plant {
  Carrot() {
    super(30);
  }
  Carrot(int x, int y) {
    super(x, y, 30);
  }
}
class Apple extends Plant {
  Apple() {
    super(20);
  }
  Apple(int x, int y) {
    super(x, y, 20);
  }
}
class Seeds extends Plant {
  Seeds() {
    super(15);
  }
  Seeds(int x, int y) {
    super(x, y, 15);
  }
}
class Grass extends Plant {
  Grass() {
    super(10);
  }
  Grass(int x, int y) {
    super(x, y, 10);
  }
}
class Weed extends Plant {
  Weed() {
    super(5);
  }
  Weed(int x, int y) {
    super(x, y, 5);
  }
}

abstract class Animal extends Organism {
  private boolean gender;
  private boolean moved;
  private boolean hasEaten;
  
  Animal(int health) {
    super(health);
    moved = true;
    hasEaten = false;
  }
  
  Animal(int x, int y, int health, boolean gender) {
    super(x, y, health);
    this.gender = gender;
    moved = true;
    hasEaten = false;
  }
  
  public void setGender(boolean gender) {
    this.gender = gender;
  }
  public void setMoved(boolean moved) {
    this.moved = moved;
  }
  public void setHasEaten(boolean hasEaten) {
    this.hasEaten = hasEaten;
  }
  
  public boolean getGender() {
    return gender;
  }
  public boolean getMoved() {
    return moved;
  }
  public boolean getHasEaten() {
    return hasEaten;
  }
}

class Sheep extends Animal {
  Sheep(int health) {
    super(health);
  }
  Sheep(int x, int y, int health, boolean gender) {
    super(x, y, health, gender);
  }
}

class Wolf extends Animal {
  Wolf(int health) {
    super(health);
  }
  Wolf(int x, int y, int health, boolean gender) {
    super(x, y, health, gender);
  }
}

class Villager extends Animal {
  Villager(int health) {
    super(health);
  }
  Villager (int x, int y, int health, boolean gender) {
    super(x, y, health, gender);
  }
}
  