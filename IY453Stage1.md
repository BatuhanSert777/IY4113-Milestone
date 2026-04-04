<style>
</style>

**Introduction**

This project presents system design and implementation of a
text based heist game called “The Jewel Heist Adventure”. In this game, the
player takes job in a jewelry store robbery scenario. The player makes choices
that affect how to story continue. Each decision can lead to success, injury or
being arrested by the police. The design includes the game structure, player
actions and possible outcomes using class diagrams and flowcharts.

<style>
</style>

**Program Specification**

The game is a text-based adventure where the player tries to
rob a jewelry store and escape without being caught. based on the selected
choice, the program will progress to different scenarios and endings. The
program ask the user to enter their name at the start of the game. While in the
game, the player makes decisions that affect how the story will be continue.
Some choices direct to success while others may in injury or being arrested by
the police.

The program includes an inventory system that allows the
player to collect and use items. The player’s situation updated as the game
progresses.

The game has four possible endings: a successful escape, an
injured escape, an injured caught and being caught by the police. User input is
checked to make sure only valid choices are accepted.

<style>
</style>

**IPO**

**Inputs**

-Player name

-player
choices (1 or 2)

-puzzle
answers

-save or
load the game

**Processes**

-Display story

-evaluate user inputs

-update player health and inventory

-manage inventory

-execute puzzle and combat

**Outputs**

-updated health and inventory

-display inventory

-final ending message
