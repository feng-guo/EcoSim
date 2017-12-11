# EcoSim

## Features

## Graphics
### Wolves
Wolves display different pictures based on its age and if it has eaten something or not.

### Sheep
Wolves display different pictures based on its age and if it has eaten something or not.

### Rabbits
Rabbits display different patterns based on the terrain

### Villagers
Villagers display different pictures based on its age and the terrain

### GUI
Uses JOptionPanes as input boxes.

## Smart moving
### All animals
Can only move once per turn, no teleporting.

### Wolves
Wolves move onto a space if a sheep is directly adjacent to it.

### Herbivores
Herbivores (excluding rabbits) move onto the space with the most nutritous plant directly adjacent to it. They also move in a different direction if they attempt to move onto a space with a wolf.

### Sheep
Sheep try to mate with adjacent sheep. 

## Mating
Babies spawn next to their parents if they are not rabbits. There is no underage sex for villagers. Also all animals can be of a certain gender and must mate with the opposite gender. Rabbits can only occupy 30% of the board to prevent overpopulation.

## Aging
### Animals
Animals age in a linear, piecewise function. Animals can die of old age.

### Plants
Plants age in a parabolic, piecewise function. They have a set age of 10 turns, based on the parabola.

## Eating
Eating meat only nets 60% of the nutrition of an animal because of inefficiencies. Same deal with plants except it's 80%.


## Additions

## New mobs/entities
### Rabbits
Rabbits are herbivores that move around the map randomly. They have less health than all other organisms due to their smaller size. Rabbits are not intelligent and therefore are not affected by the intelligence feature. Rabbits, when mating, spawn randomly around the maps to simulate the nature of rabbits: mating like crazy so there are many rabbit children. The colour of the rabbits are also dependent on the terrain the simulation takes place in.

### Pigs
Pigs are herbivores that go after nutritious plants

### Cows
Cows are herbivores that go after nutritious plants

## Villagers
Villagers move randomly around the map. They can till the soil and plant seeds around them, based on a random chance. They murder whatever was previously on the space unless it was another villager. Villagers age slower than animals and generally have more health than any animal. Villagers have a baby and adult model, which also changes based on what terrain it is. Villagers also nurture each other if they cannot mate, as well as feed other animals if the villager is not hungry. Villagers murder plants and wolves for food in addition to other animals, however.

## Creepers
Creepers are actually plants that just explode after 3 turns. Everything around it disappears to simulate a Creeper explosion.

## Plants
There are different models on plants to model different nutritious values. Examples include apples, wheat, sugarcane etc.

## Terrain
There are preset demos of which you can try out. This determines the values of the boards to accurately represent the ecosystem. In addition, you can change the terrain yourself to change the appearance of villagers and rabbits. 
