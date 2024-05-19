package vn.aptech.c2304l.learning.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class UniqueCodeBillGenerator {
    public static String generateCodeBill() {
        LocalDate localDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedString = localDate.format(formatter);

        String randomCode = UUID.randomUUID().toString().replace("-","").substring(0, 8).toUpperCase();

        return formattedString + randomCode;
    }

    private UniqueCodeBillGenerator() {
    }

    private static UniqueCodeBillGenerator instance;

    public static UniqueCodeBillGenerator getInstance() {
        if(instance == null) {
            instance = new UniqueCodeBillGenerator();
        }
        return instance;
    }



}
