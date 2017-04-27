package com.living.solutions.reference.ifixit.ui.presenters;

import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.living.solutions.reference.ifixit.ui.contracts.CategorieContracts;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

  private Observer<Map<String, Map<String, Object>>> onCategorieObserver() {
    return new Observer<Map<String, Map<String, Object>>>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        mSubscribers.add(d);
      }

      @Override
      public void onNext(@NonNull Map<String, Map<String, Object>> mapTypeToken) {
        mView.updateUICategories(mapTypeToken);
      }

      @Override
      public void onError(@NonNull Throwable e) {
        e.printStackTrace();
      }

      @Override
      public void onComplete() {

      }
    };
  }
}
