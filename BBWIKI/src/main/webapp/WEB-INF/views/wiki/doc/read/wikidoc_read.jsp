<%@ page import="com.thkmon.common.util.StringUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, height=device-height">
<title>혼둠위키</title>
<link rel="stylesheet" type="text/css" href="/websrc/css/common.css">
<script src="/websrc/js/lib/jquery-1.8.3.min.js"></script>
<script src="/websrc/js/util/ajax_util.js"></script>
<style type="text/css">
	p {
		margin: 0;
		padding: 0;
	}
	
	h1, h2, h3, h4, h5 {
		margin: 0;
		padding: 0;
	}
	
	img {
		max-width: 590px;
		cursor: pointer;
	}
</style>
<script type="text/javascript">
	window.onload = function() {
		var len = $("img").length;
		for (var i=0; i<len; i++) {
			try {
				var orgSrc = $("img").eq(i).attr("src");
				// $("img").eq(i).attr("src", "http://ddoc.kr:9090/" + orgSrc);
			
				var imgOnClick = $("img").eq(i).attr("onclick");
				if (imgOnClick == null || imgOnClick == "") {
					$("img").eq(i).attr("onclick", "window.open('" + orgSrc + "');");
				}

			} catch(e) {
				// 무시
			}
		}
		
		window.setTimeout(function(){
			resizeImgs();
		}, 100);
	}
	
	// 최초 로드시, 화면 가로 사이즈보다 이미지가 클 경우, 이미지 가로길이 조정
	function resizeImgs() {
		var len = $("img").length;
		for (var i=0; i<len; i++) {
			try {
				var imgWidth = $("img").eq(i).css("width");
				if (imgWidth.indexOf("px") == -1 && imgWidth.indexOf("PX") == -1) {
					continue;
				}
				
				if (imgWidth.indexOf("%") > -1) {
					continue;
				}
				
				imgWidth = imgWidth.replace("px", "").replace("PX", "");
				imgWidth = parseInt(imgWidth, 10);
				
				var docWidth = getWidthInt($("#mainDiv").css("width"));
				
				if (docWidth < imgWidth) {
					var newWidth = docWidth - 50;
					if (newWidth > 590) {
						newWidth = 590;
					}
					
					if (newWidth < 10) {
						newWidth = 10;
					}
					
					$("img").eq(i).css("width", newWidth + "px");
				}
			} catch(e) {
				// 무시
			}
		}
	}
	
	function getWidthInt(_width) {
		if (_width == null || _width == "") {
			return 0;
		}
		
		if (_width.indexOf("px") > -1) {
			_width = _width.replace("px", "");
		}
		
		if (_width.indexOf("PX") > -1) {
			_width = _width.replace("PX", "");
		}
		
		return parseInt(_width, 10);
	}
	
	function btnWrite_click() {
		location.href = "/wiki/doc/write.do";
	}
	
	function btnGoList_click() {
		location.href = "/wiki/doc/list.do?from_read_page=1";
	}
	
	function btnModify_click() {
		location.href = "/wiki/doc/write.do?doc_id=" + "${docId}";
	}
	
	function btnHistory_click() {
		location.href = "/wiki/doc/history.do?doc_id=" + "${docId}";
	}
	
	function btnReturnToDoc_click() {
		location.href = "/wiki/doc/read.do?doc_id=" + "${docId}";
	}
</script>
</head>
<body>
	<div id="mainDiv" style="width: 100%; height: 2px"></div>
	<br>
	<div style="width: 100%; max-width: 600px; margin: 0 auto;">
	<c:if test="${modCount==-1}">
		<h1>${title}</h1>
	</c:if>
	<c:if test="${modCount!=-1}">
		<h1>[리비전 ${modCount}] ${title}</h1>
	</c:if>
	<br>
	<% if (StringUtil.parseString(request.getAttribute("readMode")).equals("1")) { %>
		<table class="w100per" style="max-width: 600px;">
			<tr>
				<td class="w100per doc_box border1">${textObj}</td>
			</tr>
		</table>
		<br>
		<div class="lfloat">
			<c:if test="${modCount==-1}">
				<input type="button" id="btnModify" class="basic_button" value="현재글수정" onclick="btnModify_click()">
			</c:if>
			<c:if test="${modCount!=-1}">
				<input type="button" id="btnReturnToDoc" class="basic_button" value="원글보기" onclick="btnReturnToDoc_click()">
			</c:if>
			<input type="button" id="btnHistory" class="basic_button" value="히스토리" onclick="btnHistory_click()">
		</div>
		<div class="rfloat" style="text-align: right;">
			<input type="button" id="btnGoList" class="basic_button" value="목록으로" onclick="btnGoList_click()">
			<input type="button" id="btnWrite" class="basic_button" value="새글쓰기" onclick="btnWrite_click()">
		</div>
		<br>
		<br>
	<% } else { %>
		문서가 존재하지 않습니다. 새 문서를 작성해주세요.
		<br>
		<br>
		<input type="button" id="btnWrite" class="basic_button" value="새글쓰기" onclick="btnWrite_click()">
		<input type="button" id="btnGoList" class="basic_button" value="목록으로" onclick="btnGoList_click()">
		<br>
		<br>
	<% } %>
	</div>
</body>
</html>