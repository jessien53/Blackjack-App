# Blackjack-App
Introduction
This project involves creating an application for playing a game of blackjack against a computer. It utilizes JavaFX for the front end and implements a Java back end according to the specified requirements.

Rules of the Game
The game follows the standard rules of blackjack with the following specifications:

Two players: banker and user.
User interacts with the game through a GUI.
At the start, two cards are dealt to each player, with the first dealer card hidden.
User can choose to "hit" to receive another card or "stay" to end their turn.
User busts if their hand value exceeds 21.
Dealer must hit on hands 16 and lower and stay on hands 17 and higher.
The player with the higher hand wins. A draw occurs if both players have the same value hand.
A player who hits 21 on two cards gets "blackjack" and wins 150% of their bet unless both players hit "blackjack", resulting in a draw.

## Usage
To run the application:

Ensure you have Java and Maven installed.
Clone the repository.
Navigate to the project directory.
Run mvn clean compile exec:java to start the application.
