package nel.marco.model;

import java.time.LocalDateTime;

//TODO: use these details for map so that i have more information
public class ServiceDetailsModel {

  private final String name;
  private final String url;
  private final LocalDateTime createdDate;

  public ServiceDetailsModel(String name, String url, LocalDateTime createdDate) {
    this.name = name;
    this.url = url;
    this.createdDate = createdDate;
  }

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }
}
