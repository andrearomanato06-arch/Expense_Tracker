<%
    int currentPage = (Integer) request.getAttribute("page");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add new Expense</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>

    <a href="view?page=1"><button class = "back"><img src="../img/left-arrow-7360.svg" alt="goBack"></button></a>
    
    <form id = "addForm">
        <h1>Add a new expense</h1>
        <input type = "text" name = "description" class = "field" placeholder = "Description" required>

        <input type = "number" name = "amount" class = "field" placeholder = "Amount" step="0.01" required>

        <select name = "category" class = "field">
            <option disabled selected>Category</option>
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

        <input type = "date" name = "purchaseDate" class = "field" placeholder = "Date" required>

        <input type = "submit" name = "addNew" class = "submit_btn" value = "Submit" >
    </form>

    <script src = "/js/add.js" ></script>
</body>
</html>