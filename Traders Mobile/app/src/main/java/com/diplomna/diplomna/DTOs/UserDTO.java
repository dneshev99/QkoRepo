package com.diplomna.diplomna.DTOs;

public class UserDTO {

    private String username;
    private String password;
    private String email;
    private String phone;
    private AccountType accountType;

    public UserDTO(String username, String password, String email, String phone, AccountType accountType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.accountType = accountType;
    }

    public UserDTO() {}

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
