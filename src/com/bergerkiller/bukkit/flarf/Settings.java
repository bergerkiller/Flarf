package com.bergerkiller.bukkit.flarf;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class Settings {
	private static Configuration config;
	public static String localfile;
	
	public static void load(JavaPlugin plugin) {
		config = plugin.getConfiguration();
		localfile = config.getString("locale", "default");
	}
	public static void save() {
		config.setProperty("locale", localfile);
		config.save();
	}
	
}
