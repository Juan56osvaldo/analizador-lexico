# Analizador Léxico

Programa en Java que implementa un **analizador léxico** para un lenguaje de programación simplificado. Identifica palabras reservadas, identificadores, números, operadores aritméticos/relacionales/de asignación, símbolos de agrupación, delimitadores y detecta errores léxicos.

## ¿Qué hace?

El analizador recorre un código fuente carácter por carácter y clasifica cada fragmento (lexema) en uno de los siguientes tipos de token:

| Tipo de token        | Ejemplos                              |
|-----------------------|----------------------------------------|
| Palabra reservada      | `if`, `else`, `while`, `for`, `int`, `float`, `return`, `void`, `main`, `print` |
| Identificador           | `contador`, `promedio`, `i`           |
| Número                  | `10`, `8.5`                            |
| Operador aritmético      | `+` `-` `*` `/`                       |
| Operador relacional      | `==` `!=` `<` `>` `<=` `>=`           |
| Operador de asignación   | `=`                                    |
| Símbolo de agrupación    | `(` `)` `{` `}`                       |
| Delimitador              | `;` `,`                                |
| Error léxico             | Cualquier carácter no reconocido (ej. `@`, `#`) |

## Cómo ejecutarlo

Requiere Java 11 o superior.

```bash
java AnalizadorLexico.java
```

El programa corre automáticamente 3 pruebas:

1. **Código correcto** — sin errores léxicos.
2. **Código con un error léxico** — incluye un carácter inválido (`@`).
3. **Código con diversos tipos de tokens** — combina todas las categorías.

Para cada prueba imprime en consola el código fuente analizado y una tabla con cada lexema junto a su token correspondiente, además del total de tokens y errores léxicos encontrados.

## Estructura del proyecto

```
analizador-lexico/
├── AnalizadorLexico.java   # Código fuente del analizador
└── README.md               # Este archivo
```

## Autor

Juan Osvaldo Lopez Meneses Estudiante de Sistemas Computacionales
