package aret.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="expense")
public class Expense {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;
    
    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "category", nullable = false)
    private String category;

    public Expense() {}

    public Expense(int id, LocalDate purchaseDate, double amount, String description, String category) {
        this.id = id;
        this.purchaseDate = purchaseDate;
        this.amount = amount;
        this.description = description;
        this.category = category;
    }

    public Expense(LocalDate purchaseDate, double amount, String description, String category) {
        this.purchaseDate = purchaseDate;
        this.amount = amount;
        this.description = description;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("ID: ").append(id).append(" ")
            .append("Purchase date: ").append(purchaseDate).append(" ")
            .append("Description: ").append(description).append(" ")
            .append("Category: ").append(category)
            .toString();
    }
}