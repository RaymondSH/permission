package mmall.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Raymond on 2019/1/9.
 */
@Getter
@Setter
public class JSONData {
    private boolean result;
    private String msg;
    private Object data;


    public JSONData(boolean result){
        this.result = result;
    }

    public static JSONData success(Object data,String msg){
        JSONData jsonData = new JSONData(true);
        jsonData.data = data;
        jsonData.msg = msg;
        return jsonData;
    }

    public static JSONData success(Object object){
        JSONData jsonData = new JSONData(true);
        jsonData.data = object;
        return jsonData;
    }

    public static JSONData success(){
        return new JSONData(true);
    }


    public static JSONData fail(String msg){
        JSONData jsonData = new JSONData(false);
        jsonData.msg = msg;
        return jsonData;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> result = new HashMap<>();
        result.put("result", this.result);
        result.put("msg",msg);
        result.put("data",data);
        return result;
    }
}
