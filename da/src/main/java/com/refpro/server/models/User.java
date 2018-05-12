package com.refpro.server.models;

import com.refpro.server.enums.UserRole;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User implements UserDetails{
    @Id
    String ID;

    private String username;
    private String password;
    private String watchFCMtoken;
    private String mobileFCMtoken;
    private UserRole role;

    public User(String username, String password, String watchFCMtoken, String mobileFCMtoken) {
        this.username = username;
        this.password = password;
        this.watchFCMtoken = watchFCMtoken;
        this.mobileFCMtoken = mobileFCMtoken;
    }

    public User() {
    }

    public String getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority(role.toString()));

        return list;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWatchFCMtoken() {
        return watchFCMtoken;
    }

    public void setWatchFCMtoken(String watchFCMtoken) {
        this.watchFCMtoken = watchFCMtoken;
    }

    public String getMobileFCMtoken() {
        return mobileFCMtoken;
    }

    public void setMobileFCMtoken(String mobileFCMtoken) {
        this.mobileFCMtoken = mobileFCMtoken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (ID != null ? !ID.equals(user.ID) : user.ID != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (watchFCMtoken != null ? !watchFCMtoken.equals(user.watchFCMtoken) : user.watchFCMtoken != null)
            return false;
        return mobileFCMtoken != null ? mobileFCMtoken.equals(user.mobileFCMtoken) : user.mobileFCMtoken == null;

    }

    @Override
    public int hashCode() {
        int result = ID != null ? ID.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (watchFCMtoken != null ? watchFCMtoken.hashCode() : 0);
        result = 31 * result + (mobileFCMtoken != null ? mobileFCMtoken.hashCode() : 0);
        return result;
    }
}
