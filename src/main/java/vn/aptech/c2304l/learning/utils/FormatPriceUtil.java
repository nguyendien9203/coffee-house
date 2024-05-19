package vn.aptech.c2304l.learning.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatPriceUtil {
    public String formatPrice(BigDecimal price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setCurrencySymbol("₫");
        DecimalFormat decimalFormat = new DecimalFormat("#,### ₫", symbols);
        return decimalFormat.format(price);
    }
    private static FormatPriceUtil instance;
    private FormatPriceUtil() {
    }

    public static FormatPriceUtil getInstance() {
        if (instance == null) {
            instance = new FormatPriceUtil();
        }
        return instance;
    }

}
