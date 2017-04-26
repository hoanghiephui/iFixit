package com.living.solutions.reference.ifixit.ui.views;

/**
 * Created by hoanghiep on 4/26/17.
 */

public interface IView {
  void setProgressVisibility(int visibityState);

  boolean isInternetConnected();

  void onError(int msgID);

  boolean isAdded();

  void onErrorNoConnection();

  void onErrorInServer();

  void onErrorUnexpected();
}
