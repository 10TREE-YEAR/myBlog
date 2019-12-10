(function(){
    var mod = {};
    var isRequestAjax = true ;

    // //提示信息
    // mod.toastrSuccess = function (content){
    //     toastr.options = {
    //         closeButton: false,  // 是否显示关闭按钮，（提示框右上角关闭按钮）
    //         debug: false,        // 是否使用deBug模式
    //         progressBar: true,    // 是否显示进度条，（设置关闭的超时时间进度条）
    //         positionClass: "toast-top-center",   // 设置提示款显示的位置
    //         onclick: null,         // 点击消息框自定义事件 
    //         showDuration: "300",   // 显示动画的时间
    //         hideDuration: "1000",   //  消失的动画时间
    //         timeOut: "2000",          //  自动关闭超时时间 
    //         extendedTimeOut: "1000",   //  加长展示时间
    //         showEasing: "swing",      //  显示时的动画缓冲方式
    //         hideEasing: "linear",      //   消失时的动画缓冲方式
    //         showMethod: "fadeIn",     //   显示时的动画方式
    //         hideMethod: "fadeOut"     //   消失时的动画方式
    //     };
    //     if (content == null) {
    //         content = '';
    //     }
    //     var len = content.length;
    //     if (len <= 10 && len > 0) {
    //         toastr.options.timeOut = "1800";
    //     } else if (len <= 20) {
    //         toastr.options.timeOut = "2800";
    //     } else if (len <= 30) {
    //         toastr.options.timeOut = "3800";
    //     } else if (len > 30) {
    //         toastr.options.timeOut = "4800";
    //     }
    //     toastr.success(content);
    // }

    mod.register = function () {
        var userName = $("#userName").val();//用户名
        var userNum = $("#userNum").val();//账号
        var userPassword = $("#userPassword").val();//密码
        var iphone = $("#iphone").val();//手机号
        if(userName == null || userName == "" || userName == undefined){
            alert("请填写用户名！！！")
        }else if(userNum == null || userNum == "" || userNum == undefined){
            alert("请填写账号！！！")
        }else if(userPassword == null || userPassword == "" || userPassword == undefined){
            alert("请填写密码！！！")
        }else if(iphone == null || iphone == "" || iphone == undefined){
            alert("请填写手机号！！！")
        }else{
            $("#toRegister").submit;
        }

    }
    window.lgoin = mod;
})();