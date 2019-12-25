(function (e) {
    var mod = {};
    var ajaxRequest = true;
    var url;
    /**
     * 修改博客信息
     */
    mod.editBlog = function(data){
        window.location.href = "/editBlog.html?id="+data;
    };

    /**
     * 删除博客信息
     * @param data
     */
    mod.deleteBlog = function(data){
        url = "/deleteBlog.html";
        swal({
            title : "Are you sure?",
            text : "确定要删除吗？",
            type : "warning",
            showCancelButton : true,
            confirmButtonColor : '#DD6B55',
            confirmButtonText : 'Yes',
            cancelButtonText : "No",
            closeOnConfirm : false
        }, function () {
           $.ajax({
               type: 'POST',//方法类型
               url: url,
               data: {id : data},
               success: function (result){
                   console.info(result);
                   if (result.resultCode == 200) {
                       swal({
                           title: result.resultMsg,
                           text: "",
                           type: "success",
                           confirmButtonColor: '#3085d6',
                           icon: "success",
                           button: "确定",
                       }, function () {
                           window.location.href = "/blogModify.html";
                       });
                   }
                   else {
                       swal(result.message, {
                           icon: "error",
                       });
                   };
               },
               error: function () {
                   swal({
                       title: "操作失败",
                       type: "error",
                       icon: "error",
                   });
               }
           });
        });
    };

    window.blog = mod;
})(window);