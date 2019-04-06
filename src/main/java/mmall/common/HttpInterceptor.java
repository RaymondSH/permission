package mmall.common;

import mmall.util.JSONMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Raymond on 2019/1/10.
 */
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);
    private static final String START_TIME = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        Map parameterMap = request.getParameterMap();
        long start = System.currentTimeMillis();
        request.setAttribute(START_TIME,start);
        logger.info("request start:url:{},params:{}",url, JSONMapper.objToStr(parameterMap));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String url = request.getRequestURL().toString();
//        long end = System.currentTimeMillis();
//        long start = (Long)request.getAttribute(START_TIME);
//        logger.info("request finished:url:{},cost time:{}",url, end - start);
        removeThreadLocalInfo();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        long end = System.currentTimeMillis();
        long start = (Long)request.getAttribute(START_TIME);
        logger.info("request completed:url:{},cost time:{}",url, end - start);
        removeThreadLocalInfo();
    }


    public void removeThreadLocalInfo(){
        RequestHolder.remove();
    }
}
