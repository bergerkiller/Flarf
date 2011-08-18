package com.bergerkiller.bukkit.flarf;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Arena {
	private static HashMap<String, Arena> managedArenas = new HashMap<String, Arena>();
	private static ArrayList<Arena> arenas = new ArrayList<Arena>();
	public static Arena getArena(String name) {
		//direct match first
		for (Arena arena : arenas) {
			if (arena.name.equalsIgnoreCase(name)) return arena;
		}
		return null;
	}
	public static Arena matchArena(String name) {
		Arena rval = getArena(name);
		if (rval == null) {
			//matching starts
			for (Arena arena : arenas) {
				if (arena.matchName(name)) rval = arena;
			}
		}
		return rval;
	}
	public static String[] getArenaNames() {
		String[] rval = new String[arenas.size()];
		for (int i = 0;i < rval.length;i++) rval[i] = arenas.get(i).name;
		return rval;
	}
	public static Arena create(String name, Player by) {
		if (getArena(name) != null) return null;
		Arena a = new Arena();
		a.name = name;
		arenas.add(a);
		managedArenas.put(by.getName(), a);
		return a;
	}
	public static Arena edit(String name, Player by) {
		Arena a = matchArena(name);
		if (a != null) managedArenas.put(by.getName(), a);
		return a;
	}
	public static Arena getManaged(Player by) {
		return managedArenas.get(by.getName());
	}
	public static boolean addPlayerSlot(Player at, int capacity) {
		Arena a = getManaged(at);
		if (a == null) return false;
		PlayerSlot s = new PlayerSlot(at.getLocation());
		s.minplayers = capacity;
		s.maxplayers = capacity;
		a.playerslots.add(s);
		return true;
	}
	public static boolean addSpectatorSlot(Player at, int capacity) {
		Arena a = getManaged(at);
		if (a == null) return false;
		PlayerSlot s = new PlayerSlot(at.getLocation());
		s.minplayers = 0;
		s.maxplayers = capacity;
		a.spectatorslots.add(s);
		return true;
	}
	public static boolean leaveArena(Player p) {
		for (Arena a : arenas) {
			if (a.leave(p)) return true;
		}
		return false;
	}
		
	private String name;
	public ArrayList<PlayerSlot> playerslots = new ArrayList<PlayerSlot>();
	public ArrayList<PlayerSlot> spectatorslots = new ArrayList<PlayerSlot>();
	public Location mobspawnpoint;
	
	public boolean matchName(String name) {
		return FlarfUtil.matchName(this.name, name);
	}
	
	public boolean readyToPlay() {
		for (PlayerSlot slot : playerslots) {
			if (slot.isEmpty()) return false;
		}
		for (PlayerSlot slot : spectatorslots) {
			if (slot.isEmpty()) return false;
		}
		return true;
	}
	public boolean leave(Player p) {
		for (PlayerSlot slot : playerslots) {
			if (slot.remove(p)) return true;
		}
		for (PlayerSlot slot : spectatorslots) {
			if (slot.remove(p)) return true;
		}
		return false;
	}
	public boolean join(Player p) {
		for (PlayerSlot slot : playerslots) {
			if (!slot.isFull()) {
				slot.add(p);
				return true;
			}
		}
		return false;
	}
	public boolean spectate(Player p) {
		for (PlayerSlot spec : spectatorslots) {
			if (!spec.isFull()) {
				spec.add(p);
				return true;
			}
		}
		return false;
	}
	
}
