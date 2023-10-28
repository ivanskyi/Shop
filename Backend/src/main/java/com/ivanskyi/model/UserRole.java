package com.ivanskyi.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
  ROLE_MANAGER, ROLE_CLIENT;

  public String getAuthority() {
    return name();
  }

}
