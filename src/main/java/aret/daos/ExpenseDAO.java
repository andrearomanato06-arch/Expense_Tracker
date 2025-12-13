package aret.daos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;

import aret.entities.Expense;
import aret.utils.HibernateUtil;

public class ExpenseDAO {
    
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public static final int LIMIT = 10;

    public List<Expense> getAllExpenses(int page){

        List<Expense> expenses = new ArrayList<>();

        int offset = (page - 1) * LIMIT;

        try(Session session = sessionFactory.openSession()){
            
            expenses = session.createQuery("FROM Expense e ORDER BY e.purchaseDate DESC",Expense.class)
            .setFirstResult(offset)
            .setMaxResults(LIMIT)
            .list();

        }catch(Exception e){
            e.printStackTrace();
        }

        return expenses;
    }

    public int getNumberOfExpenses(){

        int numberOfExpenses = 0;

        try(Session session = sessionFactory.openSession()){
           Query<Long> query = session.createQuery("SELECT COUNT(e) FROM Expense e", Long.class);
            numberOfExpenses = query.uniqueResult().intValue();
        }catch(Exception e){
            e.printStackTrace();
        }

        return numberOfExpenses;
    }
    
    public Expense getById(int id){

        Expense expense = null;

        try(Session session = sessionFactory.openSession()){

            expense = session.find(Expense.class, id);

        }catch(Exception e){ 
            e.printStackTrace(); 
        }

        return expense;
    }

    public boolean insert(Expense expense){

        boolean success = false;

        try(Session session = sessionFactory.openSession()){

            Transaction transaction = session.beginTransaction();

            session.persist(expense);

            transaction.commit();

            success = true;

        }catch(Exception e){
            e.printStackTrace();
        }

        return success;
    }

