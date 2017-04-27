package com.living.solutions.reference.ifixit.model;

import java.util.List;
import java.util.Map;

/**
 * Created by hoanghiep on 4/27/17.
 */

public class CategoriesModel {
  private String title;
  private List<Map<String, String>> info;

  public String getTitle() {
    return title;
  }

  public List<Map<String, String>> getInfo() {
    return info;
  }
}
