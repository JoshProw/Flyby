package com.example.lance_000.flyby;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Dale on 22/06/16.
 */

//Required in order for layout to work correctly.
public class FlybyGlSurfaceView extends GLSurfaceView {

    public FlybyGlSurfaceView(Context context) {
        super(context);
    }

    public FlybyGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
