import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class Main {
    private static final Map<String, Integer> symbolTable = new HashMap<>();

    public static void main(final String[] args) {
        if (args.length != 1) {
            System.out.println("You must specify a file. Usage: java -jar dist/Part1.jar FilePath");
            return;
        }
        try {
            java.io.FileInputStream stream = new java.io.FileInputStream(args[0]);
            java.io.Reader reader = new java.io.InputStreamReader(stream);
            initScan(reader);
            sortVariables();
        }
        catch (java.io.FileNotFoundException e) {
            System.out.println("File not found : \""+args[0]+"\"");
        }
        catch (java.io.IOException e) {
            System.err.println("IO error scanning file \""+args[0]+"\"");
        }
        catch (EmptyStackException e) {
            System.err.println("Trailing END with no matching BEGIN, WHILE or IF was detected.");
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Initiates lexical scanning and prints the relevant tokens
     * @param reader InputStream reader
     * @throws IOException On error reading or accessing the file
     */
    private static void initScan(Reader reader) throws Exception {
        LexicalAnalizer scanner = new LexicalAnalizer(reader);
        Symbol symbol;
        while(!(symbol = scanner.yylex()).getType().equals(LexicalUnit.EOS)){
            System.out.println(symbol.toString());
            if (symbol.getType().equals(LexicalUnit.VARNAME)) {
                String varName = symbol.getValue().toString();
                if (!symbolTable.containsKey(varName)) {
                    symbolTable.put(varName, symbol.getLine());
                }
            }
        }
    }

    /**
     * Sorts variables by alphabetical order and prints the contents of the symbol table
     */
    private static void sortVariables() {
        List<String> varNames = new ArrayList<>(symbolTable.keySet());
        Collections.sort(varNames);
        System.out.println("\nVariables");
        for (String key: varNames) {
            System.out.println(key + "  " + symbolTable.get(key));
        }
    }
}