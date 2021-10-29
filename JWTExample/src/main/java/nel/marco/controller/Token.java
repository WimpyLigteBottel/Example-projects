package nel.marco.controller;

public class Token {

  private String header;
  private String payload;
  private String auth;
  private final String token;

  public Token(String header, String payload, String auth) {
    this.header = header;
    this.payload = payload;
    this.auth = auth;
    this.token = header + "." + payload + "." + auth;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public String getAuth() {
    return auth;
  }

  public void setAuth(String auth) {
    this.auth = auth;
  }

  public String getToken() {
    return token;
  }
}
