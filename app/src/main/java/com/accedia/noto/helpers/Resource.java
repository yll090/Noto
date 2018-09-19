package com.accedia.noto.helpers;

import static com.accedia.noto.helpers.Status.ERROR;
import static com.accedia.noto.helpers.Status.LOADING;
import static com.accedia.noto.helpers.Status.SUCCESS;

public class Resource<T> {
    private T data;
    private Status status;
    private String message;

    private Resource(Status status, T data, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String message, T data) {
        return new Resource(ERROR, data, message);
    }

    public static <T> Resource<T> loading(T data) {
        return new Resource(LOADING, data, null);
    }

}