package com.example.ca1.callback;

import java.util.List;

public interface Callback {
    // For successful operations
    void onSuccess(String message, Object... params);
    // For errors
    void onError(String errorMessage);
    // For retrieving objects
    <T> void onObjectsRetrieved(List<T> products);
}
