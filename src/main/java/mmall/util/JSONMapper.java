package mmall.util;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Raymond on 2019/1/10.
 */
public class JSONMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(JSONMapper.class);
    static {
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    public static <T> String objToStr(T var){
        if (var == null){
            return null;
        }
        try {
            return var instanceof String ? (String)var : objectMapper.writeValueAsString(var);
        }catch (Exception e){
            logger.warn("parse Object to String exception,error:{}",e);
            return null;
        }
    }

    public static <T> T strToObj(String str, TypeReference<T> typeReference){
        if (str == null || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str,typeReference));
        }catch (Exception e){
            logger.warn("parse String to Object exception,String:{},TypeReference<T>{},exception:{}",str,typeReference.getType(),e);
            return null;
        }
    }
}
