package eu.blackspectrum.factionmap;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.map.CraftMapRenderer;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

import eu.blackspectrum.factionmap.listeners.PlayerListener;

public class FactionMap extends JavaPlugin
{


	public static String	pluginName	= null;




	@Override
	public void onDisable() {
		// First collect garbage, then dump all remaining maps
		FMaps.Instance().collectGarbage();
		FMaps.Instance().dump();
	}




	@Override
	@SuppressWarnings("deprecation")
	public void onEnable() {

		if ( pluginName == null )
			pluginName = this.getName();

		this.getServer().getPluginManager().registerEvents( new PlayerListener(), this );

		new File( "plugins" + File.separator + pluginName + File.separator + "maps" ).mkdirs();

		// Schedule garbage collection
		// *********************************************************
		this.getServer().getScheduler().scheduleAsyncRepeatingTask( this, new Runnable() {


			@Override
			public void run() {
				FMaps.Instance().collectGarbage();

			}
		}, 6000, 6000 );
		// *********************************************************

		// Initialize all maps with FMaps
		// *********************************************************
		short lastedMap = -1;

		try
		{
			lastedMap = this.readLatestId();

		}
		catch ( final IOException e )
		{
			e.printStackTrace();
		}

		if ( lastedMap < 0 )
			return;

		for ( short id = 0; id <= lastedMap; id++ )
		{
			final MapView map = Bukkit.getMap( id );
			if ( map != null )
			{
				// Throw away all default map renderer
				for ( final MapRenderer rend : map.getRenderers() )
					if ( rend instanceof CraftMapRenderer )
						map.removeRenderer( rend );

				// If no renderer left add FMapRenderer
				if ( map.getRenderers().size() == 0 )
					map.addRenderer( new FMapRenderer() );

			}
		}
		// *********************************************************

	}




	// Read latest mapId from idcounts.dat
	private short readLatestId() throws IOException {
		final File f = new File( "world" + File.separator + "data" + File.separator + "idcounts.dat" );

		if ( !f.exists() )
			return -1;

		final DataInputStream dis = new DataInputStream( new FileInputStream( f ) );
		try
		{
			// Read first tag
			dis.readByte();
			// Read first tag-name
			dis.readUTF();

			// Read second tag
			dis.readByte();
			// Read second tag name
			dis.readUTF();

			// Read short
			return dis.readShort();
		}
		catch ( final IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			dis.close();

		}

		return -1;
	}

}
