<%
    if (session == null || session.getAttribute("voterId") == null) {
        response.sendRedirect("voter_login.html");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Cast Your Vote</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f2f2f2;
        }

        .box {
            width: 70%;
            margin: 80px auto;
            background: #ffffff;
            padding: 30px;
            box-shadow: 0 0 10px gray;
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 25px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 14px;
            text-align: center;
        }

        th {
            background-color: #0b2c5d;
            color: white;
        }

        img {
            width: 45px;
            height: auto;
        }

        button {
            padding: 8px 18px;
            font-size: 14px;
            cursor: pointer;
        }
    </style>
</head>

<body>

<div class="box">
    <h2>Cast Your Vote</h2>

    <!-- NO AUTOFILL -->
    <form action="vote" method="post">

<table>
    <tr>
        <th>Candidate Name</th>
        <th>Party Name</th>
        <th>Party Symbol</th>
        <th>Action</th>
    </tr>

    <tr>
        <td>Candidate-A</td>
        <td>BJP</td>
        <td><img src="images/bjp.png" width="40"></td>
        <td>
            <button type="submit" name="candidate" value="Candidate-A">
                Vote
            </button>
        </td>
    </tr>

    <tr>
        <td>Candidate-B</td>
        <td>INC</td>
        <td><img src="images/inc.png" width="40"></td>
        <td>
            <button type="submit" name="candidate" value="Candidate-B">
                Vote
            </button>
        </td>
    </tr>

    <tr>
        <td>Candidate-C</td>
        <td>AAP</td>
        <td><img src="images/aap.png" width="40"></td>
        <td>
            <button type="submit" name="candidate" value="Candidate-C">
                Vote
            </button>
        </td>
    </tr>

    <tr>
        <td>Candidate-D</td>
        <td>BSP</td>
        <td><img src="images/bsp.png" width="40"></td>
        <td>
            <button type="submit" name="candidate" value="Candidate-D">
                Vote
            </button>
        </td>
    </tr>
</table>

</form>
</div>
<div style="margin-top:30px; text-align:center;">
    <a href="voterLogout">
        <button style="padding:10px 30px;">Logout</button>
    </a>
</div>

</body>
</html>
