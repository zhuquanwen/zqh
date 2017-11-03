function searchCourier(){
    var phoneNum = $("#phoneNum").val();
    console.log(phoneNum);
    $.ajax({
        url:"order/phoneNum/" + phoneNum,
        type:"GET",
        data: null,
        success:function(result){

            if(200 != result.status){
                alert(result.info);
            }else{
                $("#span").html(result.data);
            }
        }
        // ,
        // error:function(){
        //     alert("查询出错!");
        // }
    });
}