package com.example.lance_000.flyby;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

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

    // Constants for mesh file names
    final static String SHIPMESH = "";
    final static String OBSTACLEMESH01 = "";

    // Speed of world moving towards camera
    private static float ENVRIOZSPEED = 0.015f;

    public GLGameRenderer(){
        // Iniatilize Mesh loader
        meshLoader = new Mesh();
        meshLoader.load("flybyship.obj");

        // Intialize 3d objects
        ship = new GLGameObject(0.f,0.f,0.015f);
        obstacle = new GLGameObject(0.f,0.0015f,0.0015f);

        ship.setVertices(meshLoader.vertices);
        ship.setPoints(meshLoader.idx);
        ship.loadBuffers();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // two lines boost performance at cost of quality
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);
        gl.glCullFace(GL10.GL_BACK);

        gl.glClearColor(.2f,.5f,0.9f,1);
        gl.glClearDepthf(1f);


    }

    // main rendering loop
    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glDisable(GL10.GL_DITHER);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        // What camera is looking at
        GLU.gluLookAt(gl,0,0,-5,0,0,0,0,2.f,0);

        // draw ship object


        //obstacle.draw(gl);


        ship.draw(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {


        // setup camera

        gl.glViewport(0,0,width,height);
        float ratio = (float)width/height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        // camera frustum
        gl.glFrustumf(-ratio,ratio,-1,1.f,1,5);
    }


}
