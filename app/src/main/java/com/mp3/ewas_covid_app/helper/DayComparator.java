package com.mp3.ewas_covid_app.helper;

import com.mp3.ewas_covid_app.Models.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class DayComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd HH/mm").parse(t1.getDate() + " " + t1.getTime());
            Date dateToCompare = new SimpleDateFormat("yyyy/MM/dd HH/mm").parse(t2.getDate() + " " + t1.getTime());
            return dateToCompare.compareTo(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
