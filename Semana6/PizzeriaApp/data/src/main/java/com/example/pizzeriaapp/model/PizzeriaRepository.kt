
package com.example.pizzeriaapp.model

class PizzeriaRepository {

    private val clientes = mutableListOf<Cliente>()
    private val pedidos = mutableListOf<Pedido>()

    // Registrar cliente sin duplicados
    fun registrarCliente(cliente: Cliente): Boolean {
        if (clientes.contains(cliente)) {
            return false
        }

        clientes.add(cliente)
        return true
    }

    // Registrar pedido
    fun registrarPedido(pedido: Pedido): Boolean {
        if (pedidos.any { it.id == pedido.id }) {
            return false
        }

        if (clientes.none { it == pedido.cliente }) {
            return false
        }

        pedidos.add(pedido)
        return true
    }

    // Obtener clientes registrados
    fun obtenerClientes(): List<Cliente> {
        return clientes.toList()
    }

    // Obtener pedidos registrados
    fun obtenerPedidos(): List<Pedido> {
        return pedidos.toList()
    }

    // Obtener el último cliente
    fun obtenerUltimoCliente(): String {
        return clientes.lastOrNull()?.nombre
            ?: "Sin registros"
    }
}
