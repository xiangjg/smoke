$(function () {
    initFileInput();

    queryData();
    $("#check").click(function () {
        queryData();
    });
});

var queryData = function () {
    var index = layer.load(1);
    var param = {};
    if ($("#proName").val())
        param.proName = $("#proName").val();
    if ($("#unitName").val())
        param.unitName = $("#unitName").val();
    if ($("#expName").val())
        param.expName = $("#expName").val();
    if ($("#reviewType").val())
        param.reviewType = $("#reviewType").val();
    if ($("#expType").val())
        param.expType = $("#expType").val();
    if ($("#start_time").val())
        param.stTime = $("#start_time").val();
    if ($("#end_time").val())
        param.eTime = $("#end_time").val();
    $.ajax({
        url: '/expert/query',
        dataType: "json",
        type: "post",
        data: param,
        success: function (_data) {
            if (_data && _data.code == 0) {
                layer.close(index);
                $("#expert_table").bootstrapTable('destroy');
                $('#expert_table').bootstrapTable({
                    columns: columns,
                    pageSize: 10,
                    pageList: [10, 25, 50, 100],
                    pagination: true,
                    data: _data.data
                });
            } else {
                layer.msg(_data.message);
            }
        }
    });
}

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
                            pageSize: 10,
                            pageList: [10, 25, 50, 100],
                            pagination: true,
                            data: data.data
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
    var data = $('#dataTable').bootstrapTable('getData');
    if (!data || data.length == 0) {
        layer.msg('没有需要保存的数据');
        return;
    } else {
        var isPass = true;
        //验证是否存在后台校验错误数据
        for (var i = 0; i < data.length; i++) {
            if (data[i].remark && data[i].remark.length > 0) {
                layer.msg("请根据备注错误更正后重新上传提交");
                return;
            }
        }
        //验证重复数据
        for (var i = 0; i < data.length; i++) {
            for (var j = 0; j < data.length; j++) {
                if (i != j && data[i].proName == data[j].proName && data[i].reviewTime == data[j].reviewTime && data[i].expNameSkill == data[j].expNameSkill
                    && data[i].expUnitSkill == data[j].expUnitSkill && data[i].reviewCost == data[j].reviewCost && data[i].expType == data[j].expType) {
                    isPass = false;
                    var num1 = i + 1, num2 = j + 1;
                    layer.msg('第【' + num1 + '】条数据与第【' + num2 + '】条数据重复，请问是否继续保存该数据?', {
                        time: 0 //不自动关闭
                        , btn: ['是', '否']
                        , yes: function (index) {
                            layer.close(index);
                            saveData(data);
                        }, btn2: function (index) {
                            layer.close(index);
                            return;
                        }
                    });
                }
            }
        }

        if (isPass)
            saveData(data);
    }
}

var saveData = function (data) {
    var index = layer.load(1);
    $.ajax({
        url: '/expert/save',
        dataType: "json",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (_data) {
            if (_data && _data.code == 0) {
                layer.close(index);
                layer.msg(_data.message);
                $("#dataTable").bootstrapTable('destroy');
            } else {
                layer.msg(_data.message);
            }
        }
    });
}

var uploadColumns = [
    {
        field: 'no',
        title: '序号',
        formatter: noFormatter,
        cellStyle: cellStyle
    }, {
        field: 'proName',
        title: '项目名称',
        cellStyle: cellStyle
    }, {
        field: 'reviewType',
        title: '评审类别',
        formatter: typeFormatter,
        cellStyle: cellStyle
    }, {
        field: 'reviewTime',
        title: '评审时间',
        formatter: dateFormatter,
        cellStyle: cellStyle
    }, {
        field: 'expNameSkill',
        title: '技术专家姓名',
        cellStyle: cellStyle
    }, {
        field: 'expUnitSkill',
        title: '技术专家单位',
        cellStyle: cellStyle
    }, {
        field: 'expType',
        title: '专家类别',
        formatter: typeExpFormatter,
        cellStyle: cellStyle
    }, {
        field: 'reviewCost',
        title: '评审费用(元)',
        cellStyle: cellStyle
    }, {
        field: 'remark',
        title: '备注',
        cellStyle: cellStyle
    }
];

