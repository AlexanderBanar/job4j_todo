<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>todo</title>
</head>

<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>

<script>
    $(document).ready(function () {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/todo/findAll',
            dataType: 'json'
        }).done(function (data) {
            let items = ""
            for (let i = 0; i < data.length; i++) {
                items += "<tr>" + "<th>" + data[i]["id"] + "</th>"
                items += "<td>" + data[i]["description"] + "</td>"
                items += "<td>" + data[i]["created"] + "</td>"
                if (data[i]["done"] === false) {
                    items += "<td>" + "open" + "</td>"
                } else {
                    items += "<td>" + "closed" + "</td>"
                }
                items += "</tr>"
            }
            $('#table').html(items);
        }).fail(function (err) {
            console.log(err);
        });
    })

    function filter() {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/todo/filtered',
            dataType: 'json'
        }).done(function (data) {
            let items = ""
            for (let i = 0; i < data.length; i++) {
                items += "<tr>" + "<td>" + data[i]["id"] + "</td>"
                items += "<td>" + data[i]["description"] + "</td>"
                items += "<td>" + data[i]["created"] + "</td>"
                if (data[i]["done"] === false) {
                    items += "<td>" + "open" + "</td>"
                } else {
                    items += "<td>" + "closed" + "</td>"
                }
                items += "</tr>"
            }
            $('#table').html(items);
        }).fail(function (err) {
            console.log(err);
        });
    }
</script>

<body>

<form action="<%=request.getContextPath()%>/findAll" method="post">
    <div>
        <h1>Task planner</h1>
        <h3>Manage your tasks within few clicks</h3>
        <br>
        <br>
        <h5>Fill in new task description</h5>
        <br>
        <label>
            <textarea rows="6" cols="50" name="description" required></textarea>
        </label>
        <br>
        <button type="submit" class="btn btn-primary">Save new task</button>
    </div>
</form>
<br>
<br>

<form action="<%=request.getContextPath()%>/close" method="post">
    <div>
        <h5>Type in the ID of a task to close (one shot -> one task)</h5>
        <br>
        <label>
            <input type="number" name="closedTask">
        </label>
        <button type="submit" class="btn btn-primary">Close the task</button>
    </div>
</form>
<br>
<br>

<button onclick="filter();">Filter open tasks only</button>
<br>
<br>

<div class="container">
    <div class="row pt-3">
        <h4>
            TODO list
        </h4>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Description</th>
                <th>Created</th>
                <th>Done?</th>
            </tr>
            </thead>
            <tbody id="table">
            </tbody>
        </table>
    </div>
</div>

</body>
</html>