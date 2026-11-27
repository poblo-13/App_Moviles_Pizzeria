package com.example.pizzeriaapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pizzeriaapp.model.Dueno
import com.example.pizzeriaapp.utils.Validaciones
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaVeterinaria(
    modifier: Modifier = Modifier,
    viewModel: VeterinariaViewModel = viewModel()
) {
    val resumen by viewModel.resumen.observeAsState(ResumenVeterinaria())

    val cargando by viewModel.cargando.observeAsState(false)
    val mensajeCarga by viewModel.mensajeCarga.observeAsState("")

    val duenos by viewModel.duenos.observeAsState(emptyList())
    val mascotas by viewModel.mascotas.observeAsState(emptyList())
    val mensaje by viewModel.mensaje.observeAsState("")

    var seccionActual by rememberSaveable { mutableStateOf("Inicio") }
    var seccionPendiente by rememberSaveable { mutableStateOf("Inicio") }
    var contenidoVisible by remember { mutableStateOf(false) }
    var menuAbierto by remember { mutableStateOf(false) }

    val estadoFormularios = rememberSaveableStateHolder()
    val foco = LocalFocusManager.current
    val desplazamiento = rememberScrollState()

    // Animación inicial y transiciones entre secciones.
    LaunchedEffect(seccionPendiente) {
        if (seccionPendiente != seccionActual) {
            contenidoVisible = false
            delay(300) // Espera a que termine el fade out (250 ms).
            seccionActual = seccionPendiente
        }
        contenidoVisible = true
    }

    LaunchedEffect(seccionActual) {
        desplazamiento.scrollTo(0)
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("GESTIÓN VETERINARIA") },
            actions = {
                Box {
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir menú"
                        )
                    }

                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Inicio") },
                            leadingIcon = {
                                Icon(Icons.Default.Home, contentDescription = null)
                            },
                            onClick = {
                                seccionPendiente = "Inicio"
                                menuAbierto = false
                                foco.clearFocus()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Dueños") },
                            leadingIcon = {
                                Icon(Icons.Default.People, contentDescription = null)
                            },
                            onClick = {
                                seccionPendiente = "Dueños"
                                menuAbierto = false
                                foco.clearFocus()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Mascotas") },
                            leadingIcon = {
                                Icon(Icons.Default.Pets, contentDescription = null)
                            },
                            onClick = {
                                seccionPendiente = "Mascotas"
                                menuAbierto = false
                                foco.clearFocus()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Consultas") },
                            leadingIcon = {
                                Icon(Icons.Default.MedicalServices, contentDescription = null)
                            },
                            onClick = {
                                seccionPendiente = "Consultas"
                                menuAbierto = false
                                foco.clearFocus()
                            }
                        )
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(desplazamiento)
                .padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = contenidoVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 350)),
                exit = fadeOut(animationSpec = tween(durationMillis = 250))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    estadoFormularios.SaveableStateProvider(key = seccionActual) {
                        when (seccionActual) {
                            "Inicio" -> {
                                Text(
                                    text = "Bienvenido al sistema de gestión veterinaria",
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                    text = "Resumen de registros",
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Button(
                                    onClick = {
                                        viewModel.actualizarResumenConProgreso()
                                    },
                                    enabled = !cargando,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Actualizar resumen")
                                }

                                if (cargando) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        CircularProgressIndicator()

                                        Text(
                                            text = mensajeCarga,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }

                                TarjetaResumen(
                                    titulo = "Dueños registrados",
                                    valor = resumen.totalDuenos.toString()
                                )

                                TarjetaResumen(
                                    titulo = "Mascotas registradas",
                                    valor = resumen.totalMascotas.toString()
                                )

                                TarjetaResumen(
                                    titulo = "Consultas veterinarias",
                                    valor = resumen.totalConsultas.toString()
                                )

                                HorizontalDivider()

                                Text(
                                    text = "Último dueño registrado",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(resumen.ultimoDueno)
                            }

                            "Dueños" -> {
                                SeccionDuenos(
                                    duenos = duenos,
                                    viewModel = viewModel,
                                    mensaje = mensaje
                                )
                            }

                            "Mascotas" -> {
                                FormularioMascota(
                                    duenos = duenos,
                                    viewModel = viewModel
                                )
                            }

                            "Consultas" -> {
                                FormularioConsulta(
                                    mascotas = mascotas,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SeccionDuenos(
    duenos: List<Dueno>,
    viewModel: VeterinariaViewModel,
    mensaje: String
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }

    val datosValidos =
        Validaciones.nombreValido(nombre) &&
                Validaciones.telefonoValido(telefono) &&
                Validaciones.correoValido(correo) &&
                Validaciones.direccionValida(direccion)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Registrar dueño",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            supportingText = { Text("Ejemplo: +56912345678") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.registrarDueno(
                    nombre = nombre,
                    telefono = telefono,
                    correo = correo,
                    direccion = direccion
                ) { registrado ->
                    if (registrado) {
                        nombre = ""
                        telefono = ""
                        correo = ""
                        direccion = ""
                    }
                }
            },
            enabled = datosValidos,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar dueño")
        }

        if (mensaje.isNotBlank()) {
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        HorizontalDivider()

        Text(
            text = "Dueños registrados",
            style = MaterialTheme.typography.titleLarge
        )

        if (duenos.isEmpty()) {
            Text("Todavía no existen dueños registrados.")
        } else {
            duenos.forEach { dueno ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = dueno.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Teléfono: ${dueno.telefono}")
                        Text("Correo: ${dueno.correo}")
                        Text("Dirección: ${dueno.direccion}")
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaResumen(titulo: String, valor: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
