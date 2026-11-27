
package com.example.pizzeriaapp.utils

object Validaciones {

    // VALIDAR NOMBRE
    fun nombreValido(nombre: String): Boolean {
        return nombre.trim().length >= 2
    }

    // VALIDAR CORREO ELECTRÓNICO
    fun correoValido(correo: String): Boolean {
        val limpio = correo.trim()

        val patron = Regex(
            """^[A-Z0-9._%+-]+@[A-Z0-9-]+(\.[A-Z0-9-]+)+$""",
            RegexOption.IGNORE_CASE
        )

        return limpio.length <= 254 &&
                patron.matches(limpio)
    }

    // VALIDAR TELÉFONO CHILENO
    fun telefonoValido(telefono: String): Boolean {
        val limpio = telefono.trim()

        val formatoPermitido = Regex("""^\+?[0-9()\s-]+$""")

        if (!formatoPermitido.matches(limpio)) {
            return false
        }

        val digitos = limpio.filter { it.isDigit() }

        return when {
            digitos.length == 9 ->
                digitos.first() in '2'..'9'

            digitos.length == 11 &&
                    digitos.startsWith("56") ->
                digitos[2] in '2'..'9'

            else -> false
        }
    }

    // VALIDAR DIRECCIÓN
    fun direccionValida(direccion: String): Boolean {
        return direccion.trim().length >= 5
    }

    // VALIDAR EDAD DE MASCOTA
    fun edadMascotaValida(edad: Int): Boolean {
        return edad >= 0
    }

    // VALIDAR MOTIVO DE CONSULTA
    fun motivoValido(motivo: String): Boolean {
        return motivo.trim().length >= 5
    }
}
