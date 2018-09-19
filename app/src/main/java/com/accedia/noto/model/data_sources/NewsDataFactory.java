package com.accedia.noto.model.data_sources;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.accedia.noto.api.API;

public class NewsDataFactory extends DataSource.Factory {

    private final API api;
    private MutableLiveData<FeedDataSource> mutableLiveData;
    private FeedDataSource feedDataSource;

    public NewsDataFactory(API api) {
        this.mutableLiveData = new MutableLiveData<>();
        this.api = api;
    }

    @Override
    public DataSource create() {
        feedDataSource = new FeedDataSource(api);
        mutableLiveData.postValue(feedDataSource);
        return feedDataSource;
    }

    public MutableLiveData<FeedDataSource> getFeedDataSource() {
        return mutableLiveData;
    }
}
