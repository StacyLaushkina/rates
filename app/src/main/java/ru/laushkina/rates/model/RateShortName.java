package ru.laushkina.rates.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class RateShortName {
    public final static String AUD = "AUD";
    public final static String EUR = "EUR";
    public final static String BGN = "BGN";
    public final static String BRL = "BRL";
    public final static String CAD = "CAD";
    public final static String CHF = "CHF";
    public final static String CNY = "CNY";
    public final static String CZK = "CZK";
    public final static String DKK = "DKK";
    public final static String GBP = "GBP";
    public final static String HKD = "HKD";
    public final static String HRK = "HRK";
    public final static String HUF = "HUF";
    public final static String IDR = "IDR";
    public final static String ILS = "ILS";
    public final static String INR = "INR";
    public final static String ISK = "ISK";
    public final static String JPY = "JPY";
    public final static String KRW = "KRW";
    public final static String MXN = "MXN";
    public final static String MYR = "MYR";
    public final static String NOK = "NOK";
    public final static String NZD = "NZD";
    public final static String PHP = "PHP";
    public final static String PLN = "PLN";
    public final static String RON = "RON";
    public final static String RUB = "RUB";
    public final static String SEK = "SEK";
    public final static String SGD = "SGD";
    public final static String THB = "THB";
    public final static String USD = "USD";
    public final static String ZAR = "ZAR";

    @Retention(SOURCE)
    @StringDef({
            AUD,
            BGN,
            BRL,
            CAD,
            CHF,
            CNY,
            CZK,
            DKK,
            GBP,
            HKD,
            HRK,
            HUF,
            IDR,
            ILS,
            INR,
            ISK,
            JPY,
            KRW,
            MXN,
            MYR,
            NOK,
            NZD,
            PHP,
            PLN,
            RON,
            RUB,
            SEK,
            SGD,
            THB,
            USD,
            ZAR
    })
    public @interface Name {}
}
