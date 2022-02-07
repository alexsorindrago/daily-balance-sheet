package com.fm.daily.balance.sheet.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String entityName;
    public String number;
    public BigDecimal value;
    public LocalDate dueDate;
    public String salesmanName;
    public boolean isPaid;

    @Enumerated(EnumType.STRING)
    public InvoiceType invoiceType;
}
