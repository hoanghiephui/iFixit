package com.living.solutions.reference.ifixit.dragger.modules;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.living.solutions.reference.ifixit.BuildConfig;
import com.living.solutions.reference.ifixit.dragger.scopes.PerFragment;
import com.living.solutions.reference.ifixit.server.interceptor.ServerInterceptor;
import com.living.solutions.reference.ifixit.server.methods.IFixitMethod;
import com.living.solutions.reference.ifixit.server.services.IiFixitService;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hoanghiep on 4/25/17.
 */
@Module
public class IFixitNetModule {
  private String mBaseUrl;

  public IFixitNetModule(String mBaseUrl) {
    this.mBaseUrl = mBaseUrl;
  }

  @Provides
  Cache providesOkHttpCache(Application application) {
    int cacheSize = 10 * 1024 * 1024; // 10 MiB
    return new Cache(application.getCacheDir(), cacheSize);
  }

  @Provides
  OkHttpClient providesOkHttpClien(ServerInterceptor interceptor) {
    return new OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .addInterceptor(new LoggingInterceptor.Builder()
        .loggable(BuildConfig.DEBUG)
        .setLevel(Level.BASIC)
        .log(Platform.INFO)
        .request("Request")
        .response("Response")
        .addHeader("version", BuildConfig.VERSION_NAME)
        .build())
      .build();
  }

  @PerFragment
  @Provides
  ServerInterceptor provideServerInterceptor() {
    return new ServerInterceptor();
  }


  @Provides
  Retrofit provideRetrofit(OkHttpClient okHttpClient, RxJava2CallAdapterFactory adapterFactory) {
    Gson gson = new GsonBuilder().create();
    GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
    return new Retrofit.Builder()
      .addCallAdapterFactory(adapterFactory)
      .addConverterFactory(gsonConverterFactory)
      .baseUrl(mBaseUrl)
      .client(okHttpClient)
      .build();
  }

  @PerFragment
  @Provides
  IiFixitService provideIFixitService(Retrofit retrofit) {
    return retrofit.create(IiFixitService.class);
  }


  @Provides
  IFixitMethod provideIFixitMethod(IiFixitService fixitService) {
    return new IFixitMethod(fixitService);
  }


  @Provides
  RxJava2CallAdapterFactory provideRxJava2CallAdapterFactory() {
    return RxJava2CallAdapterFactory.create();
  }
}
