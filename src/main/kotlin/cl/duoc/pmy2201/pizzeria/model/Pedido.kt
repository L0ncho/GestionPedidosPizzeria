package cl.duoc.pmy2201.pizzeria.model

import java.time.LocalDate

/**
 * Pedido de un cliente, con uno o más productos.
 *
 * @property idPedido Identificador autoincremental del pedido.
 * @property cliente Cliente que realiza el pedido.
 * @property items Productos solicitados, cada uno con su cantidad.
 * @property notas Notas opcionales del pedido.
 * @property repartidor Repartidor asignado, o null si no hay ninguno disponible.
 * @property fecha Fecha del pedido; por defecto, el día actual.
 */
class Pedido(
    val idPedido: Int,
    val cliente: Cliente,
    val items: List<ItemPedido>,
    val notas: String?,
    var repartidor: Repartidor? = null,
    val fecha: LocalDate = LocalDate.now()
) {

    var estado: EstadoPedido = EstadoPedido.EN_PREPARACION
        private set

    val notasParaMostrar: String
        get() = notas?.trim()?.takeIf { texto: String -> texto.isNotEmpty() } ?: "Sin notas"

    fun cantidadPizzas(): Int = items.sumOf { item: ItemPedido -> item.cantidad }

    /**
     * Calcula los totales del pedido con su [fecha] (ver [calcularTotales]).
     */
    fun calcularTotal(): TotalesPedido {
        val subtotal: Double = items.sumOf { item: ItemPedido -> item.subtotal() }
        val productos: List<Producto> = items.map { item: ItemPedido -> item.producto }
        return calcularTotales(subtotal = subtotal, cantidadPizzas = cantidadPizzas(), productos = productos, fecha = fecha)
    }

    /**
     * Retorna el estado que sigue al actual, o null si el pedido ya fue entregado.
     */
    fun siguienteEstado(): EstadoPedido? = when (estado) {
        EstadoPedido.EN_PREPARACION -> EstadoPedido.LISTO
        EstadoPedido.LISTO -> EstadoPedido.EN_CAMINO
        EstadoPedido.EN_CAMINO -> EstadoPedido.ENTREGADO
        EstadoPedido.ENTREGADO -> null
    }

    /**
     * Avanza al siguiente estado. Retorna false si el pedido ya fue entregado.
     */
    fun cambiarEstado(): Boolean {
        val siguiente: EstadoPedido = siguienteEstado() ?: return false
        estado = siguiente
        return true
    }
}
