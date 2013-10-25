/* JavaScript Document
 *
 * 模板详情 加载各子页面对象
 *
**/
	(function(){
		"use strict";
		
		var rightCont = {},global,data = {};
		
		global = (function(){ return this || (0,eval)('this'); }());
		global.rightCont = rightCont;
		
		var def = {
			pageHeader:document.getElementById("contHeaderTemplate").text		
		}	
		//基本信息 模板函数
		var basicInfoFn = doT.template(document.getElementById("basicInfoTemplate").text, undefined, def);
		//基本信息  添加关键词表单 模板函数
		var addKeywordFn = doT.template(document.getElementById("addKeywordInfoTemplate").text);
		//基本信息  关键词标签 模板函数
		var tagKeywordFn = doT.template(document.getElementById("tagKeywordInfoTemplate").text);
		
		//详细信息 模板函数
		var detailInfoFn = doT.template(document.getElementById("detailInfoTemplate").text, undefined, def);
		//详细信息 列表项 模板函数
		var itemDetailInfoFn = doT.template(document.getElementById("itemDetailInfoTemplate").text);
		
		//课程推广 模板函数
		var promotionFn = doT.template(document.getElementById("promotionTemplate").text, undefined, def);
		
		//访问权限 模板函数
		var accessRightFn = doT.template(document.getElementById("accessRightTemplate").text, undefined, def);
		
		//授权管理 模板函数
		var kinguserFn = doT.template(document.getElementById("kinguserTemplate").text, undefined, def);
		//授权管理 用户列表 模板函数
		var listUserKinguserFn = doT.template(document.getElementById("listUserKinguserTemplate").text);
		
		//删除课程 模板函数
		var deleteCourseFn = doT.template(document.getElementById("deleteCourseTemplate").text, undefined, def);

        //各媒体页 模板函数
        var mediaPageFn = doT.template(document.getElementById("mediaPageTemplate").text);
        //各媒体页 列表项 模板函数
        var mediaListFn = doT.template(document.getElementById("mediaListTemplate").text);
        //编辑章节标题模板函数
        var editMediaTitleFn = doT.template(document.getElementById("formEditMediaTitle").text);

		//编辑章节标题模板函数
		var editTitleFn = doT.template(document.getElementById("formEditSectionTitle").text);
		//添加章节模板函数
		var addSectionsFn = doT.template(document.getElementById("addSectionsTemplate").text, undefined, {
			edittitle:document.getElementById("formEditSectionTitle").text		
		});
		//展示章节模板函数
		var sectionsFn = doT.template(document.getElementById("sectionsTemplate").text + document.getElementById("sectionBarTemplate").text + document.getElementById("lectureContentTemplate").text);
		//load章节目录模板函数
		var loadsectionsDirectoryFn = doT.template(document.getElementById("sectionDirectoryTemplate").text + document.getElementById("sectionBarTemplate").text + document.getElementById("lectureContentTemplate").text, undefined, def);

        //加载视频页
        rightCont.loadVideoPage = function (opt,type){
            /*============================================ ajax 加载JSON数据 ================================================*/
            /*$.getJSON("url?load",{id:opt.id},function(rsult){
                 data = rsult;
                 data.pageTitle = opt.title;
                 data.lectureIndex = numParseCN(opt.index);
                 data.typeTxt = "视频";
                 data.uploadIntro = "上传视频（支持MP4、AVI、WMV格式的视频，建议小于10G）：成功上传的视频将会显示在下面的视频列表中。";
                 $("#rightCont").html(mediaPageFn(data));
             });*/
        	$.ajax({
				  url: $('#ctx').val()+"/ajax/courseContent/getMaterialsByCategoryId",
				  async:false,
				  data:{catalogId:opt.id},
				  dataType:'json',
				  success: function(rsult){
					  data = rsult;
		              data.pageTitle = opt.title;
		              data.lectureIndex = numParseCN(opt.index);
		              switch(type){
		              	case "01":
		              		data.type = "video";
		              		data.typeTxt = "视频";
		              		data.uploadIntro = "上传视频（支持MP4、AVI、WMV格式的视频，建议小于10G）：成功上传的视频将会显示在下面的视频列表中。";
		              		break;
		                case "02":
		                	data.type = "audio";
		                	data.typeTxt = "音频";
				            data.uploadIntro = "上传音频（支持MP3、MV格式的音频，建议小于10G）：成功上传的视频将会显示在下面的音频列表中。";
				            break;
		                case "04":
		                	data.type = "doc";
		                	data.typeTxt = "文档";
				            data.uploadIntro = "上传文档（支持DOC、EXCEL格式的文档，建议小于10G）：成功上传的视频将会显示在下面的文档列表中。";
				            break;
		                case "05":
		                	data.type = "ppt";
		                	data.typeTxt = "幻灯片";
				            data.uploadIntro = "上传幻灯片（建议小于10G）：成功上传的视频将会显示在下面的幻灯片列表中。";
				            break;
		                case "03":
		                	data.type = "img";
		                	data.typeTxt = "图片";
				            data.uploadIntro = "上传视频（支持JPG、PNG、BMP格式的图片，建议小于10G）：成功上传的视频将会显示在下面的图片列表中。";
				            break;
		                case "08":
		                	data.type = "exam";
		                	data.typeTxt = "测试";
				            data.uploadIntro = "";
				            break;
		                case "10":
		                	data.type = "task";
		                	data.typeTxt = "作业";
				            data.uploadIntro = "";
				            break;
		              }
		              $("#rightCont").html(mediaPageFn(data));
				  },
			});
        	/*
            data = {
                learnTime: "",
                sectionsIntro: "",
                mediaList: [
                    {
                        id: "fdid4258924985",
                        index: 2,
                        title: "俞老师对国外考试项目新教师致辞"
                    },
                    {
                        id: "fdid4255",
                        index: 1,
                        title: "新东方2013校园招聘"
                    },
                    {
                        id: "fdid425511111",
                        index: 4,
                        title: "国外考试新教师备课致辞"
                    },
                    {
                        id: "fdid425522222",
                        index: 3,
                        title: "国外部项目备课致辞"
                    },
                    {
                        id: "fdid425500",
                        index: 5,
                        title: "BC官员雅思考试解读"
                    },
                    {
                        id: "fdid43454545222",
                        index: 6,
                        title: "国外考试新教师在线备课平台介绍"
                    }
                ]
            }
            data.pageTitle = opt.title;
            data.lectureIndex = numParseCN(opt.index);
            data.typeTxt = "视频";
            data.uploadIntro = "上传视频（支持MP4、AVI、WMV格式的视频，建议小于10G）：成功上传的视频将会显示在下面的视频列表中。";
            $("#rightCont").html(mediaPageFn(data));
			*/
            $("#listMedia").sortable()
                .bind("sortupdate",function(){//绑定拖放元素改变次序事件
                    $(this).children().each(function(i){
                        $(this).find(".title>.index").text(i+1);
                    });
                })
                .delegate(".btn-ctrls","click",function(e){
                    e.preventDefault();
                    if($(this).hasClass("icon-remove")){//删除项
                        $(this).parent("li").remove();
                        $("#mediaCount").text($("#listMedia").children("li").each(function(i){
                            $(this).find(".title>.index").text(i+1);
                        }).length);
                    }else if($(this).hasClass("icon-pencil2")){//编辑项
                        var $tit = $(this).prev(".title");
                        $(this).parent("li").hide().after(editMediaTitleFn({
                            typeTxt: data.typeTxt,
                            index: $tit.children(".index").text(),
                            name: $tit.children(".name").text()
                        }));
                    }
                })
                //绑定保存,取消标题按钮事件
                .delegate(".form-edit-title .btn","click",function(e){
                    e.preventDefault();
                    if($(this).hasClass("btn-primary")){//保存
                        var val = $(this).parent().prev(".control-group").find(":text").val();
                        if(val){
                            $(this).closest(".form-edit-title").prev("li").show()
                                .find(".title>.name").text(val);
                        }else {                          
							$(this).closest(".form-edit-title").find(".control-group:first").addClass("warning").find(":text").after('<span class="help-block">请填写名称！</span>');;
							return ;
						}
                    } else{
                        $(this).closest(".form-edit-title").prev("li").show();
                    }
                    $(this).closest(".form-edit-title").remove();
                });
            $("#formMedia").validate({
                submitHandler:function(form){
                    var listArr = [];
                    $("#listMedia>li").each(function(i){
                        listArr.push({
                            id: $(this).attr("data-fdid"),
                            index: $(this).find(".title>.index").text(),
                            title: $(this).find(".title>.name").text()
                        });
                    });
                    $.post($('#ctx').val()+"/ajax/courseContent/saveCourseContent",{
                    	catalogId:opt.id,
                    	type:type,
                        pageTitle: $("#lectureName").val(),
                        learnTime:  $("#learnTime").val(),
                        sectionsIntro: $("#sectionsIntro").val(),
                        mediaList:JSON.stringify(listArr)
                    })
                        .success(function(){
                            //提交成功
                        	urlRouter("sectionsDirectory");
                        });
                }
            });
            var itemHtml,
                $listMedia = $("#listMedia"),
                addFlag = false,
                mediaData = [
                {
                    id: "fdid8348934020202002",
                    name: "雅思听力"
                },
                {
                    id: "fdid834893ewfge",
                    name: "雅思听力练习"
                },
                {
                    id: "fdid3ewfge4343",
                    name: "雅思听力技巧"
                },
                {
                    id: "fdid3lliuu23",
                    name: "雅思听力视频"
                }
            ];
            /*$("#addMedia").autocomplete("url.jsp",{
                 dataType: "json",
                 parse: function(data) {
                     return $.map(data, function(row) {
                         return {
                         data: row,
                         value: row.name,
                         result:
                         }
                     });
                 },*/
            $("#addMedia").autocomplete(mediaData,{
                formatItem: function(item) {
                    return item.name;
                },
                matchContains:true ,
                max: 10,
                scroll: false,
                width:688
            }).result(function(e,item){
                    item.typeTxt = data.typeTxt;
                    item.index = $listMedia.children("li").length + 1;
                    itemHtml = mediaListFn(item);
                    addFlag = true;
                })
                .next(".btn-primary").bind("click", function(){
                    if(addFlag){
                        $listMedia.append(itemHtml)
                            .sortable();
                        $("#mediaCount").text($listMedia.children("li").length);
                        addFlag = false;
                    }
                });
        }

        function numParseCN(n){
            var num = ["零","一","二","三","四","五","六","七","八","九","十"];
            n = typeof n == "number" ? n : parseInt(n) ;
            return (n/10)>1 ? (num[n/10] + num[10] + num[n%10]) : num[n];
        }

		//加载删除课程 	
		rightCont.loadDeleteCoursePage = function (title, fdid){	
			
			data.pageTitle = title;	
			$("#rightCont").html(deleteCourseFn(data));	
			
			$("#deleteCourse").click(function(){
				/*============================================ ajax 删除当前课程 ================================================*/
				/*$.post("url?method=delete", {id:fdid})
					.success(function(){
						// some code
					});
				*/
				urlRouter("sectionsDirectory");	//跳转到章节目录  或者其它页面
			});
				
			
		}
		
		//加载授权管理 	
		rightCont.loadKinguserPage = function (title){			
			/*============================================ ajax 加载JSON数据 ================================================*/
			/*$.getJSON("url?load",function(rsult){
				data = rsult;
				data.pageTitle = title;
				$("#rightCont").html(kinguserFn(data));	
					
			});*/
			$.ajax({
				  url: $("#ctx").val()+"/ajax/course/getAuthInfoByCourseId?courseId="+$("#courseId").val(),
				  async:false,
				  success: function(rsult){
					  data = $.parseJSON(rsult);
				  }
			});
			/*data = {//ajax 成功后删除		
				user: [
					{
						id: "fdid323",
						index: 2,
						imgUrl: "http://img.staff.xdf.cn/Photo/06/3A/a911e1178bf3725acd75ddbb9c7e3a06_9494.jpg",
						name: "杨义锋",
						mail: "yangyifeng@xdf.cn",
						org: "集团总公司",
						department: "知识管理中心",
						tissuePreparation: true,
						editingCourse: true
					},
					{
						id: "fdid324",
						index: 1,
						imgUrl: "",
						name: "刘鹍",
						mail: "liukun@xdf.cn",
						org: "集团总公司",
						department: "知识管理中心",
						tissuePreparation: false,
						editingCourse: true
					},
					{
						id: "fdid325",
						index: 0,
						imgUrl: "",
						name: "魏紫",
						mail: "weizi5@xdf.cn",
						org: "广州学校",
						department: "国外考试部",
						tissuePreparation: true,
						editingCourse: false
					}
				]
			}*/
			data.pageTitle = title;	//ajax 成功后删除
			$("#rightCont").html(kinguserFn(data));//ajax 成功后删除
			
			$("#list_user").sortable({
				handle: '.state-dragable',
				forcePlaceholderSize: false
			})
			.find("a.icon-remove-blue").bind("click",function(e){
				e.preventDefault();
				$(this).closest("tr").remove();
			});
			var allUserData ;
			
			/*$.getJSON("${ctx}/ajax/user/findByName?q="+$('#addUser').val(),function(rsult){
				allUserData = rsult;
			});*/
			/*allUserData = [
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
			]*/
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
			$("#addUser").autocomplete($("#ctx").val()+"/ajax/user/findByName",{
				formatMatch: function(item) { 
					return item.name + item.mail + item.org + item.department; 
				},
				formatItem: function(item) { 
					return '<img src="' 
						+ (item.imgUrl || 'images/temp-face36.jpg') + '" alt="">' 
						+ item.name + '（' + item.mail + '），' 
						+ item.org + '  ' + item.department; 
				},
				parse : function(data) {
					var rows = [];
					for ( var i = 0; i < data.length; i++) {
						rows[rows.length] = {
							data : data[i],
							value : data[i].name,
							result : data[i].name
						//显示在输入文本框里的内容 ,
						};
					}
					return rows;
				},
				extraParams : {
					q : function() {
						return $('#addUser').val();
					},
					deptId : function() {
						return $('#schId').val();
					}
				},
				dataType : 'json',
				matchContains:true ,
				max: 10,
				scroll: false,
				width:688
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
			
			//提交人员授权数据
			$("#submitUser").click(function(){
				var data = [];
				$("#list_user>tr").each(function(){
					data.push({
						id: $(this).attr("data-fdid"),
						index: $(this).index(),
						tissuePreparation: $(this).find(".tissuePreparation").is(":checked"),
						editingCourse: $(this).find(".editingCourse").is(":checked")
					});
				});console.log(JSON.stringify(data));
				//ajax
				/*$.post("url?updata",data)
				.success(function(){
					alert("保存成功");
				});*/
			});
		}
		
		//加载访问权限 	
		rightCont.loadAccessRightPage = function (title){
			/*============================================ ajax 加载JSON数据 ================================================*/
			$.ajax({
				  url: $('#ctx').val()+"/ajax/course/getIsPublishInfo?courseId="+$('#courseId').val(),
				  async:false,
				  dataType:'json',
				  success: function(rsult){
					  data = rsult;
				  },
			});
			/*data = {//ajax 成功后删除
				action: "",//form表单action			
				permission: "encrypt",
				encryptType: "passwordProtect",
				coursePwd: "123123123"
			}*/
			data.pageTitle = title;	//ajax 成功后删除
			$("#rightCont").html(accessRightFn(data));//ajax 成功后删除	
			
			$("#formAccessRight").validate({
				rules: {					
					coursePwd: {
						required: "#passwordProtect:checked",
						minlength: 6
					}
				},
				messages: {
					coursePwd: {
						required: "请输入密码",
						minLength: "密码必须大于6位"
					}
				}
			});
			
			//选择加密类型事件
			$("#formAccessRight #encrypt .radio").bind("click",function(){	
				if($(this).attr("for") == "passwordProtect"){
					$("#passwordProtect").attr("checked",true);//兼容ie7
					$("#coursePwd").removeAttr("disabled");
				} else {
					$("#coursePwd").attr("disabled",true);
				}						
			});	
			$('#formAccessRight a[data-toggle="tab"]').bind('click', function (e) {
				var href = 	e.target.href.split("#").pop();		
				$("#permission").val(href);
				$("#encrypt").find("input").not($("#passwordProtect").is(":checked") ? null : $("#coursePwd")).attr("disabled", href != "encrypt");								
			});
		}
		
		//加载课程推广	
		rightCont.loadPromotionPage = function (title){
			/*============================================ ajax 加载JSON数据 ================================================*/
			/*$.getJSON("url?load",function(rsult){
				data = rsult;
				data.pageTitle = title;
				$("#rightCont").html(promotionFn(data));	
					
			});*/
			data = {//ajax 成功后删除
				action: "#",//form表单action			
				coverUrl: "",
				courseSkin: {title: "国内考试"},
				courseSkinList: [
					{title: "国外考试", imgUrl: "images/courseSkin-01.png"},
					{title: "国内考试", imgUrl: "images/courseSkin-02.png"},
					{title: "英语学习", imgUrl: "images/courseSkin-03.png"},
					{title: "优能中学", imgUrl: "images/courseSkin-04.png"},
					{title: "优能小学", imgUrl: "images/courseSkin-05.png"}]
			}
			data.pageTitle = title;	//ajax 成功后删除
			$("#rightCont").html(promotionFn(data));//ajax 成功后删除	
					
			//选择课程皮肤事件
			$("#formPromotion .courseSkinList>li>a").bind("click",function(e){
				e.preventDefault();
				$(this).parent().addClass("active").siblings().removeClass("active");
				$("#courseSkin").val($(this).next("h5").text());
			});			
		}
		
		//加载详细信息	
		rightCont.loadDetailInfoPage = function (title){
			/*============================================ ajax 加载JSON数据 ================================================*/
			$.ajax({
				  url: $('#ctx').val()+"/ajax/course/getDetailCourseInfoById?courseId="+$('#courseId').val(),
				  async:false,
				  dataType:'json',
				  success: function(rsult){
					  data = rsult;
						data.pageTitle = title;
						$("#rightCont").html(detailInfoFn(data));	
				  },
			});
			/*
			data = {//ajax 成功后删除
				action: "#",//form表单action			
				courseAbstract: "",
				learnObjectives: ["了解雅思考试基本情况","了解英国留学基本情况"],
				suggestedGroup: ["新教师","学校主管"],
				courseRequirements: ["学习本课程的教师必须通过TELTS考试","准备好3-8教材"]
			}
			data.pageTitle = title;	//ajax 成功后删除
			$("#rightCont").html(detailInfoFn(data));//ajax 成功后删除	
			*/		
			$("#formDetailInfo").validate();	
			
			//添加字段列表项事件
			$("#formDetailInfo .formAdd>.btn").bind("click",function(e){
				e.preventDefault();
				if($(this).prevAll(":text").val()){
					var $field = $(this).parent().prev(".list_alert").prev(":hidden");
					var tit = $(this).prevAll(":text").val();
					var $item = $(itemDetailInfoFn(tit));
					$item.bind("closed",delItem);
					$(this).parent().removeClass("warning").prev(".list_alert").append($item);
					var _val = $field.val();
					$field.val(_val + "#" + tit);
					$(this).prevAll(":text").val("");
				} else {
					$(this).parent().addClass("warning");
				}	
					
			});
			//删除字段列表项事件
			$("#formDetailInfo .list_alert>.alert").bind("click",delItem);
			function delItem(){
				var arr = '';
				$(this).siblings(".alert").each(function(){
					arr = arr + $(this).children("span").text()+'#';
				})
				$(this).parent().prev(":hidden").val(arr);
			}			
			
		}	
		
		//加载基本信息	
		rightCont.loadBasicInfoPage = function (title){
			/*============================================ ajax 加载基本信息数据 ================================================*/
			$.ajax({
				  url: $('#ctx').val()+"/ajax/course/getBaseCourseInfoById?courseId="+$('#courseId').val(),
				  async:false,
				  dataType:'json',
				  success: function(rsult){
						data = rsult ;	
						data.pageTitle = title;
						$("#rightCont").html(basicInfoFn(data));
				  },
			});
			/*
			data = {//ajax 成功后删除
				action: "#",//模板详情_基本信息 的form表单action			
				courseTit: "集团英联邦项目雅思强化口语备课课程",
				subTit: "",
				keyword: ["雅思","新教师备课"],
				courseType: "1",
				courseTypeList: [
					{title: "国外考试", id: "1"},
					{title: "国内考试", id: "2"},
					{title: "英语学习", id: "3"},
					{title: "优能中学", id: "4"},
					{title: "优能小学", id: "5"}]		
			}
			data.pageTitle = title;	//ajax 成功后删除
			$("#rightCont").html(basicInfoFn(data));//ajax 成功后删除
			*/
			$("#formBasicInfo").validate();	
					
			//添加关键词事件
			$("#formBasicInfo .keywordWrap>.btn-add").bind("click",function(e){
				e.preventDefault();
				var $addBtn = $(this);			
				if(!$addBtn.next().hasClass("inpKeyword")){
					$addBtn.after(addKeywordFn())
					.next(".inpKeyword").children(".btn").bind("click",function(e){
						e.preventDefault();				
						if($(this).hasClass("btn-primary")){
							if($(this).prevAll(":text").val()){
								var tit = $(this).prevAll(":text").val();
								$addBtn.before(tagKeywordFn({keyword: tit})).prev().bind("closed",delKeyword);
								var _val = $("#keyword").val();
								$("#keyword").val(_val + "," + tit);
								$(this).parent().remove();
							} else {
								$(this).parent().addClass("warning");
							}					
						} else {
							$(this).parent().remove();	
						}							
					});
				}			
			});
			//删除关键词事件
			$("#formBasicInfo .keywordWrap>.alert-tag").bind("closed",delKeyword);
			function delKeyword(){
				var arr = [];
				$(this).siblings(".alert-tag").each(function(){
					arr.push($(this).children("span").text());
				})
				$("#keyword").val(arr);
			}
			//选择课程类型事件
			$("#formBasicInfo .courseType>li>a").bind("click",function(e){
				e.preventDefault();
				$(this).parent().addClass("active").siblings().removeClass("active");
				$("#courseType").val($(this).next("input").val());
			});
			
		}
		
		//加载章节目录
		rightCont.loadSectionDirectoryPage = function(title){
			/*============================================ ajax 加载章节数据 ================================================*/
			$.ajax({
				  url: $('#ctx').val()+"/ajax/catalog/getCatalogJsonByCourseId?courseId="+$('#courseId').val(),
				  async:false,
				  dataType:'json',
				  success: function(rsult){
						data = rsult ;	
						data.pageTitle = title;		
						$("#rightCont").html(loadsectionsDirectoryFn(data));
						updataProgressCourses($('#sortable').children(".lecture").length);	
				  },
			});
			/*
			data = {	//ajax 成功后删除	
				chapter: [{
						id: 'fdid3214321' ,
						index: 0,
						num: 1,
						title: "业务学习"		
					},
					{
						id: 'fdid3221' ,
						index: 4,
						num: 2,
						title: "学术准备"		
					}],
				lecture: [{
						id: 'fdid32210000' ,
						index: 2,
						num: 2,
						title: "学习入门文档"	,
						type: "doc"	
					},
					{
						id: 'fdid32210239',
						index: 1,
						num: 1,
						title: "学习入门视频"	,
						type: "video"	
					},
					{
						id: 'fdid3frewf239',
						index: 3,
						num: 3,
						title: "参加在线考试"	,
						type: "none"	
					},
					{
						id: 'fdid3frewf000111',
						index: 6,
						num: 5,
						title: "参加在线考试"	,
						type: "task"	
					},
					{
						id: 'fdid3frewf000111',
						index: 5,
						num: 4,
						title: "参加学术考试"	,
						type: "video"	
					}]
			}
			*/
			var $sections = $('#sortable');
			$sections.sortable({
				handle: '.sortable-bar',
				forcePlaceholderSize: true
			})
			.bind("sortupdate",changIndex)//绑定拖放元素改变次序事件
			//绑定编辑标题按钮事件
			.delegate(".sortable-bar>.icon-pencil2","click",function(e){
				e.preventDefault();
				var $tit = $(this).prev(".title");
				var data = rtnSectionData($tit.closest("li").hasClass("chapter"),$tit.children(".index").text(),$tit.children(".name").text());		
				$(this).closest(".sortable-bar").addClass("hide").after(editTitleFn(data));
			})	
			//绑定删除章节按钮事件
			.delegate(".sortable-bar>.icon-remove","click",function(e){
				e.preventDefault();
				var $li = $(this).closest("li");
				//=====================================================可在此 ajax 删除章节数据==================================
				$.post($('#ctx').val()+'/ajax/catalog/deleteCatalogById',{fdid: $li.attr("data-fdid")})
				.success(function(){
					$li.remove();
					changIndex();
					updataProgressCourses($sections.children(".lecture").length);
				});
				/*
				$li.remove();
                changIndex();
				updataProgressCourses($sections.children(".lecture").length);
				*/
			})
			//绑定编辑节内容按钮事件
			.delegate(".sortable-bar>.btn-edit","click",function(e){
				e.preventDefault();
				if($(this).parent().next(".lecture-content").length){
					$(this).addClass("hide").parent().next(".lecture-content").removeClass("hide");
				}				
			})
			//绑定节内容关闭按钮事件
			.delegate(".lecture-content>.hd>a.icon-remove-sign","click",function(e){
				e.preventDefault();		
				$(this).closest(".lecture-content").addClass("hide").prevAll(".sortable-bar").children(".btn-edit").removeClass("hide");
								
			})
			//绑定保存章节标题按钮事件
			.delegate(".form-edit-title .btn-primary","click",function(e){
				e.preventDefault();
				var $form = $(this).closest(".form-edit-title");
				var $tit = $form.find("input.input-block-level");
				var $li = $form.parent("li");
				var data, fdId;
				if($tit.val()){			
					if($form.prev().hasClass("sortable-bar")){//编辑已有章节					
						//=====================================================可在此 ajax 更新章节名称数据==================================
						$.post($('#ctx').val()+'/ajax/catalog/updateCatalogNameById',{fdid: $li.attr("data-fdid"), title: $tit.val()},function(data){},"json")
						.success(function(){
							$li.children(".sortable-bar").removeClass("hide").find(".name").text($tit.val());
						});
						/*
						$li.children(".sortable-bar").removeClass("hide").find(".name").text($tit.val());
						*/
					} else {//新加章节							
						//=====================================================可在此 ajax 提交新加章节的数据, 返回fdId==============================
						$.ajax({
							  url: $('#ctx').val()+"/ajax/catalog/addCatalog",
							  async:false,
							  data:{courseid:$("#courseId").val(),ischapter:$li.hasClass("chapter"),fdtotalno:parseInt($sections.children("li").length)-1,fdno:$form.find(".index").text(),title:$tit.val()},
							  dataType:'json',
							  success: function(result){
								  var data = rtnSectionData($li.hasClass("chapter"),$form.find(".index").text(),$tit.val(),$li.length,"none",result.id);
									$("#courseId").val(result.courseid);
									$li.attr("data-fdid",result.id);
									$form.before(sectionsFn(data));	
									$sections.sortable({
										handle: '.sortable-bar',
										forcePlaceholderSize: true
									});
									var numAll = $form.find(".index").text();	
									updataProgressCourses(numAll);
							  },
						});
						/*
						var tempid = "sectionId0" + Math.random() ;					
						var data = rtnSectionData($li.hasClass("chapter"),$form.find(".index").text(),$tit.val(),$li.length,"none");
						$li.attr("data-fdid",tempid)					
						$form.before(sectionsFn(data));	
						$sections.sortable({
							handle: '.sortable-bar',
							forcePlaceholderSize: true
						});
						var numAll = $form.find(".index").text();				
						updataProgressCourses(numAll);
						*/
					}			
					$form.remove();
				} else {
					$form.find(".control-group:first").addClass("warning").find(":text").after('<span class="help-block">请填写标题！</span>');;
				}
			})
			//绑定取消章节标题编辑按钮事件
			.delegate(".form-edit-title .btn-link","click",function(e){
				e.preventDefault();
				$(this).closest(".form-edit-title").prev(".sortable-bar").removeClass("hide").end().remove();		
			})	
			//绑定输入框不为空时，去掉警告样式
			.delegate(":text","keydown",function(){
				if($(this).val() && $(this).closest(".control-group").hasClass("warning")){
					$(this).closest(".control-group").removeClass("warning").find(".help-block").remove();
				}		
			})
			//绑定课程类型按钮事件
			.delegate(".lecture-content>.bd>.btn-type","click",function(e){
				if($(this).hasClass("disabled")){
					e.preventDefault();
				} else {
                    var $tit = $(this).closest(".lecture-content").prev().find(".title");
                    urlRouter(false,{
                        id: $(this).closest(".lecture").attr("data-fdid"),
                        title: $tit.find(".name").text(),
                        index: $tit.find(".index").text()
                    });
                }
			});
            //更新章节顺序方法
            function changIndex(){
                var $items = $sections.children("li"),
                    i_ch = 1,
                    i_le = 1,
                    data = {
                		courseid : $("#courseId").val(),
                        chapter : [],
                        lecture : []
                    }, $item ;
                $items.each(function(i){
                    $item = $(this);
                    if($item.hasClass("chapter")){
                        $item.find(".title>.index").text(i_ch);
                        data.chapter.push({
                            index: i,
                            num: i_ch++,
                            id: $item.attr("data-fdid")
                        });
                    } else if($item.hasClass("lecture")){
                        $item.find(".title>.index").text(i_le);
                        data.lecture.push({
                            index: i,
                            num: i_le++,
                            id: $item.attr("data-fdid")
                        });
                    }
                });
                $.post($('#ctx').val()+"/ajax/catalog/updateCatalogOrder",{courseid : $("#courseId").val(),chapter:JSON.stringify(data.chapter),lecture:JSON.stringify(data.lecture)},function(res){},'json');// ajax 更新所有章节排序
            }
			//绑定添加章事件
			$("#addChapter").bind("click",function(){
				if(!$sections.children(".lecture").last().children(".form-edit-title").length){
					if(!$sections.children(".chapter").last().children(".form-edit-title").length){
						var data = {
							chapter: {
								index: $sections.children("li").length, 
								num: $sections.children(".chapter").length + 1
							}			
						}		
						$sections.append(addSectionsFn(data));			
					} else {			
						$sections.children(".chapter").last().find(".form-edit-title .control-group:first").addClass("warning").find(":text").after('<span class="help-block">请先完成未保存的章！</span>');
					}		
				} else {			
					$sections.children(".lecture").last().find(".form-edit-title .control-group:first").addClass("warning").find(":text").after('<span class="help-block">请先完成未保存的节！</span>');
				}	
					
			});
			//绑定添加节事件
			$("#addLecture").bind("click",function(){
				if(!$sections.children(".chapter").last().children(".form-edit-title").length){
					if(!$sections.children(".lecture").last().children(".form-edit-title").length){
						var data = {
							lecture: {
								index: $sections.children("li").length,
								num: $sections.children(".lecture").length + 1
							}
						}		
						$sections.append(addSectionsFn(data));			
					} else {			
						$sections.children(".lecture").last().find(".form-edit-title .control-group:first").addClass("warning").find(":text").after('<span class="help-block">请先完成未保存的节！</span>');
					}			
				} else {			
					$sections.children(".chapter").last().find(".form-edit-title .control-group:first").addClass("warning").find(":text").after('<span class="help-block">请先完成未保存的章！</span>');
				}
			});
		}
		
		//格式化章节数据
		function rtnSectionData(isChapter,nm,tit,i,typ,fdid){
			var data = {
					chapter : undefined,
					lecture : undefined
			}
			if(isChapter){
				data.chapter = {
						id: fdid ,
						index: i,
						num: nm,
						title: tit		
				}			
			} else {
				data.lecture = {
						id: fdid ,
						index: i,
						num: nm,
						title: tit,
						type: typ
				}			
			}
			return data;
		}
		
		//更新课程进度条
		function updataProgressCourses(numAll){
			var $prog = $("#progress_courses");
			var comp = numAll - $('#sortable').find(".sortable-bar>.title>.icon-none").length;
			$prog.find(".progress>.bar").width(comp/numAll*100 + "%");
			$prog.find(".num_comp").text(comp);
			$prog.find(".num_all").text(numAll);
			if(numAll > 0){
				if(numAll == comp){
					enabledPublish();
				} else {
					disabledPublish();
				}
			}
		}
		
		//激活预览和发布按钮方法
		function enabledPublish(){
			$(window).scrollTop(0);
			$("#page-title>.btn-group>.btn").removeAttr("disabled");
		}
		
		//disabled预览和发布按钮方法
		function disabledPublish(){
			$("#page-title>.btn-group>.btn").attr("disabled","disabled");
		}
		
	}());
	
	
	