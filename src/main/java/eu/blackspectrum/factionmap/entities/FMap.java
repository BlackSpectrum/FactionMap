package eu.blackspectrum.factionmap.entities;

import java.io.Serializable;


public class FMap implements Serializable
{


	/**
	 * Generated at 03.08.2014
	 */
	private static final long	serialVersionUID	= 5746059683591805482L;
	private byte[]	pixels;



	public FMap(short id) {
		pixels = new byte[128 * 128];
	}




	public void setPixel( int x, int y, byte value ) {
		if ( x >= 0 && x < 128 && y >= 0 && y < 128 )
		{
			pixels[x + 128 * y] = value;
		}
	}




	public byte getPixel( int x, int y ) {
		if ( x >= 0 && x < 128 && y >= 0 && y < 128 )
			return pixels[x + 128 * y];
		else
		{
			return 0;
		}
	}
}
