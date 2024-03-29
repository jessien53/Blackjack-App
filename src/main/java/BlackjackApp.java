import javafx.animation.PauseTransition;
import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

public class BlackjackApp extends Application {
    // GUI components
    Button startButton, confirmBetButton, exitButton, newGameButton, stayButton, hitButton;
    TextField inputStartMoneyTF, inputBetAmountTF;
    Label title, moneyLabel, currentLabel, betLabel, startInstructions, whoWonRound, playerH, dealerH;

    // Scenes management
    HashMap<String, Scene> sceneMap;
    HBox playerHandDisplay, dealerHandDisplay;
    String winner;
    PauseTransition delay;

    // Game variables
    double totalMoney, currentBet;
    int cardWidth = 110; // ratio should be 1/1.4
    int cardHeight = 154;

    BlackjackGame game = new BlackjackGame();
    BlackjackDealer dealer = game.theDealer;
    BlackjackGameLogic logic = game.gameLogic;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Blackjack App");

        // Initialize GUI components
        initializeComponents();
        createStartAndBettingScenes();
        delay.setOnFinished(event -> {
            // After the delay, change the scene to betting screen
            betLabel.setText("You have $"+totalMoney+"\nEnter amount you wish to bet: ");
            primaryStage.setScene(sceneMap.get("betting"));
            whoWonRound.setVisible(false);
            hitButton.setDisable(false);
            stayButton.setDisable(false);
        });

