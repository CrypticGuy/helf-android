package com.example.helfcode;

import android.content.Context;
import android.util.JsonReader;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelfHelper {
    public static String getJSONData(Context context) throws IOException {
        String strJSON;
        StringBuilder buf = new StringBuilder();
        InputStream json;
        try {

            json = context.getResources().openRawResource(R.raw.helf_codes);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            while((strJSON = in.readLine()) != null) {
                buf.append(strJSON);
            }
            in.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    public static JSONObject getJSONObject(Context context) {
        try {
            JSONObject jsonObject = new JSONObject(getJSONData(context));
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
