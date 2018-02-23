$(function () {
    init();
});

var init = function () {
    $.ajax({
        url: '/user/list',
        dataType: "json",
        type: "post",
        success: function (_data) {
            if (_data && _data.code == 0) {
                $("#user_table").bootstrapTable({
                    columns: [{
                        field: 'checkbox',
                        checkbox: true
                    }, {
                        field: 'userId',
                        title: '登录名'
                    }, {
                        field: 'userName',
                        title: '用户名'
                    }, {
                        field: 'sex',
                        title: '性别',
                        formatter: sexFormatter
                    }, {
                        field: 'mobile',
                        title: '手机'
                    }, {
                        field: 'roleid',
                        title: '角色',
                        formatter: roleFormatter
                    }, {
                        field: 'operate',
                        title: '操作',
                        formatter: operateFormatter
                    }],
                    data: _data.data,
                    pagination: true,
                    pageNumber: 1,
                    pageSize: 10,
                    pageList: [10]
                });
            }
        }
    });
}

function refreshTable() {
    $.ajax({
        url: '/user/list',
        dataType: "json",
        type: "post",
        success: function (_data) {
            if (_data && _data.code == 0) {
                $("#user_table").bootstrapTable('removeAll');
                $("#user_table").bootstrapTable('append', _data.data);
            }
        }
    });
}

function operateFormatter(value, row, index) {
    if(row.userId == 'admin')
        return '';
    else
        return [
            '<a onclick="edit(\'' + row.userId + '\')" type="button" class="btn btn-xs btn-info" style="margin-right:15px;"><i class=\'ace-icon fa fa-pencil-square-o\'></i></a>',
            '<a onclick="del(\'' + row.userId + '\')" type="button" class="btn btn-xs btn-danger" style="margin-right:15px;"><i class=\'ace-icon fa fa-trash-o\'></i></a>',
            '<a onclick="reset(\'' + row.userId + '\')" type="button" class="btn btn-xs btn-danger" style="margin-right:15px;"><i class=\'ace-icon fa fa-exchange\'></i></a>',
        ].join('');
}

function roleFormatter(value, row, index) {
    return row.role.roleName;
}
function sexFormatter(value, row, index) {
    if(value == 0)
        return '女';
    else
        return '男';
}


function areaFormatter(value, row, index) {
    return row.area.adnm;
}

