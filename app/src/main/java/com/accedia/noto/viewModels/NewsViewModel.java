package com.accedia.noto.viewModels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.accedia.noto.api.API;
import com.accedia.noto.api.NetworkState;
import com.accedia.noto.dagger.Dagger;
import com.accedia.noto.helpers.AppExecutors;
import com.accedia.noto.model.Article;
import com.accedia.noto.model.data_sources.FeedDataSource;
import com.accedia.noto.model.data_sources.NewsDataFactory;

import javax.inject.Inject;

public class NewsViewModel extends ViewModel {

    @Inject
    API api;

    @Inject
    AppExecutors appExecutors;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Article>> articleLiveData;

    public NewsViewModel() {
        Dagger.getInstance().getAppComponent().inject(this);
        init();
    }

    private void init() {

        NewsDataFactory feedDataFactory = new NewsDataFactory(api);
        networkState = Transformations.switchMap(feedDataFactory.getFeedDataSource(),
                (Function<FeedDataSource, LiveData<NetworkState>>) FeedDataSource::getNetworkState);

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(20).build();

        articleLiveData = (new LivePagedListBuilder(feedDataFactory, pagedListConfig))
                .setFetchExecutor(appExecutors.getNetworkIO())
                .build();
    }

    /*
     * Getter method for the network state
     */
    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    /*
     * Getter method for the pageList
     */
    public LiveData<PagedList<Article>> getArticleLiveData() {
        return articleLiveData;
    }

}
