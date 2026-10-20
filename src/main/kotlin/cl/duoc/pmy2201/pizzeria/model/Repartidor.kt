package cl.duoc.pmy2201.pizzeria.model

/**
 * Repartidor encargado de entregar pedidos.
 *
 * @property id Identificador del repartidor.
 * @property nombre Nombre del repartidor.
 * @property telefono Teléfono de contacto.
 * @property disponible Indica si puede recibir un pedido; cambia al ser asignado.
 */
data class Repartidor(
    val id: Int,
    val nombre: String,
    val telefono: String,
    var disponible: Boolean
)
