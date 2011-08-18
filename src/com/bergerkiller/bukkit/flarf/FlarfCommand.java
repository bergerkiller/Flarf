package com.bergerkiller.bukkit.flarf;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlarfCommand implements CommandExecutor {
	private final Flarf plugin;
	public FlarfCommand(final Flarf instance) {
		this.plugin = instance;
	}

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		boolean showusage = false;
		boolean showinvalid = true;
		if (args.length == 0) {
			showusage = true;
			showinvalid = false;
		} else {
			Permission.Node node = Permission.getNode(args[0]);
			if (node == Permission.Node.NONE) {
				showusage = true;
				showinvalid = !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("?");
			} else {
				if (Permission.has(sender, node)) {
					//========================================
					//=======player and console commands======
					//========================================
					if (node == Permission.Node.LIST) {
						String[] arenanames = Arena.getArenaNames();
						if (arenanames.length == 0) {
							sender.sendMessage(Localization.get("notify.noarenasfound"));
						} else {
							sender.sendMessage(Localization.get("notify.arenasfound") + FlarfUtil.combineNames(arenanames));
						}
					} else if (node == Permission.Node.RELOAD) {
						this.plugin.load();
						sender.sendMessage(Localization.get("notify.reloaded"));
					} else if (sender instanceof Player) {
						//============================
						//=======player commands======
						//============================
						Player p = (Player) sender;
						if (node == Permission.Node.JOIN) {
							String name = "default";
							if (args.length > 1) {
								name = FlarfUtil.combineWords(1, args);
							}
							Arena a = Arena.matchArena(name);
							if (a == null) {
								p.sendMessage(Localization.get("notify.arenanotfound") + FlarfUtil.combineNames(name));
							} else {
								if (a.join(p)) {
									p.sendMessage(Localization.get("notify.arenajoin"));
								} else {
									p.sendMessage(Localization.get("notify.arenafull"));
								}								
							}
						} else if (node == Permission.Node.LEAVE) {
							if (Arena.leaveArena(p)) {
								p.sendMessage(Localization.get("notify.arenaleft"));
							} else {
								p.sendMessage(Localization.get("notify.arenanotin"));
							}
						} else if (node == Permission.Node.SPECTATE) {
							
						} else if (node == Permission.Node.CREATEARENA) {
							String name = "default";
							if (args.length > 1) {
								name = FlarfUtil.combineWords(1, args);
							}
							Arena a = Arena.create(name, p);
							if (a == null) {
								p.sendMessage(Localization.get("notify.nametaken.start") + name + Localization.get("notify.nametaken.end"));
							} else {
								p.sendMessage(Localization.get("notify.arenacreated"));
							}
						} else if (node == Permission.Node.EDITARENA) {
							String name = "default";
							if (args.length > 1) {
								name = FlarfUtil.combineWords(1, args);
							}
							Arena a = Arena.edit(name, p);
							if (a == null) {
								p.sendMessage(Localization.get("notify.arenanotfound") + FlarfUtil.combineNames(name));
							} else {
								p.sendMessage(Localization.get("notify.arenaedited"));
							}
						} else if (node == Permission.Node.ADDPLAYER) {
							int cap = 1;
							if (args.length == 2) {
								try {
									cap = Integer.parseInt(args[1]);
								} catch (Exception ex) {};
							}
							Arena.addPlayerSlot(p, cap);
							p.sendMessage(Localization.get("notify.slotadded"));
						} else if (node == Permission.Node.ADDSPECTATOR) {
							int cap = 5;
							if (args.length == 2) {
								try {
									cap = Integer.parseInt(args[1]);
								} catch (Exception ex) {};
							}
							Arena.addSpectatorSlot(p, cap);
							p.sendMessage(Localization.get("notify.slotadded"));
						}
					} else {
						sender.sendMessage(Localization.get("notify.invalid.sender"));
						showinvalid = false;
						showusage = true;
					}
				} else if (sender instanceof Player) {
					sender.sendMessage(Localization.get("notify.nopermission.command"));
				}				
			}
		}
		if (showusage) {
			if (showinvalid) {
				sender.sendMessage(Localization.get("notify.invalid.command"));
			}
			sender.sendMessage(Localization.get("help.list.command"));
			if (sender instanceof Player) {
				Player p = (Player) sender;
				//player help info
				if (Permission.has(p, Permission.Node.JOIN)) p.sendMessage(Localization.get("help.command.join"));
				if (Permission.has(p, Permission.Node.LEAVE)) p.sendMessage(Localization.get("help.command.leave"));	
				if (Permission.has(p, Permission.Node.SPECTATE)) p.sendMessage(Localization.get("help.command.spectate"));
			} else {
				//console help info
				sender.sendMessage(Localization.get("help.command.reload"));
			}
			
		}
		return true;
	}

}
