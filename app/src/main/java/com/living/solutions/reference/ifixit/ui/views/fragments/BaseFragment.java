package com.living.solutions.reference.ifixit.ui.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.living.solutions.reference.ifixit.IFixitAplication;
import com.living.solutions.reference.ifixit.R;
import com.living.solutions.reference.ifixit.dragger.components.IfixitComponent;
import com.living.solutions.reference.ifixit.ui.views.activitys.BaseActivity;
import com.living.solutions.reference.ifixit.utils.ServerUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hoanghiep on 4/28/17.
 */

public abstract class BaseFragment extends Fragment {
  protected MaterialDialog materialDialog;
  protected Snackbar mSnackbar;
  protected Unbinder mBinder;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getViewLayout(), container, false);
    injectViews(view);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void injectViews(View view) {
    mBinder = ButterKnife.bind(this, view);
  }

  protected abstract int getViewLayout();


  public void showDialogProgress() {
    materialDialog = new MaterialDialog.Builder(getActivity())
      .content(getString(R.string.progress_wait))
      .cancelable(false)
      .progress(true, 0)
      .show();
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  public void hideDialogProgress() {
    materialDialog.dismiss();
  }

  public boolean isInternetConnected() {
    return ServerUtils.isNetworkConnected(getActivity());
  }

  protected IfixitComponent getApplicationComponent() {
    return ((IFixitAplication) getActivity().getApplication()).getComponent();
  }

  protected void startFragment(int fragmentID, Fragment fragment) {
    FragmentManager fm = getChildFragmentManager();
    Fragment f = fm.findFragmentById(fragmentID);

    if (null == f) {
      fm.beginTransaction()
        .add(fragmentID, fragment)
        .commitAllowingStateLoss();
    } else {
      fm.beginTransaction()
        .replace(fragmentID, fragment)
        .commitAllowingStateLoss();
    }
  }

  public void onError(int msgID) {
    mSnackbar = Snackbar
      .make(getCoordinatorLayout(), msgID, Snackbar.LENGTH_LONG);

    mSnackbar.setActionTextColor(Color.RED);
    mSnackbar.show();
    mSnackbar.setAction(getString(R.string.try_again).toUpperCase(), onSnackbarClickListener());
  }

  public void onErrorNoConnection() {
    onError(R.string.error_no_internet);
  }

  public void onErrorInServer() {
    onError(R.string.error_no_server);
  }

  public void onErrorUnexpected() {
    onError(R.string.erro_unexpected);
  }

  protected abstract View.OnClickListener onSnackbarClickListener();

  public CoordinatorLayout getCoordinatorLayout() {
    return ((BaseActivity) getActivity()).getCoordinatorLayout();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public void onDetach() {
    super.onDetach();

    if (mBinder != null)
      mBinder.unbind();

    if (materialDialog != null)
      hideDialogProgress();

    if (mSnackbar != null)
      mSnackbar.dismiss();
  }
}
