$(function () {
    init();
});

var init = function () {
    $.ajax({
        url:'/role/list',
        dataType:"json",
        type:"post",
        success:function (_data) {
            if(_data&&_data.code == 0){
                $("#role_table").bootstrapTable({
                    columns: [{
                        field: 'checkbox',
                        checkbox:true
                    },{
                        field: 'roleId',
                        title: '角色编码'
                    }, {
                        field: 'roleName',
                        title: '角色名称'
                    }, {
                        field: 'operate',
                        title: '操作',
                        formatter: operateFormatter
                    }],
                    data: _data.data,
                    pagination:true,
                    pageNumber:1,
                    pageSize:10,
                    pageList:[10]
                });
            }
        }
    });
}

function refreshTable() {
    $.ajax({
        url:'/role/list',
        dataType:"json",
        type:"post",
        success:function (_data) {
            if(_data&&_data.code == 0){
                $("#role_table").bootstrapTable('removeAll');
                $("#role_table").bootstrapTable('append',_data.data);
            }
        }
    });
}
function operateFormatter(value, row, index) {
    if(row.roleId == 1){
        return [
            '<button onclick="menuInfo('+row.roleId+')" type="button" class="btn btn-minier btn-primary" style="margin-right:15px;">菜单权限</button>'
        ].join('');
    }else{
        return [
            '<button onclick="menuInfo('+row.roleId+')" type="button" class="btn btn-minier btn-primary" style="margin-right:15px;">菜单权限</button>',
            '<a onclick="edit('+row.roleId+')" type="button" class="btn btn-minier btn-info" style="margin-right:15px;"><i class=\'ace-icon fa fa-pencil-square-o\'></i></a>',
            '<a onclick="del('+row.roleId+')" type="button" class="btn btn-minier btn-danger" style="margin-right:15px;"><i class=\'ace-icon fa fa-trash-o\'></i></a>'
        ].join('');
    }

}
function menuInfo(roleid) {
    $.ajax({
        url:"/role/getRole",
        dataType:"json",
        type:"post",
        data:{roleId:roleid},
        success:function (_data) {
            if(_data&&_data.code==0){
                showMenuView(_data.data);
            }
        }
    });
}
function edit(roleid) {
    $.ajax({
        url:"/role/getRole",
        dataType:"json",
        type:"post",
        data:{roleId:roleid},
        success:function (_data) {
            if(_data&&_data.code==0){
                var role = _data.data;
                $("#role_edit_view").find("input[name='roleName']").val(role.roleName);
                var _layer = layer.open({
                    title: "角色信息",
                    type:1,
                    area:['300px','400px'],
                    content: $("#role_edit_view")
                });
                $("#role_edit_view").find("a").unbind('click').bind('click',function () {
                    var _type = $(this).attr('name');
                    if(_type=='save'){
                        role.roleName = $("#role_edit_view").find("input[name='roleName']").val();
                        $.ajax({
                            url:"/role/update",
                            dataType:"json",
                            contentType:"application/json",
                            type:"post",
                            data:JSON.stringify(role),
                            success:function (_data) {
                                if(_data&&_data.code==0){
                                    layer.msg("保存成功");
                                    refreshTable();
                                    layer.close(_layer);
                                }else{
                                    layer.msg("保存失败,"+_data.message);
                                }
                            }
                        });
                    }else if(_type=='cancel'){
                        layer.close(_layer);
                    }
                })
            }
        }
    });
}
function del(roleid) {
    layer.confirm('您确定要删除该角色吗?',{icon: 3, title:'提示'}, function(index){
        $.ajax({
            url:"/role/delete",
            dataType:"json",
            type:"post",
            data:{roleId:roleid},
            success:function (_data) {
                if(_data&&_data.code==0){
                    layer.msg("删除成功");
                    refreshTable();
                }else{
                    layer.msg("删除失败,"+_data.message);
                }
                layer.close(index);
            }
        });
    });
}
var showMenuView = function (role) {
    if(role){
        layerId = new Date().getTime()-1;
        layer.open({
            title: "菜单授权",
            type:1,
            btn:['确定'],
            area:['300px','450px'],
            yes:function (index) {
                var checkeds = $.fn.zTree.getZTreeObj("_ztree").getCheckedNodes(true);
                if(checkeds){
                    var rights = new Array();
                    checkeds.forEach(function (val) {
                        if(val.checked)
                            rights.push(val.id);
                    });
                    role.mis.forEach(function (mi) {
                        if(rights.indexOf(mi.menuId)>-1)
                            mi.have = true;
                        else
                            mi.have = false;
                    });
                    $.ajax({
                        url:"/role/saveRight",
                        dataType:"json",
                        contentType:"application/json",
                        type:"post",
                        data:JSON.stringify(role),
                        success:function (_data) {
                            console.log(_data)
                        }
                    })
                }
                layer.close(index);
            },
            content: $(".edit_menu_right")//'<div id="'+layerId+'"><div style="text-align: center"><input type="text"></div><div class="tree-content"><div id="'+layerId+'_ztree'+'" class="ztree"></div></div></div>'
        });
    }
    var setting = {
        check:{
            enable:true
        },
        edit:{
            enable:true,
            showRemoveBtn: false,
            showRenameBtn: false,
            drag:{
                isCopy:false,
                isMove:false
            }
        },
        data:{
            simpleData:{
                enable:true
            }
        },
        callback: {
            onClick:function (_data) {
                console.log(_data);
            },
        }
    }
    var treeData =  buildTree(role.mis);
    $.fn.zTree.init($("#_ztree"), setting, treeData);
}

