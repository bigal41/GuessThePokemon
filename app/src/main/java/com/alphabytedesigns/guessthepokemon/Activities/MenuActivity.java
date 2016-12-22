package com.alphabytedesigns.guessthepokemon.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alphabytedesigns.guessthepokemon.R;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    /////////////////////////////////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////////////////////////////////

    public final static String                          EXTRA_GENERATIONS = "com.alphabytedesigns.guessthepokemon.GENERATIONS";

    /////////////////////////////////////////////////////////////////////////////
    // MEMBERS
    /////////////////////////////////////////////////////////////////////////////

    ArrayList< Integer >                                generationList;

    /////////////////////////////////////////////////////////////////////////////
    // ACCESSORS
    /////////////////////////////////////////////////////////////////////////////

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

        //Set the layout of the menu application.
        setContentView(R.layout.activity_menu);

        generationList = new ArrayList<Integer>( );
    }

    /**
     * Called when the user clicks the GenOne button
     */
    public void startGenOneGame(View view) {

        generationList.add(1);

        //Launch the Main Game Activity with GEN ONE paramter
        Intent intent = new Intent(this, MainGameActivity.class);
        intent.putExtra(EXTRA_GENERATIONS, generationList);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the GenOne button
     */
    public void startGenTwoGame(View view) {

        generationList.add(2);

        //Launch the Main Game Activity with GEN TWO paramter
        Intent intent = new Intent(this, MainGameActivity.class);
        intent.putExtra(EXTRA_GENERATIONS, generationList);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the GenOne button
     */
    public void startCustomGame(View view) {
        //Launch the Game Selection Activity.
        Intent intent = new Intent(this, CustomGameActivity.class);
        startActivity(intent);
    }


}
