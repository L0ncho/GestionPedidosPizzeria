package cl.duoc.pmy2201.pizzeria.model

/**
 * Persona registrada en el sistema, base de [Cliente] y [Repartidor].
 *
 * @property nombre Nombre de la persona.
 * @property telefono Teléfono de contacto.
 */
open class Usuario(
    val nombre: String,
    val telefono: String
) {

    /**
     * Convierte `+56912345678` en `"+56 9 1234 5678"`; si no tiene ese formato, lo retorna tal cual.
     */
    protected fun telefonoFormateado(): String =
        if (telefono.length == LARGO_TELEFONO && telefono.startsWith(PREFIJO_MOVIL) && telefono.substring(1).all { caracter: Char -> caracter.isDigit() }) {
            "${telefono.substring(0, 3)} ${telefono.substring(3, 4)} ${telefono.substring(4, 8)} ${telefono.substring(8, 12)}"
        } else {
            telefono
        }

    /** Retorna el teléfono en el formato para mostrar (ver [telefonoFormateado]). */
    fun telefonoParaMostrar(): String = telefonoFormateado()

    open fun describir(): String = "$nombre - Teléfono: ${telefonoFormateado()}"

    private companion object {
        const val LARGO_TELEFONO: Int = 12
        const val PREFIJO_MOVIL: String = "+569"
    }
}
