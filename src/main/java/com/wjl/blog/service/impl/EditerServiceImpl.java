package com.wjl.blog.service.impl;

import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.mapper.EditerMapper;
import com.wjl.blog.service.EditerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service(value = "EditerService")
public class EditerServiceImpl implements EditerService {

    private static final Logger log = LoggerFactory.getLogger(EditerServiceImpl.class);

    @Resource
    private EditerMapper editerMapper;

    /**
     * 添加博客内容
     * @param blogContentBean
     * @return
     */
    @Override
    public boolean insertBlogContert(BlogContentBean blogContentBean) {
        return editerMapper.insertBlogContert(blogContentBean);
    }
}
