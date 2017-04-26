package com.living.solutions.reference.ifixit.server.services;

import com.google.gson.reflect.TypeToken;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by hoanghiep on 4/25/17.
 */

public interface IiFixitService {
  @GET("categories")
  Observable<TypeToken<Map<String, String>>> getCategories();
}
