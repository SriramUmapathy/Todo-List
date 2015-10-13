<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import="java.util.List" %>
     <%@ page import="springtest.Todo" %>
      <%@ page import="java.util.Date" %>
      <%@ page import="java.text.SimpleDateFormat"%>
      <%@ page import="java.util.Calendar"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script> 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<title>yesterday notes</title>
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
            <li ><a href="add">Add Note</a></li>
            <li><a href="../logout">Log out</a></li>
            <li><a href="#">Contact</a></li>
            <li class="active"><a href="#">yesterday date</a></li>
            <li ><a href="week">last one week</a></li>
          </ul>
        </div>
      </div>
    </nav>


<h2>got in</h2>
<%=name %>
<%=email %>
<div>
<button id="addbutton"><a href="add">Add Note</a></button>
<button><a href="../logout">Log out</a></button>
</div>
<div class="container">
  <table class="table">
    <thead>
      <tr>
      <th>#</th>
        <th>Todo List</th>
        <th> Date</th>
      </tr>
    </thead>
  <tbody>
		
		<%
			
		if(request.getAttribute("todoList")!=null){
				
			List<Todo> customers = 
                           (List<Todo>)request.getAttribute("todoList");
				
			if(!customers.isEmpty()){
				int i=1;
				 for(Todo c : customers){
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					 Date todaydate=new Date();
					 
					 Calendar cal = Calendar.getInstance();
					 cal.setTime(todaydate);
					 cal.add(Calendar.DAY_OF_YEAR,-2);
					 Date oneDayBefore= cal.getTime();
					
					 Date getdate=c.getDate();
					 String date = sdf.format(oneDayBefore);
					 String gotdate = sdf.format(getdate);
					 if(gotdate.equalsIgnoreCase(date)){
						 
		%>
		<tr class="bg-info">
		<td><%=i %></td>
        <td><%=c.getContent()%></td>
        <td><%=c.getDate() %></td>
        <td><a class="btn btn-info btn-sm" href="/delete/<%=c.getKey() %>" role="button">Delete</a></td>
        <td><a class="btn btn-info btn-sm" href="/edit/<%=c.getKey() %>" role="button">Edit</a></td>
      </tr>
				
			
		<%	
			i++;
					 }}
		    
			}
			
		   }
		%>
    
      
    </tbody>
  </table>
</div>
         
        

</body>
</html>