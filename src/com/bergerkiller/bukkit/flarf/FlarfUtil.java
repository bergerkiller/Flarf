package com.bergerkiller.bukkit.flarf;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class FlarfUtil {
	public static String combineWords(String... words) {
		return combineWords(0, words);
	}
	public static String combineWords(int startindex, String... words) {
		String rval = "";
		int index = 0;
		for (String word : words) {
			if (index >= startindex) {
				if (rval != "") rval += " ";
				rval += word;
			}
			index++;
		}
		return rval;
	}
	public static String combineNames(String... names) {
		if (names.length == 0) return "";
    	if (names.length == 1) return Localization.get("common.name.start") + names[0] + Localization.get("common.name.end");
    	int count = 1;
    	String name = "";
    	for (String item : names) {
    		name += Localization.get("common.name.start") + item + Localization.get("common.name.end");
    		if (count == names.length - 1) {
    			name += Localization.get("common.andseparator");
    		} else if (count != names.length) {
    			name += Localization.get("common.separator");
    		}
    		count++;
    	}
		return name;
	}
	public static boolean matchName(String name, String matchname) {
		name = name.toLowerCase().trim();
		matchname = matchname.toLowerCase().trim();
		if (name.equals(matchname)) return true;
		if (name.contains(matchname)) return true;
		return false;
	}

	public static Location getMiddle(Block... values) {
		Location[] loc = new Location[values.length];
		for (int i = 0;i < loc.length;i++) loc[i] = values[i].getLocation();
		return getMiddle(loc);
	}
	public static Location getMiddle(Location... values) {
		if (values.length == 0) return null;
		double x = 0;
		double y = 0;
		double z = 0;
		for (Location loc : values) {
			x += loc.getX() / values.length;
			y += loc.getY() / values.length;	
			z += loc.getZ() / values.length;	
		}
		return new Location(values[0].getWorld(), x, y, z);
	}
	
	private static boolean addSurfaceNeighbours(Block middle, Collection<Block> blocks) {
		boolean changed = false;
		final BlockFace[] searchdirs = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};
		for (BlockFace dir : searchdirs) {
			Block b = middle.getRelative(dir);
			if (b != null && b.getType() == middle.getType()) {
				if (!blocks.contains(b)) {
					//air/water above?
					Block above = b.getRelative(BlockFace.UP);
					if (above == null || above.getType() == Material.AIR || above.getType() == Material.WATER) {
						blocks.add(b);
						changed = true;
					}
				}
			}
		}
		return changed;
	}
	public static Block[] getSurfaceBlocks(Block middle, int radius) {
		if (middle == null || radius <= 0) return new Block[0];
		HashSet<Block> rval = new HashSet<Block>();
		if (addSurfaceNeighbours(middle, rval)) {
			//success! add the neighbours around the new blocks too
			for (Block b : rval.toArray(new Block[0])) {
				for (Block b2 : getSurfaceBlocks(b, radius - 1)) {
					rval.add(b2);
				}
			}
		}
		return rval.toArray(new Block[0]);
	}
	public static double getSurfaceRadius(Location at) {
		Block atb = at.getBlock();
		while (atb.getType() == Material.AIR || atb.getType() == Material.WATER) {
			atb = atb.getRelative(BlockFace.DOWN);
			if (atb.getLocation().getBlockY() <= 0) return 0;
		}
		double maxrad = 0;
		for (Block b : getSurfaceBlocks(atb, 10)) {
			double dist = b.getLocation().distance(atb.getLocation());
			if (dist > maxrad) maxrad = dist;
		}
		return maxrad;
	}
	
}
