package cl.duoc.pmy2201.pizzeria.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ProductoTest {

    private val recetaPepperoni: Map<String, Int> = mapOf("Masa" to 1, "Pepperoni" to 80)

    private fun crearProducto(nombre: String, precio: Double = 11990.0, categoria: Categoria = Categoria.PIZZA): Producto =
        Producto(nombre = nombre, precio = precio, ingredientes = recetaPepperoni, categoria = categoria)

    @Test
    fun mismoNombreConDistintaMayusculaYEspaciosSonIgualesYTienenMismoHash() {
        val original: Producto = crearProducto(nombre = "Pepperoni")
        val variante: Producto = crearProducto(nombre = "  PEPPERONI ")

        assertEquals(original, variante)
        assertEquals(original.hashCode(), variante.hashCode())
    }

    @Test
    fun mismoNombreYCategoriaConDistintoPrecioSonIguales() {
        val original: Producto = crearProducto(nombre = "Pepperoni", precio = 11990.0)
        val conOtroPrecio: Producto = crearProducto(nombre = "Pepperoni", precio = 9990.0)

        assertEquals(original, conOtroPrecio)
    }

    @Test
    fun mismoNombreConDistintaCategoriaNoSonIguales() {
        val pizza: Producto = crearProducto(nombre = "Pepperoni", categoria = Categoria.PIZZA)
        val ingrediente: Producto = crearProducto(nombre = "Pepperoni", categoria = Categoria.INGREDIENTE)

        assertNotEquals(pizza, ingrediente)
    }

    @Test
    fun setConDosProductosIgualesQuedaConUnSoloElemento() {
        val productos: Set<Producto> = setOf(crearProducto(nombre = "Pepperoni"), crearProducto(nombre = "pepperoni"))

        assertEquals(1, productos.size)
    }
}
