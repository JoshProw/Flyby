package com.example.lance_000.flyby;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.opengles.GL10;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;


public class GLPlayer extends GLGameObject implements View.OnTouchListener, Serializable, SensorEventListener {

    /*Player attributes*/
    private int mCoins, mHealth;

    /*The current mContext*/
    private transient Context mContext;

    /*Used for applying high pass filter to accelerometer*/
    private float[] mGravity = new float[3];

    /*Concurrent hash map handling current player pressed buttons.*/
    private transient HashMap<Integer, Boolean> mKeyMap;

    /**
     * Instantiates a new Gl player.
     *
     * @param context    the current app mContext.
     * @param resID      the resource id associated with this player.
     * @param mtlID      the mtl resource id.
     * @param intialX    the intial x position of the player.
     * @param initialY   the initial y position of the player.
     * @param initialZ   the initial z position of the player.
     * @param aposXSpeed the speed of the player in the x direction.
     * @param aposYSpeed the speed of the player in the y direction.
     * @param aposZSpeed the speed of the player in the z direction.
     */
    public GLPlayer(Context context, int resID, int mtlID, float intialX, float initialY,
                    float initialZ, float aposXSpeed, float aposYSpeed, float aposZSpeed) {
        super(resID, mtlID, intialX, initialY, initialZ, aposXSpeed, aposYSpeed, aposZSpeed);
        this.mContext = context;
        this.mKeyMap = new HashMap<>(4);
    }


    /**
     * Checks if the player has collided with a game object.
     *
     * @param {@class GLGameObject} the game object to check the collision with
     * @return true if the object is colliding with this player.
     */
    public boolean checkCollision(GLGameObject obstacle) {
        boolean collision = false;

        if (getPosZ() > (obstacle.getPosZ() - 3f) && getPosZ() < (obstacle.getPosZ() + 3f)) {
            switch (obstacle.getLocation()) {

                case "left":
                    if (getPosX() > -2f) {
                        collision = true;
                    }
                    break;
                case "right":
                    if (getPosX() < 2f) {
                        collision = true;
                    }
                    break;
                case "top":
                    if (getPosY() > 1f) {
                        collision = true;
                    }
                    break;
                case "bottom":
                    if (getPosY() < -1f) {
                        collision = true;
                    }
                    break;
            }
        }
        return collision;
    }

    /**
     * Overrides draw() in super class to account for player movement based on input.
     */
    @Override
    public void draw(GL10 gl) {
        super.draw(gl);
        move();
    }

    /**
     * Changes the position in the x direction of a player. Manages ship boundries.
     */
    public void move() {
        if (playerMovingLeft()) {
            this.setPosXSpeed(0.1f);
        } else if (playerMovingRight()) {
            this.setPosXSpeed(-0.1f);
        } else {
            this.setPosXSpeed(0);
        }

        if (getPosX() < -7f)
            setPosX(-7f);

        if (getPosX() > 7f)
            setPosX(7f);
    }

    /**
     * Method to check whether the player is moving left.
     *
     * @return true, if the player is moving left.
     */
    public boolean playerMovingLeft() {
        Boolean val = mKeyMap.get(R.id.btnLeft);
        return val != null && val;
    }


    /**
     * A method to check whether the player is moving right.
     *
     * @return true, if the player is moving right.
     */
    public boolean playerMovingRight() {
        Boolean val = mKeyMap.get(R.id.btnRight);
        return val != null && val;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                mKeyMap.put(v.getId(), true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mKeyMap.put(v.getId(), false);
                break;
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                //Todo: apply high pass filter to de-emphasize constant force (g) applied to the device

                SensorUtils.lowPassFilter(event.values, mGravity);
                SensorUtils.highPassFilter(event.values, mGravity);

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                Log.v("Sensor", String.format("X  = %f", x));
                Log.v("Sensor", String.format("Y  = %f", y));
                Log.v("Sensor", String.format("Z  = %f", z));
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                //x*sin(theta/2)
                float rX = event.values[0];
                //y*sin(theta/2)
                float rY = event.values[1];
                //z*sin(theta/2)
                float rZ = event.values[2];
                //cos(theta/2)
                float cosAngle = event.values[3];

                //Use y since device is fixed to landscape.
                //Todo: find normals and compute orientation of device
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public int getCoins() {
        return mCoins;
    }

    public void setCoins(int coins) {
        this.mCoins = coins;
    }

    public int getHealth() {
        return mHealth;
    }

    public void setHealth(int mHealth) {
        this.mHealth = mHealth;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public HashMap<Integer, Boolean> getKeyMap() {
        return mKeyMap;
    }

    public void setKeyMap(HashMap<Integer, Boolean> keyMap) {
        this.mKeyMap = keyMap;
    }

    @Override
    public void generateColours(float r, float g, float b, float a) {
        super.generateColours(r, g, b, a);
    }

    @Override
    public void setColours(float[] colours) {
        super.setColours(colours);
    }

    @Override
    public void loadBuffers() {
        super.loadBuffers();
    }

    @Override
    public void setPoints(Vector<Integer> newPoints) {
        super.setPoints(newPoints);
    }

    @Override
    public float[] getVertices() {
        return super.getVertices();
    }

    @Override
    public void setVertices(Vector<Float> newVectices) {
        super.setVertices(newVectices);
    }

    @Override
    public void loopZ() {
    }

    @Override
    public void setObstacleLocation() {
        super.setObstacleLocation();
    }

    @Override
    public String getLocation() {
        return super.getLocation();
    }

    @Override
    public float getAlpha() {
        return super.getAlpha();
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
    }

    @Override
    public float getSize() {
        return super.getSize();
    }

    @Override
    public void setSize(float size) {
        super.setSize(size);
    }

    @Override
    public float getPosXSpeed() {
        return super.getPosXSpeed();
    }

    @Override
    public void setPosXSpeed(float posXSpeed) {
        super.setPosXSpeed(posXSpeed);
    }

    @Override
    public float getPosYSpeed() {
        return super.getPosYSpeed();
    }

    @Override
    public void setPosYSpeed(float posYSpeed) {
        super.setPosYSpeed(posYSpeed);
    }

    @Override
    public float getPosZSpeed() {
        return super.getPosZSpeed();
    }

    @Override
    public void setPosZSpeed(float posZSpeed) {
        super.setPosZSpeed(posZSpeed);
    }

    @Override
    public float getPosX() {
        return super.getPosX();
    }

    @Override
    public void setPosX(float posX) {
        super.setPosX(posX);
    }

    @Override
    public float getPosY() {
        return super.getPosY();
    }

    @Override
    public void setPosY(float posY) {
        super.setPosY(posY);
    }

    @Override
    public float getPosZ() {
        return super.getPosZ();
    }

    @Override
    public void setPosZ(float posZ) {
        super.setPosZ(posZ);
    }

    @Override
    public int getMtlResourceID() {
        return super.getMtlResourceID();
    }

    @Override
    public void setMtlResourceID(int mtlID) {
        super.setMtlResourceID(mtlID);
    }

    @Override
    public int getResourceID() {
        return super.getResourceID();
    }

    @Override
    public void setResourceID(int resourceID) {
        super.setResourceID(resourceID);
    }
}