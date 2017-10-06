package com.example.loso.friendtracker.Service;

/**
 * Created by Loso on 2017/10/5.
 */

public interface WalkigTimeCallBack<T> {
    public void onSuccess(T object);
    public void onFailure(Exception e);
}
