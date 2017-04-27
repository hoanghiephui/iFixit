package com.living.solutions.reference.ifixit.ui.views.activitys;

import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.living.solutions.reference.ifixit.R;
import com.living.solutions.reference.ifixit.ui.contracts.CategorieContracts;

import java.util.Map;

import javax.inject.Inject;

public class MainActivity extends BaseActivity
  implements CategorieContracts.ICategorieView {

  @Inject
  CategorieContracts.ICategoriePresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    onSetupDrawerLayout();
    getApplicationComponent().inject(this);

    mPresenter.onBindView(this);
    mPresenter.getCategories();
  }

  private void onSetupDrawerLayout() {
    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
      this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    if (mDrawerLayout != null) {
      mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
    }
    actionBarDrawerToggle.syncState();
  }

  @Override
  protected int getActivityBaseViewID() {
    return R.layout.activity_main;
  }

  @Override
  protected View.OnClickListener onSnackbarClickListener() {
    return null;
  }

  @Override
  protected int getMenuLayoutID() {
    return 0;
  }

  @Override
  public void setProgressVisibility(int visibityState) {

  }

  @Override
  public boolean isAdded() {
    return isDestroyed();
  }

  @Override
  public void updateUICategories(Map<String, Map<String, Object>> categoris) {
    for (Map.Entry<String, Map<String, Object>> entry : categoris.entrySet()) {
      System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
    }
  }

  @Override
  protected void onDestroy() {
    mPresenter.onUnbindView();
    super.onDestroy();
  }
}
