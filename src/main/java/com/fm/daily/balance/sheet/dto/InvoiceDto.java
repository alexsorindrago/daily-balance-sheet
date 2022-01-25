package com.fm.daily.balance.sheet.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceDto {

    public Long id;
    public String name;
    public String number;
    public BigDecimal value;
    public LocalDate dueDate;
    public boolean isPaid;
}
