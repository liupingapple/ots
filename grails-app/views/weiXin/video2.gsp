<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no,minimal-ui">   
    <meta charset="UTF-8">
    <title>Document</title>
    <style type="text/css">
        *{
            padding: 0;
            margin: 0;
        }
        video{
            display:block;
            margin:0 auto;
            max-width:100%;
        }
 
    </style>
</head>
<body>
    <video id="video" src="http://7vznog.com1.z0.glb.clouddn.com/${vid}" controls></video>
    <script type="text/javascript">
        var fullscreen = function(elem) {
            var prefix = 'webkit';
              if ( elem[prefix + 'EnterFullScreen'] ) {
                return prefix + 'EnterFullScreen';
              } else if( elem[prefix + 'RequestFullScreen'] ) {
                return prefix + 'RequestFullScreen';
              };
            return false;
        };
        function autoFullScrenn(v){
            var ua   = navigator.userAgent.toLowerCase();  
            var Android = String(ua.match(/android/i)) == "android";
            // if(!Android) return;//非android系统不使用;
            var video  = v,doc = document;
            var fullscreenvideo = fullscreen(doc.createElement("video"));
            if(!fullscreen) {
                alert("不支持全屏模式");
                return;
            }
            video.addEventListener("webkitfullscreenchange",function(e){
                if(!doc.webkitIsFullScreen){//退出全屏暂停视频
                    this.pause();
                };
            }, false);
            video.addEventListener("click", function(){
                this.play();
                video[fullscreenvideo]();
            }, false);
 
            video.addEventListener('ended',function(){
                doc.webkitCancelFullScreen(); //播放完毕自动退出全屏
            },false);
 
        };
        var v = document.getElementById('video');
        autoFullScrenn(v);
    </script> 
</body>
</html>