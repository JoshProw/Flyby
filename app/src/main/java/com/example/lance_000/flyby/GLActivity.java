package com.example.lance_000.flyby;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GLActivity extends AppCompatActivity {

    GLSurfaceView mainSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainSurface = new GLSurfaceView(this);
        mainSurface.setRenderer(new GLGameRenderer());
        setContentView(mainSurface);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainSurface.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mainSurface.onResume();
    }
}
