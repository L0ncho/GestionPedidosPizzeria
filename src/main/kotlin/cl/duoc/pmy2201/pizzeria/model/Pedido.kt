package cl.duoc.pmy2201.pizzeria.model

/**
 * Pedido de un cliente: una pizza por una cantidad.
 *
 * @property idPedido Identificador autoincremental del pedido.
 * @property cliente Cliente que realiza el pedido.
 * @property pizza Pizza solicitada.
 * @property cantidadPizzas Cantidad de pizzas solicitadas.
 * @property totalPedido Total a pagar, con descuento aplicado si corresponde.
 * @property estadoPedido Estado actual del pedido.
 * @property repartidor Repartidor asignado, o null si no hay ninguno disponible.
 */
data class Pedido(
    val idPedido: Int,
    val cliente: Cliente,
    val pizza: Pizza,
    val cantidadPizzas: Int,
    val totalPedido: Double,
    var estadoPedido: EstadoPedido,
    var repartidor: Repartidor?
)
