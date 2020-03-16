package com.wjl.blog.utils;

import com.alibaba.fastjson.JSON;
import com.wjl.blog.entity.EsPage;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description: 自己es工具类
 * @param @param
 * @return @return
 * @author wangjialu
 * @throws
 * @date 2020/3/12 19:03
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class MyElasticSearchUtil {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static RestHighLevelClient client;

    /**
     * spring容器初始化的时候执行该方法
     */
    @PostConstruct
    public void initClient() {
        client = this.restHighLevelClient;
    }


    /**
     * @Description: 检查索引是否存在
     * @param @param
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/12 19:04
     */
    public static boolean isEsIndexNameExist(String indexName){
        boolean exist = false;
        try {
            exist = client.indices().exists(new GetIndexRequest(indexName),RequestOptions.DEFAULT);
        } catch (IOException e) {
            exist = false;
            e.printStackTrace();
        }
        if(exist){
            log.info("index["+indexName+"]is not exits");
        }else{
            log.info("index["+indexName+"]is exits");
        }
        return exist;
    }

    /**
     * @Description: 创建索引
     * @param @param
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/12 19:50
     */
    public static boolean createIndex(String indexName, String typeName,String shardsNum, String replicasNum,Map<String,Map<String,String>> map){
        // 1.0 查看索引是否存在
        if(isEsIndexNameExist(indexName)){
            log.info("index ["+indexName+"] is not exits!!!");
            return false;
        }
        // map 参考格式
        /*Map<String,Map<String,String>> map = new HashMap<>();
        Map<String,String> map1 = new HashMap<String ,String>();
        map1.put("type","text");
        map1.put("analyzer","ik_max_word");
        map.put(typeName,map1);*/
        // 2.0 封装文档信息
        StringBuffer buffer = new StringBuffer();
        buffer.append(" {\n" +
                " \""+typeName+"\": {\n" +
                " \"properties\": {\n");
        int i = map.size();
        int j = 1;
        for (Map.Entry<String, Map<String, String>> mapEntry : map.entrySet()) {
            String k = mapEntry.getKey();
            Map<String, String> v = mapEntry.getValue();
            buffer.append(" \"" + k + "\": {\n");
            int n = v.size();
            int p = 1;
            for (Map.Entry<String, String> entry : v.entrySet()) {
                if (n != p) {
                    buffer.append(" \"" + entry.getKey() + "\": \"" + entry.getValue() + "\",\n");
                } else {
                    buffer.append(" \"" + entry.getKey() + "\": \"" + entry.getValue() + "\"\n");
                }
                p++;
            }
            if (i != j) {
                buffer.append(" },\n");
            } else {
                buffer.append(" }\n");
            }
            j++;
        }
        buffer.append(" }\n");
        buffer.append(" }\n");
        buffer.append(" }\n");
        log.info("=================bull:"+buffer.toString()+"==================");
        CreateIndexResponse createIndexResponse = null;
        try {
            // 3.0 添加索引及分片
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName)
                        .settings(Settings.builder()
                        .put("index.number_of_shards",shardsNum)
                        .put("index.number_of_replicas",replicasNum));
            // 4.0 添加文档信息
            createIndexRequest.mapping(typeName,buffer.toString(), XContentType.JSON);
            // 4.1 设置创建索引超时2分钟
            createIndexRequest.timeout(TimeValue.timeValueMinutes(2));
            createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 5.0 返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：
        return createIndexResponse.isAcknowledged();
    }

    /**
     * @Description: 删除索引
     * @param @param
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/13 20:01
     */
    public static boolean deleteIndex(String indexName){
        // 1.0 判断索引是否存在
        boolean deleteRequest = false;
        if(!isEsIndexNameExist(indexName)){
            log.info("index["+indexName+"] is not exits, please don`t delete!");
            return deleteRequest;
        }else {
            try {
                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
                deleteRequest = client.indices().delete(deleteIndexRequest,RequestOptions.DEFAULT).isAcknowledged();
                if(!deleteRequest){
                    log.info("delete index["+indexName+"] is fail !");
                }
                return deleteRequest;
            } catch (IOException e) {
                e.printStackTrace();
                log.info("delete index["+indexName+"] is fail !");
                return false;
            }
        }

    }

    /**
     * @Description: es添加数据信息
     * @param @param
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/13 20:19
     */
    public static String addDate(XContentBuilder content, String indexName, String type, String id) throws IOException {
        IndexResponse response = null;
        IndexRequest request = new IndexRequest(indexName,type).id(id).source(content);
        response = client.index(request, RequestOptions.DEFAULT);
        log.info("addData response status:{},id:{}", response.status().getStatus(), response.getId());
        return response.getId();
    }

    /**
     * @Description: 修改es内容
     * @param @param
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/16 18:39
     */
    public static String updateData(XContentBuilder content,String index,String type, String id) throws IOException {
        UpdateResponse response = null;
        UpdateRequest request = new UpdateRequest(index, type,id).doc(content);
        response = client.update(request, RequestOptions.DEFAULT);
        log.info("updateData response status:{},id:{}", response.status().getStatus(), response.getId());
        return response.getId();
    }

    /**
     * @Description: 删除es内容
    * @param @param
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/16 18:38
     */
    public static void deleteData(String index, String type, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        log.info("delete:{}" , JSON.toJSONString(response));
    }

    /**
     * @Description: 根据条件删除es内容
     * @param builder 要删除的数据  new TermQueryBuilder("id", userId)
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/16 18:40
     */
    public static void deleteByQuery(String indexName, QueryBuilder builder) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            client.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 使用分词查询  高亮 排序 ,并分页
     * @param index          索引名称
     * @param startPage      当前页
     * @param pageSize       每页显示条数
     * @param query          查询条件
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param sortord        排序方式
     * @param highlightField 高亮字段
     * @return 结果
     * @author wangjialu
     * @throws
     * @date 2020/3/16 19:23
     */
    public static EsPage searchDataPage(String index, Integer startPage, Integer pageSize,
                            QueryBuilder query, String fields, String sortField, String sortord ,String highlightField) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置一个可选的超时，控制允许搜索的时间
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (StringUtils.isNotBlank(fields)) {
            searchSourceBuilder.fetchSource(fields, null);
        }
        //排序
        if (StringUtils.isNotBlank(sortField)) {
            //判断是否为索引映射字段
            boolean b = isEsIndexNameExist(sortField);
            if (b) {
                FieldSortBuilder sort = SortBuilders.fieldSort(sortField);
                if (StringUtils.isNotBlank(sortord) && "asc".equals(sortord)) {
                    sort.order(SortOrder.ASC);
                }else {
                    sort.order(SortOrder.DESC);
                }
                searchSourceBuilder.sort(sort);
            }
        }
        // 高亮（xxx=111,aaa=222）
        if (StringUtils.isNotBlank(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            //设置前缀
            highlightBuilder.preTags("<span style='color:red' >");
            //设置后缀
            highlightBuilder.postTags("</span>");
            HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(highlightField);
            //荧光笔类型
            highlightTitle.highlighterType("unified");
            // 设置高亮字段
            highlightBuilder.field(highlightTitle);
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        // 设置是否按查询匹配度排序
        searchSourceBuilder.explain(true);
        if (startPage <= 0) {
            startPage = 0;
        }
        //如果 pageSize是10 那么startPage>9990 (10000-pagesize) 如果 20  那么 >9980 如果 50 那么>9950
        //深度分页
        if (startPage > (10000 - pageSize)) {
            searchSourceBuilder.query(query);
            searchSourceBuilder
                    // .setScroll(TimeValue.timeValueMinutes(1))
                    .size(10000);
            //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
            log.info("\n{}", searchSourceBuilder);
            // 执行搜索,返回搜索响应信息
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = null;
            try {
                searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long totalHits = searchResponse.getHits().getTotalHits();
            if (searchResponse.status().getStatus() == 200) {
                //使用scrollId迭代查询
                List<Map<String, Object>> result = disposeScrollResult(searchResponse, highlightField);
                List<Map<String, Object>> sourceList = result.stream().parallel().skip((startPage - 1 - (10000 / pageSize)) * pageSize).limit(pageSize).collect(Collectors.toList());
                return new EsPage(startPage, pageSize, (int) totalHits, sourceList);
            }
        } else {//浅度分页
            // searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.query(query);
            /*MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("username", "pretty");
		    matchQueryBuilder.fuzziness(Fuzziness.AUTO);//在匹配查询上启用模糊匹配
		    searchSourceBuilder.query(matchQueryBuilder);*/
            // 分页应用
            searchSourceBuilder
                    //设置from确定结果索引的选项以开始搜索。默认为0
                    // .from(startPage)
                    .from((startPage - 1) * pageSize)
                    //设置size确定要返回的搜索匹配数的选项。默认为10
                    .size(pageSize);
            //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
            log.info("\n{}", searchSourceBuilder);
            // 执行搜索,返回搜索响应信息
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = null;
            try {
                searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long totalHits = searchResponse.getHits().getTotalHits();
            long length = searchResponse.getHits().getHits().length;
            log.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
            if (searchResponse.status().getStatus() == 200) {
                // 解析对象
                List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightField);
                return new EsPage(startPage, pageSize, (int) totalHits, sourceList);
            }
        }
        return null;
    }

    /**
     * @Description: 高亮结果集 特殊处理
     * @param searchResponse 搜索的结果集
     * @param highlightField 高亮字段
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/16 19:23
     */
    private static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<>();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            Map<String, Object> resultMap = getResultMap(searchHit, highlightField);
            sourceList.add(resultMap);
        }
        return sourceList;
    }

    /**
     * @Description: 获取高亮结果集
     * @param @param
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/16 19:23
     */
    private static Map<String, Object> getResultMap(SearchHit hit, String highlightField) {
        hit.getSourceAsMap().put("id", hit.getId());
        if (StringUtils.isNotBlank(highlightField)) {
            Text[] text = hit.getHighlightFields().get(highlightField).getFragments();
            String hightStr = null;
            if (text != null) {
                StringBuffer stringBuffer = new StringBuffer();
                for (Text str : text) {
                    String s = str.string();
                    //s.replaceAll("<p>", "<span>").replaceAll("</p>", "</span>");
                    stringBuffer.append(s);
                }
                hightStr = stringBuffer.toString();
                //遍历 高亮结果集，覆盖 正常结果集
                hit.getSourceAsMap().put(highlightField, hightStr);
            }
        }
        return hit.getSourceAsMap();
    }

    public static <T> List<T> search(String index, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(index);
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 处理scroll结果
     * @param @param
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/16 19:23
     */
    private static List<Map<String, Object>> disposeScrollResult(SearchResponse response, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<>();
        //使用scrollId迭代查询
        while (response.getHits().getHits().length > 0) {
            String scrollId = response.getScrollId();
            try {
                response = client.scroll(new SearchScrollRequest(scrollId), RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits.getHits()) {
                Map<String, Object> resultMap = getResultMap(hit, highlightField);
                sourceList.add(resultMap);
            }
        }
        ClearScrollRequest request = new ClearScrollRequest();
        request.addScrollId(response.getScrollId());
        try {
            client.clearScroll(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourceList;
    }
}
