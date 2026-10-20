package cl.duoc.pmy2201.pizzeria.service

/**
 * Calcula subtotal, descuento y total de un pedido.
 * Aplica [PORCENTAJE_DESCUENTO] cuando la cantidad supera [UMBRAL_PIZZAS_DESCUENTO].
 */
class CalculadoraPedido {

    fun aplicaDescuento(cantidadPizzas: Int): Boolean = cantidadPizzas > UMBRAL_PIZZAS_DESCUENTO

    fun calcular(precioUnitario: Double, cantidadPizzas: Int): ResultadoCalculo {
        val subtotal: Double = precioUnitario * cantidadPizzas.toDouble()
        val descuento: Double = if (aplicaDescuento(cantidadPizzas)) subtotal * PORCENTAJE_DESCUENTO else 0.0
        val total: Double = subtotal - descuento
        return ResultadoCalculo(subtotal = subtotal, descuento = descuento, total = total)
    }

    companion object {
        const val PORCENTAJE_DESCUENTO: Double = 0.10
        const val UMBRAL_PIZZAS_DESCUENTO: Int = 3
    }
}
