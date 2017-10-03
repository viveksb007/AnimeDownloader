var URL = document.URL;

if(URL.search("rapidvideo")){
	var downloadLink = document.getElementById("vjs_video_3_html5_api").firstChild.getAttribute("src");
    Android.sendLink(downloadLink);
}