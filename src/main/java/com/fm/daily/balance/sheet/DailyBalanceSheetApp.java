package com.fm.daily.balance.sheet;

import com.fm.daily.balance.sheet.controller.InvoiceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DailyBalanceSheetApp {

    @Autowired
    InvoiceController invoiceController;

    @Bean
    CommandLineRunner loadData() {
        return args -> {
            invoiceController.createSupplierInvoices();
            invoiceController.createCustomerInvoices();
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DailyBalanceSheetApp.class, args);
    }
}