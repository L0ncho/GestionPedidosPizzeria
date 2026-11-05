package cl.duoc.pmy2201.pizzeria.util

import cl.duoc.pmy2201.pizzeria.model.Cliente
import cl.duoc.pmy2201.pizzeria.model.Pedido
import cl.duoc.pmy2201.pizzeria.model.Producto
import kotlin.test.Test
import kotlin.test.assertTrue

class AnalizadorClasesTest {

    @Test
    fun propiedadesDeProductoIncluyenNombrePrecioYCategoria() {
        val propiedades: List<String> = AnalizadorClases.propiedadesDe(Producto::class)

        assertTrue(propiedades.containsAll(listOf("nombre", "precio", "categoria")))
    }

    @Test
    fun propiedadesDeClienteIncluyenLasHeredadasDeUsuario() {
        val propiedades: List<String> = AnalizadorClases.propiedadesDe(Cliente::class)

        assertTrue(propiedades.containsAll(listOf("nombre", "telefono", "correo", "direccion")))
    }

    @Test
    fun metodosDePedidoIncluyenPlusYComponent1() {
        val metodos: List<String> = AnalizadorClases.metodosDe(Pedido::class)

        assertTrue(metodos.containsAll(listOf("plus", "component1")))
    }
}
