import java.util.ArrayList;

class BlackjackGame {
    BlackjackDealer theDealer;
    ArrayList<Card> bankerHand;
    ArrayList<Card> playerHand;
    BlackjackGameLogic gameLogic;

    // The amount currently bet by the user
    double currentBet;
    // The total amount of value that the user has won
    double totalWinnings;

    // Constructor to initialize the objects
    public BlackjackGame() {
        // Generate and shuffle deck, deal hands
        theDealer = new BlackjackDealer();
        theDealer.generateDeck();
        theDealer.shuffleDeck();
        bankerHand = theDealer.dealHand();
        playerHand = theDealer.dealHand();
        gameLogic = new BlackjackGameLogic();
    }

    // Method to evaluate winnings based on who won
    // Returns the amount won or lost based on the value in currentBet
    public double evaluateWinnings() {
        // Determine the winner
        String winner = gameLogic.whoWon(playerHand, bankerHand);
        double winnings = 0;

        if (winner.equals("player")) {
            if (playerHasBlackjack()) {
                // Player wins with Blackjack
                winnings = currentBet * 2.5; // Gets their bet back plus 1.5 times their bet
            } else {
                // Regular win without Blackjack
                winnings = currentBet * 2; // Player wins, gets double their bet
            }
        } else if (winner.equals("push")) {
            // Push, player gets their bet back
            winnings = currentBet;
        }

        // Update totalWinnings
        totalWinnings += winnings;
        return winnings;
    }

    // Method to check if the player has blackjack
    boolean playerHasBlackjack() {
        // Check if the player has only two cards
        if (playerHand.size() == 2) {
            // Check if one of the cards is an Ace and the other is a 10, Jack, Queen, or King
            Card firstCard = playerHand.get(0);
            Card secondCard = playerHand.get(1);
            if ((firstCard.value == 1 && secondCard.value == 10) ||
                    (secondCard.value == 1 && firstCard.value == 10)) {
                // Check if the dealer also has blackjack
                Card dealerFirstCard = bankerHand.get(0);
                Card dealerSecondCard = bankerHand.get(1);
                if ((dealerFirstCard.value == 1 && dealerSecondCard.value >= 10) ||
                        (dealerSecondCard.value == 1 && dealerFirstCard.value >= 10)) {
                    return true; // It's a draw
                }
                return true; // Player has blackjack
            }
        }
        return false; // Player does not have blackjack
    }

    // Method to set the current bet amount
    public void setCurrentBet(double curBet) {
        currentBet = curBet;
    }
}
