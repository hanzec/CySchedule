package edu.iastate.coms309.cyschedulebackend.security.filter;

import com.google.gson.Gson;
import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserCredential;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserLoginToken;
import edu.iastate.coms309.cyschedulebackend.security.model.TokenObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    Gson gson;

    @Autowired
    AccountService accountService;

    @Autowired
    UserTokenService userTokenService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            TokenObject userLoginToken = userTokenService.load(httpServletRequest.getHeader("JwtToken"));
            UserDetails userDetails = accountService.loadUserByUsername(accountService.getUserEmail(userLoginToken.getUserID()));

            if (userTokenService.verify(userLoginToken, (UserCredential) userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            Response response = new Response();
            SecurityContextHolder.clearContext();
            servletResponse.getWriter().write(
                    gson.toJson(
                            response
                                    .BadRequested("Token is Not Correct")
                                    .send(((HttpServletRequest) servletRequest).getRequestURI())
                                    .addResponse("item", ex.getMessage())));
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
