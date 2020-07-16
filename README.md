# myBlog

* 博客分为前台和后台，后台完成编写、展示和修改功能，前台目前完成展示功能
* 项目使用SpringBoot搭建，使用redis、es、rabbitmq、mysql来处理博客内容，博客编写工具使用markdown内嵌工具
* 在Linux系统创建docker用来管理redis、es、mysql,使用虚拟机创建Linux建议分配内存在4G以上

# 版本
* SpringBoot 2.1.8
* redis 4.0以上
* es 6.7.1 (不支持7.0及以上)
* mysql 5.6以上
* rabbitmq 3.7以上

# 存在问题
* 项目没有实现图片上传功能
* 前台展示效果不佳,需要markdown前台组件展示
* 博客分类和审核等功能添加
