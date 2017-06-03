
<!DOCTYPE html>
<html lang="zh-cn">
  <head>
  <!-- Meta, title, CSS, favicons, etc. -->
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">

  <title>高手云</title>

  <!-- Bootstrap core CSS -->
  <link href="http://job.bootcss.com/assets/css/app.min.css" rel="stylesheet">
  <link href="http://cdn.bootcss.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
  
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'cs-select.css')}" type="text/css">
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'cs-skin-underline.css')}" type="text/css">

  <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
  <![endif]-->

  <!-- Favicons -->
  <link rel="apple-touch-icon-precomposed" href="http://job.bootcss.com/assets/ico/apple-touch-icon-precomposed.png">
  <link rel="icon" href="${resource(dir: 'images', file: 'logo_yun1.png')}">

  <script>
    var _hmt = _hmt || [];
  </script>

</head>
<body>

<div id="navbar-top" class="navbar-top navbar navbar-default navbar-fixed-top">
  <div class="container">
    <div class="navbar-header">
      <a href="/" class="navbar-brand">高手云</a>
      <!--<img src="/assets/img/bl.png">-->
    </div>
    <p class="navbar-text navbar-right co-brand hidden-xs"><g:img dir="images/" file="logo_yun1.png" /></p>
  </div>
</div>

<g:form controller="student" id="${session.user.id }">
<header id="top-header" class="top-header jumbotron" style="background-image: url(http://job.bootcss.com/assets/img/ballon.jpg);">
  <div class="container">
  	<div class="row">
  		<div class="col-md-8">
		  	<div class="page-header">
			    <h1><span>Welcome to 高手云</span> 
				</h1>
			    <p class="lead">大数据分析每一个学生的薄弱知识点，针对您的薄弱知识点智能化出题</p>
		  	</div>
		  	<div class="features media">
		  		<h5 class="media-left">上榜高手：</h5>
		  		<div class="media-body">
		        <p class="coms">
		    			<a href="#" class="label label-default label-ali" onclick="_hmt.push(['_trackEvent', 'coms', 'click', '张三'])">英语 - 王二妮</a>
		    			<a href="#" class="label label-default label-baidu" onclick="_hmt.push(['_trackEvent', 'coms', 'click', '李四'])">英语 - 阿杰</a>
		    			<a href="#" class="label label-default label-sina" onclick="_hmt.push(['_trackEvent', 'coms', 'click', '王五'])">文言文 - 小哥</a>
		    			<a href="#" class="label label-default label-sohu" onclick="_hmt.push(['_trackEvent', 'coms', 'click', '王二妮'])">物理 - 王二妮</a>
		    			<a href="#" class="label label-default label-360" onclick="_hmt.push(['_trackEvent', 'coms', 'click', '阿宝'])">化学 - 阿宝</a>
		          		<a href="#" class="label label-default label-lenovo" onclick="_hmt.push(['_trackEvent', 'coms', 'click', '毕姥爷'])">数学  - 毕姥爷</a>
		        </p>
		  	</div>
	  	</div>
	  	<section>
			<select name="oper" class="cs-select cs-skin-underline">
				<option value="0" disabled selected>请选择一个操作</option>
				<option value="xunlian">我要进入一个课程 - 训练</option>
				<option value="chakan">我要进入一个课程 - 查看</option>
				<option value="goutong">我要进入一个课程 - 沟通</option>
			</select>
		</section>
  	   </div>
    </div>
  </div>
</header>

