package edu.iastate.coms309.cyschedulebackend.security.filter;

import edu.iastate.coms309.cyschedulebackend.security.model.JwtAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends AbstractAuthenticationProcessingFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public JwtTokenFilter() {
        super(new AntPathRequestMatcher("/**", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException {
        String token = obtainToken(httpServletRequest);
        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(token);
        authRequest.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));

        logger.debug("Receiving new Request With JWT Authentication Token with endpoint : " + "[" + httpServletRequest.getRequestURI() + "]");
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainToken(HttpServletRequest httpServletRequest){
        return httpServletRequest.getHeader("jwtToken");
    }
}
