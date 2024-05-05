package com.uet.view;


import java.util.ArrayList;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import com.uet.App;
import com.uet.model.House;
import com.uet.model.SearchParameter;
import com.uet.model.UserControl;
import com.uet.threads.MultiThread;
import com.uet.viewmodel.SearchViewModel;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SearchView extends ScrollPane implements UserUpdate{
    private SearchBar searchBar;
    private SearchViewModel searchViewModel;
    private SimpleBooleanProperty housesChanged;
    private VBox container;
    private Text textResult;
    private ImageView noResultImage;
    private Button resetButton;
    private Text totalText;
    private Pagination pg;

    //user independent component
    private List<HouseOverview> listHousesContainer;

    public SearchView() {
        super();
        searchBar = new SearchBar();
        searchViewModel = new SearchViewModel();
        searchBar.setOnSearchButton(searchViewModel);
        housesChanged = new SimpleBooleanProperty(false);
        
        textResult = new Text("Hãy tìm kiếm gì đó");
        textResult.getStyleClass().addAll(Styles.TITLE_3);
        textResult.setWrappingWidth(800);

        totalText = new Text("abc");
        
        pg = new Pagination();
        pg.setMaxPageIndicatorCount(10);
        pg.currentPageIndexProperty().addListener((obs, old, neww) -> {
            if (old == neww) return;
            System.out.println((int)neww);
            searchViewModel.setOffset((int)(neww) * searchViewModel.getLimit());
            System.out.println("page change");
            searchViewModel.search();
            
        });


        noResultImage = new ImageView(App.class.getResource("noResult.png").toString());
        noResultImage.setPreserveRatio(true);
        noResultImage.setFitWidth(600);
        resetButton = new Button("Đặt lại tiêu chí");
        resetButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.DANGER);
        FontIcon resetIcon = new FontIcon(Material2AL.AUTORENEW);
        resetIcon.setIconSize(20);
        resetButton.setGraphic(resetIcon);
        resetButton.setOnAction(e -> {
            searchBar.getSearchBarViewModel().reset();
            searchViewModel.setOffset(0);
            SearchParameter t = new SearchParameter(searchBar.getSearchBarViewModel());
            searchViewModel.setSearchParameter(t);
            searchViewModel.search();
        });
        
        container = new VBox();
        this.setContent(container);
        container.setPadding(new Insets(30, 0, 10, 30));
        container.setSpacing(20);
        container.getChildren().addAll(textResult, totalText, pg);

        listHousesContainer = new ArrayList<>();
        for (int i = 0; i < searchViewModel.getLimit(); i++) {
            listHousesContainer.add(new HouseOverview());
        }
        // container.getChildren().addAll(listHousesContainer);
        //bind
        housesChanged.bind(searchViewModel.housesChangedProperty());
        System.out.println("binded");
        housesChanged.addListener((obs, old, neww) -> {
            if (neww) {
                System.out.println("update");
                this.update();
            }
        });
    }
    public SearchBar getSearchBar() {
        return searchBar;
    }
    public void update() {
        this.setVvalue(0);
        List<House> houses = searchViewModel.getHouses();
        container.getChildren().retainAll(textResult, totalText, pg);
        textResult.setText(searchViewModel.getSearchInformation());
        totalText.setText("Hiện có " + searchViewModel.getTotalHouses() + " bất động sản.");
        
        if (houses.isEmpty()) {
            container.getChildren().addAll(noResultImage, resetButton);
        }
        int totalPage = searchViewModel.getTotalHouses() / searchViewModel.getLimit();
        if (searchViewModel.getTotalHouses() % searchViewModel.getLimit() != 0)  totalPage++;
        pg.setPageCount(totalPage);
        pg.setCurrentPageIndex(searchViewModel.getOffset() / searchViewModel.getLimit());
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                try {
                    for (int i = 0; i < searchViewModel.getLimit(); i++) {
                        if (i > houses.size() - 1) {
                            return null;
                        } else {
                            final int j = i;
                            listHousesContainer.get(i).update(houses.get(i));
                            Platform.runLater(() -> {
                                // container.getChildren().add(listHousesContainer.get(j));
                                container.getChildren().add(j + 2, listHousesContainer.get(j));
                            });
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return null;
            }
            
        };
        // task.setOnSucceeded(e -> {
        //     container.getChildren().add(pg);
        // });
        MultiThread.execute(task);
        System.out.println("update succesfully");
    }
    public SearchViewModel getSearchViewModel() {
        return searchViewModel;
    }
    @Override
    public void update(boolean isLogged) {
        for (HouseOverview i : listHousesContainer) i.update(UserControl.getInstance().hasLogged());
    }
    
}
