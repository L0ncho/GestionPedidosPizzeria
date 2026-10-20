package cl.duoc.pmy2201.pizzeria.ui

/**
 * Lee y valida datos ingresados por consola.
 */
class LectorConsola {

    /**
     * Pide un texto no vacío que cumpla [validacion], repitiendo hasta obtenerlo.
     * Si no cumple, muestra [mensajeError].
     * Si la entrada termina (readlnOrNull() devuelve null), retorna [valorPorDefecto].
     */
    fun leerTextoObligatorio(
        mensaje: String,
        valorPorDefecto: String,
        validacion: (String) -> Boolean = { _: String -> true },
        mensajeError: String = "El valor ingresado no es válido. Intente nuevamente."
    ): String {
        while (true) {
            print(mensaje)
            val entrada: String = readlnOrNull() ?: return valorPorDefecto
            val texto: String = entrada.trim()
            when {
                texto.isEmpty() -> println("El valor no puede estar vacío. Intente nuevamente.")
                !validacion(texto) -> println(mensajeError)
                else -> return texto
            }
        }
    }

    /**
     * Pide un número entero entre [minimo] y [maximo] (ambos incluidos), repitiendo hasta obtenerlo.
     * Si la entrada termina (readlnOrNull() devuelve null), retorna [minimo].
     */
    fun leerEnteroEnRango(mensaje: String, minimo: Int, maximo: Int): Int {
        while (true) {
            print(mensaje)
            val entrada: String = readlnOrNull() ?: return minimo
            val texto: String = entrada.trim()
            val numero: Int? = try {
                texto.toInt()
            } catch (excepcion: NumberFormatException) {
                null
            }
            when {
                texto.isEmpty() -> println("Debe ingresar un valor.")
                numero == null -> println("Debe ingresar un número entero. Intente nuevamente.")
                numero !in minimo..maximo -> println("El número debe estar entre $minimo y $maximo. Intente nuevamente.")
                else -> return numero
            }
        }
    }

    /**
     * Pide un teléfono chileno de 9 dígitos, con o sin prefijo +56 o 56, y lo devuelve como +56XXXXXXXXX.
     * Tras [MAXIMO_INTENTOS_TELEFONO] intentos inválidos, o si la entrada termina, retorna [TELEFONO_NO_INFORMADO].
     */
    fun leerTelefono(mensaje: String): String {
        for (intento: Int in 1..MAXIMO_INTENTOS_TELEFONO) {
            print(mensaje)
            val entrada: String? = readlnOrNull()
            if (entrada == null) {
                println("No se recibió teléfono. Se registrará como \"$TELEFONO_NO_INFORMADO\".")
                return TELEFONO_NO_INFORMADO
            }

            val sinEspacios: String = entrada.filterNot { caracter: Char -> caracter.isWhitespace() }
            val coincidencia: MatchResult? = FORMATO_TELEFONO.matchEntire(sinEspacios)
            if (coincidencia != null) return "$PREFIJO_CHILE${coincidencia.groupValues[1]}"

            val intentosRestantes: Int = MAXIMO_INTENTOS_TELEFONO - intento
            if (intentosRestantes > 0) {
                println("Teléfono inválido. Ingrese 9 dígitos, con o sin +56. Intentos restantes: $intentosRestantes.")
            }
        }
        println("Se agotaron los intentos. El teléfono se registrará como \"$TELEFONO_NO_INFORMADO\".")
        return TELEFONO_NO_INFORMADO
    }

    companion object {
        const val MAXIMO_INTENTOS_TELEFONO: Int = 3
        const val TELEFONO_NO_INFORMADO: String = "No informado"
        private const val PREFIJO_CHILE: String = "+56"
        private val FORMATO_TELEFONO: Regex = Regex("""^(?:\+?56)?(\d{9})$""")
    }
}
