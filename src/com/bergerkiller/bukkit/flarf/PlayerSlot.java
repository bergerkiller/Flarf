package com.bergerkiller.bukkit.flarf;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerSlot {
	public PlayerSlot(Location loc) {
		this.location = loc;
	}
	private Location location;
	public int minplayers = 1;
	public int maxplayers = 1;
	private ArrayList<Player> players = new ArrayList<Player>();
	public Location getLocation() {
		return location;
	}
	public int getCount() {
		return players.size();
	}
	public boolean isEmpty() {
		return players.size() < minplayers;
	}
	public boolean isFull() {
		return players.size() >= maxplayers;
	}
	public Player[] getPlayers() {
		return players.toArray(new Player[0]);
	}
	public void add(Player p) {
		players.add(p);
		EntityKeeper.keep(p, location);
	}
	public boolean remove(Player p) {
		if (players.remove(p)) {
			EntityKeeper.release(p);
			return true;
		} else {
			return false;
		}
	}
	public void clear() {
		for (Player p : players) {
			EntityKeeper.release(p);
		}
		players.clear();
	}
}