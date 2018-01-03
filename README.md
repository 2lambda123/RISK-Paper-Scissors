# RISK Paper Scissors #
RISK Paper Scissors is a combination of the classic board game RISK and the popular game Rock Paper Scissors. The aim of this combination to **replace the luck-based dice throwing with the strategy of rock paper scissors.** 

[Read more about RISK here.](https://boardgamegeek.com/boardgame/181/risk)

[Read more about Rock Paper Scissors here.](https://en.wikipedia.org/wiki/Rock%E2%80%93paper%E2%80%93scissors)

**DISCLAIMER: I DO NOT OWN RISK OR ROCK PAPER SCISSORS, OBVIOUSLY. This project was made only for entertainment!**

# User Interfaces #

## Splash Screen ##
![Splash Screen](https://i.imgur.com/iHUKuJF.png)

This is the screen the player is greeted with when they start the game. 
- Pressing **start** prompts the player to choose how many computer players they will play against, to choose a name, and gives them their color, then brings up the **gameplay screen**.
- ressing **about** brings up the **about screen**. 
- Pressing **quit** exists the application.

## About Screen ##
![About Screen](https://i.imgur.com/3ktYssK.png)

This screen explains to the player how the game is played. Pressing **back** returns to the **splash screen**.

## Gameplay Screen ##
### Setup Phase ###
![Setup Phase](https://i.imgur.com/bcbXPgL.png)

When the game begins, the players are **assigned territories randomly**. This example is with six players.

### Initial Army Placement Phase ###
![Intial Army Phase](https://i.imgur.com/q5i8HIy.png)

After territories are assigned, the player may **place their initial armies** on their territories; the amount is based on how many players there are,
in accordance with the rules of RISK.

### Player Turn Phase: Placing Armies ###
![Player Turn Phase1](https://i.imgur.com/OdkRgYL.png)

After intial armies are placed, play begins with the player. According to how many territories and continents the player controls,
they are **given armies to place on their territories**. They also **receive a card**, which has a territory and attack type. 
- Clicking **End turn** ends the player's turn and play goes the next player.
- Clicking **View hand** opens the hand window, in which players can 
**turn in 3 cards with matching types or continent to recieve more armies**. 

![Hand Screen](https://i.imgur.com/SiT61GH.png)

### Player Turn Phase: Attacking ###
![Player Turn Phase2](https://i.imgur.com/rad28Jb.png)

After placing armies, the player may **attack as many times as they wish** by clicking on a territory they own and a highlighted territory to attack. After choosing a territory to attack, they **choose how many armies to attack with** (it must be less than the amount of armies on the attacking territory). In this example, the player is **attacking with Ural**.

### Player Turn Phase: Attack Result ###
![Player Turn Phase3](https://i.imgur.com/p8nJrav.png)

After attacking, the player is returned the gameplay screen and the result of the attack is shown. **The player successfully attacked 
Ukraine, which is now green (the player's color).**

After the player finishes attacking, they end their turn and the computers play. If a player's territory is attacked, they must defened it. Here is the gameplay screen after 1 full turn.

![Results](https://i.imgur.com/bcUWctf.png)

## Attack Screen ##

![Attack Screen](https://i.imgur.com/VVSyU86.png)

When a battle between territories includes a human player, the attack screen is shown. The territory on the left is the attacker, and the right territory is the defender. Next to the territory's name is the number of armies left on that territory; the attack ends when either reaches zero. If the player is attacking, they choose an attack type with 1, 2, and 3 keys. If they are defending, they choose their type with 8, 9, and 0. 
