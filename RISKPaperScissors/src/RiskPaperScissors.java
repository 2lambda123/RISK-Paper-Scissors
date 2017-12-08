import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RiskPaperScissors extends Application {
	private Player currentPlayer;
	private ArrayList<Player> players;
	private ArrayList<Card> deck;
	private ArrayList<Territory> territories;
	private HashMap<HashSet<Territory>, Integer> continentValues;
	private Map map;
	private Stage game;
	private Scene splashScreen;
	private Scene gameplayScreen;
	private Pane gameplayPane;
	private Scene aboutScreen;
	private Scene attackScreen;
	private boolean attackScreenInitialized;

	private VBox attackPane;
	private Pane attackImages;
	private Text results;
	private Text numArmies;

	private int originalAttackingArmies;
	private boolean armiesLocked;

	private static final Color[] colors = { Color.RED, Color.YELLOW, Color.BLUE, Color.ORANGE, Color.GRAY };
	private static final ArrayList<Color> colorsList = new ArrayList<Color>(Arrays.asList(colors));
	private static final Random RNG = new Random();
	private static final Color DEFAULT_PLAYER_COLOR = Color.GREEN;
	private static final String DEFAULT_PLAYER_COLOR_STRING = "GREEN";
	private static final int DEFAULT_PLAYERS = 3;
	private static final int MAX_NUM_PLAYERS = 6;
	private static final int[] STARTING_ARMIES = { 0, 0, 40, 35, 30, 25, 20 };
	private static final int MINIMUM_ARMIES = 3;
	private static final int NUM_TYPES_OF_CARDS = 3;
	private static final int MAX_HAND_SIZE = 7;
	// private static final int MAX_NUM_OF_ATTACKS = 5;
	private int playerIndex;
	private int numPlayers;

	@Override
	public void start(Stage arg0) throws Exception {
		long startTime = System.currentTimeMillis();

		game = new Stage();
		map = new DefaultMap();
		continentValues = map.getContinentValues();
		results = new Text();
		results.setFont(Font.font("Stencil", 20));
		results.setEffect(new InnerShadow());
		territories = new ArrayList<Territory>(map.getTerritories());
		deck = new ArrayList<Card>();
		armiesLocked = false;
		attackPane = new VBox();
		attackPane.setAlignment(Pos.CENTER);
		attackScreen = new Scene(attackPane, 640, 510);
		attackScreenInitialized = false;
		numArmies = new Text();
		numArmies.setX(75);
		numArmies.setY(900);
		numArmies.setFont(Font.font("Stencil", 40));

		int type = 1;
		for (Territory t : territories) {
			deck.add(new Card(t, type));
			type++;
			if (type > NUM_TYPES_OF_CARDS)
				type = 1;
			t.getImage().setOnMouseEntered(e -> {
				Text text = new Text(t + "(" + t.getPlayer() + "): " + t.getArmies() + " armies.");
				text.xProperty().bind(new SimpleDoubleProperty(e.getScreenX()));
				text.yProperty().bind(new SimpleDoubleProperty(e.getScreenY()));
			});
		}

		ImageView risk = new ImageView("file:resources/risk.png");
		ImageView paperScissors = new ImageView("file:resources/paper scissors.png");
		ImageView start = new ImageView("file:resources/start.png");
		ImageView about = new ImageView("file:resources/about.png");
		ImageView quit = new ImageView("file:resources/quit.png");
		ImageView bg = new ImageView("file:resources/bg.jpg");
		StackPane splashPane = new StackPane();
		splashPane.getChildren().addAll(bg, start, about, quit, risk, paperScissors);
		for (Node n : splashPane.getChildren()) {
			if (n instanceof ImageView) {
				ImageView img = (ImageView) n;
				img.fitWidthProperty().bind(game.widthProperty());
				img.fitHeightProperty().bind(game.heightProperty());
			}
		}
		start.setOnMouseClicked(e -> {
			List<Integer> choices = new ArrayList<>();
			for (int i = 1; i < MAX_NUM_PLAYERS; i++) {
				choices.add(i);
			}

			ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choices);
			dialog.setTitle("Choose Players");
			dialog.setHeaderText(null);
			dialog.setContentText("How many computer players do you want to play against?");
			try {
				numPlayers = dialog.showAndWait().get() + 1;
			} catch (NoSuchElementException ex) {
				numPlayers = DEFAULT_PLAYERS;
			}
			initialize();
		});
		quit.setOnMouseClicked(e -> {
			System.exit(0);
		});
		about.setOnMouseClicked(e -> {
			showAboutScreen();
		});

		ScaleTransition grow = new ScaleTransition();
		grow.setNode(paperScissors);
		grow.setByX(.2);
		grow.setByY(.2);
		grow.setAutoReverse(true);
		grow.setDuration(Duration.millis(2000));
		grow.setCycleCount(Timeline.INDEFINITE);
		grow.play();

		splashScreen = new Scene(splashPane, 800, 450);
		game.setScene(splashScreen);
		game.setTitle("RISK Paper Scissors");
		game.show();

		long endTime = System.currentTimeMillis();
		System.out.println("Showtime: " + (endTime - startTime));
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void initialize() {
		showGameplayScreen();
		dealCards();
		playerIndex = 0;
		placeInitialArmies(players.get(playerIndex));
	}

	public void nextTurn() {
		Territory attacker = null, defender = null;
		do {
			playerIndex++;
			if (playerIndex == numPlayers)
				playerIndex = 0;
			currentPlayer = players.get(playerIndex);
		} while (currentPlayer.getTerritories().size() == 0);

		alert("Next Turn", "It is now " + currentPlayer + "'s turn.");

		showGameplayScreen();
		if (!deck.isEmpty())
			dealCard(currentPlayer);
		allocateArmies(currentPlayer);
		placeArmies(currentPlayer);

		if (!currentPlayer.isHuman()) {
			currentPlayer.checkForMatches();
			// int attacks = RNG.nextInt((MAX_NUM_OF_ATTACKS - 1) + 1);
			// for (int i = 0; i < attacks; i++) {
			currentPlayer.chooseTerritory();
			attacker = currentPlayer.getAttackingWith();
			currentPlayer.attackTeritory();
			defender = currentPlayer.getAttacking();
			if (defender.getPlayer().isHuman())
				defender.getPlayer().selectAttackType(false);
			currentPlayer.setAttackingArmies(attacker.getArmies() - 1);
			battle(attacker, defender, currentPlayer.getAttackingArmies());
			// }
		}
	}

	private void showGameplayScreen() {
		if (gameplayScreen != null) {
			ImageView handButton = map.getSeeHandButton();
			ImageView endButton = map.getEndTurnButton();
			gameplayPane.getChildren().removeAll(handButton, endButton);
			if (currentPlayer.isHuman()) {
				handButton.setOnMouseClicked(e -> {
					openHand(currentPlayer);
				});
				endButton.setOnMouseClicked(e -> {
					nextTurn();
				});
				gameplayPane.getChildren().addAll(handButton, endButton);
			}
			game.setScene(gameplayScreen);
			game.show();
		} else {
			players = new ArrayList<Player>();
			TextInputDialog nameDialog = new TextInputDialog("Michael");
			nameDialog.setTitle("Enter name");
			nameDialog.setContentText("Enter name: ");
			nameDialog.setHeaderText(null);
			String name = nameDialog.showAndWait().get();
			players.add(new Player(name, true, DEFAULT_PLAYER_COLOR, this));

			alert("Welcome", "Welcome " + name + ". Your color is " + DEFAULT_PLAYER_COLOR_STRING + ".");
			for (int i = 1; i < numPlayers; i++) {
				Color c = colorsList.remove(RNG.nextInt(colorsList.size()));
				players.add(new Player("CPU" + i, false, c, this));
			}

			game.close();

			long startTime = System.currentTimeMillis();

			gameplayPane = new Pane();
			ImageView bg = map.getBackground();
			gameplayPane.getChildren().add(bg);
			gameplayPane.getChildren().add(numArmies);

			for (Territory t : map.getTerritories()) {
				ImageView img = t.getImage();
				gameplayPane.getChildren().add(img);
				t.getImage().setOnMouseEntered(e -> {
					Tooltip.install(t.getImage(), new Tooltip(t.getText()));
				});
			}

			gameplayScreen = new Scene(gameplayPane, map.getWidth(), map.getHeight());
			game.setScene(gameplayScreen);

			long endTime = System.currentTimeMillis();
			System.out.println("Map time: " + (endTime - startTime));

			currentPlayer = players.get(0);

			game.show();
		}
	}

	private void dealCards() {
		int playerIndex = 0;
		Player p;

		Collections.shuffle(deck);
		for (Card c : deck) {
			p = players.get(playerIndex);
			p.addTerritory(c.getTerritory());
			if (p.isHuman()) {
				alert("Territory Aquisition", p.getName() + " receives " + c + ".");
			}
			playerIndex++;
			if (playerIndex == numPlayers)
				playerIndex = 0;
		}
	}

	public boolean checkTerritories(Player p) {
		for (Territory t : p.getTerritories()) {
			if (t.getArmies() == 0) {
				System.out.println(t + " has " + t.getArmies() + " armies.");
				System.out.println(p + " is trying again...");
				return false;
			}
		}
		return true;
	}

	public void placeInitialArmies(Player p) {
		p.setFreeArmies(STARTING_ARMIES[numPlayers]);
		if (p.isHuman()) {
			numArmies.setText(p.getFreeArmies() + " armies left.");
			alert("Place Initial Armies",
					p + ", you have " + p.getFreeArmies() + "  armies to place in your territories.");

			gameplayScreen.setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.TAB) {
					boolean finished = false;
					while (!finished) {
						p.setFreeArmies(STARTING_ARMIES[numPlayers]);
						for (Territory t : p.getTerritories()) {
							t.setArmies(0);
						}
						while (p.hasArmies()) {
							p.addArmy();
						}
						finished = checkTerritories(p);
					}
					for (Territory t : p.getTerritories())
						t.disable();
					gameplayScreen.setOnKeyPressed(null);
					placeNextInitialArmies();
				}
			});

			for (Territory t : p.getTerritories())
				t.addInitialArmiesMode();
		} else {
			setArmyText("");
			boolean finished = false;
			while (!finished) {
				p.setFreeArmies(STARTING_ARMIES[numPlayers]);
				for (Territory t : p.getTerritories()) {
					t.setArmies(0);
				}
				while (p.hasArmies()) {
					p.addArmy();
				}
				finished = checkTerritories(p);
			}
			placeNextInitialArmies();
		}
	}

	public void placeNextInitialArmies() {
		playerIndex++;
		if (playerIndex == players.size()) {
			playerIndex = -1;
			nextTurn();
		} else
			placeInitialArmies(players.get(playerIndex));
	}

	private void placeArmies(Player p) {
		if (p.isHuman()) {
			setArmyText(p.getFreeArmies() + " armies left.");
			for (Territory t : p.getTerritories())
				t.addArmyMode();
		} else {
			while (p.hasArmies())
				p.addArmy();
		}
	}

	private void dealCard(Player p) {
		if (p.getHand().size() == MAX_HAND_SIZE) {
			alert("Full Hand", p + "'s hand is full. They do not receive a card.");
			return;
		}
		Card c = deck.get(RNG.nextInt(deck.size()));
		Territory t = c.getTerritory();
		if (p.getTerritories().contains(t)) {
			if (!p.isHuman()) {
				alert("Armies Placed",
						p + " owns the territory on the card they drew. Two armies are placed on " + t + ".");
				t.setArmies(t.getArmies() + 2);
				return;
			} else {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Matching Card");
				alert.setHeaderText(null);
				alert.setContentText("You own the territory on this card. Exchange it for 2 armies on " + t + "?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					t.setArmies(t.getArmies() + 2);
					return;
				}
			}
		}
		if (p.isHuman())
			alert("Card Dealt", p + " receives a " + c + " card.");
		p.addCard(c);
	}

	private void openHand(Player p) {
		HBox hand = new HBox(10);
		VBox cardBox;
		Text territory;
		ImageView type;
		for (Card c : p.getHand()) {
			cardBox = new VBox();
			territory = new Text(c.toString());
			type = c.getImage();
			type.setOnMouseClicked(e -> {
				p.selectCard(c);
			});
			cardBox.getChildren().addAll(territory, type);
			hand.getChildren().add(cardBox);
		}
		Scene handScene = new Scene(hand, 100 * p.getHand().size(), 175);
		Stage handStage = new Stage();
		handStage.setScene(handScene);
		handStage.setTitle(p + "'s Hand");
		handStage.show();
	}

	public int allocateArmies(Player p) {
		int num = MINIMUM_ARMIES, matches;
		for (HashSet<Territory> continent : continentValues.keySet()) {
			matches = 0;
			for (Territory t : continent) {
				if (!p.getTerritories().contains(t))
					break;
				else
					matches++;
			}
			if (matches == continent.size())
				num += continentValues.get(continent);
		}

		num += p.getTerritories().size() / 3;

		p.setFreeArmies(num);
		alert("Place Armies", p + " has " + p.getTerritories().size() + " territories. They receive "
				+ p.getFreeArmies() + " armies  to place.");
		return num;
	}

	public void turnInSet(Player p) {
		int sets = p.getSetsTurnedIn();
		int armies;
		if (sets > 0 && sets <= 5)
			armies = 4 + (2 * (sets - 1));
		else if (sets == 6)
			armies = 15;
		else
			armies = 15 + (5 * (sets - 6));
		alert("Turned in Set", p + " turned in a set of cards and recieves " + armies + " armies.");
		p.setFreeArmies(p.getFreeArmies() + armies);
		placeArmies(p);
	}

	private void showAboutScreen() {
		if (aboutScreen != null) {
			game.close();
			game.setScene(aboutScreen);
			game.show();
		} else {
			StackPane about = new StackPane();
			ImageView backButton = new ImageView("file:resources/backButton.png");
			backButton.setOnMouseClicked(e -> {
				game.close();
				game.setScene(splashScreen);
				game.show();
			});
			about.getChildren().addAll(new ImageView("file:resources/aboutScreen.jpg"), backButton);
			aboutScreen = new Scene(about, 800, 450);
			game.close();
			game.setScene(aboutScreen);
			game.show();
		}

	}

	private void showAttackScreen(Territory atk, Territory dfnd) {
		if (attackScreenInitialized) {
			attackImages.getChildren().removeAll(attackImages.getChildren());
			attackImages.getChildren().add(new ImageView("file:resources/attackBG.jpg"));
			updateAttackScreen(atk.getPlayer(), true);
			updateAttackScreen(dfnd.getPlayer(), false);
			game.setScene(attackScreen);
		} else {
			game.close();
			attackImages = new Pane();
			attackImages.getChildren().add(new ImageView("file:resources/attackBG.jpg"));
			attackPane.getChildren().add(attackImages);
			updateAttackScreen(atk.getPlayer(), true);
			updateAttackScreen(dfnd.getPlayer(), false);
			attackPane.getChildren().add(results);
			game.setScene(attackScreen);
			attackScreenInitialized = true;
			game.show();
		}
	}

	private void updateAttackScreen(Player p, boolean isAttacking) {
		Territory t = (isAttacking ? p.getAttackingWith() : currentPlayer.getAttacking());
		int armies = (isAttacking ? p.getAttackingArmies() : t.getArmies());
		Text title = new Text(t.getName() + ": " + armies);
		title.setFill(t.getPlayer().getColor());
		title.setFont(Font.font("Stencil", 20));
		title.setEffect(new InnerShadow());
		ImageView img = null;

		Player.atkType choice = t.getPlayer().getAttackType();
		switch (choice) {
		case ROCK_ATTACK:
			img = new ImageView("file:resources/rockAtk.png");
			break;
		case PAPER_ATTACK:
			img = new ImageView("file:resources/paperAtk.png");
			break;
		case SCISSORS_ATTACK:
			img = new ImageView("file:resources/scissorsAtk.png");
			break;
		case ROCK_DEFEND:
			img = new ImageView("file:resources/rockDfn.png");
			break;
		case PAPER_DEFEND:
			img = new ImageView("file:resources/paperDfn.png");
			break;
		case SCISSORS_DEFEND:
			img = new ImageView("file:resources/scissorsDfn.png");
			break;
		}
		if (p.isHuman() && attackScreen.getOnKeyPressed() == null) {
			Territory attacker = (isAttacking ? p.getAttackingWith() : currentPlayer.getAttackingWith());
			Territory defender = (isAttacking ? p.getAttacking() : currentPlayer.getAttacking());

			if (isAttacking) {
				alert("Attack", "Use the number keys to choose an attack. \n1 for Rock \n2 for Paper \n3 for Scissors");
				attackScreen.setOnKeyPressed(e -> {
					if (e.getCode() == KeyCode.DIGIT1) {
						p.setAttackType(Player.atkType.ROCK_ATTACK);
						battle(attacker, defender, p.getAttackingArmies());
					} else if (e.getCode() == KeyCode.DIGIT2) {
						p.setAttackType(Player.atkType.PAPER_ATTACK);
						battle(attacker, defender, p.getAttackingArmies());
					} else if (e.getCode() == KeyCode.DIGIT3) {
						p.setAttackType(Player.atkType.SCISSORS_ATTACK);
						battle(attacker, defender, p.getAttackingArmies());
					}
				});
			} else {
				alert("Defend", "Use the number keys to choose a defense. \n8 for Rock \n9 for Paper \n0 for Scissors");
				attackScreen.setOnKeyPressed(e -> {
					if (e.getCode() == KeyCode.DIGIT8) {
						p.setAttackType(Player.atkType.ROCK_DEFEND);
						battle(attacker, defender, currentPlayer.getAttackingArmies());
					} else if (e.getCode() == KeyCode.DIGIT9) {
						p.setAttackType(Player.atkType.PAPER_DEFEND);
						battle(attacker, defender, currentPlayer.getAttackingArmies());
					} else if (e.getCode() == KeyCode.DIGIT0) {
						p.setAttackType(Player.atkType.SCISSORS_DEFEND);
						battle(attacker, defender, currentPlayer.getAttackingArmies());
					}
				});
			}
		}
		attackImages.getChildren().addAll(img, title);
		title.xProperty().set(isAttacking ? 50 : 400);
		title.yProperty().set(150);
	}

	private int determineWinner(Player attacker, Player defender) {
		Player.atkType atk = attacker.getAttackType();
		Player.atkType dfnd = defender.getAttackType();

		if (atk == Player.atkType.PAPER_ATTACK && dfnd == Player.atkType.PAPER_DEFEND
				|| atk == Player.atkType.ROCK_ATTACK && dfnd == Player.atkType.ROCK_DEFEND
				|| atk == Player.atkType.SCISSORS_ATTACK && dfnd == Player.atkType.SCISSORS_DEFEND)
			return 0;
		if (atk == Player.atkType.ROCK_ATTACK && dfnd == Player.atkType.SCISSORS_DEFEND)
			return 1;
		if (atk == Player.atkType.PAPER_ATTACK && dfnd == Player.atkType.ROCK_DEFEND)
			return 1;
		if (atk == Player.atkType.SCISSORS_ATTACK && dfnd == Player.atkType.PAPER_DEFEND)
			return 1;
		else
			return -1;
	}

	public void battle(Territory attacker, Territory defender, int attackingWith) {
		boolean finished = false;
		if (!armiesLocked) {
			originalAttackingArmies = attackingWith;
			armiesLocked = true;
		}
		Player atkP = attacker.getPlayer(), dfnP = defender.getPlayer();

		if (!atkP.isHuman())
			atkP.selectAttackType(true);
		if (!dfnP.isHuman())
			dfnP.selectAttackType(false);

		int result = determineWinner(atkP, dfnP);

		if (result == 1) {
			results.setText(attacker + " wins!");
			defender.setArmies(defender.getArmies() - 1);
		} else if (result == -1) {
			results.setText(defender + " wins!");
			attackingWith--;
			atkP.setAttackingArmies(attackingWith);
		} else {
			results.setText("It's a tie!");
		}

		if (atkP.isHuman() || dfnP.isHuman())
			showAttackScreen(attacker, defender);

		if (attackingWith == 0) {
			alert("Battle Result", defender + "(" + dfnP + ") defends itself from " + attacker + "(" + atkP + ").");
			finished = true;
		} else if (defender.getArmies() == 0) {
			alert("Battle Result", atkP + " conquers " + defender + "!");
			defender.conqueredBy(attacker.getPlayer());
			defender.setArmies(attackingWith);
			finished = true;
		}

		if (finished) {
			attacker.setArmies(attacker.getArmies() - originalAttackingArmies);
			armiesLocked = false;
			atkP.stopAttack();
			attackScreen.setOnKeyPressed(null);
			atkP.setAttackType(null);
			dfnP.setAttackType(null);
			if (atkP.isHuman())
				atkP.chooseTerritory();
			if (gameOver()) {
				showGameplayScreen();
				Player winner = territories.get(0).getPlayer();
				alert("Game Over!", winner + " has conquered all of Earth! They win!");
				System.exit(0);
			}
			if (atkP.isHuman() || dfnP.isHuman())
				showGameplayScreen();
			if (dfnP.isHuman() && !atkP.isHuman())
				nextTurn();
			else if (!atkP.isHuman() && !dfnP.isHuman())
				nextTurn();
		} else if (!atkP.isHuman() && !dfnP.isHuman())
			battle(attacker, defender, attackingWith);
	}

	private boolean gameOver() {
		for (Territory t1 : territories)
			for (Territory t2 : territories)
				if (t1.getPlayer() != t2.getPlayer())
					return false;
		return true;
	}

	public Player currentPlayer() {
		return currentPlayer;
	}

	public void alert(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public void addCard(Card c) {
		deck.add(c);
	}

	public void setArmyText(String text) {
		numArmies.setText(text);
	}
}
