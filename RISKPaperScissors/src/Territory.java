import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Territory {
	private Player player;
	private String name;
	private int numArmies;
	private ArrayList<Territory> adjacentTo;
	private ImageView img;
	private String continent;
	private Color color;
	private String text;
	private boolean selected;

	public Territory(String name, String continent) {
		this.name = name;
		this.continent = continent;
		numArmies = 0;
		player = null;
		text = "";
		selected = false;
		adjacentTo = new ArrayList<Territory>();
	}

	public String getName() {
		return name;
	}

	public String getContinent() {
		return continent;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player p) {
		player = p;
	}

	public void conqueredBy(Player conqueror) {
		player.removeTerritory(this);
		conqueror.addTerritory(this);
		player = conqueror;
		color(conqueror.getColor());
	}

	public ImageView getImage() {
		return img;
	}

	public void setImage(ImageView image) {
		img = image;
	}

	public void addInitialArmiesMode() {
		img.setOnMouseClicked(e -> {
			numArmies++;
			System.out.println(this + " now has " + numArmies + " armies");
			player.setFreeArmies(player.getFreeArmies() - 1);
			player.getGame().setArmyText(player.getFreeArmies()+" armies left.");
			if (player.getFreeArmies() == 0) {
				if (player.getGame().checkTerritories(player)) {
					for (Territory t : player.getTerritories())
						t.disable();
					player.getGame().placeNextInitialArmies();
				} else {
					player.getGame().alert("Invalid Armies", "Some of your territories have no armies!");
					for (Territory t : player.getTerritories())
						t.setArmies(0);
					player.getGame().placeInitialArmies(player);
				}
			}
		});
	}

	public void addArmyMode() {
		img.setOnMouseClicked(e -> {
			numArmies++;
			System.out.println(this + " now has " + numArmies + " armies");
			player.setFreeArmies(player.getFreeArmies() - 1);
			player.getGame().setArmyText(player.getFreeArmies()+" armies left.");
			if (player.getFreeArmies() == 0) {
				for (Territory t : player.getTerritories())
					t.disable();
				player.getGame().setArmyText("");
				player.getGame().alert("Armies Placed", "Armies placed. Make as many attacks as you wish.");
				player.chooseTerritory();
			}
		});
	}

	public void selectMode() {
		img.setOnMouseClicked(e -> {
			player.getGame().setArmyText("");
			if (validAdjacents().size() == 0)
				player.getGame().alert("No Valid Adjacents", "This territory has no valid adjacent territories!");
			else if (numArmies == 1)
				player.getGame().alert("Not Enough Armies", "This territory only has one army!");
			else {
				for (Territory t : this.validAdjacents())
					t.img.setEffect(new InnerShadow());
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Are you sure?");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to attack with " + this + "?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					player.setAttackingWith(this);
					for (Territory t : player.getTerritories())
						t.disable();
					player.attackTeritory();
				} else {
					for (Territory t : this.validAdjacents())
						t.disable();
				}
			}
		});
	}

	public void attackMode() {
		img.setOnMouseClicked(e -> {
			player.getGame().currentPlayer().setAttacking(this);
			for (Territory t : player.getGame().currentPlayer().getAttackingWith().validAdjacents())
				t.disable();
			player.selectAttackType(false);
			player.getGame().currentPlayer().selectAttackType(true);
			player.getGame().battle(player.getGame().currentPlayer().getAttackingWith(), this, player.getGame().currentPlayer().getAttackingArmies());
		});
	}

	public void disable() {
		img.setOnMouseClicked(null);
		img.setEffect(getColor());
	}

	private Lighting getColor() {
		Lighting lighting = new Lighting();
		lighting.setDiffuseConstant(1.0);
		lighting.setSpecularConstant(0.0);
		lighting.setSpecularExponent(0.0);
		lighting.setSurfaceScale(0.0);
		lighting.setLight(new Light.Distant(45, 45, color));
		return lighting;
	}

	public void color(Color color) {
		Lighting lighting = new Lighting();
		lighting.setDiffuseConstant(1.0);
		lighting.setSpecularConstant(0.0);
		lighting.setSpecularExponent(0.0);
		lighting.setSurfaceScale(0.0);
		lighting.setLight(new Light.Distant(45, 45, color));

		img.setEffect(lighting);
		this.color = color;
	}

	public void addArmy(int num) {
		numArmies += num;
		System.out.println(this + " now has " + numArmies + " armies.");
	}

	public int getArmies() {
		return numArmies;
	}

	public void setArmies(int num) {
		numArmies = num;
	}

	public void addAdjacentTerritory(Territory t) {
		adjacentTo.add(t);
		t.adjacentTo.add(this);
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean hasArmies() {
		return numArmies > 0;
	}

	public List<Territory> validAdjacents() {
		ArrayList<Territory> valid = new ArrayList<Territory>();
		for (Territory t : adjacentTo)
			if (t.player != player)
				valid.add(t);
		return valid;
	}

	public String getText() {
		return (this + " (" + player + "): " + numArmies + " armies");
	}
}
