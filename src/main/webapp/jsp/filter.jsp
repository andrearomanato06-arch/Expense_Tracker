<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Filter</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>

    <a href="view?page=1"><button class = "back"><img src="../img/left-arrow-7360.svg" alt="goBack"></button></a>
    
    <form action = "/expense/filter" method = "GET">
        <h1>Filters</h1>

        <input type = "number" name = "amount" class = "field" placeholder = "Amount" step="2" >

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

        <input type = "date" name = "dateFrom" class = "field" >

        <input type = "date" name = "dateTo" class = "field" >

        <input type = "submit" name = "filter" class = "submit_btn" value = "Apply Filters">
    </form>

</body>
</html>