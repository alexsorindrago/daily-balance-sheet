package com.fm.daily.balance.sheet.controller;

import com.fm.daily.balance.sheet.dto.InvoiceDto;
import com.fm.daily.balance.sheet.models.Customer;
import com.fm.daily.balance.sheet.models.Invoice;
import com.fm.daily.balance.sheet.models.Salesman;
import com.fm.daily.balance.sheet.models.Supplier;
import com.fm.daily.balance.sheet.repository.CustomerRepository;
import com.fm.daily.balance.sheet.repository.InvoiceRepository;
import com.fm.daily.balance.sheet.repository.SalesmanRepository;
import com.fm.daily.balance.sheet.repository.SupplierRepository;
import com.fm.daily.balance.sheet.util.CsvReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private final SupplierRepository supplierRepository;
    private final SalesmanRepository salesmanRepository;

    @Autowired
    public InvoiceController(CsvReader csvReader,
                             InvoiceRepository invoiceRepository,
                             CustomerRepository customerRepository,
                             SupplierRepository supplierRepository,
                             SalesmanRepository salesmanRepository) {
        this.csvReader = csvReader;
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.supplierRepository = supplierRepository;
        this.salesmanRepository = salesmanRepository;
    }

    @PostMapping("/customers")
    public void createCustomerInvoices() {
        List<List<String>> entries = csvReader.loadFromFile(CUSTOMER_SOURCE);

        entries.forEach(entry -> {
            Customer customer = new Customer();
            customer.name = entry.get(0);
            customerRepository.save(customer);

            Salesman salesman = new Salesman();
            salesman.name = entry.get(4);
            salesmanRepository.save(salesman);

            Invoice invoice = new Invoice();
            invoice.number = entry.get(1);
            invoice.value = new BigDecimal(entry.get(2));
            invoice.dueDate = LocalDate.parse(entry.get(3), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            invoice.customerId = customer.id;
            invoice.salesmanId = salesman.id;
            invoiceRepository.save(invoice);
        });
    }

    @PostMapping("/suppliers")
    public void createSupplierInvoices() {
        List<List<String>> entries = csvReader.loadFromFile(SUPPLIER_SOURCE);

        entries.forEach(entry -> {
            Supplier supplier = new Supplier();
            supplier.name = entry.get(0);
            supplierRepository.save(supplier);

            Invoice invoice = new Invoice();
            invoice.number = entry.get(1);
            invoice.value = new BigDecimal(entry.get(2));
            invoice.dueDate = LocalDate.parse(entry.get(3), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            invoice.supplierId = supplier.id;
            invoiceRepository.save(invoice);
        });
    }

    @GetMapping("/customers")
    public List<InvoiceDto> findCustomerInvoices() {
        List<Customer> customers = customerRepository.findAll();

        List<InvoiceDto> result = new ArrayList<>();

        customers.forEach(customer -> {
            List<InvoiceDto> invoicesForCustomer = findInvoicesForCustomer(customer.id);
            result.addAll(invoicesForCustomer);
        });
        return result;
    }

    @GetMapping("/customers/{customerId}")
    public List<InvoiceDto> findInvoicesForCustomer(@PathVariable Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("customer not found"));

        List<Invoice> invoices = invoiceRepository.findAllByCustomerId(customerId);

        return invoices.stream()
                .map(invoice -> toInvoiceDto(invoice, customer.name))
                .collect(Collectors.toList());
    }

    @GetMapping("/suppliers")
    public List<InvoiceDto> findSupplierInvoices() {
        List<Supplier> suppliers = supplierRepository.findAll();

        List<InvoiceDto> result = new ArrayList<>();

        suppliers.forEach(supplier -> {
            List<InvoiceDto> invoicesForSupplier = findInvoicesForSupplier(supplier.id);
            result.addAll(invoicesForSupplier);
        });

        return result;
    }

    @GetMapping("/suppliers/{supplierId}")
    public List<InvoiceDto> findInvoicesForSupplier(@PathVariable Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("not found"));

        List<Invoice> invoices = invoiceRepository.findAllBySupplierId(supplierId);

        return invoices.stream()
                .map(invoice -> toInvoiceDto(invoice, supplier.name))
                .collect(Collectors.toList());
    }

    @PutMapping("/{invoiceId}")
    public void updateInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));
        invoice.isPaid = !invoice.isPaid;
        invoiceRepository.save(invoice);
    }

    private InvoiceDto toInvoiceDto(Invoice invoice, String name) {
        InvoiceDto dto = new InvoiceDto();
        dto.id = invoice.id;
        dto.name = name;
        dto.number = invoice.number;
        dto.value = invoice.value;
        dto.dueDate = invoice.dueDate;
        dto.isPaid = invoice.isPaid;
        return dto;
    }

}
