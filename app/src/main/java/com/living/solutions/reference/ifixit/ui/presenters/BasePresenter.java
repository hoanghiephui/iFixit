package com.living.solutions.reference.ifixit.ui.presenters;

import android.view.View;

import com.living.solutions.reference.ifixit.ui.views.IView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by hoanghiep on 4/26/17.
 */

public abstract class BasePresenter <V extends IView, I> implements IPresenter<V> {
  protected V mView;
  protected I mInterceptor;
  protected CompositeDisposable mSubscribers;

  public BasePresenter(I mInterceptor, CompositeDisposable mSubscribers) {
    this.mInterceptor = mInterceptor;
    this.mSubscribers = mSubscribers;
  }

  public BasePresenter() {
  }

  @Override
  public void onBindView(V view) {
    mView = view;
  }

  @Override
  public void onUnbindView() {
    if (mView != null) {
      mView = null;
    }

    if (mSubscribers != null) {
      mSubscribers.clear();
      mSubscribers.dispose();
    }
  }

  protected void onConnectionError() {
    if (mView != null) {
      mView.onErrorNoConnection();
      mView.setProgressVisibility(View.GONE);
    }
  }
}
