package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.model.Pedido

/**
 * Resultado de intentar registrar un pedido.
 */
sealed class ResultadoRegistro {

    /**
     * El pedido fue registrado y los ingredientes descontados.
     *
     * @property pedido Pedido creado.
     */
    data class Registrado(val pedido: Pedido) : ResultadoRegistro()

    /**
     * Faltan ingredientes; el inventario no se modificó.
     *
     * @property resultado Detalle de los ingredientes faltantes.
     */
    data class Rechazado(val resultado: ResultadoVerificacion.Insuficiente) : ResultadoRegistro()

    /** Ya se alcanzó la capacidad de pedidos del día. */
    object SinCapacidad : ResultadoRegistro()
}
