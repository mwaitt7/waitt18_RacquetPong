package cs301.rp2k16;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cs301.pong.R;

/**
 * PongMainActivity
 * <p/>
 * This is the activity for RacquetPong. It attaches a PongAnimator to
 * an AnimationSurface.
 * <p/>
 * IMPLEMENTATIONS OF CHOICE(part A):
 * Restart button spawns a new ball.
 * Buttons for modifying paddle size.
 * <p/>
 * IMPLEMENTATIONS OF CHOICE(part B):
 * Added sound to walls, as well as "killstreaks", losing, and the spawning of a ball. (Approved by Nux, 5pts)
 * Added randomness to bouncing off of the walls.
 * Added a scorekeeper.
 * Added a liveskeeper.
 * <p/>
 * NOTE:
 * AnimationSurface was modified a teeny bit. See AnimationSurface class heading for
 * more details.
 * <p/>
 * Special Instructions:
 * Ball will automatically spawn upon starting the app initially. If ball goes off-screen,
 * press the restart button. Other than that, its very self-explanatory. The game is over when the player
 * loses all lives, the screen flashes, and the restart button is disabled. I could not figure out
 * how to gracefully end.
 *
 * @author Andrew Nuxoll
 * @author Steven R. Vegdahl
 * @author Michael Waitt
 * @version February 2016
 */
public class PongMainActivity extends Activity implements View.OnClickListener {

    protected static Button restartButton, easyButton, mediumButton, expertButton; //Buttons on the top
    protected static MediaPlayer ThemePlayer, wallEffectPlayer, scoreEffectPlayer, gameStartPlayer,
            doublePlayer, triplePlayer, overPlayer, toastyPlayer, losePlayer, tacularPlayer, trosityPlayer,
            killionairePlayer, killpocalypsePlayer, killtastrophePlayer, killamenjaroPlayer; //Players for sounds
    protected static TextView scoreTextView; //Current score
    protected static ImageView ball1, ball2, ball3, ball4, ball5; //Displays the amount of lives
    private static boolean hasPlayed = false; //A boolean variable used to stop the playing of a sound.

