package com.bergerkiller.bukkit.flarf;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlarfPlayerListener extends PlayerListener {
	private final Flarf plugin;
	public FlarfPlayerListener(final Flarf instance) {
		this.plugin = instance;
	}
	
	@Override
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof LivingEntity) {
			EntityKeeper.keep(event.getRightClicked(), event.getRightClicked().getLocation(), 4);
		}
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		EntityKeeper.handleMove(event);
	}
}
