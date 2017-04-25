/*
 * Copyright (c) 2017 Clockbyte LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clockbyte.admobadapter.expressads;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clockbyte.admobadapter.AdViewHelper;
import com.clockbyte.admobadapter.AdmobAdapterCalculator;
import com.clockbyte.admobadapter.AdmobFetcherBase;
import com.clockbyte.admobadapter.R;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * Adapter that has common functionality for any adapters that need to show ads in-between
 * other data.
 */
public class AdmobExpressRecyclerAdapterWrapper
  extends RecyclerView.Adapter<RecyclerView.ViewHolder>
  implements AdmobFetcherBase.AdmobListener, AdmobFetcherExpress.ListenLoadAds {

  private static final int VIEW_TYPE_AD_EXPRESS = 0;
  private final static int DEFAULT_NO_OF_DATA_BETWEEN_ADS = 10;
  private final static int DEFAULT_LIMIT_OF_ADS = 3;
  private final static int DEFAULT_VIEWTYPE_SOURCE_MAX = 0;
  private final String TAG = AdmobExpressRecyclerAdapterWrapper.class.getCanonicalName();
  private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
  private boolean isLoadAdsError = false;
  private AdmobFetcherExpress adFetcher;
  private Context mContext;
  private AdmobAdapterCalculator AdapterCalculator = new AdmobAdapterCalculator();
  private int viewTypeBiggestSource;

  /**
   * Creates adapter wrapper with the test unit id and default adSize for all ad blocks
   * Use this constructor for test purposes. if you are going to release the live version
   * please use the appropriate constructor
   *
   * @param testDevicesId sets a devices ID to test ads interaction.
   *                      You could pass null but it's better to set ids for all your test devices
   *                      including emulators. for emulator just use the
   * @see #AdmobExpressRecyclerAdapterWrapper(Context, String)
   * @see {AdRequest.DEVICE_ID_EMULATOR}
   */
  public AdmobExpressRecyclerAdapterWrapper(Context context, String[] testDevicesId) {
    init(context, null, testDevicesId);
  }
  /**
   * Creates adapter wrapper with the same unit id and default adSize for all ad blocks
   *
   * @param admobReleaseUnitId sets a release unit ID for admob banners.
   *                           If you are testing the ads please use constructor which expects test device ids
   * @see #AdmobExpressRecyclerAdapterWrapper(Context, String[])
   * ID should be active, please check it in your Admob's account.
   * Be careful: don't set it or set to null if you still haven't deployed a Release.
   * Otherwise your Admob account could be banned
   */
  public AdmobExpressRecyclerAdapterWrapper(Context context, String admobReleaseUnitId) {
    this(context, admobReleaseUnitId, null, null);
  }
  /**
   * Creates adapter wrapper with multiple unit ids
   *
   * @param adPresets sets a collection of ad presets ( object which contains unit ID and AdSize for banner).
   *                  It works like cycling FIFO (first in = first out, cycling from end to start).
   *                  Each ad block will get one from the queue.
   *                  If the desired count of ad blocks is greater than this collection size
   *                  then it will go again to the first item and iterate as much as it required.
   *                  ID should be active, please check it in your Admob's account.
   *                  Be careful: don't pass release unit id without setting the testDevicesId if you still haven't deployed a Release.
   *                  Otherwise your Admob account could be banned
   *                  If you are testing the ads please use constructor which expects test device ids
   * @see #AdmobExpressRecyclerAdapterWrapper(Context, String[])
   * ID should be active, please check it in your Admob's account.
   * Be careful: don't set it or set to null if you still haven't deployed a Release.
   * Otherwise your Admob account could be banned
   */
  public AdmobExpressRecyclerAdapterWrapper(Context context, Collection<ExpressAdPreset> adPresets) {
    this(context, adPresets, null);
  }

  /**
   * Creates adapter wrapper with the same unit id and default adSize for all ad blocks, also registers your test devices
   *
   * @param admobReleaseUnitId sets a release unit ID for admob banners.
   *                           ID should be active, please check it in your Admob's account.
   *                           Be careful: don't set it or set to null if you still haven't deployed a Release.
   *                           Otherwise your Admob account could be banned
   * @param testDevicesId      sets a devices ID to test ads interaction.
   *                           You could pass null but it's better to set ids for all your test devices
   *                           including emulators. for emulator just use the
   * @see {AdRequest.DEVICE_ID_EMULATOR}
   */
  public AdmobExpressRecyclerAdapterWrapper(Context context, String admobReleaseUnitId, String[] testDevicesId) {
    this(context, admobReleaseUnitId, testDevicesId, null);
  }

  /**
   * Creates adapter wrapper with the same unit id and adSize for all ad blocks, also registers your test devices
   *
   * @param admobReleaseUnitId sets a release unit ID for admob banners.
   *                           If you are testing the ads please use constructor for tests
   * @param testDevicesId      sets a devices ID to test ads interaction.
   *                           You could pass null but it's better to set ids for all your test devices
   *                           including emulators. for emulator just use the
   * @param adSize             sets ad size. By default it equals to AdSize(AdSize.FULL_WIDTH, 150);
   * @see #AdmobExpressRecyclerAdapterWrapper(Context, String[])
   * ID should be active, please check it in your Admob's account.
   * Be careful: don't set it or set to null if you still haven't deployed a Release.
   * Otherwise your Admob account could be banned
   * @see {AdRequest.DEVICE_ID_EMULATOR}
   */
  public AdmobExpressRecyclerAdapterWrapper(Context context, String admobReleaseUnitId, String[] testDevicesId, AdSize adSize) {
    Collection<ExpressAdPreset> releaseUnitIds = Collections.singletonList(
      new ExpressAdPreset(admobReleaseUnitId, adSize));
    init(context, releaseUnitIds, testDevicesId);
  }

  /**
   * Creates adapter wrapper with multiple unit ids, also registers your test devices
   *
   * @param adPresets     sets a collection of ad presets ( object which contains unit ID and AdSize for banner).
   *                      It works like cycling FIFO (first in = first out, cycling from end to start).
   *                      Each ad block will get one from the queue.
   *                      If the desired count of ad blocks is greater than this collection size
   *                      then it will go again to the first item and iterate as much as it required.
   *                      ID should be active, please check it in your Admob's account.
   *                      Be careful: don't pass release unit id without setting the testDevicesId if you still haven't deployed a Release.
   *                      Otherwise your Admob account could be banned
   * @param testDevicesId sets a devices ID to test ads interaction.
   *                      You could pass null but it's better to set ids for all your test devices
   *                      including emulators. for emulator just use the
   * @see {AdRequest.DEVICE_ID_EMULATOR}
   */
  public AdmobExpressRecyclerAdapterWrapper(Context context, Collection<ExpressAdPreset> adPresets, String[] testDevicesId) {
    init(context, adPresets, testDevicesId);
  }

  /**
   * Creates adapter wrapper with default unit id and the same adSize for all ad blocks
   * Use this constructor for test purposes. If you are going to release the live version
   * please use the appropriate constructor
   *
   * @param testDevicesId sets a devices ID to test ads interaction.
   *                      You could pass null but it's better to set ids for all your test devices
   *                      including emulators. for emulator just use the
   * @param adSize        sets ad size. By default it equals to AdSize(AdSize.FULL_WIDTH, 150);
   * @see #AdmobExpressRecyclerAdapterWrapper(Context, String, AdSize)
   * @see {AdRequest.DEVICE_ID_EMULATOR}
   */
  public AdmobExpressRecyclerAdapterWrapper(Context context, String[] testDevicesId, AdSize adSize) {
    Collection<ExpressAdPreset> releaseUnitIds = Collections.singletonList(
      new ExpressAdPreset(null, adSize));
    init(context, null, testDevicesId);
  }

  /**
   * @param admobReleaseUnitId sets a release unit ID for admob banners.
   *                           If you are testing the ads please use constructor for tests
   * @param adSize             sets ad size. By default it equals to AdSize(AdSize.FULL_WIDTH, 150);
   * @see #AdmobExpressRecyclerAdapterWrapper(Context, String[], AdSize)
   * ID should be active, please check it in your Admob's account.
   * Be careful: don't set it or set to null if you still haven't deployed a Release.
   * Otherwise your Admob account could be banned
   */
  public AdmobExpressRecyclerAdapterWrapper(Context context, String admobReleaseUnitId, AdSize adSize) {
    this(context, admobReleaseUnitId, null, adSize);
  }

  public RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter() {
    return mAdapter;
  }

  public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
    mAdapter = adapter;
    mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override
      public void onChanged() {
        notifyDataSetChanged();
      }

      @Override
      public void onItemRangeChanged(int positionStart, int itemCount) {
        notifyItemRangeChanged(positionStart, itemCount);
      }

      @Override
      public void onItemRangeInserted(int positionStart, int itemCount) {
        notifyItemRangeInserted(positionStart, itemCount);
      }

      @Override
      public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        for (int i = 0; i < itemCount; itemCount++)
          notifyItemMoved(fromPosition + i, toPosition + i);
      }

      @Override
      public void onItemRangeRemoved(int positionStart, int itemCount) {
        notifyItemRangeRemoved(positionStart, itemCount);
      }

    });

    notifyDataSetChanged();
  }

  /*
  * Gets an object which incapsulates transformation of the source and ad blocks indices
  */
  public AdmobAdapterCalculator getAdapterCalculator() {
    return AdapterCalculator;
  }

  /*
* Injects an object which incapsulates transformation of the source and ad blocks indices. You could override calculations
* by inheritance of AdmobAdapterCalculator class
*/
  public void setAdapterCalculator(AdmobAdapterCalculator adapterCalculatordmob) {
    AdapterCalculator = adapterCalculatordmob;
  }

  /**
   * Gets the number of presets for ads that have been predefined by user (objects which contain adsize, unitIds etc).
   *
   * @return the number of ads that have been fetched
   */
  public int getAdPresetsCount() {
    return adFetcher != null ? this.adFetcher.getAdPresetsCount() : 0;
  }

  /**
   * Gets the number of ads that have been fetched so far.
   *
   * @return the number of ads that have been fetched
   */
  public int getFetchedAdsCount() {
    return adFetcher.getFetchedAdsCount();
  }

  /**
   * Gets the number of ads have been fetched so far + currently fetching ads
   *
   * @return the number of already fetched ads + currently fetching ads
   */
  public int getFetchingAdsCount() {
    return adFetcher.getFetchingAdsCount();
  }

  public int getViewTypeAdExpress() {
    return getViewTypeBiggestSource() + VIEW_TYPE_AD_EXPRESS + 1;
  }

  /*
  * Gets the number of your data items between ad blocks, by default it equals to 10.
  * You should set it according to the Admob's policies and rules which says not to
  * display more than one ad block at the visible part of the screen
  * so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
  */
  public int getNoOfDataBetweenAds() {
    return AdapterCalculator.getNoOfDataBetweenAds();
  }

  /*
  * Sets the number of your data items between ad blocks, by default it equals to 10.
  * You should set it according to the Admob's policies and rules which says not to
  * display more than one ad block at the visible part of the screen
  * so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
  */
  public void setNoOfDataBetweenAds(int mNoOfDataBetweenAds) {
    AdapterCalculator.setNoOfDataBetweenAds(mNoOfDataBetweenAds);
  }

  public int getFirstAdIndex() {
    return AdapterCalculator.getFirstAdIndex();
  }

  /*
  * Sets the first ad block index (zero-based) in the adapter, by default it equals to 0
  */
  public void setFirstAdIndex(int firstAdIndex) {
    AdapterCalculator.setFirstAdIndex(firstAdIndex);
  }

  /*
  * Gets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
  */
  public int getLimitOfAds() {
    return AdapterCalculator.getLimitOfAds();
  }

  /*
  * Sets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
  */
  public void setLimitOfAds(int mLimitOfAds) {
    AdapterCalculator.setLimitOfAds(mLimitOfAds);
  }

  /*
 * Gets the biggest value among all the view types in the underlying source adapter, by default it equals to 0.
 */
  public int getViewTypeBiggestSource() {
    return viewTypeBiggestSource;
  }

  /*
  * Sets the biggest value among all the view types in the underlying source adapter, by default it equals to 0.
  */
  public void setViewTypeBiggestSource(int viewTypeBiggestSource) {
    this.viewTypeBiggestSource = viewTypeBiggestSource;
  }

  private void init(Context context, Collection<ExpressAdPreset> expressAdPresets, String[] testDevicesId) {
    setViewTypeBiggestSource(DEFAULT_VIEWTYPE_SOURCE_MAX);
    setNoOfDataBetweenAds(DEFAULT_NO_OF_DATA_BETWEEN_ADS);
    setLimitOfAds(DEFAULT_LIMIT_OF_ADS);
    mContext = context;
    adFetcher = new AdmobFetcherExpress(mContext, this);
    if (testDevicesId != null)
      for (String testId : testDevicesId)
        adFetcher.addTestDeviceId(testId);
    adFetcher.addListener(this);
    adFetcher.setAdPresets(expressAdPresets);

    prefetchAds(AdmobFetcherExpress.PREFETCHED_ADS_SIZE);
  }

  /**
   * Creates N instances {@link NativeExpressAdView} from the next N taken instances {@link ExpressAdPreset}
   * Will start async prefetch of ad blocks to use its further
   *
   * @return last created NativeExpressAdView
   */
  private NativeExpressAdView prefetchAds(int cntToPrefetch) {
    NativeExpressAdView last = null;
    for (int i = 0; i < cntToPrefetch; i++) {
      final NativeExpressAdView item = AdViewHelper.getExpressAdView(mContext, adFetcher.takeNextAdPreset());
      adFetcher.setupAd(item);
      //50 ms throttling to prevent a high-load of server
      new Handler(mContext.getMainLooper()).postDelayed(new Runnable() {
        @Override
        public void run() {
          adFetcher.fetchAd(item);
        }
      }, 50 * i);
      last = item;
    }
    return last;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder == null)
      return;
    if (viewHolder.getItemViewType() == getViewTypeAdExpress()) {
      NativeHolder nativeExpressHolder =
        (NativeHolder) viewHolder;
      ViewGroup wrapper = nativeExpressHolder.getAdViewWrapper();
      int adPos = AdapterCalculator.getAdIndex(position);
      NativeExpressAdView adView = adFetcher.getAdForIndex(adPos);
      if (adView == null)
        adView = prefetchAds(1);
      // The NativeExpressHolder recycled by the RecyclerView may be a different
      // instance than the one used previously for this position. Clear the
      // NativeExpressHolder of any subviews in case it has a different
      // AdView associated with it, and make sure the AdView for this position doesn't
      // already have a parent of a different recycled NativeExpressHolder.
      for (int i = 0; i < wrapper.getChildCount(); i++) {
        View v = wrapper.getChildAt(i);
        if (v instanceof NativeExpressAdView) {
          wrapper.removeViewAt(i);
          break;
        }
      }
      if (adView.getParent() != null)
        ((ViewGroup) adView.getParent()).removeView(adView);
      // Add the Native Express ad to the native express ad view.
      wrapper.addView(adView);
    } else {
      int origPos = AdapterCalculator.getOriginalContentPosition(position,
        adFetcher.getFetchingAdsCount(), mAdapter.getItemCount());
      mAdapter.onBindViewHolder(viewHolder, origPos);
    }
  }

  @Override
  public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == getViewTypeAdExpress()) {
      ViewGroup wrapper = getAdViewWrapper(parent);
      return new NativeHolder(wrapper);
    } else {
      return mAdapter.onCreateViewHolder(parent, viewType);
    }
  }

  /**
   * This method can be overriden to wrap the created ad view with a custom {@link ViewGroup}.<br/>
   * For example if you need to wrap the ad with your custom CardView
   *
   * @return The wrapper {@link ViewGroup} for ad, by default {@link NativeExpressAdView} ad would be wrapped with a CardView which is returned by this method
   */
  @NonNull
  @NotNull
  protected ViewGroup getAdViewWrapper(ViewGroup parent) {
    return (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.native_express_ad_container,
      parent, false);
  }

  /**
   * <p>Gets the count of all data, including interspersed ads.</p>
   * <p/>
   * <p>If data size is 10 and an ad is to be showed after every 5 items starting at the index 0, this method
   * will return 12.</p>
   *
   * @return the total number of items this adapter can show, including ads.
   * @see AdmobExpressRecyclerAdapterWrapper#setNoOfDataBetweenAds(int)
   * @see AdmobExpressRecyclerAdapterWrapper#getNoOfDataBetweenAds()
   */
  @Override
  public int getItemCount() {

    if (mAdapter != null) {
            /*No of currently fetched ads, as long as it isn't more than no of max ads that can
            fit dataset.*/
      int noOfAds = AdapterCalculator.getAdsCountToPublish(adFetcher.getFetchingAdsCount(), mAdapter.getItemCount());
      return mAdapter.getItemCount() > 0 ? mAdapter.getItemCount() + noOfAds : 0;
    } else {
      return 0;
    }
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getItemViewType(int position) {
    checkNeedFetchAd(position);
    if (AdapterCalculator.canShowAdAtPosition(position, adFetcher.getFetchingAdsCount())) {
      return getViewTypeAdExpress();
    } else {
      int origPos = AdapterCalculator.getOriginalContentPosition(position,
        adFetcher.getFetchingAdsCount(), mAdapter.getItemCount());
      return mAdapter.getItemViewType(origPos);
    }
  }

  private void checkNeedFetchAd(int position) {
    if (AdapterCalculator.hasToFetchAd(position, adFetcher.getFetchingAdsCount()))
      prefetchAds(1);
  }

  /**
   * Clears all currently displaying ads and reinits the list
   */
  public void reinitialize() {
    adFetcher.destroyAllAds();
    prefetchAds(AdmobFetcherExpress.PREFETCHED_ADS_SIZE);
    notifyDataSetChanged();
  }

  /**
   * Free all resources, weak-refs
   */
  public void release() {
    adFetcher.release();
  }

  @Override
  public void onAdChanged(int adIdx) {
    //raise ad's neighbour item changed.
    // cheap, quick and dirty solution to avoid ad's redraw and flickering.
    int pos = getAdapterCalculator().translateAdToWrapperPosition(adIdx);
    notifyItemChanged(pos == 0 ? 1 : Math.max(0, pos - 1));
  }

  /**
   * Raised when the number of ads have changed. Adapters that implement this class
   * should notify their data views that the dataset has changed.
   */
  @Override
  public void onAdChanged() {
    notifyDataSetChanged();
  }

  @Override
  public void loadError(boolean isError) {
    if (isError) {
      isLoadAdsError = true;
    } else {
      isLoadAdsError = false;
    }
  }

  public boolean isLoadAdsError() {
    return isLoadAdsError;
  }
}
