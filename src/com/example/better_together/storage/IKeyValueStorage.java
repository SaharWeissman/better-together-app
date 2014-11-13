package com.example.better_together.storage;

import java.util.Set;

/**
 * Created by ssdd on 10/23/14.
 */
public interface IKeyValueStorage {
    boolean writeString(String key, String value);
    boolean writeStringSet(String key, Set<String> values);
    String readString(String key);
    Set<String> readStringSet(String key);
}
