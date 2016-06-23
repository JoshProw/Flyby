package com.example.lance_000.flyby;

import android.content.Context;

import java.io.*;

/**
 * Created by Dale on 22/06/16.
 */
public class GlOHandler {
    private final Context context;

    public GlOHandler(Context c) {
        this.context = c;
    }


    //Loads a game object from disk
    public GLGameObject load(String saveFile) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream =
                new ObjectInputStream(context.openFileInput(saveFile));

        return (GLGameObject) objectInputStream.readObject();
    }


    //Serializes a game object
    public void serialize(final String saveFile, final GLGameObject object) throws IOException {
        FileOutputStream fileOutputStream;
        fileOutputStream = context.openFileOutput(saveFile, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
    }

    //Helpful method for texture loading if we get time
    public String readTextFileRaw(final int resourceId) {
        final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(
                        resourceId)));

        String nextLine;
        final StringBuilder rawTxt = new StringBuilder();

        try {
            while ((nextLine = bufferedReader.readLine()) != null) {
                rawTxt.append(nextLine);
                rawTxt.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return rawTxt.toString();
    }
}