<section class="job-list">
  <div class="container">
    <div class="row">

      <div class="col-sm-6 col-md-4">
        <div class="job-item-wrap" id="course1" title="初中英语词汇与语法">
          <div class="job-item">
            <div class="job-source light-green">
              <g:img class="img-responsive" dir="images/courses/" file="c1.png" />
            </div>
            <div class="job-company">&nbsp;</div>
            <div class="job-title"><g:actionSubmit class="btn btn-primary" action="pcourse_xunlian" value="初中英语词汇与语法"/></div>
            <div class="job-salary">知识点/题目：2122/13439</div>
            <div class="job-comments">
              <p>
                  <span class="label label-default">历年真题</span>
                  <span class="label label-default">各地真题</span>
                  <span class="label label-default">名校模考</span>
                  <span class="label label-default">新世纪英语</span>
                  <span class="label label-default">新概念英语</span>
              </p>
            </div>
            <div class="job-meta">
              <span class="job-location">[当前练习学生数]</span><span class="job-publish-time">9</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="col-sm-6 col-md-4">
        <div class="job-item-wrap" id="course2" title="英语阅读理解">
          <div class="job-item">
            <div class="job-source light-green">
              <g:img class="img-responsive" dir="images/courses/" file="c2.png" />
            </div>
            <div class="job-company">&nbsp;</div>
            <div class="job-title">英语阅读理解</div>
            <div class="job-salary">知识点/题目：322/4439</div>
            <div class="job-comments">
              <p>
                  <span class="label label-default">历年真题</span>
                  <span class="label label-default">各地真题</span>
                  <span class="label label-default">名校模考</span>
                  <span class="label label-default">新世纪英语</span>
              </p>
            </div>
            <div class="job-meta">
              <span class="job-location">[当前练习学生数]</span><span class="job-publish-time">6</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="col-sm-6 col-md-4">
        <div class="job-item-wrap" id="course3" title="文言文">
          <div class="job-item">
            <div class="job-source light-green">
              <g:img class="img-responsive" dir="images/courses/" file="c3.png" />
            </div>
            <div class="job-company">&nbsp;</div>
            <div class="job-title">文言文</div>
            <div class="job-salary">知识点/题目：222/3439</div>
            <div class="job-comments">
              <p>
                  <span class="label label-default">历年真题</span>
                  <span class="label label-default">各地真题</span>
                  <span class="label label-default">名校模考</span>
              </p>
            </div>
            <div class="job-meta">
              <span class="job-location">[当前练习学生数]</span><span class="job-publish-time">4</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="col-sm-6 col-md-4">
        <div class="job-item-wrap" id="course4" title="初中物理">
          <div class="job-item">
            <div class="job-source light-green">
              <g:img class="img-responsive" dir="images/courses/" file="c4.png" />
            </div>
            <div class="job-company">&nbsp;</div>
            <div class="job-title">初中物理</div>
            <div class="job-salary">敬请期待 。。。</div>
            <div class="job-comments">
              <p>
                  
              </p>
            </div>
            <div class="job-meta">
              <span class="job-location">[当前练习学生数]</span><span class="job-publish-time">0</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="col-sm-6 col-md-4">
        <div class="job-item-wrap" id="course5" title="初中化学">
          <div class="job-item">
            <div class="job-source light-green">
              <g:img class="img-responsive" dir="images/courses/" file="c4.png" />
            </div>
            <div class="job-company">&nbsp;</div>
            <div class="job-title">初中化学</div>
            <div class="job-salary">敬请期待 。。。</div>
            <div class="job-comments">
              <p>
                  
              </p>
            </div>
            <div class="job-meta">
              <span class="job-location">[当前练习学生数]</span><span class="job-publish-time">0</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="col-sm-6 col-md-4">
        <div class="job-item-wrap" id="course6" title="初中语文">
          <div class="job-item">
            <div class="job-source light-green">
              <g:img class="img-responsive" dir="images/courses/" file="c4.png" />
            </div>
            <div class="job-company">&nbsp;</div>
            <div class="job-title">初中语文</div>
            <div class="job-salary">敬请期待 。。。</div>
            <div class="job-comments">
              <p>
                  
              </p>
            </div>
            <div class="job-meta">
              <span class="job-location">[当前练习学生数]</span><span class="job-publish-time">0</span>
            </div>
          </div>
        </div>
      </div>
           
      
    </div><!-- .row -->

    <div class="col-sm-6 col-sm-push-3 col-md-4 col-md-push-4">
      <p>
        <a class="btn-load-more btn btn-primary btn-lg btn-block" href="#"><i class="fa fa-th"></i> 更多课程敬请期待</a>
      </p>
    </div>
	
  </div><!-- .container -->
</section>

  <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
  <script src="http://cdn.bootcss.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
  <script src="http://cdn.bootcss.com/scrollup/2.4.0/jquery.scrollUp.min.js"></script>
  
  <script type="text/javascript" src="${resource(dir: 'js', file: 'classie.js')}"></script>
  <script type="text/javascript" src="${resource(dir: 'js', file: 'selectFx.js')}"></script>
  <script>
	(function() {
		[].slice.call( document.querySelectorAll( 'select.cs-select' ) ).forEach( function(el) {	
			new SelectFx(el);
		} );
	})();
  </script>
  
    <script>
    (function() {
    	$(".cs-select").change(function(){
        	alert('xxx');
        	alert($(this).val() );
        	//alert($(".btn btn-primary").attr('action'));
    	    
    	 });
	})();    
  </script>
  
  <script>
  ;(function(window, document, $){
    // $(document).ready(function(){
    //   $.adaptiveBackground.run()
    // });

    $.scrollUp({
          scrollName: 'scrollUp', // Element ID
          topDistance: '300', // Distance from top before showing element (px)
          topSpeed: 300, // Speed back to top (ms)
          animation: 'fade', // Fade, slide, none
          animationInSpeed: 200, // Animation in speed (ms)
          animationOutSpeed: 200, // Animation out speed (ms)
          scrollText: '<i class="fa fa-angle-up"></i>', // Text for element
          activeOverlay: false  // Set CSS color to display scrollUp active point, e.g '#00FFFF'
    });

    $(window).scroll(function() {
        if ($("#navbar-top").offset().top > 300) {
            $('.co-brand > img').attr('src', "${resource(dir: 'images', file: 'logo_yun1.png')}");
        } else {
            $('.co-brand > img').attr('src', "${resource(dir: 'images', file: 'logo_yun1.png')}");
        }
    });
  })(window, document, jQuery)
  </script>
  
  <script type="text/javascript">
  var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
  document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3Fd979e0116b6882c9cdc4cf2c8467d312' type='text/javascript'%3E%3C/script%3E"));
  </script>
  
	<hr class="footer-divider">
	<div class="container">
		<p>
			&copy; 2013-2014 sHOTs Software Inc. All Rights Reserved. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<g:link controller="adminUser" action="login">管理员</g:link>
			&nbsp;&nbsp;<a data-toggle="modal" data-target="#about" href="#">关于高手云</a>
		</p>
	</div>
	
	<!-- About Modal -->
	<div class="modal fade" id="about" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h3 class="modal-title" id="myModalLabel">
						高手云在线训练与测评系统
					</h3>
				</div>
				<div class="modal-body">
					版本：<g:message code="about.version"/>
					<br>名称：<g:message code="about.prod" />
					<br>网址：<g:message code="about.website" />
					<br>版权：<g:message code="about.copyright" />
					<br>详细：<g:message code="about.desc" />
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</g:form>	
</body>
</html>