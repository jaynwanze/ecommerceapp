package com.example.ca1.callback;

import java.util.List;

public interface Callback {
    void onSuccess(String message); // For successful operations
    void onError(String errorMessage); // For errors
    <T> void onObjectsRetrieved(List<T> products); // For retrieving objects
}
