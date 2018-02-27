$(function () {
    $('#id-input-file-1').ace_file_input({
        no_file:'没有选择文件',
        btn_choose:'选择',
        btn_change:'更改',
        //icon_remove:null,
        droppable:false,
        onchange:null,
        thumbnail:false, //| true | large
        //whitelist:'gif|png|jpg|jpeg'//黑白名单并没有起作用
        allowExt: ['xls','xlsx']    //该属性只是对文件后缀的控制
    });
});

var columns = [
    {
        field:'name',
        title:'序号'
    },{
        field:'name',
        title:'单位'
    },{
        field:'name',
        title:'姓名'
    },{
        field:'name',
        title:'评审次数'
    },{
        field:'name',
        title:'评审费用(元)'
    }
];

$('#expert_table').bootstrapTable({
    columns:columns,
    pageSize: 10,
    pageList: [10, 25, 50, 100],
    pagination: true,
});