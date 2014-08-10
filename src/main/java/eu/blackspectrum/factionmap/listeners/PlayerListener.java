package eu.blackspectrum.factionmap.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import eu.blackspectrum.factionmap.FMaps;

public class PlayerListener implements Listener
{





	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRightClickMap( PlayerInteractEvent event ) {

		if ( !event.getAction().equals( Action.RIGHT_CLICK_AIR ) && !event.getAction().equals( Action.RIGHT_CLICK_BLOCK ) )
			return;

		ItemStack item = event.getPlayer().getItemInHand();

		if ( item != null && item.getType().equals( Material.MAP ) )
		{
			FMaps.Instance().getFMap( item.getDurability() ).togglePlayer( event.getPlayer().getUniqueId() );
		}
	}
	
	@SuppressWarnings("deprecation")
	public void onMapInitialize(MapInitializeEvent event)
	{
		MapView newMap = event.getMap();
		
		FMaps.Instance().addFMap( newMap.getId(), newMap );
	}

}
