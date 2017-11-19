
$(function(){
    $("#tijiao").click(function(){
        var arrPerson=new Array();
        var id      = $("#id").val();
        var typeid  = $("#typeid").val();
        var name  = $("#name").val();
        var tel   = $("#phoneNum").val();

        var xs    = $("#xs").val();
        var postarea_prov     = $("#cmbProvince").val();
        var postarea_city     = $("#cmbCity").val();
        var postarea_country  = $("#cmbArea").val();
        var address  = $("#address").val();
        var tel_yz    = /^1[34578]\d{9}$/;
        var teltype =$('input:radio[name="cardType"]:checked').val();
        var items =$('input:radio[name="items"]:checked').val();
        var person =  new Object();
        var len = $(".attr").length;
        var style = $("#style").val();
        var clothSize = $("#clothSize").val();
        if (name == '') {
            alert('请输入姓名');
            return false;
        }
        if (tel == '') {
            alert('请输入手机号');
            return false;
        }
        if(!tel_yz.test(tel)){
            alert("请填写正确手机号");
            return false;
        }
        if(teltype==null){
            alert("电话卡充值类型!");
            return false;
        }
        if (postarea_prov == '' || postarea_prov ==  "-1") {
            alert('请填写省/自治区/直辖市');
            return false;
        }
        if (postarea_city == '' || postarea_city == "-1") {
            alert('请填写市/区');
            return false;
        }

        if (postarea_country == '' || postarea_country == "-1") {
            alert('请填写区/县');
            return false;
        }

        if (address == '') {
            alert('请输入详细地址');
            return false;
        }
        if(style == null || style == ""){
            alert('请选择款式');
            return false;
        }
        if(clothSize == null || clothSize == ""){
            alert('请选择尺码');
            return false;
        }

        // if (len > 0 ) {
        //     for (var i = 0; i < len; i++) {
        //         var attr = $(".attr").eq(i).val();
        //         var attid = $(".attid").eq(i).val();
        //         person[attid] = $(".attr").eq(i).val();
        //         if (attr == '') {
        //             str=$('.xiug').eq(i).text();
        //             alert('请选择'+str.substr(0,str.length-3));
        //             return false;
        //
        //         }
        //     }
        //     arrPerson.push(person);
        // }
        if(items==null){
            alert("请确认运费自理!");
            return false;
        }
        // address = postarea_prov + postarea_city + postarea_country + address;

        var contextPath = $("#CONTEXT_PATH").html();

        url = contextPath + '/order/save';
        $.ajax({
            url  :  url,
            type : 'post',
            data : {
                phoneNum: tel,
                name: name,
                sheng: postarea_prov,
                shi: postarea_city,
                qu: postarea_country,
                address: address,
                cardType: teltype,
                style: style,
                clothSize: clothSize,
                userINfo: $("#USER_INFO").html(),
                goodsName: $("#GOODS_NAME").html()
            },

            success:function(data){
                if(200 == data.status){
                    alert("提交成功");
                    /*window.location.href = contextPath + "/search";*/
                    $("#page").css("display","none");
                    $("#page2").css("display","block");
                }else{
                    alert(data.info);
                }

                console.log(data);
                // if (data==1) {
                //     alert('订单提交成功');
                //     $("#page").hide();
                //     $("#page2").show();
                //     return false;
                //
                // }else{
                //     alert('订单提交失败');
                //     return false;
                // }
            },
            error: function () {
                alert("提交出错");
            }
        });



    })
});

/*$.ajax({
    url  : 'member.php',
    type : 'post',
    data : { },
    dataType:'html',
    success:function(data){
        console.log(data);
        if (data ==1) {
            window.history.go(-1);
        }
    }
});*/

