//package io.shulie.tro.cloud.app.intercepter;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.LoggerFactory;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
///**
// * @author shulie
// * @package: io.shulie.tro.cloud.app.settings.intercepter
// * @Date 2019-07-26 15:15
// */
//public class MyHandlerInterceptor extends HandlerInterceptorAdapter {
//
//    /**
//     * 登录session key
//     */
//    public static final String SESSION_KEY = "user";
//    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
//    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MyHandlerInterceptor.class);
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//        throws Exception {
//
//        try {
//            Map params = new HashMap();
//            String userId = (String)params.get("000000");
//            threadLocal.set(userId);
//        } catch (Exception e) {
//            LOGGER.error("MyHandlerInterceptor.preHandle{}异常", e);
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//        ModelAndView modelAndView) throws Exception {
//        super.postHandle(request, response, handler, modelAndView);
//        System.out.println("===========HandlerInterceptor1 postHandle");
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//        throws Exception {
//        super.afterCompletion(request, response, handler, ex);
//        System.out.println("===========HandlerInterceptor1 afterCompletion");
//    }
//}
