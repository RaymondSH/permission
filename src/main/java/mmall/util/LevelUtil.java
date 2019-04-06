package mmall.util;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Raymond on 2019/1/10.
 */
public class LevelUtil {
    public final static String SEPARATOR=".";
    public final static String ROOT = "0";
    public static String calculateLevel(String parentLevel,Integer parentId){
        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }else {
            return StringUtils.join(parentLevel,SEPARATOR,parentId);
        }
    }
}
