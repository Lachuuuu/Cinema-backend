package com.Cinema.security.auth;

public class AuthenticationResponse {

  private String token;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public AuthenticationResponse() {
  }

  public AuthenticationResponse(String token) {
    this.token = token;
  }
}
