package com.springcloud.framework.core.elasticsearch;

import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.facet.FacetRequest;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.util.Assert;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Configuration
@ConditionalOnClass(ElasticsearchTemplate.class)
public class EsTemplate {
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	private String searchTimeout;
	
	public <T> AggregatedPage<T> queryForPage(SearchQuery query, Class<T> clazz, SearchResultMapper mapper) {
		SearchResponse response = doSearch(prepareSearch(query, clazz), query);
		return mapper.mapResults(response, clazz, query.getPageable());
	}

	public <T> Page<T> startScroll(long scrollTimeInMillis, SearchQuery searchQuery, Class<T> clazz, SearchResultMapper mapper) {
		SearchResponse response = doScroll(prepareScroll(searchQuery, scrollTimeInMillis, clazz), searchQuery);
		return mapper.mapResults(response, clazz, null);
	}

	public <T> Page<T> continueScroll(@Nullable String scrollId, long scrollTimeInMillis, Class<T> clazz, SearchResultMapper mapper) {
		SearchResponse response = getSearchResponse(elasticsearchTemplate.getClient().prepareSearchScroll(scrollId)
				.setScroll(TimeValue.timeValueMillis(scrollTimeInMillis)).execute());
		return mapper.mapResults(response, clazz, Pageable.unpaged());
	}

	private <T> SearchRequestBuilder prepareScroll(Query query, long scrollTimeInMillis, Class<T> clazz) {
		setPersistentEntityIndexAndType(query, clazz);
		return prepareScroll(query, scrollTimeInMillis);
	}

	private SearchRequestBuilder prepareScroll(Query query, long scrollTimeInMillis) {
		SearchRequestBuilder requestBuilder = elasticsearchTemplate.getClient().prepareSearch(toArray(query.getIndices()))
				.setTypes(toArray(query.getTypes())).setScroll(TimeValue.timeValueMillis(scrollTimeInMillis)).setFrom(0);

		if(query.getPageable().isPaged()){
			requestBuilder.setSize(query.getPageable().getPageSize());
		}

		if (!isEmpty(query.getFields())) {
			requestBuilder.setFetchSource(toArray(query.getFields()), null);
		}
		return requestBuilder;
	}

	private SearchResponse doScroll(SearchRequestBuilder requestBuilder, SearchQuery searchQuery) {
		Assert.notNull(searchQuery.getIndices(), "No index defined for Query");
		Assert.notNull(searchQuery.getTypes(), "No type define for Query");
		Assert.notNull(searchQuery.getPageable(), "Query.pageable is required for scan & scroll");

		if (searchQuery.getFilter() != null) {
			requestBuilder.setPostFilter(searchQuery.getFilter());
		}
		if (searchQuery.getHighlightFields() != null) {
			HighlightBuilder highlighter=new HighlightBuilder();
			for (HighlightBuilder.Field highlightField : searchQuery.getHighlightFields()) {
				highlighter.field(highlightField);
			}
			requestBuilder.highlighter(highlighter);
		}

		return getSearchResponse(requestBuilder.setQuery(searchQuery.getQuery()).execute());
	}
	
	private <T> SearchRequestBuilder prepareSearch(Query query, Class<T> clazz) {
		setPersistentEntityIndexAndType(query, clazz);
		return prepareSearch(query);
	}
	
