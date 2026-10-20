package cl.duoc.pmy2201.pizzeria.service

/**
 * Resultado de verificar el stock para un pedido.
 */
sealed class ResultadoStock {

    /**
     * Hay stock suficiente y ya fue descontado.
     *
     * @property stockRestante Stock que queda después del descuento.
     */
    data class Confirmado(val stockRestante: Int) : ResultadoStock()

    /**
     * No hay stock suficiente; el stock no se modificó.
     *
     * @property stockDisponible Stock actual de la pizza.
     * @property cantidadSolicitada Cantidad de pizzas pedida.
     */
    data class Rechazado(val stockDisponible: Int, val cantidadSolicitada: Int) : ResultadoStock()
}
