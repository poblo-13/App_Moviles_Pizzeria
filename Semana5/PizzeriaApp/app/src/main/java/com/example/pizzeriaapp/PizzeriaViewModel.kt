
package com.example.pizzeriaapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzeriaapp.model.Cliente
import com.example.pizzeriaapp.model.Pedido
import com.example.pizzeriaapp.model.PizzeriaRepository
import kotlinx.coroutines.launch

// INFORMACIÓN QUE MOSTRARÁ LA PANTALLA INICIAL
data class ResumenPizzeria(
    val totalClientes: Int = 0,
    val totalPedidos: Int = 0,
    val ultimoCliente: String = "Sin registros"
)

class PizzeriaViewModel : ViewModel() {

    private val repositorio = PizzeriaRepository()

    // RESUMEN OBSERVABLE
    private val _resumen = MutableLiveData(ResumenPizzeria())
    val resumen: LiveData<ResumenPizzeria> = _resumen

    // LISTA DE CLIENTES
    private val _clientes = MutableLiveData<List<Cliente>>(emptyList())
    val clientes: LiveData<List<Cliente>> = _clientes

    // LISTA DE PEDIDOS
    private val _pedidos = MutableLiveData<List<Pedido>>(emptyList())
    val pedidos: LiveData<List<Pedido>> = _pedidos

    // MENSAJES DE RESULTADO
    private val _mensaje = MutableLiveData("")
    val mensaje: LiveData<String> = _mensaje

    // REGISTRAR CLIENTES
    fun registrarCliente(cliente: Cliente) {
        viewModelScope.launch {
            val registrado = repositorio.registrarCliente(cliente)

            _mensaje.value = if (registrado) {
                "Cliente registrado correctamente."
            } else {
                "El cliente ya se encuentra registrado."
            }

            actualizarDatos()
        }
    }

    // REGISTRAR PEDIDOS
    fun registrarPedido(pedido: Pedido) {
        viewModelScope.launch {
            val registrado = repositorio.registrarPedido(pedido)

            _mensaje.value = if (registrado) {
                "Pedido registrado correctamente."
            } else {
                "No se pudo registrar el pedido."
            }

            actualizarDatos()
        }
    }

    // SINCRONIZAR DATOS CON LA INTERFAZ
    private fun actualizarDatos() {

        val listaClientes = repositorio.obtenerClientes()
        val listaPedidos = repositorio.obtenerPedidos()

        _clientes.value = listaClientes
        _pedidos.value = listaPedidos

        _resumen.value = ResumenPizzeria(
            totalClientes = listaClientes.size,
            totalPedidos = listaPedidos.size,
            ultimoCliente = repositorio.obtenerUltimoCliente()
        )
    }
}
