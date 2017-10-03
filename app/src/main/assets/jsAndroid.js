var URL = document.URL;
var serverList = document.getElementById("selectServer");
var server = serverList.options[serverList.selectedIndex].value;

if(server.search("s=rapidvideo") != -1){
  rapidVideo();
}else if(server.search("s=beta") != -1 || server.search("s=kissanime")){
  betaServer();
}else if(server.search("s=openload") != -1){
  openload();
}

function betaServer(){
    var downloadLink = document.getElementById("my_video_1_html5_api").getAttribute("src");
    Android.sendLink(downloadLink);
}

function rapidVideo(){
  var iFrameDiv = document.getElementById("divContentVideo");
  var iFrameSrc = iFrameDiv.firstChild.getAttribute("src");
  Android.iFrameLink(iFrameSrc);
}