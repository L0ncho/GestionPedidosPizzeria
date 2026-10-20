package cl.duoc.pmy2201.pizzeria.model

/**
 * Estados por los que pasa un pedido.
 *
 * @property descripcion Texto legible del estado.
 */
enum class EstadoPedido(val descripcion: String) {
    PREPARACION("Preparación"),
    EN_CAMINO("En Camino"),
    ENTREGADO("Entregado")
}
