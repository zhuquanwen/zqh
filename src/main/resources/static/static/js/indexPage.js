var searchFlag = false;
$(function(){
    initTable();
    initDate();
    initFileInput();
    initImportModalHiden();
});




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
        pageSize: 20,                       //每页的记录行数（*）
        pageList: [10, 20, 50, 100],        //可供选择的每页的行数（*）
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showExport: true,
        // exportDataType: 'all',
        responseHandler: responseHandler,
        onLoadSuccess: function(){

        },
        columns: [
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
                field : 'courierNum',
                title : '单号',
                align : 'center',
                valign : 'middle',
                sortable : true
            }, {
                field : 'name',
                title : '姓名',
                align : 'center',
                valign : 'middle',
                sortable : true
            }, {
                field : 'phoneNum',
                title : '手机',
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
                title : '记录时间',
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
   /* $('.form_datetime').datetimepicker({
        //language:  'fr',
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
        showMeridian: 1
    });*/

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
                $("#orderDate").val(result.data.orderDate);
                $("#name1").val(result.data.name);
                $("#courierNum1").val(result.data.courierNum);
                $("#phoneNum1").val(result.data.phoneNum);
                $("#id1").val(result.data.id);
                $("#editWindow").modal('show');
            }
        }
    });

}

function clearEditForm(){
    $("#orderDate").val(null);
    $("#name1").val(null);
    $("#courierNum1").val(null);
    $("#phoneNum1").val(null);

}
function add() {
    clearEditForm();
    $("#editWindow").modal('show');
}
function save(){
    var param = {
        phoneNum : $("#phoneNum1").val() != "" ? $("#phoneNum1").val() : null,
        courierNum : $("#courierNum1").val() != "" ? $("#courierNum1").val() : null,
        orderDate : $("#orderDate").val() != "" ? $("#orderDate").val() : null,
        name : $("#name1").val() != "" ? $("#name1").val() : null

    }
    if($("#id1").val() != null){
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
               $('#result-table').bootstrapTable("refresh");
           }
        }
    });
}
function deleteData(node){
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
