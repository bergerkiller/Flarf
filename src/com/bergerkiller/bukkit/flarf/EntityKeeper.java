package com.bergerkiller.bukkit.flarf;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityKeeper {
	private static HashMap<Entity, Region> regions = new HashMap<Entity, Region>();
	
	private static class Region {
		public Location middle;
		public double radius;
		public boolean isIn(Entity e) {
			return isIn(e.getLocation());
		}
		public boolean isIn(Location loc) {
			if (loc.getWorld() != middle.getWorld()) return false;
			if (Math.abs(loc.getY() - middle.getY()) > 2) return false;
			return loc.distance(middle) <= radius;
		}
	}

	public static void update() {
		for (Entity e : regions.keySet()) {
			if (!(e instanceof Player)) {
				Region r = regions.get(e);
				if (!r.isIn(e)) {
					e.teleport(r.middle);
				}
			}
 		}
	}
	public static void startUpdating(JavaPlugin plugin, int interval) {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				update();
			}
		}, 0, interval);
	}
	
	public static void handleMove(PlayerMoveEvent event) {
		Region r = regions.get(event.getPlayer());
		if (r == null) return;
		if (!r.isIn(event.getTo())) {
			event.setTo(r.middle);
		}
	}
	public static void keep(Entity e, Location at, double radius) {
		Region r = new Region();
		r.middle = at;
		r.radius = radius;
		regions.put(e, r);
	}
	public static void keep(Entity e, Location at) {
		keep(e, at, FlarfUtil.getSurfaceRadius(at));
	}
	public static void release(Entity e) {
		regions.remove(e);
	}
}