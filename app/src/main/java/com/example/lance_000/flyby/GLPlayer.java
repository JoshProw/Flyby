package com.example.lance_000.flyby;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.opengles.GL10;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dale on 22/06/16.
 */

/**
 * Created by Dale on 22/06/16.
 */
public class GLPlayer extends GLGameObject implements View.OnTouchListener,Serializable{

    private static final String PLAYER_LOG_TAG = "Player";

    //How many coins the player current has
    private int coins;

    private int health;

    private Context context;

    private ConcurrentHashMap<Integer, Boolean> keyMap = new ConcurrentHashMap<>(4);


    public GLPlayer(Context context, int resID, int mtlID, float intialX, float initialY,
                    float initialZ, float aposXSpeed, float aposYSpeed, float aposZSpeed) {
        super(resID, mtlID, intialX, initialY, initialZ, aposXSpeed, aposYSpeed, aposZSpeed);
        this.context = context;
    }


    public boolean isCollidingWith(GLGameObject object){
        //to implement
        return false;
    }

    @Override
    public void draw(GL10 gl) {
        move();
        super.draw(gl);
    }

    public void move(){
        if(playerMovingLeft()){
            this.setPosXSpeed(0.1f);
        }else if(playerMovingRight()){
            this.setPosXSpeed(-0.1f);
        }else{
            this.setPosXSpeed(0);
        }
    }

    public boolean playerMovingLeft(){
        Boolean val = keyMap.get(R.id.btnLeft);
        return val != null && val;
    }

    public boolean playerMovingRight() {
        Boolean val = keyMap.get(R.id.btnRight);
        return val != null && val;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                keyMap.put(v.getId(),true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                keyMap.put(v.getId(),false);
                break;
        }
        return true;
    }
}