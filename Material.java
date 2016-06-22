package com.example.lance_000.flyby;

/**
 * Material
 * Created by Josh on 12/06/2016.
 * <p>
 * A Material class, stores object material data
 */
public class Material {
    String name;
    Float[] diffuseCol;
    Float[] specularCol;
    Float[] ambientCol;

    Float specularWeight;

    /**
     * Material
     * Initializer for the Material object
     */
    public Material() {
        String name = "newMaterial";
        Float[] diffuseCol = {0f, 0f, 0f, 0f};
        Float[] specularCol = {0f, 0f, 0f, 0f};
        Float[] ambientCol = {0f, 0f, 0f, 0f};

        specularWeight = 100f;
    }
}
