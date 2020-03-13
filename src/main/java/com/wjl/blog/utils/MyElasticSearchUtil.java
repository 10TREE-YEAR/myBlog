package com.wjl.blog.utils;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * @Title:
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
     * @Title:
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
     * @Title:
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
     * @Title:
     * @Description: 删除索引信息
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


}
