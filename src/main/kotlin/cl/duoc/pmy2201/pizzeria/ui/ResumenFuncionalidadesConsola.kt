package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.model.Categoria
import cl.duoc.pmy2201.pizzeria.model.Cliente
import cl.duoc.pmy2201.pizzeria.model.ItemPedido
import cl.duoc.pmy2201.pizzeria.model.Pedido
import cl.duoc.pmy2201.pizzeria.model.PizzaClasica
import cl.duoc.pmy2201.pizzeria.model.Producto
import cl.duoc.pmy2201.pizzeria.model.TotalesPedido
import cl.duoc.pmy2201.pizzeria.model.esPromocionable
import cl.duoc.pmy2201.pizzeria.util.AnalizadorClases
import cl.duoc.pmy2201.pizzeria.util.FormatoMoneda
import cl.duoc.pmy2201.pizzeria.util.formatearTelefono
import kotlin.reflect.KClass

/**
 * Muestra por consola un resumen de las funcionalidades del sistema: operador `+` entre pedidos,
 * desestructuración, anotación [cl.duoc.pmy2201.pizzeria.model.Promocionable], igualdad de objetos y reflection.
 * Solo muestra información: no modifica pedidos, clientes ni inventario.
 */
class ResumenFuncionalidadesConsola {

    /**
     * Muestra el resumen bajo el título "RESUMEN DE FUNCIONALIDADES".
     *
     * @param pedidos Pedidos registrados en el día, en orden de registro.
     * @param clientes Clientes registrados en el día, sin duplicados.
     * @param productos Productos del catálogo.
     */
    fun mostrar(pedidos: Array<Pedido>, clientes: Set<Cliente>, productos: List<Producto>) {
        println()
        println(SEPARADOR)
        println("RESUMEN DE FUNCIONALIDADES")
        println(SEPARADOR)

        val clienteCombinado: Cliente? = mostrarPedidosCombinados(pedidos)
        println(SEPARADOR_SECCION)
        mostrarCliente(clienteCombinado ?: clientes.firstOrNull())
        println(SEPARADOR_SECCION)
        mostrarPromocionables(productos)
        println(SEPARADOR_SECCION)
        mostrarIgualdad(pedidos, clientes, productos)
        println(SEPARADOR_SECCION)
        mostrarAnalisisDeClases()
        println(SEPARADOR)
    }

    /**
     * Combina con `+` los dos primeros pedidos de un mismo cliente y muestra el resultado desestructurado.
     * El pedido combinado es un objeto nuevo que no se registra. Retorna su cliente, o null si no hubo pedidos que combinar.
     */
    private fun mostrarPedidosCombinados(pedidos: Array<Pedido>): Cliente? {
        println("Pedidos combinados (operador +)")
        val pedidosDelMismoCliente: List<Pedido>? = pedidos
            .groupBy { pedido: Pedido -> pedido.cliente }
            .values
            .firstOrNull { pedidosCliente: List<Pedido> -> pedidosCliente.size >= 2 }
        if (pedidosDelMismoCliente == null) {
            println("  Registre al menos dos pedidos de un mismo cliente para ver esta sección.")
            return null
        }

        val pedido1: Pedido = pedidosDelMismoCliente[0]
        val pedido2: Pedido = pedidosDelMismoCliente[1]
        for (pedido: Pedido in listOf(pedido1, pedido2)) {
            println(
                "  Pedido N° ${pedido.idPedido}: ${pedido.cantidadPizzas()} pizzas - " +
                    "Total: ${FormatoMoneda.formatear(pedido.calcularTotal().total)}"
            )
        }

        val combinado: Pedido = pedido1 + pedido2
        val (cliente: Cliente, productos: List<ItemPedido>, total: Double) = combinado
        val totales: TotalesPedido = combinado.calcularTotal()
        println("  Pedido N° ${pedido1.idPedido} + Pedido N° ${pedido2.idPedido} (cliente: ${cliente.nombre})")
        for (item: ItemPedido in productos) {
            println("    ${item.producto.nombre} x ${item.cantidad}")
        }
        println("    Descuento por cantidad: -${FormatoMoneda.formatear(totales.descuentoCantidad)}")
        println("    Descuento promoción: -${FormatoMoneda.formatear(totales.descuentoPromocion)}")
        println("    Total: ${FormatoMoneda.formatear(total)}")
        return cliente
    }

    /**
     * Muestra los datos de [cliente] obtenidos con desestructuración, o un aviso si no hay clientes.
     */
    private fun mostrarCliente(cliente: Cliente?) {
        println("Cliente (desestructuración)")
        if (cliente == null) {
            println("  No hay clientes registrados hoy.")
            return
        }
        val (nombre: String, correo: String?, telefono: String) = cliente
        println("  Nombre: $nombre")
        println("  Correo: ${correo ?: "Sin correo"}")
        println("  Teléfono: ${formatearTelefono(telefono)}")
    }

