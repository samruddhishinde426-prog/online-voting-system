<%
    // prevent direct access without voting
    if (session == null || session.getAttribute("voterId") == null) {
        response.sendRedirect("index.html");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Thank You</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f2f2f2;
            text-align: center;
            margin-top: 120px;
        }
        h1 {
            color: #0b2c5d;
        }
        a button {
            margin-top: 30px;
            padding: 12px 30px;
            font-size: 16px;
            cursor: pointer;
        }
    </style>
</head>

<body>

<h1>Thank You!</h1>
<p><strong>Thank you for your precious vote.</strong></p>

<a href="voterLogout">
    <button>Logout</button>
</a>

</body>
</html>
