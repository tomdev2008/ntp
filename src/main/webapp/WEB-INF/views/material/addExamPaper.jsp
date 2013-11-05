<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html class=""> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctx}/resources/css/DTotal.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/jquery.autocomplete.css">

<!-- 授权管理 用户列表 模板 -->
<script id="listUserKinguserTemplate" type="text/x-dot-template">
    <tr data-fdid="{{=it.id}}">
        <td class="tdTit">
            <div class="pr">
                <div class="state-dragable"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></div>
                <img src="{{=it.imgUrl || 'images/temp-face36.jpg'}}" alt="">{{=it.name}}（{{=it.mail}}），{{=it.org}} {{=it.department}}
            </div>
        </td>
        <td><input type="checkbox" checked class="tissuePreparation" /></td>
        <td><input type="checkbox" class="editingCourse" /></td>
        <td><a href="#" class="icon-remove-blue"></a></td>
    </tr>
</script>

<!-- 作业详情编辑 模板 -->
<script id="examDetailTemplate" type="text/x-dot-template">
    <div class="page-header">
        <a href="${ctx}/material/addExamPaper?fdType=10" class="backParent">返回当前作业包</a>
        <h4>{{=it.examPaperName}}</h4>
        <div class="btn-group">
            <button class="btn btn-large btn-primary" id="saveExam" type="button">保存</button>
            <button class="btn btn-large btn-primary" disabled id="exportExam" type="button">导出</button>
            <button class="btn btn-white btn-large " id="delExam" type="button">删除</button>
        </div>
    </div>
    <div class="page-body editingBody">
        <form action="{{=it.action || '#'}}" id="formEditDTotal" class="form-horizontal" method="post">
            <section class="section">
                <div class="control-group">
                    <label class="control-label" >题型设置</label>
                    <div class="controls">
                        <input name="examType" id="examType" value="{{=it.examType || 'uploadWork'}}" type="hidden" />
                        <div class="btn-group btns-radio" data-toggle="buttons-radio">
                            <button class="btn btn-large{{?!it.examType || it.examType == 'uploadWork'}} active{{?}}" id="uploadWork" type="button">上传作业</button>
                            <button class="btn btn-large{{?it.examType == 'onlineAnswer'}} active{{?}}" id="onlineAnswer" type="button">在线作答</button>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="examName">作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业</label>
                    <div class="controls">
                        <input value="{{=it.examName || ''}}" id="examName" placeholder="请输入作业名称" required class="span6"
                               name="examName" type="text"><span class="date">2013/02/14 10:01 AM</span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="examStem">作业简介</label>
                    <div class="controls"><textarea placeholder="请使用#...#标记填空题的答案，例如：新教师在线备课课程的第三章学习内容是#标准化教案#" rows="4"
                                                    class="input-block-level" required id="examStem"
                                                    name="examStem">{{=it.examStem || ''}}</textarea>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" >作业总分 <small>(单位分)</small></label>
                    <div class="controls">
                        <input name="examScore" id="examScore" value="{{=it.examScore || ''}}" type="hidden"/>
                            {{#def.timeLine:it.timeLine}}
                    </div>
                </div>
            </section>
            <section class="section mt20">
                <label>辅助材料（上传辅助材料，建议小于2G）</label>
                <div class="control-upload">
                    <div class="upload-fileName">高新技术产业各领域增加值饼形图（单位：亿元）.jpg <i class="icon-paperClip"></i></div>
                    <span class="progress"> <div class="bar" style="width:20%;"></div> </span>
                    <span class="txt"><span>20%</span>，剩余时间：<span>00:00:29</span></span>
                    <button class="btn btn-primary btn-large" type="button">上传</button>
                </div>
                <ul class="unstyled list-attachment" id="listAttachment">
                    {{~it.listAttachment :att:index}}
                        {{~it.listAttachment :att2:index2}}
                            {{?index == att2.index}}
                                {{#def.item:att2}}
                            {{?}}
                        {{~}}
                    {{~}}
                </ul>
            </section>

            <button class="btn btn-block btn-submit btn-inverse" type="submit">保存</button>
        </form>
    </div>
</script>

    <!-- 时间轴 模板 -->
    <script id="timeLineTemplate" type="text/x-dot-template">
        {{?!it.timeLine}}
            {{#def.timeLine:it}}
        {{?}}
        {{##def.timeLine:param:
            <div class="timeLine">
                    <div class="num">0</div>
            {{ for(var i=1; i <= param.total; i++){ }}
            <a title="{{=i*param.span}}{{=it.unit || ''}}" href="#" style="width: {{=(param.width-param.total-1)/param.total}}px"
            class="{{?i*param.span==param.span}}first {{?}}{{?param.curPos && i*param.span<=param.curPos}}active{{?}}"><span class="num">{{=i*param.span}}</span></a>
            {{ } }}
            </div>
        #}}
    </script>

    <!-- 作业详情 列表项 模板 -->
    <script id="itemExamDetailTemplate" type="text/x-dot-template">
        {{?it.flag === "add"}}
             {{#def.item:it}}
        {{?}}
        {{##def.item:param:
            <li data-fdid="{{=param.id || 'temp'}}">
                <div class="state-dragable"><span class="icon-bar"></span><span
                        class="icon-bar"></span><span class="icon-bar"></span><span
                        class="icon-bar"></span><span class="icon-bar"></span></div>
                <{{?param.url}}a href="{{=param.url}}" target="_blank" {{??}}div{{?}} class="name">{{=param.name}}</{{?param.url}}a{{??}}div{{?}}>
                <div class="item-ctrl">
                    {{?param.isAnswer != undefined}}
                    <label class="{{?it.examType == 'uploadWork'}}checkbox{{??it.examType == 'onlineAnswer'}}radio{{??}}checkbox{{?}} inline">
                        <input type="{{?it.examType == 'uploadWork'}}checkbox{{??it.examType == 'onlineAnswer'}}radio{{??}}checkbox{{?}}"
                        {{?param.isAnswer}}checked{{?}} name="isAnswer" />
                        我是答案</label>
                    <a class="icon-pencil-blue" href="#"></a>
                    {{?}}
                    <a href="#" class="icon-remove-blue"></a>
                </div>
            </li>
        #}}
    </script>
    <script id="taskPaperListTemplate" type="text/x-dot-template">
	    <tr data-fdid="{{=it.id}}" >
		    <td class="tdTit">
		        <div class="pr">
		            <div class="state-dragable"><span class="icon-bar"></span><span
		                    class="icon-bar"></span><span class="icon-bar"></span><span
		                    class="icon-bar"></span><span class="icon-bar"></span></div>
		            <a href="#">{{=it.subject}}</a>
		        </div>
		    </td>
		    <td><input type="text" onblur="initScore()" value="{{=it.score}}" data-toggle="tooltip" title="输入数字做为分值" class="itemScore input-mini">分</td>
		    <td><a href="#" class="icon-remove-blue"></a></td>
		</tr>
    </script>

    <script src="${ctx}/resources/js/doT.min.js"></script>
</head>

<body>

<section class="container">
	<section class="clearfix mt20">
	  <section class="col-left pull-left">
	  <%@ include file="/WEB-INF/views/group/menu.jsp" %>
	  </section>
		<section class="w790 pull-right" id="rightCont">
	        <div class="page-header">
                <a href="${ctx}/material/findList?fdType=10&order=FDCREATETIME" class="backParent">返回作业包列表</a>
                <h4>雅思口语强化课程教案解读作业包</h4>
                <div class="btn-group">
                    <button class="btn btn-large btn-primary" id="saveExamPaper" type="button">保存</button>
                    <button class="btn btn-large btn-primary" disabled id="exportExamPaper" type="button">导出</button>
                    <button class="btn btn-white btn-large " id="delExamPaper" type="button">删除</button>
                </div>
	        </div>
            <div class="page-body editingBody">
                <form action="#" id="formEditDTotal" class="form-horizontal" method="post">
                    <section class="section">
                        <div class="control-group">
                            <label class="control-label" for="examPaperName">作业包</label>
                            <div class="controls">
                                <input value="${materialInfo.fdName}" id="examPaperName" required class="span6"
                                       name="examPaperName" type="text">
                                <span class="date"><fmt:formatDate value="${materialInfo.fdCreateTime}" pattern="yyyy/MM/dd hh:mm aa"/></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="examPaperIntro">作业包<div>简介</div></label>
                            <div class="controls">
                            <textarea placeholder="非必填项" rows="4"
                                      class="input-block-level" id="examPaperIntro"
                                      name="examPaperIntro">${materialInfo.fdDescription}</textarea>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" >建议时间 <small>(单位天)</small></label>
                            <div class="controls">${materialInfo.fdStudyTime}
                                <input name="examPaperTime" id="examPaperTime" value="30" type="hidden"/>
                            </div>
                        </div>

                    </section>
                    <section class="section mt20">
                        <div class="hd">
                            <label for="passScore" class="miniInput-label">
                                作业列表（共计10题，满分50分，及格 <input class="input-mini" id="passScore" name="passScore" value="40" type="text"/>      分）
                            </label>
                            <button class="btn btn-primary btn-large" id="addExam" type="button">添加作业</button>
                        </div>
                        <div class="bd">
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>作业</th>
                                    <th>分数</th>
                                    <th>删除</th>
                                </tr>
                                </thead>
                                <tbody id="list_exam">
                               
                                <!-- <tr data-fdid="fdid324" >
                                    <td class="tdTit">
                                        <div class="pr">
                                            <div class="state-dragable"><span class="icon-bar"></span><span
                                                    class="icon-bar"></span><span class="icon-bar"></span><span
                                                    class="icon-bar"></span><span class="icon-bar"></span></div>
                                            <a href="#">强化段讲解到考试流程哪一部分的时候应该放相应视频给学生看？</a>
                                        </div>
                                    </td>
                                    <td><input type="text" value="5" data-toggle="tooltip" title="输入数字做为分值" class="itemScore input-mini">分</td>
                                    <td><a href="#" class="icon-remove-blue"></a></td>
                                </tr> -->
                                
                                </tbody>
                            </table>
                        </div>
                    </section>
                    <section class="section mt20">
                        <div class="control-group">
                            <label class="control-label" for="author">作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;者</label>
                            <div class="controls">
                                <input value="${materialInfo.fdAuthor}" id="author" required class="input-block-level"
                                       name="author" type="text">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="authorIntro">作者简介</label>
                            <div class="controls">
                            <textarea placeholder="非必填项" rows="4"
                                      class="input-block-level" id="authorIntro"
                                      name="authorIntro">${materialInfo.fdDescription}</textarea>
                            </div>
                        </div>
                    </section>
                    <div class="page-header mt20"> <h4>权限设置</h4> </div>
                    <section class="section">
                        <label>权限设置<input type="hidden" id="permission" name="permission" value="open"></label>
                        <ul class="nav nav-pills">
                            <li class="active"><a data-toggle="tab" href="#open">公开</a></li>
                            <li><a data-toggle="tab" href="#encrypt">加密</a></li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="open">
                                提示：“公开”素材将允许所有主管在管理课程的过程中使用，而“加密”素材将允许您手动授权某些主管使用本课程素材。
                            </div>
                            <div class="tab-pane" id="encrypt">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th>授权用户</th>
                                        <th>组织备课</th>
                                        <th>编辑课程</th>
                                        <th>删除</th>
                                    </tr>
                                    </thead>
                                    <tbody id="list_user">
                                    <tr data-fdid="fdid325" draggable="true">
                                        <td class="tdTit">
                                            <div class="pr">
                                                <div class="state-dragable"><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span></div>
                                                <img src="images/temp-face36.jpg" alt="">魏紫（weizi5@xdf.cn），广州学校 国外考试部
                                            </div>
                                        </td>
                                        <td><input type="checkbox" checked="" class="tissuePreparation"></td>
                                        <td><input type="checkbox" class="editingCourse"></td>
                                        <td><a href="#" class="icon-remove-blue"></a></td>
                                    </tr>
                                    <tr data-fdid="fdid324" draggable="true">
                                        <td class="tdTit">
                                            <div class="pr">
                                                <div class="state-dragable"><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span></div>
                                                <img src="images/temp-face36.jpg" alt="">刘鹍（liukun@xdf.cn），集团总公司 知识管理中心
                                            </div>
                                        </td>
                                        <td><input type="checkbox" class="tissuePreparation"></td>
                                        <td><input type="checkbox" checked="" class="editingCourse"></td>
                                        <td><a href="#" class="icon-remove-blue"></a></td>
                                    </tr>
                                    <tr data-fdid="fdid323" draggable="true">
                                        <td class="tdTit">
                                            <div class="pr">
                                                <div class="state-dragable"><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span></div>
                                                <img src="images/temp-face36.jpg"
                                                 alt="">杨义锋（yangyifeng@xdf.cn），集团总公司 知识管理中心
                                            </div>
                                        </td>
                                        <td><input type="checkbox" checked="" class="tissuePreparation"></td>
                                        <td><input type="checkbox" checked="" class="editingCourse"></td>
                                        <td><a href="#" class="icon-remove-blue"></a></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="pr">
                                    <input type="text" id="addUser" class="autoComplete input-block-level" placeholder="请输入人名、邮箱、机构或者部门查找用户">
                                    <i class="icon-search"></i>
                                </div>
                            </div>
                        </div>
                    </section>
                    <button class="btn btn-block btn-submit btn-inverse" type="submit">保存</button>
                </form>
            </div>
	    </section>
	</section>
<input type="hidden" id="materialId" value="${materialInfo.fdId}"/>
</section>
<script type="text/javascript" src="${ctx}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.autocomplete.pack.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.sortable.js"></script>
<script type="text/javascript">
$(function(){
    $.Placeholder.init();
    $('.itemScore[data-toggle="tooltip"]').tooltip({
        trigger: "focus"
    });
});
/**
 * Created by wqh on 13-11-1.
 */
$(function(){
    //授权管理 用户列表 模板函数
    var listUserKinguserFn = doT.template(document.getElementById("listUserKinguserTemplate").text);
    //时间轴 模板函数
    var timeLineFn = doT.template(document.getElementById("timeLineTemplate").text);
    
   //初始化作业列表
    initExamPaperList();

    $("#examPaperTime").after(timeLineFn({//时间轴控件 配置数据
        width: 670, //时间轴控件 宽度
        total: 7, //总格数
        curPos: 4, //当前位置
        span: 1, //每格的进制
        unit: "天"
    }));
    $(".timeLine>a").tooltip()
            .click(function(e){
                e.preventDefault();
                $(this).prevAll("a").add(this).addClass("active");
                $(this).nextAll("a").removeClass("active");
                $("#examPaperTime").val( $(this).children(".num").text());
            });

    //作业详情编辑页面 模板函数
    var examDetailFn = doT.template(document.getElementById("examDetailTemplate").text
            + document.getElementById("itemExamDetailTemplate").text
            + document.getElementById("timeLineTemplate").text);

    //作业详情 列表项 模板函数
    var itemExamDetailFn = doT.template(document.getElementById("itemExamDetailTemplate").text);

    $("#formEditDTotal").validate({
        submitHandler: submitForm
    });
    $("#saveExamPaper").click(function(e){
        $("#formEditDTotal").trigger("submit");
    });
    //删除作业包事件
    $("#delExamPaper").click(function(e){
        //
    });

    /*提交表单函数*/
    function submitForm(form){
        var data = {
            examPaperName: $("#examPaperName").val(),
            examPaperIntro: $("#examPaperIntro").val(),
            author: $("#author").val(),
            authorIntro: $("#authorIntro").val(),
            permission:$("#permission").val(),
            listExam: [],
            kingUser: []
        };
        if($("#list_exam").children("tr").length){
            //push 作业列表数据
            $("#list_exam>tr").each(function(i){
                data.listExam.push({
                    id: $(this).attr("data-fdid"),
                    index: i,
                    editingCourse: $(this).find(".itemScore").val()
                });
            });
        }
        if(data.permission === "encrypt"){
            //push人员授权数据
            $("#list_user>tr").each(function(i){
                data.kingUser.push({
                    id: $(this).attr("data-fdid"),
                    index: i,
                    tissuePreparation: $(this).find(".tissuePreparation").is(":checked"),
                    editingCourse: $(this).find(".editingCourse").is(":checked")
                });
            });
        }
        //console.log(JSON.stringify(data));
        //ajax
        /*$.post("url?updata",data)
         .success(function(){
         alert("保存成功");
         });*/
    }
    $('#formEditDTotal a[data-toggle="tab"]').on('shown', function (e) {
        var href = 	e.target.href.split("#").pop();
        $("#permission").val(href);
    });
    $("#list_user,#list_exam").sortable({
        handle: '.state-dragable'
    })
            .find("a.icon-remove-blue").bind("click",function(e){
                e.preventDefault();
                $(this).closest("tr").remove();
            });

    var allUserData ;

    allUserData = [
        {
            id: "fdid3232323",
            imgUrl: "http://img.staff.xdf.cn/Photo/06/3A/a911e1178bf3725acd75ddbb9c7e3a06_9494.jpg",
            name: "杨小锋",
            mail: "yangyifeng@xdf.cn",
            org: "集团总公司",
            department: "知识管理中心"
        },
        {
            id: "fdid32234",
            imgUrl: "",
            name: "刘小鹍",
            mail: "liukun@xdf.cn",
            org: "集团总公司",
            department: "知识管理中心"
        },
        {
            id: "fdid328",
            imgUrl: "",
            name: "魏小紫",
            mail: "weizi5@xdf.cn",
            org: "广州学校",
            department: "国外考试部"
        }
    ]
    /*$("#addUser").autocomplete("url.jsp",{
     dataType: "json",
     parse: function(data) {
     return $.map(data, function(row) {
     return {
     data: row,
     value: row.name,
     result: row.name + " <" + row.mail + ">"
     }
     });
     },*/
    $("#addUser").autocomplete(allUserData,{
        formatMatch: function(item) {
            return item.name + item.mail + item.org + item.department;
        },
        formatItem: function(item) {
            return '<img src="'
                    + (item.imgUrl || 'images/temp-face36.jpg') + '" alt="">'
                    + item.name + '（' + item.mail + '），'
                    + item.org + '  ' + item.department;
        },
        matchContains:true ,
        max: 10,
        scroll: false,
        width:748
    }).result(function(e,item){
                $(this).val(item.name);
                $("#list_user").append(listUserKinguserFn(item))
                        .sortable({
                            handle: '.state-dragable',
                            forcePlaceholderSize: true
                        })
                        .find("a.icon-remove-blue").bind("click",function(e){
                            e.preventDefault();
                            $(this).closest("tr").remove();
                        });

            });


    //添加作业事件
    $("#addExam").bind("click",function(e){
        loadExamPage();
    });

    //打开作业列表中的作业详情
    $("#list_exam>tr>.tdTit .pr a").click(function(e){
        e.preventDefault();
        var id = $(this).closest("tr").attr("data-fdid");
        loadExamPage(id);
        //
    });
    /*加载作业页面*/
    function loadExamPage(fdid){
        var data = {};
        if( !fdid){//添加作业数据
            data = {
                examScoreTotal: 20,
                examType: "uploadWork"
            };
        } else {
            // ajax 获取已存在的作业数据
            /*$.get("url",{id: fdid}).success(function(result){
             data = result;
             })*/

            data = {// ajax 后删除
                examType: "uploadWork",//uploadWork, onlineAnswer
                examScore: 10,
                examName: "雅思写作强化DEMO第03课",
                examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，" +
                        "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。" +
                        "高新技术产业各领域的增加值如下图所示：  （5 分）  PASS ",
                listAttachment: [
                    {
                        id:"fdid4205325wef",
                        index: 0,
                        name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                        url: "#"
                    },
                    {
                        id:"fdid97867",
                        index: 3,
                        name: "高新技术产业各领域咨询报告.pdf",
                        url: "#"
                    }
                ]
            };
        }
        data.examPaperName = "雅思口语强化课程教案解读作业包";  //当前作业包名称
        data.timeLine = {//时间轴控件 配置数据
            width: 670, //时间轴控件 宽度
            total: 20, //总格数
            curPos: data.examScore, //当前位置
            span: 1, //每格的进制
            unit: "分"
        }
        $("#rightCont").html(examDetailFn(data));

        //应用拖放效果
        $(".list-attachment").sortable({
            handle: '.state-dragable'
        })
            //移除列表项
                .delegate(".item-ctrl>.icon-remove-blue","click",function(e){
                    e.preventDefault();
                    $(this).closest("li").remove();
                });


        $(".timeLine>a").tooltip()
                .click(function(e){//分数控制
                    e.preventDefault();
                    $(this).prevAll("a").add(this).addClass("active");
                    $(this).nextAll("a").removeClass("active");
                    $("#examScore").val( $(this).children(".num").text());
                });

        $("#formEditDTotal").validate({
            submitHandler: submitForm
        });
        $("#saveExam").click(function(e){
            $("#formEditDTotal").trigger("submit");
        });

        //作业类型选择
        $('[data-toggle="buttons-radio"]>.btn').click(function(){
            $("#examType").val(this.id);
        });

        /*提交作业详情表单函数*/
        function submitForm(form){
            var data = {
            	materialId:$("#materialId").val(),
                examType: $("#examType").val(),
                examName: $("#examName").val(),
                examStem: $("#examStem").val(),
                examScore: $("#examScore").val(),
                listAttachment: []
            };
            if($("#listAttachment>li").length){
                //push 附件列表数据
                $("#listAttachment>li").each(function(i){
                    data.listAttachment.push({
                        id: $(this).attr("data-fdid"),
                        index: i,
                        url: $(this).find(".name").attr("href")
                    });
                });
            }
            //console.log(data);
            //ajax
            $.ajax({
				  url: "${ctx}/ajax/taskPaper/saveOrUpdateTaskPaper?taskId="+fdid+"&materialId="+$("#materialId").val(),
				  async:false,
				  data:data,
				  type: "POST",
				  dataType:'json',
				  success: function(result){
					  window.location.href="${ctx}/material/addExamPaper?materialId="+result.materialId;
				  },
			});
        }
    }
});
    
function initExamPaperList(){
	var id = $("#materialId").val();
	if(id!==null&&id!=""){
		var itemExamDetailFn = doT.template(document.getElementById("taskPaperListTemplate").text);
	    $.ajax({
			  url: "${ctx}/ajax/taskPaper/getTaskByMaterialId?materialId="+id,
			  async:false,
			  dataType : 'json',
			  success: function(result){
				  var data = JSON.stringify(result);
				  alert(data);
				  var html = "";
				 for(var i in result){
					  html += itemExamDetailFn(result[i]);
				  }
				  $("#list_exam").html(html); 
				  //initScore();
			  }
		});
	}
}
</script>
</body>
</html>
