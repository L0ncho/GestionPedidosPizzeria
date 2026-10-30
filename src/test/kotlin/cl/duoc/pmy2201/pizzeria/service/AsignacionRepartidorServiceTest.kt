package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.data.RepartidorRepository
import cl.duoc.pmy2201.pizzeria.model.Repartidor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AsignacionRepartidorServiceTest {

    private class RepartidorRepositoryEnMemoria(
        private val repartidores: List<Repartidor>
    ) : RepartidorRepository {
        override fun obtenerTodos(): List<Repartidor> = repartidores
        override fun erroresDeCarga(): List<String> = emptyList()
    }

    private fun crearRepartidores(): List<Repartidor> = listOf(
        Repartidor(id = 1, nombre = "Matías Rojas", telefono = "+56911111111", disponible = true, distanciaKm = 3.5),
        Repartidor(id = 2, nombre = "Camila Fuentes", telefono = "+56922222222", disponible = false, distanciaKm = 1.2),
        Repartidor(id = 3, nombre = "Diego Soto", telefono = "+56933333333", disponible = true, distanciaKm = 2.0)
    )

    @Test
    fun asignaAlDisponibleMasCercanoYLoMarcaComoNoDisponible() {
        val repartidores: List<Repartidor> = crearRepartidores()
        val servicio: AsignacionRepartidorService = AsignacionRepartidorService(RepartidorRepositoryEnMemoria(repartidores))

        val asignado: Repartidor? = servicio.asignar()

        assertNotNull(asignado)
        assertEquals(3, asignado.id)
        assertFalse(asignado.disponible)
        assertFalse(repartidores[2].disponible)
    }

    @Test
    fun retornaNullSiNoQuedaNingunoDisponible() {
        val repartidores: List<Repartidor> = crearRepartidores()
        val servicio: AsignacionRepartidorService = AsignacionRepartidorService(RepartidorRepositoryEnMemoria(repartidores))

        servicio.asignar()
        servicio.asignar()
        val terceraAsignacion: Repartidor? = servicio.asignar()

        assertNull(terceraAsignacion)
    }
}
