package com.living.solutions.reference.ifixit.dragger.modules;

import com.living.solutions.reference.ifixit.server.methods.IFixitMethod;
import com.living.solutions.reference.ifixit.ui.contracts.CategorieContracts;
import com.living.solutions.reference.ifixit.ui.interceptor.CategoriesInterceptor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hoanghiep on 4/26/17.
 */

@Module
public class InterceptorModule {
  @Provides
  public CategorieContracts.ICategorieInterceptor provideCategorieInterceptor(IFixitMethod method) {
    return new CategoriesInterceptor(method);
  }
}
