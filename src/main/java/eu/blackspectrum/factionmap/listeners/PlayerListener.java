package eu.blackspectrum.factionmap.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import eu.blackspectrum.factionmap.Board;
import eu.blackspectrum.factionmap.FMapRenderer;
import eu.blackspectrum.factionmap.entities.FMap;

public class PlayerListener implements Listener
{


	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerSwitchItemHeld( PlayerItemHeldEvent event ) {
		ItemStack item = event.getPlayer().getInventory().getItem( event.getPreviousSlot() );

		if ( item != null && item.getType().equals( Material.MAP ) )
		{
			if ( Board.Instance().isFMap( item.getDurability() ) )
			{

			}
		}
	}




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRightClickMap( PlayerInteractEvent event ) {
		if ( event.isCancelled() )
			return;

//		if ( !event.getAction().equals( Action.RIGHT_CLICK_AIR ) && !event.getAction().equals( Action.RIGHT_CLICK_BLOCK ) )
//			return;

		ItemStack item = event.getPlayer().getItemInHand();

		if ( item != null && item.getType().equals( Material.MAP ) )
		{
			if(Board.Instance().isFMap( item.getDurability() ))
				return;
			
			Board.Instance().getFMap( item.getDurability() ).addRenderer( new FMapRenderer( new FMap( (short) 0 ) ) );

			event.getPlayer().sendMessage( "Activating FMap with ID: " + Board.Instance().getFMap( item.getDurability() ).getId() );

			item.setDurability( Board.Instance().getFMap( item.getDurability() ).getId() );
		}
	}
}
