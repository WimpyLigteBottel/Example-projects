package nel.marco.db.mongo.filter;

import lombok.Data;

@Data
public class CustomerFilter {

  private String id;
  private String name;
  private Integer age;
  private String sortBy;
  private String activeType;

  private int maxResults;
  private boolean ascending;

  public void setAscending(boolean ascending) {
    this.ascending = ascending;
  }

  public boolean getAscending() {
    return ascending;
  }
}
