package com.fm.daily.balance.sheet.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice {

    public Long id;
    public String number;
    public BigDecimal value;
    public LocalDate dueDate;
    public boolean isPaid;
}
