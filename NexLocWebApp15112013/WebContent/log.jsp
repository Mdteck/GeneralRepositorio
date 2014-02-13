<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
    <%@ page  import="java.io.FileInputStream" %>
<%@ page  import="java.io.BufferedInputStream"  %>
<%@ page  import="java.io.File"  %>
<%@ page import="java.io.IOException" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Logs</title>
</head>
<body>

<%
	BufferedInputStream buf = null;
	ServletOutputStream myOut = null;

	try {

		myOut = response.getOutputStream();
		File myfile = new File("C:\\Sun\\AppServer09_2_2\\domains\\domain1\\logs\\server.log");

		response.setContentType("text/plain");
		response.addHeader("Content-Disposition", "attachment; filename=NexLocWebApp.log");
		response.setContentLength((int) myfile.length());

		FileInputStream input = new FileInputStream(myfile);
		buf = new BufferedInputStream(input);
		int readBytes = 0;

		while ((readBytes = buf.read()) != -1)
			myOut.write(readBytes);

	} catch (IOException ioe) {

		throw new ServletException(ioe.getMessage());

	} finally {
		if (myOut != null)
			myOut.close();
		if (buf != null)
			buf.close();

	}
%>
</body>
</html>