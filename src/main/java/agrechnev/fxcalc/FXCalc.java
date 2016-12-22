package agrechnev.fxcalc;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.stream.IntStream;

/**
 * Created by Oleksiy Grechnyev on 11/26/2016.
 */
public class FXCalc extends Application {
    // The calculator object
    private Calculator calculator = new CalculatorImpl();

    // The screen as JavaFX label
    private Label screen;


    @Override
    public void start(Stage stage) throws Exception {

        // We use a grid pane
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(20));

        // Calculator buttons
        // 0-9 numerals
        Button btnNum[] = new Button[10];

        // More lambda-friendly than the for loop, i=0,..,9
        // Worse performace though
        IntStream.range(0, 10).forEach(i -> {
            // Create a 0-9 button
            btnNum[i] = new Button(Integer.toString(i));
            btnNum[i].setOnAction(ae -> {
                calculator.pressNumeral(i); // Here I can use i, hurray!
                updateScreen();
            });
        });

        // Operator buttons
        Button btnAdd = createOpButton('+');
        Button btnSub = createOpButton('-');
        Button btnMul = createOpButton('*');
        Button btnDiv = createOpButton('/');
        Button btnEqual = createOpButton('=');

        // Misc buttons
        Button btnClear = createMiscButton(MiscButton.CLEAR, "C");
        Button btnPM = createMiscButton(MiscButton.PLUS_MINUS, "\u00b1");
        Button btnDot = createMiscButton(MiscButton.DOT, ".");

        // The screen
        screen = new Label();
        updateScreen();

        // Add the components at correct positions
        root.add(screen, 0, 0, 5, 1);

        root.add(btnNum[7], 0, 2);
        root.add(btnNum[8], 1, 2);
        root.add(btnNum[9], 2, 2);
        root.add(btnNum[4], 0, 3);
        root.add(btnNum[5], 1, 3);
        root.add(btnNum[6], 2, 3);
        root.add(btnNum[1], 0, 4);
        root.add(btnNum[2], 1, 4);
        root.add(btnNum[3], 2, 4);
        root.add(btnNum[0], 0, 5);

        root.add(btnAdd, 3, 5);
        root.add(btnSub, 3, 4);
        root.add(btnMul, 3, 3);
        root.add(btnDiv, 3, 2);
        root.add(btnEqual, 4, 5);

        root.add(btnClear, 4, 1);
        root.add(btnPM, 1 , 5);
        root.add(btnDot, 2 , 5);

        Scene scene = new Scene(root);
        stage.setTitle("JavaFX calculator");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Create an operator button for operators +-* /=
     *
     * @param op
     * @return
     */
    private Button createOpButton(char op) {
        Button button = new Button(Character.toString(op));

        button.setOnAction(ae -> {
            calculator.pressOp(op);
            updateScreen();
        });

        return button;
    }

    /**
     * Create a misc button
     * @param miscButton
     * @param name
     * @return
     */
    private Button createMiscButton(MiscButton miscButton, String name) {
        Button button = new Button(name);

        button.setOnAction(ae -> {
            calculator.pressMisc(miscButton);
            updateScreen();
        });

        return button;
    }

    private void updateScreen() {
        screen.setText(calculator.getScreen());
    }
}
