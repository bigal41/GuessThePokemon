package com.alphabytedesigns.guessthepokemon.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.alphabytedesigns.guessthepokemon.R;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import java.util.ArrayList;

public class CustomGameActivity extends AppCompatActivity {

    /////////////////////////////////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////////////////////////////////

    public final static String                  EXTRA_GENERATIONS           = "com.alphabytedesigns.guessthepokemon.GENERATIONS";

    public final static String                  EXTRA_PLAYABLE_POKEMON      = "com.alphabytedesigns.guessthepokemon.PLAYABLE_POKEMON";

    public final int                            TOTAL_GEN_ONE       = 151;

    public final int                            TOTAL_GEN_TWO       = 100;

    public final int                            TOTAL_GEN_THREE     = 135;

    public final int                            TOTAL_GEN_FOUR      = 107;

    public final int                            TOTAL_GEN_FIVE      = 156;

    public final int                            TOTAL_GEN_SIX       = 72;

    public final int                            TOTAL_GEN_SEVEN     = 81;

    /////////////////////////////////////////////////////////////////////////////
    // MEMBERS
    /////////////////////////////////////////////////////////////////////////////

    ArrayList< Integer >                        generationList;

    int                                         totalPokemon;

    int                                         playablePokemonAmount;

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

    public void onCheckboxClicked(View view) {

        //Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        switch( view.getId())
        {
            case R.id.chkGen1:
                if( checked )
                {
                    totalPokemon += TOTAL_GEN_ONE;
                    generationList.add(1);
                }
                else
                {
                    totalPokemon -= TOTAL_GEN_ONE;
                    generationList.remove( Integer.valueOf(1) );
                }
                break;

            case R.id.chkGen2:
                if( checked )
                {
                    totalPokemon += TOTAL_GEN_TWO;
                    generationList.add(2);
                }
                else
                {
                    totalPokemon -= TOTAL_GEN_TWO;
                    generationList.remove(Integer.valueOf(2));
                }
                break;

            case R.id.chkGen3:
                if( checked )
                {
                    totalPokemon += TOTAL_GEN_THREE;
                    generationList.add(3);
                }
                else
                {
                    totalPokemon -= TOTAL_GEN_THREE;
                    generationList.remove(Integer.valueOf(3));
                }
                break;

            case R.id.chkGen4:
                if( checked )
                {
                    totalPokemon += TOTAL_GEN_FOUR;
                    generationList.add(4);
                }
                else
                {
                    totalPokemon -= TOTAL_GEN_FOUR;
                    generationList.remove(Integer.valueOf(4));
                }
                break;

            case R.id.chkGen5:
                if( checked )
                {
                    totalPokemon += TOTAL_GEN_FIVE;
                    generationList.add(5);
                }
                else
                {
                    totalPokemon -= TOTAL_GEN_FIVE;
                    generationList.remove(Integer.valueOf(5));
                }
                break;

            case R.id.chkGen6:
                if( checked )
                {
                    totalPokemon += TOTAL_GEN_SIX;
                    generationList.add(6);
                }
                else
                {
                    totalPokemon -= TOTAL_GEN_SIX;
                    generationList.remove(Integer.valueOf(6));
                }
                break;

            case R.id.chkGen7:
                if( checked )
                {
                    totalPokemon += TOTAL_GEN_SEVEN;
                    generationList.add(7);
                }
                else
                {
                    totalPokemon -= TOTAL_GEN_SEVEN;
                    generationList.remove(Integer.valueOf(7));
                }
                break;

            default:
                break;
        }

        // get seekbar from view
        final CrystalSeekbar pkmnAmount = (CrystalSeekbar) this.findViewById(R.id.pkmnAmount);

        pkmnAmount.setMaxValue(totalPokemon);

        pkmnAmount.apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game);

        generationList = new ArrayList<Integer>( );

        // get seekbar from view
        final CrystalSeekbar pkmnAmount = (CrystalSeekbar) this.findViewById(R.id.pkmnAmount);
        pkmnAmount.setMaxValue( 0 );

        // get min and max text view
        final TextView tvMin = (TextView) this.findViewById(R.id.tvAmountPkmn);

        // set listener
        pkmnAmount.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                tvMin.setText(String.valueOf(minValue));
            }
        });

        pkmnAmount.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                playablePokemonAmount = value.intValue();
            }
        });

    }

    /**
     * Called when the user clicks the start game button
     */
    public void startGame(View view) {

        if( generationList.size() == 0 )
        {
            return;
        }

        if( playablePokemonAmount == 0 )
        {
            return;
        }

        Log.d("CustomGame", "Playable abount is " + playablePokemonAmount );
        Log.d("CustomGameActivity", "The generations added so far are  " + generationList.toString());

        //Launch the Main Game Activity with the list of generations and the amount playable pokemon.
        Intent intent = new Intent(this, MainGameActivity.class);
        intent.putExtra(EXTRA_GENERATIONS, generationList);
        intent.putExtra(EXTRA_PLAYABLE_POKEMON, playablePokemonAmount);
        startActivity(intent);
    }
}
