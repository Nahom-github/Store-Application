import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ElectronicStoreApp extends Application {
    ElectronicStore model;
    ElectronicStoreView view;

    public ElectronicStoreApp() {
       model = ElectronicStore.createStore();
        view = new ElectronicStoreView();
    }

    public void start(Stage primaryStage) {
        Pane aPane = new Pane();

        // Create the view
        ElectronicStoreView  view = new ElectronicStoreView();
        view.start(model);
        aPane.getChildren().add(view);

        view.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {// this tracks whenever the user mouse clicks anywhere on the application
            @Override
            public void handle(MouseEvent mouseEvent) {
                view.getButtonPane().getDeleteButton().disableProperty().bind(Bindings.isEmpty(view.getCartList().getSelectionModel().getSelectedItems())); // disables the button based on if an item in the listview is selected or not
                view.getButtonPane().getAddButton().disableProperty().bind(Bindings.isEmpty(view.getStockList().getSelectionModel().getSelectedItems()));
            }
        });

        view.getButtonPane().getResetButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model = ElectronicStore.createStore();
                view.reset(model);
                view.getStockList().getSelectionModel().select(-1); // deselects any item in the listview
                view.getCartList().getSelectionModel().select(-1);

            }
        });

        view.getStockList().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                view.getCartList().getSelectionModel().select(-1);
            }
        });

        view.getCartList().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                view.getStockList().getSelectionModel().select(-1);
            }
        });

        view.getButtonPane().getCompleteButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                view.completePurchase(model);
                view.getStockList().getSelectionModel().select(-1);
                view.getCartList().getSelectionModel().select(-1);
            }
        });

        view.getButtonPane().getAddButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<Product> finalprod = view.nonNullProductList(null, model,false);
                view.getButtonPane().getCompleteButton().setDisable(false);
                int index = view.getStockList().getSelectionModel().getSelectedIndex();
                view.getStockList().getItems().get(index).sellUnits(1);
                view.add(model, view.getStockList().getItems().get(index));
                if(view.getStockList().getItems().get(index).getStockQuantity() == 0){ //removes item if there is no stock available
                    finalprod.remove(view.getStockList().getItems().get(index));
                    view.update(finalprod,model);
                }
                view.getCartList().getSelectionModel().select(-1);
            }
        } );

        view.getButtonPane().getDeleteButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int index = view.getCartList().getSelectionModel().getSelectedIndex();
                view.remove(model, view.getCartList().getItems().get(index));
                view.getStockList().getSelectionModel().select(-1);
            }
        } );

        view.getPopList().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                view.getStockList().getSelectionModel().select(-1);
                view.getCartList().getSelectionModel().select(-1);
            }
        });

        view.getSales().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                view.getCartList().getSelectionModel().select(-1);
                view.getStockList().getSelectionModel().select(-1);
            }
        });

        view.getPerSale().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                view.getCartList().getSelectionModel().select(-1);
                view.getStockList().getSelectionModel().select(-1);
            }
        });

        view.getRevenue().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                view.getCartList().getSelectionModel().select(-1);
                view.getStockList().getSelectionModel().select(-1);
            }
        });

        primaryStage.setTitle("Electronic Store Application - "+ model.getName());
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(aPane));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
