package com.living.solutions.reference.ifixit.ui.contracts;

import com.living.solutions.reference.ifixit.ui.presenters.IPresenter;
import com.living.solutions.reference.ifixit.ui.views.IView;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by hoanghiep on 4/26/17.
 */

public class CategorieContracts {
  public interface ICategorieInterceptor {
    Observable<Map<String, Map<String, Object>>> getCategories();
  }

  public interface ICategoriePresenter extends IPresenter<ICategorieView> {
    void getCategories();
  }

  public interface ICategorieView extends IView {
    void updateUICategories(Map<String, Map<String, Object>> categoris);
  }
}
