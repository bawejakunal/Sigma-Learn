function getParameterByName(name, url) {
	console.log("inside getParameterByName");
    if (!url) {
      url = window.location.href;
    }
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function init() {
	console.log("inside init");
    gapi.client.setApiKey("AIzaSyCst8PU0qL-KB7C1fZ9ZeyUihtgsH03-es");
    gapi.client.load("youtube", "v3", function() {
        console.log("READY GAPI");
        var query = getParameterByName("q");
		// document.getElementById("search").value=query;
		// init();
		searchFunc(query);
    });
}

function tplawesome(e,t){res=e;for(var n=0;n<t.length;n++){res=res.replace(/\{\{(.*?)\}\}/g,function(e,r){return t[n][r]})}return res}

function searchFunc(q) {
	console.log("inside searchFunc");
    // $("form").on("submit", function(e) {
       // e.preventDefault();

       //one year date
       var date = new Date();
       date.setFullYear( date.getFullYear() - 1 );
       console.log(date.toISOString());
       // prepare the request
       var request = gapi.client.youtube.search.list({
            part: "snippet",
            type: "video",
            q: encodeURIComponent(q).replace(/%20/g, "+"),
            maxResults: 3,
            order: "viewCount",
            publishedAfter: date.toISOString()
       }); 
       // execute the request
       request.execute(function(response) {
          var results = response.result;
          $("#results").html("");
          $.each(results.items, function(index, item) {
            $.get("tpl/item.html", function(data) {
                $("#results").append(tplawesome(data, [{"title":item.snippet.title, "videoid":item.id.videoId}]));
            });
          });
          resetVideoHeight();
       });
    // });
    
    $(window).on("resize", resetVideoHeight);
}

function resetVideoHeight() {
    $(".video").css("height", $("#results").width() * 9/16);
}

// $( document ).ready(function() {
// 	console.log("document ready");
    
// });
