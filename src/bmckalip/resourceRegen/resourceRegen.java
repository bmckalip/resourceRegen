package bmckalip.resourceRegen;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class resourceRegen extends JavaPlugin implements Listener {
	Configuration config = getConfig();
	@EventHandler
	public void regenNoobOre(BlockBreakEvent event){
		World world = Bukkit.getWorld(config.getString("world"));
		Block block = event.getBlock();
		
		ProtectedRegion region = getWorldGuard().getRegionManager(world).getRegion(config.getString("region"));
		Location blockLocation = event.getBlock().getLocation();
		int x = blockLocation.getBlockX();
		int y = blockLocation.getBlockY();
		int z = blockLocation.getBlockZ();
		Vector blockVector = new Vector(x, y, z);
			if(region != null && region.contains(blockVector) && event.getBlock().getWorld().equals(world)) replace(block);
	}
	@EventHandler
	public void regenSand(EntityChangeBlockEvent event){
		World world = Bukkit.getWorld(config.getString("world"));
		Block block = event.getBlock();
		
		ProtectedRegion region = getWorldGuard().getRegionManager(world).getRegion(config.getString("region"));
		Location blockLocation = event.getBlock().getLocation();
		int x = blockLocation.getBlockX();
		int y = blockLocation.getBlockY();
		int z = blockLocation.getBlockZ();
		Vector blockVector = new Vector(x, y, z);
		if(region != null && region.contains(blockVector) && event.getBlock().getWorld().equals(world)) replace(block);
	}
	public void onEnable(){
		config.options().copyDefaults(true);
		saveConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	    return (WorldGuardPlugin) plugin;
    }
    
	public void replace(Block block){
		int seconds = getConfig().getInt("delay") * 20;
		Random random = new Random();
		int multiplier = (random.nextInt(90) + 1) * 20;
		int delay = multiplier + seconds;
		
		if(block.getType().equals(Material.AIR)){
			block.setType(Material.SAND);
		}
		
		final BlockState state = block.getState();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){public void run(){state.update(true, false);}}, delay);
	}
}

