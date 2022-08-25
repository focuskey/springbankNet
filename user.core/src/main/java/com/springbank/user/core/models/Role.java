package com.springbank.user.core.models;

import org.springframework.security.core.GrantedAuthority;

// to ensure that our roles complies to the granted authority roles that are part of the spring starter security package. we need to implement the granted authority interface.
public enum Role implements GrantedAuthority {
    READ_PRIVILEGE, WRITE_PRIVILEGE;


    @Override
    public String getAuthority() {

        return name();
    }
}
