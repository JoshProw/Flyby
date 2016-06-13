package com.example.lance_000.flyby;

import android.graphics.PointF;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by lance_000 on 9/06/2016.
 *
 * Class for storing all the methods and properties of game 3d objects
 */
public class GLGameObject {

    float alpha = 1;
    float size = 2;

    float posXSpeed;
    float posYSpeed;
    float posZSpeed;

    float posX = 0.f;
    float posY = 0.f;
    float posZ = 0.f;

    // Vertices to make a triangle
    private float vertices[];

    // Colours for vertices
    private float verticesColours[];

    // chunks of cube, drawing them with triangles
    private short[] pointIndex;


    private FloatBuffer vertBuffer;
    private ShortBuffer pointBuffer;
    private FloatBuffer colourBuffer;

    public GLGameObject(float aposXSpeed, float aposYSpeed, float aposZSpeed) {

        // Set velocity
        posXSpeed = aposXSpeed;
        posYSpeed = aposYSpeed;
        posZSpeed = aposZSpeed;
    }

    public void loadBuffers()
    {
        // Store vertices (4 bytes per float)
        ByteBuffer bBuff = ByteBuffer.allocateDirect(vertices.length*4);
        bBuff.order(ByteOrder.nativeOrder()); // prevents garbage collector from throwing data away

        vertBuffer = bBuff.asFloatBuffer(); // use as float buffer
        vertBuffer.put(vertices); // store vertices in buffer
        vertBuffer.position(0); // starting position in buffer

        // Store points (2 bytes per short)
        ByteBuffer pointBuff = ByteBuffer.allocateDirect(pointIndex.length*2);
        pointBuff.order(ByteOrder.nativeOrder());

        pointBuffer = pointBuff.asShortBuffer();
        pointBuffer.put(pointIndex);
        pointBuffer.position(0);

        /*
        // Store points (2 bytes per short)
        ByteBuffer colBuff = ByteBuffer.allocateDirect(verticesColours.length*4);
        colBuff.order(ByteOrder.nativeOrder());

        colourBuffer = colBuff.asFloatBuffer();
        colourBuffer.put(verticesColours);
        colourBuffer.position(0);*/
    }

    public void setVertices(Vector<Float> newVectices)
    {
        vertices=new float[newVectices.size()];

        //newVectices.toArray(vertices);

        for(int i = 0; i<newVectices.size();i++)
        {
            vertices[i] = newVectices.elementAt(i);
        }
    }

    public void setPoints(Vector<Integer> newPoints)
    {
        pointIndex=new short[newPoints.size()];

        for(int i = 0; i<newPoints.size();i++)
        {
            pointIndex[i] = newPoints.elementAt(i).shortValue();
        }
    }

    public float[] getVertices()
    {
        return vertices;
    }

    private void setXspeed(float x)
    {
        posXSpeed = x;
    }

    private void setYspeed(float y)
    {
        posXSpeed = y;
    }

    private void setZspeed(float z)
    {
        posXSpeed = z;
    }

    public void draw(GL10 gl){

        posX+= posXSpeed;
        posY+= posYSpeed;
        posZ+= posZSpeed;

        gl.glPushMatrix();
        gl.glTranslatef(posX,posY,posZ);

        //Matrix.setIdentityf(vertices, 0);
        //Matrix.translateM(vertices,0,posX,posY,0);
        Matrix.scaleM(vertices, 0, 0.25f, 0.25f, 0.25f); // apply scale

        gl.glFrontFace(GL10.GL_CW); // connect points in clockwise order

        // remove faces not seen by camera
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_FRONT); // which faces to remove

        // enable state to read from vertex array
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        //gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        // first: number of points (2 for 2d)
        // second: type of values in vertex array
        // third: if wanting to skip over values each step
        // fourth: buffer to get values from
        gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertBuffer);

        //gl.glColorPointer(4,GL10.GL_FLOAT,0,colourBuffer);
        // first: way to connect points
        // second: how many points do we have?
        // third: type of values we're working with
        // fourth: buffer to get points from
        //gl.glDrawArrays(GL10.GL_TRIANGLES,0,pointIndex.length);
        gl.glDrawElements(GL10.GL_TRIANGLES,pointIndex.length,GL10.GL_UNSIGNED_SHORT,pointBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        //gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);

        gl.glPopMatrix();
    }
}
