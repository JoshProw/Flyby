package com.example.lance_000.flyby;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Dale on 22/06/16.
 */
public class GlIOHandler {


    Context context;

    public GlIOHandler(Context c) {
        this.context = c;
    }

    public GLGameObject load(String saveFile) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream =
                new ObjectInputStream(context.openFileInput(saveFile));

        return (GLGameObject) objectInputStream.readObject();
    }

    public void serialize(String saveFile, GLGameObject object) throws IOException {
        FileOutputStream fileOutputStream;
        fileOutputStream = context.openFileOutput(saveFile, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
    }
}
