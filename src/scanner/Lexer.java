package scanner;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Lexer {

    private Scanner input;
    private final boolean debug;

    public Lexer(String path, boolean debug) {
        this.debug = debug;

        if (debug) {
            System.err.println("Lexer: initialising, path = " + path);
        }

        try {
            input = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            System.err.println("Error: File " + path + " not found");
            System.exit(1);
        }
    }

    private Token item;

    public Token getItem() {
        return item;
    }

    public boolean endOfInput() {
        return !input.hasNext();
    }

    public void next() {
        String word = "EOF";
        if (!endOfInput()) {
            word = input.next();
        }
        item = new Token(word);
        if (debug) {
            System.err.println("Lexer: item = " + word);
        }
    }
}
