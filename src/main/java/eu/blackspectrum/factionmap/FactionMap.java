package eu.blackspectrum.factionmap;

import org.bukkit.plugin.java.JavaPlugin;

import eu.blackspectrum.factionmap.listeners.PlayerListener;

public class FactionMap extends JavaPlugin
{


	public void onEnable() {
		getServer().getPluginManager().registerEvents( new PlayerListener(), this );
	}




}
