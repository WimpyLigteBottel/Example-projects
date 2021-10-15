<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/jsp/header/header.jspf" %>
    <title>Hello world Example</title>
</head>
<body class="middle">
<form name="welcomeForm" method="POST" action="${flowExecutionUrl}">

    <div>
        welcome ${name}
        <br/> <br/>
        ${result}
    </div>
    <br/>
    <div>
        <input type="submit" name="_eventId_back" value="Back"/>
    </div>

</form>
</body>
</html>
<script>

    $(document).ready(function () {
        console.log("ready!");
    });
</script>
