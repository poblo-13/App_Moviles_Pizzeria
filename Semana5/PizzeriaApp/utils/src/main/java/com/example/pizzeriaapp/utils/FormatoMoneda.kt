package com.example.pizzeriaapp.utils

import java.util.Locale

fun formatoPesos(valor: Int): String {
    return "\$" + String.format(Locale.US, "%,d", valor).replace(",", ".")
}