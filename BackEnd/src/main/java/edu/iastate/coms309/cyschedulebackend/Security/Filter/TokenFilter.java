package edu.iastate.coms309.cyschedulebackend.Security.Filter;

import edu.iastate.coms309.cyschedulebackend.Security.TokenAT;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class TokenFilter extends AccessControlFilter {


    @Autowired
    private UserTokenService userTokenService;

    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response,
                                      Object mappedValue) throws Exception {

        String userID = ((HttpServletRequest) request).getHeader("userID");
        String token = ((HttpServletRequest) request).getHeader("authorization");

        if (token.isEmpty() || userID.isEmpty() )
            return false;
        // 获取无状态Token
        TokenAT accessToken = new TokenAT(token,userID);

        try {
            // 委托给Realm进行登录
            getSubject(request, response).login(accessToken);
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
        return false;
    }

}
