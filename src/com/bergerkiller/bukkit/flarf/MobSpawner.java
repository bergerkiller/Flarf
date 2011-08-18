package com.bergerkiller.bukkit.flarf;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class MobSpawner {
	public MobSpawner(Location loc) {
		location = loc;
	}
	private Location location;
	private ArrayList<Wave> waves = new ArrayList<Wave>();
		
	public static class Wave {
		private HashMap<Entity, Integer> animals = new HashMap<Entity, Integer>();
		public void add(Entity e, int amount) {
			animals.put(e, amount);
		}
	}
	
}
