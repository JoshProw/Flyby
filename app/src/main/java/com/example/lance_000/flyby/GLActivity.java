package com.example.lance_000.flyby;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.util.HashMap;

/**
 * The type Gl activity.
 */
public class GLActivity extends AppCompatActivity {
    private GLSurfaceView mGlSurface;
    private GLGameRenderer mRenderer;
    private GLPlayer mPlayer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private boolean mUseAccelerometer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initial window settings.
        setWindowFlags();

        //Set the content view
        setContentView(R.layout.activity_gl);

        //Loads the mPlayer.
        loadPlayerContext();

        //Determines input method (buttons/sensor)
        initInputMethod();

        //Start the gl surface view
        bootstrapGlSurfaceView();
    }

    /**
     * Creates the gl surface view and sets the mRenderer implementation.
     */
    public void bootstrapGlSurfaceView() {
        //Obtain the gl surface view
        mGlSurface = (GLSurfaceView) findViewById(R.id.glSurfaceView);

        //set the gl surface mRenderer
        mGlSurface.setRenderer(createRenderer());
    }

    /**
     * Loads the mPlayer mContext from internal app storage.
     */
    public void loadPlayerContext() {
        try {
            //Load the mPlayer game object
            mPlayer = (GLPlayer) GLIOHandler.load(getApplicationContext(), getString(R.string.gl_player_save_file));
            mPlayer.setContext(getApplicationContext());
            mPlayer.setKeyMap(new HashMap<Integer, Boolean>());
        } catch (IOException | ClassNotFoundException e) {
            mPlayer = new GLPlayer(this.getApplicationContext(), R.raw.flybyship, R.raw.flybyshipm, 0f, -3, 0f, 0f, 0.0f, 0.0f);
        }
    }

    /**
     * Requests no window title and sets the window to be hardware accelerated.
     */
    public void setWindowFlags() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    /**
     * Determines the input method (sensors/buttons)
     */
    public void initInputMethod() {
        //The game buttons
        View[] views = new View[]{findViewById(R.id.btnLeft), findViewById(R.id.btnRight)};

        //Set up mAccelerometer
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //We have access to an mAccelerometer
        if (mUseAccelerometer && (mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) != null) {
            //Register sensor listener
            mSensorManager.registerListener(mPlayer, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
            //remove the buttons from the view
            for (View v : views) {
                v.setVisibility(View.INVISIBLE);
            }
        } else {
            //Since we have no mAccelerometer or its disabled, show buttons instead
            for (View v : views) {
                v.setOnTouchListener(mPlayer);
            }
        }
    }

    /**
     * Creates and initializes the game mRenderer.
     *
     * @return a new GLGameRender object.
     */
    public GLGameRenderer createRenderer() {
        //Create the mRenderer
        mRenderer = new GLGameRenderer(mPlayer, this.getApplicationContext());

        //inititalize the mRenderer
        mRenderer.init();

        return mRenderer;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurface.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPlayer != null) {
            try {
                GLIOHandler.serialize(getApplicationContext(),
                        getString(R.string.gl_player_save_file), mPlayer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (mAccelerometer != null)
            mSensorManager.registerListener(mPlayer, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);

        mGlSurface.onResume();
    }
}
