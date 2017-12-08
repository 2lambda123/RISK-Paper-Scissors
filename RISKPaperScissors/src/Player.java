import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.paint.Color;

public class Player {
	private boolean isHuman;
	private String name;
	private HashSet<Card> hand;
	private Color color;
	private ArrayList<Territory> territories;
	private int freeArmies;
	private atkType attack;
	private Territory attackingWith;
	private Territory attacking;
	private RiskPaperScissors game;
	private HashSet<Card> selectedCards;
	private int setsTurnedIn;
	private boolean hasArmiesChosen;
	private int numAttackingArmies;

	public enum atkType {
		ROCK_DEFEND, ROCK_ATTACK, PAPER_DEFEND, PAPER_ATTACK, SCISSORS_DEFEND, SCISSORS_ATTACK
	};

	private static final Random RNG = new Random();

	public Player(String name, boolean isHuman, Color color, RiskPaperScissors game) {
		this.isHuman = isHuman;
		this.name = name;
		this.color = color;
		this.game = game;
		freeArmies = 0;
		setsTurnedIn = 0;
		numAttackingArmies = 0;
		hasArmiesChosen = false;
		hand = new HashSet<Card>();
		territories = new ArrayList<Territory>();
		selectedCards = new HashSet<Card>();
	}

	public void setFreeArmies(int num) {
		freeArmies = num;
	}

	public int getFreeArmies() {
		return freeArmies;
	}

	public boolean hasArmies() {
		return freeArmies > 0;
	}

	public void addCard(Card c) {
		hand.add(c);
	}

	public boolean isHuman() {
		return isHuman;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public void removeTerritory(Territory t) {
		territories.remove(t);
	}

	public void addTerritory(Territory t) {
		territories.add(t);
		t.setPlayer(this);
		t.color(color);
	}

	public List<Territory> getTerritories() {
		return territories;
	}

	public atkType selectAttackType(boolean isAttacking) {
		if (isHuman) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(isAttacking ? "Attack" : "Defend");
			alert.setHeaderText(null);
			String msg="";
			if (isAttacking)
				msg = "Which attack type will you start with?";
			else
				msg = game.currentPlayer().attacking + " is under attack! What attack type do you start defending with?";
			alert.setContentText(msg);
			ButtonType rock = new ButtonType("Rock");
			ButtonType paper = new ButtonType("Paper");
			ButtonType scissors = new ButtonType("Scissors");
			alert.getButtonTypes().setAll(rock, paper, scissors);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == rock) {
				setAttackType(isAttacking ? Player.atkType.ROCK_ATTACK : Player.atkType.ROCK_DEFEND);
			} else if (result.get() == paper) {
				setAttackType(isAttacking ? Player.atkType.PAPER_ATTACK : Player.atkType.PAPER_DEFEND);
			} else if (result.get() == scissors) {
				setAttackType(isAttacking ? Player.atkType.SCISSORS_ATTACK : Player.atkType.SCISSORS_DEFEND);
			}
		} else {
			int n = RNG.nextInt(3);
			if (n == 0)
				attack = isAttacking ? atkType.ROCK_ATTACK : atkType.ROCK_DEFEND;
			else if (n == 1)
				attack = isAttacking ? atkType.PAPER_ATTACK : atkType.PAPER_DEFEND;
			else
				attack = isAttacking ? atkType.SCISSORS_ATTACK : atkType.SCISSORS_DEFEND;
		}
		return attack;
	}

	public void setAttackType(atkType atk) {
		attack = atk;
	}

	public atkType getAttackType() {
		return attack;
	}

	public void addArmy() {
		territories.get(RNG.nextInt(territories.size())).addArmy(1);
		freeArmies--;
	}

	public void chooseTerritory() {
		if (isHuman) {
			for (Territory t : territories)
				t.selectMode();
		} else {
			Territory t = territories.get(RNG.nextInt(territories.size()));
			while (t.validAdjacents().size() == 0 || t.getArmies() == 1) {
				t = territories.get(RNG.nextInt(territories.size()));
			}
			attackingWith = t;
		}
	}

	public void attackTeritory() {
		if (isHuman) {
			for (Territory t : attackingWith.validAdjacents())
				t.attackMode();
		} else {
			List<Territory> adj = attackingWith.validAdjacents();
			Territory t = adj.get(RNG.nextInt(adj.size()));
			while (t == attackingWith)
				t = adj.get(RNG.nextInt(adj.size()));
			attacking = t;
		}

	}

	public void setAttackingArmies(int armies) {
		numAttackingArmies = armies;
	}
	
	public int getAttackingArmies() {
		if (!isHuman)
			return numAttackingArmies;
		else {
			if (hasArmiesChosen)
				return numAttackingArmies;
			int i = 1;
			List<Integer> choices = new ArrayList<Integer>();
			for (; i < attackingWith.getArmies(); i++)
				choices.add(i);
			ChoiceDialog<Integer> armyChooser = new ChoiceDialog<Integer>(i-1, choices);
			armyChooser.setHeaderText(null);
			armyChooser.setTitle("Choose Attacking Armies");
			armyChooser.setContentText("How many armies will you attack with?");
			numAttackingArmies = armyChooser.showAndWait().get();
			
			hasArmiesChosen = true;
			return numAttackingArmies;
		}
	}

	public void setAttackingWith(Territory t) {
		attackingWith = t;
	}

	public RiskPaperScissors getGame() {
		return game;
	}

	public Territory getAttackingWith() {
		return attackingWith;
	}

	public void stopAttack() {
		hasArmiesChosen = false;
	}
	
	public Territory getAttacking() {
		return attacking;
	}

	public void setAttacking(Territory t) {
		attacking = t;
	}

	public Set<Card> getHand() {
		return hand;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public void selectCard(Card c) {
		boolean selected = c.select();
		if (selected) {
			selectedCards.add(c);
			if (selectedCards.size() == 3) {
				if (!checkForMatches()) {
					game.alert("Mismatch", "Those cards do not match.");
					selectedCards.remove(c);
					c.select();
				}
			}
		} else
			selectedCards.remove(c);
	}

	public int getSetsTurnedIn() {
		return setsTurnedIn;
	}

	public boolean checkForMatches() {
		Set<Card> cards = (isHuman ? selectedCards : hand);
		for (Card c1 : cards)
			for (Card c2 : cards)
				for (Card c3 : cards)
					if (c1 != c2 && c2 != c3 && c1 != c3 && c1.matches(c2) && c2.matches(c3)) {
						removeCard(c1);
						removeCard(c2);
						removeCard(c3);
						selectedCards.remove(c1);
						selectedCards.remove(c2);
						selectedCards.remove(c3);
						setsTurnedIn++;
						game.turnInSet(this);
						return true;
					}
		return false;
	}

	private void removeCard(Card c) {
		hand.remove(c);
		game.addCard(c);
	}
	
	public SimpleStringProperty getArmiesProperty() {
		return new SimpleStringProperty(freeArmies + " armies left");
	}
}
