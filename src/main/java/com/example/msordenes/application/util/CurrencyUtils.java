package com.example.msordenes.application.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {

    private static final Locale CL = new Locale("es", "CL");

    public static String formatCLP(Integer monto) {
        if (monto == null) return "$0";

        NumberFormat nf = NumberFormat.getCurrencyInstance(CL);
        return nf.format(monto);
    }
}
