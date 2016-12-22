package agrechnev.fxcalc;

/**
 * Created by Oleksiy Grechnyev on 11/27/2016.
 * The calculator interface
 */
public interface Calculator {
    // Get the screen value
    String getScreen();

    // Button presses
    void pressNumeral(int i); // Numerals  : 0-9
    void pressOp(char op); // Operator : +-*/=
    void pressMisc(MiscButton miscButton); // All other buttons

}
