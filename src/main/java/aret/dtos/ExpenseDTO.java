package aret.dtos;

import java.time.LocalDate;

public record ExpenseDTO (
    LocalDate purchaseDate, 
    double amount, 
    String description, 
    String category){
    
}