    /**
     * creates an AnimationSurface containing an AnimationTest.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);// Does stuff
        setContentView(R.layout.activity_pong_main);// Does stuff


        //------------*Initialize buttons*------------//
        restartButton = (Button) findViewById(R.id.RestartButton);
        easyButton = (Button) findViewById(R.id.EasyButton);
        mediumButton = (Button) findViewById(R.id.MediumButton);
        expertButton = (Button) findViewById(R.id.expertButton);
        restartButton.setOnClickListener(this);
        easyButton.setOnClickListener(this);
        mediumButton.setOnClickListener(this);
        expertButton.setOnClickListener(this);

        //------------*Initialize TextView*------------//
        scoreTextView = (TextView) findViewById(R.id.ScoreAmountTextView);

        //------------*Initialize ImageViews*------------//
        ball1 = (ImageView) findViewById(R.id.ball1);
        ball2 = (ImageView) findViewById(R.id.ball2);
        ball3 = (ImageView) findViewById(R.id.ball3);
        ball4 = (ImageView) findViewById(R.id.ball4);
        ball5 = (ImageView) findViewById(R.id.ball5);

        //------------*Initialize MediaPlayers*------------//
        ThemePlayer = MediaPlayer.create(this, R.raw.theme);
        wallEffectPlayer = MediaPlayer.create(this, R.raw.hitmarker);
        scoreEffectPlayer = MediaPlayer.create(this, R.raw.toasty);
        gameStartPlayer = MediaPlayer.create(this, R.raw.whoosh);
        doublePlayer = MediaPlayer.create(this, R.raw.doublekill);
        triplePlayer = MediaPlayer.create(this, R.raw.triple);
        toastyPlayer = MediaPlayer.create(this, R.raw.toasty);
        losePlayer = MediaPlayer.create(this, R.raw.lose);
        tacularPlayer = MediaPlayer.create(this, R.raw.tacular);
        trosityPlayer = MediaPlayer.create(this, R.raw.trosity);
        overPlayer = MediaPlayer.create(this, R.raw.over);
        killtastrophePlayer = MediaPlayer.create(this, R.raw.killtastrophe);
        killpocalypsePlayer = MediaPlayer.create(this, R.raw.killpocalypse);
        killionairePlayer = MediaPlayer.create(this, R.raw.killionaire);
        killamenjaroPlayer = MediaPlayer.create(this, R.raw.killamenjaro);

        //Connect the animation surface with the animator
        final AnimationSurface mySurface = (AnimationSurface) this
                .findViewById(R.id.animationSurface);
        mySurface.setAnimator(new AnimationTest());
    }

    /**
     * Controls what happens when a button is pressed
     *
     * @param v a View object being clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == restartButton.getId()) {
            AnimationTest.count = 0; //Restarts the game
        } else if (v.getId() == easyButton.getId()) {
            AnimationTest.difficulty = 0; //Sissy paddle
        } else if (v.getId() == mediumButton.getId()) {
            AnimationTest.difficulty = 1; //Amateur paddle
        } else if (v.getId() == expertButton.getId()) {
            AnimationTest.difficulty = 2; //Paddle of the gods
        }
    }

    /**
     * Sets the running total, also handles the various "killstreak" sounds
     * <p/>
     * CAVEAT: I am aware that 14 different MediaPlayers is probably not an ideal way to go about
     * this. However, I figured it to be less clunky than dealing with one MediaPlayer with 14 sound files.
     *
     * @param runningTotal the running total of points score in the game
     */
    public static void setRunningTotal(int runningTotal) {
        scoreTextView.setText(String.valueOf(runningTotal));
        if (runningTotal == 2) {
            if (!AnimationTest.alreadyExecuted) {
                //If doublekill, and the player has not been alerted of this, let them know.
                doublePlayer.start();
                AnimationTest.alreadyExecuted = true;
            }
        } else if (runningTotal == 3) {
            if (AnimationTest.alreadyExecuted) {
                //If triplekill, and the player has not been alerted of this, let them know.
                triplePlayer.start();
                AnimationTest.alreadyExecuted = !AnimationTest.alreadyExecuted;
            }
        } else if (runningTotal == 4) {
            if (!AnimationTest.alreadyExecuted) {
                //If overkill, and the player has not been alerted of this, let them know.
                overPlayer.start();
                AnimationTest.alreadyExecuted = !AnimationTest.alreadyExecuted;
            }
        } else if (runningTotal == 5) {
            if (AnimationTest.alreadyExecuted) {
                //If killtacular, and the player has not been alerted of this, let them know.
                tacularPlayer.start();
                AnimationTest.alreadyExecuted = !AnimationTest.alreadyExecuted;
            }
        } else if (runningTotal == 6) {
            if (!AnimationTest.alreadyExecuted) {
                //If killtrosity, and the player has not been alerted of this, let them know.
                trosityPlayer.start();
                AnimationTest.alreadyExecuted = !AnimationTest.alreadyExecuted;
            }
        } else if (runningTotal == 7) {
            if (AnimationTest.alreadyExecuted) {
                //If killamenjaro, and the player has not been alerted of this, let them know.
                killamenjaroPlayer.start();
                AnimationTest.alreadyExecuted = !AnimationTest.alreadyExecuted;
            }
        } else if (runningTotal == 8) {
            if (!AnimationTest.alreadyExecuted) {
                //If killtastrophe, and the player has not been alerted of this, let them know.
                killtastrophePlayer.start();
                AnimationTest.alreadyExecuted = !AnimationTest.alreadyExecuted;
            }
        } else if (runningTotal == 9) {
            if (AnimationTest.alreadyExecuted) {
                //If killpocalypse, and the player has not been alerted of this, let them know.
                killpocalypsePlayer.start();
                AnimationTest.alreadyExecuted = !AnimationTest.alreadyExecuted;
            }
        } else if (runningTotal == 10) {
            if (!AnimationTest.alreadyExecuted) {
                //If killionaire, and the player has not been alerted of this, let them know.
                killionairePlayer.start();
                AnimationTest.alreadyExecuted = !AnimationTest.alreadyExecuted;
            }
        } else if (runningTotal == 20) {
            if (AnimationTest.alreadyExecuted) {
                //Toasty!!!
                toastyPlayer.start();
                AnimationTest.alreadyExecuted = !AnimationTest.alreadyExecuted;
            }
        }
    }

    /**
     * Sets the number of lives, also handles what happens when the player loses.
     *
     * @param numLives Number of lives that the player has.
     */
    public static void setNumLives(int numLives) {
        //Handles the visibility of the lives
        if (numLives == 4) {
            ball5.setVisibility(View.INVISIBLE);
        } else if (numLives == 3) {
            ball4.setVisibility(View.INVISIBLE);
        } else if (numLives == 2) {
            ball3.setVisibility(View.INVISIBLE);
        } else if (numLives == 1) {
            ball2.setVisibility(View.INVISIBLE);
        } else if (numLives == 0 && !hasPlayed) {
            //Handles what happens when player looses.
            ball1.setVisibility(View.INVISIBLE);
            ThemePlayer.stop();
            losePlayer.start();
            restartButton.setEnabled(false);
            hasPlayed = true;
            AnimationTest.flash(Color.WHITE, 100);
        }
    }


    /**
     * Handles what happens on pause.
     */
    public void onPause() {
        super.onPause();
        ThemePlayer.pause();
        losePlayer.pause();
    }

    /**
     * Handles what happens on resume.
     */
    public void onResume() {
        super.onResume();
        ThemePlayer.start();
    }
}
