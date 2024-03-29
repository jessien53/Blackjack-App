import java.util.ArrayList;

class BlackjackGameLogic {
    // Determines the winner between the player and the dealer based on their hands
    public String whoWon(ArrayList<Card> playerHand, ArrayList<Card> dealerHand) {
        int playerTotal = handTotal(playerHand); // Total value of player's hand
        int dealerTotal = handTotal(dealerHand); // Total value of dealer's hand

        if (playerTotal > 21) {
            return "dealer"; // Player busts, dealer wins
        } else if (dealerTotal > 21) {
            return "player"; // Dealer busts, player wins
        } else if (playerTotal > dealerTotal) {
            return "player"; // Player has higher hand value, player wins
        } else if (dealerTotal > playerTotal) {
            return "dealer"; // Dealer has higher hand value, dealer wins
        } else {
            return "push"; // Equal hand values, it's a push (tie)
        }
    }

    // Calculates the total value of cards in a hand, considering the possibility of Aces being 1 or 11
    public int handTotal(ArrayList<Card> hand) {
        int total = 0; // Initialize total value of the hand
        int numAces = 0; // Number of Aces in the hand

        // Iterate through each card in the hand
        for (Card card : hand) {
            total += card.value; // Add the value of the card to the total
            if (card.value == 1) { // If the card is an Ace
                numAces++; // Increment the count of Aces
            }
        }

        // Adjust total for Aces if necessary
        while (total <= 11 && numAces > 0) {
            total += 10; // Treat Ace as 11 to avoid busting
            numAces--; // Reduce the count of Aces treated as 11
        }

        return total; // Return the total value of the hand
    }

    // Determines if the dealer should draw another card based on the value of their hand
    public boolean evaluateBankerDraw(ArrayList<Card> hand) {
        int total = handTotal(hand); // Total value of the dealer's hand
        return total <= 16; // Return true if the total is 16 or less, indicating the dealer should draw another card
    }
}
