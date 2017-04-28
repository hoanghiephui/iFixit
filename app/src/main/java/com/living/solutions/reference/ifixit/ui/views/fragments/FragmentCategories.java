package com.living.solutions.reference.ifixit.ui.views.fragments;

import android.view.View;

import com.living.solutions.reference.ifixit.R;

/**
 * Created by hoanghiep on 4/28/17.
 */

public class FragmentCategories extends BaseFragment {
  @Override
  protected int getViewLayout() {
    return R.layout.fragment_categories;
  }

  @Override
  protected View.OnClickListener onSnackbarClickListener() {
    return null;
  }
}
