package cl.duoc.pmy2201.pizzeria.data

/**
 * Repositorio de inventario que lee `inventario.csv` una sola vez y mantiene los datos en memoria.
 */
class InventarioRepositoryCsv(
    private val lectorCsv: LectorCsv = LectorCsv()
) : InventarioRepository {

    private val carga: CargaInventario by lazy { cargar() }

    override fun obtenerInventario(): MutableMap<String, Int> = carga.inventario

    override fun unidadDe(ingrediente: String): String? = carga.unidades[ingrediente]

    override fun erroresDeCarga(): List<String> = carga.errores

    private fun cargar(): CargaInventario {
        val resultado: ResultadoLectura = lectorCsv.leer(NOMBRE_ARCHIVO)
        val inventario: MutableMap<String, Int> = mutableMapOf()
        val unidades: MutableMap<String, String> = mutableMapOf()
        val errores: MutableList<String> = resultado.errores.toMutableList()

        resultado.filas.forEachIndexed { indice: Int, fila: List<String> ->
            val filaInventario: FilaInventario? = convertirFila(fila)
            when {
                filaInventario == null ->
                    errores.add("Fila ${indice + 1} inválida en $NOMBRE_ARCHIVO: ${fila.joinToString(";")}")
                inventario.containsKey(filaInventario.ingrediente) ->
                    errores.add("Ingrediente repetido ${filaInventario.ingrediente} en $NOMBRE_ARCHIVO (fila ${indice + 1})")
                else -> {
                    inventario[filaInventario.ingrediente] = filaInventario.cantidad
                    unidades[filaInventario.ingrediente] = filaInventario.unidad
                }
            }
        }

        return CargaInventario(inventario = inventario, unidades = unidades.toMap(), errores = errores.toList())
    }

    private fun convertirFila(fila: List<String>): FilaInventario? {
        if (fila.size != CANTIDAD_COLUMNAS) return null

        val ingrediente: String = fila[0]
        val cantidad: Int? = fila[1].toIntOrNull()
        val unidad: String = fila[2]

        return if (ingrediente.isNotEmpty() && cantidad != null && cantidad >= 0 && unidad in UNIDADES_PERMITIDAS) {
            FilaInventario(ingrediente = ingrediente, cantidad = cantidad, unidad = unidad)
        } else {
            null
        }
    }

    private data class FilaInventario(
        val ingrediente: String,
        val cantidad: Int,
        val unidad: String
    )

    private data class CargaInventario(
        val inventario: MutableMap<String, Int>,
        val unidades: Map<String, String>,
        val errores: List<String>
    )

    private companion object {
        const val NOMBRE_ARCHIVO: String = "inventario.csv"
        const val CANTIDAD_COLUMNAS: Int = 3
        val UNIDADES_PERMITIDAS: Set<String> = setOf("g", "unidades")
    }
}
