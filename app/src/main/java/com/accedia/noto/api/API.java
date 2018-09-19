package com.accedia.noto.api;

import com.accedia.noto.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {


    /*
     * We would be using the below url:
     * https://newsapi.org/v2/everything?q=movies&apiKey=079dac74a5f94ebdb990ecf61c8854b7&pageSize=20&page=2
     * The url has four query parameters.
     * We would be changing the pageSize and the page
     */
    @GET("/v2/everything")
    Call<Feed> fetchFeed(@Query("q") String q,
                         @Query("apiKey") String apiKey,
                         @Query("page") long page,
                         @Query("pageSize") int pageSize);
}
