var searchFlag = false;
$(function(){
    initTable();
    // initDate();
    initFileInput();
    initImportModalHiden();
    initValidator();
    $('#editWindow').on('hidden.bs.modal', function() {
        $("#editForm").data('bootstrapValidator').destroy();
        $('#editForm').data('bootstrapValidator', null);
        initValidator();
    });
});



function initValidator(){

    // $("#editForm").data('bootstrapValidator').resetForm();
    $('#editForm').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name1: {
                message: '用户名验证失败',
                validators: {
                    stringLength: {
                        min: 2,
                        max: 15,
                        message: '用户名长度必须在2到15位之间'
                    }
                }
            }
                // ,
            // courierNum1: {
            //     validators: {
            //         notEmpty: {
            //             message: '单号不能为空'
            //         },
            //         regexp: {
            //             regexp: /^[0-9]*$/,
            //             message: '单号必须为数字组成'
            //         }
            //     }
            // }
            ,
            phoneNum1: {
                validators: {
                    notEmpty: {
                        message: '手机号码不能为空'
                    },
                    stringLength: {
                        min: 6,
                        max: 11,
                        message: '手机号码必须在6到11位之间'
                    },
                    regexp: {
                        regexp: /^[0-9]*$/,
                        message: '手机号码必须为数字组成'
                    }
                }
            },
            goodsName1: {
                validators: {
                    notEmpty: {
                        message: '商品名称不能为空'
                    },
                    stringLength: {
                        min: 2,
                            max: 30,
                            message: '商品名称必须在2到30位之间'
                    }
                }
            },
            style1: {
                validators: {
                    notEmpty: {
                        message: '款式不能为空'
                    },
                    stringLength: {
                        min: 2,
                            max: 30,
                            message: '款式名称必须在2到30位之间'
                    }
                }
            },
            clothSize1: {
                validators: {
                    notEmpty: {
                        message: '尺码不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 30,
                        message: '尺码名称必须在1到30位之间'
                    }
                }
            },
            address1: {
                validators: {
                    notEmpty: {
                        message: '地址不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 70,
                        message: '地址名称必须在2到70位之间'
                    }
                }
            },
            spreadUserName1: {
                validators: {
                    notEmpty: {
                        message: '推广人不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 20,
                        message: '推广人名称必须在2到70位之间'
                    }
                }
            }

        }
        });

}

function initImportModalHiden(){
    $('#importWindow').on('hidden.bs.modal', function () {
        $('#result-table').bootstrapTable("refresh");
    })
}

function initFileInput() {

    var FileInput = function () {
        var oFile = new Object();
        //初始化fileinput控件（第一次初始化）
        oFile.Init = function(ctrlName, uploadUrl) {
            var control = $('#' + ctrlName);

            //初始化上传控件的样式
            control.fileinput({
                language: 'zh', //设置语言
                uploadUrl: uploadUrl, //上传的地址
                allowedFileExtensions: ['xls','xlsx'],//接收的文件后缀
                uploadAsync: true, //默认异步上传
                showUpload: true, //是否显示上传按钮

                showRemove : true, //显示移除按钮
                showPreview : true, //是否显示预览
                showCaption: false,//是否显示标题
                browseClass: "btn btn-primary", //按钮样式
                dropZoneEnabled: true,//是否显示拖拽区域
                maxFileCount: 5, //表示允许同时上传的最大文件个数
                enctype: 'multipart/form-data',
                validateInitialCount:true
            });

            //导入文件上传完成之后的事件
            $("#txt_file").on("fileuploaded", function (event, data, previewId, index) {
                var response = data.response;
                if(!response.status){
                    alert(response.message);
                }else{

                }
                // //1.初始化表格
                // var oTable = new TableInit();
                // oTable.Init(data);
                // $("#div_startimport").show();
            });
        }
        return oFile;
    };
    //0.初始化fileinput
    var oFileInput = new FileInput();
    oFileInput.Init("txt_file", "importOrder");
}

