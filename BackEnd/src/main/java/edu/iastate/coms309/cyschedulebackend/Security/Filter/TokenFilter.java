package edu.iastate.coms309.cyschedulebackend.Security.Filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import edu.iastate.coms309.cyschedulebackend.Utils.JwtTokenUtil;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;


public class TokenFilter extends AccessControlFilter {

    @Autowired
    Gson gson;

    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response,
                                      Object mappedValue) throws Exception {
        String token = ((HttpServletRequest) request).getHeader("authorization");

        if (token.isEmpty())
            return false;

        // 获取无状态Token
        UserToken userToken = JwtTokenUtil.JwtFromString(token);

        try {
            // 委托给Realm进行登录
            getSubject(request, response).login(userToken);
        } catch (Exception e) {
            return false;
        }
        // 通过isPermitted 才能调用doGetAuthorizationInfo方法获取权限信息
        getSubject(request, response).isPermitted(((HttpServletRequest) request).getRequestURI());
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(gson.toJson(new Response().Unauthorized().send()));
        return false;
    }

}
