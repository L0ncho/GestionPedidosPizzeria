package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.model.Cliente
import cl.duoc.pmy2201.pizzeria.service.ClienteService

/**
 * Registra por consola los datos del cliente que realiza el pedido.
 */
class RegistroClienteConsola(
    private val lector: LectorConsola,
    private val clienteService: ClienteService
) {

    /**
     * Pide nombre y correo. Si ya hay un cliente registrado con esos datos, lo muestra y lo reutiliza
     * sin pedir dirección ni teléfono. Si no, pide dirección y teléfono, lo registra en [ClienteService],
     * muestra los datos registrados y retorna el nuevo [Cliente].
     */
    fun registrar(): Cliente {
        println()
        val nombre: String = lector.leerTextoObligatorio(
            mensaje = "Nombre del cliente: ",
            valorPorDefecto = NOMBRE_POR_DEFECTO,
            validacion = { texto: String -> esNombreValido(texto) },
            mensajeError = "Nombre inválido. Use solo letras, espacios, ' o -, con al menos $LARGO_MINIMO_NOMBRE caracteres y al menos una letra."
        )
        val correo: String? = lector.leerCorreo("Correo (Enter para omitir): ")

        val existente: Cliente? = clienteService.buscar(nombre, correo)
        if (existente != null) {
            println()
            println("Cliente ya registrado:")
            println(existente.describir())
            return existente
        }

        val direccion: String = lector.leerTextoObligatorio(
            mensaje = "Dirección de entrega: ",
            valorPorDefecto = DIRECCION_POR_DEFECTO,
            validacion = { texto: String -> esDireccionValida(texto) },
            mensajeError = "Dirección inválida. Debe tener al menos $LARGO_MINIMO_DIRECCION caracteres y al menos una letra."
        )
        val telefono: String = lector.leerTelefono("Teléfono de contacto: ")

        val cliente: Cliente = Cliente(nombre = nombre, telefono = telefono, correo = correo, direccion = direccion)
        clienteService.registrar(cliente)

        println()
        println("Cliente registrado")
        println(cliente.describir())

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
