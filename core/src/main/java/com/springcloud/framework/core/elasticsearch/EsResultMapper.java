package com.springcloud.framework.core.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.*;

/**
 * @author zhangkun
 * @version 1.0
 * @date 2018/4/23
 * @description
 *
 */
public class EsResultMapper implements SearchResultMapper {

    private ObjectMapper objectMapper;

    public EsResultMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
            List<T> result = new ArrayList<T>();
            for (SearchHit searchHit : response.getHits().getHits()) {
                Map<String, Object> map = searchHit.getSource();
                try {
                    Map<String, HighlightField> hitMap = searchHit.getHighlightFields();
                    Iterator<Map.Entry<String, HighlightField>> entryIterator = hitMap.entrySet().iterator();
                    while (entryIterator.hasNext()) {
                        Map.Entry<String, HighlightField> entry = entryIterator.next();
                        String[] keys = entry.getKey().split("\\.");
                        Map<String, Object> r = map;
                        for (int i = 0; i < keys.length; i++) {
                            if (r == null) {
                                continue;
                            }
                            if (i == keys.length - 1) {
                                r.put(keys[i], entry.getValue().fragments()[0].toString());
                            } else {
                                r = (Map) r.get(keys[i]);
                            }
                        }
                    }
                    T entity = objectMapper.readValue(objectMapper.writeValueAsString(map), clazz);
                    result.add(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (result.size() > 0) {
                return new AggregatedPageImpl<T>((List<T>) result, response.getScrollId());
            }
            return new AggregatedPageImpl<T>(Collections.EMPTY_LIST, response.getScrollId());
    }


}
