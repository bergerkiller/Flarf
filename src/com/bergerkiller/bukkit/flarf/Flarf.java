package com.bergerkiller.bukkit.flarf;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Flarf extends JavaPlugin {
	private final FlarfCommand cmd = new FlarfCommand(this);
	private final FlarfPlayerListener playerListener = new FlarfPlayerListener(this);
	
	private static Logger logger = Logger.getLogger("Minecraft");
	public static void log(Level level, String message) {
		logger.log(level, message);
	}
	
	public void load() {
		Settings.load(this);
		
		Localization.init(this, Settings.localfile);
	}
	public void save() {
		Settings.save();
	}
	
	public void onEnable() {
		//Event registering
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener, Priority.Highest, this);
        
        //Commands
        getCommand("flarf").setExecutor(cmd);
        
        Permission.init(this);
        EntityKeeper.startUpdating(this, 10);
        load();
                
        //final msg
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}
	public void onDisable() {
        save();
		System.out.println("Flarf disabled!");
	}

}
