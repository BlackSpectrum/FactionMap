package eu.blackspectrum.factionmap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import eu.blackspectrum.factionmap.entities.FMap;

public class FMaps
{


	private final HashMap<Short, FMap>	fMaps					= new HashMap<Short, FMap>();

	private final long					GARBAGE_COLLECT_TIME	= 300;

	private static FMaps				instance;




	// Singleton
	public static FMaps Instance() {
		if ( instance == null )
			instance = new FMaps();

		return instance;
	}




	private FMaps() {

	}




	public void addFMap( final short id ) {
		this.fMaps.put( id, new FMap( id ) );
	}




	// Dump and remove those that didnt get used for some time
	public void collectGarbage() {
		final long now = System.currentTimeMillis();
		final Iterator<FMap> it = this.fMaps.values().iterator();

		while ( it.hasNext() )
		{
			final FMap map = it.next();

			if ( now - map.getLastUsed() - this.GARBAGE_COLLECT_TIME * 1000 >= 0 )
			{
				map.dump();
				it.remove();
			}
		}
	}




	// Dump everything, but do not remove
	public void dump() {
		for ( final FMap fMap : this.fMaps.values() )
			fMap.dump();
	}




	public FMap getFMap( final short id ) {
		if ( !this.fMaps.containsKey( id ) )
			this.fMaps.put( id, new FMap( id ) );

		return this.fMaps.get( id );
	}




	public Collection<FMap> getFMaps() {
		return this.fMaps.values();
	}

}
