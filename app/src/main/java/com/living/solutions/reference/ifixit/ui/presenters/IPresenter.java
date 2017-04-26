package com.living.solutions.reference.ifixit.ui.presenters;

import com.living.solutions.reference.ifixit.ui.views.IView;

/**
 * Created by hoanghiep on 4/26/17.
 */

public interface IPresenter<V extends IView> {
  void onBindView(V view);

  void onUnbindView();

}
