function parseJson(w) {
    return eval("(" + w + ")");
}

function getCookie(c_name) {
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=")
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1
            c_end = document.cookie.indexOf(";", c_start)
            if (c_end == -1) c_end = document.cookie.length
            return unescape(document.cookie.substring(c_start, c_end))
        }
    }
    return ""
}
var selfName;
var msg_to;
var object_type;
var msg_from;
var chartList = new Array();

var showCont = {};
ws = new WebSocket('ws://127.0.0.1:8080/InstantCommunication/WebChat');
ws.onopen = function ()//在websocket建立完成后发送身份认证 同时 服务器会返回当前用户信息
{
    var jessonid = getCookie("jessonid");//jessonid用来作为身份认证的凭证
    var username = getCookie("username");
    var groupname = getCookie("groupname");
    var content = {
        'username': username,
        'groupname': groupname,
        'jessonid': jessonid
    };
    var data = {
        'Type': 'Authenticate',
        'Content': content
    };
    ws.send(JSON.stringify(data));
};

/*
 window.onload = function() {
 chart.innerHTML="";
 var group_name="asdw";
 var group_name2="12314";
 chart.innerHTML+="<input  id="+group_name+" type='button' class='btn_grey' value="+group_name+" onClick='change(\""+group_name + "\",\"" +'group'+"\")'/>";
 chart.innerHTML+="<input type='button' class='btn_grey' value='×'	onClick='delChart(\""+group_name+"\")'/>";
 chart.innerHTML+="<input  id="+group_name2+" type='button' class='btn_grey' value="+group_name2+" onClick='change(\""+group_name2 + "\",\"" +'group'+"\")'/>";
 chart.innerHTML+="<input type='button' class='btn_grey' value='×12'	onClick='delChart(\""+group_name2+"\")'/>";

 alert(chart.innerHTML);
 }
 */


