package cl.duoc.pmy2201.pizzeria.service

/**
 * Resultado de verificar el inventario para un pedido.
 */
sealed class ResultadoVerificacion {

    /** Hay ingredientes suficientes para todo el pedido. */
    object Disponible : ResultadoVerificacion()

    /**
     * Faltan ingredientes; el inventario no se modificó.
     *
     * @property faltantes Ingrediente → cantidad que falta para completar el pedido.
     */
    data class Insuficiente(val faltantes: Map<String, Int>) : ResultadoVerificacion() {

        /** Nombres de los ingredientes que faltan. */
        val ingredientesFaltantes: Set<String>
            get() = faltantes.keys
    }
}
