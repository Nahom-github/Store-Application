import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ElectronicStoreView extends Pane {
    private ListView<Product> stockList, popList;
    private ListView<String> cartList;
    private TextField sales, revenue, perSale;
    private ButtonPane buttonPane;
    private ConcurrentHashMap<Product, Integer> cart;
    private Label cartLabel;
    private float currentCartTotal;
    private float soldItems;
    private int salesInt;


    public ListView<String> getCartList() {
                return cartList;
        }

        public ButtonPane getButtonPane() {
                return buttonPane;
        }

        public ListView<Product> getPopList() {
                return popList;
        }

        public ListView<Product> getStockList() {
                return stockList;
        }

        public TextField getPerSale() {
                return perSale;
        }

        public TextField getRevenue() {
                return revenue;
        }

        public TextField getSales() {
                return sales;
        }

        public void reset(ElectronicStore model){ // resets the store when the reset button is clicked
        currentCartTotal = 0;
        soldItems = 0;
        salesInt = 0;
        sales.setText("0");
        revenue.setText("0.00");
        perSale.setText("N/A");
        cartLabel.setText("Current Cart($0.00):");
        cartList.getItems().clear();
        cart.clear();
        start(model);
        }

        public void start(ElectronicStore model){ // starts the application with the correct info
            Product[] products = model.getStock();
            ArrayList<Product> finalprod = nonNullProductList(null,  model,false);
            Product[] popularstart = new Product[3];
            for (int i = 0; i < 3; i++) {
                popularstart[i] = products[i];
            }
            getButtonPane().getDeleteButton().disableProperty().bind(Bindings.isEmpty(getCartList().getSelectionModel().getSelectedItems()));
            getButtonPane().getAddButton().disableProperty().bind(Bindings.isEmpty(getStockList().getSelectionModel().getSelectedItems()));
            getButtonPane().getCompleteButton().setDisable(true);

          stockList.setItems(FXCollections.observableArrayList(finalprod));
          popList.setItems(FXCollections.observableArrayList(popularstart));
    }

    public void completePurchase(ElectronicStore model){ // this runs when the complete purchase button is clicked
        buttonPane.getCompleteButton().setDisable(true);
        Product[] poularItems = new Product[3];
        soldItems += currentCartTotal;
        revenue.setText(String.format("%.2f",soldItems));
        salesInt++;
        sales.setText(String.valueOf(salesInt));
        perSale.setText(String.format("%.2f",soldItems/salesInt));
        currentCartTotal = 0;
        cartLabel.setText("Current Cart($"+currentCartTotal+")");
        cartList.getItems().clear();
        cart.clear();
        ArrayList<Product> finalprod = nonNullProductList(null,model,true);
        Collections.sort(finalprod, new Comparator<Product>() { //sorts the item in finalprod based on how much is sold from greatest to lowest
            @Override
            public int compare(Product o1, Product o2) {
                return Integer.compare(o2.getSoldQuantity(), o1.getSoldQuantity());
            }
        });
        for (int i = 0; i < 3; i++) { // fills array with the 3 most popular items
            poularItems[i] = finalprod.get(i);
        }
        popList.setItems(FXCollections.observableArrayList(poularItems));
    }

    public void remove(ElectronicStore model, String p){ // this runs when the remove button is clicked
        for (Product key : cart.keySet()) { // iterates through the hashmap
            Integer value = cart.get(key);
            String compare = value + " X " + key;
            if (compare.equals(p)) { // once it reaches the specified item in the cart
                currentCartTotal = (float) (currentCartTotal - key.getPrice()); // removes the amount from the cart total
                key.returnUnits();
                if (value == 1) {
                    cart.remove(key); // removes the item from the cart if its the last
                } else {
                    cart.put(key, cart.get(key) - 1); // lowers the amount of that item by 1
                }
            }
        }
        cartList.getItems().clear();
        ArrayList<String> cartString = updateCart();
        ArrayList<Product> finalprod = nonNullProductList(null,model,false);
        updateListview(finalprod,cartString); // updates the view
    }

    public void add(ElectronicStore model, Product p) { // this runs when the add button is clicked
        currentCartTotal += p.getPrice();
        ArrayList<Product> finalprod = nonNullProductList(null,model, false);
        if(!cart.containsKey(p)) cart.put(p, 1); // if the cart does not have this item, add this item to the cart
        else cart.put(p, cart.get(p) + 1); // if it is already in the cart add one more of it to the cart
        ArrayList<String> cartString = updateCart();
        updateListview(finalprod,cartString); // updates the view
    }

    public void update(ArrayList<Product> arrayProduct, ElectronicStore model){ // this method just fills up the cart list and stock list with the correct information and displays it
        ArrayList<Product> finalprod = nonNullProductList(arrayProduct,model, false);
        ArrayList<String> cartString = updateCart();
        updateListview(finalprod,cartString);
    }

    public ArrayList<String> updateCart(){ // this method is used to fill the cart info into an arraylist that can be used to display on the listview
        ArrayList<String> cartString = new ArrayList<>();
        for(Map.Entry<Product, Integer> x: cart.entrySet()){
            Product key = x.getKey();
            Integer value = x.getValue();
            cartString.add(value+" X " +key.toString());
        }
        return cartString;
    }

    public void updateListview(ArrayList<Product> finalprod, ArrayList<String> cartString){ // updates items in list view
        stockList.setItems(FXCollections.observableArrayList(finalprod));
        cartList.setItems(FXCollections.observableArrayList(cartString));
        cartLabel.setText("Current Cart($"+currentCartTotal+")");
    }

    public ArrayList<Product> nonNullProductList(ArrayList<Product> arrayProduct, ElectronicStore model, boolean complete){ // makes an arraylist of the stock with no null entries
        ArrayList<Product> finalprod = new ArrayList<>();
        if(arrayProduct != null){ // this one is used when there is one item left in stock and it must be removed from the stock list
            for (Product x : arrayProduct) {
                if (x != null && x.getStockQuantity() != 0) finalprod.add(x);
            }
            return finalprod;
        }
        else if(!complete ){ // this one is used when you just want the stock with no null entries and all of them have remaining stock
            Product[] products = model.getStock();
            for (Product x : products) {
                if (x != null && x.getStockQuantity() != 0) finalprod.add(x);
            }
            return finalprod;
        }
        else{ // this one is used when completing a purchase
            Product[] products = model.getStock();
            for (Product x : products) {
                if (x != null) finalprod.add(x);
            }
            return finalprod;
        }
    }

    public ElectronicStoreView() {
        currentCartTotal = 0.00f;
        salesInt = 0;
        soldItems = 0;
        cart = new ConcurrentHashMap<>();
        Label summary = new Label("Store Summary:");
        summary.relocate(40, 40);
        Label stock = new Label("Store Stock:");
        stock.relocate(300, 40);
        cartLabel =  new Label("Current Cart($0.00):");
        cartLabel.relocate(600, 40);
        Label nSales = new Label("# Sales:");
        nSales.relocate(40, 70);
        Label trevenue = new Label(" Revenue:");
        trevenue.relocate(25, 100);
        Label pSale = new Label("  $ / Sale:");
        pSale.relocate(25, 130);
        Label popularItem = new Label("Most popular Items:");
        popularItem.relocate(35, 165);
        stockList = new ListView<Product>();
        stockList.relocate(200, 60);
        stockList.setPrefSize(275, 285);
        cartList = new ListView<String>();
        cartList.relocate(490, 60);
        cartList.setPrefSize(275, 285);
        popList = new ListView<Product>();
        popList.setPrefSize(172,156);
        popList.relocate(15,190);
        sales = new TextField("0");
        sales.setPrefSize(96,26);
        sales.relocate(85,65);
        revenue = new TextField("0.00");
        revenue.relocate(85,95);
        revenue.setPrefSize(96,26);
        perSale = new TextField("N/A");
        perSale.relocate(85,125);
        perSale.setPrefSize(96,26);
        buttonPane = new ButtonPane();
        buttonPane.relocate(26,347);

        getChildren().addAll(summary, stock, cartLabel, nSales, revenue, pSale, popularItem, stockList, cartList,sales,trevenue,perSale,popList,buttonPane);
        setPrefSize(800, 400);

    }
}
