package com.living.solutions.reference.ifixit.ui.presenters;

import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.living.solutions.reference.ifixit.ui.contracts.CategorieContracts;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by hoanghiep on 4/26/17.
 */

public class CategoriePresenter extends BasePresenter<CategorieContracts.ICategorieView, CategorieContracts.ICategorieInterceptor>
        implements CategorieContracts.ICategoriePresenter {

  @Inject
  public CategoriePresenter(CategorieContracts.ICategorieInterceptor mInterceptor, CompositeDisposable mSubscribers) {
    super(mInterceptor, mSubscribers);
  }

  @Override
  public void getCategories() {
    mView.setProgressVisibility(View.VISIBLE);
    if (mView.isInternetConnected()) {
      mInterceptor.getCategories()
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(onCategorieObserver());
    } else {
      onConnectionError();
    }
  }

  private Observer<TypeToken<Map<String, String>>> onCategorieObserver() {
    return new Observer<TypeToken<Map<String, String>>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        mSubscribers.add(d);
      }

      @Override
      public void onNext(@NonNull TypeToken<Map<String, String>> mapTypeToken) {
        mView.updateUICategories(mapTypeToken);
      }

      @Override
      public void onError(@NonNull Throwable e) {

      }

      @Override
      public void onComplete() {

      }
    };
  }
}
