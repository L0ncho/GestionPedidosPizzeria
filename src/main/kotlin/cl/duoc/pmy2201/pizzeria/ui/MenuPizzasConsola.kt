package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.model.ItemPedido
import cl.duoc.pmy2201.pizzeria.model.PORCENTAJE_DESCUENTO
import cl.duoc.pmy2201.pizzeria.model.Producto
import cl.duoc.pmy2201.pizzeria.model.UMBRAL_PIZZAS_DESCUENTO
import cl.duoc.pmy2201.pizzeria.service.InventarioService
import cl.duoc.pmy2201.pizzeria.service.ResultadoVerificacion
import cl.duoc.pmy2201.pizzeria.util.RANGO_CANTIDAD_PIZZAS
import cl.duoc.pmy2201.pizzeria.util.esCantidadValida
import kotlin.math.roundToInt

/**
 * Muestra el catálogo de productos por consola y arma los items del pedido.
 *
 * @param unidadDe Entrega la unidad de un ingrediente, o null si no se conoce.
 */
class MenuPizzasConsola(
    private val lector: LectorConsola,
    private val inventarioService: InventarioService,
    private val mensajes: MensajesPedidoConsola,
    private val unidadDe: (String) -> String?
) {

    /**
     * Muestra [productos] como lista numerada y pide productos y cantidades hasta que el usuario termine
     * o se alcance el límite superior de [RANGO_CANTIDAD_PIZZAS]. Si un producto se elige más de una vez, suma las cantidades.
     * Cada item se agrega solo si el inventario alcanza para el pedido completo; si no, se informa
     * qué falta y se puede elegir otro producto, terminar con los items ya agregados o cancelar.
     * Retorna una lista vacía si el pedido se cancela.
     * [productos] no debe estar vacía.
     */
    fun elegirItems(productos: List<Producto>): List<ItemPedido> {
        println()
        println("Catálogo de pizzas")
        val porcentajeDescuento: Int = (PORCENTAJE_DESCUENTO * 100).roundToInt()
        println("Promoción: $porcentajeDescuento% de descuento en pedidos de más de $UMBRAL_PIZZAS_DESCUENTO pizzas")
        productos.forEachIndexed { indice: Int, producto: Producto ->
            println("${indice + 1}. ${producto.detallarProducto()}")
        }

        val items: MutableList<ItemPedido> = mutableListOf()
        var agregarOtro: Boolean = true

        while (agregarOtro) {
            println()
            val opcion: Int = lector.leerEnteroEnRango("Elija un producto: ", 1, productos.size)
            val producto: Producto = productos[opcion - 1]
            val cantidad: Int = pedirCantidad(totalPizzas(items))

            val itemsConNuevo: MutableList<ItemPedido> = items.toMutableList()
            agregarItem(itemsConNuevo, producto, cantidad)
            val verificacion: ResultadoVerificacion = inventarioService.verificar(itemsConNuevo)
            if (verificacion is ResultadoVerificacion.Insuficiente) {
                mensajes.mostrarIngredientesInsuficientes(verificacion, unidadDe)
                when (pedirAccionSinStock(hayItems = items.isNotEmpty())) {
                    OPCION_TERMINAR -> return items.toList()
                    OPCION_CANCELAR -> return emptyList()
                    else -> continue
                }
            }
            agregarItem(items, producto, cantidad)

            val total: Int = totalPizzas(items)
            if (total >= RANGO_CANTIDAD_PIZZAS.last) {
                println("Se alcanzó el máximo de ${RANGO_CANTIDAD_PIZZAS.last} pizzas por pedido.")
                agregarOtro = false
            } else {
                val respuesta: String = lector.leerTextoObligatorio(
                    mensaje = "¿Agregar otro producto? (s/n): ",
                    valorPorDefecto = "n",
                    validacion = { texto: String -> texto.lowercase() in RESPUESTAS_VALIDAS },
                    mensajeError = "Responda s o n."
                )
                agregarOtro = respuesta.lowercase() == "s"
            }
        }

        return items.toList()
    }

    /**
     * Pide las notas del pedido; retorna null si se omiten.
     */
    fun pedirNotas(): String? = lector.leerTextoOpcional("Notas del pedido (Enter para omitir): ")

    /**
     * Pregunta qué hacer cuando no alcanzan los ingredientes. La opción [OPCION_TERMINAR]
     * solo se ofrece y se acepta si [hayItems].
     */
    private fun pedirAccionSinStock(hayItems: Boolean): Int {
        println()
        println("¿Qué desea hacer?")
        println("$OPCION_ELEGIR_OTRO. Elegir otro producto o una cantidad menor")
        if (hayItems) {
            println("$OPCION_TERMINAR. Terminar el pedido con los productos ya agregados")
        }
        println("$OPCION_CANCELAR. Cancelar el pedido")

        while (true) {
            val opcion: Int = lector.leerEnteroEnRango("Elija una opción: ", OPCION_ELEGIR_OTRO, OPCION_CANCELAR)
            if (opcion != OPCION_TERMINAR || hayItems) return opcion
            println("Aún no hay productos agregados. Elija $OPCION_ELEGIR_OTRO o $OPCION_CANCELAR.")
        }
    }

    private fun pedirCantidad(totalActual: Int): Int {
        while (true) {
            val cantidad: Int = lector.leerEnteroEnRango("Cantidad: ", RANGO_CANTIDAD_PIZZAS.first, RANGO_CANTIDAD_PIZZAS.last)
            if (esCantidadValida(totalActual + cantidad)) return cantidad
            val disponibles: Int = RANGO_CANTIDAD_PIZZAS.last - totalActual
            println("El pedido no puede superar ${RANGO_CANTIDAD_PIZZAS.last} pizzas. Puede agregar como máximo $disponibles.")
        }
    }

    private fun agregarItem(items: MutableList<ItemPedido>, producto: Producto, cantidad: Int) {
        val indice: Int = items.indexOfFirst { item: ItemPedido -> item.producto == producto }
        if (indice >= 0) {
            items[indice] = items[indice].copy(cantidad = items[indice].cantidad + cantidad)
        } else {
            items.add(ItemPedido(producto = producto, cantidad = cantidad))
        }
    }

    private fun totalPizzas(items: List<ItemPedido>): Int = items.sumOf { item: ItemPedido -> item.cantidad }

    companion object {
        private const val OPCION_ELEGIR_OTRO: Int = 1
        private const val OPCION_TERMINAR: Int = 2
        private const val OPCION_CANCELAR: Int = 3
        private val RESPUESTAS_VALIDAS: Set<String> = setOf("s", "n")
    }
}
