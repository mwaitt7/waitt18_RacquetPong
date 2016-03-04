package cs301.rp2k16;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Class that animates a ball moving on a background.
 *
 * @author Michael Waitt
 * @version February 2016
 */
public class AnimationTest extends PongMainActivity implements Animator {

    public static int count = 0;// boolean variable for when the game restarts
    public static int difficulty = 0; // variable used to set the wideness of the paddle
    public static int runningTotal = 0;
    private final int RADIUS = 20;// radius of the ball
    private int xPos, yPos;// tracks the x/y-coordinates of the ball
    private Paint paint = new Paint();// the paint used to paint several objects on the screen
    private int xVelocity = randomize(-30, 30);// a completely random xVelocity
    private int yVelocity = randomize(-30, 30);// a completely random yVelocity
    private int width, height, paddleCenter; //Tracks the width, height, and location of paddle.
    private int numLives = 5; //Number of lives that the player starts with
    private boolean ballOutOfPlay; //Determines if the ball is out of play
    public static boolean alreadyExecuted = false; //A boolean variable telling sounds when to play.
    public static Paint flashPaint; // has color for background flash
    public static int flashCount; // counts down ticks for background-flash

    /**
     * Randomly picks a number between min and max
     *
     * @param min Minimum value
     * @param max Maximum value
     * @return Random number between min and max
     */
    public static int randomize(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Interval between animation frames: .03 seconds (i.e., about 33 times
     * per second).
     *
     * @return the time interval between frames, in milliseconds.
     */
    @Override
    public int interval() {
        return 30;
    }

    /**
     * The background color: a nice shade of red.
     *
     * @return the background color onto which we will draw the image.
     */
    @Override
    public int backgroundColor() {
        // create/return the background color
        return Color.rgb(178, 34, 34);
    }

    /**
     * Spawns a ball in the default position
     *
     * @param g     Canvas to paint on
     * @param paint Paint used for painting
     */
    public void spawnBall(Canvas g, Paint paint) {
        // Draw the ball in the correct position.
        xPos = width / 2;
        yPos = height / 4;
        g.drawCircle(xPos, yPos, RADIUS, paint);
        gameStartPlayer.start();
    }

    public void changeYSpeed() {
        //Randomizes the speed in the y direction.
        yVelocity = randomize(5, 70);
    }

    public void changeXSpeed() {
        //Randomizes the speed in the x direction.
        xVelocity = randomize(5, 70);
    }

    /**
     * Draws the paddle
     *
     * @param paint Paint used for painting
     * @param g     Canvas used for canvassing
     */
    public void drawPaddle(Canvas g, Paint paint) {
        if (difficulty == 0) {
            g.drawRect(paddleCenter - 200, height - 85,
                    paddleCenter + 200, height - 25, paint);// Draws a sissy paddle
        } else if (difficulty == 1) {
            g.drawRect(paddleCenter - 100, height - 85,
                    paddleCenter + 100, height - 25, paint);// Draws an amateur paddle
        } else if (difficulty == 2) {
            g.drawRect(paddleCenter - 30, height - 85,
                    paddleCenter + 30, height - 25, paint);// Draws the paddle of the gods
        }
    }

    /**
     * Action to perform on clock tick
     * <p/>
     * CAVEAT: Ball glitches into the paddle upon hitting it from the side.
     * Attempted to fix it, but halted my efforts in order to maintain the prettiness
     * of the code. (It got really messy) However, it almost never occurs.
     *
     * @param g the graphics object on which to draw
     */
    @Override
    public void tick(Canvas g) {
        //------------*Set the color of paint*------------//
        paint.setColor(Color.rgb(255, 215, 0));

        //------------*Check if game is just starting up/restarting...*------------//
        if (count == 0) {
            //Game is just starting, initialize all variables and spawn ball.
            height = g.getHeight();
            width = g.getWidth();
            ballOutOfPlay = false;
            paddleCenter = width / 2;
            xVelocity = randomize(-70, 70);
            yVelocity = randomize(-70, 70);
            spawnBall(g, paint);
            ThemePlayer.start();
            ThemePlayer.setLooping(true);
            count++; //Increment count until next restart
        }

        //------------*Else, if-statement-ception*------------//
        else {
            drawPaddle(g, paint);
            xPos += xVelocity;// Move the ball in the x direction
            yPos += yVelocity;// Move the ball in the y direction

            if (yPos < 80) {
                //If it hits the top, send it back in a random direction and speed
                changeXSpeed();
                yVelocity = yVelocity * -1;
                wallEffectPlayer.start();
            } else if ((xPos < 80 && yPos < height - 95) || ((xPos > width - 80) && yPos < height - 95)) {
                //If it hits the left or right wall, send it back in a random direction and speed
                changeYSpeed();
                xVelocity = xVelocity * -1;
                wallEffectPlayer.start();
            } else if (yPos > height - 95 && yPos < height - 25) {
                if (difficulty == 0) {
                    if (xPos < paddleCenter + 200 && xPos > paddleCenter - 200) {
                        //If hits the sissy paddle, send it back
                        yVelocity = yVelocity * -1;
                        wallEffectPlayer.start();
                        runningTotal++;
                    }
                } else if (difficulty == 1) {
                    if (xPos < paddleCenter + 100 && xPos > paddleCenter - 100) {
                        //If it hits the amateur paddle, send it back
                        yVelocity = yVelocity * -1;
                        wallEffectPlayer.start();
                        runningTotal++;
                    }
                } else if (difficulty == 2) {
                    if (xPos < paddleCenter + 30 && xPos > paddleCenter - 30) {
                        //If it hits the paddle of the gods, send it back
                        yVelocity = yVelocity * -1;
                        wallEffectPlayer.start();
                        runningTotal++;
                    }
                }
            } else if (yPos > height && !ballOutOfPlay) {
                //If user misses, they lose a life, and points.
                ballOutOfPlay = true;
                runningTotal = runningTotal - 5;
                numLives--;

            }
            //------------*Draw the Ball and Paddle*------------//
            g.drawCircle(xPos, yPos, RADIUS, paint);
            drawPaddle(g, paint);


            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRunningTotal(runningTotal);
                    if (numLives < 5) {
                        setNumLives(numLives);
                    }
                }
            });
            /**
             * External Citation
             * Date: 1 March 2016
             * Problem: Could not edit UI from non-UI thread.
             * Resource:
             * Dr. Nuxoll
             * Solution: Call runOnUiThread() method.
             */