function initTable(){

    var url = "order";
    $('#result-table').bootstrapTable({
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        cache: false,
        striped: true,                              //是否显示行间隔色
        sortName: "id",
        sortOrder: "desc",
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        url:url,
        height: $(window).height() - 110,
        width:$(window).width(),
        showColumns:true,
        pagination:true,
        queryParams : queryParams,
        minimumCountColumns:2,
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 20, 50, 100],        //可供选择的每页的行数（*）
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        // showExport: true,
        // exportDataType: 'all',
        responseHandler: responseHandler,
        showRefresh: true,
        //是否显示刷新按钮
        // onRefresh: function(){
        //     $("#editForm").data('bootstrapValidator').destroy();
        //     $('#editForm').data('bootstrapValidator', null);
        //
        // },
        onLoadSuccess: function(){

        },
        onLoadError: function(status){  //加载失败时执行

            alert("加载数据出错!");
        },
        columns: [
            {
                field:"checkbox",checkbox: true,title:"选择",class:"tablebody",align:"center",valign:"middle",
            },
            {
                field: '',
                title: '序号',
                align : 'center',
                valign : 'middle',
                formatter: function (value, row, index) {
                    return index+1;
                }
            },
            {
                field : 'name',
                title : '买家姓名',
                align : 'center',
                valign : 'middle',
                sortable : true
            },

            {
                field : 'sheng',
                title : '买家收货省',
                align : 'center',
                valign : 'middle',
                sortable : true
            },
            {
                field : 'shi',
                title : '买家收货市',
                align : 'center',
                valign : 'middle',
                sortable : true
            },
            {
                field : 'qu',
                title : '买家收货区',
                align : 'center',
                valign : 'middle',
                sortable : true
            },

            {
                field : 'address',
                title : '买家收货地址',
                align : 'center',
                valign : 'middle',
                sortable : true
            },{
                field : 'phoneNum',
                title : '买家手机',
                align : 'center',
                valign : 'middle',
                sortable : true
            },{
                field : 'spreadUserName',
                title : '发件人',
                align : 'center',
                valign : 'middle',
                sortable : true
            },
            {
                field : 'goodsName',
                title : '商品名称',
                align : 'center',
                valign : 'middle',
                sortable : true
            }, {
                field : 'style',
                title : '款式',
                align : 'center',
                valign : 'middle',
                sortable : true
            }, {
                field : 'clothSize',
                title : '尺码',
                align : 'center',
                valign : 'middle',
                sortable : true
            },{
                field : 'cardType',
                title : '充值卡类型',
                align : 'center',
                valign : 'middle',
                sortable : true ,
                formatter : function (value, row, index){
                    if ("1" == value){
                        return "移动";
                    }else if("2" == value){
                        return "联通";
                    }else if("3" == value){
                        return "电信";
                    }
                }
            },
            {
                field : 'courierNum',
                title : '单号',
                align : 'center',
                valign : 'middle',
                sortable : true
            }, {
                field : 'orderDate',
                title : '下单时间',
                align : 'center',
                valign : 'middle',
                sortable : true
                // ,
                // formatter : function (value, row, index){
                //     return new Date(value).format('yyyy-MM-dd hh:mm:ss');
                // }
            }, {
                field : 'recordDate',
                title : '发货时间',
                align : 'center',
                valign : 'middle',
                sortable : true
                // ,
                // formatter : function (value, row, index){
                //     return new Date(value).format('yyyy-MM-dd hh:mm:ss');
                // }
            },{
                field : '',
                title : '操作',
                align : 'center',
                valign : 'middle'
                ,
                formatter : function (value, row, index){
                    return "<a onclick='editData(this);' href='javascript:void(0);'><img " +
                        "style='width:24px;height: 24px;' src='image/edit.png'/><span style='display: none;'>" + row.id + "</span> " +
                        "<a href='javascript:void(0);' onclick='deleteData(this);'>" +
                        "<img  style='width:24px;height: 24px;' src='image/delete.png'/>" +
                        "<span style='display: none;'>" + row.id + "</span></a>";
                }
            }
        ]
    });
}

function initDate(){
    $("#startDate").datetimepicker({
        format: "yyyy-mm-dd hh:ii",
        autoclose: true,
        todayBtn: true,
        language:"zh-CN",
        startDate: "2013-02-14 10:00",
        minuteStep: 10
    }).on("changeDate",function(ev){
        var startDate = $("#startDate").val();
        $("#endDate").datetimepicker("setStartDate",startDate);
        $("#startDate").datetimepicker("hide");
    });

    $("#endDate").datetimepicker({
        format: "yyyy-mm-dd hh:ii",
        autoclose: true,
        todayBtn: true,
        language:"zh-CN",
        startDate: "2013-02-14 10:00",
        minuteStep: 10
    }).on("changeDate",function(ev){
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        if(startDate != "" && endDate !=""){
            if(!checkEndTime(startDate,endDate)){
                $("#endDate").val("");
                alert("开始时间不能大于结束时间");
                return;
            }
        }
        $("#startDate").datetimepicker("setEndDate",endDate);
        $("#endDate").datetimepicker("hide");
    });
    $("#startDate").datetimepicker("setEndDate",null);
    $("#endDate").datetimepicker("setStartDate",null);
    $("#startDate").val(null);
    $("#endDate").val(null);
}

function checkEndTime(startDate,endDate){
    if(startDate > endDate){
        return false;
    }else{
        return true;
    }
}


