var searchFlag = false;
$(function(){
    initTable();

    // initFileInput();
    // initImportModalHiden();
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
                message: '尺码名称验证失败',
                validators: {
                    notEmpty: {
                        message: '尺码名称不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 20,
                        message: '尺码名称长度必须在1到20位之间'
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

    var url = "clothSize";
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
                title : '尺码名称',
                align : 'center',
                valign : 'middle',
                sortable : true
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
        name : $("#name").val(),
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
        url:"clothSize/" + id,
        type:"GET",
        data: null,
        success:function(result){

            if(200 != result.status){
                alert(result.info);
            }else{
                if("默认" == result.data.name){
                    alert("默认的尺码不允许编辑");
                    return;
                }
                $("#name1").val(result.data.name);
                $("#id1").val(result.data.id);
                $("#editWindow").modal('show');
            }
        }
    });

}

function clearEditForm(){
    $("#name1").val(null);
    $("#id1").val(null);
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
        name : $("#name1").val() != "" ? $("#name1").val() : null

    }
    if($("#id1").val() != null && $("#id1").val() != ""){
        param.id = $("#id1").val();
    }
    // var array = new Array();
    // array[0] = param;
    $.ajax({
        url:"clothSize/save",
        type:"POST",
        data: param,
        success:function(result){
           if(200 != result.status){
               alert(result.info);
           }else{
               $("#editWindow").modal('hide');
               // $("#queryForm")[0].reset();
               $("#name1").val("");
               $("#name").val("");
               $("#descriptor").val("");
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
        url:"clothSize/delete/" + id,
        type:"GET",
        data: null,
        success:function(result){

            if(200 != result.status){
                alert(result.info);
            }else{
                // $("#queryForm")[0].reset();
                $("#name").val("");
                $("#name1").val("");
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

function deleteInBatch(){
    var r=confirm("删除不可恢复,确定删除?")
    if (r!=true){
        return;
    }
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
        var url="style/deleteInBatch";
        $.ajax({
            dataType: "json",
            traditional:true,//这使json格式的字符不会被转码
            data: {"orderList": json},
            type: "post",
            url: url,
            success : function (data) {
                // alert(data.info);
                searchFlag = true;
                $('#result-table').bootstrapTable("refresh");
                searchFlag = false;
            },
            error : function (data){
                alert("删除出错!");
            }
        });
    }
}