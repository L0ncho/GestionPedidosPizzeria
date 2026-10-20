package cl.duoc.pmy2201.pizzeria.model

/**
 * Pizza del catálogo, en tamaño Familiar.
 *
 * @property nombrePizza Nombre de la pizza.
 * @property precioUnitario Precio de una pizza.
 * @property ingredientesDisponibles Cantidad de pizzas que se pueden preparar en el turno; se reduce en 1 por cada pizza preparada.
 */
data class Pizza(
    val nombrePizza: String,
    val precioUnitario: Double,
    var ingredientesDisponibles: Int
)
