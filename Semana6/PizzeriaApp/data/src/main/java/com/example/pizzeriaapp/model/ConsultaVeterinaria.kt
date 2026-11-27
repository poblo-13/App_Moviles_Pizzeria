
package com.example.pizzeriaapp.model

import java.time.LocalDate

data class ConsultaVeterinaria(
    val id: Int,
    val mascotaId: Int,
    val motivo: String,
    val fecha: LocalDate = LocalDate.now(),
    val estado: String = "Pendiente"
)
