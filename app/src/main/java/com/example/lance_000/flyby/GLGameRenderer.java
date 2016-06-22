package com.example.lance_000.flyby;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;
import android.renderscript.ScriptGroup;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by lance_000 on 9/06/2016.
 *
 * Main renderer that handles all the game rendering
 */
public class GLGameRenderer implements GLSurfaceView.Renderer {

    // Objects used to load .OBJ mesh file
    Mesh meshLoader;

    //NOTE: bottom left corner of render window is 0,0
    // Objects to render
    private GLGameObject ship;
    private GLGameObject obstacle;
    private GLGameObject tunnel;

    // Constants for mesh file names
    final static String SHIPMESH = "";
    final static String OBSTACLEMESH01 = "";

    // Speed of world moving towards camera
    private static float ENVRIOZSPEED = 0.015f;

    public GLGameRenderer(Context activity){
        // Iniatilize Mesh loader
        meshLoader = new Mesh();

        // Load resource files into inputstreams for passing
        InputStream inputStream;
        InputStream inputStreamMtl;
        try {
            inputStream = activity.getResources().openRawResource(R.raw.flybyship);
            inputStreamMtl = activity.getResources().openRawResource(R.raw.flybyshipm);

            meshLoader.load(inputStream,activity,inputStreamMtl);

            inputStream.close();
            inputStreamMtl.close();
        }
        catch(Exception e)
        {
            // Error
        }
        // Intialize 3d objects


        ship = new GLGameObject(0f,-3f,0f,0f,0.f,0.0f);

        ship.setVertices(meshLoader.getVertices());
        ship.setPoints(meshLoader.getPoints());

        ship.generateColours(1,0,0,1);

        ship.loadBuffers();

        // Iniatilize Mesh loader
        meshLoader = new Mesh();
        // obstacle
        try {
            inputStream = activity.getResources().openRawResource(R.raw.obstacle);
            inputStreamMtl = activity.getResources().openRawResource(R.raw.obstaclem);

            meshLoader.load(inputStream,activity,inputStreamMtl);

            inputStream.close();
            inputStreamMtl.close();
        }
        catch(Exception e)
        {
            // Error
        }
        // Intialize 3d objects


        obstacle = new GLGameObject(0f,0f,30f,0f,0.f,-0.088f);
        //obstacle = new GLGameObject(0.f,0.0f,0.0f);

        obstacle.setVertices(meshLoader.getVertices());
        obstacle.setPoints(meshLoader.getPoints());
        obstacle.loadBuffers();

        // Tunnel
        // Iniatilize Mesh loader
        meshLoader = new Mesh();
        // obstacle
        try {
            inputStream = activity.getResources().openRawResource(R.raw.tunnel);
            inputStreamMtl = activity.getResources().openRawResource(R.raw.tunnelm);

            meshLoader.load(inputStream,activity,inputStreamMtl);

            inputStream.close();
            inputStreamMtl.close();
        }
        catch(Exception e)
        {
            // Error
        }
        // Intialize 3d objects


        tunnel = new GLGameObject(0f,0f,0f,0f,0.f,0f);
        //obstacle = new GLGameObject(0.f,0.0f,0.0f);

        tunnel.setVertices(meshLoader.getVertices());
        tunnel.setPoints(meshLoader.getPoints());

        tunnel.generateColours(0.2f,0.2f,0.2f,1);

        tunnel.loadBuffers();

    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // two lines boost performance at cost of quality
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);
        gl.glCullFace(GL10.GL_BACK);

        gl.glClearColor(0f,0f,0f,1);
        gl.glClearDepthf(1f);


    }

    float angle = 10;
    // main rendering loop
    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
        gl.glFogf(GL10.GL_FOG_START, 1f);
        gl.glFogf(GL10.GL_FOG_END, 20f);
        gl.glEnable(GL10.GL_FOG);

        gl.glDisable(GL10.GL_DITHER);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        // What camera is looking at
        GLU.gluLookAt(gl,0,0,-5,0,0,0,0,2.f,0);

        //gl.glScalef(0.2f,0.2f,0.2f);
        //gl.glRotatef(angle,0,1,0);
        // draw ship object
        tunnel.draw(gl);
        obstacle.draw(gl);
        ship.draw(gl);

        //angle+=1;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // setup camera
        gl.glViewport(0,0,width,height);
        float ratio = (float)width/height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        // camera frustum
        gl.glFrustumf(-ratio,ratio,-1,1.f,1,2500);
    }


}
