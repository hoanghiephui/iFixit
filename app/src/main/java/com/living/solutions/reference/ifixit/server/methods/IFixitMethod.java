package com.living.solutions.reference.ifixit.server.methods;

import com.living.solutions.reference.ifixit.server.services.IiFixitService;

import javax.inject.Inject;

/**
 * Created by hoanghiep on 4/25/17.
 */

public class IFixitMethod {
  private IiFixitService fixitService;

  @Inject
  public IFixitMethod(IiFixitService fixitService) {
    this.fixitService = fixitService;
  }
}
