package com.fm.daily.balance.sheet.util;

import com.fm.daily.balance.sheet.models.Invoice;
import com.fm.daily.balance.sheet.models.InvoiceType;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CsvReader {

    private static final String COMMA_DELIMITER = ",";

    public List<List<String>> loadFromFile(String filePath) {
        List<List<String>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(records);

        return records;
    }

    public List<Invoice> loadFromFile(String filePath, InvoiceType invoiceType) {
        List<Invoice> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                Invoice invoice = new Invoice();
                invoice.entityName = values[0];
                invoice.number = values[1];
                invoice.value = new BigDecimal(values[2]);
                invoice.dueDate = LocalDate.parse(values[3], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                if (values.length == 5) {
                    invoice.salesmanName = values[4];
                }
                invoice.invoiceType = invoiceType;
                records.add(invoice);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(records);

        return records;
    }
}
