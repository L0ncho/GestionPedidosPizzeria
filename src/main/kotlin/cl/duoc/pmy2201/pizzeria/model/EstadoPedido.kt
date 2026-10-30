package cl.duoc.pmy2201.pizzeria.model

/**
 * Estados por los que pasa un pedido.
 *
 * @property descripcion Texto legible del estado.
 * @property completado Indica si el pedido ya fue completado; solo es `true` en [ENTREGADO].
 */
enum class EstadoPedido(val descripcion: String, val completado: Boolean) {
    EN_PREPARACION("En preparación", false),
    LISTO("Listo", false),
    EN_CAMINO("En camino", false),
    ENTREGADO("Entregado", true)
}
