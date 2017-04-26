package com.living.solutions.reference.ifixit.dragger.components;

import com.living.solutions.reference.ifixit.dragger.modules.ApplicationModule;
import com.living.solutions.reference.ifixit.dragger.modules.IFixitNetModule;
import com.living.solutions.reference.ifixit.dragger.modules.InterceptorModule;
import com.living.solutions.reference.ifixit.dragger.modules.PresenterModule;
import com.living.solutions.reference.ifixit.dragger.scopes.PerFragment;
import com.living.solutions.reference.ifixit.ui.views.activitys.MainActivity;

import dagger.Component;

/**
 * Created by hoanghiep on 4/26/17.
 */
@PerFragment
@Component(modules = {PresenterModule.class,
        ApplicationModule.class,
        IFixitNetModule.class,
        InterceptorModule.class})
public interface IfixitComponent {
  void inject(MainActivity mainActivity);
}