function edit(userid) {
    $.ajax({
        url: "/user/getUserById",
        dataType: "json",
        type: "post",
        data: {userId: userid},
        success: function (_data) {
            if (_data && _data.code == 0) {
                var user = _data.data.user;
                var roleList = _data.data.roleList;
                $("#user_edit_view").find("input[name='userId']").val(user.userId);
                $("#user_edit_view").find("input[name='userName']").val(user.userName);
                $("#user_edit_view").find("input[name='mobile']").val(user.mobile);
                $("#user_edit_view").find("input[name='adcd']").val(user.area.adnm);
                $("#user_edit_view").find("input[name='adcd']").attr("data",user.area.adcd);
                var $role = $($("#user_edit_view").find("select[name='role']")[0]);
                $role.empty();
                for (var i = 0; i < roleList.length; i++) {
                    if (roleList[i].roleId == user.role.roleId)
                        $role.append("<option value='" + roleList[i].roleId + "' selected ='selected'>" + roleList[i].roleName + "</option>");
                    else
                        $role.append("<option value='" + roleList[i].roleId + "'>" + roleList[i].roleName + "</option>");
                }
                var $sex = $($("#user_edit_view").find("select[name='sex']")[0]);
                $sex.empty();
                if (user.sex == 1) {
                    $sex.append("<option value='1' selected ='selected'>男</option>");
                    $sex.append("<option value='0'>女</option>");
                } else if (user.sex == 0) {
                    $sex.append("<option value='1'>男</option>");
                    $sex.append("<option value='0' selected ='selected'>女</option>");
                } else {
                    $sex.append("<option value='1'>男</option>");
                    $sex.append("<option value='0'>女</option>");
                }
                var _layer = layer.open({
                    title: "用户信息",
                    type: 1,
                    area: ['400px', '500px'],
                    content: $("#user_edit_view")
                });
                $(".selectpicker").selectpicker('refresh');
                $("#user_edit_view").find("a").unbind('click').bind('click', function () {
                    var _type = $(this).attr('name');
                    if (_type == 'save') {
                        user.userName = $("#user_edit_view").find("input[name='userName']").val();
                        user.mobile = $("#user_edit_view").find("input[name='mobile']").val();
                        user.sex = $("#user_edit_view").find("select[name='sex']").val();
                        user.role.roleId = $("#user_edit_view").find("select[name='role']").selectpicker('val');
                        user.area.adcd = $("#user_edit_view").find("input[name='adcd']").attr('data');
                        $.ajax({
                            url: "/user/update",
                            dataType: "json",
                            contentType: "application/json",
                            type: "post",
                            data: JSON.stringify(user),
                            success: function (_data) {
                                if (_data && _data.code == 0) {
                                    layer.msg("保存成功");
                                    refreshTable();
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

function del(userid) {
    layer.confirm('您确定要删除该用户吗?', {icon: 3, title: '提示'}, function (index) {
        $.ajax({
            url: "/user/delete",
            dataType: "json",
            type: "post",
            data: {userId: userid},
            success: function (_data) {
                if (_data && _data.code == 0) {
                    layer.msg("删除成功");
                    refreshTable();
                } else {
                    layer.msg("删除失败," + _data.message);
                }
                layer.close(index);
            }
        });
    });
}

function reset(userId) {
    layer.confirm('您确定要重置该用户的密码吗?', {icon: 3, title: '提示'}, function (index) {
        $.ajax({
            url: "/user/reset/password",
            dataType: "json",
            type: "post",
            data: {userId:userId},
            success: function (_data) {
                if (_data && _data.code == 0) {
                    layer.msg("重置成功,密码为[123456]");
                    refreshTable();
                } else {
                    layer.msg("重置失败," + _data.message);
                }
                layer.close(index);
            }
        });
    });
}

function deleteSelectUser() {
    var users = $("#user_table").bootstrapTable('getAllSelections');
    if (!users || users.length == 0) {
        layer.msg("请选择您要删除的用户");
        return;
    } else {
        var userIds = "";
        users.forEach(function (user) {
            userIds += user.userid + ",";
        })
        layer.confirm('您确定要删除选择的用户吗?', {icon: 3, title: '提示'}, function (index) {
            $.ajax({
                url: "/user/deleteUsers",
                dataType: "json",
                type: "post",
                data: {userIds: userIds},
                success: function (_data) {
                    if (_data && _data.code == 0) {
                        layer.msg("删除成功");
                        refreshTable();
                    } else {
                        layer.msg("删除失败," + _data.message);
                    }
                    layer.close(index);
                }
            });
        });
    }
}

function addUser() {
    var _layer = layer.open({
        title: "新增用户",
        type: 1,
        area: ['400px', '500px'],
        content: $("#user_add_view")
    });
    $("#user_add_view").find("a").unbind('click').bind('click', function () {
        var _type = $(this).attr('name');
        if (_type == 'save') {
            var user = {role:{},area:{}};
            user.userId = $("#user_add_view").find("input[name='userId']").val();
            user.userName = $("#user_add_view").find("input[name='userName']").val();
            user.password = $("#user_add_view").find("input[name='password']").val();
            user.mobile = $("#user_add_view").find("input[name='mobile']").val();
            user.sex = $("#user_add_view").find("select[name='sex']").val();
            user.role.roleId = $("#user_add_view").find("select[name='role']").selectpicker('val');

            var pass = true;
            if(user.mobile == null||user.mobile == ''){
                layer.tips("请填写手机号","input[name='mobile']",{tipsMore: true});
                pass = false;
            }
            if(user.userId == null||user.userId == ''){
                layer.tips("请填写登录名","input[name='userId']",{tipsMore: true});
                pass = false;
            }else{
                $.ajax({
                    url: "/user/getUserById",
                    dataType: "json",
                    type: "post",
                    data: {userId:user.userId},
                    success: function (_data) {
                        if(_data&&_data.data&&_data.data.user){
                            layer.tips("登录名已存在","input[name='userId']",{tipsMore: true});
                            pass = false;
                        }
                        if(user.userName == null||user.userName == ''){
                            layer.tips("请填写用户姓名","input[name='userName']",{tipsMore: true});
                            pass = false;
                        }
                        if(user.password == null||user.password == ''){
                            layer.tips("请填写登录密码","input[name='password']",{tipsMore: true});
                            pass = false;
                        }
                        if(!pass)
                            return;
                        $.ajax({
                            url: "/user/save",
                            dataType: "json",
                            contentType: "application/json",
                            type: "post",
                            data: JSON.stringify(user),
                            success: function (_data) {
                                if (_data && _data.code == 0) {
                                    layer.msg("保存成功");
                                    refreshTable();
                                    layer.close(_layer);
                                } else {
                                    layer.msg("保存失败," + _data.message);
                                }
                            }
                        });
                    }
                });
            }
        } else if (_type == 'cancel') {
            layer.close(_layer);
        }
    })
}