function queryParams() {
    var param = {
        phoneNum : $("#phoneNum").val(),
        courierNum : $("#courierNum").val(),
        startDate : $("#startDate").val(),
        endDate : $("#endDate").val(),
        spreadUserName: $("#spreadUserName").val(),
        goodsName: $("#goodsName").val(),
        limit : this.limit, // 页面大小
        offset : this.offset, // 页码
        page : this.pageNumber - 1,
        size : this.pageSize,
        field :this.sortName,
        direction :this.sortOrder
    }
    if(searchFlag){
        param.page = 0;
        this.pageNumber = 1;
        searchFlag = false;
    }
    return param;
}

function responseHandler(res) {
    if (res) {
        return {
            "rows" : res.result,
            "total" : res.totalCount
        };
    } else {
        return {
            "rows" : [],
            "total" : 0
        };
    }
}



function editData(node){
    clearEditForm();
    initValidator();
    var idNode= $(node).find("span");
    var id = idNode.text();
    $.ajax({
        url:"order/" + id,
        type:"GET",
        data: null,
        success:function(result){

            if(200 != result.status){
                alert(result.info);
            }else{
                $("#orderDate1").val(result.data.orderDate);
                $("#orderDate").val(result.data.orderDate);
                $("#name1").val(result.data.name);
                $("#courierNum1").val(result.data.courierNum);
                $("#phoneNum1").val(result.data.phoneNum);
                $("#id1").val(result.data.id);
                $("#goodsName1").val(result.data.goodsName);
                $("#style1").val(result.data.style);
                $("#clothSize1").val(result.data.clothSize);
                $("#address1").val(result.data.address);
                $("#cardType1").val(result.data.cardType);
                $("#spreadUserName1").val(result.data.spreadUserName);
                $("#recordDate1").val(result.data.recordDate);
                $("#recordDate").val(result.data.recordDate);
                $("#cmbProvince").val(result.data.sheng);
                $("#cmbCity").val(result.data.shi);
                $("#cmbArea").val(result.data.qu);

                $("#editWindow").modal('show');
            }
        }
    });

}

function clearEditForm(){
    $("#orderDate").val(null);
    $("#orderDate1").val(null);
    $("#name1").val(null);
    $("#courierNum1").val(null);
    $("#phoneNum1").val(null);
    $("#id1").val(null);
    $("#goodsName1").val(null);
    $("#style1").val(null);
    $("#clothSize1").val(null);
    $("#address1").val(null);
    $("#cardType1").val(null);
    $("#spreadUserName1").val(null);
    $("#recordDate1").val(null);
    $("#recordDate").val(null);
    $("#cmbProvince").val(null);
    $("#cmbCity").val(null);
    $("#cmbArea").val(null);


}
function add() {
    clearEditForm();
    initValidator();
    $("#editWindow").modal('show');
}
function save(){
    var validate = $('#editForm').bootstrapValidator('validate');
    var isValid = $("#editForm").data('bootstrapValidator').isValid();
    if(!isValid){
        return;
    }

    var param = {
        phoneNum : $("#phoneNum1").val() != "" ? $("#phoneNum1").val() : null,
        courierNum : $("#courierNum1").val() != "" ? $("#courierNum1").val() : null,
        orderDate : $("#orderDate").val() != "" ? $("#orderDate").val() : null,
        name : $("#name1").val() != "" ? $("#name1").val() : null,
        goodsName : $("#goodsName1").val != "" ? $("#goodsName1").val() : null,
        style :  $("#style1").val() != "" ?  $("#style1").val() : null,
        clothSize : $("#clothSize1").val() != "" ? $("#clothSize1").val() : null,
        address : $("#address1").val() != "" ? $("#address1").val() : null,
        cardType : $("#cardType1").val() != "" ? $("#cardType1").val() : null,
        recordDate :  $("#recordDate1").val() != "" ?  $("#recordDate1").val() : null,
        spreadUserName : $("#spreadUserName1").val() != "" ?  $("#spreadUserName1").val() : null,
        sheng : $("#cmbProvince").val() != "" ?  $("#cmbProvince").val() : null,
        shi : $("#cmbCity").val() != "" ?  $("#cmbCity").val() : null,
        qu : $("#cmbArea").val() != "" ?  $("#cmbArea").val() : null
    }
    if($("#id1").val() != null && $("#id1").val() != ""){
        param.id = $("#id1").val();
    }
    // var array = new Array();
    // array[0] = param;
    $.ajax({
        url:"order/save",
        type:"POST",
        data: param,
        success:function(result){
           if(200 != result.status){
               alert(result.info);
           }else{
               $("#editWindow").modal('hide');
               // $("#queryForm")[0].reset();
               $("#name1").val("");
               $("#courierNum").val("");
               $("#phoneNum").val("");
               $("#name").val("");
               $("#courierNum1").val("");
               $("#phoneNum1").val("");
               $("#startDate").val("");
               $("#endDate").val("");
               $("#endDate1").val("");
               $("#startDate1").val("");
               $("#goodsName1").val("");
               $("#style1").val("");
               $("#clothSize1").val("");
               $("#address1").val("");
               $("#cardType1").val("");
               $("#spreadUserName1").val("");
               $("#recordDate1").val("");
               $("#recordDate").val("");
               $("#cmbProvince").val("");
               $("#cmbCity").val("");
               $("#cmbArea").val("");


               $('#result-table').bootstrapTable("refresh");
           }
        }
    });
}
function deleteData(node){
    var r=confirm("删除不可恢复,确定删除?")
    if (r!=true){
        return;
    }
    var idNode= $(node).find("span");
    var id = idNode.text();
    $.ajax({
        url:"order/delete/" + id,
        type:"GET",
        data: null,
        success:function(result){

            if(200 != result.status){
                alert(result.info);
            }else{
                // $("#queryForm")[0].reset();
                $("#startDate").val("");
                $("#endDate").val("");
                $("#endDate1").val("");
                $("#startDate1").val("");
                $("#name1").val("");
                $("#courierNum").val("");
                $("#phoneNum").val("");
                $("#courierNum1").val("");
                $("#phoneNum1").val("");
                $("#goodsName1").val("");
                $("#style1").val("");
                $("#clothSize1").val("");
                $("#address1").val("");
                $("#cardType1").val("");
                $("#spreadUserName1").val("");
                $("#recordDate1").val("");
                $("#recordDate").val("");
                $("#cmbProvince").val("");
                $("#cmbCity").val("");
                $("#cmbArea").val("");
                $('#result-table').bootstrapTable("refresh");

            }
        }
    });
}

