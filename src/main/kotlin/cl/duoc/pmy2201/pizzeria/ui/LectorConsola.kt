package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.util.CORREO_POR_DEFECTO
import cl.duoc.pmy2201.pizzeria.util.TELEFONO_POR_DEFECTO
import cl.duoc.pmy2201.pizzeria.util.esCorreoValido
import cl.duoc.pmy2201.pizzeria.util.normalizarTelefono

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
     * Pide un texto opcional y lo devuelve sin espacios al inicio ni al final.
     * Si se presiona Enter sin escribir nada, o si la entrada termina, retorna null.
     */
    fun leerTextoOpcional(mensaje: String): String? {
        print(mensaje)
        val texto: String = readlnOrNull()?.trim() ?: return null
        return texto.ifEmpty { null }
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
     * Tras [MAXIMO_INTENTOS] intentos inválidos, o si la entrada termina, retorna [TELEFONO_POR_DEFECTO].
     */
    fun leerTelefono(mensaje: String): String {
        for (intento: Int in 1..MAXIMO_INTENTOS) {
            print(mensaje)
            val entrada: String? = readlnOrNull()
            if (entrada == null) {
                println("No se recibió teléfono. Se registrará como $TELEFONO_POR_DEFECTO.")
                return TELEFONO_POR_DEFECTO
            }

            val telefono: String? = normalizarTelefono(entrada.trim())
            if (telefono != null) return telefono

            val intentosRestantes: Int = MAXIMO_INTENTOS - intento
            if (intentosRestantes > 0) {
                println("Teléfono inválido. Ingrese 9 dígitos, con o sin +56. Intentos restantes: $intentosRestantes.")
            }
        }
        println("Se agotaron los intentos. El teléfono se registrará como $TELEFONO_POR_DEFECTO.")
        return TELEFONO_POR_DEFECTO
    }

    /**
     * Pide un correo opcional y lo devuelve sin espacios y en minúsculas.
     * Si se presiona Enter sin escribir nada, o si la entrada termina, retorna null (sin correo).
     * Tras [MAXIMO_INTENTOS] intentos inválidos, retorna [CORREO_POR_DEFECTO].
     */
    fun leerCorreo(mensaje: String): String? {
        for (intento: Int in 1..MAXIMO_INTENTOS) {
            print(mensaje)
            val texto: String = readlnOrNull()?.trim() ?: return null
            if (texto.isEmpty()) return null
            if (esCorreoValido(texto)) return texto.lowercase()

            val intentosRestantes: Int = MAXIMO_INTENTOS - intento
            if (intentosRestantes > 0) {
                println("Correo inválido. Use el formato usuario@dominio.ext o presione Enter para omitirlo. Intentos restantes: $intentosRestantes.")
            }
        }
        println("Se agotaron los intentos. El correo se registrará como $CORREO_POR_DEFECTO.")
        return CORREO_POR_DEFECTO
    }

    companion object {
        const val MAXIMO_INTENTOS: Int = 3
    }
}
