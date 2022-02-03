package com.fm.daily.balance.sheet;

import com.fm.daily.balance.sheet.controller.InvoiceController;
import com.fm.daily.balance.sheet.dto.InvoiceDto;
import com.fm.daily.balance.sheet.models.Customer;
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
        Invoice customerInvoice1 = new Invoice();
        customerInvoice1.customerName = "Customer 1";
        customerInvoice1.number = "P1234";
        customerInvoice1.value = new BigDecimal("100");
        customerInvoice1.dueDate = LocalDate.of(2022, 1, 25);

        Invoice customerInvoice2 = new Invoice();
        customerInvoice2.customerName = "Customer 1";
        customerInvoice2.number = "P1234";
        customerInvoice2.value = new BigDecimal("100");
        customerInvoice2.dueDate = LocalDate.of(2022, 1, 25);

        invoiceController.createInvoices(List.of(customerInvoice1, customerInvoice2));

        Invoice supplierInvoice1 = new Invoice();
        supplierInvoice1.customerName = "Supplier 1";
        supplierInvoice1.number = "P1234";
        supplierInvoice1.value = new BigDecimal("100");
        supplierInvoice1.dueDate = LocalDate.of(2022, 1, 25);

        Invoice supplierInvoice2 = new Invoice();
        supplierInvoice2.customerName = "Supplier 1";
        supplierInvoice2.number = "P1234";
        supplierInvoice2.value = new BigDecimal("100");
        supplierInvoice2.dueDate = LocalDate.of(2022, 1, 25);
        invoiceController.createInvoices(List.of(supplierInvoice1, supplierInvoice2));

        invoiceController.createCustomerInvoices();

        List<InvoiceDto> customerInvoices = invoiceController.findCustomerInvoices();
        assertThat(customerInvoices).hasSize(2);

        List<InvoiceDto> invoicesForCustomer1 = invoiceController.findInvoicesForCustomer(1L);
        assertThat(invoicesForCustomer1).hasSize(1);

        List<InvoiceDto> invoicesForSalesman1 = invoiceController.findInvoicesForSalesman(5L);
        assertThat(invoicesForSalesman1).hasSize(1);

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
    void shouldFindInvoicesBetweenDates() {
        Customer customer = invoiceController.createCustomer(new Customer("Customer 1"));

        Invoice customerInvoice1 = new Invoice();
        customerInvoice1.customerName = "Customer 1";
        customerInvoice1.number = "P1234";
        customerInvoice1.value = new BigDecimal("100");
        LocalDate dueDate = LocalDate.of(2022, 1, 25);
        customerInvoice1.dueDate = dueDate;
        customerInvoice1.customerId = customer.id;

        Invoice customerInvoice2 = new Invoice();
        customerInvoice2.customerName = "Customer 1";
        customerInvoice2.number = "P1234";
        customerInvoice2.value = new BigDecimal("100");
        customerInvoice2.dueDate = LocalDate.of(2022, 1, 27);
        customerInvoice2.customerId = customer.id;

        invoiceController.createInvoices(List.of(customerInvoice1, customerInvoice2));

        List<InvoiceDto> invoices = invoiceController.findAllByCustomerIdBetween(customer.id, dueDate, dueDate.plusDays(2));
        assertThat(invoices).hasSize(2);
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
