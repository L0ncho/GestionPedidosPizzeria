package cl.duoc.pmy2201.pizzeria

import cl.duoc.pmy2201.pizzeria.data.InventarioRepository
import cl.duoc.pmy2201.pizzeria.data.InventarioRepositoryCsv
import cl.duoc.pmy2201.pizzeria.data.ProductoRepository
import cl.duoc.pmy2201.pizzeria.data.ProductoRepositoryCsv
import cl.duoc.pmy2201.pizzeria.data.RepartidorRepository
import cl.duoc.pmy2201.pizzeria.data.RepartidorRepositoryCsv
import cl.duoc.pmy2201.pizzeria.model.Cliente
import cl.duoc.pmy2201.pizzeria.model.EstadoPedido
import cl.duoc.pmy2201.pizzeria.model.ItemPedido
import cl.duoc.pmy2201.pizzeria.model.Pedido
import cl.duoc.pmy2201.pizzeria.model.Producto
import cl.duoc.pmy2201.pizzeria.model.Repartidor
import cl.duoc.pmy2201.pizzeria.service.AsignacionRepartidorService
import cl.duoc.pmy2201.pizzeria.service.ClienteService
import cl.duoc.pmy2201.pizzeria.service.InventarioService
import cl.duoc.pmy2201.pizzeria.service.PedidoService
import cl.duoc.pmy2201.pizzeria.service.ResultadoRegistro
import cl.duoc.pmy2201.pizzeria.ui.InventarioConsola
import cl.duoc.pmy2201.pizzeria.ui.LectorConsola
import cl.duoc.pmy2201.pizzeria.ui.MensajesPedidoConsola
import cl.duoc.pmy2201.pizzeria.ui.MenuPizzasConsola
import cl.duoc.pmy2201.pizzeria.ui.RegistroClienteConsola
import cl.duoc.pmy2201.pizzeria.ui.ReporteDiaConsola
import cl.duoc.pmy2201.pizzeria.ui.ResumenFuncionalidadesConsola
import cl.duoc.pmy2201.pizzeria.ui.ResumenPedidoConsola

private const val OPCION_REGISTRAR_PEDIDO: Int = 1
private const val OPCION_REPORTE_DIA: Int = 2
private const val OPCION_CAMBIAR_ESTADO: Int = 3
private const val OPCION_INVENTARIO: Int = 4
private const val OPCION_RESUMEN_FUNCIONALIDADES: Int = 5
private const val OPCION_SALIR: Int = 6

fun main() {
    val productoRepository: ProductoRepository = ProductoRepositoryCsv()
    val inventarioRepository: InventarioRepository = InventarioRepositoryCsv()
    val repartidorRepository: RepartidorRepository = RepartidorRepositoryCsv()

    val inventarioService: InventarioService = InventarioService(inventarioRepository)
    val asignacionRepartidorService: AsignacionRepartidorService = AsignacionRepartidorService(repartidorRepository)
    val pedidoService: PedidoService = PedidoService(inventarioService, asignacionRepartidorService)
    val clienteService: ClienteService = ClienteService()

    val lector: LectorConsola = LectorConsola()
    val mensajes: MensajesPedidoConsola = MensajesPedidoConsola()
    val registroClienteConsola: RegistroClienteConsola = RegistroClienteConsola(lector, clienteService)
    val menuPizzasConsola: MenuPizzasConsola = MenuPizzasConsola(
        lector = lector,
        inventarioService = inventarioService,
        mensajes = mensajes,
        unidadDe = inventarioRepository::unidadDe
    )
    val resumenPedidoConsola: ResumenPedidoConsola = ResumenPedidoConsola()
    val reporteDiaConsola: ReporteDiaConsola = ReporteDiaConsola()
    val inventarioConsola: InventarioConsola = InventarioConsola(lector, inventarioService, inventarioRepository)
    val resumenFuncionalidadesConsola: ResumenFuncionalidadesConsola = ResumenFuncionalidadesConsola()

    mensajes.mostrarBienvenida()
    mensajes.mostrarErroresDeCarga(
        productoRepository.erroresDeCarga() +
            inventarioRepository.erroresDeCarga() +
            repartidorRepository.erroresDeCarga()
    )

    val productos: List<Producto> = productoRepository.obtenerTodos()
    if (productos.isEmpty()) {
        mensajes.mostrarSinProductos()
        return
    }

    var opcion: Int
    do {
        mensajes.mostrarMenuPrincipal()
        opcion = lector.leerEnteroEnRango("Elija una opción: ", OPCION_REGISTRAR_PEDIDO, OPCION_SALIR)
        when (opcion) {
            OPCION_REGISTRAR_PEDIDO -> registrarPedido(
                productos = productos,
                inventarioRepository = inventarioRepository,
                pedidoService = pedidoService,
                registroClienteConsola = registroClienteConsola,
                menuPizzasConsola = menuPizzasConsola,
                mensajes = mensajes,
                resumenPedidoConsola = resumenPedidoConsola
            )
            OPCION_REPORTE_DIA -> reporteDiaConsola.mostrar(pedidoService.pedidosDelDia())
            OPCION_CAMBIAR_ESTADO -> cambiarEstadoPedido(pedidoService, asignacionRepartidorService, lector, mensajes)
            OPCION_INVENTARIO -> inventarioConsola.gestionar()
            OPCION_RESUMEN_FUNCIONALIDADES -> resumenFuncionalidadesConsola.mostrar(
                pedidos = pedidoService.pedidosDelDia(),
                clientes = clienteService.clientesDelDia(),
                productos = productos
            )
            OPCION_SALIR -> mensajes.mostrarDespedida()
        }
    } while (opcion != OPCION_SALIR)
}

