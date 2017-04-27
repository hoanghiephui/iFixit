package com.living.solutions.reference.ifixit.dragger.modules;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class GeneralModule {
  @Provides
  CompositeDisposable providesCompositeDisposable() {
    return new CompositeDisposable();
  }
}
