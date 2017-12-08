import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.image.ImageView;
/**
 * A Map object representing the classic RISK map.
 * Source for images: https://snikle.wordpress.com/2010/09/03/half-completed-works-fields-of-battle/
 * @author Michael Rolland
 * @version 2017.11.28
 *
 */
public class DefaultMap extends Map {
	public DefaultMap() {
		background = new ImageView("file:resources/DefaultMap/bg.jpg");
		seeHandButton = new ImageView("file:resources/DefaultMap/handButton.png");
		endTurnButton = new ImageView("file:resources/DefaultMap/endTurnButton.png");
		width = 1604;
		height = 952;
		
		HashSet<Territory> nAmerica = new HashSet<Territory>();

		Territory alberta = new Territory("Alberta", "North America");
		Territory ontario = new Territory("Ontario", "North America");
		Territory westUS = new Territory("Western US", "North America");
		Territory eastUS = new Territory("Eastern US", "North America");
		Territory quebec = new Territory("Quebec", "North America");
		Territory nw = new Territory("Northwest Territory", "North America");
		Territory alaska = new Territory("Alaska", "North America");
		Territory greenland = new Territory("Greenland", "North America");
		Territory cAmerica = new Territory("Central America", "North America");

		nAmerica.add(alberta);
		nAmerica.add(ontario);
		nAmerica.add(nw);
		nAmerica.add(westUS);
		nAmerica.add(eastUS);
		nAmerica.add(quebec);
		nAmerica.add(alaska);
		nAmerica.add(greenland);
		nAmerica.add(cAmerica);

		alberta.addAdjacentTerritory(ontario);
		alberta.addAdjacentTerritory(westUS);
		alberta.addAdjacentTerritory(nw);
		alberta.addAdjacentTerritory(alaska);
		westUS.addAdjacentTerritory(eastUS);
		westUS.addAdjacentTerritory(ontario);
		eastUS.addAdjacentTerritory(quebec);
		eastUS.addAdjacentTerritory(ontario);
		greenland.addAdjacentTerritory(quebec);
		greenland.addAdjacentTerritory(ontario);
		greenland.addAdjacentTerritory(nw);
		cAmerica.addAdjacentTerritory(westUS);
		alaska.addAdjacentTerritory(nw);
		ontario.addAdjacentTerritory(nw);
		ontario.addAdjacentTerritory(quebec);

		HashSet<Territory> sAmerica = new HashSet<Territory>();
		
		Territory venezuela = new Territory("Venezuela", "South America");
		Territory brazil = new Territory("Brazil", "South America");
		Territory argentina = new Territory("Argentina", "South America");
		
		sAmerica.add(venezuela);
		sAmerica.add(brazil);
		sAmerica.add(argentina);
		
		cAmerica.addAdjacentTerritory(venezuela);
		brazil.addAdjacentTerritory(venezuela);
		brazil.addAdjacentTerritory(argentina);
		argentina.addAdjacentTerritory(venezuela);
		
		HashSet<Territory> africa = new HashSet<Territory>();
		
		Territory nAfrica = new Territory("North Africa", "Africa");
		Territory eAfrica = new Territory("East Africa", "Africa");
		Territory sAfrica = new Territory("South Africa", "Africa");
		Territory congo = new Territory("Congo", "Africa");
		Territory madagascar = new Territory("Madagascar", "Africa");
		Territory egypt = new Territory("Egypt", "Africa");
		
		africa.add(nAfrica);
		africa.add(eAfrica);
		africa.add(sAfrica);
		africa.add(congo);
		africa.add(madagascar);
		africa.add(egypt);
		
		brazil.addAdjacentTerritory(nAfrica);
		nAfrica.addAdjacentTerritory(congo);
		nAfrica.addAdjacentTerritory(egypt);
		eAfrica.addAdjacentTerritory(sAfrica);
		eAfrica.addAdjacentTerritory(egypt);
		congo.addAdjacentTerritory(sAfrica);
		congo.addAdjacentTerritory(eAfrica);
		congo.addAdjacentTerritory(egypt);
		madagascar.addAdjacentTerritory(eAfrica);
		madagascar.addAdjacentTerritory(sAfrica);
		
		HashSet<Territory> europe = new HashSet<Territory>();
		
		Territory iceland = new Territory("Iceland", "Europe");
		Territory england = new Territory("Great Britain", "Europe");
		Territory wEurope = new Territory("Western Europe", "Europe");
		Territory nEurope = new Territory("Northern Europe", "Europe");
		Territory sEurope = new Territory("Southern Europe", "Europe");
		Territory ukraine = new Territory("Ukraine", "Europe");
		Territory scandinavia = new Territory("Scandinavia", "Europe");
		
		europe.add(iceland);
		europe.add(england);
		europe.add(wEurope);
		europe.add(sEurope);
		europe.add(nEurope);
		europe.add(ukraine);
		europe.add(scandinavia);
		
		greenland.addAdjacentTerritory(iceland);
		nAfrica.addAdjacentTerritory(wEurope);
		nAfrica.addAdjacentTerritory(sEurope);
		egypt.addAdjacentTerritory(sEurope);
		wEurope.addAdjacentTerritory(england);
		wEurope.addAdjacentTerritory(sEurope);
		wEurope.addAdjacentTerritory(nEurope);
		england.addAdjacentTerritory(scandinavia);
		england.addAdjacentTerritory(nEurope);
		england.addAdjacentTerritory(iceland);
		ukraine.addAdjacentTerritory(scandinavia);
		ukraine.addAdjacentTerritory(nEurope);
		sEurope.addAdjacentTerritory(ukraine);
		sEurope.addAdjacentTerritory(nEurope);
		
		HashSet<Territory> asia = new HashSet<Territory>();
		
		Territory mEast = new Territory("Middle East", "Asia");
		Territory afghan = new Territory("Afghanistan", "Asia");
		Territory india = new Territory("India", "Asia");
		Territory ural = new Territory("Ural", "Asia");
		Territory siberia = new Territory("Siberia", "Asia");
		Territory china = new Territory("China", "Asia");
		Territory siam = new Territory("Siam", "Asia");
		Territory irkutsk = new Territory("Irkutsk", "Asia");
		Territory yakutsk = new Territory("Yakutsk", "Asia");
		Territory mongolia = new Territory("Mongolia", "Asia");
		Territory kamchatka = new Territory("Kamchatka", "Asia");
		Territory japan = new Territory("Japan", "Asia");
		
		asia.add(mEast);
		asia.add(afghan);
		asia.add(india);
		asia.add(ural);
		asia.add(siberia);
		asia.add(china);
		asia.add(siam);
		asia.add(irkutsk);
		asia.add(yakutsk);
		asia.add(mongolia);
		asia.add(kamchatka);
		asia.add(japan);
		
		egypt.addAdjacentTerritory(mEast);
		alaska.addAdjacentTerritory(kamchatka);
		ukraine.addAdjacentTerritory(afghan);
		ukraine.addAdjacentTerritory(mEast);
		ukraine.addAdjacentTerritory(ural);
		afghan.addAdjacentTerritory(ural);
		afghan.addAdjacentTerritory(mEast);
		afghan.addAdjacentTerritory(india);
		siberia.addAdjacentTerritory(ural);
		siberia.addAdjacentTerritory(irkutsk);
		siberia.addAdjacentTerritory(yakutsk);
		siberia.addAdjacentTerritory(mongolia);
		siberia.addAdjacentTerritory(china);
		siberia.addAdjacentTerritory(afghan);
		yakutsk.addAdjacentTerritory(kamchatka);
		yakutsk.addAdjacentTerritory(irkutsk);
		mongolia.addAdjacentTerritory(irkutsk);
		mongolia.addAdjacentTerritory(kamchatka);
		mongolia.addAdjacentTerritory(japan);
		japan.addAdjacentTerritory(kamchatka);
		china.addAdjacentTerritory(india);
		china.addAdjacentTerritory(siam);
		china.addAdjacentTerritory(afghan);
		china.addAdjacentTerritory(mongolia);
		india.addAdjacentTerritory(siam);
		india.addAdjacentTerritory(mEast);
		irkutsk.addAdjacentTerritory(kamchatka);
		
		HashSet<Territory> australia = new HashSet<Territory>();
		
		Territory indonesia = new Territory("Indonesia", "Australia");
		Territory guinea = new Territory("New Guinea", "Australia");
		Territory wAustralia = new Territory("Western Australia", "Australia");
		Territory eAustralia = new Territory("Eastern Australia", "Australia");
		
		australia.add(indonesia);
		australia.add(guinea);
		australia.add(wAustralia);
		australia.add(eAustralia);
		
		siam.addAdjacentTerritory(indonesia);
		eAustralia.addAdjacentTerritory(wAustralia);
		guinea.addAdjacentTerritory(eAustralia);
		guinea.addAdjacentTerritory(wAustralia);
		guinea.addAdjacentTerritory(indonesia);
		indonesia.addAdjacentTerritory(wAustralia);
		
		territories = new HashSet<Territory>();
		territories.addAll(nAmerica);
		territories.addAll(sAmerica);
		territories.addAll(europe);
		territories.addAll(australia);
		territories.addAll(asia);
		territories.addAll(africa);
		
		continents = new HashSet<HashSet<Territory>>();
		continents.add(nAmerica);
		continents.add(sAmerica);
		continents.add(africa);
		continents.add(europe);
		continents.add(australia);
		continents.add(asia);
		
		continentValues = new HashMap<HashSet<Territory>, Integer>();
		continentValues.put(nAmerica, 5);
		continentValues.put(sAmerica, 2);
		continentValues.put(africa, 3);
		continentValues.put(asia, 7);
		continentValues.put(europe, 5);
		continentValues.put(australia, 2);

		territories.parallelStream()
			.forEach(e -> {
				e.setImage(new ImageView("file:resources/DefaultMap/"+e.getName()+".png"));
			});
	}
}
