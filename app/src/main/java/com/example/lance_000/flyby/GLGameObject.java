package com.example.lance_000.flyby;

import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;
import java.util.Vector;

/**
 * Created by lance_000 on 9/06/2016.
 *
 * Class for storing all the methods and properties of game 3d objects
 */
public class GLGameObject implements Serializable {

    float alpha = 1;
    float size = 2;

    //Object speed
    float posXSpeed;
    float posYSpeed;
    float posZSpeed;

    //Current object position
    float posX;
    float posY;
    float posZ;
    private location mPos;
    // Vertices to make a triangle
    private float mVertices[];
    // Colours for mVertices
    private float mVerticesColours[];
    // chunks of cube, drawing them with triangles
    private short[] pointIndex;
    //buffers
    private transient FloatBuffer mVertBuffer;
    private transient ShortBuffer mPointBuffer;
    private transient FloatBuffer mColourBuffer;
    private int mtlID;
    private int resourceID;
    public GLGameObject(int resID, int mtlID, float initialX, float initialY, float initialZ, float aposXSpeed, float aposYSpeed, float aposZSpeed) {
        //Mtl resource id
        this.mtlID = mtlID;
        //Resource id
        this.resourceID = resID;

        // Set velocity
        posX = initialX;
        posY = initialY;
        posZ = initialZ;

        posXSpeed = aposXSpeed;
        posYSpeed = aposYSpeed;
        posZSpeed = aposZSpeed;
    }

    public void generateColours(float r,float g, float b, float a)
    {
        mVerticesColours = new float[(mVertices.length / 3) * 4];
        for (int i = 0; i < mVerticesColours.length; i += 4)
        {
            int j = i;
            mVerticesColours[j++] = r;
            mVerticesColours[j++] = g;
            mVerticesColours[j++] = b;
            mVerticesColours[j] = a;
        }
        for (int i = 0; i < mVerticesColours.length; i++)
        {
            Log.d("COLOURS", "" + mVerticesColours[i]);
        }
        Log.d("VERTICES COUNT", "" + mVertices.length);
        Log.d("COLOUR COUNT", "" + mVerticesColours.length);
    }

    public void setColours(float[] colours)
    {
        mVerticesColours = colours;
    }

    public void loadBuffers()
    {
        // Store mVertices (4 bytes per float)
        ByteBuffer bBuff = ByteBuffer.allocateDirect(mVertices.length * 4);
        bBuff.order(ByteOrder.nativeOrder()); // prevents garbage collector from throwing data away

        mVertBuffer = bBuff.asFloatBuffer(); // use as float buffer
        mVertBuffer.put(mVertices); // store vertices in buffer
        mVertBuffer.position(0); // starting position in buffer

        // Store points (2 bytes per short)
        ByteBuffer pointBuff = ByteBuffer.allocateDirect(pointIndex.length*2);
        pointBuff.order(ByteOrder.nativeOrder());

        mPointBuffer = pointBuff.asShortBuffer();
        mPointBuffer.put(pointIndex);
        mPointBuffer.position(0);

        if (mVerticesColours != null) {
            // Store points (2 bytes per short)
            ByteBuffer colBuff = ByteBuffer.allocateDirect(mVerticesColours.length * 4);
            colBuff.order(ByteOrder.nativeOrder());

            mColourBuffer = colBuff.asFloatBuffer();
            mColourBuffer.put(mVerticesColours);
            mColourBuffer.position(0);
        }
    }

    public void setPoints(Vector<Integer> newPoints)
    {
        pointIndex=new short[newPoints.size()];
        //newPoints.toArray(new Short[]{}); <--should work without loop

        for(int i = 0; i<newPoints.size();i++)
        {
            pointIndex[i] = newPoints.elementAt(i).shortValue();
        }
    }

    public float[] getVertices()
    {
        return mVertices;
    }

    public void setVertices(Vector<Float> newVectices) {
        mVertices = new float[newVectices.size()];

        //newVectices.toArray(mVertices);

        for (int i = 0; i < newVectices.size(); i++) {
            mVertices[i] = newVectices.elementAt(i);
            //Log.d("VERT INDEX: "+i,""+mVertices[i]);
        }
    }

    public location randomizeLocation() {
        Random randomizer = new Random();
        int randInt = randomizer.nextInt(4);
        mPos = location.values()[randInt];
        //Log.d("ENUM",mPos.toString());
        return mPos;
    }

    public void loopZ()
    {
        if (posZ <= 0) {
            posZ = 80;
            setObstacleLocation();
        }
    }

    public void setObstacleLocation()
    {
        switch (randomizeLocation()) {
            case left:
                posX = 10;
                posY = 0;
                break;
            case right:
                posX = -10;
                posY = 0;
                break;
            case top:
                posX = 0;
                posY = -10;
            case bottom:
                posX = 0;
                posY = 10;
        }
    }

    public void draw(GL10 gl){

        posX+= posXSpeed;
        posY+= posYSpeed;
        posZ+= posZSpeed;

        gl.glPushMatrix();
        gl.glTranslatef(posX,posY,posZ);

        //Matrix.setIdentityf(mVertices, 0);
        //Matrix.translateM(mVertices,0,posX,posY,0);
        Matrix.scaleM(mVertices, 0, 0.25f, 0.25f, 0.25f); // apply scale

        gl.glFrontFace(GL10.GL_CCW); // connect points in clockwise order

        // remove faces not seen by camera
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_FRONT); // which faces to remove

        // enable state to read from vertex array
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        if (mVerticesColours != null)
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        // first: number of points (2 for 2d)
        // second: type of values in vertex array
        // third: if wanting to skip over values each step
        // fourth: buffer to get values from
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertBuffer);

        if (mVerticesColours != null)
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColourBuffer);
        // first: way to connect points
        // second: how many points do we have?
        // third: type of values we're working with
        // fourth: buffer to get points from
        //gl.glDrawArrays(GL10.GL_TRIANGLES,0,pointIndex.length);
        gl.glDrawElements(GL10.GL_TRIANGLES, pointIndex.length, GL10.GL_UNSIGNED_SHORT, mPointBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        if (mVerticesColours != null)
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDisable(GL10.GL_CULL_FACE);

        gl.glPopMatrix();
    }

    public String getLocation() {
        if (mPos != null) {
            return mPos.toString();
        }
        return "";
    }

    private void setXspeed(float x) {
        posXSpeed = x;
    }

    private void setYspeed(float y) {
        posXSpeed = y;
    }

    private void setZspeed(float z) {
        posXSpeed = z;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getPosXSpeed() {
        return posXSpeed;
    }

    public void setPosXSpeed(float posXSpeed) {
        this.posXSpeed = posXSpeed;
    }

    public float getPosYSpeed() {
        return posYSpeed;
    }

    public void setPosYSpeed(float posYSpeed) {
        this.posYSpeed = posYSpeed;
    }

    public float getPosZSpeed() {
        return posZSpeed;
    }

    public void setPosZSpeed(float posZSpeed) {
        this.posZSpeed = posZSpeed;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPosZ() {
        return posZ;
    }

    public void setPosZ(float posZ) {
        this.posZ = posZ;
    }

    public int getMtlResourceID() {
        return mtlID;
    }

    public void setMtlResourceID(int mtlID) {
        this.mtlID = mtlID;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    private enum location {left, right, top, bottom}
}
