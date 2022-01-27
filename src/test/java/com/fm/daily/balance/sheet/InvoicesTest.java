package com.fm.daily.balance.sheet;

import com.fm.daily.balance.sheet.controller.InvoiceController;
import com.fm.daily.balance.sheet.dto.InvoiceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InvoicesTest {

    @Autowired
    private InvoiceController invoiceController;

    @Test
    void shouldDisplayCustomers() {
        invoiceController.createCustomerInvoices();

        List<InvoiceDto> customerInvoices = invoiceController.findCustomerInvoices();
        assertThat(customerInvoices).hasSize(2);

        Long firstInvoiceId = customerInvoices.get(0).id;
        invoiceController.updateInvoice(firstInvoiceId);

        List<InvoiceDto> updatedInvoices = invoiceController.findCustomerInvoices();
        boolean isPaid = updatedInvoices.get(0).isPaid;
        assertThat(isPaid).isTrue();
    }

    @Test
    void shouldCreateSupplierInvoice() {
        invoiceController.createSupplierInvoices();

        List<InvoiceDto> supplierInvoices = invoiceController.findSupplierInvoices();
        assertThat(supplierInvoices).hasSize(2);

        Long firstInvoiceId = supplierInvoices.get(0).id;
        invoiceController.updateInvoice(firstInvoiceId);

        List<InvoiceDto> updatedInvoices = invoiceController.findSupplierInvoices();
        boolean isPaid = updatedInvoices.get(0).isPaid;
        assertThat(isPaid).isTrue();
    }
}
