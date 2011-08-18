package com.bergerkiller.bukkit.flarf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Localization {
	private static HashMap<String, String> messages = new HashMap<String, String>();
	public static String get(String key) {
		String message = messages.get(key);
		if (message == null) {
			Flarf.log(Level.WARNING, "[Flarf] Localization key not found: " + key);
			messages.put(key, key);
			return key;
		}
	    //color codes
		int index = 0;
		while (true) {
			index = message.indexOf("$", index);
			if (index >= 0 && index < message.length() - 1) {
				String n = message.substring(index + 1, index + 2);
				String repl = "$" + n;
				//set repl
				if (n.equals("0")) {
					repl = ChatColor.BLACK.toString();
				} else if (n.equals("1")) {
					repl = ChatColor.DARK_BLUE.toString();
				} else if (n.equals("2")) {
					repl = ChatColor.DARK_GREEN.toString();
				} else if (n.equals("3")) {
					repl = ChatColor.DARK_AQUA.toString();
				} else if (n.equals("4")) {
					repl = ChatColor.DARK_RED.toString();
				} else if (n.equals("5")) {
					repl = ChatColor.DARK_PURPLE.toString();
				} else if (n.equals("6")) {
					repl = ChatColor.GOLD.toString();
				} else if (n.equals("7")) {
					repl = ChatColor.GRAY.toString();
				} else if (n.equals("8")) {
					repl = ChatColor.DARK_GRAY.toString();
				} else if (n.equals("9")) {
					repl = ChatColor.BLUE.toString();
				} else if (n.equals("a")) {
					repl = ChatColor.GREEN.toString();
				} else if (n.equals("b")) {
					repl = ChatColor.AQUA.toString();
				} else if (n.equals("c")) {
					repl = ChatColor.RED.toString();
				} else if (n.equals("d")) {
					repl = ChatColor.LIGHT_PURPLE.toString();
				} else if (n.equals("e")) {
					repl = ChatColor.YELLOW.toString();
				} else if (n.equals("f")) {
					repl = ChatColor.WHITE.toString();
				}
				//repl
				message = message.substring(0, index) + repl + message.substring(index + 2);
				index += 1;
			} else {
				break;
			}
			index += 1;
		}
		return message;
	}
	public static void init(JavaPlugin plugin, String localname) {
		messages.clear();
		//write some default messages here
		//help
		messages.put("help.list.command", "$eYou can use the following commands:");
		messages.put("help.command.join", "$f    /flarf join [arena name] - Join the arena as a contestant");
		messages.put("help.command.leave", "$f    /flarf leave - Leave the arena you are in");
		messages.put("help.command.spectate", "$f    /flarf spectate [arena name] - Join the arena as a spectator");
		messages.put("help.command.reload", "$f    /flarf reload - reloads the settings from the configuration file");
		
		//notify
		messages.put("notify.nopermission.command", "$cYou do not have permission to use this command!");
		messages.put("notify.nopermission.use", "$cYou do not have permission to use this!");
		messages.put("notify.invalid.command", "$cInvalid command!");
		messages.put("notify.invalid.name", "$cName not found!");
		messages.put("notify.invalid.sender", "$cThis command is only for players!");
		messages.put("notify.noarenasfound", "$eNo arenas found!");
		messages.put("notify.arenasfound", "$eAvailable arenas: ");
		messages.put("notify.arenanotfound", "$eArena not found: ");
		messages.put("notify.arenacreated", "$aArena created!");
		messages.put("notify.arenaedited", "$aYou are now editing this arena!");
		messages.put("notify.slotadded", "$eSlot added!");
		messages.put("notify.reloaded", "$eFlarf has been reloaded!");
		messages.put("notify.nametaken.start", "$eThe name $f'");
		messages.put("notify.nametaken.end", "$f'$e is already taken!");
		messages.put("notify.arenajoin", "$eYou joined the arena!");
		messages.put("notify.arenaleft", "$cYou left the arena!");	
		messages.put("notify.arenanotin", "$cYou are not in an arena!");	
		messages.put("notify.arenafull", "$cThis arena is full!");
		
		//common
		messages.put("common.andseparator", "$f and ");
		messages.put("common.separator", "$f, ");
		messages.put("common.name.start", "$f'$e");
		messages.put("common.name.end", "$f'");
		
		File locale = new File(plugin.getDataFolder() + File.separator + "locale");
		if (!locale.exists()) locale.mkdirs();
		//write to default file if not present
		File defaultloc = new File(locale + File.separator + "default.txt");
		if (!defaultloc.exists()) {
			try {
				BufferedWriter w = new BufferedWriter(new FileWriter(defaultloc));
				w.write("# This is a localization file. In here key-sentence pairs are stored.");
				w.newLine();
				w.write("# $x characters can be used to indiciate various colors. (e.g. $f for white)");
				for (String key : messages.keySet()) {
					w.newLine();
					w.write(key + " " + messages.get(key));
				}
				w.close();
			} catch (IOException exception) {
				Flarf.log(Level.WARNING, "[Flarf] Failed to write defaults to locale\\defaults.txt!");
				exception.printStackTrace();
			}
		}
		//read locale
		locale = new File(locale + File.separator + localname + ".txt");
		if (locale.exists()) {
			messages.clear();
			try {
				BufferedReader r = new BufferedReader(new FileReader(locale));
				String textline = null;
				while((textline = r.readLine()) != null) {
					if (!textline.startsWith("#")) {
						int firstspace = textline.indexOf(" ");
						if (firstspace > 0) {
							String key = textline.substring(0, firstspace);
							String message = textline.substring(firstspace + 1);
							messages.put(key, message);
						}
					}
				}
				r.close();
			} catch (IOException exception) {
				Flarf.log(Level.WARNING, "[Flarf] Failed to read the localization from locale\\" + localname + ".txt!");
				exception.printStackTrace();
			}
		}
	}

}
