package com.accedia.noto.helpers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.Objects;

import javax.inject.Inject;

public abstract class NetworkBoundResource<Request, Result> {


    @Inject
    AppExecutors appExecutors;
    private MediatorLiveData<Result> result = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        result.setValue((Result) Resource.loading(null));
//        @Suppress("LeakingThis")
        final LiveData<Result> dbSource = loadFromDb();
        result.addSource(dbSource, new Observer<Result>() {
            @Override
            public void onChanged(@Nullable Result data) {
                result.removeSource(dbSource);
                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource);
                } else {
                    result.addSource(dbSource, new Observer<Result>() {
                        @Override
                        public void onChanged(@Nullable Result data) {
                            setValue(Resource.success(data));
                        }
                    });
                }
            }
        });
    }

    @MainThread
    private void setValue(Resource<Result> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue((Result) newValue);
        }
    }

    private void fetchFromNetwork(final LiveData<Result> dbSource) {
        final LiveData<ApiResponse<Request>> apiResponse = createCall();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource, new Observer<Result>() {
            @Override
            public void onChanged(@Nullable Result data) {
                setValue(Resource.loading(data));
            }
        });
        result.addSource(apiResponse, new Observer<ApiResponse<Request>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<Request> requestApiResponse) {
                result.removeSource(apiResponse);
                result.removeSource(dbSource);
                if (requestApiResponse instanceof ApiSuccessResponse) {
                    appExecutors.getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveCallResult(processResponse((ApiSuccessResponse<Request>) requestApiResponse));
                            appExecutors.getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    // we specially request a new live data,
                                    // otherwise we will get immediately last cached value,
                                    // which may not be updated with latest results received from network.
                                    result.addSource(loadFromDb(), new Observer<Result>() {
                                        @Override
                                        public void onChanged(@Nullable Result result) {
                                            setValue(Resource.success(result));
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else if (requestApiResponse instanceof ApiEmptyResponse) {
                    appExecutors.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            // reload from disk whatever we had
                            result.addSource(loadFromDb(), new Observer<Result>() {
                                @Override
                                public void onChanged(@Nullable Result result) {
                                    setValue(Resource.success(result));
                                }
                            });
                        }
                    });
                } else if (requestApiResponse instanceof ApiErrorResponse) {
                    onFetchFailed();
                    result.addSource(dbSource, new Observer<Result>() {
                        @Override
                        public void onChanged(@Nullable Result result) {
                            setValue(Resource.error(((ApiErrorResponse) requestApiResponse).getErrorMessage(), result));
                        }
                    });
                }

            }
        });

    }

    protected void onFetchFailed() {
    }

    @WorkerThread
    protected Request processResponse(ApiSuccessResponse<Request> response) {
        return response.getBody();
    }

    @WorkerThread
    protected abstract void saveCallResult(Request item);

    @MainThread
    protected abstract boolean shouldFetch(Result data);

    @MainThread
    protected abstract LiveData<Result> loadFromDb();

    @MainThread
    protected abstract LiveData<ApiResponse<Request>> createCall();

}
