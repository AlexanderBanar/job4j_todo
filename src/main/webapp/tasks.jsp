<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            url: 'http://localhost:8080/todo/findAll.do',
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
            url: 'http://localhost:8080/todo/filter.do',
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
                items += "<td>"
                    for (let j = 0; j < data[i]["categories"].length; j++) {
                        items += data[i]["categories"][j]
                        items += "; "
                    }
                items += "</td>" + "</tr>"
            }
            $('#table').html(items);
        }).fail(function (err) {
            console.log(err);
        });
    }

</script>

<body>

<form action="<%=request.getContextPath()%>/findAll.do" method="post">
    <div>
        <li class="nav-item">
            <% if (request.getSession().getAttribute("user") == null) { %>
            <a class="nav-link" href="<%=request.getContextPath()%>/tasks.jsp">Login</a>
            <% } else { %>
            <a class="nav-link" href="<%=request.getContextPath()%>/logout.do"> <c:out value="${user.name}"/> | Logout</a>
            <% } %>
        </li>
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
        <label class="col-form-label col-sm-3" for="cIds" style="font-weight: 900">Choose category(-ies)</label>
        <div class="col-sm-5" href="<%=request.getContextPath()%>/category.do">
            <select class="form-control" name="cIds" id="cIds" multiple>
                <c:forEach items="${allCategories}" var="category">
                    <option value="${category.id}">${category.name}</option>
                </c:forEach>
            </select>
        </div>
        <br>
        <button type="submit" class="btn btn-primary">Save new task</button>
    </div>
</form>
<br>
<br>

<form action="<%=request.getContextPath()%>/close.do" method="post">
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
                <th>Categories</th>
            </tr>
            </thead>
            <tbody id="table">
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
