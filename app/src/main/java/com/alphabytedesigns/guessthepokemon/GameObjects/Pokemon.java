package com.alphabytedesigns.guessthepokemon.GameObjects;

/**
 * Created by aclark on 12/15/2016.
 */

public class Pokemon {

    /////////////////////////////////////////////////////////////////////////////
    // MEMBERS
    /////////////////////////////////////////////////////////////////////////////

    private int                             id;

    private String                          darkUrl;

    private String                          name;

    private String                          normalUrl;

    /////////////////////////////////////////////////////////////////////////////
    // ACCESSORS
    /////////////////////////////////////////////////////////////////////////////

    public int 		                        getId( ) 	{ return this.id; }

    public String                           getDarkUrl( ) { return this.darkUrl; }

    public String 	                        getName( ) 	{ return this.name; }

    public String 	                        getNormalURL( )	{ return this.normalUrl; }

    /////////////////////////////////////////////////////////////////////////////
    // MUTATORS
    /////////////////////////////////////////////////////////////////////////////

    public void 	                        setId( int value ) 		{ this.id = value; }

    public void                             setDarkUrl( String value ) { this.darkUrl = value;}

    public void 	                        setName( String value ) { this.name = value; }

    public void		                        setNormalUrl(String value )	{ this.normalUrl = value; }

    /////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    /////////////////////////////////////////////////////////////////////////////

    public Pokemon( int id, String name, String normalUrl, String darkUrl )
    {
        this.id 	    = id;
        this.darkUrl    = darkUrl;
        this.name 	    = name;
        this.normalUrl 	= normalUrl;
    }

    /////////////////////////////////////////////////////////////////////////////
    // METHODS
    /////////////////////////////////////////////////////////////////////////////
}
