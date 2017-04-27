package com.living.solutions.reference.ifixit.ui.interceptor;

import com.living.solutions.reference.ifixit.server.methods.IFixitMethod;
import com.living.solutions.reference.ifixit.ui.contracts.CategorieContracts;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by hoanghiep on 4/26/17.
 */

public class CategoriesInterceptor implements CategorieContracts.ICategorieInterceptor {
  private IFixitMethod iFixitMethod;

  @Inject
  public CategoriesInterceptor(IFixitMethod iFixitMethod) {
    this.iFixitMethod = iFixitMethod;
  }

  @Override
  public Observable<Map<String, Map<String, Object>>> getCategories() {
    return iFixitMethod.getCategories();
  }
}
