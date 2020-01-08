package com.demon4u.blog.config;

import com.demon4u.blog.util.ClientUtil;
import com.demon4u.blog.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class TimeInterceptor extends HandlerInterceptorAdapter {
	
	private ThreadLocal<Long> startTime = new ThreadLocal<>();
	
	private Logger timelog = LogUtil.getTimeLog();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		startTime.set(System.currentTimeMillis());
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
		Map<String, Object> params = new HashMap<>(request.getParameterMap());
		long cost = 0;
		try {
			cost = System.currentTimeMillis() - startTime.get();
		} catch (Exception e) {
		}
		timelog.info("{}#{}#{}#{}#{}#{}", request.getMethod(), cost, ClientUtil.getIpAddr(request),
				request.getRequestURI(), ClientUtil.getDeviceType(request), paramsToStr(params));
	}

	private static String paramsToStr(Map<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry  : map.entrySet()) {
			String key = entry.getKey();
			String value = "";
			String[] array = (String[]) entry.getValue();
			if (array != null && array.length > 0) {
				value = array[array.length - 1];
			}
			sb.append(key).append("=");
			sb.append(value);
			sb.append("&");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
