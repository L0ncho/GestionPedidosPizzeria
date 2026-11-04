package cl.duoc.pmy2201.pizzeria.data

import cl.duoc.pmy2201.pizzeria.model.PizzaClasica
import cl.duoc.pmy2201.pizzeria.model.PizzaEspecial
import cl.duoc.pmy2201.pizzeria.model.Producto

/**
 * Repositorio de productos que lee `pizzas.csv` y `recetas.csv` una sola vez y mantiene los datos en memoria.
 */
class ProductoRepositoryCsv(
    private val lectorCsv: LectorCsv = LectorCsv()
) : ProductoRepository {

    private val carga: CargaProductos by lazy { cargar() }

    override fun obtenerTodos(): List<Producto> = carga.productos

    override fun erroresDeCarga(): List<String> = carga.errores

    private fun cargar(): CargaProductos {
        val errores: MutableList<String> = mutableListOf()
        val recetasPorPizza: Map<String, Map<String, Int>> = cargarRecetas(errores)

        val resultado: ResultadoLectura = lectorCsv.leer(ARCHIVO_PIZZAS)
        errores.addAll(resultado.errores)
        val productos: MutableList<Producto> = mutableListOf()

        resultado.filas.forEachIndexed { indice: Int, fila: List<String> ->
            val filaPizza: FilaPizza? = convertirFilaPizza(fila)
            val receta: Map<String, Int>? = filaPizza?.let { pizza: FilaPizza -> recetasPorPizza[pizza.nombre] }
            when {
                filaPizza == null ->
                    errores.add("Fila ${indice + 1} inválida en $ARCHIVO_PIZZAS: ${fila.joinToString(";")}")
                receta == null ->
                    errores.add("La pizza ${filaPizza.nombre} no tiene receta en $ARCHIVO_RECETAS y se omite.")
                else -> {
                    val producto: Producto? = crearPizza(filaPizza, receta)
                    if (producto == null) {
                        errores.add("Fila ${indice + 1} con tipo inválido en $ARCHIVO_PIZZAS: ${fila.joinToString(";")}")
                    } else if (producto in productos) {
                        errores.add("Fila ${indice + 1} duplicada en $ARCHIVO_PIZZAS: ${producto.nombre} (ya existe en el catálogo)")
                    } else {
                        productos.add(producto)
                    }
                }
            }
        }

        return CargaProductos(productos = productos.toList(), errores = errores.toList())
    }

    private fun cargarRecetas(errores: MutableList<String>): Map<String, Map<String, Int>> {
        val resultado: ResultadoLectura = lectorCsv.leer(ARCHIVO_RECETAS)
        errores.addAll(resultado.errores)
        val filasValidas: MutableList<FilaReceta> = mutableListOf()

        resultado.filas.forEachIndexed { indice: Int, fila: List<String> ->
            val filaReceta: FilaReceta? = convertirFilaReceta(fila)
            if (filaReceta != null) {
                filasValidas.add(filaReceta)
            } else {
                errores.add("Fila ${indice + 1} inválida en $ARCHIVO_RECETAS: ${fila.joinToString(";")}")
            }
        }

        return filasValidas
            .groupBy { filaReceta: FilaReceta -> filaReceta.pizza }
            .mapValues { entrada: Map.Entry<String, List<FilaReceta>> ->
                val receta: MutableMap<String, Int> = mutableMapOf()
                entrada.value.forEach { filaReceta: FilaReceta ->
                    if (receta.containsKey(filaReceta.ingrediente)) {
                        errores.add("Ingrediente repetido ${filaReceta.ingrediente} en la receta de ${entrada.key}")
                    } else {
                        receta[filaReceta.ingrediente] = filaReceta.cantidad
                    }
                }
                receta.toMap()
            }
    }

    /**
     * Crea la pizza según [FilaPizza.tipo], sin distinguir mayúsculas.
     * Retorna null si el tipo no es [TIPO_CLASICA] ni [TIPO_ESPECIAL].
     */
    private fun crearPizza(filaPizza: FilaPizza, receta: Map<String, Int>): Producto? =
        when (filaPizza.tipo.lowercase()) {
            TIPO_CLASICA.lowercase() ->
                PizzaClasica(nombre = filaPizza.nombre, precio = filaPizza.precio, ingredientes = receta)
            TIPO_ESPECIAL.lowercase() ->
                PizzaEspecial(nombre = filaPizza.nombre, precio = filaPizza.precio, ingredientes = receta)
            else -> null
        }

    private fun convertirFilaPizza(fila: List<String>): FilaPizza? {
        if (fila.size != COLUMNAS_PIZZAS) return null

        val nombre: String = fila[0]
        val precio: Double? = fila[1].toDoubleOrNull()
        val tipo: String = fila[2]

        return if (nombre.isNotEmpty() && precio != null && precio > 0) {
            FilaPizza(nombre = nombre, precio = precio, tipo = tipo)
        } else {
            null
        }
    }

    private fun convertirFilaReceta(fila: List<String>): FilaReceta? {
        if (fila.size != COLUMNAS_RECETAS) return null

        val pizza: String = fila[0]
        val ingrediente: String = fila[1]
        val cantidad: Int? = fila[2].toIntOrNull()

        return if (pizza.isNotEmpty() && ingrediente.isNotEmpty() && cantidad != null && cantidad > 0) {
            FilaReceta(pizza = pizza, ingrediente = ingrediente, cantidad = cantidad)
        } else {
            null
        }
    }

    private data class FilaPizza(
        val nombre: String,
        val precio: Double,
        val tipo: String
    )

    private data class FilaReceta(
        val pizza: String,
        val ingrediente: String,
        val cantidad: Int
    )

    private data class CargaProductos(
        val productos: List<Producto>,
        val errores: List<String>
    )

    private companion object {
        const val ARCHIVO_PIZZAS: String = "pizzas.csv"
        const val ARCHIVO_RECETAS: String = "recetas.csv"
        const val COLUMNAS_PIZZAS: Int = 3
        const val TIPO_CLASICA: String = "Clasica"
        const val TIPO_ESPECIAL: String = "Especial"
        const val COLUMNAS_RECETAS: Int = 3
    }
}
