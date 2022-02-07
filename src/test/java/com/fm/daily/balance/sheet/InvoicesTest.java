package com.fm.daily.balance.sheet;

import com.fm.daily.balance.sheet.controller.InvoiceController;
import com.fm.daily.balance.sheet.models.Invoice;
import com.fm.daily.balance.sheet.models.InvoiceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InvoicesTest {

    @Autowired
    private InvoiceController invoiceController;

    @Test
    void shouldLoadData() {
        invoiceController.loadData();
    }

    @Test
    void shouldFindInvoicesBetweenDates() {
        Invoice invoice1 = new Invoice();
        invoice1.entityName = "Customer 1";
        invoice1.number = "P1111";
        invoice1.value = new BigDecimal("100");
        invoice1.dueDate = LocalDate.of(2022, 1, 25);
        invoice1.salesmanName = "Salesman 1";
        invoice1.invoiceType = InvoiceType.CUSTOMER;

        Invoice invoice2 = new Invoice();
        invoice2.entityName = "Customer 2";
        invoice2.number = "P2222";
        invoice2.value = new BigDecimal("100");
        invoice2.dueDate = LocalDate.of(2022, 1, 25);
        invoice2.salesmanName = "Salesman 2";
        invoice2.invoiceType = InvoiceType.CUSTOMER;

        Invoice invoice3 = new Invoice();
        invoice3.entityName = "Customer 2";
        invoice3.number = "P3333";
        invoice3.value = new BigDecimal("100");
        invoice3.dueDate = LocalDate.of(2022, 1, 27);
        invoice3.salesmanName = "Salesman 2";
        invoice3.invoiceType = InvoiceType.CUSTOMER;

        Invoice invoice4 = new Invoice();
        invoice4.entityName = "Supplier 1";
        invoice4.number = "P4444";
        invoice4.value = new BigDecimal("100");
        invoice4.dueDate = LocalDate.of(2022, 1, 27);
        invoice4.invoiceType = InvoiceType.SUPPLIER;

        invoiceController.createInvoices(List.of(invoice1, invoice2, invoice3, invoice4));

        List<Invoice> invoices = invoiceController.findInvoices(null, null, null, null);
        assertThat(invoices).hasSize(4);

        invoices = invoiceController.findInvoices(InvoiceType.CUSTOMER, null, null, null);
        assertThat(invoices).hasSize(3);

        invoices = invoiceController.findInvoices(InvoiceType.CUSTOMER, "Customer 1", null, null);
        assertThat(invoices).hasSize(1);

        invoices = invoiceController.findInvoices(InvoiceType.SUPPLIER, "Supplier 1", null, null);
        assertThat(invoices).hasSize(1);

        invoices = invoiceController.findInvoices(
                InvoiceType.CUSTOMER,
                "Customer 2",
                LocalDate.of(2022, 1, 27),
                null);
        assertThat(invoices).hasSize(1);

        invoices = invoiceController.findInvoices(
                InvoiceType.CUSTOMER,
                "Customer 2",
                LocalDate.of(2022, 1, 25),
                LocalDate.of(2022, 1, 27));
        assertThat(invoices).hasSize(2);
    }
}
