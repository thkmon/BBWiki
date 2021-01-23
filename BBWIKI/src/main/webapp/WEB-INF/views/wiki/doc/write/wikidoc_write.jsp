<%@ page import="com.thkmon.common.util.StringUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	boolean bModifyMode = StringUtil.parseString(request.getAttribute("modifyMode")).equals("1");

	boolean bLogon = false;
	String hdwkUserId = "";
	String hdwkUserName = "";
	
	try {
		if (request.getSession() != null) {
			hdwkUserId = StringUtil.parseString(request.getSession().getAttribute("hdwkUserId"));
			hdwkUserName = StringUtil.parseString(request.getSession().getAttribute("hdwkUserName"));
			
			if (hdwkUserId != null && hdwkUserId.length() > 0) {
				if (hdwkUserName != null && hdwkUserName.length() > 0) {
					bLogon = true;
				}
			}
		}
	} catch (Exception e) {}
%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, height=device-height">
<title>혼둠위키</title>
<link rel="stylesheet" type="text/css" href="/websrc/css/common.css">
<script src="/websrc/js/lib/jquery-1.8.3.min.js"></script>
<script src="/websrc/js/util/ajax_util.js"></script>
<style type="text/css">
</style>
<script type="text/javascript">
	
	var oEditors = [];
	
	var sLang = "ko_KR";	// 언어 (ko_KR/ en_US/ ja_JP/ zh_CN/ zh_TW), default = ko_KR
	
	window.onload = function() {
		window.setTimeout(function(){
			
			try {
				document.getElementById("doc_title").focus();
			} catch (e) {
				// 무시
			}
			
			// 추가 글꼴 목록
			//var aAdditionalFontSet = [["MS UI Gothic", "MS UI Gothic"], ["Comic Sans MS", "Comic Sans MS"],["TEST","TEST"]];
			
			nhn.husky.EZCreator.createInIFrame({
				oAppRef: oEditors,
				elPlaceHolder: "ir1",
				sSkinURI: "/websrc/editor/SmartEditor2Skin.html",	
				htParams : {
					bUseToolbar : true,				// 툴바 사용 여부 (true:사용/ false:사용하지 않음)
					bUseVerticalResizer : true,		// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
					bUseModeChanger : true,			// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
					//bSkipXssFilter : true,		// client-side xss filter 무시 여부 (true:사용하지 않음 / 그외:사용)
					//aAdditionalFontList : aAdditionalFontSet,		// 추가 글꼴 목록
					fOnBeforeUnload : function(){
						//alert("완료!");
					},
					I18N_LOCALE : sLang
				}, //boolean
				fOnAppLoad : function(){
					//예제 코드
					//oEditors.getById["ir1"].exec("PASTE_HTML", ["로딩이 완료된 후에 본문에 삽입되는 text입니다."]);
					
					// setDefaultFont();
				},
				fCreator: "createSEditor2"
			});
			
		}, 10);
	}
	
	function btnWrite_click() {
		
		var htmlCont = getContentHTML();
		document.getElementById("doc_content").value = htmlCont;
		
		AjaxUtil_SendPost("/wiki/doc/write_req.do", "#doc_write_form", function(){
			if (arguments != null && arguments[0] != null) {
				var tmpResult = arguments[0];
				if (tmpResult != null && tmpResult.length > 2 && tmpResult.substring(0, 2) == "1/") {
					<% if (bModifyMode) { %>
						alert("문서가 수정되었습니다.");
					<% } else { %>
						alert("문서가 작성되었습니다.");
					<% } %>
					
					var newDocId = tmpResult.substring(2);
					location.href = "/wiki/doc/read.do?doc_id=" + newDocId;
				} else {
					alert("오류가 발생하였습니다. : " + tmpResult);
				}
			}
		});
	}
	
	function btnGoList_click() {
		location.href = "/wiki/doc/list.do";
	}
	
	// 본문에 내용 넣기
	function pasteHTML() {
		var sHTML = "<span style='color:#FF0000;'>이미지도 같은 방식으로 삽입합니다.<\/span>";
		oEditors.getById["ir1"].exec("PASTE_HTML", [sHTML]);
	}
	
	// 본문 내용 가져오기
	function getContentHTML() {
		var sHTML = oEditors.getById["ir1"].getIR();
		// alert(sHTML);
		return sHTML;
	}
	
	// 서버로 내용 전송
	function submitContents(elClickedObj) {
		oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);	// 에디터의 내용이 textarea에 적용됩니다.
		
		// 에디터의 내용에 대한 값 검증은 이곳에서 document.getElementById("ir1").value를 이용해서 처리하면 됩니다.
		
		try {
			elClickedObj.form.submit();
		} catch(e) {}
	}
	
	// 기본 폰트 지정하기
	//function setDefaultFont() {
