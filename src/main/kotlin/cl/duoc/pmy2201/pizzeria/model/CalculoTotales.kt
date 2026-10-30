package cl.duoc.pmy2201.pizzeria.model

const val PORCENTAJE_DESCUENTO: Double = 0.10
const val UMBRAL_PIZZAS_DESCUENTO: Int = 3
const val TASA_IVA: Double = 0.19

/**
 * Totales de un pedido. Los precios incluyen IVA.
 *
 * @property subtotal Suma de los productos antes del descuento.
 * @property descuento Monto descontado.
 * @property total Monto a pagar, con IVA incluido.
 * @property neto Total sin IVA.
 * @property iva IVA contenido en el total.
 */
data class TotalesPedido(
    val subtotal: Double,
    val descuento: Double,
    val total: Double,
    val neto: Double,
    val iva: Double
)

/**
 * Calcula los totales aplicando [PORCENTAJE_DESCUENTO] cuando [cantidadPizzas] supera [UMBRAL_PIZZAS_DESCUENTO],
 * y desglosa el IVA del total.
 */
fun calcularTotales(subtotal: Double, cantidadPizzas: Int): TotalesPedido {
    val descuento: Double = if (cantidadPizzas > UMBRAL_PIZZAS_DESCUENTO) subtotal * PORCENTAJE_DESCUENTO else 0.0
    val total: Double = subtotal - descuento
    val neto: Double = total / (1 + TASA_IVA)
    val iva: Double = total - neto
    return TotalesPedido(subtotal = subtotal, descuento = descuento, total = total, neto = neto, iva = iva)
}
