package cl.duoc.pmy2201.pizzeria.model

/**
 * Cliente que realiza el pedido.
 *
 * @property nombre Nombre del cliente.
 * @property direccion Dirección de entrega.
 * @property telefono Teléfono de contacto, o "No informado" si no se entregó uno válido.
 */
data class Cliente(
    val nombre: String,
    val direccion: String,
    val telefono: String
)
