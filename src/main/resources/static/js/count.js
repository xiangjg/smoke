$(function () {
    $('#expert_table').bootstrapTable({
        columns: columns,
        pageSize: 20,
        pageList: [10, 25, 50, 100],
        pagination: true
    });
    $("#check").click(function () {
        queryData();
    });
    $("#export").click(function () {
        var url = '/expert/count/export',first = false;
        if($("#unitName").val()){
            if(!first)
                url+= '?unitName='+$("#unitName").val();
            else
                url+= '&unitName='+$("#unitName").val();
            first = true;
        }
        if($("#expName").val()){
            if(!first)
                url+= '?expName='+$("#expName").val();
            else
                url+= '&expName='+$("#expName").val();
            first = true;
        }
        if($("#reviewType").val()){
            if(!first)
                url+= '?reviewType='+$("#reviewType").val();
            else
                url+= '&reviewType='+$("#reviewType").val();
            first = true;
        }
        if($("#start_time").val()){
            if(!first)
                url+= '?stTime='+$("#start_time").val();
            else
                url+= '&stTime='+$("#start_time").val();
            first = true;
        }
        if($("#end_time").val()){
            if(!first)
                url+= '?eTime='+$("#end_time").val();
            else
                url+= '&eTime='+$("#end_time").val();
        }
        window.location.href = url;
    });

    queryData();
});

var queryData = function () {
    var index = layer.load(1);
    var param = {};
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
        url: '/expert/count/query',
        dataType: "json",
        type: "post",
        data: param,
        success:function (_data) {
            if(_data&&_data.code==0){
                layer.close(index);
                $("#expert_table").bootstrapTable('destroy');
                $('#expert_table').bootstrapTable({
                    columns: columns,
                    pageSize: 10,
                    pageList: [10, 25, 50, 100],
                    pagination: true,
                    data:_data.data
                });
            }else{
                layer.msg(_data.message);
            }
        }
    });
}

var columns = [
    {
        field: 'no',
        title: '序号',
        formatter: noFormatter
    }, {
        field: 'unit',
        title: '单位'
    }, {
        field: 'name',
        title: '姓名'
    }, {
        field: 'num',
        title: '评审次数'
    }, {
        field: 'cost',
        title: '评审费用(元)'
    }
];

function noFormatter(value, row, index) {
    return index + 1;
}