package com.alphabytedesigns.guessthepokemon.Game;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphabytedesigns.guessthepokemon.Activities.MainGameActivity;
import com.alphabytedesigns.guessthepokemon.Database.DatabaseHelper;
import com.alphabytedesigns.guessthepokemon.GameObjects.Pokemon;
import com.alphabytedesigns.guessthepokemon.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Home on 12/15/2016.
 */

public class MainGame {

    /////////////////////////////////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // MEMBERS
    /////////////////////////////////////////////////////////////////////////////

    private List<Button>                                    answerBtnList;

    /**
     * This is a map for the answer buttons. The <b>key</b> is the actual <i>Button</i> and the
     * <b>value</b> is either true or false depending if there is a letter on the button.
     */
    private Map<Button, Boolean>                            answerBtnMap;

    private int                                             correctPokemonNeeded;

    private List<Button>				                    letterBtnList;

    private Hashtable<Integer,String>                       letterHashTable;

    private MainGameActivity                                mActivity;

    private DatabaseHelper                                  mDbHelper;

    private int                                             numOfGuesses;

    private Pokemon                                         pokemon;

    private ArrayList<Integer>                              pokemonList;

    /////////////////////////////////////////////////////////////////////////////
    // ACCESSORS
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // MUTATORS
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    /////////////////////////////////////////////////////////////////////////////

    public MainGame( DatabaseHelper mDbHelper, MainGameActivity mActivity, int playablePokemon )
    {
        this.mDbHelper = mDbHelper;
        this.mActivity = mActivity;

        this.correctPokemonNeeded = playablePokemon;

        //TODO: Do we need this?
        this.letterHashTable = mDbHelper.getLetters( );

        this.createPokemonList( );
    }

    /////////////////////////////////////////////////////////////////////////////
    // METHODS
    /////////////////////////////////////////////////////////////////////////////


    public boolean checkWinner( )
    {
        StringBuilder   sb = new StringBuilder( );
        String          guessedName;

        final TextView tv = (TextView) mActivity.findViewById(R.id.lblNumGuesses);

        for( Button btn : this.answerBtnList )
        {
            sb.append( btn.getText());
        }

        guessedName = sb.toString().toLowerCase();

        numOfGuesses++;

        if( numOfGuesses == 1 )
            tv.setText( numOfGuesses + " Guess");
        else
            tv.setText( numOfGuesses + " Guesses");

        return ( guessedName.equals( this.pokemon.getName() ) );
    }// checkWinner