ws.onmessage = function (event) {
    var recvStr = event.data;//获取websocket的数据
    console.log("recvStr:" + recvStr);
    var jsonObject = parseJson(recvStr);//将字符串转换成json
    var Type = jsonObject["Type"];		//获取json的Type字段
    var Content = jsonObject["Content"];	//获取json的Content字段

    if (Type == "getMyIdentifyResult") {//获取我的用户名
        selfName = Content["username"];
        //document.getElementById("button_info").value = selfName;
    }
    else if (Type == "getAllUsersResult") {//获取当前所有用户信息  包括了在线和不在线的
        var html = "<input id='searchContent' type='text' size=50% value='' onKeyDown='if(event.keyCode==13 && event.ctrlKey){searchUser();}'/><input name='search_submit' type='button' class='btn_grey' value='搜索' onClick='searchUser()'/>";
        var num = parseInt(Content["amount"]);//获取元素数量字段
        for (var i = 0; i < num; i++) {
            var username = Content[i + ""]["username"];//获取用户名
            var userstate = Content[i + ""]["userstate"];//获取用户在线状态
            html = html + "<input type='button' class='" + userstate + "' value='" + username + "' onClick='showMsgToUser(\"" + username + "\")'/><br><br>";
        }
        mainList.innerHTML = html;
    }

    else if (Type == "getGroupsResult") {
        var html = "";
        var num = parseInt(Content["amount"]);//获取元素数量字段
        for (var i = 0; i < num; i++) {
            var groupname = Content[i + ""]["groupname"];//获取群组名
            html = html + "<li onClick='showMsgToGroup(\"" + groupname + "\")'/>"+"<img class='avatar' width='30' height='30' alt='webpack' src='img/3.jpg'><p class='name'>"+groupname+"</p></li>";
            /*html = html + "<input type='button' class='btn_grey' value='" + groupname + "' 	onClick='showMsgToGroup(\"" + groupname + "\")'/>";*/
            /*"<li onClick='showMsgToGroup(\"" + groupname + "\")'/>"+"<img class='avatar' width='30' height='30' alt='webpack' src='img/3.jpg'><p class='name'>"+groupname+"</p></li>"*/
        }
        mainList.innerHTML = html;
    }

    else if (Type == "getGroupHistoryMsgResult") {
        for (var i = 0; i < chartList.length; i++) {
            if (Content["name"] == chartList[i]["name"]) {
                var num = parseInt(Content["amount"]);
                for (var j = 0; j < num; j++) {
                    chartList[i]["html"] += "<font color='blue'>" + Content[j + ""]["from"] + ":</font><font color='blue'>" + Content[j + ""]["message_content"] + "</font>";
                }
                chartList[i]["extraHtml"] = "";
                member.innerHTML = chartList[i]["extraHtml"];
                msgContent.innerHTML = chartList[i]["html"];
            }
        }
    }
    else if (Type == "getUserHistoryMsgResult") {
        for (var i = 0; i < chartList.length; i++) {
            if (Content["name"] == chartList[i]["name"]) {
                var num = parseInt(Content["amount"]);
                for (var j = 0; j < num; j++) {
                    chartList[i]["html"] += "<font color='blue'>" + Content["name"] + ":</font><font color='blue'>" + Content[j + ""]["message_content"] + "</font><br>";
                }
                chartList[i]["extraHtml"] = "";
                member.innerHTML = chartList[i]["extraHtml"];
                msgContent.innerHTML = chartList[i]["html"];
            }
        }
    }
    else if (Type == "searchResult") {
        var html = "<input id='searchContent' type='text' size=50% value='' onKeyDown='if(event.keyCode==13 && event.ctrlKey){searchUser();}'/><input name='search_submit' type='button' class='btn_grey' value='搜索' onClick='searchUser()'/>";
        var num = parseInt(Content["amount"]);
        for (var i = 0; i < num; i++) {
            var username = Content[i + ""]["username"];
            var userstate = Content[i + ""]["userstate"];
            html = html + "<input type='button' class='" + userstate + "' value='" + username + "' onClick='showMsgToUser(\"" + username + "\")'/>";
        }
        mainList.innerHTML = html;
    }
    else if (Type == "getNewMessageResult") {
        var flag = true;
        for (var i = 0; i < chartList.length; i++) {
            if (Content["from"] == chartList[i]["name"] || Content["to"] == chartList[i]["name"]) {
                chartList[i]["html"] += "<font color='blue'>" + Content["from"] + ":</font><font color='blue'>" + Content["message_content"] + "</font><br>";
                flag = false;
            }
            if (msg_to == chartList[i]["name"]) {
                msgContent.innerHTML = chartList[i]["html"];
            }
        }
        if (flag) {
            if (Content["from_type"] == "group") {
                showMsgToGroup(Content["to"]);
                /*
                 msg_to=Content["to"];
                 object_type="group";
                 chartList.push({'name':Content["to"],'type':'group','html':'','extraHtml':''});
                 chart.innerHTML+="<input	id="+msg_to+" type='button' class='btn_grey' value="+msg_to+"	onClick='change(\""+msg_to + "\",\"" +'group'+"\")'/>";
                 chart.innerHTML+="<input type='button' class='btn_grey' value='×'	onClick='delChart(\""+msg_to+"\")'/>";
                 */
            } else {
                showMsgToUser(Content["from"]);
                /*
                 msg_to=Content["from"];
                 object_type="user";
                 chartList.push({'name':msg_to,'type':'user','html':'','extraHtml':''});
                 chart.innerHTML+="<input id="+msg_to+" type='button' class='btn_grey' value="+msg_to+"	onClick='change(\""+msg_to + "\",\"" +'user'+"\")'/>";
                 chart.innerHTML+="<input type='button' class='btn_grey' value='×'	onClick='delChart(\""+msg_to+"\")'/>";
                 */
            }
        }
    }
    else if (Type == "getUserIdentifyResult") {

    }
}

