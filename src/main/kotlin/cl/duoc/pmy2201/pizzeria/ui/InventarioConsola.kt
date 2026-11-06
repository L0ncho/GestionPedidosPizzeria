package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.data.InventarioRepository
import cl.duoc.pmy2201.pizzeria.service.InventarioService

/**
 * Muestra el inventario por consola y permite reabastecer un ingrediente.
 */
class InventarioConsola(
    private val lector: LectorConsola,
    private val inventarioService: InventarioService,
    private val inventarioRepository: InventarioRepository
) {

    /**
     * Muestra el inventario numerado, pide un ingrediente y la cantidad recibida (repitiendo hasta que sea válida)
     * y lo reabastece. Con 0 vuelve al menú sin cambios. Si la cantidad haría superar el máximo de Int,
     * informa el error, no modifica el stock y vuelve a pedir la cantidad.
     */
    fun gestionar() {
        val ingredientes: List<String> = inventarioRepository.obtenerInventario().keys.toList()

        println()
        println("Inventario")
        for (indice: Int in ingredientes.indices) {
            println("${indice + 1}. ${ingredientes[indice]} - ${stockConUnidad(ingredientes[indice])}")
        }

        val opcion: Int = lector.leerEnteroEnRango("Elija un ingrediente (0 para volver): ", 0, ingredientes.size)
        if (opcion == 0) return
        val ingrediente: String = ingredientes[opcion - 1]

        var reabastecido: Boolean = false
        while (!reabastecido) {
            val texto: String = lector.leerTextoObligatorio(mensaje = "Cantidad recibida: ", valorPorDefecto = "")
            try {
                inventarioService.reabastecer(ingrediente, texto.toInt())
                reabastecido = true
            } catch (excepcion: NumberFormatException) {
                println("Debe ingresar un número entero.")
            } catch (excepcion: IllegalArgumentException) {
                println(excepcion.message)
            } catch (excepcion: ArithmeticException) {
                println("La cantidad supera el máximo permitido para el inventario.")
            }
        }

        println("$ingrediente reabastecido. Nuevo stock: ${stockConUnidad(ingrediente)}")
    }

    /** Retorna el stock del [ingrediente] con su unidad, por ejemplo "40 unidades". */
    private fun stockConUnidad(ingrediente: String): String {
        val cantidad: Int = inventarioRepository.obtenerInventario()[ingrediente] ?: 0
        val unidad: String = inventarioRepository.unidadDe(ingrediente) ?: ""
        return "$cantidad $unidad".trim()
    }
}
