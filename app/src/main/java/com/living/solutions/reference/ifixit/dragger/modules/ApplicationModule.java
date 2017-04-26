package com.living.solutions.reference.ifixit.dragger.modules;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hoanghiep on 4/26/17.
 */
@Module
public class ApplicationModule {
  Application application;

  public ApplicationModule(Application application) {
    this.application = application;
  }

  @Provides
  public Application provideApplication() {
    return application;
  }
}
