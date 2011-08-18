package com.bergerkiller.bukkit.flarf;

import java.util.logging.Level;

import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijiko.permissions.PermissionHandler;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Permission {
	/*
	 * The following permission nodes are used:
	 * flarf.user.join
	 * flarf.user.leave
	 * flarf.user.spectate
	 * 
	 * 
	 * 
	 */
	
	private static PermissionHandler permissionHandler = null;
	public static void init(JavaPlugin plugin) {
		Plugin permissionsPlugin = plugin.getServer().getPluginManager().getPlugin("Permissions");

		if (permissionsPlugin == null) {
		    Flarf.log(Level.WARNING, "[Flarf] Permission system not detected, defaulting to OP");
		    return;
		}
		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		Flarf.log(Level.INFO, "[Flarf] Found and will use permissions plugin "+((Permissions)permissionsPlugin).getDescription().getFullName());
	}
	public static boolean has(CommandSender sender, Node command) {
		if (sender instanceof Player) return has((Player) sender, command);
		return true;
	}
	public static boolean has(Player player, Node command) { 
		return has(player, command.toString());
	}
	public static boolean has(Player player, String command) {
		if (permissionHandler == null) {
			//some standard stuff that is allowed for everyone
			final String[] usercommands = new String[] {"flarf.user.join", "flarf.user.leave", "flarf.user.spectate", "flarf.user.list"};
			for (String usercommand : usercommands) {
				if (command.equalsIgnoreCase(usercommand)) return true;
			}
			return player.isOp();
		} else {
			return permissionHandler.has(player, command);
		}
	}
	
	public enum Node {
		NONE {
		    public String toString() {
		        return "";
		    }
		},
		JOIN {
		    public String toString() {
		        return "flarf.user.join";
		    }
		},
		LEAVE {
		    public String toString() {
		        return "flarf.user.leave";
		    }
		},
		SPECTATE {
		    public String toString() {
		        return "flarf.user.spectate";
		    }
		},
		LIST {
		    public String toString() {
		        return "flarf.user.list";
		    }
		},
		CREATEARENA {
		    public String toString() {
		        return "flarf.admin.create";
		    }
		},
		EDITARENA {
		    public String toString() {
		        return "flarf.admin.edit";
		    }
		},
		RELOAD {
		    public String toString() {
		        return "flarf.admin.reload";
		    }
		},
		ADDPLAYER {
		    public String toString() {
		        return "flarf.admin.addplayer";
		    }
		},
		ADDSPECTATOR {
		    public String toString() {
		        return "flarf.admin.addspectator";
		    }
		}
	}
	
	public static Node getNode(String subcommand) {
		subcommand = subcommand.toLowerCase();
		if (subcommand.equals("join") || subcommand.equals("j")) {
			return Node.JOIN;
		} else if (subcommand.equals("leave") || subcommand.equals("l")) {
			return Node.LEAVE;
		} else if (subcommand.equals("spectate") || subcommand.equals("spect")) {
			return Node.SPECTATE;
		} else if (subcommand.equals("spec") || subcommand.equals("s")) {
			return Node.SPECTATE;
		} else if (subcommand.equals("list") || subcommand.equals("lst")) {
			return Node.LIST;
		} else if (subcommand.equals("create") || subcommand.equals("c")) {
			return Node.CREATEARENA;
		} else if (subcommand.equals("edit") || subcommand.equals("e")) {
			return Node.EDITARENA;
		} else if (subcommand.equals("reload")) {
			return Node.RELOAD;
		} else if (subcommand.equals("addplayer") || subcommand.equals("addplayerslot")) {
			return Node.ADDPLAYER;
		} else if (subcommand.equals("addspectator") || subcommand.equals("addspectatorslot")) {
			return Node.ADDSPECTATOR;
		}
		return Node.NONE;
	}
	
	
}
