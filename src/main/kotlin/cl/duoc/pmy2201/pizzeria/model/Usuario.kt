package cl.duoc.pmy2201.pizzeria.model

import cl.duoc.pmy2201.pizzeria.util.formatearTelefono

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
     * Convierte `+56931717552` en `"+56 (9) 3171-7552"` usando [formatearTelefono];
     * si no tiene ese formato, lo retorna tal cual.
     */
    protected fun telefonoFormateado(): String = formatearTelefono(telefono)

    /** Retorna el teléfono en el formato para mostrar (ver [telefonoFormateado]). */
    fun telefonoParaMostrar(): String = telefonoFormateado()

    open fun describir(): String = "$nombre - Teléfono: ${telefonoFormateado()}"
}
