package com.example.lance_000.flyby;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Vector;

/** Mesh
 * Created by Josh on 12/06/2016.
 *
 * A Mesh loader for importing obj files
 */

public class Mesh {
    static final Float[] DEFAULT_COLOUR = {0.8f,0.8f,0.8f,1f};
    public Vector<Float> vertices = new Vector<Float>();
    public Vector<Float> colours = new Vector<Float>();
    public Vector<Integer> idx = new Vector<Integer>();
    public Vector<Material> materials = new Vector<Material>();

    /**Mesh
     * Initializer for the Mesh Object
     */
    public Mesh()
    {
    }

    public Vector<Float> getVertices()
    {
        return vertices;
    }

    public Vector<Integer> getPoints()
    {
        return idx;
    }

    public void LoadText(int resourceId, Context activity) {
        // The InputStream opens the resourceId and sends it to the buffer
        InputStream is = activity.getResources().openRawResource(resourceId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;

        try {
            // While the BufferedReader readLine is not null
            while ((readLine = br.readLine()) != null) {
                Log.d("TEXT", readLine);
            }

            // Close the InputStream and BufferedReader
            is.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Method : loadMats
     * loads materials from the MTL file and stores them in a vector collection
     *
     * @param filename the string file name of the MTL file (example.mtl)
     */
    void loadMats(InputStream filename, Context activity) {

        BufferedReader br = null;

        try {
            File f = new File("your file name");
            br = new BufferedReader(new InputStreamReader(filename));
        }
        catch(Exception e)
        {
            Log.d("TEST","FAILED TO LOAD");
            // failed to load file
        }


        try {

            String line;

            Material newMaterial = null;
            Boolean first = true;

            while ((line = br.readLine()) != null){
                Log.d("LINE",line);
                String data[] = line.split(" ");

                String prefix = data[0];

                if (prefix.equalsIgnoreCase("#")) {
                    // comment, don't do anything
                    continue;
                } else if (prefix.equalsIgnoreCase("newmtl")) {
                    String name = data[1];

                    if (!first) {
                        materials.add(newMaterial);
                    } else {
                        first = false;
                    }

                    newMaterial = new Material();
                    newMaterial.name = name;
                    continue;

                } else if (prefix.equalsIgnoreCase("Ns")) {
                    Float specWeight = Float.parseFloat(data[1]);
                    newMaterial.specularWeight = specWeight;
                    continue;
                } else if (prefix.equalsIgnoreCase("Ka")) {
                    newMaterial.ambientCol = readColour(data);
                    continue;

                } else if (prefix.equalsIgnoreCase("Kd")) {
                    newMaterial.diffuseCol = readColour(data);
                    continue;

                } else if (prefix.equalsIgnoreCase("Ks")) {
                    newMaterial.specularCol = readColour(data);
                    continue;

                } else if (prefix.equalsIgnoreCase("Tr")) {
                    float transparency = Float.parseFloat(data[1]);

                    newMaterial.ambientCol[3] = 1 - transparency;
                    newMaterial.diffuseCol[3] = 1 - transparency;
                    newMaterial.specularCol[3] = 1 - transparency;
                    continue;

                } else if (prefix.equalsIgnoreCase("d")) {
                    float dissolved = Float.parseFloat(data[1]);

                    newMaterial.ambientCol[3] = dissolved;
                    newMaterial.diffuseCol[3] = dissolved;
                    newMaterial.specularCol[3] = dissolved;
                    continue;

                } else if (prefix.equalsIgnoreCase("illum")) {
                    // illumination models aren't implemented at yet
                    continue;

                } else {
                    // ignore anything else
                }
            }
            materials.add(newMaterial);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Log.d("MATERIAL",""+materials);
    }

    /**Method : readColour
     * reads the colour data from a colour MTL field
     * @param data the MTL field as an array
     * @return a Float[] of the colour data
     */
    private Float[] readColour(String[] data){
        Float[] colour = {0.4f,0.4f,0.4f,1f};
        try {
            //Red
            colour[0] = Float.parseFloat(data[1]);

            //Green
            colour[1] = Float.parseFloat(data[2]);

            //Blue
            colour[2] = Float.parseFloat(data[3]);

            return colour;

        }catch (Exception e){
            return colour;
        }
    }

    /**Method : load
     * loads the mesh vertex and material data stored in the OBJ and MTL files
     * @param filename the file name of the OBJ file as a String
     */
    public void load(InputStream filename, Context activity, InputStream inputStreamMtl) {

        BufferedReader br = null;
        InputStream inputStream;

        try {
            File f = new File("your file name");
            br = new BufferedReader(new InputStreamReader(filename));
        }
        catch(Exception e)
        {
            // failed to load file
            System.out.print("FAILED TO LOAD FILE");
        }

        Material currentMat = null;

        try {
            String line;

            Material newMaterial = null;
            Boolean first = true;

            while ((line = br.readLine()) != null){
                String data[] = line.split(" ");

                String prefix = data[0];

                if (prefix.equalsIgnoreCase("#")) {
                    // comment, don't do anything
                    continue;
                } else if (prefix.equalsIgnoreCase("mtllib")) {
                    String mtlFile = data[1];
                    loadMats(inputStreamMtl, activity);

                }
                else if (prefix.equalsIgnoreCase("usemtl")) {
                    for (Material mat: materials) {
                        if(mat.name.equalsIgnoreCase(data[1])){
                            currentMat = mat;
                            break;
                        }
                    }
                    continue;

                }
                else if (prefix.equalsIgnoreCase("o")) {
                    //Object name - ignored
                    continue;

                }
                else if (prefix.equalsIgnoreCase("s")) {
                    //Smooth Shading - ignored
                    continue;

                }
                else if (prefix.equalsIgnoreCase("v")) {

                    String noPrefix = line.substring(2);
                    String[] vertData = noPrefix.split(" ");

                    //Log.d("TEST",faceData[0]+" "+faceData[1]+" "+faceData[2]);
                    for(int i = 0; i<3;i++)
                    {
                        vertices.add(Float.parseFloat(vertData[i]));
                    }

                    continue;

                }
                else if (prefix.equalsIgnoreCase("f"))
                {

                        String noPrefix = line.substring(2);
                        noPrefix = noPrefix.replace(" ","//");
                        String[] faceData = noPrefix.split("//");

                        for(int j = 0; j< 6;j+=2)
                        {
                            idx.add(Integer.parseInt(faceData[j])-1);
                        }

                    if (currentMat != null){
                        colours.addAll(Arrays.asList(currentMat.diffuseCol));
                    }else{
                        colours.addAll(Arrays.asList(DEFAULT_COLOUR));
                    }
                    continue;

                }
                else if (prefix.equalsIgnoreCase("illum")) {
                    // illumination models aren't implemented at yet
                    continue;

                } else {
                    // ignore anything else
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("VERTS","VERTICES ARE: "+vertices);
        Log.d("POINTS","POINTS ARE: "+idx);
    }
}