function buildTree(data1) {
    var data = [];
    var root = "所有菜单";
    function walk(nodes, data) {
        if (!nodes) {
            return;
        }
        $.each(nodes, function(id, node) {
            var obj = {
                id :node.menuId,
                name : node.menuName != null ? node.menuName : root,
                pId : node.parentId != null ? node.parentId : root,
                checked:node.have!= null ? node.have : false
            };
            data.push(obj);
        });
    }

    walk(data1, data);
    return data;
}

function deleteSelectUser() {
    var roles = $("#role_table").bootstrapTable('getAllSelections');
    if(!roles||roles.length==0){
        layer.msg("请选择您要删除的角色");
        return;
    }else{
        var roleIds = "";
        roles.forEach(function (role) {
            roleIds += role.roleid + ",";
        })
        layer.confirm('您确定要删除选择的角色吗?',{icon: 3, title:'提示'}, function(index){
            $.ajax({
                url:"/role/deleteRoles",
                dataType:"json",
                type:"post",
                data:{roleIds:roleIds},
                success:function (_data) {
                    if(_data&&_data.code==0){
                        layer.msg("删除成功");
                        refreshTable();
                    }else{
                        layer.msg("删除失败,"+_data.message);
                    }
                    layer.close(index);
                }
            });
        });
    }
}

function addRole() {
    var _layer = layer.open({
        title: "新增角色",
        type:1,
        area:['300px','200px'],
        content: $("#role_add_view")
    });
    $("#role_add_view").find("a").unbind('click').bind('click',function () {
        var _type = $(this).attr('name');
        if(_type=='save'){
            var role = {};
            role.roleName = $("#role_add_view").find("input[name='roleName']").val();
            $.ajax({
                url:"/role/save",
                dataType:"json",
                contentType:"application/json",
                type:"post",
                data:JSON.stringify(role),
                success:function (_data) {
                    if(_data&&_data.code==0){
                        layer.msg("保存成功");
                        refreshTable();
                        layer.close(_layer);
                    }else{
                        layer.msg("保存失败,"+_data.message);
                    }
                }
            });
        }else if(_type=='cancel'){
            layer.close(_layer);
        }
    })
}