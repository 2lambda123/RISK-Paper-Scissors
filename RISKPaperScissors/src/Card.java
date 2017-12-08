import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;

public class Card {
	private Territory territory;
	private int armyType;
	private ImageView img;
	private boolean selected;

	public Card(Territory t, int armyType) {
		territory = t;
		this.armyType = armyType;
		switch (armyType) {
		case 1:
			img = new ImageView("file:resources/rock.png");
			break;
		case 2:
			img = new ImageView("file:resources/paper.png");
			break;
		case 3:
			img = new ImageView("file:resources/scissors.png");
			break;
		}
		selected = false;
	}

	public Territory getTerritory() {
		return territory;
	}

	public boolean select() {
		selected = !selected;
		if (selected) 
			img.setEffect(new InnerShadow());
		else
			img.setEffect(null);
		return selected;
	}

	public int getType() {
		return armyType;
	}

	public ImageView getImage() {
		return img;
	}

	public void setImage(ImageView image) {
		img = image;
	}

	public boolean matches(Card other) {
		return (armyType == other.armyType || territory.getContinent().equals(other.territory.getContinent()));
	}

	@Override
	public String toString() {
		return territory.toString() + " (" + territory.getContinent() + ")";
	}
}
