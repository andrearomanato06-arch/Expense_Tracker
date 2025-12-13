package aret;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import aret.daos.ExpenseDAO;
import aret.entities.Expense;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        ExpenseDAO eDAO = new ExpenseDAO();
        System.out.println(eDAO.getNumberOfExpenses());
        assertTrue(true);
    }
}
