package cl.duoc.pmy2201.pizzeria.util

/** Teléfono que se usa cuando no se logra obtener uno válido. */
const val TELEFONO_POR_DEFECTO: String = "+56900000000"

/** Correo que se usa cuando no se logra obtener uno válido. */
const val CORREO_POR_DEFECTO: String = "sin.correo@example.com"

private const val PREFIJO_CHILE: String = "+56"
private val FORMATO_TELEFONO: Regex = Regex("""^(?:\+?56)?(\d{9})$""")
private val FORMATO_CORREO: Regex = Regex("""^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$""")

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
 * Indica si [texto] tiene el formato usuario@dominio.ext (sin considerar espacios ni mayúsculas).
 */
fun esCorreoValido(texto: String): Boolean = FORMATO_CORREO.matches(texto.trim().lowercase())
