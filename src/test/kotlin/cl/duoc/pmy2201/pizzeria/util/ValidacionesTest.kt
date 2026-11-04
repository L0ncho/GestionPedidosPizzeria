package cl.duoc.pmy2201.pizzeria.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidacionesTest {

    @Test
    fun celularSeFormateaConPrimerDigitoEntreParentesis() {
        val formateado: String = formatearTelefono("+56931717552")

        assertEquals("+56 (9) 3171-7552", formateado)
    }

    @Test
    fun fijoSeFormateaConPrimerDigitoEntreParentesis() {
        val formateado: String = formatearTelefono("+56223456789")

        assertEquals("+56 (2) 2345-6789", formateado)
    }

    @Test
    fun textoQueNoCalzaConElPatronSeRetornaSinCambios() {
        val formateado: String = formatearTelefono("12345")

        assertEquals("12345", formateado)
    }

    @Test
    fun limitesDelRangoSonCantidadesValidas() {
        val minimoEsValido: Boolean = esCantidadValida(1)
        val maximoEsValido: Boolean = esCantidadValida(100)

        assertTrue(minimoEsValido)
        assertTrue(maximoEsValido)
    }

    @Test
    fun valoresFueraDelRangoNoSonCantidadesValidas() {
        val ceroEsValido: Boolean = esCantidadValida(0)
        val cientoUnoEsValido: Boolean = esCantidadValida(101)

        assertFalse(ceroEsValido)
        assertFalse(cientoUnoEsValido)
    }
}
