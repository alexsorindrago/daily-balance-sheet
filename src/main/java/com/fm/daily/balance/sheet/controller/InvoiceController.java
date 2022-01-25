package com.fm.daily.balance.sheet.controller;

import com.fm.daily.balance.sheet.CsvReader;
import com.fm.daily.balance.sheet.dto.CustomerInvoiceVM;
import com.fm.daily.balance.sheet.dto.InvoiceDto;
import com.fm.daily.balance.sheet.models.Customer;
import com.fm.daily.balance.sheet.models.Invoice;
import com.fm.daily.balance.sheet.models.Supplier;
import com.fm.daily.balance.sheet.repository.CustomerRepository;
import com.fm.daily.balance.sheet.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/invoices")
@RestController
public class InvoiceController {

    private static final String CUSTOMER_SOURCE = "src/main/resources/customerData.csv";
    private static final String SUPPLIER_SOURCE = "src/main/resources/supplierData.csv";

    private final CsvReader csvReader;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public InvoiceController(CsvReader csvReader, InvoiceRepository invoiceRepository, CustomerRepository customerRepository) {
        this.csvReader = csvReader;
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
    }

    public void createCustomerInvoices() {
        List<List<String>> entries = csvReader.loadFromFile(CUSTOMER_SOURCE);

        entries.forEach(entry -> {
            CustomerInvoiceVM invoiceVM = new CustomerInvoiceVM();
            invoiceVM.name = entry.get(0);
            invoiceVM.number = entry.get(1);
            invoiceVM.value = new BigDecimal(entry.get(2));
            invoiceVM.dueDate = LocalDate.parse(entry.get(3), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            Customer customer = new Customer();
            customer.name = invoiceVM.name;
            customerRepository.save(customer);

            Invoice invoice = new Invoice();
            invoice.number = invoiceVM.number;
            invoice.value = invoiceVM.value;
            invoice.dueDate = invoiceVM.dueDate;
            invoiceRepository.save(invoice);
        });
    }

    public List<InvoiceDto> findCustomerInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream()
                .map(invoice -> toInvoiceDto(invoice))
                .collect(Collectors.toList());

        //TODO:  add customer name
    }

    public List<InvoiceDto> findSupplierInvoices() {
        // get invoice data
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream()
                .map(invoice -> toInvoiceDto(invoice))
                .collect(Collectors.toList());

        // add customer name
    }

    private InvoiceDto toInvoiceDto(Invoice invoice) {
        InvoiceDto dto = new InvoiceDto();
        dto.id = invoice.id;
        dto.number = invoice.number;
        dto.value = invoice.value;
        dto.dueDate = invoice.dueDate;
        dto.isPaid = invoice.isPaid;
        return dto;
    }


    public List<Supplier> toSupplierList(List<List<String>> data) {
        return data.stream()
                .map(entry -> {
                    Supplier supplier = new Supplier();
                    supplier.name = entry.get(0);
                    return supplier;
                })
                .collect(Collectors.toList());
    }
}
