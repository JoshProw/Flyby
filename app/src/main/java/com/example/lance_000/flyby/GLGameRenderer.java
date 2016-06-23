package com.example.lance_000.flyby;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by lance_000 on 9/06/2016.
 * <p>
 * Main renderer that handles all the game rendering
 * <p>
 * Dale: Edited constructor + objects now take mtlResourceID+resourceID
 */
public class GLGameRenderer implements GLSurfaceView.Renderer {
    // Constants for mesh file names
    final static String mShipMesh = "";
    final static String mObstactMesh01 = "";

    // Speed of world moving towards camera
    private static final float mEnvironSpeed = 0.015f;
    //The current context
    final Context mContext;
    //True if a collision has been detected during rendering
    boolean mCollisionDetected = false;
    //The number of obstacles to render
    private int mNumObstacles;
    // Objects used to load .OBJ mesh file
    private Mesh meshLoader;
    private GLGameObject mTunnel;
    private GLPlayer mShip;
    //The game objects.
    private ArrayList<GLGameObject> mGLGameObjects;


    public GLGameRenderer(GLPlayer ship, Context activity) {
        this.mShip = ship;
        this.mContext = activity;
        this.mNumObstacles = 4;
    }

    public static float getEnvironSpeed() {
        return mEnvironSpeed;
    }

    public void init() {
        mGLGameObjects = new ArrayList() {
            {
                for (int i = 0; i < mNumObstacles; i++) {
                    GLGameObject obstacle = new GLGameObject(
                            R.raw.obstacle,
                            R.raw.obstaclem,
                            0f, 0f, 70f, 0f, 0.f, -0.088f);
                    obstacle.setObstacleLocation();
                    add(obstacle);
                }
            }
        };

        mTunnel = new GLGameObject(R.raw.tunnel, R.raw.tunnelm, 0f, 0f, 0f, 0f, 0.f, 0f);

        loadMeshFromResources(mTunnel);
        loadMeshFromResources(mShip);

        for (GLGameObject object : mGLGameObjects) {
            loadMeshFromResources(object);
        }

        mTunnel.generateColours(0.2f, 0.2f, 0.2f, 1);
        mTunnel.loadBuffers();
    }

    public void loadMeshFromResources(final GLGameObject gameObject) {

        meshLoader = new Mesh();

        // Load resource files into inputstreams for passing
        InputStream inputStream;
        InputStream inputStreamMtl;
        try {
            inputStream = mContext.getResources().openRawResource(gameObject.getResourceID());
            inputStreamMtl = mContext.getResources().openRawResource(gameObject.getMtlResourceID());

            meshLoader.load(inputStream, mContext, inputStreamMtl);

            inputStream.close();
            inputStreamMtl.close();
        } catch (Exception e) {
            //print stack trace
            e.printStackTrace();
        }

        gameObject.setVertices(meshLoader.getVertices());
        gameObject.setPoints(meshLoader.getPoints());
        gameObject.generateColours(1, 0, 0, 1);
        gameObject.loadBuffers();
    }

    /*
        Removed a game object from the rendering pipeline
     */
    public void removeGameObject(GLGameObject object) {
        mGLGameObjects.remove(object);
    }

    /*
        Adds a game object to the rendering pipeline
     */
    public void addGameObject(GLGameObject object) {
        mGLGameObjects.add(object);
    }

    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
        // two lines boost performance at cost of quality
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glCullFace(GL10.GL_BACK);

        gl.glClearColor(0f, 0f, 0f, 1);
        gl.glClearDepthf(1f);
    }

    // main rendering loop
    @Override
    public void onDrawFrame(final GL10 gl) {

        gl.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
        gl.glFogf(GL10.GL_FOG_START, 1f);
        gl.glFogf(GL10.GL_FOG_END, 20f);
        gl.glEnable(GL10.GL_FOG);

        gl.glDisable(GL10.GL_DITHER);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        // What camera is looking at
        GLU.gluLookAt(gl, 0, 0, -5, 0, 0, 0, 0, 2.f, 0);

        mTunnel.draw(gl);

        for (GLGameObject object : mGLGameObjects) {
            if (mShip.checkCollision(object)) {
                mCollisionDetected = true;
            }
            object.draw(gl);
            object.loopZ();
        }

        if (!mCollisionDetected)
            mShip.draw(gl);

        mCollisionDetected = false;
    }

    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, int height) {
        //Avoid divide by 0
        if (height == 0) height = 1;

        // setup camera
        gl.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        gl.glMatrixMode(GL10.GL_PROJECTION);

        gl.glLoadIdentity();

        // camera frustum
        gl.glFrustumf(-ratio, ratio, -1, 1.f, 1, 2500);
    }

    public int getNumObstacles() {
        return mNumObstacles;
    }

    public void setNumObstacles(int mNumObstacles) {
        this.mNumObstacles = mNumObstacles;
    }

    public Context getContext() {
        return mContext;
    }

    public Mesh getMeshLoader() {
        return meshLoader;
    }

    public void setMeshLoader(Mesh meshLoader) {
        this.meshLoader = meshLoader;
    }

    public GLGameObject getTunnel() {
        return mTunnel;
    }

    public void setTunnel(GLGameObject mTunnel) {
        this.mTunnel = mTunnel;
    }

    public GLPlayer getShip() {
        return mShip;
    }

    public void setShip(GLPlayer mShip) {
        this.mShip = mShip;
    }
}
