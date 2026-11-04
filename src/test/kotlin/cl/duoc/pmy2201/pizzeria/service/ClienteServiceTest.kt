package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.model.Cliente
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ClienteServiceTest {

    private val ana: Cliente = Cliente(
        nombre = "Ana Pérez",
        telefono = "+56931717552",
        correo = "ana@example.com",
        direccion = "Av. Siempre Viva 123"
    )

    @Test
    fun registrarDosVecesElMismoClienteSoloLoAgregaUnaVez() {
        val clienteService: ClienteService = ClienteService()

        val primerRegistro: Boolean = clienteService.registrar(ana)
        val segundoRegistro: Boolean = clienteService.registrar(ana)

        assertTrue(primerRegistro)
        assertFalse(segundoRegistro)
        assertEquals(1, clienteService.clientesDelDia().size)
    }

    @Test
    fun buscarEncuentraAlClienteRegistrado() {
        val clienteService: ClienteService = ClienteService()
        clienteService.registrar(ana)

        val encontrado: Cliente? = clienteService.buscar(nombre = "ana pérez", correo = "ANA@example.com")

        assertSame(ana, encontrado)
    }
}
