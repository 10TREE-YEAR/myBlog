package com.wjl.blog.service.impl;

import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.mapper.EditerMapper;
import com.wjl.blog.service.EditerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service(value = "EditerService")
public class EditerServiceImpl implements EditerService {

    private static final Logger log = LoggerFactory.getLogger(EditerServiceImpl.class);

    private static final ThreadLocal<DateFormat> dateTime = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        }
    };

    @Resource
    private EditerMapper editerMapper;

    /**
     * 添加博客内容
     * @param blogContentBean
     * @return
     */
    @Override
    public boolean insertBlogContert(BlogContentBean blogContentBean) {
        // 填写博客信息
        String blogId = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
        blogContentBean.setId(blogId);
        blogContentBean.setStartTime(dateTime.get().format(new Date()));
        blogContentBean.setState("1");
        return editerMapper.insertBlogContert(blogContentBean);
    }

    /**
     * 查询修改的博客信息
     * @return
     */
    @Override
    public List<BlogContentBean> queryBlogContentList() {
        return editerMapper.queryBlogContentList();
    }
}
