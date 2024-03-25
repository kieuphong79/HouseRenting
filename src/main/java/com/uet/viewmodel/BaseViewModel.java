package com.uet.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BaseViewModel {
    private StringProperty curCategory;
    public BaseViewModel() {
        curCategory = new SimpleStringProperty();

    }
    public StringProperty curCategortStringProperty() {
        return curCategory;
    }
}
