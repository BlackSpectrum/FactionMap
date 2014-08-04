package eu.blackspectrum.factionmap;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.map.MapView;

public class Board
{


	private HashMap<Short, Short>	fMaps		= new HashMap<Short, Short>();

	private static Board	instance	= null;




	public static Board Instance() {
		if ( instance == null )
			instance = new Board();

		return instance;
	}




	@SuppressWarnings("deprecation")
	// Deprecation since Bukkit doesnt offer other methods..
	public MapView getFMap( short map ) {

		if ( !fMaps.containsKey( map ) )
			createFMap( map );

		return Bukkit.getMap( fMaps.get( map ) ) ;
	}




	@SuppressWarnings("deprecation")
	// Deprecation since Bukkit doesnt offer other methods...
	public void createFMap( short map ) {
		if ( !fMaps.containsKey( map ) )
		{
			MapView oldMap = Bukkit.getMap( map );
			MapView fMap = Bukkit.createMap( oldMap.getWorld() );
			fMap.setCenterX( oldMap.getCenterX() );
			fMap.setCenterZ( oldMap.getCenterZ() );
			
			fMaps.put( map,  fMap.getId() );
	}}
		




	public boolean isFMap( short map ) {
		return fMaps.containsValue( map );
	}




	private Board() {

	}

}
