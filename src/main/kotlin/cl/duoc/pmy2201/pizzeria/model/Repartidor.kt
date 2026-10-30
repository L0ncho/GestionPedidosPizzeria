package cl.duoc.pmy2201.pizzeria.model

/**
 * Repartidor encargado de entregar pedidos.
 *
 * @property id Identificador del repartidor.
 * @param nombre Nombre del repartidor.
 * @param telefono Teléfono de contacto.
 * @property disponible Indica si puede recibir un pedido; cambia al ser asignado.
 * @property distanciaKm Distancia simulada a la pizzería, en kilómetros.
 */
class Repartidor(
    val id: Int,
    nombre: String,
    telefono: String,
    var disponible: Boolean,
    val distanciaKm: Double
) : Usuario(nombre, telefono) {

    override fun describir(): String = "${super.describir()} - Distancia: $distanciaKm km"

    /**
     * Si [pedido] está [EstadoPedido.EN_CAMINO], lo marca como [EstadoPedido.ENTREGADO],
     * deja al repartidor disponible y retorna true. En otro estado no cambia nada y retorna false.
     */
    fun entregarPedido(pedido: Pedido): Boolean {
        if (pedido.estado != EstadoPedido.EN_CAMINO) return false
        pedido.cambiarEstado()
        disponible = true
        return true
    }
}
