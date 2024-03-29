import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BlackjackTests {
    // Card class tests
    @Test
    void testCardInitialization() {
        Card card = new Card("Hearts", 10);
        assertEquals("Hearts", card.suit);
        assertEquals(10, card.value);
        assertNull(card.faceCard); // Ensure faceCard is initially null
    }

    @Test
    void testToString() {
        // Test toString() method for numeric value cards
        Card numericCard = new Card("Diamonds", 7);
        assertEquals("7-D", numericCard.toString());

        // Test toString() method for face cards
        Card faceCard = new Card("Clubs", 10);
        faceCard.faceCard = "Q"; // Set faceCard to "Queen"
        assertEquals("Q-C", faceCard.toString());

        // Test toString() method for non-face cards with value 10
        Card tenValueCard = new Card("Spades", 10);
        assertEquals("10-S", tenValueCard.toString());
    }

    @Test
    void testToStringWithNullFaceCard() {
        // Test toString() method when faceCard is null
        Card card = new Card("Hearts", 10);
        assertEquals("10-H", card.toString());
    }

    // BlackjackGame tests
    @Test
    void testEvaluateWinnings_PlayerWinsWithoutBlackjack() {
        BlackjackGame game = new BlackjackGame();
        game.setCurrentBet(20);
        game.playerHand.clear();
        game.playerHand.add(new Card("Hearts", 8)); // Player hand: 8
        game.playerHand.add(new Card("Diamonds", 10)); // Player hand: 8, 10
        game.bankerHand.clear();
        game.bankerHand.add(new Card("Spades", 7)); // Banker hand: 7
        game.bankerHand.add(new Card("Clubs", 6)); // Banker hand: 7, 6
        double winnings = game.evaluateWinnings();
        assertEquals(40, winnings); // Player wins, gets double their bet
    }

    @Test
    void testEvaluateWinnings_PlayerWinsWithBlackjack() {
        BlackjackGame game = new BlackjackGame();
        game.setCurrentBet(20);
        game.playerHand.clear();
        game.playerHand.add(new Card("Hearts", 1)); // Player hand: Ace
        game.playerHand.add(new Card("Diamonds", 10)); // Player hand: Ace, 10 (Blackjack)
        game.bankerHand.clear();
        game.bankerHand.add(new Card("Spades", 7)); // Banker hand: 7
        game.bankerHand.add(new Card("Clubs", 6)); // Banker hand: 7, 6
        double winnings = game.evaluateWinnings();
        assertEquals(50, winnings); // Player wins with Blackjack, gets 2.5 times their bet
    }

    @Test
    void testEvaluateWinnings_Push() {
        BlackjackGame game = new BlackjackGame();
        game.setCurrentBet(20);
        game.playerHand.clear();
        game.playerHand.add(new Card("Hearts", 10)); // Player hand: 10
        game.playerHand.add(new Card("Diamonds", 9)); // Player hand: 10, 9
        game.bankerHand.clear();
        game.bankerHand.add(new Card("Spades", 10)); // Banker hand: 10
        game.bankerHand.add(new Card("Clubs", 9)); // Banker hand: 10, 9
        double winnings = game.evaluateWinnings();
        assertEquals(20, winnings); // Push, player gets their bet back
    }

    @Test
    void testEvaluateWinnings_Loss() {
        BlackjackGame game = new BlackjackGame();
        game.setCurrentBet(20);
        game.playerHand.clear();
        game.playerHand.add(new Card("Hearts", 5)); // Player hand: 10
        game.playerHand.add(new Card("Diamonds", 9)); // Player hand: 10, 9
        game.bankerHand.clear();
        game.bankerHand.add(new Card("Spades", 10)); // Banker hand: 10
        game.bankerHand.add(new Card("Clubs", 9)); // Banker hand: 10, 9
        double winnings = game.evaluateWinnings();
        assertEquals(0, winnings); // Loss, player does not get their bet back
    }

    @Test
    void testEvaluateWinnings_TotalWinningsUpdated() {
        BlackjackGame game = new BlackjackGame();
        game.setCurrentBet(30);
        game.playerHand.clear();
        game.playerHand.add(new Card("Hearts", 5)); // Player hand: 5
        game.playerHand.add(new Card("Diamonds", 9)); // Player hand: 5, 9
        game.bankerHand.clear();
        game.bankerHand.add(new Card("Spades", 9)); // Banker hand: 9
        game.bankerHand.add(new Card("Clubs", 4)); // Banker hand: 9, 4
        double winnings = game.evaluateWinnings();
        assertEquals(60, winnings); // Player wins, total winnings should be updated to 60
        assertEquals(60, game.totalWinnings);
    }

    @Test
    void testPlayerHasBlackjack_True() {
        BlackjackGame game = new BlackjackGame();
        game.playerHand.clear();
        game.playerHand.add(new Card("Hearts", 1)); // Player hand: Ace
        game.playerHand.add(new Card("Diamonds", 10)); // Player hand: Ace, 10 (Blackjack)
        assertTrue(game.playerHasBlackjack());
    }

    @Test
    void testPlayerHasBlackjack_False() {
        BlackjackGame game = new BlackjackGame();
        game.playerHand.clear();
        game.playerHand.add(new Card("Hearts", 1)); // Player hand: Ace
        game.playerHand.add(new Card("Diamonds", 9)); // Player hand: Ace, 9
        assertFalse(game.playerHasBlackjack());
    }

    @Test
    void testSetCurrentBet() {
        BlackjackGame game = new BlackjackGame();
        game.setCurrentBet(50);
        assertEquals(50, game.currentBet);
    }

    // BlackjackGameLogic tests
    @Test
    void testWhoWon_PlayerBusts() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> playerHand = new ArrayList<>();
        playerHand.add(new Card("Hearts", 10)); // Card value: 10
        playerHand.add(new Card("Clubs", 9));   // Card value: 9
        playerHand.add(new Card("Diamonds", 5)); // Card value: 5
        assertEquals("dealer", logic.whoWon(playerHand, new ArrayList<>())); // Player busts, dealer wins
    }

    @Test
    void testWhoWon_DealerBusts() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> dealerHand = new ArrayList<>();
        dealerHand.add(new Card("Spades", 10)); // Card value: 10
        dealerHand.add(new Card("Clubs", 8));   // Card value: 8
        dealerHand.add(new Card("Hearts", 7));  // Card value: 7
        assertEquals("player", logic.whoWon(new ArrayList<>(), dealerHand)); // Dealer busts, player wins
    }

    @Test
    void testWhoWon_PlayerWins() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> playerHand = new ArrayList<>();
        playerHand.add(new Card("Diamonds", 10)); // Card value: 10
        playerHand.add(new Card("Spades", 7));    // Card value: 7
        ArrayList<Card> dealerHand = new ArrayList<>();
        dealerHand.add(new Card("Hearts", 9));    // Card value: 9
        dealerHand.add(new Card("Clubs", 6));     // Card value: 6
        assertEquals("player", logic.whoWon(playerHand, dealerHand)); // Player has higher hand value, player wins
    }

    @Test
    void testWhoWon_DealerWins() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> playerHand = new ArrayList<>();
        playerHand.add(new Card("Diamonds", 9)); // Card value: 9
        playerHand.add(new Card("Spades", 8));   // Card value: 8
        ArrayList<Card> dealerHand = new ArrayList<>();
        dealerHand.add(new Card("Hearts", 10));  // Card value: 10
        dealerHand.add(new Card("Clubs", 9));    // Card value: 9
        assertEquals("dealer", logic.whoWon(playerHand, dealerHand)); // Dealer has higher hand value, dealer wins
    }

    @Test
    void testWhoWon_Push() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> playerHand = new ArrayList<>();
        playerHand.add(new Card("Diamonds", 10)); // Card value: 10
        playerHand.add(new Card("Spades", 8));    // Card value: 8
        ArrayList<Card> dealerHand = new ArrayList<>();
        dealerHand.add(new Card("Hearts", 10));   // Card value: 10
        dealerHand.add(new Card("Clubs", 8));     // Card value: 8
        assertEquals("push", logic.whoWon(playerHand, dealerHand)); // Equal hand values, it's a push (tie)
    }

    @Test
    void testHandTotal_NoAces() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card("Hearts", 2)); // Card value: 2
        hand.add(new Card("Clubs", 7));  // Card value: 7
        hand.add(new Card("Diamonds", 8)); // Card value: 8
        assertEquals(17, logic.handTotal(hand)); // Total value should be 17
    }

    @Test
    void testHandTotal_WithAce() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card("Hearts", 1)); // Ace (value can be either 1 or 11)
        hand.add(new Card("Clubs", 7));  // Card value: 7
        hand.add(new Card("Diamonds", 10)); // Card value: 10
        assertEquals(18, logic.handTotal(hand)); // Total value should be 18 (Ace treated as 1)
    }

    @Test
    void testHandTotal_WithAceInFavor() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card("Hearts", 1)); // Ace (value can be either 1 or 11)
        hand.add(new Card("Clubs", 7));  // Card value: 7
        assertEquals(18, logic.handTotal(hand)); // Total value should be 18 (Ace treated as 1)
    }

    @Test
    void testHandTotal_WithAces() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card("Hearts", 1)); // Ace (value can be either 1 or 11)
        hand.add(new Card("Clubs", 1)); // Ace (value can be either 1 or 11)
        hand.add(new Card("Diamonds", 1)); // Ace (value can be either 1 or 11)
        hand.add(new Card("Clubs", 7));  // Card value: 7
        hand.add(new Card("Diamonds", 10)); // Card value: 10
        assertEquals(20, logic.handTotal(hand)); // Total value should be 18 (Aces treated as 1)
    }

    @Test
    void testHandTotal_WithAcesInFavor() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card("Hearts", 1)); // Ace (value can be either 1 or 11)
        hand.add(new Card("Clubs", 1)); // Ace (value can be either 1 or 11)
        hand.add(new Card("Clubs", 2));  // Card value: 2
        hand.add(new Card("Diamonds", 2)); // Card value: 2
        assertEquals(16, logic.handTotal(hand)); // Total value should be 18 (Aces treated as 11 & 1)
    }

    @Test
    void testEvaluateBankerDraw_ShouldDraw() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card("Hearts", 7)); // Card value: 7
        hand.add(new Card("Clubs", 8));  // Card value: 8
        assertTrue(logic.evaluateBankerDraw(hand)); // Total value is 15, should draw another card
    }

    @Test
    void testEvaluateBankerDraw_ShouldNotDraw() {
        BlackjackGameLogic logic = new BlackjackGameLogic();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card("Hearts", 10)); // Card value: 10
        hand.add(new Card("Clubs", 9));   // Card value: 9
        assertFalse(logic.evaluateBankerDraw(hand)); // Total value is 19, should not draw another card
    }

    // BlackjackDealer tests
    @Test
    void testGenerateDeck() {
        BlackjackDealer dealer = new BlackjackDealer();
        dealer.generateDeck();
        assertEquals(52, dealer.deckSize()); // Check if the deck size is 52 after generation
    }

    @Test
    void testDealHand() {
        BlackjackDealer dealer = new BlackjackDealer();
        dealer.generateDeck();
        ArrayList<Card> hand = dealer.dealHand();
        assertEquals(2, hand.size()); // Check if the hand contains two cards
        assertEquals(50, dealer.deckSize()); // Check if the deck size is reduced by two after dealing
    }

    @Test
    void testDrawOne() {
        BlackjackDealer dealer = new BlackjackDealer();
        dealer.generateDeck();
        Card card = dealer.drawOne();
        assertNotNull(card); // Check if a card is drawn
        assertEquals(51, dealer.deckSize()); // Check if the deck size is reduced by one after drawing
    }

    @Test
    void testShuffleDeck() {
        BlackjackDealer dealer = new BlackjackDealer();
        dealer.generateDeck();
        ArrayList<Card> originalDeck = new ArrayList<>(dealer.deck);
        dealer.shuffleDeck();
        assertNotEquals(originalDeck, dealer.deck); // Check if the deck is shuffled
    }

    @Test
    void testDeckSize() {
        BlackjackDealer dealer = new BlackjackDealer();
        assertEquals(0, dealer.deckSize()); // Check if the deck size is 0 initially
        dealer.generateDeck();
        assertEquals(52, dealer.deckSize()); // Check if the deck size is 52 after generation
    }

}
