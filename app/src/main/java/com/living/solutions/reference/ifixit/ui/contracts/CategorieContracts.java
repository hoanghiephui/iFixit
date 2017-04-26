package com.living.solutions.reference.ifixit.ui.contracts;

import com.google.gson.reflect.TypeToken;
import com.living.solutions.reference.ifixit.ui.presenters.IPresenter;
import com.living.solutions.reference.ifixit.ui.views.IView;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by hoanghiep on 4/26/17.
 */

public class CategorieContracts {
  public interface ICategorieInterceptor {
    Observable<TypeToken<Map<String, String>>> getCategories();
  }

  public interface ICategoriePresenter extends IPresenter<ICategorieView> {
    void getCategories();
  }

  public interface ICategorieView extends IView {
    void updateUICategories(TypeToken<Map<String, String>> categoris);
  }
}
