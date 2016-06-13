package com.example.lance_000.flyby;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

/** Mesh
 * Created by Josh on 12/06/2016.
 *
 * A Mesh loader for importing obj files
 */

public class Mesh {
    Vector <Float> vertices;
    Vector <Float> colours;
    Vector <Integer> idx;
    Vector<Material> materials;

    /**Method : loadMats
     * loads materials from the MTL file and stores them in a vector collection
     * @param filename the string file name of the MTL file (example.mtl)
     */
    void loadMats(String filename) {
        File file = new File(filename);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
            String line;

            Material newMaterial = null;
            Boolean first = true;

            while ((line = br.readLine()) != null){
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
                    int specWeight = Integer.parseInt(data[1]);
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

    /**Mesh
     * Initializer for the Mesh Object
     */
    public Mesh()
    {

    }

    /**Method : load
     * loads the mesh vertex and material data stored in the OBJ and MTL files
     * @param filename the file name of the OBJ file as a String
     */
    public void load(String filename) {
        File file = new File(filename);
        BufferedReader br = null;

        Material currentMat = null;

        try {
            br = new BufferedReader(new FileReader(file));
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
                    loadMats(mtlFile);

                } else if (prefix.equalsIgnoreCase("usemtl")) {
                    //loop through materials and if one matches the name set it to current mat
                    continue;

                } else if (prefix.equalsIgnoreCase("o")) {
                    //Object name - ignored
                    continue;

                } else if (prefix.equalsIgnoreCase("s")) {
                    //Smooth Shading - ignored
                    continue;

                } else if (prefix.equalsIgnoreCase("v")) {
                    for (int i = 1;i < 4; i++)
                    {
                        vertices.add(Float.parseFloat(data[i]));
                    }
                    continue;

                } else if (prefix.equalsIgnoreCase("f")) {
                    for (int i = 1;i < 4; i++)
                    {
                        String face = data[i];

                        String[] faceData = face.split("/");
                        for(String tmp: faceData){
                            idx.add(Integer.parseInt(tmp)-1);
                        }
                        colours.addAll(new Vector<Float>(Arrays.asList(currentMat.specularCol)));
                    }
                    continue;

                } else if (prefix.equalsIgnoreCase("illum")) {
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
    }
}

