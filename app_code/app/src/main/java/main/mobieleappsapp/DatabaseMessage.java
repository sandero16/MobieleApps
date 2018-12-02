package main.mobieleappsapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class DatabaseMessage {

    @PrimaryKey(autoGenerate = true)
    private int id;

    int type;
    String message;
    String extra;

    public DatabaseMessage(int id, int type, String message, String extra) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.extra = extra;
    }

    public DatabaseMessage(JSONObject obj){

        try {

            id = 0;
            type = (Integer) obj.get("type");
            message = (String)obj.get("message");
            extra = (String)obj.get("extra");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public JSONObject toJSON() {

        JSONObject obj = new JSONObject();

        try {
            obj.put("type",type);
            obj.put("message",message);
            obj.put("extra",extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