        // Event handling
        // Start button event handler
        startButton.setOnAction(e -> {
            try {
                double input = Double.parseDouble(inputStartMoneyTF.getText());
                if (input > 0.0) {
                    totalMoney = input;
                    // Proceed to the betting scene
                    betLabel.setText("You have $"+totalMoney+"\nEnter amount you wish to bet: ");
                    primaryStage.setScene(sceneMap.get("betting"));
                } else {
                    inputStartMoneyTF.clear();
                    inputStartMoneyTF.setPromptText("Error: Number cannot be negative");
                }
            } catch (NumberFormatException ex) {
                inputStartMoneyTF.clear();
                // Show an error alert if the input is not a valid number
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a positive number.");

                // Show the Alert
                alert.showAndWait();
            }
        });
        // Confirm bet button event handler
        confirmBetButton.setOnAction(e -> {
            try {
                // Get the bet amount entered by the user
                double betAmount = Double.parseDouble(inputBetAmountTF.getText());

                // Check if the bet amount is valid
                if (betAmount > 0.0 && betAmount <= totalMoney) {
                    // Place the bet
                    currentBet = betAmount;
                    totalMoney -= currentBet; // Deduct bet amount from total money

                    // Proceed to the game
                    game.setCurrentBet(currentBet);
                    createGameScene();
                    primaryStage.setScene(sceneMap.get("game"));

                } else {
                    // Display error message if the bet amount is invalid
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Bet Amount");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter a valid bet amount. Between 0 and " + totalMoney + ".");
                    alert.showAndWait();
                }
            } catch (NumberFormatException ex) {
                // Display error message if the input is not a valid number
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid number for the bet amount.");
                alert.showAndWait();
            }
        });
        // Stay button event handler
        stayButton.setOnAction(e -> {
            while (game.gameLogic.evaluateBankerDraw(game.bankerHand)) {
                Card newCard = game.theDealer.drawOne();
                game.bankerHand.add(newCard);
                Image cardImg = new Image(newCard.toString() + ".png");
                ImageView cv = new ImageView(cardImg);
                cv.setFitHeight(cardHeight);
                cv.setFitWidth(cardWidth);
                cv.setPreserveRatio(true);
                dealerHandDisplay.getChildren().add(cv);
            }
            winner = game.gameLogic.whoWon(game.playerHand, game.bankerHand);
            totalMoney += game.evaluateWinnings();
            dealerH.setText("Dealer Hand: "+game.gameLogic.handTotal(game.bankerHand));
            if (winner.equals("player")) {
                whoWonRound.setText("You won this round!");
                whoWonRound.setStyle("-fx-font-size: 16;" + "-fx-background-color: rgb(21,194,24); " +
                        "-fx-text-fill: black; " + "-fx-border-color: black; " +
                        "-fx-border-width: 1px; " + "-fx-border-style: solid;" +
                        "-fx-padding: 5px;");
                whoWonRound.setVisible(true);
            } else if (winner.equals("dealer")) {
                whoWonRound.setText("You lost this round :(");
                whoWonRound.setStyle("-fx-background-color: red; " + "-fx-font-size: 16;" +
                        "-fx-text-fill: black; " + "-fx-border-color: black; " +
                        "-fx-border-width: 1px; " + "-fx-border-style: solid;" +
                        "-fx-padding: 5px;");
                whoWonRound.setVisible(true);
            } else {
                whoWonRound.setText("Draw");
                whoWonRound.setStyle("-fx-background-color: yellow; " + "-fx-font-size: 16;" +
                        "-fx-text-fill: black; " + "-fx-border-color: black; " +
                        "-fx-border-width: 1px; " + "-fx-border-style: solid;" +
                        "-fx-padding: 5px;");
                whoWonRound.setVisible(true);
            }
            // Show first hidden card
            Card newCard = game.bankerHand.get(0);
            game.bankerHand.add(newCard);
            Image cardImg = new Image(newCard.toString() + ".png");
            ImageView cv = new ImageView(cardImg);
            cv.setFitHeight(cardHeight);
            cv.setFitWidth(cardWidth);
            cv.setPreserveRatio(true);
            dealerHandDisplay.getChildren().set(0, cv);
            // Discontinue game if their totalMoney have depleted
            if (totalMoney > 0) {
                inputBetAmountTF.clear();
                delay.play(); // Start the delay
            } else {
                whoWonRound.setVisible(false);
                createResultScene();
                primaryStage.setScene(sceneMap.get("result"));
            }
        });
        // Hit button event handler
        hitButton.setOnAction(e -> {
            Card newCard = game.theDealer.drawOne();
            game.playerHand.add(newCard);
            Image cardImg = new Image(newCard.toString() + ".png");
            ImageView cv = new ImageView(cardImg);
            cv.setFitHeight(cardHeight);
            cv.setFitWidth(cardWidth);
            cv.setPreserveRatio(true);
            playerHandDisplay.getChildren().add(cv);
            playerH.setText("Player's Hand: "+game.gameLogic.handTotal(game.playerHand));
            PauseTransition delay2=new PauseTransition(Duration.seconds(3));
            delay2.setOnFinished(event -> {
                // After the delay, change the scene to result screen
                createResultScene();
                primaryStage.setScene(sceneMap.get("result"));
                whoWonRound.setVisible(false);
                hitButton.setDisable(false);
                stayButton.setDisable(false);
            });
            // Check if player busts
            if (game.gameLogic.handTotal(game.playerHand) > 21) {
                hitButton.setDisable(true);
                stayButton.setDisable(true);
                whoWonRound.setText("Bust");
                whoWonRound.setStyle("-fx-background-color: red; " + "-fx-font-size: 16;" +
                        "-fx-text-fill: black; " + "-fx-border-color: black; " +
                        "-fx-border-width: 1px; " + "-fx-border-style: solid;" +
                        "-fx-padding: 5px;");
                whoWonRound.setVisible(true);
                // Discontinue game if their totalMoney have depleted
                if (totalMoney > 0) {
                    inputBetAmountTF.clear();
                    delay.play(); // Start the delay and continue to betting screen
                } else {
                    delay2.play(); // Start delay and exit to result screen
                }
            }
        });
        // New game button event handler
        newGameButton.setOnAction(e -> {
            inputStartMoneyTF.clear();
            inputBetAmountTF.clear();
            primaryStage.setScene(sceneMap.get("start"));
        });
        // Exit button event handler
        exitButton.setOnAction(e -> primaryStage.close());

        // Set the initial scene
        primaryStage.setScene(sceneMap.get("start"));
        primaryStage.setTitle("Blackjack App");
        primaryStage.show();

    }

    // Method to initialize GUI components
    private void initializeComponents() {
        sceneMap = new HashMap<>();
        // Initialize GUI components
        title = new Label("Welcome to Blackjack!");
        whoWonRound = new Label();

        inputStartMoneyTF = new TextField();
        inputStartMoneyTF.setPromptText("Enter starting money amount");
        startButton = new Button("Start Game");
        startInstructions = new Label("Enter the amount of money to start the game with.\n" +
                "(A positive number with no letters)");

        betLabel = new Label();
        inputBetAmountTF = new TextField();
        inputBetAmountTF.setPromptText("Enter bet amount");
        confirmBetButton = new Button("Confirm Bet");

        newGameButton = new Button("New Game");
        stayButton = new Button("Stay");
        hitButton = new Button("Hit");
        exitButton = new Button("Exit");

        delay = new PauseTransition(Duration.seconds(3));
    }

    // Method to create start and betting scenes
    private void createStartAndBettingScenes() {
        // Start Screen
        BorderPane startPane = new BorderPane();
        startPane.setPadding(new Insets(50));
        startPane.setStyle("-fx-background-color: rgb(17,124,19);");
        title.setStyle("-fx-font-size: 40;" + "-fx-border-size: 20;" + "-fx-text-fill: black;" +
                "-fx-font-weight: bold;");
        startButton.setStyle("-fx-background-color: black;" + "-fx-text-fill: white;" + "-fx-font-size: 13;");
        startButton.setPrefSize(100, 40);
        //inputStartMoneyTF.setStyle("-fx-background-color: grey;");
        VBox startVB = new VBox(10, startInstructions, inputStartMoneyTF, startButton);
        startVB.setAlignment(Pos.CENTER);
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        startPane.setTop(titleBox);
        startPane.setCenter(startVB);
        startInstructions.setStyle("-fx-font-size: 16;" + "-fx-text-fill: white;");

        Scene startScene = new Scene(startPane, 600, 500);

        // Betting Screen
        BorderPane bettingPane = new BorderPane();
        bettingPane.setPadding(new Insets(50));
        bettingPane.setStyle("-fx-background-color: rgb(17,124,19);");
        betLabel.setStyle("-fx-font-size: 16;" + "-fx-text-fill: white;");
        //inputBetAmountTF.setStyle("-fx-background-color: grey;");
        confirmBetButton.setStyle("-fx-background-color: black;" + "-fx-text-fill: white;" + "-fx-font-size: 13;");
        confirmBetButton.setPrefSize(100, 40);
        VBox bettingVB = new VBox(10, betLabel, inputBetAmountTF, confirmBetButton);
        bettingVB.setAlignment(Pos.CENTER);
        bettingPane.setCenter(bettingVB);
        Scene bettingScene = new Scene(bettingPane, 600, 500);

        // Place the scenes in the sceneMap
        sceneMap.put("start", startScene);
        sceneMap.put("betting", bettingScene);


    }

    // Method to create the result scene
    public void createResultScene() {
        // Result Screen
        BorderPane resultPane = new BorderPane();
        resultPane.setPadding(new Insets(50));
        Label result = new Label();
        result.setStyle("-fx-font-size: 32;" + "-fx-font-weight: bold;");
        resultPane.setStyle("-fx-background-color: red;");
        result.setText("You ran out of money...\t:(");
        exitButton.setStyle("-fx-background-color: black;" + "-fx-text-fill: white;" + "-fx-font-size: 16;");
        newGameButton.setStyle("-fx-background-color: black;" + "-fx-text-fill: white;" + "-fx-font-size: 16;");
        HBox resultButtons = new HBox(40, newGameButton, exitButton);
        resultButtons.setAlignment(Pos.BOTTOM_CENTER);
        VBox resultLayout = new VBox(100, result, resultButtons);
        resultLayout.setAlignment(Pos.CENTER);
        resultPane.setCenter(resultLayout);
        Scene resultScene = new Scene(resultPane, 700, 600);

        sceneMap.put("result", resultScene);
    }

    // Method to create the game scene
    public void createGameScene() {
        // Start the game
        if (game.theDealer.deckSize() < 10) {
            dealer.shuffleDeck();
        } // Not enough cards to deal new round
        else {
            game.bankerHand = dealer.dealHand();
            game.playerHand = dealer.dealHand();
        }

        // Game Screen
        BorderPane gamePane = new BorderPane();
        gamePane.setPadding(new Insets(50));
        gamePane.setStyle("-fx-background-color: rgb(17,124,19);");

        // Buttons at bottom of screen
        hitButton.setStyle("-fx-background-color: black;" + "-fx-text-fill: white;" + "-fx-font-size: 16;");
        stayButton.setStyle("-fx-background-color: black;" + "-fx-text-fill: white;" + "-fx-font-size: 16;");
        hitButton.setPrefSize(70, 50);
        stayButton.setPrefSize(70, 50);
        HBox buttons = new HBox(40, hitButton, stayButton);
        buttons.setAlignment(Pos.CENTER);
        gamePane.setBottom(buttons);

        // Display money stats on top right of screen
        moneyLabel = new Label("Total Money: $" + totalMoney);
        currentLabel = new Label("Current Bet Amount: $" + currentBet);
        moneyLabel.setStyle("-fx-font-size: 16;" + "-fx-background-color: white; " +
                "-fx-text-fill: black; " + "-fx-border-color: black; " +
                "-fx-border-width: 1px; " + "-fx-border-style: solid;" +
                "-fx-padding: 5px;");
        currentLabel.setStyle("-fx-font-size: 16;" + "-fx-background-color: white; " +
                "-fx-text-fill: black; " + "-fx-border-color: black; " +
                "-fx-border-width: 1px; " + "-fx-border-style: solid;" +
                "-fx-padding: 5px;");
        VBox moneyDisplay = new VBox(10, moneyLabel, currentLabel, whoWonRound);
        gamePane.setRight(moneyDisplay);

        // Display the cards dealt
        Image hiddenCard = new Image("BACK.png");
        ImageView hc = new ImageView(hiddenCard);
        hc.setFitHeight(cardHeight);
        hc.setFitWidth(cardWidth);
        hc.setPreserveRatio(true);
        dealerHandDisplay = new HBox(10, hc);
        // Get dealer's hand cards after first one that is hidden
        for (int i = 1; i < game.bankerHand.size(); i++) {
            Card card = game.bankerHand.get(i);
            Image cardImg = new Image(card.toString() + ".png");
            ImageView cv = new ImageView(cardImg);
            cv.setFitHeight(cardHeight);
            cv.setFitWidth(cardWidth);
            cv.setPreserveRatio(true);
            dealerHandDisplay.getChildren().add(cv);
        }
        // Get player's hand cards
        playerHandDisplay = new HBox(10);
        for (Card card : game.playerHand) {
            Image cardImg = new Image(card.toString() + ".png");
            ImageView cv = new ImageView(cardImg);
            cv.setFitHeight(cardHeight);
            cv.setFitWidth(cardWidth);
            cv.setPreserveRatio(true);
            playerHandDisplay.getChildren().add(cv);
        }
        dealerH = new Label("Dealer's Hand:");
        playerH = new Label("Player's Hand: "+logic.handTotal(game.playerHand));
        dealerH.setStyle("-fx-text-fill: white;" + "-fx-font-size: 18;");
        playerH.setStyle("-fx-text-fill: white;" + "-fx-font-size: 18;");

        VBox cardsDisplay = new VBox(20, dealerH, dealerHandDisplay, playerH, playerHandDisplay);
        gamePane.setCenter(cardsDisplay);

        // Check if hit button should be disabled given the player's cards
        if (logic.handTotal(game.playerHand) > 21) {
            hitButton.setDisable(true);
        }

        Scene gameScene = new Scene(gamePane, 1000, 600);
        sceneMap.put("game", gameScene);
    }

}
