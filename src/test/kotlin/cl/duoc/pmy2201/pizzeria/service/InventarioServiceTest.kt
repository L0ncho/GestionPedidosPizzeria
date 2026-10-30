package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.data.InventarioRepository
import cl.duoc.pmy2201.pizzeria.model.ItemPedido
import cl.duoc.pmy2201.pizzeria.model.Producto
import kotlin.test.Test
import kotlin.test.assertEquals
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
        ingredientes = mapOf("Queso mozzarella" to 150, "Queso azul" to 60)
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
}
