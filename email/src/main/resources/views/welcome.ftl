<!DOCTYPE html>
<html>
<head>
<title>Welcome</title>
</head>
<body>
<p>Welcome to our website!</p>

<#if userPassword??>
<p>Your password: ${userPassword}</p>
<#else>
<p>Your password is not available</p>
</#if>

<#if userEmail??>
<p>Your email: ${userEmail}</p>
<#else>
<p>Your email is not available</p>
</#if>

<p>Thank you for using our application!</p>

</body>
</html>

