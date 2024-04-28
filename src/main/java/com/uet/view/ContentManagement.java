package com.uet.view;



import java.util.Hashtable;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import com.uet.model.House;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class ContentManagement extends TabPane {
    public static final int SEARCH_FUNCTION = -1;
    private static final int DEFAULT_FUNCTION = SEARCH_FUNCTION;
    private static ContentManagement singleton;
    public static ContentManagement getInstance() {
        if (singleton == null) singleton = new ContentManagement();
        return singleton;
    }
    private Hashtable<Integer, Tab> funtionTabCache;
    private ContentManagement() {
        super();
        funtionTabCache = new Hashtable<>();
        this.setTabMaxWidth(200);
        addSearchTab();
        this.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
    }
    private void addSearchTab() {
        var searchView = new SearchView();
        Tab searchTab = new Tab("Tìm kiếm", new VBox(searchView.getSearchBar(), searchView));
        FontIcon si = new FontIcon(Material2MZ.SEARCH);
        si.setIconSize(20);
        searchTab.setGraphic(si);
        searchTab.setClosable(false);
        this.getTabs().add(searchTab);
        funtionTabCache.put(SEARCH_FUNCTION, searchTab);
    }
    public void showFunction(int id) {
        // functionTabCache chac chan co node day
        if (!this.getTabs().contains(funtionTabCache.get(id))) {
            this.getTabs().add(funtionTabCache.get(id));
        }
        this.getSelectionModel().select(funtionTabCache.get(id));
    }
    public void addHouseView(House house) {
        Tab res = new Tab(house.getTitle());
        res.setContent(new HouseView(house));
        FontIcon houseIcon = new FontIcon(Material2OutlinedAL.HOUSE);
        houseIcon.setIconSize(20);
        res.setGraphic(houseIcon);
        this.getTabs().add(res);
        this.getSelectionModel().select(res);
    } 
}
