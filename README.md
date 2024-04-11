# PenguinGame
Project-Based Object Oriented Programming Class\
Virtual World Project\
CSC 203, Fall '22\
Java

# IDE
IntelliJe\
External Libraries: openjdk-18

# Game Description
A single player game, where the user spawns as a Penguin next to an igloo.
The goal is to not be killed by sharks. Sharks can be populated by clicking on a water square,
and automatically navigate towards the penguin. Sharks cause 1 health damage when coming into
contact with the penguin, but the penguin can revive its health by eating the fish, which are
equivalent to the health status of the penguin. If the penguin  is at max health, the player
can go to the igloo to "drop off" said fish. Lastly, birds populate and eat the fish bones,
which populate a new fish!\

To move around: AWSD\
To move view: arrow keys

# Known Bugs
* Birds continuously populate
* Player can get trapped between FISH, BIRD, and SHARK entities and be stuck in continuous loop
* Clicking too fast causes program to fail

# Simulation Rules

There are 8 entities, which act according to the following rules:
1. IGLOO:
    * Remains static.  Does not animate or complete any actions.  
    * Is the destination for Player entities
2. BIRD
    * Is a fish-reviving entity
    * Navigates to the nearest FISH BONE to transform it back to a FISH
3. OBSTACLE
    * ice blocks - entities cannot move through it
    * the ice blocks are not just a background image as it blocks entity paths
4. SHARK
    * navigates towards the player and causes 1 health damage
5. FISH BONES
    * a stump does not animate or complete any actions
    * it is a destination for FAIRY entities
6. FISH
    * fish are stationary and animated
    * Player entities can transform a FISH entity into FISH BONES by eating it
    * BIRD entities can transform FISH BONES back into FISH by reviving it
7. TREE
    * animates but completes no action
