<%@ page language="java" contentType="text/html" %>
<%@ page import="java.util.Date" %>
<html>
<head><title>ACME Benefits Enrollment</title></head>
<body bgcolor="#FFFFFF">

<table width="600" border="0" cellpadding="4">
<tr><td bgcolor="#003366">
  <font face="Arial" size="4" color="#FFFFFF"><b>ACME Benefits Enrollment</b></font>
</td></tr>
</table>

<p><font face="Arial" size="2">
Open enrollment closes 30 November. Contact HR extension 4412 with questions.
</font></p>

<form method="post" action="/benefits/login">
<table border="0" cellpadding="3">
<tr>
  <td><font face="Arial" size="2">User ID</font></td>
  <td><input type="text" name="user" size="20"></td>
</tr>
<tr>
  <td><font face="Arial" size="2">Password</font></td>
  <td><input type="password" name="password" size="20"></td>
</tr>
<tr>
  <td colspan="2"><input type="submit" value="Sign On"></td>
</tr>
</table>
</form>

<hr>
<font face="Arial" size="1">
ACME Corporation Benefits System v4.2.1<br>
Page generated <%= new Date() %>
</font>

</body>
</html>
