package com.alphabytedesigns.guessthepokemon.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alphabytedesigns.guessthepokemon.GameObjects.Pokemon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * Created by Home on 12/15/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    /////////////////////////////////////////////////////////////////////////////
    // MEMBERS
    /////////////////////////////////////////////////////////////////////////////

    //Constants
    private static final String                 COLUMN_ID 			    = "id";
    private static final String                 LETTER_COLUMN_ID        = "id";
    private static final String                 COLUMN_NORMAL_LOCATION 	= "normal_url";
    private static final String                 COLUMN_DARK_LOCATION    = "dark_url";
    private static final String                 COLUMN_TITLE 		    = "name";
    private static final String                 DB_NAME                 = "guess_the_pokemon";
    private static final int                    SCHEMA_VERSION          = 1;
    private static final String                 POKEMON_TABLE_NAME      = "pokemon";
    private static final String                 LETTER_TABLE_NAME       = "letters";

    //Non Constants
    private final Context                       mContext;
    private SQLiteDatabase                      mDatabase;
    private final String                        dbPath;

    /////////////////////////////////////////////////////////////////////////////
    // ACCESSORS
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // MUTATORS
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    /////////////////////////////////////////////////////////////////////////////

    public DatabaseHelper( Context context )
    {
        super(context, DB_NAME, null, SCHEMA_VERSION);

        this.mContext = context;

        this.dbPath = mContext.getDatabasePath( DB_NAME ).getPath();
    }

    /////////////////////////////////////////////////////////////////////////////
    // METHODS
    /////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @throws IOException
     */
    public void createDatabase( )
        throws IOException
    {
        //Check if the database exists
        boolean dbExist = checkDatabase( );
        
        if( dbExist )
        {
            SQLiteDatabase.deleteDatabase( new File( dbPath ) );
            copyDatabase( );
        }
        else
        {
            this.getReadableDatabase();

            try
            {
                copyDatabase( );
            }
            catch ( IOException ex )
            {
                throw new IOException( "Error copying database", ex );
            }
        }

    }// createDatabase


    /**
     *
     * @return if the the database actually exists
     */
    private boolean checkDatabase( )
    {
        SQLiteDatabase checkDB = null;

        try
        {
            checkDB = SQLiteDatabase.openDatabase( dbPath, null, SQLiteDatabase.OPEN_READONLY );
        }
        catch ( SQLiteException ex )
        {
            Log.d("MainGame", "Database does not exists yet.");
        }

        if( checkDB != null )
        {
            checkDB.close( );
        }

        return checkDB != null;

    }// checkDatabase


    /**
     *
     * @throws IOException
     */
    private void copyDatabase( )
        throws IOException
    {
        InputStream mInput = mContext.getAssets().open( DB_NAME );

        String outFileName = dbPath;

        OutputStream mOutput = new FileOutputStream( outFileName );

        byte[ ] buffer = new byte[ 1024 ];
        int length;

        while( ( length = mInput.read( buffer ) ) > 0 )
        {
            mOutput.write( buffer, 0, length );
        }

        mOutput.flush( );
        mOutput.close( );
        mInput.close( );
    }// copyDatabase


    /**
     *
     * @throws SQLException
     */
    public void openDatabase( )
        throws SQLException
    {
        mDatabase = SQLiteDatabase.openDatabase( dbPath, null, SQLiteDatabase.OPEN_READONLY );
    }// openDatabase;


    /**
     *
     */
    @Override
    public synchronized void close( )
    {
        if( mDatabase != null )
        {
            mDatabase.close( );
        }

        super.close( );
    }// close;


    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    /**
     *
     * @param id
     * @return
     */
    public Pokemon getPokemonById( int id )
    {
        String query = "SELECT * FROM " + POKEMON_TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;

        Cursor c = mDatabase.rawQuery( query, null );
        c.moveToFirst( );
        Pokemon pokemon = new Pokemon( c.getInt( c.getColumnIndex( COLUMN_ID ) ), c.getString( c.getColumnIndex( COLUMN_TITLE ) ), c.getString( c.getColumnIndex( COLUMN_NORMAL_LOCATION ) ), c.getString( c.getColumnIndex( COLUMN_DARK_LOCATION ) ) );
        c.close( );

        return pokemon;
    }// getPokemonById


    /**
     *
     * @return
     */
    public Hashtable< Integer, String > getLetters( )
    {
        Hashtable< Integer, String > letters = new Hashtable<Integer, String>( );
        String query = "SELECT * FROM " + LETTER_TABLE_NAME;

        Cursor c = mDatabase.rawQuery( query, null );
        c.moveToFirst( );
        while( ! c.isLast() )
        {
            letters.put(c.getInt( c.getColumnIndex( LETTER_COLUMN_ID ) ), c.getString( c.getColumnIndex( COLUMN_TITLE ) ) );
            c.moveToNext();
        }
        c.close();

        letters.put( 26, "Z");

        return letters;

    }// getLetters
}
