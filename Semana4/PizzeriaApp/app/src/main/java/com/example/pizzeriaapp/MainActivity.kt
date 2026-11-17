
package com.example.pizzeriaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pizzeriaapp.model.Cliente
import com.example.pizzeriaapp.model.DetallePedido
import com.example.pizzeriaapp.model.Pedido
import com.example.pizzeriaapp.model.PizzaCuatroQuesos
import com.example.pizzeriaapp.model.PizzaPepperoni
import com.example.pizzeriaapp.model.Producto
import com.example.pizzeriaapp.model.Promocionable
import com.example.pizzeriaapp.ui.theme.PizzeriaAppTheme
import java.time.LocalDate
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PizzeriaAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PantallaPizzeria(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}

@Composable
fun PantallaPizzeria(modifier: Modifier = Modifier) {

    val catalogo = remember {
        listOf<Producto>(
            PizzaPepperoni(),
            PizzaCuatroQuesos(),
            Producto(
                "Napolitana",
                8990,
                mapOf(
                    "Masa" to 1,
                    "Queso" to 2,
                    "Salsa de tomate" to 1,
                    "Tomate" to 2
                )
            ),
            Producto(
                "Todas las Carnes",
                14990,
                mapOf(
                    "Masa" to 1,
                    "Queso" to 2,
                    "Salsa de tomate" to 1,
                    "Carne" to 3
                )
            ),
            Producto(
                "Pollo al Pesto",
                13990,
                mapOf(
                    "Masa" to 1,
                    "Queso" to 2,
                    "Pollo" to 2,
                    "Pesto" to 1
                )
            )
        ).distinct()
    }

    var productoSeleccionado by remember {
        mutableStateOf<Producto?>(null)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Greeting(
            name = "Bienvenidos a la mejor pizzería de la ciudad"
        )

        HorizontalDivider()

        Text("PIZZERÍA DI PABLO")
        Text("Nuestro menú de pizzas")

        catalogo.forEach { producto ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(producto.nombre)
                    Text(formatoPesos(producto.precio))

                    val promocion = producto.javaClass
                        .getAnnotation(Promocionable::class.java)

                    if (promocion != null) {
                        Text("Promoción: ${promocion.descuento}%")
                    }
                }

                Button(
                    onClick = {
                        productoSeleccionado = producto
                    }
                ) {
                    Text("Probar")
                }
            }

            HorizontalDivider()
        }

        productoSeleccionado?.let { producto ->

            val clientePrueba = Cliente(
                "Cliente de prueba",
                "+56 (9) 1234-5678",
                "prueba@ejemplo.com",
                "Domicilio de prueba"
            )

            val pedidoPrueba = Pedido(
                id = 0,
                cliente = clientePrueba,
                productos = listOf(
                    DetallePedido(producto, 1)
                ),
                zonaEntrega = "Centro",
                fechaPedido = LocalDate.now()
            )

            Text("RESUMEN DE PEDIDO DE PRUEBA")
            Text("Pizza: ${producto.nombre}")
            Text("Cantidad: 1")
            Text("Fecha: ${pedidoPrueba.fechaPedido}")
            Text("Subtotal: ${formatoPesos(pedidoPrueba.calcularSubtotal())}")
            Text("Descuento: ${formatoPesos(pedidoPrueba.calcularDescuentoPromocional())}")
            Text("IVA (19%): ${formatoPesos(pedidoPrueba.calcularImpuestos())}")
            Text("Total: ${formatoPesos(pedidoPrueba.calcularTotal())}")

            Text("Simulación: no se registra una venta.")
        }
    }
}

fun formatoPesos(valor: Int): String {
    return "\$" + String.format(
        Locale.US,
        "%,d",
        valor
    ).replace(",", ".")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PizzeriaAppTheme {
        PantallaPizzeria()
    }
}
