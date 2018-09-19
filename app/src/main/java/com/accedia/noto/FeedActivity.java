package com.accedia.noto;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.accedia.noto.adapters.FeedListAdapter;
import com.accedia.noto.databinding.FeedActivityBinding;
import com.accedia.noto.viewModels.NewsViewModel;

public class FeedActivity extends AppCompatActivity {

    private FeedListAdapter adapter;
    private NewsViewModel feedViewModel;
    private FeedActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Step 1: Using DataBinding, we setup the layout for the activity
         *
         * */
        binding = DataBindingUtil.setContentView(this, R.layout.feed_activity);

        /*
         * Step 2: Initialize the ViewModel
         *
         * */
        feedViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        /*
         * Step 2: Setup the adapter class for the RecyclerView
         *
         * */
        binding.listFeed.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new FeedListAdapter(getApplicationContext());


        /*
         * Step 4: When a new page is available, we call submitList() method
         * of the PagedListAdapter class
         *
         * */
        feedViewModel.getArticleLiveData().observe(this, pagedList -> {
            adapter.submitList(pagedList);
        });

        feedViewModel.getNetworkState().observe(this, networkState -> {
            adapter.setNetworkState(networkState);
        });

        binding.listFeed.setAdapter(adapter);
    }
}