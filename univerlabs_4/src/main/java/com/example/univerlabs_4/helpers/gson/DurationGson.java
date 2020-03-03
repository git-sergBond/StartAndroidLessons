package com.example.univerlabs_4.helpers.gson;

import android.net.ParseException;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationGson {

    static String DURATION_FORMAT;
/*
    public static class DurationDeserializer implements JsonDeserializer<Duration> {

        @Override
        public Duration deserialize(JsonElement jsonElement, Type typeOF,
                                    JsonDeserializationContext context) throws JsonParseException {
            try {
                return Duration.parse(jsonElement.getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            throw  new JsonParseException("Unparseable Duration: \"" + jsonElement.getAsString()+ "\". Supported formats: " + DURATION_FORMAT);
        }
    }
*/
}
