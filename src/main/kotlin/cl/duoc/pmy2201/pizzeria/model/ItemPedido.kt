package cl.duoc.pmy2201.pizzeria.model

/**
 * Línea de un pedido: un producto y su cantidad.
 *
 * @property producto Producto solicitado.
 * @property cantidad Cantidad solicitada.
 */
data class ItemPedido(
    val producto: Producto,
    val cantidad: Int
) {

    fun subtotal(): Double = producto.precio * cantidad
}
