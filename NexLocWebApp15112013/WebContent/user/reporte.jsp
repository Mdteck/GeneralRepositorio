<%@ page import="java.io.PrintWriter" pageEncoding="UTF-8" %>
<%@ page contentType="application/excel" language="java" %>

<%
    response.reset();
    response.setHeader("Content-type","application/excel;charset=UTF-8");
    response.setHeader("Content-disposition","inline; filename=reporte.xls");

    PrintWriter op = response.getWriter();
    String CSV = request.getParameter("data");
    if (CSV == null)		CSV="No hay datos.";
	op.write(CSV);
%>