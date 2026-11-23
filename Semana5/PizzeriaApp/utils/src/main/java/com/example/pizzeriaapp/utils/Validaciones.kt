package com.example.pizzeriaapp.utils

object Validaciones {
    fun nombreValido(nombre: String): Boolean = nombre.trim().isNotEmpty()
    fun correoValido(correo: String): Boolean {
        val limpio = correo.trim()
        return limpio.contains('@') && limpio.substringAfter('@').contains('.')
    }
}