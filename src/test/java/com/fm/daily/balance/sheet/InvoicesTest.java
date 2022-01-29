package com.fm.daily.balance.sheet;

import com.fm.daily.balance.sheet.controller.InvoiceController;
import com.fm.daily.balance.sheet.dto.InvoiceDto;
import com.fm.daily.balance.sheet.models.Invoice;
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
    void shouldDisplayCustomers() {
        Invoice invoice = new Invoice();
        invoice.customerName = "Customer 1";
        invoice.number = "P1234";
        invoice.value = new BigDecimal("100");
        invoice.dueDate = LocalDate.of(2022, 1, 25);

        invoiceController.createCustomerInvoices();

        List<InvoiceDto> customerInvoices = invoiceController.findCustomerInvoices();
        assertThat(customerInvoices).hasSize(2);

        Long firstInvoiceId = customerInvoices.get(0).id;
        invoiceController.payInvoice(firstInvoiceId);

        List<InvoiceDto> updatedInvoices = invoiceController.findCustomerInvoices();
        boolean isPaid = updatedInvoices.get(0).isPaid;
        assertThat(isPaid).isTrue();

        invoiceController.unPayInvoice(firstInvoiceId);
        List<InvoiceDto> updatedInvoices2 = invoiceController.findCustomerInvoices();
        boolean isNotPaid = updatedInvoices2.get(0).isPaid;
        assertThat(isNotPaid).isTrue();
    }

    @Test
    void shouldCreateSupplierInvoice() {
        invoiceController.createSupplierInvoices();

        List<InvoiceDto> supplierInvoices = invoiceController.findSupplierInvoices();
        assertThat(supplierInvoices).hasSize(2);

        Long firstInvoiceId = supplierInvoices.get(0).id;
        invoiceController.payInvoice(firstInvoiceId);

        List<InvoiceDto> updatedInvoices = invoiceController.findSupplierInvoices();
        boolean isPaid = updatedInvoices.get(0).isPaid;
        assertThat(isPaid).isTrue();
    }
}
