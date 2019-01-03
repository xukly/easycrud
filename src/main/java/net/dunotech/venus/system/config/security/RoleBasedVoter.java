package net.dunotech.venus.system.config.security;

import net.dunotech.venus.system.entity.common.JwtUser;
import net.dunotech.venus.system.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class RoleBasedVoter implements AccessDecisionVoter<Object> {
    private String rolePrefix = "ROLE_";

    private static SysUserService sysUserService;


    public RoleBasedVoter() {
    }

    public String getRolePrefix() {
        return this.rolePrefix;
    }

    public void setRolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
    }

    public boolean supports(ConfigAttribute attribute) {
        return attribute.getAttribute() != null && attribute.getAttribute().startsWith(this.getRolePrefix());
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }

    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        if (authentication == null) {
            return -1;
        } else {
            Collection<? extends GrantedAuthority> authorities = this.extractAuthorities(authentication);
            Iterator var6 = attributes.iterator();

            while(true) {
                ConfigAttribute attribute;
                do {
                    if (!var6.hasNext()) {
                        return 0;
                    }

                    attribute = (ConfigAttribute) var6.next();
                    if(attribute.getAttribute() != null || !attribute.toString().equals("permitAll")){
                        break;
                    }
                } while (!this.supports(attribute));

                FilterInvocation fi = (FilterInvocation) object;
                String url = fi.getRequestUrl();
                if(url.contains("?")){
                    url = url.substring(0,url.indexOf("?"));
                }
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
                JwtUser jwtUser = (JwtUser) sysUserService.loadUserByUsername(token.getName());
                Iterator<SimpleGrantedAuthority> iterator = (Iterator<SimpleGrantedAuthority>) jwtUser.getAuthorities().iterator();
                while (iterator.hasNext()) {
                    SimpleGrantedAuthority sga = iterator.next();
                    String role = sga.getAuthority();
                    String[] urlArray = role.split(",");
                    for (String urlElement : urlArray) {
                        if (url.equals(urlElement)) {
                            return 1;
                        }
                    }
                }
                return -1;
            }
        }
    }

    Collection<? extends GrantedAuthority> extractAuthorities(Authentication authentication) {
        return authentication.getAuthorities();
    }

    public void setSysUserService(SysUserService sysUserService) {
        RoleBasedVoter.sysUserService = sysUserService;
    }
}
