package edu.iastate.coms309.cyschedulebackend.security.filter;

import edu.iastate.coms309.cyschedulebackend.persistence.model.UserCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends AbstractAuthenticationProcessingFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected JwtTokenFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    @Transactional
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            String token = ((HttpServletRequest) servletRequest).getHeader("jwttoken");
            TokenObject userLoginToken = userTokenService.load(token);
            UserDetails userDetails = accountService.loadUserByUsername(accountService.getUserEmail(userLoginToken.getUserID()));

            if (userTokenService.verify(userLoginToken, (UserCredential) userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException {
        return null;
    }
}
