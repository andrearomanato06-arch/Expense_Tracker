<%@page import="aret.entities.Expense"%>
<%
    Expense e = (Expense) request.getAttribute("expense");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>

    <a href="view?page=1"><button class = "back"><img src="../img/left-arrow-7360.svg" alt="goBack"></button></a>
    
    <form id = "update_form">

        <input type="hidden" name = "id" value = "<%= e.getId() %>">

        <h1>Edit expense n. <%= e.getId() %> </h1>
        <input type = "text" name = "description" class = "field" placeholder = "Description" value = "<%= e.getDescription() %>">

        <input type = "number" name = "amount" class = "field" placeholder = "Amount" step="0.01" value = "<%= e.getAmount() %>">

        <select name = "category" class = "field" >
            <option disabled selected><%= e.getCategory() %></option>
            <option value="Books">Books</option>
            <option value = "Car">Car</option>
            <option value = "Clothes">Clothes</option>
            <option value = "Concerts">Concerts</option>
            <option value = "Food">Food</option>
            <option value = "Gifts">Gifts</option>
            <option value = "Guitar">Guitar</option>
            <option value = "Subscriptions">Subscriptions</option>
            <option value = "Videogames">Videogames</option>

        </select>

        <input type = "date" name = "purchaseDate" class = "field" placeholder = "Date" value="<%= e.getPurchaseDate() %>">

        <input type = "submit" name = "update" class = "submit_btn" value = "Change">
    </form>
    <script src = "/js/update.js"></script>
</body>
</html>