function noFormatter(value, row, index) {
    return index + 1;
}

function dateFormatter(value, row, index) {
    if (value)
        return moment(value).format('YYYY-MM-DD');
    else
        return '-';
}

function typeFormatter(value, row, index) {
    var _val = "";
    switch (parseInt(value)) {
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
    if (row.remark && row.remark.length > 0)
        return {css: {"background-color": "rgba(255, 100, 48, 0.54)"}};
    else
        return {css: {"background-color": "rgba(255, 255, 255, 0.33)"}};
}


var columns = [{
    field: 'checkbox',
    checkbox: true
}, {
    field: 'no',
    title: '序号',
    formatter: noFormatter,
    cellStyle: cellStyle
}, {
    field: 'proName',
    title: '项目名称',
    cellStyle: cellStyle
}, {
    field: 'reviewType',
    title: '评审类别',
    formatter: typeFormatter,
    cellStyle: cellStyle
}, {
    field: 'reviewTime',
    title: '评审时间',
    formatter: dateFormatter,
    cellStyle: cellStyle
}, {
    field: 'expNameSkill',
    title: '专家姓名',
    cellStyle: cellStyle
}, {
    field: 'expUnitSkill',
    title: '专家单位',
    cellStyle: cellStyle
}, {
    field: 'expType',
    title: '专家类别',
    formatter: typeExpFormatter,
    cellStyle: cellStyle
}, {
    field: 'reviewCost',
    title: '评审费用(元)',
    cellStyle: cellStyle
}, {
    field: 'operate',
    title: '操作',
    formatter: operateFormatter,
    cellStyle: cellStyle
}, {
    field: 'remark',
    title: '备注',
    cellStyle: cellStyle
}
];

function operateFormatter(value, row, index) {
    return [
        '<a onclick="edit(\'' + row.id + '\')" type="button" class="btn btn-minier btn-info" style="margin-right:15px;" title="修改数据"><i class=\'ace-icon fa fa-pencil-square-o\'></i></a>',
        '<a onclick="del(\'' + row.id + '\')" type="button" class="btn btn-minier btn-danger" style="margin-right:15px;" title="删除数据"><i class=\'ace-icon fa fa-trash-o\'></i></a>'
    ].join('');
}

function typeExpFormatter(value, row, index) {
    var _val = "";
    switch (parseInt(value)) {
        case 1:
            _val = "技术专家";
            break
        case 2:
            _val = "经管专家";
            break;
        default:
            break;
    }
    return _val;
}

function edit(id) {
    $.ajax({
        url: "/expert/getById",
        dataType: "json",
        type: "post",
        data: {id: id},
        success: function (_data) {
            if (_data && _data.code == 0) {
                var smoke = _data.data;
                $("#expert_edit_view").find("input[name='proName']").val(smoke.proName);
                //$("#expert_edit_view").find("input[name='expUnitSkill']").val(smoke.expUnitSkill);
                $("#expert_edit_view").find("input[name='expNameSkill']").val(smoke.expNameSkill);
                $("#expert_edit_view").find("input[name='reviewCost']").val(smoke.reviewCost);
                var $expUnitSkill = $($("#expert_edit_view").find("select[name='expUnitSkill']")[0]);
                $expUnitSkill.empty();
                var unitNames = ['公司本部', '技术中心', '营销中心', '原料供应中心', '贵阳卷烟厂', '遵义卷烟厂', '毕节卷烟厂', '贵定卷烟厂', '铜仁卷烟厂', '兴义烟叶储运站'];
                $expUnitSkill.append("<option value=''>请选择单位名称</option>");
                unitNames.forEach(function (t) {
                    if (t == smoke.expUnitSkill)
                        $expUnitSkill.append("<option value='" + t + "' selected ='selected'>" + t + "</option>");
                    else
                        $expUnitSkill.append("<option value='" + t + "'>" + t + "</option>");
                });
                var $reviewType = $($("#expert_edit_view").find("select[name='reviewType']")[0]);
                $reviewType.empty();
                var reviewTypes = [{value:1,name:'立项评审'},{value:2,name:'检查评价'},{value:3,name:'变更评审'},{value:4,name:'撤销评审'},{value:5,name:'结题评审'}]
                reviewTypes.forEach(function (t) {
                    if (t.value == smoke.reviewType)
                        $reviewType.append("<option value='" + t.value + "' selected ='selected'>" + t.name + "</option>");
                    else
                        $reviewType.append("<option value='" + t.value + "'>" + t.name + "</option>");
                });
                var _layer = layer.open({
                    title: "修改信息",
                    type: 1,
                    area: ['400px', '500px'],
                    content: $("#expert_edit_view")
                });
                $("#expert_edit_view").find("a").unbind('click').bind('click', function () {
                    var _type = $(this).attr('name');
                    if (_type == 'save') {
                        smoke.proName = $("#expert_edit_view").find("input[name='proName']").val();
                        //smoke.expUnitSkill = $("#expert_edit_view").find("input[name='expUnitSkill']").val();
                        smoke.expNameSkill = $("#expert_edit_view").find("input[name='expNameSkill']").val();
                        smoke.reviewCost = $("#expert_edit_view").find("input[name='reviewCost']").val();
                        smoke.reviewType = $("#expert_edit_view").find("select[name='reviewType']").val();
                        smoke.expUnitSkill = $("#expert_edit_view").find("select[name='expUnitSkill']").val();
                        $.ajax({
                            url: "/expert/update",
                            dataType: "json",
                            contentType: "application/json",
                            type: "post",
                            data: JSON.stringify(smoke),
                            success: function (_data) {
                                if (_data && _data.code == 0) {
                                    layer.msg("保存成功");
                                    queryData();
                                    layer.close(_layer);
                                } else {
                                    layer.msg("保存失败," + _data.message);
                                }
                            }
                        });
                    } else if (_type == 'cancel') {
                        layer.close(_layer);
                    }
                })
            }
        }
    });
}

function del(id) {
    layer.confirm('您确定要删除该记录吗?', {icon: 3, title: '提示'}, function (index) {
        $.ajax({
            url: "/expert/delete",
            dataType: "json",
            type: "post",
            data: {id: id},
            success: function (_data) {
                if (_data && _data.code == 0) {
                    layer.msg("删除成功");
                    queryData();
                } else {
                    layer.msg("删除失败," + _data.message);
                }
                layer.close(index);
            }
        });
    });
}

function deleteSelectData() {
    var datas = $("#expert_table").bootstrapTable('getAllSelections');
    if (!datas || datas.length == 0) {
        layer.msg("请选择您要删除的记录");
        return;
    } else {
        var dataIds = "";
        datas.forEach(function (data) {
            dataIds += data.id + ",";
        })
        layer.confirm('您确定要删除选择的记录吗?', {icon: 3, title: '提示'}, function (index) {
            $.ajax({
                url: "/expert/deleteDataArr",
                dataType: "json",
                type: "post",
                data: {dataIds: dataIds},
                success: function (_data) {
                    if (_data && _data.code == 0) {
                        layer.msg("删除成功");
                        queryData();
                    } else {
                        layer.msg("删除失败," + _data.message);
                    }
                    layer.close(index);
                }
            });
        });
    }
}

$('#expert_table').bootstrapTable({
    columns: columns,
    pageSize: 10,
    pageList: [10, 25, 50, 100],
    pagination: true,
});