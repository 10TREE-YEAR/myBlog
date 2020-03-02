package com.wjl.blog.controller;


import com.wjl.blog.entity.EsDemoBean;
import com.wjl.blog.repository.EsDemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
* @Description:  es deme控制层
* @Param:
* @return:
* @Author: wangjialu
* @Date: 2020/2/27
*/
@RestController
@RequestMapping("/es")
@Slf4j
public class EsDemoController {

    @Autowired
    private EsDemoRepository esDemoRepository;


    /**
    * @Description: es添加信息
    * @Param: [esDemoBean]
    * @return: java.lang.String
    * @Author: wangjialu
    * @Date: 2020/2/27
    */
    @GetMapping("/add")
    public String add(){
        EsDemoBean esDemoBean = new EsDemoBean();
        esDemoBean.setId(UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
        esDemoBean.setTime(new Date());
        esDemoBean.setTitle("这是一个EsDemo");
        esDemoRepository.save(esDemoBean);
        return "success";
    }


    /** 
    * @Description: 根据Id查询es保存的内容
    * @Param: [id] 
    * @return: java.lang.String 
    * @Author: wangjialu
    * @Date: 2020/3/2 
    */ 
    @GetMapping("/get/{id}")
    public String selectEsContentById(@PathVariable(value = "id") String id){
        String content = "";

        if(StringUtils.isEmpty(id)){
            return "error: 查询的id为空，请重新输入！";
        }
        // 1.0 根据Id查询内容
        Optional<EsDemoBean> blogModelOptional = esDemoRepository.findById(id);
        if(blogModelOptional.isPresent()){
            EsDemoBean esDemoBean = blogModelOptional.get();
            content = esDemoBean.getTitle();
        }
        return content;
    }


}
