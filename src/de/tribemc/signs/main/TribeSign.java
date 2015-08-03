package de.tribemc.signs.main;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class TribeSign {

	private Location loc;
	private ItemStack revard;
	// Angabe in Sekunden
	private int delay;
	// Daten werden nicht gespichert
	private LinkedHashMap<String, Long> user;

	public TribeSign(Location loc, ItemStack rev, int delay) {
		this.loc = loc;
		this.revard = rev;
		this.delay = delay;
		this.user = new LinkedHashMap<>();
	}

	public Location getLocation() {
		return this.loc;
	}

	public ItemStack getItemRevard() {
		return this.revard;
	}

	public void setRevard(ItemStack revard) {
		this.revard = revard;
	}

	public int getDelayInSeconds() {
		return this.delay;
	}

	public long getNextUse() {
		return delay * 1000 + System.currentTimeMillis();
	}

	public long getRemainingTime(String spieler) {
		if (!this.user.containsKey(spieler))
			return -1;
		return this.user.get(spieler) - System.currentTimeMillis();
	}

	public void setNextUse(String spieler) {
		this.user.put(spieler, getNextUse());
	}

	public void cleanUp() {
		for (String s : this.user.keySet())
			if (this.user.get(s) < System.currentTimeMillis())
				this.user.remove(s);
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
}