            if (flashCount > 0) {
                // we are flashing: draw the "flash" color
                g.drawRect(0, 0, width, height, flashPaint);

                // decrement the flash count by the number of milliseconds in
                // our interval
                flashCount -= interval();

                // if we've finished, "release" the flash-painting object
                if (flashCount <= 0) {
                    flashPaint = null;
                }
            }
        }
    }

    /**
     * External Citation
     * Date: 25 February 2016
     * Problem: Clueless on animating stuff
     * Resource:
     * http://www.techrepublic.com/blog/software-engineer/
     * bouncing-a-ball-on-androids-canvas/
     * Solution: Referenced the sample code.
     */

    /**
     * Causes the background to be changed ("flash") for the given period
     * of time.
     *
     * @param color  the color to flash
     * @param millis the number of milliseconds for this the flash should occur
     */
    public static void flash(int color, int millis) {
        flashCount = millis; // set the flash count
        flashPaint = new Paint(); // create painter ...
        flashPaint.setColor(color); // ... with the appropriate color
    }

    @Override
    public boolean doPause() {
        return false; //never pause
    }

    @Override
    public boolean doQuit() {
        return false; //never quit
    }


    /**
     * Handles what happens when the user touches the SurfaceView
     *
     * @param event the MotionEvent that happened
     */
    @Override
    public void onTouch(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        if (action == MotionEvent.ACTION_MOVE) {
            if (difficulty == 0) {
                if ((x < paddleCenter + 200) && x > paddleCenter - 200) {
                    //If sissy paddle, recenter the sissy paddle.
                    paddleCenter = x;
                }
            } else if (difficulty == 1) {
                if ((x < paddleCenter + 100) && x > paddleCenter - 100) {
                    //If amateur paddle, recenter the amateur paddle.
                    paddleCenter = x;
                }
            } else if (difficulty == 2) {
                if ((x < paddleCenter + 30) && x > paddleCenter - 30) {
                    //If paddle of the gods, recenter the amateur paddle.
                    paddleCenter = x;
                }
            }
        }
    }
    /**
     * External Citation
     * Date: 1 March 2016
     * Problem: Program was not animating paddle and ball simultaneously
     * Resource:
     * Dr. Nuxoll
     * Solution: Don't call drawPaddle from the onTouch method.
     */
}
