
package com.example.pizzeriaapp.model

import com.example.pizzeriaapp.utils.Validaciones

class VeterinariaRepository {

    private val duenos = mutableListOf<Dueno>()
    private val mascotas = mutableListOf<Mascota>()
    private val consultas = mutableListOf<ConsultaVeterinaria>()

    // REGISTRAR DUEÑO
    fun registrarDueno(
        nombre: String,
        telefono: String,
        correo: String,
        direccion: String
    ): Boolean {

        if (!Validaciones.nombreValido(nombre) ||
            !Validaciones.telefonoValido(telefono) ||
            !Validaciones.correoValido(correo) ||
            !Validaciones.direccionValida(direccion)
        ) {
            return false
        }

        val correoNormalizado = correo.trim().lowercase()

        // Evitar dueños duplicados por correo.
        if (duenos.any {
                it.correo.equals(correoNormalizado, ignoreCase = true)
            }
        ) {
            return false
        }

        val nuevoId = (duenos.maxOfOrNull { it.id } ?: 0) + 1

        duenos.add(
            Dueno(
                id = nuevoId,
                nombre = nombre.trim(),
                telefono = telefono.trim(),
                correo = correoNormalizado,
                direccion = direccion.trim()
            )
        )

        return true
    }

    // REGISTRAR MASCOTA
    fun registrarMascota(
        nombre: String,
        especie: String,
        edad: Int,
        duenoId: Int
    ): Boolean {

        if (!Validaciones.nombreValido(nombre) ||
            especie.isBlank() ||
            !Validaciones.edadMascotaValida(edad)
        ) {
            return false
        }

        // Comprobar que el dueño existe.
        if (duenos.none { it.id == duenoId }) {
            return false
        }

        // Evitar duplicados según los datos disponibles.
        val duplicada = mascotas.any {
            it.duenoId == duenoId &&
                    it.nombre.equals(nombre.trim(), ignoreCase = true) &&
                    it.especie.equals(especie.trim(), ignoreCase = true) &&
                    it.edad == edad
        }

        if (duplicada) return false

        val nuevoId = (mascotas.maxOfOrNull { it.id } ?: 0) + 1

        mascotas.add(
            Mascota(
                id = nuevoId,
                nombre = nombre.trim(),
                especie = especie.trim(),
                edad = edad,
                duenoId = duenoId
            )
        )

        return true
    }

    // REGISTRAR CONSULTA VETERINARIA
    fun registrarConsulta(
        mascotaId: Int,
        motivo: String
    ): Boolean {

        if (!Validaciones.motivoValido(motivo)) {
            return false
        }

        // Comprobar que la mascota existe.
        if (mascotas.none { it.id == mascotaId }) {
            return false
        }

        val nuevoId = (consultas.maxOfOrNull { it.id } ?: 0) + 1

        consultas.add(
            ConsultaVeterinaria(
                id = nuevoId,
                mascotaId = mascotaId,
                motivo = motivo.trim()
            )
        )

        return true
    }

    // CONSULTAR DATOS
    fun obtenerDuenos(): List<Dueno> = duenos.toList()

    fun obtenerMascotas(): List<Mascota> = mascotas.toList()

    fun obtenerConsultas(): List<ConsultaVeterinaria> =
        consultas.toList()

    fun obtenerUltimoDueno(): String =
        duenos.lastOrNull()?.nombre ?: "Sin registros"
}
