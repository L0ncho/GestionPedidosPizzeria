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
     * Funciones de desestructuración: permiten escribir `val (cliente, productos, total) = pedido`.
     * [component1] retorna el cliente, [component2] los items y [component3] el total final de [calcularTotal].
     */
    operator fun component1(): Cliente = cliente

    /** Retorna los [items] del pedido (ver [component1]). */
    operator fun component2(): List<ItemPedido> = items

    /** Retorna el total final del pedido, con descuentos, según [calcularTotal] (ver [component1]). */
    operator fun component3(): Double = calcularTotal().total

    /**
     * Combina este pedido con [otro] del mismo cliente usando el operador `+`.
     * El nuevo pedido usa el [idPedido], el [cliente] y la [fecha] de este pedido, no tiene repartidor
     * y parte en estado [EstadoPedido.EN_PREPARACION] (En preparación).
     * Los items se suman; si un producto está en ambos (según `==`), queda una sola línea con la cantidad sumada.
     * Las notas que no son null se unen con " / ". El total se recalcula con [calcularTotal], incluidos los descuentos.
     * No modifica los pedidos originales.
     *
     * @throws IllegalArgumentException si los clientes de ambos pedidos no son iguales (`==`).
     */
    operator fun plus(otro: Pedido): Pedido {
        require(cliente == otro.cliente) {
            "No se pueden combinar los pedidos $idPedido y ${otro.idPedido}: pertenecen a clientes distintos."
        }
        val cantidadesPorProducto: Map<Producto, Int> = (items + otro.items)
            .groupingBy { item: ItemPedido -> item.producto }
            .fold(0) { acumulado: Int, item: ItemPedido -> acumulado + item.cantidad }
        val itemsCombinados: List<ItemPedido> = cantidadesPorProducto.map { entrada: Map.Entry<Producto, Int> ->
            ItemPedido(producto = entrada.key, cantidad = entrada.value)
        }
        val notasCombinadas: String? = listOfNotNull(notas, otro.notas)
            .joinToString(" / ")
            .takeIf { texto: String -> texto.isNotEmpty() }
        return Pedido(
            idPedido = idPedido,
            cliente = cliente,
            items = itemsCombinados,
            notas = notasCombinadas,
            repartidor = null,
            fecha = fecha
        )
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
