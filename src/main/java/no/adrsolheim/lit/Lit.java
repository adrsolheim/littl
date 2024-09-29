package no.adrsolheim.lit;

import java.io.*;
import java.util.List;

import static java.lang.StringTemplate.STR;

public class Lit {

    private static final int COMMAND_INCORRECTLY_USED = 64;
    private static final int INPUT_DATA_INCORRECT = 65;
    private static final int NORMAL_EXIT = 0;
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: littl [sourcefile]");
            System.exit(COMMAND_INCORRECTLY_USED); // command used incorrectly
        }
        if (args.length == 1) {
            runFile(args[1]);
        } else {
            repl();
        }
        int exitCode = hadError ? INPUT_DATA_INCORRECT : NORMAL_EXIT;
        System.exit(exitCode);

    }

    public static void run(String sourceCode) {
        Lexer lexer = new Lexer(sourceCode);
        List<Token> tokens = lexer.scanTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    public static void runFile(String filePath) throws FileNotFoundException  {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder sourceCodeBuilder = new StringBuilder();
        reader.lines()
                .map(line -> line + "\\n")
                .forEach(sourceCodeBuilder::append);

        String sourceCode = sourceCodeBuilder.toString();
        run(sourceCode);
        if (hadError) System.exit(INPUT_DATA_INCORRECT);
    }

    public static void repl() throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("> ");
            String line = inputReader.readLine();
            if (line == null) break; // CTRL-D

            run(line);
        }
    }

    public static void parserError(int line, Token trigger) {
        String message = trigger.tokenType == TokenType.EOF ? "[ParserError] at end of source file" : String.format("[ParserError] at '%s'", trigger.lexeme);
        reportError(line, "", message);
    }

    public static void lexerError(int line, String message) {
        reportError(line, "", "[LexerError] " + message);
    }

    private static void reportError(int line, String location, String message) {
        System.err.println(STR."[line \{line}] Error \{location}: \{message}");
        hadError = true;
    }
}
