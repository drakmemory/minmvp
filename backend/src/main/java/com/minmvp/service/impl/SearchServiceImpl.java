package com.minmvp.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minmvp.config.ElasticsearchConfig;
import com.minmvp.entity.User;
import com.minmvp.mapper.UserMapper;
import com.minmvp.service.SearchService;
import com.minmvp.vo.SearchResultVO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired(required = false)
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Map<String, Object> searchUsers(String keyword, Integer page, Integer size) {
        if (elasticsearchTemplate != null && keyword != null && !keyword.trim().isEmpty()) {
            try {
                return searchFromEs(keyword, page, size);
            } catch (Exception e) {
                log.warn("Elasticsearch search failed, falling back to MySQL: {}", e.getMessage());
            }
        }
        return searchFromDatabase(keyword, page, size);
    }

    @Override
    public void syncDataToEs() {
        if (elasticsearchTemplate == null) {
            log.warn("ElasticsearchTemplate is not available, skipping sync");
            return;
        }
        try {
            List<User> users = userMapper.selectList(null);
            List<IndexQuery> queries = users.stream()
                    .map(user -> new IndexQueryBuilder()
                            .withId(String.valueOf(user.getId()))
                            .withIndexName(ElasticsearchConfig.USER_INDEX)
                            .withType("user")
                            .withSource(JSON.toJSONString(user))
                            .build())
                    .collect(Collectors.toList());
            elasticsearchTemplate.bulkIndex(queries);
            log.info("Synced {} users to Elasticsearch", users.size());
        } catch (Exception e) {
            log.error("Failed to sync data to Elasticsearch: {}", e.getMessage());
        }
    }

    private Map<String, Object> searchFromEs(String keyword, Integer page, Integer size) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "name", "phone", "email"))
                .withPageable(PageRequest.of(page - 1, size))
                .build();

        return elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Map<String, Object>>() {
            @Override
            public Map<String, Object> extract(SearchResponse response) {
                List<SearchResultVO> list = new ArrayList<>();
                SearchHit[] hits = response.getHits().getHits();
                for (SearchHit hit : hits) {
                    Map<String, Object> source = hit.getSourceAsMap();
                    SearchResultVO vo = new SearchResultVO();
                    vo.setId(toLong(source.get("id")));
                    vo.setName(toStr(source.get("name")));
                    vo.setPhone(toStr(source.get("phone")));
                    vo.setEmail(toStr(source.get("email")));
                    vo.setRole(toStr(source.get("role")));
                    vo.setScore((double) hit.getScore());
                    list.add(vo);
                }
                Map<String, Object> result = new HashMap<>();
                result.put("total", response.getHits().getTotalHits());
                result.put("list", list);
                return result;
            }
        });
    }

    private Map<String, Object> searchFromDatabase(String keyword, Integer page, Integer size) {
        Page<User> userPage = new Page<>(page, size);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like("name", keyword)
                    .or().like("phone", keyword)
                    .or().like("email", keyword));
        }
        userMapper.selectPage(userPage, wrapper);

        List<SearchResultVO> list = userPage.getRecords().stream()
                .map(user -> {
                    SearchResultVO vo = new SearchResultVO();
                    vo.setId(user.getId());
                    vo.setName(user.getName());
                    vo.setPhone(user.getPhone());
                    vo.setEmail(user.getEmail());
                    vo.setRole(user.getRole());
                    vo.setScore(1.0);
                    return vo;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("total", userPage.getTotal());
        result.put("list", list);
        return result;
    }

    private static Long toLong(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Number) return ((Number) obj).longValue();
        try {
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String toStr(Object obj) {
        return obj != null ? obj.toString() : null;
    }
}
