<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script> 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){
	$('#formid').submit(function(event) {
	       
	      var content = $('#todotext').val();
	      var json = { "content" : content};
	       
	    $.ajax({
	        url: "ajax",
	        data: JSON.stringify(json),
	        type: "POST",
	         
	        beforeSend: function(xhr) {
	            xhr.setRequestHeader("Accept", "application/json");
	            xhr.setRequestHeader("Content-Type", "application/json");
	        },
	        success: function(a) {
	             
	                console.log(a);
	                console.log(a.date);
	                var content = a.content;
	                var date=a.date;
	                var key=a.key;
	                console.log(key);
	                $("#response").append("<li>todays notes <strong>"+content+" </strong>created at <i>"+date+"</i></li>");
	                $("#mytable").append("<tr class='bg-info'><td>"+content+"</td><td>"+ date +"</td></tr>");
	                $("input[type=text], textarea").val("");
	            	
	        }
	    });
	      
	    event.preventDefault();
	  });	
	
	

	
});


</script>
</head>
<body>

<%
if(session.getAttribute("email")==null || session.getAttribute("name")==null)
response.sendRedirect("/");

String name= (String)session.getAttribute("name");
String email= (String)session.getAttribute("email");
%>

<nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" style="color: white;">Todo List</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li ><a href="/authen/first">Home</a></li>
            <li class="active"><a href="#">Add Note</a></li>
            <li><a href="../logout">Log out</a></li>
            <li><a href="#">Contact</a></li>
             <li ><a href="yest">yesterday date</a></li>
            <li ><a href="week">last one week</a></li>
          </ul>
        </div>
      </div>
    </nav>

<h2>got in</h2>
<%=name %>
<%=email %>
<h2>add your note today</h2>
<div >
<ul id="response">
</ul>
</div>
<button><a href="/authen/first">go back</a></button>
<button><a href="../logout">Log out</a></button>
<div id="id2"></div>

<form  id="formid" method="post">
<input type="text" id="todotext" size="50"/>
<input type=submit value="submit"/>
</form>

<table class="table">
    <thead>
      <tr>
        <th>Todo List</th>
        <th> Date</th>
        
      </tr>
    </thead>
  <tbody id="mytable">
  
				   
    </tbody>
  </table>
				
  

</body>
</html>