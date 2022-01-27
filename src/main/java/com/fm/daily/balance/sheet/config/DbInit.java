package com.fm.daily.balance.sheet.config;

import com.fm.daily.balance.sheet.controller.InvoiceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbInit {

    @Autowired
    InvoiceController invoiceController;

    @Bean
    CommandLineRunner loadData() {
        return args -> {
            invoiceController.createSupplierInvoices();
            invoiceController.createCustomerInvoices();
        };
    }
}
