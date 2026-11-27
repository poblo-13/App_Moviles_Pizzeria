
package com.example.pizzeriaapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pizzeriaapp.model.Mascota
import com.example.pizzeriaapp.utils.Validaciones

@Composable
fun FormularioConsulta(
    mascotas: List<Mascota>,
    viewModel: VeterinariaViewModel
) {
    var mascotaSeleccionadaId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    var motivo by rememberSaveable {
        mutableStateOf("")
    }

    var menuAbierto by remember {
        mutableStateOf(false)
    }

    var resultadoRegistro by rememberSaveable {
        mutableStateOf("")
    }

    val consultas by viewModel.consultas.observeAsState(
        emptyList()
    )

    val mascotaSeleccionada = mascotas.firstOrNull {
        it.id == mascotaSeleccionadaId
    }

    val datosValidos =
        mascotaSeleccionada != null &&
                Validaciones.motivoValido(motivo)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Registrar consulta veterinaria",
            style = MaterialTheme.typography.titleLarge
        )

        if (mascotas.isEmpty()) {
            Text(
                "Primero debes registrar una mascota."
            )
        }

        Text(
            text = "Seleccionar mascota",
            style = MaterialTheme.typography.titleMedium
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedButton(
                onClick = {
                    menuAbierto = true
                },
                enabled = mascotas.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    mascotaSeleccionada?.nombre
                        ?: "Elegir mascota"
                )
            }

            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = {
                    menuAbierto = false
                }
            ) {
                mascotas.forEach { mascota ->

                    DropdownMenuItem(
                        text = {
                            Text(
                                "${mascota.nombre} - ${mascota.especie}"
                            )
                        },
                        onClick = {
                            mascotaSeleccionadaId = mascota.id
                            menuAbierto = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = motivo,
            onValueChange = {
                motivo = it
                resultadoRegistro = ""
            },
            label = {
                Text("Motivo de la consulta")
            },
            placeholder = {
                Text("Ejemplo: Control veterinario general")
            },
            minLines = 2,
            maxLines = 4,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {

                val mascotaValida = mascotaSeleccionada

                if (mascotaValida != null) {

                    viewModel.registrarConsulta(
                        mascotaId = mascotaValida.id,
                        motivo = motivo
                    ) { registrada ->

                        resultadoRegistro = if (registrada) {
                            "Consulta registrada correctamente."
                        } else {
                            "No se pudo registrar la consulta."
                        }

                        // Limpiar solo cuando el registro sea exitoso.
                        if (registrada) {
                            mascotaSeleccionadaId = null
                            motivo = ""
                        }
                    }
                }
            },
            enabled = datosValidos,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar consulta")
        }

        if (resultadoRegistro.isNotBlank()) {
            Text(
                text = resultadoRegistro,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        HorizontalDivider()

        Text(
            text = "Consultas registradas",
            style = MaterialTheme.typography.titleLarge
        )

        if (consultas.isEmpty()) {

            Text(
                "Todavía no existen consultas veterinarias."
            )

        } else {

            consultas.forEach { consulta ->

                val nombreMascota = mascotas.firstOrNull {
                    it.id == consulta.mascotaId
                }?.nombre ?: "Mascota no encontrada"

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(4.dp)
                    ) {

                        Text(
                            text = "Consulta #${consulta.id}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("Mascota: $nombreMascota")
                        Text("Motivo: ${consulta.motivo}")
                        Text("Fecha: ${consulta.fecha}")
                        Text("Estado: ${consulta.estado}")
                    }
                }
            }
        }
    }
}