/**
 * Registra al cliente, pide los items y las notas, e intenta registrar el pedido mostrando el resultado.
 */
private fun registrarPedido(
    productos: List<Producto>,
    inventarioRepository: InventarioRepository,
    pedidoService: PedidoService,
    registroClienteConsola: RegistroClienteConsola,
    menuPizzasConsola: MenuPizzasConsola,
    mensajes: MensajesPedidoConsola,
    resumenPedidoConsola: ResumenPedidoConsola
) {
    val cliente: Cliente = registroClienteConsola.registrar()
    val items: List<ItemPedido> = menuPizzasConsola.elegirItems(productos)
    if (items.isEmpty()) {
        mensajes.mostrarPedidoCancelado()
        return
    }
    val notas: String? = menuPizzasConsola.pedirNotas()

    when (val resultado: ResultadoRegistro = pedidoService.registrarPedido(cliente, items, notas)) {
        is ResultadoRegistro.Registrado -> {
            val pedido: Pedido = resultado.pedido
            if (pedido.calcularTotal().descuentoCantidad > 0) {
                mensajes.mostrarDescuentoAplicado()
            }
            if (pedido.repartidor == null) {
                mensajes.mostrarSinRepartidor()
            }
            mensajes.mostrarNotificacion(pedido)
            resumenPedidoConsola.mostrar(pedido)
        }
        is ResultadoRegistro.Rechazado ->
            mensajes.mostrarIngredientesInsuficientes(resultado.resultado, inventarioRepository::unidadDe)
        ResultadoRegistro.SinCapacidad -> mensajes.mostrarSinCapacidad()
    }
}

/**
 * Muestra los pedidos pendientes, pide un N° y avanza el pedido al siguiente estado si corresponde.
 * Si un pedido LISTO no tiene repartidor, intenta asignarle el disponible más cercano antes de despacharlo.
 */
private fun cambiarEstadoPedido(
    pedidoService: PedidoService,
    asignacionRepartidorService: AsignacionRepartidorService,
    lector: LectorConsola,
    mensajes: MensajesPedidoConsola
) {
    val pendientes: List<Pedido> = pedidoService.pedidosDelDia().filter { pedido: Pedido -> !pedido.estado.completado }
    if (pendientes.isEmpty()) {
        mensajes.mostrarSinPedidosPendientes()
        return
    }
    mensajes.mostrarPedidosPendientes(pendientes)

    val idPedido: Int = lector.leerEnteroEnRango("N° del pedido (0 para volver): ", 0, PedidoService.CAPACIDAD_PEDIDOS_DIA)
    if (idPedido == 0) return
    val pedido: Pedido? = pedidoService.buscarPorId(idPedido)
    if (pedido == null) {
        mensajes.mostrarPedidoNoEncontrado()
        return
    }

    val estadoAnterior: EstadoPedido = pedido.estado
    val repartidor: Repartidor? = pedido.repartidor
    val cambiado: Boolean = when (estadoAnterior) {
        EstadoPedido.EN_PREPARACION -> pedido.cambiarEstado()
        EstadoPedido.LISTO -> {
            val repartidorDespacho: Repartidor? = repartidor ?: asignacionRepartidorService.asignar()
            if (repartidorDespacho == null) {
                mensajes.mostrarNoSePuedeDespachar()
                false
            } else {
                pedido.repartidor = repartidorDespacho
                pedido.cambiarEstado()
            }
        }
        EstadoPedido.EN_CAMINO -> if (repartidor == null) {
            mensajes.mostrarNoSePuedeEntregar()
            false
        } else {
            repartidor.entregarPedido(pedido)
        }
        EstadoPedido.ENTREGADO -> {
            mensajes.mostrarPedidoYaEntregado(pedido)
            false
        }
    }

    if (cambiado) {
        mensajes.mostrarCambioEstado(pedido, estadoAnterior)
    }
}
