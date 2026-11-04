package cl.duoc.pmy2201.pizzeria.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ClienteTest {

    private fun crearCliente(
        nombre: String = "Ana Pérez",
        correo: String? = "ana@example.com",
        telefono: String = "+56931717552",
        direccion: String = "Av. Siempre Viva 123"
    ): Cliente = Cliente(nombre = nombre, telefono = telefono, correo = correo, direccion = direccion)

    @Test
    fun mismoNombreYCorreoConDistintaMayusculaSonIgualesYTienenMismoHash() {
        val original: Cliente = crearCliente(nombre = "Ana Pérez", correo = "ana@example.com")
        val variante: Cliente = crearCliente(nombre = "  ANA PÉREZ ", correo = "ANA@Example.com")

        assertEquals(original, variante)
        assertEquals(original.hashCode(), variante.hashCode())
    }

    @Test
    fun distintoCorreoNoSonIguales() {
        val original: Cliente = crearCliente(correo = "ana@example.com")
        val otroCorreo: Cliente = crearCliente(correo = "ana.perez@example.com")

        assertNotEquals(original, otroCorreo)
    }

    @Test
    fun distintaDireccionYTelefonoSonIguales() {
        val original: Cliente = crearCliente(telefono = "+56931717552", direccion = "Av. Siempre Viva 123")
        val otrosDatos: Cliente = crearCliente(telefono = "+56223456789", direccion = "Calle Falsa 456")

        assertEquals(original, otrosDatos)
    }

    @Test
    fun setConDosClientesIgualesQuedaConUnSoloElemento() {
        val clientes: Set<Cliente> = setOf(
            crearCliente(nombre = "Ana Pérez"),
            crearCliente(nombre = "ana pérez")
        )

        assertEquals(1, clientes.size)
    }
}
