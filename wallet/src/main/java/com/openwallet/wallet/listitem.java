package com.openwallet.wallet;

import org.json.JSONException;
import org.json.JSONObject;

public class listitem {
    public String Name;
    public String Info;
    public int    Image;

    public listitem(String Name, String Info, int Image){
        this.Name = Name;
        this.Info = Info;
        this.Image = Image;
    }
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Name", this.Name);
            obj.put("Info", this.Info);
            obj.put("Image", this.Image);
        } catch (JSONException e) {

        }
        return obj;
    }
}
