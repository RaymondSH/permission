package mmall.common;

import mmall.model.User;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Raymond on 2019/1/12.
 */
public class RequestHolder {

    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void setUserHolder(User user){
        userHolder.set(user);
    }

    public static void setRequestHolder(HttpServletRequest request){
        requestHolder.set(request);
    }

    public static User getUser(){
        return userHolder.get();
    }

    public static HttpServletRequest getRequest(){
        return requestHolder.get();
    }

    public static void remove(){
        userHolder.remove();
        requestHolder.remove();
    }
}
