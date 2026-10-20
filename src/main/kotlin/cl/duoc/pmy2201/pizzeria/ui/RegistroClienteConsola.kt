package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.model.Cliente

/**
 * Registra por consola los datos del cliente que realiza el pedido.
 */
class RegistroClienteConsola(private val lector: LectorConsola) {

    /**
     * Pide nombre, dirección y teléfono, muestra los datos registrados y retorna el [Cliente].
     */
    fun registrar(): Cliente {
        println()
        val nombre: String = lector.leerTextoObligatorio(
            mensaje = "Nombre del cliente: ",
            valorPorDefecto = NOMBRE_POR_DEFECTO,
            validacion = { texto: String -> esNombreValido(texto) },
            mensajeError = "Nombre inválido. Use solo letras, espacios, ' o -, con al menos $LARGO_MINIMO_NOMBRE caracteres y al menos una letra."
        )
        val direccion: String = lector.leerTextoObligatorio(
            mensaje = "Dirección de entrega: ",
            valorPorDefecto = DIRECCION_POR_DEFECTO,
            validacion = { texto: String -> esDireccionValida(texto) },
            mensajeError = "Dirección inválida. Debe tener al menos $LARGO_MINIMO_DIRECCION caracteres y al menos una letra."
        )
        val telefono: String = lector.leerTelefono("Teléfono de contacto: ")

        val cliente: Cliente = Cliente(nombre = nombre, direccion = direccion, telefono = telefono)

        println()
        println("Cliente registrado")
        println("Nombre: ${cliente.nombre}")
        println("Dirección: ${cliente.direccion}")
        println("Teléfono: ${cliente.telefono}")

        return cliente
    }

    private fun esNombreValido(nombre: String): Boolean =
        nombre.length >= LARGO_MINIMO_NOMBRE &&
            FORMATO_NOMBRE.matches(nombre) &&
            nombre.any { caracter: Char -> caracter.isLetter() }

    private fun esDireccionValida(direccion: String): Boolean =
        direccion.length >= LARGO_MINIMO_DIRECCION && direccion.any { caracter: Char -> caracter.isLetter() }

    private companion object {
        const val NOMBRE_POR_DEFECTO: String = "Cliente sin nombre"
        const val DIRECCION_POR_DEFECTO: String = "Sin dirección"
        const val LARGO_MINIMO_NOMBRE: Int = 2
        const val LARGO_MINIMO_DIRECCION: Int = 5
        val FORMATO_NOMBRE: Regex = Regex("""^[A-Za-zÁÉÍÓÚáéíóúÑñÜü' -]+$""")
    }
}
