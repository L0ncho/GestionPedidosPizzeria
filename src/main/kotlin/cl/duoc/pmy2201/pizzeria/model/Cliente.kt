package cl.duoc.pmy2201.pizzeria.model

/**
 * Cliente que realiza el pedido.
 *
 * @param nombre Nombre del cliente.
 * @param telefono Teléfono de contacto, o "No informado" si no se entregó uno válido.
 * @property correo Correo electrónico, o `null` si no se informó.
 * @property direccion Dirección de entrega.
 */
class Cliente(
    nombre: String,
    telefono: String,
    val correo: String?,
    val direccion: String
) : Usuario(nombre, telefono) {

    override fun describir(): String =
        "${super.describir()} - Correo: ${correo ?: "Sin correo"} - Dirección: $direccion"
}
