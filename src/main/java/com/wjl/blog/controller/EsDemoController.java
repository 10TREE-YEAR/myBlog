package com.wjl.blog.controller;


import com.wjl.blog.entity.EsDemoBean;
import com.wjl.blog.repository.EsDemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
        esDemoBean.setId("1");
        esDemoBean.setTime(new Date());
        esDemoBean.setTitle("这是一个EsDemo");
        esDemoRepository.save(esDemoBean);
        return "success";
    }


}