    public boolean delete(Expense expense){

        boolean success = false;

        try(Session session = sessionFactory.openSession()){

            Transaction transaction = session.beginTransaction();

            session.remove(expense);

            transaction.commit();

            success = true;

        }catch(Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    public boolean updatePurchaseDate(int id, LocalDate newPurchaseDate){

        boolean success = false;

        try(Session session = sessionFactory.openSession()){

            Transaction transaction = session.beginTransaction();

            int rowsAffected = session.createMutationQuery(
                "UPDATE Expense e SET e.purchaseDate = :newPurchaseDate WHERE e.id =:id"
            )
            .setParameter("newPurchaseDate", newPurchaseDate)
            .setParameter("id", id)
            .executeUpdate();

            transaction.commit();

            if(rowsAffected > 0){
                success = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return success;
    }

    public boolean updateAmount(int id, double newAmount){

        boolean success = false;

        try(Session session = sessionFactory.openSession()){

            Transaction transaction = session.beginTransaction();

            int rowsAffected = session.createMutationQuery(
                "UPDATE Expense e SET e.amount = :newAmount WHERE e.id = :id"
            )
            .setParameter("newAmount", newAmount)
            .setParameter("id", id)
            .executeUpdate();

            transaction.commit();

            if(rowsAffected > 0){
                success = true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return success;
    }

    public boolean updateDescription(int id, String newDescription){

        boolean success = false;

        try(Session session = sessionFactory.openSession()){

            Transaction transaction = session.beginTransaction();

            int rowsAffected = session.createMutationQuery(
                "UPDATE Expense e SET e.description = :newDescription WHERE e.id = :id"
            )
            .setParameter("newDescription",newDescription)
            .setParameter("id",id)
            .executeUpdate();

            transaction.commit();

            if(rowsAffected > 0){
                success = true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        
        return success;
    }

    public boolean updateCategory(int id, String newCategory){
        
        boolean success = false;

        try(Session session = sessionFactory.openSession()){

            Transaction transaction = session.beginTransaction();

            int rowsAffected = session.createMutationQuery(
                "UPDATE Expense e SET e.category = :newCategory WHERE e.id = :id"
            )
            .setParameter("newCategory", newCategory)
            .setParameter("id", id)
            .executeUpdate();

            transaction.commit();

            if(rowsAffected > 0){
                success = true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return success;
    }

    public List<Expense> filter(String category, String dateFrom, String dateTo, String amount){
        
        List<Expense> expenses = new ArrayList<>();

        LocalDate selectedDateFrom = null;
        LocalDate selectedDateTo = null;
        double selectedAmount = 0;

        StringBuilder query = new StringBuilder("FROM Expense e WHERE 1=1");

        if(category != null && !category.isBlank()){
            query.append(" AND e.category = :category");
        }

        if(dateFrom != null && !dateFrom.isBlank()){
            query.append(" AND e.purchaseDate >=:dateFrom");
            selectedDateFrom = LocalDate.parse(dateFrom);
        }

        if(dateTo != null && !dateTo.isBlank()){
            query.append(" AND e.purchaseDate <=:dateTo");
            selectedDateTo = LocalDate.parse(dateTo);
        }

        if(amount != null && !amount.isBlank()){
            query.append(" AND e.amount >=:amount");
            selectedAmount = Double.parseDouble(amount);
        }

        try(Session session = sessionFactory.openSession()){

            query.append(" ORDER BY e.purchaseDate DESC");

            Query<Expense> preparedQuery = session.createQuery(query.toString(), Expense.class); 

            if(category != null && !category.isBlank()){
                preparedQuery.setParameter("category", category);
            }
            if(selectedAmount != 0){
                preparedQuery.setParameter("amount",selectedAmount);
            }
            if(selectedDateFrom != null){
                preparedQuery.setParameter("dateFrom", selectedDateFrom);
            }
            if(selectedDateTo != null){
                preparedQuery.setParameter("dateTo", selectedDateTo);
            }

            
            
            expenses = preparedQuery.list();

        }catch(Exception e){
            e.printStackTrace();
        }
        
        return expenses;
    }

    // public List<Expense> getByStartingDate(LocalDate startingDate){

    //     List<Expense> expenses = new ArrayList<>();

    //     try(Session  session = sessionFactory.openSession()){

    //         expenses = session.createQuery(
    //             "FROM Expense e WHERE e.purchaseDate >= :startingDate",
    //             Expense.class
    //         )
    //         .setParameter("startingDate",startingDate)
    //         .list();

    //     }catch(Exception e){
    //         e.printStackTrace();
    //     }

    //     return expenses;
    // }

    // public List<Expense> getByCategory(String searchedCategory){

    //     List<Expense> expenses = new ArrayList<>();

    //     try(Session session = sessionFactory.openSession()){

    //         expenses = session.createQuery(
    //             "FROM Expense e WHERE e.category = :searchedCategory"
    //             ,Expense.class
    //         )
    //         .setParameter("searchedCategory",searchedCategory)
    //         .list();

    //     }catch(Exception e){
    //         e.printStackTrace();
    //     }

    //     return expenses;
    // }

    // public List<Expense> getByDateAndCategory(LocalDate date, String category){

    //     List<Expense> expenses = new ArrayList<>();

    //     try(Session session = sessionFactory.openSession()){

    //         expenses = session.createQuery(
    //             "FROM Expense e WHERE e.category = :category AND e.purchaseDate >= :date",
    //             Expense.class
    //         )
    //         .setParameter("category",category)
    //         .setParameter("date", date)
    //         .list();

    //     }catch(Exception e){
    //         e.printStackTrace();
    //     }

    //     return expenses;
    // }

    // public List<Expense> getByAnAmountOverOf(double amount){

    //     List<Expense> expenses = new ArrayList<>();

    //     try(Session session = sessionFactory.openSession()){

    //         expenses = session.createQuery(
    //             "FROM Expense e WHERE e.amount >= :amount"
    //             ,Expense.class
    //         )
    //         .setParameter("amount",amount)
    //         .list();

    //     }catch(Exception e){
    //         e.printStackTrace();
    //     }

    //     return expenses;
    // }
}