<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>

<head>
<link rel="stylesheet" href="${ctx}/resources/css/global.css" />
<link href="${ctx}/resources/css/home.css" rel="stylesheet" type="text/css">
</head>

<body>

<div class="banner-bg">
  <div class="container banner">
    <a href="#" class="start-btn"></a>
  </div>
</div>
<div class="bar-tools">
	<div class="container">
		<div class="bar-tools-wrap">
			<ul class="counting nav clearfix">
				<li>
                    <i class="icon-school-home"></i>
                    <span>
                       <h2>30+</h2>
                        学校
                    </span>
                </li>
				<li>
                    <i class="icon-teacher-home"></i>
                	<span><h2>1,000+</h2>
                    新教师 </span>
                </li>
                <li>
                    <i class="icon-mentor-home"></i>
                	<span><h2>1,000+</h2>
                    导师 </span>
                </li>
                <li>
                    <i class="icon-book-home"></i>
                	<span><h2>1,000+</h2>
                    课程</span>
                </li>
			</ul>
		</div>
	</div>
</div>
<div class="container">
	<div class="page-header">
		<h3>备课平台产品介绍</h3>
	</div>
	<div class="proIntro">
        <ul class="listIntro thumbnails">
            <li class="thumbnail">
                <i class="icon-intro-1"></i>
                <h4>免费在线课程</h4>
                <p>淘宝上<em>连衣裙的搜</em>索趋势是怎样的？如商品、行业、事件等）的搜索和成交走势。</p>
            </li>
            <li class="divider">
                <i class="icon-circle-home"></i>
            </li>
            <li class="thumbnail">
                <i class="icon-intro-2"></i>
                <h4>集团教研分享</h4>
                <p>淘宝上搜索、购买<em>iPhone4的都是什么样</em>的人？用淘宝指数查看不同商品的消费人群特征。</p>
            </li>
            <li class="divider">
                <i class="icon-circle-home"></i>
            </li>
            <li class="thumbnail">
                <i class="icon-intro-3"></i>
                <h4>教师职业发展</h4>
                <p>最近7天淘宝最火的搜索词、<em>行业和品牌</em>是？
                    基于淘宝搜索和成交的排行榜观数据清晰呈现。</p>
            </li>
            <li class="divider">
                <i class="icon-circle-home"></i>
            </li>
            <li class="thumbnail">
                <i class="icon-intro-4"></i>
                <h4>名师公开课堂</h4>
                <p>淘宝上搜索、购买<em>iPhone4的都是什么样</em>的人？用淘宝指数查看不同商品的消费人群特征。</p>
            </li>
            <li class="divider">
                <i class="icon-circle-home"></i>
            </li>
            <li class="thumbnail">
                <i class="icon-intro-5"></i>
                <h4>学校师训跟踪</h4>
                <p>最近7天淘宝最火的搜索词、<em>行业和品牌</em>是？
                    基于淘宝搜索和成交的排行榜观数据清晰呈现。</p>
            </li>
            <li class="divider">
                <i class="icon-circle-home"></i>
            </li>
            <li class="thumbnail">
                <i class="icon-intro-6"></i>
                <h4>学习通关证书</h4>
                <p>淘宝上<em>连衣裙的搜</em>索趋势是怎样的？如商品、行业、事件等）的搜索和成交走势。</p>
            </li>
        </ul>
    </div>
	<div class="page-header">
		<h3>倾听一线的心声</h3>
	</div>
  <div class="tabbable tabs-below">
   	<div class="tab-content">
			<div class="tab-pane active" id="tab1">
    			<div><p><i class="icon-shyhl"></i>初入职场时，尤其感觉到了职场技能的欠缺。工作之外的时间可以来这里看看，随时都可以学习，感觉真心不错！<i class="icon-shyhr"></i></p></div>
    		</div>
    		<div class="tab-pane" id="tab2">
   				<div><p><i class="icon-shyhl"></i>下班后会来看看有没有更新，然后学习一下，很受用。<i class="icon-shyhr"></i></p></div>
    		</div>
    		<div class="tab-pane" id="tab3">
    			<div><p><i class="icon-shyhl"></i>在这里发现很多熟悉的培训师视频教程，真心不错！<i class="icon-shyhr"></i></p></div>
    		</div>
    		<div class="tab-pane" id="tab4">
    			<div><p><i class="icon-shyhl"></i>手头的资料大多是零散知识片段，真想学点什么的时候总缺乏系统性的知识体系。在线备课平台很不错，把知识有机的组织在一起，让人耳目一新。足见信息的组织精炼同样非常重要！<i class="icon-shyhr"></i></p></div>
    		</div>
   	  </div>
   	  <ul class="nav nav-tabs" id="myTabs">
   		  <li class="active"><a href="#tab1" data-toggle="tab">
       	  	<img src="${ctx}/resources/images/temp-face70.jpg" width="70" height="70">
            <h5>Grace</h5>
            <p>雅思教师</p>
          </a></li>
   		  <li><a href="#tab2" data-toggle="tab">
       	  	<img src="${ctx}/resources/images/temp-face70.jpg" width="70" height="70">
            <h5>Eric</h5>
            <p>托福教师</p>
          </a></li>
   		  <li><a href="#tab3" data-toggle="tab">
       	  	<img src="${ctx}/resources/images/temp-face70.jpg" width="70" height="70">
            <h5>Daniel</h5>
            <p>考研教师</p>
          </a></li>
   		  <li><a href="#tab4" data-toggle="tab">
       	  	<img src="${ctx}/resources/images/temp-face70.jpg" width="70" height="70">
            <h5>Felix</h5>
            <p>SAT教师</p>
          </a></li>
          <li class="caret"></li>
   	  </ul>
    </div>
     <div class="page-header">
		<h3>新东方与你同在</h3>
	</div>
	<div class="grid clearfix" id="grid">
        <div class="pull-left w385">
            <div class="media">
                <a class="pull-left" href="#">
                    <img src="${ctx}/resources/images/hoverfold-01.jpg" alt=""/>
                </a>
                <div class="media-body">
                    <div class="hd">北京学校</div>
                    <div class="bd">
                        <i class="icon-shyhl"></i>
                        <h4>不要只懂生存
                            不懂生活</h4>
                        <i class="icon-shyhr"></i>
                    </div>
                    <div class="ft">
                        新教师 99+ <span>|</span>
                        导师 700+
                    </div>
                </div>
            </div>
            <div class="media">
                <a class="pull-left" href="#">
                    <img src="${ctx}/resources/images/hoverfold-01.jpg" class="media-object" alt=""/>
                </a>
                <div class="media-body">
                    <div class="hd">北京学校</div>
                    <div class="bd">
                        <i class="icon-shyhl"></i>
                        <h4>不要只懂生存
                            不懂生活</h4>
                        <i class="icon-shyhr"></i>
                    </div>
                    <div class="ft">
                        新教师 99+ <span>|</span>
                        导师 700+
                    </div>
                </div>
            </div>
        </div>
        <div class="media">
            <a class="" href="#">
                <img src="${ctx}/resources/images/hoverfold-01.jpg" class="media-object" alt=""/>
            </a>
            <div class="media-body">
                <div class="hd">北京学校</div>
                <div class="bd">
                    <i class="icon-shyhl"></i>
                    <h4>不要只懂生存
                        不懂生活</h4>
                    <i class="icon-shyhr"></i>
                </div>
                <div class="ft">
                    新教师 99+ <span>|</span>
                    导师 700+
                </div>
            </div>
        </div>
        <div class="media">
            <a class="pull-left" href="#">
                <img src="${ctx}/resources/images/hoverfold-01.jpg" class="media-object" alt=""/>
            </a>
            <div class="media-body">
                <div class="hd">北京学校</div>
                <div class="bd">
                    <i class="icon-shyhl"></i>
                    <h4>不要只懂生存
                        不懂生活</h4>
                    <i class="icon-shyhr"></i>
                </div>
                <div class="ft">
                    新教师 99+ <span>|</span>
                    导师 700+
                </div>
            </div>
        </div>
        <div class="media">
            <a class="pull-left" href="#">
                <img src="${ctx}/resources/images/hoverfold-01.jpg" class="media-object" alt=""/>
            </a>
            <div class="media-body">
                <div class="hd">北京学校</div>
                <div class="bd">
                    <i class="icon-shyhl"></i>
                    <h4>不要只懂生存
                        不懂生活</h4>
                    <i class="icon-shyhr"></i>
                </div>
                <div class="ft">
                    新教师 99+ <span>|</span>
                    导师 700+
                </div>
            </div>
        </div>
    </div>
    <div class="grid-bottom">
         	<div class="pages">            	
                <div class="btn-group">
                	<button class="btn btn-primary" disabled><i class="icon-chevron-left icon-white"></i></button>
                    <button class="btn btn-primary"><i class="icon-chevron-right icon-white"></i></button>
                </div>
            </div>
     </div>
</div>

<script type="text/javascript">	
			
$("#myTabs>li>a").bind("mouseover",function(e){
	$(this).tab("show");		
})
.on("shown",function(e){
	$("#myTabs>.caret").stop(false,true).animate({left: $(this).parent().index()*250 + 46 + "px"},"slow","swing");
});
		
</script>
</body>
</html>
