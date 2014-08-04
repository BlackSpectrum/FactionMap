package eu.blackspectrum.factionmap;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;

import eu.blackspectrum.factionmap.entities.FMap;

public class FMapRenderer extends MapRenderer
{


	private final FMap	fMap;

	private final int	VIEW_DISTANCE	= 9;




	public FMapRenderer(FMap fMap) {
		this.fMap = fMap;

	}




	@Override
	public void render( MapView map, MapCanvas canvas, Player player ) {

		String playerWorld = player.getWorld().getName();

		int scaleMod = map.getScale().ordinal();
		int step = 16 >> scaleMod;

		// These are WORLD coordinates
		int topLeftX = map.getCenterX() - ( 64 << scaleMod );
		int topLeftZ = map.getCenterZ() - ( 64 << scaleMod );

		int playerMapX = ( player.getLocation().getBlockX() - topLeftX ) >> scaleMod;
		int playerMapZ = ( player.getLocation().getBlockZ() - topLeftZ ) >> scaleMod;

		// Minimum value needs to be rounded to multiple of step
		for ( int x = ( (int) ( playerMapX - VIEW_DISTANCE * step ) / step ) * step; x <= playerMapX + VIEW_DISTANCE * step; x += step )
			for ( int z = ( (int) ( playerMapZ - VIEW_DISTANCE * step ) / step ) * step; z <= playerMapZ + VIEW_DISTANCE * step; z += step )
			{
				// Convert current pixel to world chunk coordinates
				byte cachedColour = getColour( ( topLeftX + ( x << scaleMod ) ) >> 4, ( topLeftZ + ( z << scaleMod ) ) >> 4, playerWorld );
				for ( int xi = x; xi < x + step; xi++ )
					for ( int zi = z; zi < z + step; zi++ )
					{
						if ( ( xi - playerMapX ) * ( xi - playerMapX ) + ( zi - playerMapZ ) * ( zi - playerMapZ ) <= VIEW_DISTANCE
								* VIEW_DISTANCE * step * step )
							fMap.setPixel( xi, zi, cachedColour );
					}
			}

		for ( int x = 0; x < 128; x++ )
			for ( int y = 0; y < 128; y++ )
				canvas.setPixel( x, y, fMap.getPixel( x, y ) );
	}




	private byte getColour( int chunkX, int chunkZ, String world ) {
		Faction faction = BoardColls.get().getFactionAt( PS.valueOf( world, chunkX, chunkZ ) );

		if ( faction == null )
			return 4;

		if ( faction.isNone() )
			return 4;
		else
		{
			return 58;
		}

	}
}
