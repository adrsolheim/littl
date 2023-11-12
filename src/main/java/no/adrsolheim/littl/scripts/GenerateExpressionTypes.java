package no.adrsolheim.littl.scripts;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateExpressionTypes {
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Specify a single output directory. Defaults to working directory");
        }
        String outputDir = args.length == 1 ? args[0] : ".";
        GenerateExpressionTypes.createExpressionTypes(outputDir, "Expr", Arrays.asList(
                "Literal  : Object value",
                "Binary   : Expr left, Token operator, Expr right",
                "Unary    : Token operator, Expr expr",
                "Grouping : Expr expr"
        ));
    }

    private static void createExpressionTypes(String outputDir, String baseName, List<String> expressionTypes) throws IOException {
        String fileName = STR."\{outputDir}/\{baseName}.java".replace("//","/");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(STR."""
                    package no.adrsolheim.littl;

                    public abstract class \{baseName} {

                        abstract <R> R accept(Visitor<R> visitor);

                    \{expressionTypes.stream().map(exprType -> definitionToClass(exprType, baseName)).collect(Collectors.joining("\n"))}
                    \{visitorInterface(baseName, expressionTypes, 4)}
                    }
                    """);
        }

    }

    private static String definitionToClass(String typeDefinition, String baseName) {
        String[] classParams = typeDefinition.split(":");
        String className = classParams[0].strip();
        List<String> params = Arrays.stream(classParams[1].split(","))
                .map(String::strip)
                .collect(Collectors.toList());

        return STR."""
                    public static class \{className} extends \{baseName} {

                        \{memberVariables(params, 8)}

                        public \{className} (\{String.join(", ", params)}) {
                            \{paramAssignments(params, 12)}
                        }

                        @Override
                        public <R> R accept(Visitor<R> visitor) {
                            return visitor.visit(this);
                        }
                    }
                """;
    }

    private static String memberVariables(List<String> params, int indent) {
        StringBuilder sb = new StringBuilder();
        String newline = "";
        for (String param : params) {
            sb.append(STR."\{newline}final \{param};");
            newline = "\n" + pad(indent);
        }
        return sb.toString();
    }

    private static String paramAssignments(List<String> params, int indent) {
        List<String> varNames = params.stream().map(p -> p.split(" ")[1]).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        String newline = "";
        for (String varName : varNames) {
            sb.append(STR."\{newline}this.\{varName} = \{varName};");
            newline = "\n" + pad(indent);
        }
        return sb.toString();
    }

    private static String visitorInterface(String baseName, List<String> expressionTypes, int indent) {
        List<String> expressions = expressionTypes.stream()
                .map(e -> e.split(":")[0].strip())
                .collect(Collectors.toList());
        String newline = "\n" + pad(indent+4);
        String ws = pad(indent); // whitespace indentation

        return STR."""

                \{ws}interface Visitor<R> {
                \{ws}    \{expressions.stream().map(expr -> String.format("<R> R visit(%s %s);", expr, baseName.toLowerCase())).collect(Collectors.joining(newline))}
                \{ws}}
                """;
    }

    private static String pad(int n) {
        char[] whitespaces = new char[n];
        for (int i = 0; i < n; i++) {
            whitespaces[i] = ' ';
        }
        return String.valueOf(whitespaces);
    }
}
