package com.issueflow.security;

import com.issueflow.modal.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private static User currentUser;

    public static String getCurrentUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getPrincipal() == null){
            throw new BadCredentialsException("User is not logged in");
        }


        return authentication.getName();
        /*
        if(authentication.getPrincipal() instanceof UserDetails userDetails){
            return userDetails.getUsername();
        }
        throw new BadCredentialsException("User is not logged in");

         */
    }

}
