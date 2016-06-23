package com.example.lance_000.flyby;


public final class SensorUtils {

    private SensorUtils() {
    }


    //remove transient forces
    public static void lowPassFilter(float[] values, float[] gravity) {
        for (int i = 0; i < values.length; i++) {
            gravity[i] = values[i] * gravity[i];
        }
    }

    //Remove constant forces
    //no need to return
    public static void highPassFilter(float[] values, float[] gravity) {
        for (int i = 0; i < values.length; i++) {
            gravity[i] = values[i] * gravity[i];
        }
    }
}
