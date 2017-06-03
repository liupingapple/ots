<html>
<head>
<meta name="viewport" content="width=device-width">

<style type="text/css">
* {
	padding: 0;
	margin: 0;
}

video {
	display: block;
	margin: 0 auto;
	max-width: 100%;
}
</style>

<r:require modules="jquery" />
<r:layoutResources />
</head>

<body>
	<%--<video controls="controls" autoplay="autoplay" loop="loop"
		src="${createLink(uri: '/')}/video/${vid}.mp4">
	</video>	
	--%>
	
	<!-- Yuanming's 7niu -->
	<%--<video id="my_video" controls="controls" preload="auto" autoplay="autoplay" src="http://7xiacg.com2.z0.glb.qiniucdn.com/${vid}"></video>--%>	
	
	<!-- Taylor's 7niu -->
	<video id="my_video" controls="controls" preload="auto" autoplay="autoplay" src="http://7vznog.com1.z0.glb.clouddn.com/${vid}"></video>
		
</body>

<script type="text/javascript"><%--
//判断各种浏览器，找到正确的方法
function launchFullScreen(element) {
  if(element.requestFullscreen) {
    element.requestFullscreen();
  } else if(element.mozRequestFullScreen) {
    element.mozRequestFullScreen();
  } else if(element.webkitRequestFullscreen) {
    element.webkitRequestFullscreen();
  } else if(element.msRequestFullscreen) {
    element.msRequestFullscreen();
  }
};

$(function(){  
	launchFullScreen("#my_video"); 
	
}); 

// 启动全屏!
// launchFullScreen(document.documentElement); // 整个网页
// launchFullScreen(document.getElementById("videoElement")); // 某个页面元素

--%>



	var fullscreen = function(elem) {
		var prefix = 'webkit';
		if (elem[prefix + 'EnterFullScreen']) {
			return prefix + 'EnterFullScreen';
		} else if (elem[prefix + 'RequestFullScreen']) {
			return prefix + 'RequestFullScreen';
		}
		;
		return false;
	};
	
	function autoFullScrenn(v) {
		var ua = navigator.userAgent.toLowerCase();
		var Android = String(ua.match(/android/i)) == "android";
		 if(!Android) return;//非android系统不使用;
		var video = v, doc = document;
		var fullscreenvideo = fullscreen(doc.createElement("my_video"));
		if (!fullscreen) {
			alert("不支持全屏模式");
			return;
		}
		video.addEventListener("webkitfullscreenchange", function(e) {
			if (!doc.webkitIsFullScreen) {//退出全屏暂停视频
				this.pause();
			}
			;
		}, false);
		video.addEventListener("click", function() {
			this.play();
			video[fullscreenvideo]();
		}, false);

		video.addEventListener('ended', function() {
			doc.webkitCancelFullScreen(); //播放完毕自动退出全屏
		}, false);

	};
	var v = document.getElementById('my_video');
	autoFullScrenn(v);
	
</script>
</html>