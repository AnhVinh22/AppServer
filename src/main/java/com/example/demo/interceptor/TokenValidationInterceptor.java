package com.example.demo.interceptor;

import com.example.demo.annotation.RoleAdmin;
import com.example.demo.constant.ERole;
import com.example.demo.exception.UserTypeNotAllow;
import com.example.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class TokenValidationInterceptor implements HandlerInterceptor {

    @Autowired
    public JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        System.out.println("Token: " + token);
        String role = jwtService.parseTokenToRole(token);

        HandlerMethod hm = (HandlerMethod) handler;
        Method method = hm.getMethod();
        if (method.isAnnotationPresent(RoleAdmin.class)) {
            if (!role.equals(ERole.ADMIN.name())) {
                throw new UserTypeNotAllow("Unauthorized");
            }
        }
        return true;
    }
}
