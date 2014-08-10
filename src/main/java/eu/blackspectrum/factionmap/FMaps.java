package eu.blackspectrum.factionmap;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.map.MapView;

import eu.blackspectrum.factionmap.entities.FMap;

public class FMaps
{


	private HashMap<Short, FMap>	fMaps	= new HashMap<Short, FMap>();

	private static FMaps			instance;




	private FMaps() {

	}




	public static FMaps Instance() {
		if ( instance == null )
			instance = new FMaps();

		
		
		return instance;
	}

	
	public void addFMap(short id, MapView map)
	{
		fMaps.put( id, new FMap( id, map ) );
	}


	@SuppressWarnings("deprecation")
	public FMap getFMap( short id ) {
		if ( !fMaps.containsKey( id ) )
			fMaps.put( id, new FMap( id, Bukkit.getMap( id ) ) );

		return fMaps.get( id );
	}
	
	public Collection<FMap> getFMaps()
	{
		return fMaps.values();
	}

}
