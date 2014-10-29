package com.example.better_together.storage;

/**
 * Created by ssdd on 10/23/14.
 */
public interface IKeyValueStorage {
    boolean writeString(String key, String value);
    String readString(String key);
}
