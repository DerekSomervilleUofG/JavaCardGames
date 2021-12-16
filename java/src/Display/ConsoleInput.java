package Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleInput extends Input{
    private Scanner userInput = new Scanner(System.in);

    public String getString(){
        return userInput.nextLine();
    }

}