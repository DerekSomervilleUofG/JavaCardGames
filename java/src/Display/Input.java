package Display;

public abstract class Input {

    public abstract String getString();

    public int getInteger(){
        int result;
        result = -1;
        do {
            try {
                result = Integer.parseInt(getString());
            } catch (NumberFormatException exception) {
                // Display.Output expected NumberFormatException.
                System.out.println("Please enter a integer");
            }
        } while (result < 0);

        return result;
    }
}
