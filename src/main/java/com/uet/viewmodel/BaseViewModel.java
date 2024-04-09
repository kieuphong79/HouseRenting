package com.uet.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BaseViewModel {
    private StringProperty curCategory;
    public BaseViewModel(String defaultView) {
        curCategory = new SimpleStringProperty(defaultView);
    }
    public StringProperty curCategortStringProperty() {
        return curCategory;
    }
}
