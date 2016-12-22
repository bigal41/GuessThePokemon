package com.alphabytedesigns.guessthepokemon.Activities;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphabytedesigns.guessthepokemon.Database.DatabaseHelper;
import com.alphabytedesigns.guessthepokemon.Game.MainGame;
import com.alphabytedesigns.guessthepokemon.R;

import java.io.IOException;
import java.util.ArrayList;

public class MainGameActivity extends AppCompatActivity {

    /////////////////////////////////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////////////////////////////////

    private static final int[] BUTTONS = {
            R.id.btnLetter1,
            R.id.btnLetter2,
            R.id.btnLetter3,
            R.id.btnLetter4,
            R.id.btnLetter5,
            R.id.btnLetter6,
            R.id.btnLetter7,
            R.id.btnLetter8,
            R.id.btnLetter9,
            R.id.btnLetter10,
            R.id.btnLetter11,
            R.id.btnLetter12,
    };

    private static final int[] ANSWER_BUTTONS = {
            R.id.btnAnswer1,
            R.id.btnAnswer2,
            R.id.btnAnswer3,
            R.id.btnAnswer4,
            R.id.btnAnswer5,
            R.id.btnAnswer6,
            R.id.btnAnswer7,
            R.id.btnAnswer8,
            R.id.btnAnswer9,
            R.id.btnAnswer10,
    };

    /////////////////////////////////////////////////////////////////////////////
    // MEMBERS
    /////////////////////////////////////////////////////////////////////////////

    private static DatabaseHelper           dbHelper;

    private MainGame                        mGame;

    private  static ArrayList< Integer >    mGeneration;

    private static int                      mPlayablePokemon;

    /////////////////////////////////////////////////////////////////////////////
    // ACCESSORS
    /////////////////////////////////////////////////////////////////////////////

    public static int[]                 getButtons( )           { return BUTTONS; }

    public static int[]                 getAnswerButtons( )     { return ANSWER_BUTTONS; }

    public static ArrayList<Integer>    getGeneration( )        { return mGeneration; }

    /////////////////////////////////////////////////////////////////////////////
    // MUTATORS
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // METHODS
    /////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_game);

        Intent intent = getIntent();
        mGeneration = intent.getIntegerArrayListExtra( CustomGameActivity.EXTRA_GENERATIONS );
        mPlayablePokemon = intent.getIntExtra( CustomGameActivity.EXTRA_PLAYABLE_POKEMON, 150 );

        if( savedInstanceState == null )
        {
             dbHelper = new DatabaseHelper( this );

            try {
                dbHelper.createDatabase();
                dbHelper.openDatabase();
            }
            catch ( IOException ex )
            {
                throw new Error("Unable to create database");
            }
            catch ( SQLException e )
            {
                e.printStackTrace();
            }

            mGame = new MainGame( dbHelper, this, mPlayablePokemon );
            mGame.setUpGame( mGeneration );
        }
    }

    @Override
    protected void onStop( )
    {
        super.onStop( );
        dbHelper.close();
    }
}
