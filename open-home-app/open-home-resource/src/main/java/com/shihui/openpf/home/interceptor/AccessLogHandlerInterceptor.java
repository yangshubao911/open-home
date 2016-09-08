package com.shihui.openpf.home.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * http访问日志拦截器
 * 
 * @author felix
 */
public class AccessLogHandlerInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 记入http访问日志
     * 格式: HH:mm:ss {ip} {method} {url}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        StringBuilder param = new StringBuilder("?");
        Enumeration<?> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String key = (String) parameters.nextElement();
            String[] values = request.getParameterValues(key);
            for (String value : values) {
                param.append("&").append(key).append("=").append(value);
            }
        }
        this.logger.info("{} {} {}{}", ip, method, url, param);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}
}
