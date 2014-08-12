package eu.blackspectrum.factionmap.listeners;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.map.CraftMapRenderer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import eu.blackspectrum.factionmap.FMapRenderer;
import eu.blackspectrum.factionmap.FMaps;

public class PlayerListener implements Listener
{


	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMapInitialize( final MapInitializeEvent event ) {
		final MapView newMap = event.getMap();

		if ( newMap != null )
		{
			// Throw away all default map renderer
			for ( final MapRenderer rend : newMap.getRenderers() )
				if ( rend instanceof CraftMapRenderer )
					newMap.removeRenderer( rend );

			// If no renderer left add FMapRenderer
			if ( newMap.getRenderers().size() == 0 )
				newMap.addRenderer( new FMapRenderer() );

		}
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRightClickMap( final PlayerInteractEvent event ) {

		if ( !event.getAction().equals( Action.RIGHT_CLICK_AIR ) && !event.getAction().equals( Action.RIGHT_CLICK_BLOCK ) )
			return;
		
		final ItemStack item = event.getPlayer().getItemInHand();

		if ( item != null && item.getType().equals( Material.MAP ) )
			FMaps.Instance().getFMap( item.getDurability() ).togglePlayer( event.getPlayer().getUniqueId() );
	}

}
