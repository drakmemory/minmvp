package com.minmvp.service;

import java.util.Map;

public interface SearchService {
    Map<String, Object> searchUsers(String keyword, Integer page, Integer size);
    void syncDataToEs();
}
