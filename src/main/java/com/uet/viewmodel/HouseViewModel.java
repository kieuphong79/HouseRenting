package com.uet.viewmodel;

import com.uet.model.House;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HouseViewModel {
    private ObjectProperty<House> houseProperty;
    public HouseViewModel() {
        houseProperty = new SimpleObjectProperty<>();
        
    }
}
