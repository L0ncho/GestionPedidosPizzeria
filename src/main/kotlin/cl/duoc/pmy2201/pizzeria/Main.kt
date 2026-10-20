package cl.duoc.pmy2201.pizzeria

import cl.duoc.pmy2201.pizzeria.data.PizzaRepository
import cl.duoc.pmy2201.pizzeria.data.PizzaRepositoryCsv
import cl.duoc.pmy2201.pizzeria.data.RepartidorRepository
import cl.duoc.pmy2201.pizzeria.data.RepartidorRepositoryCsv
import cl.duoc.pmy2201.pizzeria.model.Cliente
import cl.duoc.pmy2201.pizzeria.model.Pedido
import cl.duoc.pmy2201.pizzeria.model.Pizza
import cl.duoc.pmy2201.pizzeria.model.Repartidor
import cl.duoc.pmy2201.pizzeria.service.AsignacionRepartidorService
import cl.duoc.pmy2201.pizzeria.service.CalculadoraPedido
import cl.duoc.pmy2201.pizzeria.service.InventarioService
import cl.duoc.pmy2201.pizzeria.service.PedidoService
import cl.duoc.pmy2201.pizzeria.service.ResultadoCalculo
import cl.duoc.pmy2201.pizzeria.service.ResultadoStock
import cl.duoc.pmy2201.pizzeria.ui.FormatoMoneda
import cl.duoc.pmy2201.pizzeria.ui.LectorConsola
import cl.duoc.pmy2201.pizzeria.ui.MensajesPedidoConsola
import cl.duoc.pmy2201.pizzeria.ui.MenuPizzasConsola
import cl.duoc.pmy2201.pizzeria.ui.RegistroClienteConsola
import kotlin.math.roundToInt

private const val MAXIMO_PIZZAS_POR_PEDIDO: Int = 20
private const val TAMANO_PIZZA: String = "Familiar"

fun main() {
    val lector: LectorConsola = LectorConsola()
    val mensajes: MensajesPedidoConsola = MensajesPedidoConsola(lector)
    val registroClienteConsola: RegistroClienteConsola = RegistroClienteConsola(lector)
    val menuPizzasConsola: MenuPizzasConsola = MenuPizzasConsola(lector)

    mensajes.mostrarBienvenida()

    val pizzaRepository: PizzaRepository = PizzaRepositoryCsv()
    val repartidorRepository: RepartidorRepository = RepartidorRepositoryCsv()

    val inventarioService: InventarioService = InventarioService()
    val calculadoraPedido: CalculadoraPedido = CalculadoraPedido()
    val asignacionRepartidorService: AsignacionRepartidorService = AsignacionRepartidorService(repartidorRepository)
    val pedidoService: PedidoService = PedidoService()

    mensajes.mostrarErroresDeCarga(pizzaRepository.erroresDeCarga() + repartidorRepository.erroresDeCarga())

    val pizzas: List<Pizza> = pizzaRepository.obtenerTodas()
    if (pizzas.isEmpty()) {
        mensajes.mostrarSinPizzas()
        return
    }

    val cliente: Cliente = registroClienteConsola.registrar()
    var pizza: Pizza
    var cantidadPizzas: Int

    do {
        pizza = menuPizzasConsola.elegirPizza(pizzas)
        cantidadPizzas = lector.leerEnteroEnRango("Cantidad de pizzas: ", 1, MAXIMO_PIZZAS_POR_PEDIDO)
        val resultadoStock: ResultadoStock = inventarioService.verificarYDescontar(pizza, cantidadPizzas)
        val stockConfirmado: Boolean = mensajes.mostrarResultadoStock(pizza, resultadoStock)

        if (!stockConfirmado && !mensajes.preguntarModificarPedido()) {
            mensajes.mostrarPedidoCancelado()
            return
        }
    } while (!stockConfirmado)

    if (calculadoraPedido.aplicaDescuento(cantidadPizzas)) {
        mensajes.mostrarDescuentoAplicado()
    }

    val calculo: ResultadoCalculo = calculadoraPedido.calcular(pizza.precioUnitario, cantidadPizzas)

    val repartidor: Repartidor? = asignacionRepartidorService.asignar()
    if (repartidor == null) {
        mensajes.avisarSinRepartidor()
    }

    val pedido: Pedido = pedidoService.crearPedido(
        cliente = cliente,
        pizza = pizza,
        cantidadPizzas = cantidadPizzas,
        totalPedido = calculo.total,
        repartidor = repartidor
    )

    mostrarResumen(pedido, calculo)
}

private fun mostrarResumen(pedido: Pedido, calculo: ResultadoCalculo) {
    val separador: String = "=".repeat(45)
    val separadorSeccion: String = "-".repeat(45)
    val porcentajeDescuento: Int = (CalculadoraPedido.PORCENTAJE_DESCUENTO * 100).roundToInt()
    val nombreRepartidor: String = pedido.repartidor?.nombre ?: "Sin repartidor disponible"

    fun linea(etiqueta: String, valor: String) {
        println("  ${"$etiqueta:".padEnd(23)}$valor")
    }

    println()
    println(separador)
    println("RESUMEN DEL PEDIDO N° ${pedido.idPedido}")
    println(separador)
    println("Cliente")
    linea("Nombre", pedido.cliente.nombre)
    linea("Dirección de entrega", pedido.cliente.direccion)
    linea("Teléfono", pedido.cliente.telefono)
    println(separadorSeccion)
    println("Pizza")
    linea("Nombre", pedido.pizza.nombrePizza)
    linea("Tamaño", TAMANO_PIZZA)
    linea("Precio unitario", FormatoMoneda.formatear(pedido.pizza.precioUnitario))
    println(separadorSeccion)
    println("Pedido")
    linea("Cantidad", pedido.cantidadPizzas.toString())
    linea("Subtotal", FormatoMoneda.formatear(calculo.subtotal))
    if (calculo.descuento > 0.0) {
        linea("Descuento $porcentajeDescuento%", "-${FormatoMoneda.formatear(calculo.descuento)}")
    }
    linea("Total", FormatoMoneda.formatear(calculo.total))
    linea("Estado", pedido.estadoPedido.descripcion)
    linea("Repartidor", nombreRepartidor)
    println(separador)
}
