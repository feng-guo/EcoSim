abstract class Plant extends Organism {
  Plant(int health) {
    super(health);
  }
  
  Plant(int x, int y, int health) {
    super(x, y, health);
  }
}

class SugarCane extends Plant {
  SugarCane() {
    super(100);
  }
  SugarCane(int x, int y) {
    super(x, y, 100);
  }
}