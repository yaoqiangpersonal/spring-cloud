<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>hello</h1>
</body>

<script>
	var url = 'ws://' + window.location.host + '/websocket/marco';
	var sock = new WebSocket(url);
	
	sock.onopen = function(){
		console.log('opening');
		sayMarco();
	};
	
	sock.onmessage = function(e){
		console.log('Received message: ' + e.data);
		setTimeout(function(){sayMarco()},2000);
	}
	
	sock.onclose = function(){
		console.log('closing');
	};
	
	function sayMarco(){
		console.log('sending Marco');
		sock.send('Marco!');
	};
	
	
</script>

</html>