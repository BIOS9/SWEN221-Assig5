// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package model;

/**
 * Settlements have a type and an owner in the form of a player.
 *
 * @author Julian Mackay
 *
 */
public class Settlement {
	private SettlementType type;
	private final Player player;

	public Settlement(Player player) {
		this.type = SettlementType.TOWN;
		this.player = player;
	}

	/**
	 * Upgrade a town to a city.
	 *
	 * @return
	 */
	public Boolean upgrade() {
		if (type.equals(SettlementType.TOWN)) {
			this.type = SettlementType.CITY;
			this.player.score(1);
			return true;
		} else
			return false;
	}

	public Player getPlayer() {
		return player;
	}

	public SettlementType getType() {
		return this.type;
	}
}
