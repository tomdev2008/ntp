<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>

<head>
<link rel="stylesheet" href="${ctx}/resources/css/global.css" />
<link href="${ctx}/resources/css/home.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${ctx}/resources/js/modernizr.custom.69142.js"></script> 
</head>

<body>

<div class="banner-bg">
  <div class="container banner">
    <div class="start-prepare">
      <p>赶快点击下面的按钮，开始备课闯关之旅吧！</p>
      <a href="${ctx}/course/courseIndexAll" class="start-btn"><span class="txt">开始备课吧</span><span><b class="caret"></b></span></a>
    </div>
  </div>
</div>
<div class="bar-tools">
	<div class="container">
		<div class="bar-tools-wrap navbar navbar-inverse">
			<ul class="nav counting">
				<li>
                	<h2>30+</h2>
                    学校                
                </li>
				<li>
                	<h2>1,000+</h2>
                    新教师                
                </li>
                <li>
                	<h2>100+</h2>
                    导师                
                </li>
                <li>
                	<h2>40+</h2>
                    课程                
                </li>
			</ul>
			<ul class="nav nav-link">
				<li><a href="#">学前辅导</a></li>
				<li><a href="#">小学辅导</a></li>
				<li><a href="#">中学辅导</a></li>
				<li><a href="#">大学考试 </a></li>
				<li><a href="#">英语学习</a></li>
				<li><a href="#">出国留学</a></li>
				<li><a href="#">多语种</a></li>
				<li><a href="#">夏冬令营</a></li>
				<li><a href="#">更多</a></li>
			</ul>
		</div>
	</div>
