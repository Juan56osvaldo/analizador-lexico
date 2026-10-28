import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analizador Lexico
 * -------------------
 * Reconoce los siguientes tipos de token de un lenguaje simplificado:
 *   - Palabras reservadas   (if, else, while, for, int, float, return, void, main, print)
 *   - Identificadores       (nombres de variables/funciones)
 *   - Numeros                (enteros y decimales)
 *   - Operadores aritmeticos (+  -  *  /)
 *   - Operadores relacionales (==  !=  <  >  <=  >=)
 *   - Operadores de asignacion (=)
 *   - Simbolos de agrupacion  ( ) { }
 *   - Delimitadores           ; ,
 *
 * Cualquier caracter que no coincida con ninguna de las reglas anteriores
 * se reporta como ERROR LEXICO.
 *
 * Autor: Juan Osvaldo Lopez Meneses
 */
public class AnalizadorLexico {

    // Palabras reservadas del lenguaje simplificado
    private static final List<String> PALABRAS_RESERVADAS = Arrays.asList(
            "if", "else", "while", "for", "int", "float",
            "return", "void", "main", "print"
    );

    // Patrones (expresiones regulares) para cada tipo de token.
    // El orden importa: se evaluan de arriba hacia abajo.
    private static final Pattern PATRON_IDENTIFICADOR = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    private static final Pattern PATRON_NUMERO = Pattern.compile("\\d+(\\.\\d+)?");
    private static final Pattern PATRON_OP_RELACIONAL = Pattern.compile("==|!=|<=|>=|<|>");
    private static final Pattern PATRON_OP_ARITMETICO = Pattern.compile("[+\\-*/]");
    private static final Pattern PATRON_OP_ASIGNACION = Pattern.compile("=");
    private static final Pattern PATRON_AGRUPACION = Pattern.compile("[(){}]");
    private static final Pattern PATRON_DELIMITADOR = Pattern.compile("[;,]");
    private static final Pattern PATRON_ESPACIO = Pattern.compile("[ \\t\\r\\n]+");

    /**
     * Representa un token reconocido por el analizador.
     */
    static class Token {
        String lexema;
        String tipo;

        Token(String lexema, String tipo) {
            this.lexema = lexema;
            this.tipo = tipo;
        }
    }

    /**
     * Recorre el codigo fuente caracter por caracter (apoyandose en
     * expresiones regulares) y construye la lista de tokens.
     *
     * @param codigo codigo fuente a analizar
     * @return lista de tokens encontrados, incluidos los errores lexicos
     */
    public static List<Token> analizar(String codigo) {
        List<Token> tokens = new ArrayList<>();
        int pos = 0;

        while (pos < codigo.length()) {
            String resto = codigo.substring(pos);
            Matcher m;

            // 1) Ignorar espacios en blanco
            m = PATRON_ESPACIO.matcher(resto);
            if (m.lookingAt()) {
                pos += m.end();
                continue;
            }

            // 2) Identificadores / palabras reservadas
            m = PATRON_IDENTIFICADOR.matcher(resto);
            if (m.lookingAt()) {
                String lexema = m.group();
                String tipo = PALABRAS_RESERVADAS.contains(lexema)
                        ? "PALABRA_RESERVADA"
                        : "IDENTIFICADOR";
                tokens.add(new Token(lexema, tipo));
                pos += lexema.length();
                continue;
            }

            // 3) Numeros (enteros y decimales)
            m = PATRON_NUMERO.matcher(resto);
            if (m.lookingAt()) {
                String lexema = m.group();
                tokens.add(new Token(lexema, "NUMERO"));
                pos += lexema.length();
                continue;
            }

            // 4) Operadores relacionales (deben ir antes que '=' simple)
            m = PATRON_OP_RELACIONAL.matcher(resto);
            if (m.lookingAt()) {
                String lexema = m.group();
                tokens.add(new Token(lexema, "OP_RELACIONAL"));
                pos += lexema.length();
                continue;
            }

            // 5) Operador de asignacion
            m = PATRON_OP_ASIGNACION.matcher(resto);
            if (m.lookingAt()) {
                String lexema = m.group();
                tokens.add(new Token(lexema, "OP_ASIGNACION"));
                pos += lexema.length();
                continue;
            }

            // 6) Operadores aritmeticos
            m = PATRON_OP_ARITMETICO.matcher(resto);
            if (m.lookingAt()) {
                String lexema = m.group();
                tokens.add(new Token(lexema, "OP_ARITMETICO"));
                pos += lexema.length();
                continue;
            }

            // 7) Simbolos de agrupacion
            m = PATRON_AGRUPACION.matcher(resto);
            if (m.lookingAt()) {
                String lexema = m.group();
                tokens.add(new Token(lexema, "AGRUPACION"));
                pos += lexema.length();
                continue;
            }

            // 8) Delimitadores
            m = PATRON_DELIMITADOR.matcher(resto);
            if (m.lookingAt()) {
                String lexema = m.group();
                tokens.add(new Token(lexema, "DELIMITADOR"));
                pos += lexema.length();
                continue;
            }

            // 9) Si nada coincidio, es un error lexico (se reporta un solo caracter)
            String caracter = String.valueOf(codigo.charAt(pos));
            tokens.add(new Token(caracter, "ERROR_LEXICO"));
            pos += 1;
        }

        return tokens;
    }

    /**
     * Imprime la tabla de resultados de un analisis en consola.
     */
    private static void imprimirResultado(String titulo, String codigo) {
        System.out.println("============================================================");
        System.out.println(titulo);
        System.out.println("============================================================");
        System.out.println("Codigo fuente:");
        System.out.println(codigo);
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-20s | %-20s%n", "LEXEMA", "TOKEN");
        System.out.println("------------------------------------------------------------");

        List<Token> tokens = analizar(codigo);
        int errores = 0;
        for (Token t : tokens) {
            System.out.printf("%-20s | %-20s%n", t.lexema, t.tipo);
            if (t.tipo.equals("ERROR_LEXICO")) {
                errores++;
            }
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Total de tokens: " + tokens.size());
        System.out.println("Errores lexicos encontrados: " + errores);
        System.out.println();
    }

    public static void main(String[] args) {

        // -----------------------------------------------------------
        // Prueba 1: codigo correcto (sin errores lexicos)
        // -----------------------------------------------------------
        String prueba1 =
                "int contador = 0;\n" +
                "if (contador < 10) {\n" +
                "    contador = contador + 1;\n" +
                "}\n";
        imprimirResultado("PRUEBA 1: Codigo correcto", prueba1);

        // -----------------------------------------------------------
        // Prueba 2: codigo con un error lexico (caracter '@' no valido)
        // -----------------------------------------------------------
        String prueba2 =
                "int @total = 5;\n" +
                "print(total);\n";
        imprimirResultado("PRUEBA 2: Codigo con error lexico", prueba2);

        // -----------------------------------------------------------
        // Prueba 3: codigo con diversos tipos de tokens
        // -----------------------------------------------------------
        String prueba3 =
                "float promedio = 8.5;\n" +
                "for (i = 0; i <= 10; i = i + 1) {\n" +
                "    print(promedio);\n" +
                "}\n";
        imprimirResultado("PRUEBA 3: Diversos tipos de tokens", prueba3);
    }
}
