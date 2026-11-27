
package com.example.pizzeriaapp.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacionesTest {

    @Test
    fun nombreValido() {
        assertTrue(Validaciones.nombreValido("Juan Pérez"))
        assertFalse(Validaciones.nombreValido(""))
        assertFalse(Validaciones.nombreValido("A"))
    }

    @Test
    fun correoValido() {
        assertTrue(
            Validaciones.correoValido("juan@ejemplo.com")
        )

        assertFalse(
            Validaciones.correoValido("juan@")
        )

        assertFalse(
            Validaciones.correoValido("correo-invalido")
        )
    }

    @Test
    fun telefonoValido() {
        assertTrue(
            Validaciones.telefonoValido("+56912345678")
        )

        assertFalse(
            Validaciones.telefonoValido("123")
        )
    }

    @Test
    fun edadMascotaValida() {
        assertTrue(Validaciones.edadMascotaValida(3))
        assertTrue(Validaciones.edadMascotaValida(0))
        assertFalse(Validaciones.edadMascotaValida(-1))
    }

    @Test
    fun motivoConsultaValido() {
        assertTrue(
            Validaciones.motivoValido("Control general")
        )

        assertFalse(
            Validaciones.motivoValido("Mal")
        )
    }
}
