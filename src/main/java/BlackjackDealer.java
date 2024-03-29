import java.util.ArrayList;
import java.util.Random;

class BlackjackDealer {
    ArrayList<Card> deck;

    // Generates a standard deck of 52 cards, consisting of 13 faces and 4 suits
    public void generateDeck() {
        deck = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10}; // Values for Ace through King

        for (String suit : suits) {
            for (int i = 0; i < values.length; i++) {
                Card card = new Card(suit, values[i]);
                // Set face card values if necessary
                if (i == 10) {
                    card.faceCard = "Q";
                }
                if (i == 11) {
                    card.faceCard = "J";
                }
                if (i == 12) {
                    card.faceCard = "K";
                }
                deck.add(card);
            }
        }
    }

    // Deals a hand of two cards and returns an ArrayList containing those cards
    public ArrayList<Card> dealHand() {
        ArrayList<Card> hand = new ArrayList<>();

        // Ensure there are enough cards in the deck
        if (deckSize() < 2) {
            generateDeck(); // If the deck is empty or almost empty, generate a new deck
            shuffleDeck();  // Shuffle the new deck
        }

        // Deal the first card
        Card card1 = drawOne();
        hand.add(card1);

        // Deal the second card
        Card card2 = drawOne();
        hand.add(card2);

        return hand;
    }

    // Draws one card from the top of the deck and returns it
    public Card drawOne() {
        if (deck == null || deck.isEmpty()) {
            // If the deck is empty or uninitialized, return null or handle the error accordingly
            return null; // or throw new IllegalStateException("Deck is empty");
        }

        // Remove and return the card from the top of the deck
        return deck.remove(0);
    }

    // Shuffles the deck by randomly swapping cards' positions
    public void shuffleDeck() {
        generateDeck(); // Generate a new deck to ensure all cards are present
        Random random = new Random();
        for (int i = 0; i < deck.size(); i++) {
            int r = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(r);
            deck.set(i, randomCard);
            deck.set(r, currCard);
        }
    }

    // Returns the number of cards left in the deck
    // After a call to shuffleDeck(), this should be 52
    public int deckSize() {
        return (deck != null) ? deck.size() : 0;
    }
}
