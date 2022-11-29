package com.mp3.ewas_covid_app.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ArrayListTransactionWrapper implements Serializable {
    private ArrayList<Transaction> wrappedData;

    public ArrayListTransactionWrapper(ArrayList<Transaction> wrappedData) {
        this.wrappedData = wrappedData;
    }

    public ArrayList<Transaction> getWrappedData() {
        return wrappedData;
    }
}
