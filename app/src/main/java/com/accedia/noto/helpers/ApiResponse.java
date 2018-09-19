package com.accedia.noto.helpers;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;

public class ApiResponse<T> {
    public static <T> ApiErrorResponse<T> create(Throwable error) {
        return new ApiErrorResponse(error.getMessage() != null ? error.getMessage() : "unknown error");
    }

    public static <T> ApiResponse<T> create(Response<T> response) {
        if (response.isSuccessful()) {
            T body = response.body();
            if (body == null || response.code() == 204) {
                return new ApiEmptyResponse();
            } else {
                return new ApiSuccessResponse(
                        body,
                        response.headers().get("link")
                );
            }
        } else {
            String msg = null;
            try {
                msg = response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String errorMsg = TextUtils.isEmpty(msg) ?
                    response.message() :
                    msg;
            return new ApiErrorResponse(errorMsg != null ? errorMsg : "unknown error");
        }
    }
}

/**
 * separate class for HTTP 204 resposes so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> extends ApiResponse<T> {

}

class ApiSuccessResponse<T> extends ApiResponse<T> {
    private final T body;
    private final Map<String, String> links;

    private Pattern LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"");
    private Pattern PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)");
    private final String NEXT_LINK = "next";

    private Map<String, String> extractLinks(String input) {
        Map<String, String> links = new HashMap<>();
        Matcher matcher = LINK_PATTERN.matcher(input);

        while (matcher.find()) {
            int count = matcher.groupCount();
            if (count == 2) {
                links.put(matcher.group(2), matcher.group(1));
            }
        }
        return links;
    }

    public ApiSuccessResponse(T body,
                              String linkHeader) {
        this.body = body;
        this.links = extractLinks(linkHeader);
    }

    public Integer getNextPage() {
        Integer nextPage = null;
        String nextLink = links.get(NEXT_LINK);
        if (nextLink != null) {
            Matcher matcher = PAGE_PATTERN.matcher(nextLink);
            if (!matcher.find() || matcher.groupCount() != 1) {
                nextPage = null;
            } else {
                try {
                    nextPage = Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException nfe) {
                    nextPage = null;
                }
            }
        }
        return nextPage;
    }

    public T getBody() {
        return body;
    }
}

class ApiErrorResponse<T> extends ApiResponse<T> {
    private String errorMessage;

    public ApiErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}