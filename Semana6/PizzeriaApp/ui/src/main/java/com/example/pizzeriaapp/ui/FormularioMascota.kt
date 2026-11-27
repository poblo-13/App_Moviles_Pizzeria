
package com.example.pizzeriaapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pizzeriaapp.model.Dueno
import com.example.pizzeriaapp.utils.Validaciones

@Composable
fun FormularioMascota(
    duenos: List<Dueno>,
    viewModel: VeterinariaViewModel
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var especie by rememberSaveable { mutableStateOf("") }
    var edadTexto by rememberSaveable { mutableStateOf("") }

    var duenoSeleccionadoId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    var menuAbierto by remember {
        mutableStateOf(false)
    }

    val mascotas by viewModel.mascotas.observeAsState(
        emptyList()
    )

    val duenoSeleccionado = duenos.firstOrNull {
        it.id == duenoSeleccionadoId
    }

    val edad = edadTexto.toIntOrNull()

    val datosValidos =
        Validaciones.nombreValido(nombre) &&
                especie.isNotBlank() &&
                edad != null &&
                Validaciones.edadMascotaValida(edad) &&
                duenoSeleccionado != null

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Registrar mascota",
            style = MaterialTheme.typography.titleLarge
        )

        if (duenos.isEmpty()) {
            Text(
                "Primero debes registrar un dueño."
            )
        }

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de la mascota") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = especie,
            onValueChange = { especie = it },
            label = { Text("Especie") },
            placeholder = { Text("Ejemplo: Perro o Gato") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = edadTexto,
            onValueChange = { edadTexto = it },
            label = { Text("Edad en años") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Seleccionar dueño",
            style = MaterialTheme.typography.titleMedium
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedButton(
                onClick = { menuAbierto = true },
                enabled = duenos.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    duenoSeleccionado?.nombre
                        ?: "Elegir dueño"
                )
            }

            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = {
                    menuAbierto = false
                }
            ) {
                duenos.forEach { dueno ->

                    DropdownMenuItem(
                        text = {
                            Text(dueno.nombre)
                        },
                        onClick = {
                            duenoSeleccionadoId = dueno.id
                            menuAbierto = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {

                val edadValida = edad
                val duenoValido = duenoSeleccionado

                if (edadValida != null &&
                    duenoValido != null
                ) {
                    viewModel.registrarMascota(
                        nombre = nombre,
                        especie = especie,
                        edad = edadValida,
                        duenoId = duenoValido.id
                    ) { registrada ->

                        if (registrada) {
                            nombre = ""
                            especie = ""
                            edadTexto = ""
                            duenoSeleccionadoId = null
                        }
                    }
                }
            },
            enabled = datosValidos,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar mascota")
        }

        HorizontalDivider()

        Text(
            text = "Mascotas registradas",
            style = MaterialTheme.typography.titleLarge
        )

        if (mascotas.isEmpty()) {
            Text("Todavía no existen mascotas registradas.")
        } else {
            mascotas.forEach { mascota ->

                val nombreDueno = duenos.firstOrNull {
                    it.id == mascota.duenoId
                }?.nombre ?: "Dueño no encontrado"

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            mascota.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("Especie: ${mascota.especie}")
                        Text("Edad: ${mascota.edad} años")
                        Text("Dueño: $nombreDueno")
                    }
                }
            }
        }
    }
}
