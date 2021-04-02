package com.example.incubation_planner.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SessionTimerInterceptor implements HandlerInterceptor {
    private HttpSession session;
    private static final long MAX_INACTIVE_SESSION_TIME = 250 * 1000;
    private Logger LOGGER = LoggerFactory.getLogger(SessionTimerInterceptor.class);

    public SessionTimerInterceptor(HttpSession session) {
        this.session = session;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.info("Pre handle method session timer - checking session start time");
        long startTime = System.currentTimeMillis();
        request.setAttribute("executionTime", startTime);

        if (UserInterceptor.isUserLogged()) {
            session = request.getSession();

            if (System.currentTimeMillis() - session.getLastAccessedTime()
                    > MAX_INACTIVE_SESSION_TIME) {
                LOGGER.warn("Logging out due to inactive session");
                SecurityContextHolder.clearContext();
                request.logout();
                response.addHeader("Inactive-Session-Header", "Value-Inactive");
                response.sendRedirect("/users/expired");
            }
        }
        return true;
    }

}
