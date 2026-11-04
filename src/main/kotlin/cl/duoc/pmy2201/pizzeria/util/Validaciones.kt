package cl.duoc.pmy2201.pizzeria.util

/** Teléfono que se usa cuando no se logra obtener uno válido. */
const val TELEFONO_POR_DEFECTO: String = "+56900000000"

/** Correo que se usa cuando no se logra obtener uno válido. */
const val CORREO_POR_DEFECTO: String = "sin.correo@example.com"

/** Rango permitido de pizzas, tanto por producto como para el total del pedido. */
val RANGO_CANTIDAD_PIZZAS: IntRange = 1..100

private const val PREFIJO_CHILE: String = "+56"
private val FORMATO_TELEFONO: Regex = Regex("""^(?:\+?56)?(\d{9})$""")
private val FORMATO_CORREO: Regex = Regex("""^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$""")
private val FORMATO_TELEFONO_NORMALIZADO: Regex = Regex("""^\+56(\d)(\d{4})(\d{4})$""")

/**
 * Normaliza un teléfono chileno de 9 dígitos, con o sin prefijo +56 o 56, al formato +56XXXXXXXXX.
 * Retorna null si [texto] no tiene ese formato.
 */
fun normalizarTelefono(texto: String): String? {
    val sinEspacios: String = texto.replace(" ", "")
    val coincidencia: MatchResult? = FORMATO_TELEFONO.matchEntire(sinEspacios)
    return coincidencia?.let { resultado: MatchResult -> "$PREFIJO_CHILE${resultado.groupValues[1]}" }
}

/**
 * Da formato `+56 (X) XXXX-XXXX` a un teléfono normalizado (+56 seguido de 9 dígitos),
 * por ejemplo `"+56931717552"` → `"+56 (9) 3171-7552"`.
 * Si [telefono] no tiene ese formato, lo retorna sin cambios.
 */
fun formatearTelefono(telefono: String): String {
    val coincidencia: MatchResult? = FORMATO_TELEFONO_NORMALIZADO.matchEntire(telefono)
    return if (coincidencia != null) {
        val (bloque1: String, bloque2: String, bloque3: String) = coincidencia.destructured
        "$PREFIJO_CHILE ($bloque1) $bloque2-$bloque3"
    } else {
        telefono
    }
}

/**
 * Indica si [texto] tiene el formato usuario@dominio.ext (sin considerar espacios ni mayúsculas).
 */
fun esCorreoValido(texto: String): Boolean = FORMATO_CORREO.matches(texto.trim().lowercase())

/**
 * Indica si [cantidad] está dentro de [RANGO_CANTIDAD_PIZZAS].
 */
fun esCantidadValida(cantidad: Int): Boolean = cantidad in RANGO_CANTIDAD_PIZZAS