function showSelfInfo() {
    window.open("selfInfo.jsp", 'newwindow', 'height=800, width=400, top=90, left=800, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
}

function showGroup() {
    var content = {'action': 'none'};
    var com = {
        'Type': 'getGroups',
        'Content': content
    }
    ws.send(JSON.stringify(com));
}

function delChart(part_id) {
    for (var i = 0; i < chartList.length; i++) {
        if (chartList[i]["name"] == part_id) {
            chartList.remove(i);
            break;
        }
    }
    alert(part_id);
    alert(chart.innerHTML);
    var reg = new RegExp("<input.*?id=\"" + part_id + "\".*?>", "g");
    var temp = chart.innerHTML.replace(reg, "");
    alert(temp);
    chart.innerHTML = temp;
    //删除列表栏目
}
function showMsgList() {
    var content = {'action': 'none'};
    var com = {
        'Type': 'getMsgList',
        'Content': content
    }
    ws.send(JSON.stringify(com));
}

function showUsers() {
    var content = {'action': 'none'};
    var com = {
        'Type': 'getAllUsers',
        'Content': content
    }
    ws.send(JSON.stringify(com));
}

function showMsgToGroup(group_name) {
    var flag = true;
    msg_to = group_name;
    object_type = "group";
    for (var i = 0; i < chartList.length; i++) {
        if (group_name == chartList[i]["name"]) {
            flag = false;
        }
    }
    if (flag) {
        chartList.push({'name': group_name, 'type': 'group', 'html': '', 'extraHtml': ''});
        chart.innerHTML += "<input	id=" + group_name + " type='button' class='btn_grey' value=" + group_name + "	onClick='change(\"" + group_name + "\",\"" + 'group' + "\")'/>";
        chart.innerHTML += "<input type='button' class='btn_grey' value='×'	onClick='delChart(\"" + group_name + "\")'/>";
        var content = {
            'group_name': group_name,
            'begintime': "1504108800",
            'endtime': "1504195200"
        };
        var com = {
            'Type': 'getGroupHistoryMsg',
            'Content': content
        }
        ws.send(JSON.stringify(com));
    } else {
        change(group_name, "group");
    }
}

function showMsgToUser(user_name) {
    var flag = true;
    msg_to = user_name;
    object_type = "user";
    for (var i = 0; i < chartList.length; i++) {
        if (user_name == chartList[i]["name"]) {
            flag = false;
        }
    }
    if (flag) {
        chartList.push({'name': user_name, 'type': 'user', 'html': '', 'extraHtml': ''});
        chart.innerHTML += "<input id=" + user_name + " type='button' class='btn_grey' value=" + user_name + "	onClick='change(\"" + user_name + "\",\"" + 'user' + "\")'/>";
        chart.innerHTML += "<input type='button' class='btn_grey' value='×'	onClick='delChart(\"" + user_name + "\")'/>";
        var content = {
            'username': user_name,
            'begintime': "1504108800",
            'endtime': "1504195200"
        };
        var com = {
            'Type': 'getUserHistoryMsg',
            'Content': content
        }
        ws.send(JSON.stringify(com));
    } else {
        change(user_name, "user");
    }
}

function change(name, type) {
    msg_to = name;
    object_type = type;
    for (var i = 0; i < chartList.length; i++) {
        if (name == chartList[i]["name"]) {
            msgContent.innerHTML = chartList[i]["html"];
        }
    }
}
function getUserInfo(user_name) {
    var content = {
        'username': user_name
    };
    var com = {
        'Type': 'getUserIdentify',
        'Content': content
    }
    ws.send(JSON.stringify(com));
}
function searchUser() {
    var content = {
        'criteria': document.getElementById("searchContent").value
    };
    var com = {
        'Type': 'search',
        'Content': content
    }
    ws.send(JSON.stringify(com));
}

function display(part_id) {
    document.getElementById(part_id).style.display = "block";
}

function disappear(part_id) {
    document.getElementById(part_id).style.display = "none";
}

function send() { //验证聊天信息并发送
    msgContent.in += ("<font color='red'>" + selfName + ":</font><font color='blue'>" + document.getElementById("sendMsg").value + "</font><br>");
    for (var i = 0; i < chartList.length; i++) {
        if (chartList[i]["name" == msg_to]) {
            chartList[i]["html"] += ("<font color='red'>" + selfName + ":</font><font color='blue'>" + document.getElementById("sendMsg").value + "</font><br>");
        }
    }
    var content = {
        'object_type': object_type,
        'to': msg_to,
        'msg': document.getElementById("sendMsg").value
    };
    var com = {
        'Type': 'sendMsg',
        'Content': content
    }
    ws.send(JSON.stringify(com));
}


function onerror() {
    alert("很抱歉，服务器出现错误，当前窗口将关闭！");
    window.opener = null;
    window.close();
}

function Exit() {
    window.location.href = "leave.jsp";
    alert("欢迎您下次光临！");
}


window.onbeforeunload = function () { //当用户单击浏览器中的关闭按钮时执行退出操作
    if (event.clientY < 0 && event.clientX > document.body.scrollWidth) {
        Exit(); //执行退出操作
    }
}