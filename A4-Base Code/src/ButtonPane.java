import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ButtonPane extends Pane {
    private     Button  addButton, deleteButton, completeButton, resetButton;

    public Button getAddButton() { return addButton; }
    public Button getDeleteButton() { return deleteButton; }
    public Button getCompleteButton() { return completeButton; }

    public Button getResetButton() {
        return resetButton;
    }

    public ButtonPane() {
        Pane innerPane = new Pane();
        resetButton = new Button("Reset Store");
        resetButton.relocate(0,0);
        resetButton.setPrefSize(144,47);

        // Create the buttons
        addButton = new Button("Add to Cart");
        addButton.relocate(240, 0);
        addButton.setPrefSize(144,47);

        deleteButton = new Button("Remove from Cart");
        deleteButton.relocate(466, 0);
        deleteButton.setPrefSize(144,47);

        completeButton = new Button("Complete Sale");
        completeButton.relocate(611, 0);
        completeButton.setPrefSize(144,47);

        // Add all three buttons to the pane
        innerPane.getChildren().addAll(addButton, deleteButton, completeButton,resetButton);

        getChildren().addAll(innerPane);
    }
}
