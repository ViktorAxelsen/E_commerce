package com.viktor.e_commerce;

public interface CartChangeListener {

    void selected(double total);

    void isAllSelected(boolean is);

    void delete();
}
