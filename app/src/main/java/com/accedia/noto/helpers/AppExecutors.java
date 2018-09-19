package com.accedia.noto.helpers;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

public class AppExecutors {

    private ExecutorService diskIO;
    private ExecutorService networkIO;
    private MainThreadExecutor mainThread;

    public AppExecutors(ExecutorService diskIO, ExecutorService networkIO) {
        this.mainThread = new MainThreadExecutor();
        this.diskIO = diskIO;
        this.networkIO = networkIO;
    }


    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}