    /**
     * Muestra los productos de [productos] cuya clase está marcada con @Promocionable (ver [esPromocionable]).
     */
    private fun mostrarPromocionables(productos: List<Producto>) {
        println("Productos promocionables (@Promocionable)")
        val promocionables: List<Producto> = productos.filter { producto: Producto -> esPromocionable(producto) }
        if (promocionables.isEmpty()) {
            println("  No hay productos promocionables en el catálogo.")
            return
        }
        for (producto: Producto in promocionables) {
            println("  ${producto.nombre} - ${FormatoMoneda.formatear(producto.precio)}")
        }
    }

    /**
     * Compara productos con `==` y muestra cuántos pedidos y clientes distintos hay en el día.
     * La [PizzaClasica] "pepperoni" (en minúsculas y con otro precio) y el producto con categoría
     * [Categoria.INGREDIENTE] se crean solo para la comparación.
     * `==` usa [Producto.equals] (nombre y categoría), mientras que `===` compara si son el mismo objeto en memoria.
     */
    private fun mostrarIgualdad(pedidos: Array<Pedido>, clientes: Set<Cliente>, productos: List<Producto>) {
        println("Igualdad de objetos")
        val pepperoniCatalogo: Producto? = buscarPepperoni(productos)
        if (pepperoniCatalogo == null) {
            println("  No se encontró Pepperoni en el catálogo.")
        } else {
            val pepperoniNueva: PizzaClasica = PizzaClasica(
                nombre = "pepperoni",
                precio = PRECIO_PEPPERONI_COMPARACION,
                ingredientes = pepperoniCatalogo.ingredientes
            )
            val igualesConDobleIgual: Boolean = pepperoniCatalogo == pepperoniNueva
            val mismoObjeto: Boolean = pepperoniCatalogo === pepperoniNueva
            println(
                "  Pepperoni (catálogo, ${FormatoMoneda.formatear(pepperoniCatalogo.precio)}) == " +
                    "pepperoni (nueva, ${FormatoMoneda.formatear(pepperoniNueva.precio)}): " +
                    "$igualesConDobleIgual → ${describirIgualdad(igualesConDobleIgual)}" +
                    if (igualesConDobleIgual) ", mismo nombre y categoría aunque cambie el precio" else ""
            )
            println(
                "  Pepperoni (catálogo) === pepperoni (nueva): $mismoObjeto → ${describirIgualdad(mismoObjeto)}" +
                    if (mismoObjeto) "" else ", son objetos diferentes en memoria"
            )

            val pepperoniIngrediente: Producto = Producto(
                nombre = pepperoniCatalogo.nombre,
                precio = pepperoniCatalogo.precio,
                ingredientes = pepperoniCatalogo.ingredientes,
                categoria = Categoria.INGREDIENTE
            )
            val otraCategoria: Boolean = pepperoniCatalogo == pepperoniIngrediente
            println(
                "  Pepperoni (${pepperoniCatalogo.categoria.descripcion}) == Pepperoni (${pepperoniIngrediente.categoria.descripcion}): " +
                    "$otraCategoria → ${describirIgualdad(otraCategoria)}" +
                    if (otraCategoria) "" else ", otra categoría"
            )
        }
        println("  Pedidos registrados: ${pedidos.size} | Clientes distintos: ${clientes.size}")
        println("  Los clientes repetidos (mismo nombre y correo, según ==) no se duplicaron.")
    }

    /**
     * Muestra las propiedades y los métodos declarados en [Pedido], [Producto] y [Cliente] usando [AnalizadorClases].
     */
    private fun mostrarAnalisisDeClases() {
        println("Análisis de clases (reflection)")
        val clases: List<KClass<*>> = listOf(Pedido::class, Producto::class, Cliente::class)
        for (clase: KClass<*> in clases) {
            println("  ${clase.simpleName}")
            println("    Propiedades: ${AnalizadorClases.propiedadesDe(clase).joinToString(", ")}")
            println("    Métodos: ${AnalizadorClases.metodosDe(clase).joinToString(", ")}")
        }
    }

    private fun buscarPepperoni(productos: List<Producto>): Producto? =
        productos.find { producto: Producto -> producto.nombre.equals(NOMBRE_PEPPERONI, ignoreCase = true) }

    private fun describirIgualdad(iguales: Boolean): String = if (iguales) "iguales" else "distintos"

    private companion object {
        const val NOMBRE_PEPPERONI: String = "Pepperoni"
        const val PRECIO_PEPPERONI_COMPARACION: Double = 9990.0
        val SEPARADOR: String = "=".repeat(40)
        val SEPARADOR_SECCION: String = "-".repeat(40)
    }
}
