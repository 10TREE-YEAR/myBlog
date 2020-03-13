package com.wjl.blog.constant;

/**
* @Description: es常量类
* @Param:
* @return:
* @Author: wangjialu
* @Date: 2020/3/10
*/
public class ElasticSearchConstant {

    /**
    * @Description: es 索引名称常量类
    * @Param:
    * @return:
    * @Author: wangjialu
    * @Date: 2020/3/10
    */
    public static final class EsIndexName{
        public static final String BLOG_CONTENT_INDEX_NAME = "q_blog";// index名称
    }

    /**
     * @Description: es 类型常量类
     * @Param:
     * @return:
     * @Author: wangjialu
     * @Date: 2020/3/10
     */
    public static final class EsTypeName{
        public static final String BLOG_CONTENT_TYPE_NAME = "blog_content"; // type名称
    }

    /**
     * @Title:
     * @Description: es setting常量类
     * @param @param
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/13 19:37
     */
    public static final class  EsSetting{
        public static final String SHARDS_NUM = "5"; // 主分片数量
        public static final String REPLICAS_NUM = "3"; // 副分片数量
    }
}
