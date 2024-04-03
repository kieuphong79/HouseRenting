package com.uet.view;


import com.uet.model.House;
import com.uet.viewmodel.HouseViewModel;

import javafx.scene.layout.StackPane;

public class HouseView extends StackPane{
    private House curHouse;
    private HouseViewModel houseViewModel;

    public HouseView() {
        //Test 
        super();
        curHouse = House.sample; 
        //
        //initialize 
        houseViewModel = new HouseViewModel();
        ImageShower imageShower = new ImageShower(curHouse.getImagesUrl());
        
        //binding
    }

}
