package eu.blackspectrum.factionmap.entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.WorldMap;

import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import eu.blackspectrum.factionmap.FMapRenderer;
import eu.blackspectrum.factionmap.FactionMap;

public class FMap
{


	private Set<UUID>		factionModePlayers	= new HashSet<UUID>();
	private byte[]			pixels;
	private final short		id;
	private final WorldMap	map;
	private long			lastUsed;




	public FMap(short id, MapView mapView) {
		this.id = id;
		pixels = null;

		lastUsed = 0;

		map = (WorldMap) MinecraftServer.getServer().worlds.get( 0 ).worldMaps.get( WorldMap.class, "map_" + id );

		for ( MapRenderer render : mapView.getRenderers() )
			mapView.removeRenderer( render );

		mapView.addRenderer( new FMapRenderer( this ) );
	}




	public void setLastUsed( long lastUsed ) {
		this.lastUsed = lastUsed;
	}




	public boolean shouldDeinitialize() {
		return System.currentTimeMillis() - lastUsed - 300 >= 0;
	}




	public void deinitialize() {
		if ( pixels != null )
		{
			write( id, pixels );
			pixels = null;
		}
	}




	public void initialize() {
		if ( pixels == null )
			pixels = read( id );
	}




	public void togglePlayer( UUID uid ) {

		if ( factionModePlayers.contains( uid ) )
		{
			factionModePlayers.remove( uid );
		}
		else
		{
			factionModePlayers.add( uid );
		}
	}




	public void removePlayer( UUID uid ) {
		factionModePlayers.remove( uid );
	}




	public Collection<UUID> getPlayers() {
		return factionModePlayers;
	}




	public void setPixel( int x, int y, byte value ) {
		if ( x >= 0 && x < 128 && y >= 0 && y < 128 )
		{
			pixels[x + 128 * y] = value;
		}
	}




	public byte getPixel( int x, int y, Player player ) {
		if ( x >= 0 && x < 128 && y >= 0 && y < 128 )
			if ( isFactionModeForPlayer( player ) )
				return pixels[x + 128 * y];
			else
			{
				return map.colors[x + 128 * y];
			}
		else
		{
			return 0;
		}
	}




	public boolean isFactionModeForPlayer( Player player ) {
		return factionModePlayers.contains( player.getUniqueId() );
	}




	public short getId() {
		return id;
	}




	public static byte[] read( short id ) {
		byte[] data = new byte[128 * 128];

		File file = new File( "plugins" + File.separator + FactionMap.pluginName + File.separator + "maps" + File.separator + "map_" + id
				+ ".dat" );

		if ( !file.exists() )
			return data;

		try
		{
			FileInputStream fis = new FileInputStream( file );
			GZIPInputStream gis = new GZIPInputStream( fis );

			byte[] buffer = new byte[1024];
			int len;
			int pos = 0;
			while ( ( len = gis.read( buffer ) ) != -1 )
			{
				System.arraycopy( buffer, 0, data, pos, len );
				pos += len;
			}
			// close resources

			gis.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		return data;
	}




	public static void write( short id, byte[] data ) {
		File file = new File( "plugins" + File.separator + FactionMap.pluginName + File.separator + "maps" + File.separator + "map_" + id
				+ ".dat" );

		try
		{
			if ( !file.exists() )
				file.createNewFile();

			FileOutputStream fos = new FileOutputStream( file );
			GZIPOutputStream gzipOS = new GZIPOutputStream( fos );

			gzipOS.write( data );

			// close resources
			gzipOS.close();
			fos.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}
}
