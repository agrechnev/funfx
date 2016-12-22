package agrechnev.fxcalc;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by Oleksiy Grechnyev on 11/27/2016.
 */
public class CalculatorImpl implements Calculator {

    // MathContext of the calculator
    // Used to round up BigDecimal to a finite precision
    private final MathContext mathContext = MathContext.DECIMAL128;

    // The current value (the screen)
    private BigDecimal value = BigDecimal.ZERO;
    // Does this number have a dot somewhere ?
    private boolean hasDot = false;

    // Are we starting a new number?
    // E.g. after clean or after numerical operation
    // If false, then we are in the middle of entering a number
    private boolean startingNew = true;

    // Is there an op in progress ???
    boolean opInProgress = false;
    private BigDecimal lastValue; // Last value saved for arithmetic operations
    private char pendingOp; // The pending operation, use = or next op to complete


    @Override
    public String getScreen() {
        return value.toString();
    }

    /**
     * A numeral 0-9 is pressed on the calculator
     *
     * @param i
     */
    @Override
    public void pressNumeral(int i) {

        if (startingNew) {
            // Starting a new number
            value = new BigDecimal(i);
            startingNew = false;
        } else if (hasDot) {
            // Input after dot
            // Example: value = 3.14  , pressed 1,
            // newNum = 0.001, value+newNum = 3.141
            BigDecimal newNum = new BigDecimal(i).movePointLeft(value.scale() + 1);
            value = value.add(newNum, mathContext);

        } else {
            // Input with no dot
            value = value.movePointRight(1).add(new BigDecimal(i));
        }
    }

    /**
     * An operator +-* /= is pressed
     *
     * @param op
     */
    @Override
    public void pressOp(char op) {
        switch (op) {
            case '+':
            case '-':
            case '*':
            case '/':

                // Check if there is another op in progress
                // We finish it first and get the result
                if (opInProgress) executeOp();

                // Save the op and value
                opInProgress = true;
                pendingOp = op;
                lastValue = value;
                startNewInput();
                break;

            case '=':
                // Run the pending op if any
                if (opInProgress) executeOp();
                opInProgress = false;
        }
    }

    /**
     * All other calculator buttons except numerals and arithmetic operators
     *
     * @param miscButton
     */
    @Override
    public void pressMisc(MiscButton miscButton) {
        switch (miscButton) {
            case CLEAR:  // Clear screen
                value = BigDecimal.ZERO;
                opInProgress = false;
                startNewInput();
                break;

            case PLUS_MINUS:   // Change sign and nothing else
                value = value.negate();
                break;

            case DOT:  // Place a decimal dot
                hasDot=true;

                // Dot at a new input gives 0.
                if (startingNew) {
                    value=BigDecimal.ZERO;
                    startingNew=false;
                }
                break;
        }
    }

    private void executeOp() {
        // Nothing to do if no operation
        if (!opInProgress) return;

        // Finish the pending operation using our finite precision mathContext
        switch (pendingOp) {
            case '+':
                value = lastValue.add(value, mathContext);
                break;
            case '-':
                value = lastValue.subtract(value, mathContext);
                break;
            case '*':
                value = lastValue.multiply(value, mathContext);
                break;
            case '/':
                value = lastValue.divide(value, mathContext);
                break;
        }

        // Always ready for a new input after operation
        startNewInput();
    }

    /**
     * Start a new input
     */
    private void startNewInput() {
        startingNew = true;
        hasDot = false;
    }
}
