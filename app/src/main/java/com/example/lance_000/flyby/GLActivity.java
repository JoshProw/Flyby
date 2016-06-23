package com.example.lance_000.flyby;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

public class GLActivity extends AppCompatActivity {

    View[] views;
    private GLSurfaceView _glSurface;
    private GlOHandler glIOHandler;
    private GLGameRenderer renderer;
    private GLPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);


        setContentView(R.layout.activity_gl);

        views = new View[]{
                findViewById(R.id.btnLeft),
                findViewById(R.id.btnRight)
        };

        //Create the game object loader
        glIOHandler = new GlOHandler(this.getApplicationContext());

        try {
            //Load the player game object
            player = (GLPlayer) glIOHandler.load(getString(R.string.gl_player_save_file));
        } catch (IOException | ClassNotFoundException e) {
            player = new GLPlayer(this.getApplicationContext(), R.raw.flybyship, R.raw.flybyshipm, 0f, -3, 0f, 0f, 0.0f, 0.0f);
        }

        for (View v : views) {
            v.setOnTouchListener(player);
        }

        //Obtain the gl surface view
        _glSurface = (GLSurfaceView) findViewById(R.id.glSurfaceView);

        //Create the renderer
        renderer = new GLGameRenderer(this.getApplicationContext());

        //Add player to rendering pipeline
        renderer.addGameObject(player);

        //inititalize the renderer
        renderer.init();

        //set the gl surface renderer
        _glSurface.setRenderer(renderer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _glSurface.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            try {
                glIOHandler.serialize(
                        getString(R.string.gl_player_save_file), player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        _glSurface.onResume();
    }
}
