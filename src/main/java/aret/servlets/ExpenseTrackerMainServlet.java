package aret.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import aret.daos.ExpenseDAO;
import aret.dtos.ExpenseDTO;
import aret.entities.Expense;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ExpenseTrackerServlet", urlPatterns = "/expense/*")
public class ExpenseTrackerMainServlet extends HttpServlet{

    ExpenseDAO expenseDAO = new ExpenseDAO();

    ObjectMapper mapper = new ObjectMapper()
    .registerModule(new JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        String path = request.getPathInfo();

        if(path == null || path.isBlank()){
            response.setStatus(400);
            return;
        }

        switch(path){

            case "/view":
                view(request,response);
            break;

            case "/addNew":
                addNew(request, response);
            break;

            case "/applyFilters":
                applyFilters(request, response);
            break;

            case "/change":
                change(request, response);
            break;

            case "/getAllExpenses": 
                getAllExpenses(request, response);
            break;

            case "/getById": 
                getById(request, response);
            break;

            case "/filter":
                filter(request,response);
            break;

            // case "/filter/byAmountOver": 
            //     getByAmountOver(request, response);
            // break;

            // case "/filter/byDate":
            //     getbyDate(request, response);    
            // break;

            // case "/filter/byCategory": 
            //     getByCategory(request, response);
            // break;

            // case "/filter/byDateAndCategory": 
            //     getByDateAndCategory(request, response);
            // break;

        }
    }

    private void view(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        
        int page = 0;
        try{
            page  = Integer.parseInt(request.getParameter("page"));
        }catch(NullPointerException e){
            page = 1;
        }catch(NumberFormatException e){
            page = 1;
        }
        request.setAttribute("page", page);
        
        List<Expense> expenses =  expenseDAO.getAllExpenses(page);
        request.setAttribute("expenses", expenses);

        int totalExpenses = expenseDAO.getNumberOfExpenses();
        int pageToShow = (int) Math.ceil((double) totalExpenses / ExpenseDAO.LIMIT);
        request.setAttribute("pageToShow", pageToShow);

        request.getRequestDispatcher("/jsp/index.jsp").forward(request, response);

    }

    private void addNew(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        request.getRequestDispatcher("/jsp/add.jsp").forward(request, response);

    }

    private void applyFilters(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        request.getRequestDispatcher("/jsp/filter.jsp").forward(request, response);

    }

    private void change(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        Expense expense = expenseDAO.getById(Integer.parseInt(request.getParameter("id")));
        if(expense == null){
            response.setStatus(404);
            response.getWriter().write("NULL");
        }else{
            response.getWriter().write(expense.toString());
        }
        request.setAttribute("expense", expense);

        request.getRequestDispatcher("/jsp/edit.jsp").forward(request, response);

    }

    private void getById(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        response.setContentType("application/json");
        
        int id = 0;
        try{

            id = Integer.parseInt(request.getParameter("id"));   

        }catch(NumberFormatException e){

            e.printStackTrace();

            response.setStatus(400);

            return;
        }

        Expense expense = expenseDAO.getById(id);

        if(expense != null){

            response.setStatus(200);
            response.getWriter().write(expense.toString());

        }else{
            response.setStatus(404);
        }
    }

    private void getAllExpenses(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        response.setContentType("application/json");

        int page = Integer.parseInt(request.getParameter("page")); 
        
        List<Expense> expenses  = expenseDAO.getAllExpenses(page);
        
        if(!expenses.isEmpty()){
            
            response.setStatus(200);
            
            for (Expense e : expenses) {
                response.getWriter().println(e.toString());
            }

        }else{
            response.getWriter().write("No data found");
        }
    }

    protected void filter(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        response.setContentType("application/json");

        String category = request.getParameter("category");
        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        String amount = request.getParameter("amount");

        List<Expense> expenses = expenseDAO.filter(category,dateFrom,dateTo,amount);

        
        request.setAttribute("page", 1);

        int totalExpenses = expenseDAO.getNumberOfExpenses();
        int pageToShow = (int) Math.ceil((double) totalExpenses / ExpenseDAO.LIMIT);
        request.setAttribute("pageToShow", pageToShow);


        if(!expenses.isEmpty()){
            response.setStatus(200);

            request.setAttribute("expenses", expenses);

            for(Expense e: expenses){
                response.getWriter().println(e.toString());
            }

            request.getRequestDispatcher("/jsp/index.jsp").forward(request, response);

        }else if(expenses.isEmpty()){
            response.setStatus(200);
            response.getWriter().write("no data found with the specified parameters");
        }else{
            response.setStatus(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        response.setContentType("application/json");

        String path = request.getPathInfo();

        if(path.equals("/insert")){

            ExpenseDTO expenseDTO = mapper.readValue(request.getInputStream().readAllBytes(), ExpenseDTO.class);

            if(expenseDAO.insert(new Expense(
                expenseDTO.purchaseDate(),
                expenseDTO.amount(),
                expenseDTO.description(),
                expenseDTO.category()
            ))){

                System.out.println("doPOST: ricevuto");
                response.setStatus(200);
                response.getWriter().write("{\"result\":\"ok\"}");
            }else{
                response.setStatus(400);
                response.getWriter().write("{\"result\":\"error\"}");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        response.setContentType("application/json");

        boolean updatePurchaseDate = true;
        boolean updateAmount = true;
        boolean updateCategory = true;
        boolean updateDescription = true;

        if(request.getPathInfo().equals("/update")){
            
            int id = 0;
            try{

                id = Integer.parseInt(request.getParameter("id"));

            }catch(NumberFormatException e){
                
                e.printStackTrace();

                response.setStatus(400);

                return; 
            }
            
            if(id > 0){
                
                Expense expesne = expenseDAO.getById(id);
                
                if(expesne == null){
                    response.setStatus(404);
                    return;
                }
                
                if(!request.getParameter("purchaseDate").toString().isBlank()){
                    
                    LocalDate purchaseDate = LocalDate.parse(request.getParameter("purchaseDate"));
                    if(!purchaseDate.equals(expesne.getPurchaseDate())){
                        updatePurchaseDate = expenseDAO.updatePurchaseDate(id, purchaseDate);
                    }
                }

                if(!request.getParameter("amount").isBlank()){
                    
                    double amount = Double.parseDouble(request.getParameter("amount"));
                    if(amount != expesne.getAmount()){
                        updateAmount = expenseDAO.updateAmount(id, amount);
                    }
                }

                if(!request.getParameter("category").isBlank()){

                    String category = request.getParameter("category");
                    if(!category.equals(expesne.getCategory())){
                        updateCategory = expenseDAO.updateCategory(id, category);
                    }
                }

                if(!request.getParameter("description").isBlank()){

                    String dedscription = request.getParameter("description");
                    if(!dedscription.equals(expesne.getDescription())){
                        updateDescription = expenseDAO.updateDescription(id, dedscription);
                    }
                }

                if(updatePurchaseDate == true 
                    && updateAmount == true
                    && updateCategory == true
                    && updateDescription == true){
                        response.setStatus(200);
                        response.getWriter().println(expesne.toString());
                }

            }else{
                response.setStatus(400);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        String path = request.getPathInfo();

        boolean success = false;

        if(path.equals("/delete")){


            String queryString = request.getQueryString();

            String parts[] = queryString.split("\\?id=");
            System.out.println("parts 0: " + parts[0]);
            //System.out.println("parts 1: " + parts[1]);
            
            int id = 0;
            try{

                id = Integer.parseInt(request.getParameter("id"));

            }catch(NumberFormatException e){
                
                e.printStackTrace();
                response.setStatus(400);
                return;
            }

            if(id > 0){

                Expense expense = expenseDAO.getById(id);
                if(expense != null){
                    success = expenseDAO.delete(expense);
                }
            }else{
                response.setStatus(404);
                response.getWriter().write("No expense found with the specified ID");
                return;
            }

            if(success){
                response.setStatus(200);
            }
        }
    }   
    
    // private void getByAmountOver(HttpServletRequest request, HttpServletResponse response)
    // throws ServletException, IOException{

    //     response.setContentType("application/json");

    //     double amount = 0;
    //     try{

    //         amount = Double.parseDouble(request.getParameter("amount"));

    //     }catch(NumberFormatException e){

    //         e.printStackTrace();

    //         response.setStatus(400);

    //         return;
    //     }
        
    //     List<Expense> expenses = expenseDAO.getByAnAmountOverOf(amount);
        
    //     if(!expenses.isEmpty()){
            
    //         response.setStatus(200);
            
    //         for (Expense e : expenses) {
    //             response.getWriter().println(e.toString());
    //         }

    //     }else{
    //         response.getWriter().write("No data found");
    //     }
    // }

    // private void getbyDate(HttpServletRequest request, HttpServletResponse response)
    // throws ServletException, IOException{

    //     response.setContentType("application/json");

    //     LocalDate date = null;
        
    //     try{

    //         date = LocalDate.parse(request.getParameter("date"));
        
    //     }catch(Exception e){
            
    //         e.printStackTrace();

    //         response.setStatus(400);

    //         return;
    //     }

    //     List<Expense> expenses = expenseDAO.getByStartingDate(date);

    //     if(!expenses.isEmpty()){
            
    //         response.setStatus(200);

    //         for (Expense e : expenses) {
    //             response.getWriter().println(e.toString());
    //         }

    //     }else{
    //         response.getWriter().write("No data found");
    //     }
    // }

    // private void getByCategory(HttpServletRequest request, HttpServletResponse response)
    // throws ServletException, IOException{

    //     response.setContentType("application/json");

    //     String category = request.getParameter("category");

    //     if(category != null && !category.isBlank()){
            
    //         List<Expense> expenses = expenseDAO.getByCategory(category);

    //         if(!expenses.isEmpty()){

    //             response.setStatus(200);

    //             for (Expense e : expenses) {
    //                 response.getWriter().println(e.toString());
    //             }

    //         }else{
    //             response.getWriter().write("No data found");
    //         }

    //     }else{
    //         response.setStatus(400);
    //     }
    // }

    
    // private void getByDateAndCategory(HttpServletRequest request, HttpServletResponse response)
    // throws ServletException, IOException{

    //     response.setContentType("application/json"); 

    //     String category = request.getParameter("category");
    //     LocalDate date = LocalDate.parse(request.getParameter("date"));

    //     if(category != null && date != null && !category.isBlank()){

    //         List<Expense> expenses = expenseDAO.getByDateAndCategory(date, category);

    //         if(!expenses.isEmpty()){

    //             response.setStatus(200);

    //             for(Expense e : expenses){
    //                 response.getWriter().println(e.toString());
    //             }

    //         }else{
    //             response.getWriter().write("No data found");
    //         }

    //     }else{
    //         response.setStatus(400);
    //     }
    // }
}