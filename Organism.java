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
  public int compareTo(Organism organismTwo) {
    if (health > organismTwo.getHealth()) {
      return 1;
    } else if (health < organismTwo.getHealth()) {
      return -1;
    } else {
      return 0;
    }
  }
  
  //These following methods were supposed to be abstract but I had classes that shouldn't belong in some subclasses
  //Instead dynamic dispatch will cover the method calling
  public boolean getGender() {
    return true;
  }
  public boolean getMoved() {
    return true;
  }
  public boolean getHasEaten() {
    return true;
  }
  public void setGender(boolean gender){};
  public void setMoved(boolean moved){};
  public void setHasEaten(boolean hasEaten){};
}