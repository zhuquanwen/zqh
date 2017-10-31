$(function(){
    initTable();
    initDate();
});

function initTable(){
    var url = "order";
    $('#result-table').bootstrapTable({
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        cache: false,
        striped: true,                              //是否显示行间隔色
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
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showExport: true,
        exportDataType: 'all',
        responseHandler: responseHandler,
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
                valign : 'middle'
                // ,
                // formatter : function (value, row, index){
                //     return new Date(value).format('yyyy-MM-dd hh:mm:ss');
                // }
            }, {
                field : 'recordDate',
                title : '记录时间',
                align : 'center',
                valign : 'middle'
                // ,
                // formatter : function (value, row, index){
                //     return new Date(value).format('yyyy-MM-dd hh:mm:ss');
                // }
            }]
    });
}

function initDate(){
    var start = {
        elem: '#startDate',
        format: 'YYYY-MM-DD hh:mm:ss',
        min: laydate.now(-7),
        max: laydate.now(),
        istime: true,
        istoday: false,
        choose: function (datas) {
            end.min = datas; //开始日选好后，重置结束日的最小日期
            end.start = datas //将结束日的初始值设定为开始日
        }
    };
    var end = {
        elem: '#endDate',
        format: 'YYYY-MM-DD hh:mm:ss',
        min: laydate.now(-7),
        max: laydate.now(),
        istime: true, //是否开启时间选择
        isclear: true, //是否显示清空
        istoday: true, //是否显示今天
        issure: true, //是否显示确认
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    laydate(start);
    laydate(end);
}



function queryParams(params) {
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