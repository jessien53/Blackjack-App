class Card {
    String suit; // Suit of the card
    int value; // Numeric value of the card
    String faceCard; // Special string representation for face cards (e.g., Jack, Queen, King)

    // Constructor to initialize a card with given suit and value
    Card(String theSuit, int theValue) {
        suit = theSuit;
        value = theValue;
    }

    // toString method to aid in getting image for each card
    public String toString() {
        // If the card is a face card (Jack, Queen, King) and faceCard is set
        if (value == 10 && faceCard != null) {
            return faceCard + "-" + suit.charAt(0); // Return face card name and suit abbreviation
        }
        return value + "-" + suit.charAt(0); // Otherwise, return value and suit abbreviation
    }
}
