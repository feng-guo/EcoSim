class Creeper extends Plant {
  //Note that the health of the creeper does nothing of use
  //Also according to the Game Theory youtube channel, creepers are plants
  Creeper() {
    super(100);
  }
  Creeper(int x, int y) {
    super(x, y, 100);
  }
}