
package com.example.pizzeriaapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.pizzeriaapp.model.Cliente
import com.example.pizzeriaapp.model.DetallePedido
import com.example.pizzeriaapp.model.Pedido
import com.example.pizzeriaapp.model.PizzaPepperoni

@Composable
fun PantallaResumen(
    modifier: Modifier = Modifier,
    viewModel: PizzeriaViewModel = viewModel()
) {
    val resumen by viewModel.resumen.observeAsState(
        ResumenPizzeria()
    )

    val clientes by viewModel.clientes.observeAsState(
        emptyList()
    )

    val pedidos by viewModel.pedidos.observeAsState(
        emptyList()
    )

    val mensaje by viewModel.mensaje.observeAsState("")

    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }

    val datosValidos = nombre.isNotBlank() &&
            telefono.isNotBlank() &&
            correo.contains("@") &&
            direccion.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "PIZZERÍA DI PABLO",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Resumen de registros - Semana 5")

        HorizontalDivider()

        Text(
            text = "Clientes registrados: ${resumen.totalClientes}",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Pedidos realizados: ${resumen.totalPedidos}",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Último cliente: ${resumen.ultimoCliente}",
            style = MaterialTheme.typography.titleMedium
        )

        HorizontalDivider()

        Text(
            text = "Registrar cliente",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
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
            enabled = datosValidos,
            onClick = {
                viewModel.registrarCliente(
                    Cliente(
                        nombre.trim(),
                        telefono.trim(),
                        correo.trim(),
                        direccion.trim()
                    )
                )

                nombre = ""
                telefono = ""
                correo = ""
                direccion = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar cliente")
        }

        if (mensaje.isNotBlank()) {
            Text(mensaje)
        }

        HorizontalDivider()

        Text(
            text = "Registrar pedido de prueba",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            "Se registrará una pizza Pepperoni para el último cliente."
        )

        Button(
            enabled = clientes.isNotEmpty(),
            onClick = {
                val cliente = clientes.lastOrNull()

                if (cliente != null) {
                    val nuevoId = (pedidos.maxOfOrNull {
                        it.id
                    } ?: 0) + 1

                    val pedido = Pedido(
                        id = nuevoId,
                        cliente = cliente,
                        productos = listOf(
                            DetallePedido(
                                PizzaPepperoni(),
                                1
                            )
                        ),
                        zonaEntrega = "Centro"
                    )

                    viewModel.registrarPedido(pedido)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar pedido")
        }

        HorizontalDivider()

        Text(
            text = "Clientes",
            style = MaterialTheme.typography.titleLarge
        )

        clientes.forEach { cliente ->
            Text("• ${cliente.nombre}")
        }

        Text(
            text = "Pedidos",
            style = MaterialTheme.typography.titleLarge
        )

        pedidos.forEach { pedido ->
            Text(
                "• Pedido #${pedido.id} - ${pedido.cliente.nombre}"
            )
        }
    }
}
