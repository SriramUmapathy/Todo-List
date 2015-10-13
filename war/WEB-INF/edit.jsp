<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.List" %>
     <%@ page import="springtest.Todo" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>edit page</title>
</head>
<body>
<%
if(session.getAttribute("email")==null || session.getAttribute("name")==null)
response.sendRedirect("/");

String name= (String)session.getAttribute("name");
String email= (String)session.getAttribute("email");
%>
<h2>got in</h2>
<%=name %>
<%=email %>
<h2>Edit your note</h2>
<%
Todo todo = new Todo();

if(request.getAttribute("todo")!=null){

	todo = (Todo)request.getAttribute("todo");
	
}
%>
<form  id="formid" action="../editvalue" method="post">
<input type="hidden" name="key" id="key" value="<%=todo.getKey()%>" />
<input type="text" name="content"id="todotext" size="50" value="<%=todo.getContent() %>"/>
<input type=submit value="submit"/>
</form>
</body>
</html>