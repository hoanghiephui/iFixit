package com.living.solutions.reference.ifixit.ui.views.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.living.solutions.reference.ifixit.IFixitAplication;
import com.living.solutions.reference.ifixit.R;
import com.living.solutions.reference.ifixit.dragger.components.IfixitComponent;
import com.living.solutions.reference.ifixit.utils.ServerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hoanghiep on 4/27/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
  DrawerLayout.DrawerListener {

  protected Unbinder mBinder;
  protected Snackbar mSnackbar;
  @Nullable
  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @Nullable
  @BindView(R.id.contentPanel)
  CoordinatorLayout mCoordinatorLayout;
  @Nullable
  @BindView(R.id.drawer_layout)
  DrawerLayout mDrawerLayout;
  @Nullable
  @BindView(R.id.nav_view)
  NavigationView mNavigationView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getActivityBaseViewID());
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
    onSetupActionBar();
    onSetupNavigationDrawer();
  }

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    injectViews();
  }

  private void onSetupActionBar() {
    if (mToolbar != null) {
      setSupportActionBar(mToolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
  }

  private void onSetupNavigationDrawer() {
    if (mNavigationView != null) {
      mNavigationView.setNavigationItemSelectedListener(this);
      View view = mNavigationView.getHeaderView(0);

      if (mDrawerLayout != null) {
        mDrawerLayout.addDrawerListener(this);
      }
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (getMenuLayoutID() != 0)
      getMenuInflater().inflate(getMenuLayoutID(), menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return false;
    }
  }

  @Override
  protected void onDestroy() {
    if (mBinder != null) {
      mBinder.unbind();
    }
    super.onDestroy();
  }

  private void injectViews() {
    mBinder = ButterKnife.bind(this);
  }

  protected void startFragment(int fragmentID, Fragment fragment) {
    FragmentManager fm = getSupportFragmentManager();
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

  protected void startFragment(int fragmentID, Fragment fragment, String tag) {
    FragmentManager fm = getSupportFragmentManager();
    Fragment f = fm.findFragmentById(fragmentID);

    if (null == f) {
      fm.beginTransaction()
        .add(fragmentID, fragment, tag)
        .commitAllowingStateLoss();
    } else {
      fm.beginTransaction()
        .replace(fragmentID, fragment, tag)
        .commitAllowingStateLoss();
    }
  }

  protected void setActivityTitle(String title) {
    if (!TextUtils.isEmpty(title))
      if (mToolbar != null) {
        mToolbar.setTitle(title);
      }
  }

  protected void setActivitySubtitle(String subtitle) {
    if (!TextUtils.isEmpty(subtitle))
      if (mToolbar != null) {
        mToolbar.setSubtitle(subtitle);
      }
  }

  protected abstract int getActivityBaseViewID();

  protected abstract View.OnClickListener onSnackbarClickListener();

  protected abstract int getMenuLayoutID();

  public void onError(int msgID) {
    mSnackbar = Snackbar
      .make(getCoordinatorLayout(), getString(msgID), Snackbar.LENGTH_LONG);

    mSnackbar.setActionTextColor(Color.RED);
    mSnackbar.show();
    mSnackbar.setAction(getString(R.string.try_again).toUpperCase(), onSnackbarClickListener());

  }

  public CoordinatorLayout getCoordinatorLayout() {
    return mCoordinatorLayout;
  }

  protected IfixitComponent getApplicationComponent() {
    return ((IFixitAplication) this.getApplication()).getComponent();
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

  public boolean isInternetConnected() {
    return ServerUtils.isNetworkConnected(this);
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      // Handle the camera action
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onDrawerSlide(View drawerView, float slideOffset) {
    if (mCoordinatorLayout != null) {
      mCoordinatorLayout.setX(slideOffset * drawerView.getWidth());
    }
  }

  @Override
  public void onDrawerOpened(View drawerView) {

  }

  @Override
  public void onDrawerClosed(View drawerView) {

  }

  @Override
  public void onDrawerStateChanged(int newState) {

  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

}