function getCurrentTime(){
    Date.prototype.format = function(format) {
        var date = {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "h+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "q+": Math.floor((this.getMonth() + 3) / 3),
            "S+": this.getMilliseconds()
        };
        if (/(y+)/i.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
        }
        for (var k in date) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1
                    ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
            }
        }
        return format;
    }
    var newDate = new Date();
    return newDate.format('yyyy-MM-dd h:m:s');
}

function query(){
    // $('#result-table').bootstrapTable('refreshOptions',{pageNumber:1});
    searchFlag = true;
    $('#result-table').bootstrapTable("refresh");
    searchFlag = false;

}

function importOrder(){
    $("#importWindow").modal('show');
}
function exportOrder(){
    var query = queryParams();
    var url = "orderExport?phoneNum=" + query.phoneNum
        +"&courierNum=" + query.courierNum + "&startDate=" + query.startDate
        +"&endDate=" + query.endDate + "&spreadUserName=" + query.spreadUserName
        +"&goodsName=" + query.goodsName + "&page=0&size=3";
    window.open(url);
    // $.ajax({
    //     dataType: "json",
    //     traditional:true,//这使json格式的字符不会被转码
    //     data: {"order":JSON.stringify(query)},
    //     type: "POST",
    //     url: url,
    //     success : function (data) {
    //
    //     },
    //     error : function (data){
    //         alert("导出出错!");
    //     }
    // });

}

function deleteInBatch(){
    var a= $("#result-table").bootstrapTable('getSelections');
    if(a.length<=0){
        alert("请选中一个复选框再进行删除操作");
    }else{
       $.each(a,function(index,data){
           console.log(data);
           delete data.checkbox;
       })
        console.log(a);
        var json=JSON.stringify(a);
        var url="order/deleteInBatch";
        $.ajax({
            dataType: "json",
            traditional:true,//这使json格式的字符不会被转码
            data: {"orderList": json},
            type: "post",
            url: url,
            success : function (data) {
                // alert(data.info);
                searchFlag = true;
                $("#startDate").val("");
                $("#endDate").val("");
                $("#endDate1").val("");
                $("#startDate1").val("");
                $("#name1").val("");
                $("#courierNum").val("");
                $("#phoneNum").val("");
                $("#courierNum1").val("");
                $("#phoneNum1").val("");
                $("#goodsName1").val("");
                $("#style1").val("");
                $("#clothSize1").val("");
                $("#address1").val("");
                $("#cardType1").val("");
                $("#spreadUserName1").val("");
                $("#recordDate1").val("");
                $("#recordDate").val("");
                $("#cmbProvince").val("");
                $("#cmbCity").val("");
                $("#cmbArea").val("");
                $('#result-table').bootstrapTable("refresh");
                searchFlag = false;
            },
            error : function (data){
                alert("删除出错!");
            }
        });
    }
}