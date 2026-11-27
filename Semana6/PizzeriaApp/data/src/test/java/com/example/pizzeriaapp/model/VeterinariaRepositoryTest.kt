
package com.example.pizzeriaapp.model

import org.junit.Assert.*
import org.junit.Test

class VeterinariaRepositoryTest {

    private fun crearRepositorioConDueno(): VeterinariaRepository {
        val repositorio = VeterinariaRepository()

        repositorio.registrarDueno(
            "Juan Pérez",
            "+56912345678",
            "juan@ejemplo.com",
            "Concepción 123"
        )

        return repositorio
    }

    @Test
    fun registrarDuenoCorrectamente() {
        val repositorio = crearRepositorioConDueno()

        assertEquals(1, repositorio.obtenerDuenos().size)
        assertEquals("Juan Pérez", repositorio.obtenerUltimoDueno())
    }

    @Test
    fun rechazarDuenoDuplicado() {
        val repositorio = crearRepositorioConDueno()

        val resultado = repositorio.registrarDueno(
            "Juan Pérez",
            "+56912345678",
            "JUAN@EJEMPLO.COM",
            "Concepción 123"
        )

        assertFalse(resultado)
        assertEquals(1, repositorio.obtenerDuenos().size)
    }

    @Test
    fun registrarMascotaConDuenoExistente() {
        val repositorio = crearRepositorioConDueno()

        val resultado = repositorio.registrarMascota(
            "Luna",
            "Gato",
            3,
            1
        )

        assertTrue(resultado)
        assertEquals(1, repositorio.obtenerMascotas().size)
        assertEquals(1, repositorio.obtenerMascotas()[0].duenoId)
    }

    @Test
    fun rechazarMascotaSinDueno() {
        val repositorio = VeterinariaRepository()

        val resultado = repositorio.registrarMascota(
            "Luna",
            "Gato",
            3,
            999
        )

        assertFalse(resultado)
        assertTrue(repositorio.obtenerMascotas().isEmpty())
    }

    @Test
    fun rechazarMascotaDuplicada() {
        val repositorio = crearRepositorioConDueno()

        assertTrue(
            repositorio.registrarMascota("Luna", "Gato", 3, 1)
        )

        assertFalse(
            repositorio.registrarMascota("Luna", "Gato", 3, 1)
        )

        assertEquals(1, repositorio.obtenerMascotas().size)
    }

    @Test
    fun registrarConsultaDeMascotaExistente() {
        val repositorio = crearRepositorioConDueno()

        repositorio.registrarMascota("Luna", "Gato", 3, 1)

        val resultado = repositorio.registrarConsulta(
            1,
            "Control veterinario general"
        )

        assertTrue(resultado)
        assertEquals(1, repositorio.obtenerConsultas().size)
        assertEquals(1, repositorio.obtenerConsultas()[0].id)
    }

    @Test
    fun rechazarConsultaSinMascota() {
        val repositorio = VeterinariaRepository()

        val resultado = repositorio.registrarConsulta(
            999,
            "Control veterinario general"
        )

        assertFalse(resultado)
        assertTrue(repositorio.obtenerConsultas().isEmpty())
    }
}
