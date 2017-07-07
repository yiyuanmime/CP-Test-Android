package com.cp.user.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.devland.esperandro.serialization.Serializer;

/**
 * Created by yi on 07/07/2017.
 */

public class GsonSerializer implements Serializer {

    private final Gson gson;

    public GsonSerializer() {
        super();
        this.gson = new GsonBuilder().create();
    }

    public GsonSerializer(final Gson gson) {
        super();
        this.gson = gson;
    }

    @Override
    public String serialize(final Object object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T deserialize(final String serializedObject, final Class<T> clazz) {

        T deserialized = null;

        if (serializedObject != null)
            deserialized = gson.fromJson(serializedObject, clazz);

        return deserialized;
    }

}