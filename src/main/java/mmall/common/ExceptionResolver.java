package mmall.common;

import mmall.exception.ParamException;
import mmall.exception.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Raymond on 2019/1/9.
 */
public class ExceptionResolver implements HandlerExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        String url = request.getRequestURL().toString();
        ModelAndView modelAndView;
        String defaultMsg = "system error";

        //数据请求(.json)和页面请求(.page)
        if (url.endsWith(".json")){
            if (e instanceof PermissionException || e instanceof ParamException){
                JSONData result = JSONData.fail(e.getMessage());
                modelAndView = new ModelAndView("jsonView",result.toMap());
            }else {
                logger.error("UnKnow json exception,url:"+url,e);
                JSONData result = JSONData.fail(defaultMsg);
                modelAndView = new ModelAndView("jsonView",result.toMap());
            }
        }else if (url.endsWith(".page")){
            logger.error("UnKnow page exception,url:"+url,e);
            JSONData result = JSONData.fail(defaultMsg);
            modelAndView = new ModelAndView("exception",result.toMap());
        }else {
            logger.error("UnKnow exception,url:"+url,e);
            JSONData result = JSONData.fail(defaultMsg);
            modelAndView = new ModelAndView("jsonView",result.toMap());
        }
        return modelAndView;
    }
}
