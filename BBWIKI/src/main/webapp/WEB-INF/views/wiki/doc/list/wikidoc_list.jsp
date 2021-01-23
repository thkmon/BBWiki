<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, height=device-height">
<title>혼둠위키</title>
<link rel="stylesheet" type="text/css" href="/websrc/css/common.css">
<style type="text/css">
</style>
<script type="text/javascript">
	var g_serachTitle = "";
	var g_pageNum = 0;
	var g_totalPage = 0;
	var g_searchMode = "";
	
	function btnWrite_click() {
		location.href = "/wiki/doc/write.do";
	}
	
	function inputSearch_OnKeyUp() {
		var keyCode = event.keyCode || event.which;
		if (keyCode == "13") {
			btnSearch_click();
		}
	}
	
	function btnHome_click() {
		location.href = "/wiki/doc/list.do";
	}
	
	function btnSearch_click() {
		location.href = "/wiki/doc/list.do?search_title=" + encodeURIComponent(input_search.value);
	}
	
	function btnAll_click() {
		location.href = "/wiki/doc/list.do?search_mode=all";
	}
	
	function btnShowIncomp_click() {
		location.href = "/wiki/doc/list.do?search_mode=uncomp";
	}
	
	function btnShowRecent_click() {
		location.href = "/wiki/doc/list.do";
	}
	
	// [상공으로] 버튼 클릭
	function btnGoMain_click() {
		location.href = "http://hondoom.com/";
	}
	
	// [이전페이지]
	function btnPrevPage_click() {
		var strUrl = "/wiki/doc/list.do?page_num=" + (g_pageNum - 1);
		if (g_serachTitle != "") {
			strUrl += "&search_title=" + encodeURIComponent(g_serachTitle);
		}
		
		location.href = strUrl;
	}
	
	// [다음페이지]
	function btnNextPage_click() {
		var strUrl = "/wiki/doc/list.do?page_num=" + (g_pageNum + 1);
		if (g_serachTitle != "") {
			strUrl += "&search_title=" + encodeURIComponent(g_serachTitle);
		}
		
		location.href = strUrl;
	}
	
	window.onload = function() {
		try {
			document.getElementById("input_search").focus();
		} catch (ee) {
			// 무시
		}
		
		g_serachTitle = "${searchTitle}";
		g_pageNum = ${pageNum};
		g_totalPage = ${totalPage};
		g_searchMode = "${searchMode}";
		
		if (g_searchMode != "uncomp") {
			document.getElementById("divPage").style.display = "";
			
			if (g_serachTitle != null && g_serachTitle != "") {
				document.getElementById("input_search").value = g_serachTitle;
			}
			
			if ((g_pageNum - 1) > 0) {
				document.getElementById("btnPrevPage").disabled = false;
			}
			
			if ((g_pageNum + 1) <= g_totalPage) {
				document.getElementById("btnNextPage").disabled = false;
			}
		}
	}
</script>
</head>
<body>
<div style="width: 100%; max-width: 600px; margin: 0 auto;">
	<a href="javascript:btnGoMain_click()">혼돈과 어둠의 땅 상공으로</a>
	<h1><a href="javascript:btnHome_click()" style="text-decoration: none; color: #000000;">혼둠위키</a></h1>
	위키를 기록해주세요.<br>
	<input type="button" id="btnWrite" class="basic_button" value="새글쓰기" onclick="btnWrite_click()">
	<c:if test="${searchMode!=''}">
		<input type="button" id="btnShowRecent" class="basic_button" value="최근위키 조회" onclick="btnShowRecent_click()">
	</c:if>
	<c:if test="${searchMode!='uncomp'}">
		&nbsp;<input type="button" id="btnShowIncomp" class="basic_button" value="미완성위키 조회" onclick="btnShowIncomp_click()">
	</c:if>
	<br>
	<br>
	원하는 위키를 검색해보세요.
	<table style="width: 96%; max-width: 500px;">
		<tr>
			<td style="vertical-align: top;">
				<div class="lfloat w100per">
					<input type="text" class="w100per" style="width: 100%; height: 26px;" id="input_search" onkeyup="inputSearch_OnKeyUp()" value="${searchTitle}" />&nbsp;
				</div>
			</td>
			<td style="width: 80px; vertical-align: top;">
				<div class="lfloat" style="padding-left: 6px;">
					<input type="button" id="btnSearch" class="basic_button" value="제목검색" onclick="btnSearch_click()">
				</div>
			</td>
		</tr>
	</table>
	<!-- <input type="button" id="btnAll" class="basic_button" value="전체조회" onclick="btnAll_click()"> -->
	<b>${docListTitle}</b>
	<br>
	${docList}
	<div id="divPage" style="display: none; margin: 0 auto; width: 100%;">
		<table style="border: 0; width: 100%;">
			<tr>
				<td colspan="2" style="text-align: center;">
					현재 ${pageNum} / ${totalPage} 페이지
				</td>
			</tr>
			<tr>
				<td>
					<div id="divPrevPage" class="rfloat">
						<input type="button" id="btnPrevPage" class="basic_button" value="이전페이지" onclick="btnPrevPage_click()" disabled="disabled">
						&nbsp;
					</div>
				</td>
				<td>
					<div id="divNextPage" class="lfloat">
						<input type="button" id="btnNextPage" class="basic_button" value="다음페이지" onclick="btnNextPage_click()" disabled="disabled">
					</div>
				</td>
			</tr>
		</table>
	</div>
	<br>&nbsp;
	<br>&nbsp;
</div>
</body>
</html>