package cl.duoc.pmy2201.pizzeria.model

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PromocionableTest {

    private val receta: Map<String, Int> = mapOf("Masa" to 1, "Queso mozzarella" to 150)

    @Test
    fun pizzaClasicaEsPromocionable() {
        val pizza: PizzaClasica = PizzaClasica(nombre = "Pepperoni", precio = 11990.0, ingredientes = receta)

        assertTrue(esPromocionable(pizza))
    }

    @Test
    fun pizzaEspecialNoEsPromocionable() {
        val pizza: PizzaEspecial = PizzaEspecial(nombre = "Cuatro Quesos", precio = 13990.0, ingredientes = receta)

        assertFalse(esPromocionable(pizza))
    }
}
