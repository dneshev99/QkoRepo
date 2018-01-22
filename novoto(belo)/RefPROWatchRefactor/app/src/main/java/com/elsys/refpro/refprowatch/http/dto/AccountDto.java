package com.elsys.refpro.refprowatch.http.dto;

public class AccountDto {

  private String username;
  private String password;

  public AccountDto(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public AccountDto() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