    /**
     *
     */
    private void createAnswerBtnListeners( )
    {
        for( Button btn : this.answerBtnList )
        {
            btn.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View view)
                {
                    Button btn = (Button) view;
                    for( Map.Entry<Button, Boolean> pairs : answerBtnMap.entrySet() )
                    {
                        if( pairs.getKey().getId() == btn.getId() )
                        {
                            pairs.setValue(false);
                            for( Button b : letterBtnList )
                            {
                                if( b.getText() ==  pairs.getKey().getText())
                                {
                                    b.setEnabled(true);
                                }
                            }

                            pairs.getKey().setText(" ");
                            break;
                        }
                    }
                }
            });
        }
    }// createAnswerBtnListeners

    /**
     *
     */
    private void createAnswerBtnMapAndList( )
    {
        this.answerBtnMap = new LinkedHashMap<Button, Boolean>( );
        this.answerBtnList = new LinkedList<Button>( );

        for( int id : MainGameActivity.getAnswerButtons() )
        {
            this.answerBtnMap.put( (Button) mActivity.findViewById(id), false);
            this.answerBtnList.add( (Button) mActivity.findViewById(id) );
        }
    }// createAnswerBtnMap


    /**
     *
     */
    private void createLetterBtnListeners( )
    {
        for( Button btn : this.letterBtnList )
        {
            btn.setOnClickListener( new View.OnClickListener() {

                @Override
                public void  onClick(View view)
                {
                    Button btn = (Button) view;
                    boolean hasBeenSet = false;

                    //We need to loop over the Answer Btn Map and find the next button that does not
                    // have a text set -- This is determined by the Boolean value.
                    Iterator< Map.Entry< Button, Boolean > > it = answerBtnMap.entrySet().iterator();

                    while (it.hasNext())
                    {
                        //Get the current pair.
                        Map.Entry<Button, Boolean> answerPair = it.next();

                        //If pair's value is set to false and the if the button is enabled
                        //We found the next available answer button.

                        // NOTE: The reason we check if the button is enabled is because we hide
                        // and disable buttons that are not needed
                        if( ! answerPair.getValue() && answerPair.getKey().isEnabled())
                        {
                            //Sanity Check
                            if( hasBeenSet ) return;

                            //Set the text value to the same as the letter button.
                            answerPair.getKey().setText( btn.getText() );

                            //We set the value to true so we know that this button text has been set
                            answerPair.setValue(true);

                            //Disable the letter button.
                            btn.setEnabled(false);
                            hasBeenSet = true;

                            //We want to break now since we have successfully set an answer button
                            break;

                        }
                    }

                    //Next we want to loop through all the answer buttons and check if the values
                    //have been set.

                    //Reset the iterator
                    it = answerBtnMap.entrySet().iterator();
                    Boolean completedGuess = false;

                    while (it.hasNext()) {
                        //Get the current pair.
                        Map.Entry<Button, Boolean> answerPair = it.next();

                        if (!answerPair.getValue() && answerPair.getKey().isEnabled())
                        {
                            completedGuess = false;
                            break;
                        }
                        else {
                            completedGuess = true;
                        }
                    }

                    if( completedGuess )
                    {
                        if( checkWinner() )
                        {
                            displaySuccessDialog();
                            return;
                        }
                    }
                }
            });
        }
    }// createLetterBtnListeners


    /**
     *
     */
    private void createLetterBtnList( )
    {
        this.letterBtnList = new ArrayList<Button>( );

        for( int id : MainGameActivity.getButtons() )
        {
            this.letterBtnList.add( (Button) mActivity.findViewById(id) );
        }
    }// createLetterBtnList


    /**
     *
     */
    private void createPokemonList( )
    {
        //Create an Array List that will store the list of pokemon ids.
        this.pokemonList = new ArrayList<Integer>( );

        Random      rand = new Random( );
        int         pid;
        //Add Numbers from 1 to 150 to the list
        //TODO: Add support for the other generations
        for( int i = 1; i <= correctPokemonNeeded; i++ )
        {

            do {
                pid = rand.nextInt(150 + 1);
            }while (this.pokemonList.contains(pid));

            this.pokemonList.add(pid);
        }
    }// createPokemonList


    /**
     *
     */
    private void displaySuccessDialog( )
    {

        final ImageView iv = (ImageView) mActivity.findViewById(R.id.ivPokemon);
        final Button nxtPkmn = (Button) mActivity.findViewById(R.id.btnNxtPkmon);
        final TextView tv = (TextView) mActivity.findViewById(R.id.SuccessDialog);
        final GridLayout gl = (GridLayout) mActivity.findViewById(R.id.grdAnswerKeys);

        //Grab the stored image of the pokemon
        int imageResource = mActivity.getResources().getIdentifier(pokemon.getNormalURL(), null, mActivity.getPackageName());

        //Display the image of the pokemon
        Drawable image = mActivity.getResources().getDrawable(imageResource);
        iv.setImageDrawable(image);

        //Hide the answer keys
        gl.setVisibility(View.INVISIBLE);

        //Hide the letter buttons
        for( Button b : letterBtnList )
        {
            b.setEnabled( false );
            b.setVisibility(View.INVISIBLE);
        }

        if( this.pokemonList.size() != 0 ) {

            tv.setVisibility(View.VISIBLE);
            nxtPkmn.setEnabled(true);
            nxtPkmn.setVisibility(View.VISIBLE);

            nxtPkmn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    nxtPkmn.setEnabled(false);
                    nxtPkmn.setVisibility(View.INVISIBLE);

                    tv.setVisibility(View.INVISIBLE);

                    gl.setVisibility(View.VISIBLE);

                    resetGameBoard( );

                }

            });
        }
        else
        {
            Log.d("MainGame", "Game is over. It took you " + numOfGuesses + " to clear " + correctPokemonNeeded + " Pokemon");
        }

    }// displaySuccessDialog


    /**
     *
     */
    private void randomNewPokemon( )
    {
        Log.d("MainGame", "Entering randomNewPokemon()...");

        //Variables
        ImageView iv = (ImageView) this.mActivity.findViewById(R.id.ivPokemon);
        TextView  tv = (TextView) this.mActivity.findViewById(R.id.lblLevel);
        Random    rand = new Random( );

        //Set the text for the amount of pokemon left to guess out of 150
        //TODO: Have this be a constant based on the generation.
        tv.setText( this.pokemonList.size( ) + "/" + correctPokemonNeeded);

        //Get a random pokemon id from the list of pokemon ids
        boolean validPID = false;

        //while( ! validPID ) {
        int pid = rand.nextInt( this.pokemonList.size() + 1 );


        //}

        //Get pokemon object from the database based on the random Pokemon ID
        this.pokemon = mDbHelper.getPokemonById( this.pokemonList.get(pid));

        //Remove the ID from the list so it cannot be used again.
        this.pokemonList.remove(pid);

        //Grab the stored image of the pokemon
        int imageResource = this.mActivity.getResources().getIdentifier(pokemon.getDarkUrl(), null, this.mActivity.getPackageName());

        //Display the image of the Pokemon
        Drawable image = this.mActivity.getResources().getDrawable(imageResource);
        iv.setImageDrawable(image);

        Log.d("MainGame", "randomNewPokemon complete");
    }// randomNewPokemon


    /**
     *
     */
    private void resetGameBoard( )
    {
       this.letterHashTable = mDbHelper.getLetters();

        for( Button btn : this.answerBtnList )
        {
            btn.setEnabled( true );
        }

        Log.d("MainGame", "Entering resetGameBoard...");

        this.randomNewPokemon();
        this.setLetters();

        for( Button btn : this.letterBtnList )
        {
            btn.setEnabled(true);
            btn.setVisibility(View.VISIBLE);
        }

        for(Map.Entry<Button, Boolean> pairs : answerBtnMap.entrySet() )
        {
            Log.d("MainGame", pairs.getKey().getText().toString() );

            pairs.getKey().setText("");
            pairs.setValue(false);
        }

        Log.d("MainGame", "resetGameBoard complete");

    }// resetGameBoard

    /**
     *
     */
    private void setLetters( )
    {
        Log.d("MainGame", "Entering setLetters()..." );

        ArrayList<Character> name = new ArrayList<Character>();
        Random rand = new Random();
        int r;
        Button b;
        boolean keyExists = false;

        //Convert Pokemon name to a character array
        char[] pChar = this.pokemon.getName().toUpperCase().toCharArray();

        //Add it to an array list
        for( char c : pChar )
            name.add(c);

        //Get the amount of extra letters that need to be generated.
        int numOfRandomLetters = this.letterBtnList.size() - name.size();
        int sizeOfPokemonName = name.size();

        //Scan through letter list and remove each letter from array;
        for( Character aName : name )
        {
            for( int key : this.letterHashTable.keySet() )
            {
                if( aName == this.letterHashTable.get(key).toCharArray()[0])
                {
                    this.letterHashTable.remove(key);
                    break;
                }
            }
        }

        for( int i = 0; i < numOfRandomLetters; i++ )
        {
            do
            {
                r = rand.nextInt( this.letterHashTable.size() + 1 - 1 ) + 1;
                if( this.letterHashTable.get(r) != null )
                {
                    keyExists = true;
                }
            }
            while ( ! keyExists );

            keyExists = false;

            name.add( this.letterHashTable.get(r).charAt(0));
            this.letterHashTable.remove(r);
        }

        for( Button aLetterBtnList : letterBtnList )
        {
            r = rand.nextInt(name.size());
            b = aLetterBtnList;
            b.setText(name.get(r).toString());
            name.remove(r);
        }

        for( int i = 0; i < this.answerBtnMap.size(); i++ )
        {
            if( i < sizeOfPokemonName )
            {
                b = this.answerBtnList.get(i);
                b.setEnabled(true);
                b.setVisibility(View.VISIBLE);
            }
            else
            {
                b = this.answerBtnList.get(i);
                b.setEnabled(false);
                b.setVisibility(View.INVISIBLE);
            }
        }

        Log.d("MainGame", "setLetters() complete");

    }// setLetters


    /**
     *
     */
    public void setUpGame( ArrayList<Integer> generations )
    {
        Log.d("MainGame", "Setting up game");

        final TextView tv = (TextView) mActivity.findViewById(R.id.lblNumGuesses);

        this.randomNewPokemon();
        this.createAnswerBtnMapAndList();
        this.createLetterBtnList();

        this.createLetterBtnListeners();
        this.createAnswerBtnListeners();
        this.setLetters();

        numOfGuesses = 0;

        tv.setText( numOfGuesses + " Guesses");
    }// setUpGame

}
