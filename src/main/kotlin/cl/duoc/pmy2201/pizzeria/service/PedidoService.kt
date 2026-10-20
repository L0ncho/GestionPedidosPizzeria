package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.model.Cliente
import cl.duoc.pmy2201.pizzeria.model.EstadoPedido
import cl.duoc.pmy2201.pizzeria.model.Pedido
import cl.duoc.pmy2201.pizzeria.model.Pizza
import cl.duoc.pmy2201.pizzeria.model.Repartidor

/**
 * Crea pedidos con idPedido autoincremental, partiendo en 1.
 */
class PedidoService {

    private var siguienteIdPedido: Int = 1

    /**
     * Crea un [Pedido] con el siguiente id y estado [EstadoPedido.PREPARACION].
     */
    fun crearPedido(
        cliente: Cliente,
        pizza: Pizza,
        cantidadPizzas: Int,
        totalPedido: Double,
        repartidor: Repartidor?
    ): Pedido {
        val idPedido: Int = siguienteIdPedido
        siguienteIdPedido++
        return Pedido(
            idPedido = idPedido,
            cliente = cliente,
            pizza = pizza,
            cantidadPizzas = cantidadPizzas,
            totalPedido = totalPedido,
            estadoPedido = EstadoPedido.PREPARACION,
            repartidor = repartidor
        )
    }
}
