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
</style>
<script type="text/javascript">
window.onload = function(){
	window.setTimeout(function(){
		location.href = "/wiki/doc/list.do";
	}, 10);
}
</script>
</head>
<body>
<a href="/wiki/doc/list.do">혼둠위키 바로가기</a>
</body>
</html>