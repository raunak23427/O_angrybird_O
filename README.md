
# Angry Birds Clone - Static GUI Implementation
## CSE201 Advanced Programming Project


### Team Members:

- Raunak Kumar Giri - 2023427
- M Rayhankhan - 2022269


# Angry Birds Clone

## Project Description
This is an Angry Birds-style game developed as part of CSE 201 (Advanced Programming) using Java and LibGDX. The game follows the classic Angry Birds mechanics where players launch birds from a slingshot to destroy structures and eliminate pigs.

## Project Overview
In this game, players use a slingshot to launch different types of birds at structures containing pigs. The objective is to destroy all pigs in each level using a limited number of birds, with each bird having unique abilities and characteristics.

## Project Features
- Multiple bird types with unique abilities
  - Red Bird (standard)
  - Blue Bird 
  - Black Bird 
- Different block materials with varying durability
  - Wood
  - Glass
  - Steel
- Multiple pig types
  - Normal Pig
  - King Pig
  - Soldier Pig
- Physics-based gameplay using Box2D
- Slingshot mechanics with trajectory calculation
- Level progression
- Game state serialization
- Save and restore game functionality
- Multiple levels with increasing difficulty

## Prerequisites
- Java Development Kit (JDK) 11 or higher
- Gradle
- LibGDX
- Box2D Physics Library

## Setup and Installation

### Clone the Repository
```bash
git clone [Your Repository URL]
cd angry-birds-clone
```

### Build the Project
```bash
./gradlew build
```

### Run the Game
```bash
./gradlew run
```

### Run Tests
```bash
./gradlew test
```

## Game Controls
- Click and drag to aim the bird
- Release to launch the bird
- Pause button to pause the game
- Exit button to return to main menu

## Design Patterns Used
- Factory Pattern (for creating different bird and pig types)
- Singleton Pattern (for game state management)
- Strategy Pattern (for bird special abilities)

## Technologies Used
- Java
- LibGDX
- Box2D Physics
- Gradle
- JUnit for testing

## Contributions
### Raunak Kumar Giri - 2023427
- Implemented game physics
- Developed slingshot mechanics
- Created block and pig classes

### M Rayhankhan - 2022269
- Designed UI/UX
- Implemented game state management
- Developed serialization functionality

## License


## Acknowledgments
- LibGDX Documentation
- Course Instructors

## Future Improvements
- Add more bird and pig types
- Enhanced graphics and animations
