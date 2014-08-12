package eu.blackspectrum.factionmap.entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.WorldMap;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;

import eu.blackspectrum.factionmap.FactionMap;

public class FMap
{


	private final Set<UUID>	factionModePlayers;
	private final byte[]	pixels;
	private final short		id;
	private final WorldMap	map;
	private long			lastUsed;




	// read compressed pixels from disc
	public static byte[] readData( final short id ) {
		final byte[] data = new byte[128 * 128];

		final File file = new File( "plugins" + File.separator + FactionMap.pluginName + File.separator + "maps" + File.separator + "map_"
				+ id + ".dat" );

		if ( !file.exists() )
			return data;

		try
		{
			final FileInputStream fis = new FileInputStream( file );
			final GZIPInputStream gis = new GZIPInputStream( fis );

			final byte[] buffer = new byte[4096];
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
		catch ( final IOException e )
		{
			e.printStackTrace();
		}
		return data;
	}




	public static Set<UUID> readPlayers( final short id ) {
		final Set<UUID> players = new HashSet<UUID>();

		final File file = new File( "plugins" + File.separator + FactionMap.pluginName + File.separator + "maps" + File.separator + "map_"
				+ id + "_players.dat" );

		if ( !file.exists() )
			return players;

		try
		{
			final FileInputStream fis = new FileInputStream( file );
			final ObjectInputStream ois = new ObjectInputStream( fis );

			final int size = ois.readInt();

			for ( int i = 0; i < size; i++ )
				players.add( UUID.fromString( ois.readUTF() ) );

			// close resources
			ois.close();
		}
		catch ( final IOException e )
		{
			e.printStackTrace();
		}
		return players;
	}




	// write pixels to disc with gzip
	public static void writeData( final short id, final byte[] data ) {
		final File file = new File( "plugins" + File.separator + FactionMap.pluginName + File.separator + "maps" + File.separator + "map_"
				+ id + ".dat" );

		try
		{
			if ( !file.exists() )
				file.createNewFile();

			final FileOutputStream fos = new FileOutputStream( file );
			final GZIPOutputStream gzipOS = new GZIPOutputStream( fos );

			final byte[] buffer = new byte[4096];

			int pos = 0;
			while ( pos < 128 * 128 )
			{
				System.arraycopy( data, pos, buffer, 0, buffer.length );
				gzipOS.write( buffer );
				pos += buffer.length;
			}

			// close resources
			gzipOS.flush();
			gzipOS.close();
			fos.close();
		}
		catch ( final IOException e )
		{
			e.printStackTrace();
		}
	}




	public static void writePlayers( final short id, final Set<UUID> players ) {
		final File file = new File( "plugins" + File.separator + FactionMap.pluginName + File.separator + "maps" + File.separator + "map_"
				+ id + "_players.dat" );

		try
		{
			if ( !file.exists() )
				file.createNewFile();

			final FileOutputStream fos = new FileOutputStream( file );
			final ObjectOutputStream oos = new ObjectOutputStream( fos );

			oos.writeInt( players.size() );

			for ( final UUID uuid : players )
				oos.writeUTF( uuid.toString() );

			// close resources
			oos.flush();
			oos.close();
			fos.close();
		}
		catch ( final IOException e )
		{
			e.printStackTrace();
		}
	}




	public FMap(final short id) {
		this.id = id;
		this.pixels = readData( id );

		this.factionModePlayers = readPlayers( id );

		this.lastUsed = 0;

		// get the normal map
		this.map = (WorldMap) MinecraftServer.getServer().worlds.get( 0 ).worldMaps.get( WorldMap.class, "map_" + id );

	}




	// dump to disc
	public void dump() {
		writeData( this.id, this.pixels );
		writePlayers( this.id, this.factionModePlayers );
	}




	public short getId() {
		return this.id;
	}




	public long getLastUsed() {
		return this.lastUsed;
	}




	public Collection<UUID> getPlayers() {
		return this.factionModePlayers;
	}




	public boolean isFactionModeForPlayer( final Player player ) {
		return this.factionModePlayers.contains( player.getUniqueId() );
	}




	public void removePlayer( final UUID uid ) {
		this.factionModePlayers.remove( uid );
	}




	public void renderFactions( final MapCanvas canvas ) {
		for ( int x = 0; x < 128; x++ )
			for ( int z = 0; z < 128; z++ )
				canvas.setPixel( x, z, this.pixels[x + 128 * z] );
	}




	public void renderTerrain( final MapCanvas canvas ) {
		for ( int x = 0; x < 128; x++ )
			for ( int z = 0; z < 128; z++ )
				canvas.setPixel( x, z, this.map.colors[x + 128 * z] );
	}




	public void setLastUsed( final long lastUsed ) {
		this.lastUsed = lastUsed;
	}




	public void setPixel( final int x, final int y, final byte value ) {
		if ( x >= 0 && x < 128 && y >= 0 && y < 128 )
			this.pixels[x + 128 * y] = value;
	}




	public void togglePlayer( final UUID uid ) {

		if ( this.factionModePlayers.contains( uid ) )
			this.factionModePlayers.remove( uid );
		else
			this.factionModePlayers.add( uid );
	}
}
