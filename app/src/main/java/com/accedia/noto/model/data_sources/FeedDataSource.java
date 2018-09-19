package com.accedia.noto.model.data_sources;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.accedia.noto.api.API;
import com.accedia.noto.api.NetworkState;
import com.accedia.noto.helpers.BaseConstants;
import com.accedia.noto.model.Article;
import com.accedia.noto.model.Feed;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedDataSource extends PageKeyedDataSource<Long, Article> implements BaseConstants {

    private static final String TAG = FeedDataSource.class.getSimpleName();
    private final API api;


    private MutableLiveData networkState;
    private MutableLiveData initialLoading;

    public FeedDataSource(API api) {
        this.api = api;

        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Article> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        api.fetchFeed(QUERY, API_KEY, 1, params.requestedLoadSize)
                .enqueue(new Callback<Feed>() {
                    @Override
                    public void onResponse(Call<Feed> call, Response<Feed> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body().getArticles(), null, 2l);
                            initialLoading.postValue(NetworkState.LOADED);
                            networkState.postValue(NetworkState.LOADED);

                        } else {
                            initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Feed> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Article> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Article> callback) {
        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);

        networkState.postValue(NetworkState.LOADING);

        api.fetchFeed(QUERY, API_KEY, params.key, params.requestedLoadSize).enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                /*
                 * If the request is successful, then we will update the callback
                 * with the latest feed items and
                 * "params.key+1" -> set the next key for the next iteration.
                 */
                if (response.isSuccessful()) {
                    long nextKey = (params.key == response.body().getTotalResults()) ? null : params.key + 1;
                    callback.onResult(response.body().getArticles(), nextKey);
                    networkState.postValue(NetworkState.LOADED);

                } else
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });
    }
}
