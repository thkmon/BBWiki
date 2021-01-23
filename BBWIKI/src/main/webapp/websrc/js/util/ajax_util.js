function AjaxUtil_SendPost(_url, _formTagId, _callBackFunc) {
	
	if (_url == null || _url.length == 0) {
		alert("AjaxUtil.sendPost : 대상 주소를 알 수 없습니다.");
		return false;
	}
	
	var tgUrl = _url;
	var params = $(_formTagId).serialize();
	
    $.ajax({
        type : "POST",  
        url : tgUrl,
        data : params,      
        success : function(result) {
             if (_callBackFunc != null) {
            	 _callBackFunc(result);
             } else {
            	 alert("sendPost 결과 : " + result);
             }
        },
        error : function(e){
        	if (e != null) {
        		var eMsg = e.responseText;
        		
        		if (eMsg != null && eMsg.length > 0) {
        			eMsg = eMsg.toLowerCase();
        			if (eMsg.indexOf("http status 404")) {
        				alert("페이지를 찾을 수 없습니다. (404)");
        			} else {
        				alert("에러 발생 : " + e.responseText);
        			}
        		}
        	}
        }
    });      
}