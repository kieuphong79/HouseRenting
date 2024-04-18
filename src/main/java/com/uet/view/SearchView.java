package com.uet.view;


import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class SearchView extends ScrollPane {
    private SearchBar searchBar;
    public SearchView() {
        super();
        searchBar = new SearchBar();
        VBox container = new VBox();
        
    }
    public SearchBar getSearchBar() {
        return searchBar;
    }
}
