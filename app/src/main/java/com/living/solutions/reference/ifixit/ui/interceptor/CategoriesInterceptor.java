package com.living.solutions.reference.ifixit.ui.interceptor;

import com.google.gson.reflect.TypeToken;
import com.living.solutions.reference.ifixit.server.methods.IFixitMethod;
import com.living.solutions.reference.ifixit.ui.contracts.CategorieContracts;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by hoanghiep on 4/26/17.
 */

public class CategoriesInterceptor implements CategorieContracts.ICategorieInterceptor{
  private IFixitMethod iFixitMethod;

  @Inject
  public CategoriesInterceptor(IFixitMethod iFixitMethod) {
    this.iFixitMethod = iFixitMethod;
  }

  @Override
  public Observable<TypeToken<Map<String, String>>> getCategories() {
    return iFixitMethod.getCategories();
  }
}
