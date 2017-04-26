package com.living.solutions.reference.ifixit.dragger.modules;

import com.living.solutions.reference.ifixit.ui.contracts.CategorieContracts;
import com.living.solutions.reference.ifixit.ui.presenters.CategoriePresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by hoanghiep on 4/26/17.
 */
@Module
public class PresenterModule {
  @Provides
  public CategorieContracts.ICategoriePresenter providesCategoriePresenter(CategorieContracts.ICategorieInterceptor mInterceptor, CompositeDisposable mSubscribers) {
    return new CategoriePresenter(mInterceptor, mSubscribers);
  }
}
