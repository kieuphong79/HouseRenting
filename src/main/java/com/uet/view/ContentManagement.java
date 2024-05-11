package com.uet.view;



import java.util.Hashtable;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;

import com.uet.model.SearchParameter;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class ContentManagement extends TabPane {
    public static final int SEARCH_FUNCTION = -1;
    private static ContentManagement singleton;
    public static ContentManagement getInstance() {
        if (singleton == null) singleton = new ContentManagement();
        return singleton;
    }
    private Hashtable<Integer, Tab> tabCache;
    private ContentManagement() {
        super();
        tabCache = new Hashtable<>();
        this.setTabMaxWidth(200);
        this.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
    }
    public void addSearchTab() {
        var searchView = new SearchView();
        var searchViewModel = searchView.getSearchViewModel();
        searchViewModel.setOffset(0);
        SearchParameter t = new SearchParameter(searchView.getSearchBar().getSearchBarViewModel());
        searchViewModel.setSearchParameter(t);
        searchViewModel.search();
        Tab searchTab = new Tab("Tìm kiếm", searchView);
        FontIcon si = new FontIcon(Material2MZ.SEARCH);
        si.setIconSize(20);
        searchTab.setGraphic(si);
        searchTab.setClosable(false);
        this.getTabs().add(searchTab);
        tabCache.put(SEARCH_FUNCTION, searchTab);
    }
    public void showFunction(int id) {
        // functionTabCache chac chan co node day
        if (!this.getTabs().contains(tabCache.get(id))) {
            this.getTabs().add(tabCache.get(id));
        }
        this.getSelectionModel().select(tabCache.get(id));
    }
    // public void addHouseView(House house) {
    //     Tab res = new Tab(house.getTitle());
    //     res.setContent(new HouseView(house));
    //     FontIcon houseIcon = new FontIcon(Material2OutlinedAL.HOUSE);
    //     houseIcon.setIconSize(20);
    //     res.setGraphic(houseIcon);
    //     this.getTabs().add(res);
    //     this.getSelectionModel().select(res);
    // } 
    // public void addUserView(UserView userview) {
    //     Tab res = new Tab("Cá nhân");
    //     res.setContent(userview);
    //     var icon = new FontIcon(Material2AL.ACCOUNT_CIRCLE);
    //     icon.setIconSize(20);
    //     res.setGraphic(icon);
    //     this.getTabs().add(res);
    //     this.getSelectionModel().select(res);
    // }
    public void addContent(Node node, String title, FontIcon icon) {
        var res = new Tab(title);
        res.setContent(node);
        icon.setIconSize(20);
        res.setGraphic(icon);
        this.getTabs().add(res);
        this.getSelectionModel().select(res);

    }
}
