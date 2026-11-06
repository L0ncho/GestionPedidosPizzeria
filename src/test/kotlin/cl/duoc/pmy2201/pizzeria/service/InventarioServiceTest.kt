package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.data.InventarioRepository
import cl.duoc.pmy2201.pizzeria.model.Categoria
import cl.duoc.pmy2201.pizzeria.model.ItemPedido
import cl.duoc.pmy2201.pizzeria.model.Producto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class InventarioServiceTest {

    private class InventarioRepositoryEnMemoria(
        private val inventario: MutableMap<String, Int>
    ) : InventarioRepository {
        override fun obtenerInventario(): MutableMap<String, Int> = inventario
        override fun unidadDe(ingrediente: String): String? = if (inventario.containsKey(ingrediente)) "g" else null
        override fun erroresDeCarga(): List<String> = emptyList()
    }

    private val cuatroQuesos: Producto = Producto(
        nombre = "Cuatro Quesos",
        precio = 13990.0,
        ingredientes = mapOf("Queso mozzarella" to 150, "Queso azul" to 60),
        categoria = Categoria.PIZZA
    )

    private fun crearInventario(): MutableMap<String, Int> =
        mutableMapOf("Queso mozzarella" to 8000, "Queso azul" to 150)

    @Test
    fun stockSuficienteRetornaDisponible() {
        val inventarioService: InventarioService = InventarioService(InventarioRepositoryEnMemoria(crearInventario()))

        val resultado: ResultadoVerificacion = inventarioService.verificar(listOf(ItemPedido(producto = cuatroQuesos, cantidad = 2)))

        assertIs<ResultadoVerificacion.Disponible>(resultado)
    }

    @Test
    fun quesoAzulInsuficienteRetornaFaltanteSinModificarInventario() {
        val inventario: MutableMap<String, Int> = crearInventario()
        val inventarioService: InventarioService = InventarioService(InventarioRepositoryEnMemoria(inventario))

        val resultado: ResultadoVerificacion = inventarioService.verificar(listOf(ItemPedido(producto = cuatroQuesos, cantidad = 3)))

        val insuficiente: ResultadoVerificacion.Insuficiente = assertIs<ResultadoVerificacion.Insuficiente>(resultado)
        assertEquals(mapOf("Queso azul" to 30), insuficiente.faltantes)
        assertEquals(crearInventario(), inventario)
    }

    @Test
    fun descontarConStockInsuficienteLanzaExcepcionSinModificarIngredientes() {
        val inventario: MutableMap<String, Int> = crearInventario()
        val inventarioService: InventarioService = InventarioService(InventarioRepositoryEnMemoria(inventario))

        assertFailsWith<IllegalArgumentException> {
            inventarioService.descontar(mapOf("Queso mozzarella" to 300, "Queso azul" to 200))
        }

        assertEquals(crearInventario(), inventario)
    }

    @Test
    fun prepararLoteConPizzasQueCompartenIngredientesDescuentaLaSumaAcumulada() {
        val hawaiana: Producto = Producto(
            nombre = "Hawaiana",
            precio = 11990.0,
            ingredientes = mapOf(
                "Masa" to 1,
                "Salsa de tomate" to 150,
                "Queso mozzarella" to 250,
                "Jamón" to 120,
                "Piña" to 120
            ),
            categoria = Categoria.PIZZA
        )
        val inventario: MutableMap<String, Int> = mutableMapOf(
            "Masa" to 40,
            "Salsa de tomate" to 6000,
            "Queso mozzarella" to 8000,
            "Queso azul" to 150,
            "Jamón" to 2000,
            "Piña" to 1500
        )
        val inventarioService: InventarioService = InventarioService(InventarioRepositoryEnMemoria(inventario))

        val descontados: Map<String, Int> = inventarioService.prepararLote(
            listOf(
                ItemPedido(producto = cuatroQuesos, cantidad = 2),
                ItemPedido(producto = hawaiana, cantidad = 1)
            )
        )

        assertEquals(
            mapOf(
                "Queso mozzarella" to 550,
                "Queso azul" to 120,
                "Masa" to 1,
                "Salsa de tomate" to 150,
                "Jamón" to 120,
                "Piña" to 120
            ),
            descontados
        )
        assertEquals(
            mapOf(
                "Masa" to 39,
                "Salsa de tomate" to 5850,
                "Queso mozzarella" to 7450,
                "Queso azul" to 30,
                "Jamón" to 1880,
                "Piña" to 1380
            ),
            inventario
        )
    }

    @Test
    fun reabastecerConCantidadQueDesbordaLanzaExcepcionSinCambiarStock() {
        val inventario: MutableMap<String, Int> = crearInventario()
        val inventarioService: InventarioService = InventarioService(InventarioRepositoryEnMemoria(inventario))

        assertFailsWith<ArithmeticException> {
            inventarioService.reabastecer("Queso azul", Int.MAX_VALUE)
        }

        assertEquals(crearInventario(), inventario)
    }

    @Test
    fun calcularIngredientesLoteConValoresQueDesbordanLanzaExcepcion() {
        val inventarioService: InventarioService = InventarioService(InventarioRepositoryEnMemoria(crearInventario()))

        assertFailsWith<ArithmeticException> {
            inventarioService.calcularIngredientesLote(mapOf("Queso mozzarella" to Int.MAX_VALUE), 2)
        }
    }
}
