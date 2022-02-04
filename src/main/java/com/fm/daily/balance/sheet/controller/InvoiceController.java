package com.fm.daily.balance.sheet.controller;

import com.fm.daily.balance.sheet.models.Invoice;
import com.fm.daily.balance.sheet.models.InvoiceType;
import com.fm.daily.balance.sheet.repository.InvoiceRepository;
import com.fm.daily.balance.sheet.util.CsvReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/invoices")
@RestController
public class InvoiceController {

    private static final String CUSTOMER_SOURCE = "src/main/resources/customerData.csv";
    private static final String SUPPLIER_SOURCE = "src/main/resources/supplierData.csv";

    private final CsvReader csvReader;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceController(CsvReader csvReader,
                             InvoiceRepository invoiceRepository) {
        this.csvReader = csvReader;
        this.invoiceRepository = invoiceRepository;
    }

    public void loadData() {
        List<Invoice> customerInvoices = csvReader.loadFromFile(CUSTOMER_SOURCE, InvoiceType.CUSTOMER);
        List<Invoice> supplierInvoices = csvReader.loadFromFile(SUPPLIER_SOURCE, InvoiceType.SUPPLIER);
        createInvoices(customerInvoices);
        createInvoices(supplierInvoices);
    }

    @PostMapping
    public List<Invoice> createInvoices(List<Invoice> invoices) {
        return invoices.stream()
                .map(invoice -> {
                    invoiceRepository.findByNumber(invoice.number)
                            .ifPresent(existingInvoice -> {
                                throw new RuntimeException("duplicate invoice");
                            });
                    return invoiceRepository.save(invoice);
                })
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<Invoice> findInvoices(@RequestParam(required = false) InvoiceType invoiceType,
                                      @RequestParam(required = false) String entityName,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate startDate,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate endDate
    ) {
        return invoiceRepository.findInvoices(invoiceType, entityName, startDate, endDate);
    }

    @PutMapping("/{invoiceId}/pay")
    public void payInvoice(@PathVariable Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));
        invoice.isPaid = true;
        invoiceRepository.save(invoice);
    }

    @PutMapping("/{invoiceId}/un-pay")
    public void unPayInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));
        invoice.isPaid = false;
        invoiceRepository.save(invoice);
    }

}
