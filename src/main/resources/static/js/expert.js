$(function () {
    initFileInput();

    $("#check").click(function () {
        var index = layer.load(1);
        var param = {};
        if($("#proName").val())
            param.proName = $("#proName").val();
        if($("#unitName").val())
            param.unitName = $("#unitName").val();
        if($("#expName").val())
            param.expName = $("#expName").val();
        if($("#reviewType").val())
            param.reviewType = $("#reviewType").val();
        if($("#start_time").val())
            param.stTime = $("#start_time").val();
        if($("#end_time").val())
            param.eTime = $("#end_time").val();
        $.ajax({
            url: '/expert/query',
            dataType: "json",
            type: "post",
            data: param,
            success:function (_data) {
                if(_data&&_data.code==0){
                    layer.close(index);
                    $("#expert_table").bootstrapTable('destroy');
                    $('#expert_table').bootstrapTable({
                        columns: columns,
                        pageSize: 20,
                        pageList: [10, 25, 50, 100],
                        pagination: true,
                        data:_data.data
                    });
                }else{
                    layer.msg(_data.message);
                }
            }
        });
    });
});

var initFileInput = function () {
    $('#id-input-file-1').ace_file_input({
        no_file: '没有选择文件',
        btn_choose: '选择',
        btn_change: '更改',
        //icon_remove:null,
        droppable: false,
        onchange: null,
        thumbnail: false, //| true | large
        //whitelist:'gif|png|jpg|jpeg'//黑白名单并没有起作用
        allowExt: ['xls', 'xlsx']    //该属性只是对文件后缀的控制
    });
}

var myupload = function () {
    if ($("#id-input-file-1").val().length > 0) {
        var url = "/expert/upload";
        //加载层
        var index = layer.load(1);
        $.ajaxFileUpload
        (
            {
                url: url, //用于文件上传的服务器端请求地址
                type: 'post',
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'id-input-file-1', //文件上传域的ID
                dataType: 'json', //返回值类型 一般设置为json
                //data: { type:type }, //此参数非常严谨，写错一个引号都不行
                success: function (data, status)  //服务器成功响应处理函数
                {
                    $("#chooseFile").empty();
                    $("#chooseFile").append("<input type=\"file\" id=\"id-input-file-1\" name=\"file\" accept=\".xls,.xlsx\" />");
                    initFileInput();
                    layer.close(index);
                    if (data.code == 0) {
                        // var columns=columns1;
                        // columns.forEach(function (t) { t.align = 'center' })
                        // Table.loadData({tableId: 'dataTable', data: data.data, columns: columns})
                        $("#dataTable").bootstrapTable('destroy');
                        $('#dataTable').bootstrapTable({
                            columns: uploadColumns,
                            pageSize: 20,
                            pageList: [10, 25, 50, 100],
                            pagination: true,
                            data:data.data
                        });
                    } else {
                        layer.msg(data.message);
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    console.log(e);
                }
            }
        )
    }
    else {
        layer.msg("请选择要上传的文件", {time: 3 * 1000});
    }
}

function save() {
    var data = 	$('#dataTable').bootstrapTable('getData');
    if(!data||data.length==0){
        layer.msg('没有需要保存的数据');
        return;
    }else{
        for(var i=0;i<data.length;i++){
            if(data[i].remark&&data[i].remark.length>0){
                layer.msg("请根据备注错误更正后重新上传提交");
                return;
            }
        }
        var index = layer.load(1);
        $.ajax({
            url: '/expert/save',
            dataType: "json",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify(data),
            success:function (_data) {
                if(_data&&_data.code==0){
                    layer.close(index);
                    layer.msg(_data.message);
                    $("#dataTable").bootstrapTable('destroy');
                }else{
                    layer.msg(_data.message);
                }
            }
        });
    }

}

var uploadColumns = [
    {
        field: 'no',
        title: '序号',
        formatter: noFormatter,
        cellStyle:cellStyle
    }, {
        field: 'proName',
        title: '项目名称',
        cellStyle:cellStyle
    }, {
        field: 'reviewType',
        title: '评审类别',
        formatter: typeFormatter,
        cellStyle:cellStyle
    }, {
        field: 'reviewTime',
        title: '评审时间',
        formatter: dateFormatter,
        cellStyle:cellStyle
    }, {
        field: 'expNameSkill',
        title: '技术专家姓名',
        cellStyle:cellStyle
    }, {
        field: 'expUnitSkill',
        title: '技术专家单位',
        cellStyle:cellStyle
    }, {
        field: 'expNameManage',
        title: '经管专家姓名',
        cellStyle:cellStyle
    }, {
        field: 'expUnitManage',
        title: '经管专家单位',
        cellStyle:cellStyle
    }, {
        field: 'reviewCost',
        title: '评审费用(元)',
        cellStyle:cellStyle
    }, {
        field: 'remark',
        title: '备注',
        cellStyle:cellStyle
    }
];

function noFormatter(value, row, index) {
    return index + 1;
}

function dateFormatter(value, row, index) {
    if(value)
        return moment(value).format('YYYY-MM-DD');
    else
        return '-';
}

function typeFormatter(value, row, index) {
    var _val = "";
    switch (value){
        case 1:
            _val = "立项评审";
            break
        case 2:
            _val = "检查评价";
            break
        case 3:
            _val = "变更评审";
            break
        case 4:
            _val = "撤销评审";
            break
        case 5:
            _val = "结题评审";
            break
        default:
            break;
    }
    return _val;
}

function cellStyle(value, row, index) {
    if(row.remark!=null)
        return { css: {"background-color": "rgba(255, 100, 48, 0.54)"}};
    else
        return { css: {"background-color": "rgba(255, 255, 255, 0.33)"}};
}


var columns = [
    {
        field: 'no',
        title: '序号',
        formatter: noFormatter
    }, {
        field: 'proName',
        title: '项目名称'
    }, {
        field: 'reviewType',
        title: '评审类别',
        formatter: typeFormatter
    }, {
        field: 'reviewTime',
        title: '评审时间',
        formatter: dateFormatter
    }, {
        field: 'expNameSkill',
        title: '技术专家姓名'
    }, {
        field: 'expUnitSkill',
        title: '技术专家单位'
    }, {
        field: 'expNameManage',
        title: '经管专家姓名'
    }, {
        field: 'expUnitManage',
        title: '经管专家单位'
    }, {
        field: 'reviewCost',
        title: '评审费用(元)'
    }
];

$('#expert_table').bootstrapTable({
    columns: columns,
    pageSize: 10,
    pageList: [10, 25, 50, 100],
    pagination: true,
});