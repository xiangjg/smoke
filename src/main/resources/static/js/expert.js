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