// 		var sDefaultFont = "verdana";
// 		var nFontSize = 11;
// 		oEditors.getById["ir1"].setDefaultFont(sDefaultFont, nFontSize);
	//}
	
	function pasteHTML2(_path) {
		try {
			var sHTML = "<img src=\"" + _path + "\"/>";
			oEditors.getById["ir1"].exec("PASTE_HTML", [sHTML]);
		} catch (e) {
			alert(e);
		}
	}
</script>
<script type="text/javascript" src="/websrc/editor/js/service/HuskyEZCreator.js" charset="utf-8"></script>
</head>
<body>
	<br>
	<div style="width: 100%; max-width: 600px; margin: 0 auto;">
	<h1>
		글쓰기
	</h1>
	<form id="doc_write_form" name="doc_write_form">
		<b>제목</b>
		<br>
		<% if (bModifyMode) { %>
			<span>${title}</span>
			<%-- 문서 아이디 : 문서 수정시에만 서버에서 문서 아이디 내려온다. --%>
			<input type="text" id="doc_id" name="doc_id" class="hidden" value="${docId}" max="30" maxlength="30">
			<%-- 문서 리비전 : 문서 수정시에만 서버에서 문서 리비전 내려온다. --%>
			<input type="text" id="max_mod_count" name="max_mod_count" class="hidden" value="${maxModCount}" max="30" maxlength="30">
		<% } else { %>
			<input type="text" id="doc_title" name="doc_title" class="w99per" value="${title}" max="500" maxlength="500">
		<% } %>
		<br>
		<br>
		<% if (!bLogon) { %>
		<b>작성자</b>
		<br>
		* 정확히 기입해주세요. 부탁드립니다.
		<input type="text" id="reg_user_name" name="reg_user_name" class="w99per" value="" max="30" maxlength="30">
		<br>
		<br>
		<% } %>
		<% if (bModifyMode) { %>
			<input type="text" id="modify_mode" name="modify_mode" class="hidden" value="${modifyMode}" max="1" maxlength="1">
		<% } %>
		<b>내용</b>
		<br>
		* 링크거는 법 : 링크하시려면 [[문서제목]] 또는 [[임의텍스트:문서제목]] 형태로 쓰세요.
		<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		ex) [[혼돈과 어둠의 땅]] 또는 [[어떤 사이트:혼돈과 어둠의 땅]]
		<br>
		<table class="w99per">
			<tr>
				<td class="w99per">
					<textarea id="ir1" name="ir1" class="w99per" rows="20" cols="10">${textObj}</textarea>
					<textarea id="doc_content" name="doc_content" class="hidden" rows="1" cols="1"></textarea>
				</td>
			</tr>
		</table>
	</form>
	<% if (bModifyMode) { %>
		<input type="button" id="btnWrite" class="basic_button" value="수정하기" onclick="btnWrite_click()">
	<% } else { %>
		<input type="button" id="btnWrite" class="basic_button" value="글쓰기" onclick="btnWrite_click()">
	<% } %>
		&nbsp;
		<input type="button" id="btnGoList" class="basic_button" value="목록으로" onclick="btnGoList_click()">
	<br>
	<br>
	
	<form action="sample/viewer/index.php" method="post">
	<!-- 	<textarea name="ir1" id="ir1" rows="10" cols="100" style="width:766px; height:412px; display:none;"></textarea> -->
		<!--textarea name="ir1" id="ir1" rows="10" cols="100" style="width:100%; height:412px; min-width:610px; display:none;"></textarea-->
		<p>
	<!-- 		<input type="button" class="basic_button" onclick="pasteHTML();" value="본문에 내용 넣기" /> -->
	<!-- 		<input type="button" class="basic_button" onclick="showHTML();" value="본문 내용 가져오기" /> -->
	<!-- 		<input type="button" class="basic_button" onclick="submitContents(this);" value="서버로 내용 전송" /> -->
		</p>
	</form>
</div>
</body>
</html>