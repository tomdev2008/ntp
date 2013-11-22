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
		};
		// 基本信息 模板函数
		var basicInfoFn = doT.template(document.getElementById("basicInfoTemplate").text, undefined, def);
		
		
		// 课程推广 模板函数
		var promotionFn = doT.template(document.getElementById("promotionTemplate").text, undefined, def);

		// 删除课程 模板函数
		var deleteSeriesFn = doT.template(document.getElementById("deleteSeriesTemplate").text, undefined, def);

        
		// 编辑章节标题模板函数
		var editTitleFn = doT.template(document.getElementById("formEditSectionTitle").text);
		// 添加章节模板函数
		var addSectionsFn = doT.template(document.getElementById("addSectionsTemplate").text, undefined, {
			edittitle:document.getElementById("formEditSectionTitle").text		
		});
		// 展示章节模板函数
		var sectionsFn = doT.template(document.getElementById("sectionsTemplate").text + document.getElementById("sectionBarTemplate").text + document.getElementById("lectureContentTemplate").text);
		// load章节目录模板函数
		var loadsectionsDirectoryFn = doT.template(document.getElementById("sectionDirectoryTemplate").text + document.getElementById("sectionBarTemplate").text + document.getElementById("lectureContentTemplate").text, undefined, def);



        function numParseCN(n){
            var num = ["零","一","二","三","四","五","六","七","八","九","十"];
            n = typeof n == "number" ? n : parseInt(n) ;
            return (n/10)>1 ? (num[n/10] + num[10] + num[n%10]) : num[n];
        }

		// 加载删除课程
		rightCont.loadDeleteCoursePage = function (title, fdid){	
			data.pageTitle = title;	
			$("#rightCont").html(deleteSeriesFn(data));	
			
			$("#deleteCourse").click(function(){confirmDel();});
			function confirmDel(){
				$.fn.jalert("您确认要删除当前课程？",deleteCourse)
			}
			// 调用ajax删除当前课程
			function deleteCourse(){
				/*
				 * ============================================ ajax 删除当前课程
				 * ================================================
				 */
				 $.post($('#ctx').val()+"/ajax/course/deleteCourse",{
					 courseId:fdid
		         })
		             .success(function(){
		                 // 提交成功
		            	 window.location.href=$('#ctx').val()+"/course/findcourseInfos";
		             });
			}
		}
		

		
		// 加载系列推广
		rightCont.loadPromotionPage = function (title){
			/*
			 * ============================================ ajax 加载JSON数据
			 * ================================================
			 */
			$.ajax({
				  url: $('#ctx').val()+"/ajax/course/showcover?courseId="+$('#courseId').val(),
				  async:false,
				  dataType:'json',
				  success: function(rsult){
					  if(rsult.coverUrl==""){
						  data.coverUrl = "";
					  }else{
						  data.coverUrl =  $('#ctx').val()+"/common/file/image/"+rsult.coverUrl;
					  }
					  data.courseSkinList = [
					     			{title: "国外考试", imgUrl:  $('#ctx').val()+"/resources/images/courseSkin-01.png"},
					    			{title: "国内考试", imgUrl:  $('#ctx').val()+"/resources/images/courseSkin-02.png"},
					    			{title: "英语学习", imgUrl:  $('#ctx').val()+"/resources/images/courseSkin-03.png"},
					    			{title: "优能中学", imgUrl:  $('#ctx').val()+"/resources/images/courseSkin-04.png"},
					    			{title: "优能小学", imgUrl:  $('#ctx').val()+"/resources/images/courseSkin-05.png"}];
					  data.courseSkin= {title: "国内考试"};
					  data.pageTitle = title;	// ajax 成功后删除
					  $("#rightCont").html(promotionFn(data));// ajax 成功后删除
				  },
			});
			/*
			 * $.getJSON("url?load",function(rsult){ data = rsult;
			 * data.pageTitle = title; $("#rightCont").html(promotionFn(data));
			 * 
			 * });
			 */
			// data = {//ajax 成功后删除
			// action: "#",//form表单action
			// coverUrl: "",
			// courseSkin: {title: "国内考试"},
			// courseSkinList: [
			// {title: "国外考试", imgUrl: "images/courseSkin-01.png"},
			// {title: "国内考试", imgUrl: "images/courseSkin-02.png"},
			// {title: "英语学习", imgUrl: "images/courseSkin-03.png"},
			// {title: "优能中学", imgUrl: "images/courseSkin-04.png"},
			// {title: "优能小学", imgUrl: "images/courseSkin-05.png"}]
			// }
			data.pageTitle = title;	// ajax 成功后删除
			$("#rightCont").html(promotionFn(data));// ajax 成功后删除
					
			// 选择课程皮肤事件
			$("#formPromotion .courseSkinList>li>a").bind("click",function(e){
				e.preventDefault();
				$(this).parent().addClass("active").siblings().removeClass("active");
				$("#courseSkin").val($(this).next("h5").text());
			});		
			/* 课程推广 封页图片上传 */
			var $progress ,flag = true,pct,interval,countdown = 0,byteUped = 0;

			jQuery("#upMovie").uploadify({
				        'height' : 40,
	                    'width' : 68,
	                    'multi' : false,
	                    'simUploadLimit' : 1,
	                    'swf' : $('#ctx').val()+'/resources/uploadify/uploadify.swf',
	                    "buttonClass": "btn btn-primary btn-large",
	                    'buttonText' : '上 传',
	                    'uploader' : $('#ctx').val()+'/common/file/o_upload',
	                    'auto' : true,// 选中后自动上传文件
	                    'fileTypeExts' : '*.jpg;*.png;',
	                    'fileSizeLimit':2048,// 限制文件大小为2m
	                    'onInit' : function(){
	                    	$progress = $('<span class="progress"><div class="bar" style="width:0%;"></div> </span>\
	                		<span class="txt"><span class="pct">0%</span><span class="countdown"></span></span>');
	                    	$("#upMovie").next(".uploadify-queue").remove();
	                    },
	                    'onUploadStart' : function (file) {
	                    	$("#upMovie").before($progress);
	                        //$uploadBtn.uploadify("settings", "formData");
	                    },
	                    'onUploadSuccess' : function (file, data, Response) {
	                        if (Response) {
	                        	$progress.find(".countdown").empty();
	                        	var objvalue = eval("(" + data + ")");
	                            jQuery("#attIdID").val(objvalue.attId);
	                           if (jQuery("#imgshow")) {
                       			jQuery("#imgshow").attr('src',  $('#ctx').val()+'/common/file/image/' + objvalue.attId);
                   			   } 
	                          
	                        }
	                    },
	                    'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
	                    	pct = Math.round((bytesUploaded/bytesTotal)*100)+'%';
	                    	byteUped = bytesUploaded;
	                    	if(flag){
	                    		interval = setInterval(uploadSpeed,100);
	                    		flag = false;
	                    	}
	                    	if(bytesUploaded == bytesTotal){
	                    		clearInterval(interval);
	                    		
	                    	}
	                    	$progress.find(".bar").width(pct).end().find(".pct").text(pct);
	                    	countdown>0 && $progress.find(".countdown").text(secTransform((bytesTotal-bytesUploaded)/countdown));
	                    }
	         });
	                function uploadSpeed(){
	            		countdown = byteUped - countdown;
	            	}
	            	function secTransform(s){
	            		if( typeof s == "number"){
	            			s = Math.ceil(s);
	            			var t = "";
	            			if(s>3600){
	            				t= Math.ceil(s/3600) + "小时" + Math.ceil(s%3600/60) + "分钟" + s%3600%60 + "秒";
	            			} else if(s>60){
	            				t= Math.ceil(s/60) + "分钟" + s%60 + "秒";
	            			} else {
	            				t= s + "秒";
	            			}
	            			return "，剩余时间：" + t;
	            		}else{
	            			return null;
	            		}		
	            	}

		}
		// 加载基本信息
		rightCont.loadBasicInfoPage = function (title){
			/*
			 * ============================================ ajax 加载基本信息数据
			 * ================================================
			 */
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
			 * data = {//ajax 成功后删除 action: "#",//模板详情_基本信息 的form表单action
			 * courseTit: "集团英联邦项目雅思强化口语备课课程", subTit: "", keyword:
			 * ["雅思","新教师备课"], courseType: "1", courseTypeList: [ {title:
			 * "国外考试", id: "1"}, {title: "国内考试", id: "2"}, {title: "英语学习", id:
			 * "3"}, {title: "优能中学", id: "4"}, {title: "优能小学", id: "5"}] }
			 * data.pageTitle = title; //ajax 成功后删除
			 * $("#rightCont").html(basicInfoFn(data));//ajax 成功后删除
			 */
			$("#formBasicInfo").validate();	
			$("#formBasicInfo").find(".btns-radio>.btn").on("click",function(){
                $("#sectionOrder").val(this.id);
            });		
			// 添加关键词事件
			$("#formBasicInfo .keywordWrap>.btn-add").bind("click",function(e){
				e.preventDefault();
				var $addBtn = $(this);			
				if(!$addBtn.next().hasClass("inpKeyword")){
					$addBtn.after(addKeywordFn())
					.next(".inpKeyword").children(".btn").bind("click",function(e){
						e.preventDefault();				
						if($(this).hasClass("btn-primary")){
							$(this).next().next(".help-block").remove();
							if($(this).prevAll(":text").val()){
								var tit = $(this).prevAll(":text").val();
								var _val = $("#keyword").val();
								if(_val.indexOf(tit)<0){
									$addBtn.before(tagKeywordFn({keyword: tit})).prev().bind("click",delKeyword);
									$("#keyword").val(_val + "," + tit);
									$(this).parent().remove();
								}else{
									$(this).next().after('<span class="help-block">不能添加重复的关键词！</span>');
									$("#addKey").val("");
								}								
							} else {
								$(this).parent().addClass("warning");
							}					
						} else {
							$(this).parent().remove();	
						}							
					});
				}
				$("#addKey").autocomplete($("#ctx").val()+"/ajax/course/findTagInfosByKey",{
					formatMatch: function(item) {
						return item.fdName + item.fdDescription; 
					},
					formatItem: function(item) {
						$("#addKey").parent().removeClass("warning").find(".help-block").remove();
						return item.fdName; 
					},
					parse : function(data) {
						$("#addKey").parent().removeClass("warning").find(".help-block").remove();
						var rows = [];
						for ( var i = 0; i < data.length; i++) {
							rows[rows.length] = {
								data : data[i],
								value :  data[i].fdName,
								result :  data[i].fdName
							// 显示在输入文本框里的内容 ,
							};
						}
						return rows;
					},
					dataType : 'json',
					matchContains:true ,
					max: 10,
					scroll: false,
					width:336
				});
			});
			// 删除关键词事件
			$("#formBasicInfo .keywordWrap>.alert-tag").bind("click",delKeyword);
			function delKeyword(){
				var arr = [];
				$(this).siblings(".alert-tag").each(function(){
					arr.push($(this).children("span").text());
				})
				$("#keyword").val(arr);
			}
			// 选择课程类型事件
			$("#formBasicInfo .courseType>li>a").bind("click",function(e){
				e.preventDefault();
				$(this).parent().addClass("active").siblings().removeClass("active");
				$("#courseType").val($(this).next("input").val());
			});
			
		}
		
		// 加载章节目录
		rightCont.loadSectionDirectoryPage = function(title){
			/*
			 * ============================================ ajax 加载章节数据
			 * ================================================
			 */
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
			 * data = { //ajax 成功后删除 chapter: [{ id: 'fdid3214321' , index: 0,
			 * num: 1, title: "业务学习" }, { id: 'fdid3221' , index: 4, num: 2,
			 * title: "学术准备" }], lecture: [{ id: 'fdid32210000' , index: 2, num:
			 * 2, title: "学习入门文档" , type: "doc" }, { id: 'fdid32210239', index:
			 * 1, num: 1, title: "学习入门视频" , type: "video" }, { id:
			 * 'fdid3frewf239', index: 3, num: 3, title: "参加在线考试" , type: "none" }, {
			 * id: 'fdid3frewf000111', index: 6, num: 5, title: "参加在线考试" , type:
			 * "task" }, { id: 'fdid3frewf000111', index: 5, num: 4, title:
			 * "参加学术考试" , type: "video" }] }
			 */
			var $sections = $('#sortable');
			$sections.sortable({
				handle: '.sortable-bar',
				forcePlaceholderSize: true
			})
			.bind("sortupdate",changIndex)// 绑定拖放元素改变次序事件
			// 绑定编辑标题按钮事件
			.delegate(".sortable-bar>.icon-pencil2","click",function(e){
				e.preventDefault();
				var $tit = $(this).prev(".title");
				var data = rtnSectionData($tit.closest("li").hasClass("chapter"),$tit.children(".index").text(),$tit.children(".name").text());		
				$(this).closest(".sortable-bar").addClass("hide").after(editTitleFn(data));
			})	
			// 绑定删除章节按钮事件
			.delegate(".sortable-bar>.icon-remove","click",function(e){
				e.preventDefault();
				var $li = $(this).closest("li");
				// =====================================================可在此 ajax
				// 删除章节数据==================================
				$.post($('#ctx').val()+'/ajax/catalog/deleteCatalogById',{fdid: $li.attr("data-fdid")})
				.success(function(){
					$li.remove();
					changIndex();
					updataProgressCourses($sections.children(".lecture").length);
				});
				/*
				 * $li.remove(); changIndex();
				 * updataProgressCourses($sections.children(".lecture").length);
				 */
			})
			// 绑定编辑节内容按钮事件
			.delegate(".sortable-bar>.btn-edit","click",function(e){
				e.preventDefault();
				if($(this).parent().next(".lecture-content").length){
					$(this).addClass("hide").parent().next(".lecture-content").removeClass("hide");
				}				
			})
			// 绑定节内容关闭按钮事件
			.delegate(".lecture-content>.hd>a.icon-remove-sign","click",function(e){
				e.preventDefault();		
				$(this).closest(".lecture-content").addClass("hide").prevAll(".sortable-bar").children(".btn-edit").removeClass("hide");
								
			})
			// 绑定保存章节标题按钮事件
			.delegate(".form-edit-title .btn-primary","click",function(e){
				e.preventDefault();
				var $form = $(this).closest(".form-edit-title");
				var $tit = $form.find("input.input-block-level");
				var $li = $form.parent("li");
				var data, fdId;
				if($tit.val()){			
					if($form.prev().hasClass("sortable-bar")){// 编辑已有章节
						// =====================================================可在此
						// ajax 更新章节名称数据==================================
						$.post($('#ctx').val()+'/ajax/catalog/updateCatalogNameById',{fdid: $li.attr("data-fdid"), title: $tit.val()},function(data){},"json")
						.success(function(){
							$li.children(".sortable-bar").removeClass("hide").find(".name").text($tit.val());
						});
						/*
						 * $li.children(".sortable-bar").removeClass("hide").find(".name").text($tit.val());
						 */
					} else {// 新加章节
						// =====================================================可在此
						// ajax 提交新加章节的数据, 返回fdId==============================
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
						 * var tempid = "sectionId0" + Math.random() ; var data =
						 * rtnSectionData($li.hasClass("chapter"),$form.find(".index").text(),$tit.val(),$li.length,"none");
						 * $li.attr("data-fdid",tempid)
						 * $form.before(sectionsFn(data)); $sections.sortable({
						 * handle: '.sortable-bar', forcePlaceholderSize: true
						 * }); var numAll = $form.find(".index").text();
						 * updataProgressCourses(numAll);
						 */
					}			
					$form.remove();
				} else {
					$form.find(".control-group:first").addClass("warning").find(":text").after('<span class="help-block">请填写标题！</span>');;
				}
			})
			// 绑定取消章节标题编辑按钮事件
			.delegate(".form-edit-title .btn-link","click",function(e){
				e.preventDefault();
				var $form = $(this).closest(".form-edit-title");
				var $li = $form.parent("li");
				if(!$li.attr("data-fdid")){
					$li.remove();
				}
				$(this).closest(".form-edit-title").prev(".sortable-bar").removeClass("hide").end().remove();		
			})	
			// 绑定输入框不为空时，去掉警告样式
			.delegate(":text","keydown",function(){
				if($(this).val() && $(this).closest(".control-group").hasClass("warning")){
					$(this).closest(".control-group").removeClass("warning").find(".help-block").remove();
				}		
			})
			// 绑定课程类型按钮事件
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
            // 更新章节顺序方法
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
                $.post($('#ctx').val()+"/ajax/catalog/updateCatalogOrder",{courseid : $("#courseId").val(),chapter:JSON.stringify(data.chapter),lecture:JSON.stringify(data.lecture)},function(res){},'json');// ajax
																																																				// 更新所有章节排序
            }
			// 绑定添加阶段事件
			$("#addSeries").bind("click",function(){
				if(!$sections.children(".lecture").last().children(".form-edit-title").length){
					if(!$sections.children(".chapter").last().children(".form-edit-title").length){
						var data = {
							chapter: {
								index: $sections.children("li").length, 
								num: $sections.children(".chapter").length + 1
							}			
						};		
						$sections.append(addSectionsFn(data));			
					} else {
						$sections.children(".chapter").last().find(".form-edit-title .control-group:first").removeClass("warning").find(".help-block").remove();
						$sections.children(".chapter").last().find(".form-edit-title .control-group:first").addClass("warning").find(":text").after('<span class="help-block">请先完成未保存的阶段！</span>');
					}		
				} else {
					$sections.children(".lecture").last().find(".form-edit-title .control-group:first").removeClass("warning").find(".help-block").remove();
					$sections.children(".lecture").last().find(".form-edit-title .control-group:first").addClass("warning").find(":text").after('<span class="help-block">请先完成未保存的课程！</span>');
				}	
					
			});
			// 绑定添加课程事件
			$("#addCourse").bind("click",function(){
				if(!$sections.children(".chapter").last().children(".form-edit-title").length){
					if(!$sections.children(".lecture").last().children(".form-edit-title").length){
						var data = {
							lecture: {
								index: $sections.children("li").length,
								num: $sections.children(".lecture").length + 1
							}
						};		
						
						$sections.append(addSectionsFn(data));			
					} else {
						$sections.children(".lecture").last().find(".form-edit-title .control-group:first").removeClass("warning").find(".help-block").remove();
						$sections.children(".lecture").last().find(".form-edit-title .control-group:first").addClass("warning").find(":text").after('<span class="help-block">请先完成未保存的课程！</span>');
					}			
				} else {
					$sections.children(".chapter").last().find(".form-edit-title .control-group:first").removeClass("warning").find(".help-block").remove();
					$sections.children(".chapter").last().find(".form-edit-title .control-group:first").addClass("warning").find(":text").after('<span class="help-block">请先完成未保存的阶段！</span>');
				}
				alert($("input[name=courses]").length);
				initCourseSelect();
			});
			/****************************************/
			   
		}
		
		// 格式化章节数据
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
		
		// 更新课程进度条
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
		
		// 激活预览和发布按钮方法
		function enabledPublish(){
			$(window).scrollTop(0);
			$("#page-title>.btn-group>.btn").removeAttr("disabled");
		}
		
		// disabled预览和发布按钮方法
		function disabledPublish(){
			$("#page-title>.btn-group>.btn").attr("disabled","disabled");
		}

	}());
	
	
	