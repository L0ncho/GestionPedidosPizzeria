package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.model.Cliente
import cl.duoc.pmy2201.pizzeria.model.ItemPedido
import cl.duoc.pmy2201.pizzeria.model.Pedido

/**
 * Registra los pedidos del día: verifica y descuenta ingredientes, asigna repartidor
 * y guarda cada pedido con idPedido autoincremental, partiendo en 1.
 */
class PedidoService(
    private val inventarioService: InventarioService,
    private val asignacionRepartidorService: AsignacionRepartidorService
) {

    private val arregloPedidos: Array<Pedido?> = arrayOfNulls(CAPACIDAD_PEDIDOS_DIA)
    private var cantidadPedidos: Int = 0

    /**
     * Registra un pedido con los [items] del [cliente]:
     * - [ResultadoRegistro.SinCapacidad] si ya se alcanzó [CAPACIDAD_PEDIDOS_DIA]; no se modifica nada.
     * - [ResultadoRegistro.Rechazado] si faltan ingredientes; no se modifica el inventario.
     * - [ResultadoRegistro.Registrado] si hay ingredientes: los descuenta, crea el pedido,
     *   le asigna el repartidor disponible más cercano y lo guarda.
     */
    fun registrarPedido(cliente: Cliente, items: List<ItemPedido>, notas: String?): ResultadoRegistro {
        if (cantidadPedidos >= CAPACIDAD_PEDIDOS_DIA) return ResultadoRegistro.SinCapacidad

        return when (val verificacion: ResultadoVerificacion = inventarioService.verificar(items)) {
            is ResultadoVerificacion.Insuficiente -> ResultadoRegistro.Rechazado(resultado = verificacion)
            ResultadoVerificacion.Disponible -> {
                items.forEach { item: ItemPedido ->
                    inventarioService.prepararLote(item.producto.ingredientes, item.cantidad)
                }
                val pedido: Pedido = Pedido(
                    idPedido = cantidadPedidos + 1,
                    cliente = cliente,
                    items = items,
                    notas = notas,
                    repartidor = asignacionRepartidorService.asignar()
                )
                arregloPedidos[cantidadPedidos] = pedido
                cantidadPedidos++
                ResultadoRegistro.Registrado(pedido = pedido)
            }
        }
    }

    /** Devuelve los pedidos registrados en el día como arreglo, en orden de registro y sin posiciones vacías. */
    fun pedidosDelDia(): Array<Pedido> = arregloPedidos.filterNotNull().toTypedArray()

    /** Devuelve el pedido con el [id] indicado, o null si no existe. */
    fun buscarPorId(id: Int): Pedido? = pedidosDelDia().find { pedido: Pedido -> pedido.idPedido == id }

    companion object {
        const val CAPACIDAD_PEDIDOS_DIA: Int = 100
    }
}
