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
            sum: {
                validators: {
                    notEmpty: {
                        message: '库存不能为空'
                    },
                    regexp: {
                        regexp: /^[0-9]*$/,
                        message: '库存必须为数字组成'
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

    var url = "stock";
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
                field : 'goodsName',
                title : '商品名称',
                align : 'center',
                valign : 'middle',
                sortable : false
            },{
                field : 'styleName',
                title : '款式名称',
                align : 'center',
                valign : 'middle',
                sortable : false
            },{
                field : 'clothSizeName',
                title : '尺码名称',
                align : 'center',
                valign : 'middle',
                sortable : false
            },{
                field : 'sum',
                title : '库存',
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
        goodsName : $("#goodsName").val(),
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
    initCombobox();
    initValidator();
    var idNode= $(node).find("span");
    var id = idNode.text();
    $.ajax({
        url:"stock/" + id,
        type:"GET",
        data: null,
        success:function(result){

            if(200 != result.status){
                alert(result.info);
            }else{
                $("#id1").val(result.data.id);
                $("#goodsId").val(result.data.goods.id);
                $("#styleId").val(result.data.style.id);
                $("#clothSizeId").val(result.data.clothSize.id);
                $("#sum").val(result.data.sum);
                $("#clothSizeId").attr("disabled", true);
                $("#styleId").attr("disabled", true);
                $("#goodsId").attr("disabled", true);

                $("#editWindow").modal('show');
            }
        }
    });

}

function clearEditForm(){
    $("#goodsName1").val(null);
    $("#sum").val(null);
    $("#styleName").val(null);
    $("#clothSizeName").val(null);
    $("#id1").val(null);
}
function add() {
    clearEditForm();
    initCombobox();
    initValidator();
    $("#editWindow").modal('show');
}
function initCombobox() {
    $.ajax({
        url:"goodsAll" ,
        type:"post",
        data: null,
        success:function(result){

            if(200 != result.status){
                alert(result.info);
            }else{
                var data = result.data;
                var html = ""
                $.each(data,function(index,goods){
                    var id = goods.id;
                    var name = goods.name;
                    html += "<option value='" + id + "'>";
                    html += name;
                    html += "</option>";
                })
                $("#goodsId").html(html);
            }
        }
    });
    $.ajax({
        url:"styleAll" ,
        type:"post",
        data: null,
        success:function(result){

            if(200 != result.status){
                alert(result.info);
            }else{
                var data = result.data;
                var html = ""
                $.each(data,function(index,style){
                    var id = style.id;
                    var name = style.name;
                    html += "<option value='" + id + "'>";
                    html += name;
                    html += "</option>";
                })
                $("#styleId").html(html);
            }
        }
    });
    $.ajax({
        url:"clothSizeAll" ,
        type:"post",
        data: null,
        success:function(result){

            if(200 != result.status){
                alert(result.info);
            }else{
                var data = result.data;
                var html = ""
                $.each(data,function(index,clothSize){
                    var id = clothSize.id;
                    var name = clothSize.name;
                    html += "<option value='" + id + "'>";
                    html += name;
                    html += "</option>";
                })
                $("#clothSizeId").html(html);
            }
        }
    });
}

function save(){
    var validate = $('#editForm').bootstrapValidator('validate');
    var isValid = $("#editForm").data('bootstrapValidator').isValid();
    if(!isValid){
        return;
    }
    var param = {
        sum : $("#sum").val() != "" ? $("#sum").val() : null,
        goodsId : $("#goodsId").val(),
        clothSizeId : $("#clothSizeId").val(),
        styleId : $("#styleId").val()
    }
    if($("#id1").val() != null && $("#id1").val() != ""){
        param.id = $("#id1").val();
    }
    // var array = new Array();
    // array[0] = param;
    $.ajax({
        url:"stock/save",
        type:"POST",
        data: param,
        success:function(result){
           if(200 != result.status){
               alert(result.info);
           }else{
               $("#editWindow").modal('hide');
               // $("#queryForm")[0].reset();
               $("#goodsName").val("");
               $("#goodsId").val("");
               $("#clothSizeId").val("");
               $("#styleId").val("");
               $("#goodsId").attr("disabled", false);

               $("#styleId").attr("disabled", false);

               $("#clothSizeId").attr("disabled", false);

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
        url:"stock/delete/" + id,
        type:"GET",
        data: null,
        success:function(result){

            if(200 != result.status){
                alert(result.info);
            }else{
                // $("#queryForm")[0].reset();
                $("#goodsName").val("");
                $("#goodsId").val("");
                $("#clothSizeId").val("");
                $("#styleId").val("");
                $("#goodsId").attr("disabled", false);

                $("#styleId").attr("disabled", false);

                $("#clothSizeId").attr("disabled", false);
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