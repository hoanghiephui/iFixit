package com.living.solutions.reference.ifixit;

import android.app.Application;

import com.living.solutions.reference.ifixit.dragger.components.IfixitComponent;
import com.living.solutions.reference.ifixit.dragger.modules.ApplicationModule;
import com.living.solutions.reference.ifixit.dragger.modules.IFixitNetModule;

/**
 * Created by hoanghiep on 4/26/17.
 */

public class IFixitAplication extends Application {
  private IfixitComponent component;

  @Override
  public void onCreate() {
    super.onCreate();
    component = DaggerIfixitComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .iFixitNetModule(new IFixitNetModule("https://www.ifixit.com/api/2.0/"))
            .build();
  }

  public IfixitComponent getComponent() {
    return component;
  }
}
