package cl.duoc.pmy2201.pizzeria.data

import cl.duoc.pmy2201.pizzeria.model.Pizza

/**
 * Repositorio de pizzas que lee `pizzas.csv` una sola vez y mantiene los datos en memoria.
 */
class PizzaRepositoryCsv(
    private val lectorCsv: LectorCsv = LectorCsv()
) : PizzaRepository {

    private val carga: CargaPizzas by lazy { cargar() }

    override fun obtenerTodas(): List<Pizza> = carga.pizzas

    override fun erroresDeCarga(): List<String> = carga.errores

    private fun cargar(): CargaPizzas {
        val resultado: ResultadoLectura = lectorCsv.leer(NOMBRE_ARCHIVO)
        val pizzas: MutableList<Pizza> = mutableListOf()
        val errores: MutableList<String> = resultado.errores.toMutableList()

        resultado.filas.forEachIndexed { indice: Int, fila: List<String> ->
            val pizza: Pizza? = convertirFila(fila)
            if (pizza != null) {
                pizzas.add(pizza)
            } else {
                errores.add("Fila ${indice + 1} inválida en $NOMBRE_ARCHIVO: ${fila.joinToString(";")}")
            }
        }

        return CargaPizzas(pizzas = pizzas.toList(), errores = errores.toList())
    }

    private fun convertirFila(fila: List<String>): Pizza? {
        if (fila.size != CANTIDAD_COLUMNAS) return null

        val nombrePizza: String = fila[0]
        val precioUnitario: Double? = fila[1].toDoubleOrNull()
        val ingredientesDisponibles: Int? = fila[2].toIntOrNull()

        return if (
            nombrePizza.isNotEmpty() &&
            precioUnitario != null && precioUnitario > 0 &&
            ingredientesDisponibles != null && ingredientesDisponibles >= 0
        ) {
            Pizza(
                nombrePizza = nombrePizza,
                precioUnitario = precioUnitario,
                ingredientesDisponibles = ingredientesDisponibles
            )
        } else {
            null
        }
    }

    private data class CargaPizzas(
        val pizzas: List<Pizza>,
        val errores: List<String>
    )

    private companion object {
        const val NOMBRE_ARCHIVO: String = "pizzas.csv"
        const val CANTIDAD_COLUMNAS: Int = 3
    }
}
