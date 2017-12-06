import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.image.ImageView;

public abstract class Map {
	protected HashSet<Territory> territories;
	protected HashSet<HashSet<Territory>> continents;
	protected HashMap<HashSet<Territory>, Integer> continentValues;
	protected double width;
	protected double height;
	protected ImageView seeHandButton;
	protected ImageView endTurnButton;
	protected ImageView background;
		
	public ImageView getBackground() {
		return background;
	}
	public HashSet<Territory> getTerritories() {
		return territories;
	}
	public HashSet<HashSet<Territory>> getContinents() {
		return continents;
	}
	public HashMap<HashSet<Territory>, Integer> getContinentValues() {
		return continentValues;
	}
	public ImageView getSeeHandButton() {
		return seeHandButton;
	}
	public ImageView getEndTurnButton() {
		return endTurnButton;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
}
