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
	function btnReturnToDoc_click() {
		location.href = "/wiki/doc/read.do?doc_id=" + "${docId}";
	}
	
	window.onload = function() {
		
	}
</script>
</head>
<body>
<div style="width: 100%; max-width: 600px; margin: 0 auto;">
	<br>
	<h1>${docTitle}</h1>
	<input type="button" id="btnReturnToDoc" class="basic_button" value="원글보기" onclick="btnReturnToDoc_click()">
	<br>
	<br>
	${historyList}
	<br>&nbsp;
	<br>&nbsp;
</div>
</body>
</html>