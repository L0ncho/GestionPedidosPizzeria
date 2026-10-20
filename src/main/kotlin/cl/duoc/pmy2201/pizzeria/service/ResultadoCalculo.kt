package cl.duoc.pmy2201.pizzeria.service

/**
 * Montos calculados para un pedido.
 *
 * @property subtotal Precio unitario por cantidad, sin descuento.
 * @property descuento Monto descontado (0.0 si no aplica).
 * @property total Subtotal menos descuento.
 */
data class ResultadoCalculo(
    val subtotal: Double,
    val descuento: Double,
    val total: Double
)
