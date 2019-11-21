package com.redhat.messaging.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private int id;

    @NotNull
    @Size(min = 1, max = 10)
    @Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
    private String item;

    @NotNull
    @Positive
    private int amount;

    @NotNull
    @Size(min = 1, max = 30)
    @Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
    private String description;

    @NotNull
    private byte processed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getProcessed() {
        return processed;
    }

    public void setProcessed(byte processed) {
        this.processed = processed;
    }
}
