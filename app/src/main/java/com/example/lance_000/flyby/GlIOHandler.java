package com.example.lance_000.flyby;

import android.content.Context;

import java.io.*;
import java.util.Objects;

/**
 * Created by Dale
 * <p>
 * Utility class for performing IO operations.
 */
public final class GLIOHandler {

    private GLIOHandler() {
    }

    /**
     * Loads a serialized game object.
     *
     * @param context  the context
     * @param saveFile the save file
     * @return the gl game object
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public static GLGameObject load(final Context context, String saveFile) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(context);
        ObjectInputStream objectInputStream =
                new ObjectInputStream(context.openFileInput(saveFile));
        return (GLGameObject) objectInputStream.readObject();
    }

    /**
     * Serialize.
     *
     * @param context  the context
     * @param saveFile the save file
     * @param object   the object
     * @throws IOException the io exception
     */
    public static void serialize(final Context context, final String saveFile, final GLGameObject object) throws IOException {
        Objects.requireNonNull(context);
        FileOutputStream fileOutputStream;
        fileOutputStream = context.openFileOutput(saveFile, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
    }

    /**
     * Reads a raw text file to a string.
     * Helpful method for texture/shader loading
     *
     * @param context    the context
     * @param resourceId the resource id
     * @return the string
     */
    public static String readTextFileRaw(final Context context, final int resourceId) {
        Objects.requireNonNull(context);
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
