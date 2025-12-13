<%@page import="java.util.*"%>
<%@page import="aret.entities.Expense"%>

<%
    List<Expense> expenses = (List<Expense>) request.getAttribute("expenses");
    int currentPage = (Integer) request.getAttribute("page");
    int pageToShow = (Integer) request.getAttribute("pageToShow");

%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Expense tracker</title>
    <link rel="stylesheet" href="../css/style.css">    
</head>
<body>
        
    <div class="utils">
        <h2>Total: </h2>

        <div class="page_navigator">
            <%
                if(currentPage > 1){%>
                    <a href="view?page=<%= currentPage-1 %>">Previous</a>
            <%}%>

            <%
                for(int i = currentPage; i < (currentPage + 5); i++){
                    if(i > pageToShow){
                        break;
                    }%>
                    <a href="view?page=<%= i %>" ><%= i %></a>
            <%}%>
            

            <%
                if(currentPage < pageToShow){ %>
                    <a href="view?page=<%= currentPage+1 %>" >Next</a>
            <%}%>
        </div>
       

        <a href="addNew">
            <button class="utils_btn">
                Add
            </button>
        </a>
        
        <a href="applyFilters">
            <button class="utils_btn">
                Filter
            </button>
        </a>
    </div>

    <table>
        <tr class ="tableHeader">
            <th>Date</th>
            <th>Description</th>
            <th>Amount</th>
            <th>Category</th>
            <th>Actions</th>
        </tr>

        <%
            for(Expense e : expenses){ %>

                <tr class = "expenseData">
                    <td> <%= e.getPurchaseDate() %> </td>
                    <td> <%= e.getDescription() %> </td>
                    <td> <%= e.getAmount() %> </td>
                    <td> <%= e.getCategory() %> </td>
                    <td class="actions">

                        <button class="remove" onclick = "deleteExpense('<%= e.getId() %>')">
                            <img src="../img/trash.svg" alt="remove">
                        </button>

                        <a href="change?id=<%= e.getId() %>">
                            <button class="edit">
                                <img src="../img/pencil.svg" alt="edit">
                            </button>
                        </a>
                    </td>
                </tr>
            <%}%>
    </table>

    <script>
        function deleteExpense(id){     
            
            if(!id || id.toString().trim() === "") {
            alert("Errore: ID non valido!");
                return;
            }
                    
            if(confirm('Are you sure you want to remove this expense from the list?')){
                fetch(`/expense/delete?id=` + id,{
                    method: 'DELETE'
                })
                .then(response => {
                    if(response.ok){
                        alert('Expense remove successfully!');
                        window.location.href = '/expense/view?page=1';
                    }else{
                        alert('An error occoured during expense delete process!!');
                    }
                }).catch(error => console.error(error));
            }
        }
    </script>
</body>
</html>