import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class MailObject {
    private LinkedHashMap<String, String> jsonDict;

    public MailObject(String msg){
        try {
            JSONObject obj = new JSONObject(msg);
            this.jsonDict = new LinkedHashMap<>(obj.length());
            this.toMap(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void toMap(JSONObject obj){
        Iterator<String> keysItr = obj.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            String value = obj.getString(key);
            this.jsonDict.put(key, value);
        }
    }

    public String getKeysInsert(){
        StringBuilder returnString= new StringBuilder();
        for(String key: this.jsonDict.keySet()){
            returnString.append(key).append(", ");
        }
        return returnString.substring(0, returnString.length() - 2);
    }

    public int getJsonDictLen(){
        return this.jsonDict.size();
    }

    public LinkedHashMap<String, String> getJsonDict() {
        return jsonDict;
    }

    public String getValue(String key){
        return this.jsonDict.get(key);
    }
}
