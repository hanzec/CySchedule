package edu.iastate.coms309.cyschedulebackend.Security;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.Utils.JwtTokenUtil;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AccountService accountService;

    @Autowired
    UserTokenService userTokenService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Set<String> userRole = new HashSet<>();
        Set<String> userPermission = new HashSet<>();
        String userID = (String) principalCollection.getPrimaryPrincipal();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        accountService.loadUserByUserID(userID).getUserRoles().forEach(role -> {
            userRole.add(role.toString());
            role.getRolePermissions().forEach(permission -> {
                userPermission.add(permission.toString());
            });
        });
        info.setRoles(userRole);
        info.setStringPermissions(userPermission);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof UserToken)) {
            return null;
        }
        UserToken jwtToken = (UserToken) authenticationToken;
        if (userTokenService.verify(jwtToken))
            return new SimpleAuthenticationInfo("jwt:" + jwtToken.getUserID(),jwtToken.getCredentials(),this.getName());
        else
            return null;
    }

    @Override
    public Class<?> getAuthenticationTokenClass() { return UserToken.class; }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UserToken;
    }

}