	private SearchRequestBuilder prepareSearch(Query query) {
		Assert.notNull(query.getIndices(), "No index defined for Query");
		Assert.notNull(query.getTypes(), "No type defined for Query");

		int startRecord = 0;
		SearchRequestBuilder searchRequestBuilder = elasticsearchTemplate.getClient().prepareSearch(toArray(query.getIndices()))
				.setSearchType(query.getSearchType()).setTypes(toArray(query.getTypes()));

		if (query.getSourceFilter() != null) {
			SourceFilter sourceFilter = query.getSourceFilter();
			searchRequestBuilder.setFetchSource(sourceFilter.getIncludes(), sourceFilter.getExcludes());
		}

		if (query.getPageable().isPaged()) {
			startRecord = query.getPageable().getPageNumber() * query.getPageable().getPageSize();
			searchRequestBuilder.setSize(query.getPageable().getPageSize());
		}
		searchRequestBuilder.setFrom(startRecord);

		if (!query.getFields().isEmpty()) {
			searchRequestBuilder.setFetchSource(toArray(query.getFields()),null);
		}

		if (query.getSort() != null) {
			for (Sort.Order order : query.getSort()) {
				searchRequestBuilder.addSort(order.getProperty(),
						order.getDirection() == Sort.Direction.DESC ? SortOrder.DESC : SortOrder.ASC);
			}
		}

		if (query.getMinScore() > 0) {
			searchRequestBuilder.setMinScore(query.getMinScore());
		}
		return searchRequestBuilder;
	}
	
	private static String[] toArray(List<String> values) {
		String[] valuesAsArray = new String[values.size()];
		return values.toArray(valuesAsArray);
	}
	
	private void setPersistentEntityIndexAndType(Query query, Class clazz) {
		if (query.getIndices().isEmpty()) {
			query.addIndices(retrieveIndexNameFromPersistentEntity(clazz));
		}
		if (query.getTypes().isEmpty()) {
			query.addTypes(retrieveTypeFromPersistentEntity(clazz));
		}
	}
	
	private String[] retrieveTypeFromPersistentEntity(Class clazz) {
		if (clazz != null) {
			return new String[] { elasticsearchTemplate.getPersistentEntityFor(clazz).getIndexType() };
		}
		return null;
	}
	
	private String[] retrieveIndexNameFromPersistentEntity(Class clazz) {
		if (clazz != null) {
			return new String[] { elasticsearchTemplate.getPersistentEntityFor(clazz).getIndexName() };
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	private SearchResponse doSearch(SearchRequestBuilder searchRequest, SearchQuery searchQuery) {
		if (searchQuery.getFilter() != null) {
			searchRequest.setPostFilter(searchQuery.getFilter());
		}

		if (!isEmpty(searchQuery.getElasticsearchSorts())) {
			for (SortBuilder sort : searchQuery.getElasticsearchSorts()) {
				searchRequest.addSort(sort);
			}
		}

		if (!searchQuery.getScriptFields().isEmpty()) {
			//_source should be return all the time
			//searchRequest.addStoredField("_source");
			for (ScriptField scriptedField : searchQuery.getScriptFields()) {
				searchRequest.addScriptField(scriptedField.fieldName(), scriptedField.script());
			}
		}

		//fix bug
		if (searchQuery.getHighlightFields() != null) {
			HighlightBuilder highlighter=new HighlightBuilder();
			for (HighlightBuilder.Field highlightField : searchQuery.getHighlightFields()) {
				highlighter.field(highlightField);
			}
			searchRequest.highlighter(highlighter);
		}

		if (!isEmpty(searchQuery.getIndicesBoost())) {
			for (IndexBoost indexBoost : searchQuery.getIndicesBoost()) {
				searchRequest.addIndexBoost(indexBoost.getIndexName(), indexBoost.getBoost());
			}
		}

		if (!isEmpty(searchQuery.getAggregations())) {
			for (AbstractAggregationBuilder aggregationBuilder : searchQuery.getAggregations()) {
				searchRequest.addAggregation(aggregationBuilder);
			}
		}

		if (!isEmpty(searchQuery.getFacets())) {
			for (FacetRequest aggregatedFacet : searchQuery.getFacets()) {
				searchRequest.addAggregation(aggregatedFacet.getFacet());
			}
		}
		return getSearchResponse(searchRequest.setQuery(searchQuery.getQuery()).execute());
	}

	private SearchResponse getSearchResponse(ListenableActionFuture<SearchResponse> response) {
		return searchTimeout == null ? response.actionGet() : response.actionGet(searchTimeout);
	}

	public void setSearchTimeout(String searchTimeout) {
		this.searchTimeout = searchTimeout;
	}
	
}
