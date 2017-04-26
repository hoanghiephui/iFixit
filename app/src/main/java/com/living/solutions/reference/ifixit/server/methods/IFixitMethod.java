package com.living.solutions.reference.ifixit.server.methods;

import com.google.gson.reflect.TypeToken;
import com.living.solutions.reference.ifixit.server.services.IiFixitService;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by hoanghiep on 4/25/17.
 */

public class IFixitMethod {
  private IiFixitService fixitService;

  @Inject
  public IFixitMethod(IiFixitService fixitService) {
    this.fixitService = fixitService;
  }

  public Observable<TypeToken<Map<String, String>>> getCategories() {
    return fixitService.getCategories();
  }
}