</div>
<div class="container">
	<div class="page-header">
		<h3>在线备课六步闯关</h3>
	</div>
    <div class="online6-wrap">
    	<dl class="dl-onliner6 clearfix">
    		<dt><img src="${ctx}/resources/images/online6-tit.jpg" width="360" height="240">
            	<div class="caption">
            		<h3>在线备课六步闯关</h3>
            		<p><span class="text-warning">1,000+</span>人参与</p>
            	</div>
            </dt>
    		<dd>
            	<img src="${ctx}/resources/images/online6-1.jpg" >
            	<div class="caption">
           		  <h3>业务学习</h3>
           	  	</div>
            </dd>
    		<dd>
            	<img src="${ctx}/resources/images/online6-2.jpg" >
            	<div class="caption">
           		  <h3>学术准备</h3>
           	  	</div></dd>
    		<dd>
            	<img src="${ctx}/resources/images/online6-3.jpg" >
            	<div class="caption">
           		  <h3>标准化教案学习</h3>
           	  	</div></dd>
    		<dd>
            	<img src="${ctx}/resources/images/online6-4.jpg" >
            	<div class="caption">
           		  <h3>课件编写</h3>
           	  	</div></dd>
    		<dd>
            	<img src="${ctx}/resources/images/online6-5.jpg" >
            	<div class="caption">
           		  <h3>批课</h3>
           	  	</div></dd>
    		<dd>
            	<img src="${ctx}/resources/images/online6-6.jpg" >
            	<div class="caption">
           		  <h3>批课确认</h3>
           	  	</div></dd>
    	</dl>
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
   	  <ul class="nav nav-tabs">
   		  <li class="active"><a href="#tab1" data-toggle="tab">
       	  	<img src="${ctx}/resources/images/temp-face70.jpg" width="70" height="70">
            <h5>Grace</h5>
            <p>雅思教师</p>
            <b class="caret"></b>
          </a></li>
   		  <li><a href="#tab2" data-toggle="tab">
       	  	<img src="${ctx}/resources/images/temp-face70.jpg" width="70" height="70">
            <h5>Eric</h5>
            <p>托福教师</p>
            <b class="caret"></b>
          </a></li>
   		  <li><a href="#tab3" data-toggle="tab">
       	  	<img src="${ctx}/resources/images/temp-face70.jpg" width="70" height="70">
            <h5>Daniel</h5>
            <p>考研教师</p>
            <b class="caret"></b>
          </a></li>
   		  <li><a href="#tab4" data-toggle="tab">
       	  	<img src="${ctx}/resources/images/temp-face70.jpg" width="70" height="70">
            <h5>Felix</h5>
            <p>SAT教师</p>
            <b class="caret"></b>
          </a></li>
   	  </ul>
    </div>
     <div class="page-header">
		<h3>新东方与你同在</h3>
	</div>
    <div class="grid clearfix" id="grid">
   	  <div class="view">
   		  <div class="view-back">
            	<span>新教师
                	<span class="text-warning">100+</span>
                </span>
                <span>导师
                	<span class="text-warning">10+</span>
                </span>
                <a href="#">&rarr;</a>
          </div>
   		  <img src="${ctx}/resources/images/hoverfold-01.jpg" alt="">
          <h5>广州学校</h5>
       </div>
       <div class="view">
   		  <div class="view-back">
            	<span>新教师
                	<span class="text-warning">100+</span>
                </span>
                <span>导师
                	<span class="text-warning">10+</span>
                </span>
                <a href="#">&rarr;</a>
          </div>
   		  <img src="${ctx}/resources/images/hoverfold-02.jpg" alt="">
          <h5>武汉学校</h5>
       </div>
       <div class="view">
   		  <div class="view-back">
            	<span>新教师
                	<span class="text-warning">100+</span>
                </span>
                <span>导师
                	<span class="text-warning">10+</span>
                </span>
                <a href="#">&rarr;</a>
          </div>
   		  <img src="${ctx}/resources/images/hoverfold-03.jpg" alt="">
          <h5>杭州学校</h5>
       </div>
       <div class="view">
   		  <div class="view-back">
            	<span>新教师
                	<span class="text-warning">100+</span>
                </span>
                <span>导师
                	<span class="text-warning">10+</span>
                </span>
                <a href="#">&rarr;</a>
          </div>
   		  <img src="${ctx}/resources/images/hoverfold-04.jpg" alt="">
          <h5>深圳学校</h5>
       </div>
       <div class="view">
   		  <div class="view-back">
            	<span>新教师
                	<span class="text-warning">100+</span>
                </span>
                <span>导师
                	<span class="text-warning">10+</span>
                </span>
                <a href="#">&rarr;</a>
          </div>
   		  <img src="${ctx}/resources/images/hoverfold-05.jpg" alt="">
          <h5>济南学校</h5>
       </div>
       <div class="view">
   		  <div class="view-back">
            	<span>新教师
                	<span class="text-warning">100+</span>
                </span>
                <span>导师
                	<span class="text-warning">10+</span>
                </span>
                <a href="#">&rarr;</a>
          </div>
   		  <img src="${ctx}/resources/images/hoverfold-06.jpg" alt="">
          <h5>南京学校</h5>
       </div>
       <div class="view">
   		  <div class="view-back">
            	<span>新教师
                	<span class="text-warning">100+</span>
                </span>
                <span>导师
                	<span class="text-warning">10+</span>
                </span>
                <a href="#">&rarr;</a>
          </div>
   		  <img src="${ctx}/resources/images/hoverfold-07.jpg" alt="">
          <h5>苏州学校</h5>
       </div>
       <div class="view">
   		  <div class="view-back">
            	<span>新教师
                	<span class="text-warning">100+</span>
                </span>
                <span>导师
                	<span class="text-warning">10+</span>
                </span>
                <a href="#">&rarr;</a>
          </div>
   		  <img src="${ctx}/resources/images/hoverfold-08.jpg" alt="">
          <h5>无锡学校</h5>
       </div>
       <div class="view">
   		  <div class="view-back">
            	<span>新教师
                	<span class="text-warning">100+</span>
                </span>
                <span>导师
                	<span class="text-warning">10+</span>
                </span>
                <a href="#">&rarr;</a>
          </div>
   		  <img src="${ctx}/resources/images/hoverfold-09.jpg" alt="">
          <h5>敬请期待</h5>
       </div>
       <div class="view">
   		  <div class="view-back">
            	<span>新教师
                	<span class="text-warning">100+</span>
                </span>
                <span>导师
                	<span class="text-warning">10+</span>
                </span>
                <a href="#">&rarr;</a>
          </div>
   		  <img src="${ctx}/resources/images/hoverfold-10.jpg" alt="">
          <h5>更多学校</h5>
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
			
	Modernizr.load({
		test: Modernizr.csstransforms3d && Modernizr.csstransitions,
		yep : ['${ctx}/resources/js/jquery.hoverfold.js'],
		nope: '${ctx}/resources/css/fallback.css',
		callback : function( url, result, key ) {
				
			if( url === '${ctx}/resources/js/jquery.hoverfold.js' ) {
				$( '#grid' ).hoverfold();
			}

		}
	});
		
</script>
</body>
</html>
