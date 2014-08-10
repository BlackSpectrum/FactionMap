package eu.blackspectrum.factionmap;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

import eu.blackspectrum.factionmap.entities.FMap;
import eu.blackspectrum.factionmap.listeners.PlayerListener;

public class FactionMap extends JavaPlugin
{


	public static String	pluginName	= null;




	@SuppressWarnings("deprecation")
	public void onEnable() {

		if ( pluginName == null )
			pluginName = getName();

		getServer().getPluginManager().registerEvents( new PlayerListener(), this );
		
		 new File( "plugins" + File.separator + pluginName + File.separator + "maps" ).mkdirs();

		// Initialize all maps with FMaps

		short lastedMap = -1;

		try
		{
			lastedMap = readLatestId();

		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}

		if ( lastedMap < 0 )
			return;

		for ( short id = 0; id <= lastedMap; id++ )
		{
			MapView map = Bukkit.getMap( id );
			if ( map != null )
				FMaps.Instance().addFMap( id, map );
		}
	}
	
	public void onDisable(){
		for(FMap map : FMaps.Instance().getFMaps())
		{
			map.deinitialize();
		}
	}




	private short readLatestId() throws IOException {
		File f = new File( "world" + File.separator + "data" + File.separator + "idcounts.dat" );

		DataInputStream dis = new DataInputStream( new FileInputStream( f ) );
		try
		{

			dis.readByte();
			dis.readUTF();

			dis.readByte();
			dis.readUTF();
			return dis.readShort();
		}
		catch ( IOException e )
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
