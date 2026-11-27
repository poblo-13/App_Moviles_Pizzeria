
package com.example.pizzeriaapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzeriaapp.model.ConsultaVeterinaria
import com.example.pizzeriaapp.model.Dueno
import com.example.pizzeriaapp.model.Mascota
import com.example.pizzeriaapp.model.VeterinariaRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

// RESUMEN DE LA VETERINARIA
data class ResumenVeterinaria(
    val totalDuenos: Int = 0,
    val totalMascotas: Int = 0,
    val totalConsultas: Int = 0,
    val ultimoDueno: String = "Sin registros"
)

class VeterinariaViewModel : ViewModel() {

    private val repositorio = VeterinariaRepository()

    // LISTA DE DUEÑOS
    private val _duenos = MutableLiveData<List<Dueno>>(emptyList())
    val duenos: LiveData<List<Dueno>> = _duenos

    // LISTA DE MASCOTAS
    private val _mascotas = MutableLiveData<List<Mascota>>(emptyList())
    val mascotas: LiveData<List<Mascota>> = _mascotas

    // LISTA DE CONSULTAS
    private val _consultas =
        MutableLiveData<List<ConsultaVeterinaria>>(emptyList())

    val consultas: LiveData<List<ConsultaVeterinaria>> = _consultas

    // RESUMEN DINÁMICO
    private val _resumen = MutableLiveData(ResumenVeterinaria())
    val resumen: LiveData<ResumenVeterinaria> = _resumen

    // MENSAJES PARA EL USUARIO
    private val _mensaje = MutableLiveData("")
    val mensaje: LiveData<String> = _mensaje

    // ESTADO DEL INDICADOR DE PROGRESO
    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    // MENSAJE DURANTE LA CARGA
    private val _mensajeCarga = MutableLiveData("")
    val mensajeCarga: LiveData<String> = _mensajeCarga

    // REGISTRAR DUEÑO
    fun registrarDueno(
        nombre: String,
        telefono: String,
        correo: String,
        direccion: String,
        alFinalizar: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {

            val registrado = repositorio.registrarDueno(
                nombre,
                telefono,
                correo,
                direccion
            )

            _mensaje.value = if (registrado) {
                "Dueño registrado correctamente."
            } else {
                "No se pudo registrar al dueño. Revisa los datos o el correo duplicado."
            }

            actualizarDatos()

            // Informar a la pantalla si el registro fue exitoso.
            alFinalizar(registrado)
        }
    }


    // REGISTRAR MASCOTA
    fun registrarMascota(
        nombre: String,
        especie: String,
        edad: Int,
        duenoId: Int,
        alFinalizar: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {

            val registrada = repositorio.registrarMascota(
                nombre,
                especie,
                edad,
                duenoId
            )

            _mensaje.value = if (registrada) {
                "Mascota registrada correctamente."
            } else {
                "No se pudo registrar la mascota. Revisa los datos o si ya existe."
            }

            actualizarDatos()

            // Limpiar los campos solo después de un registro exitoso.
            alFinalizar(registrada)
        }
    }


    // REGISTRAR CONSULTA

    fun registrarConsulta(
        mascotaId: Int,
        motivo: String,
        alFinalizar: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {

            val registrada = repositorio.registrarConsulta(
                mascotaId,
                motivo
            )

            _mensaje.value = if (registrada) {
                "Consulta veterinaria registrada correctamente."
            } else {
                "No se pudo registrar la consulta. Revisa los datos."
            }

            actualizarDatos()

            // Informar si el registro fue exitoso.
            alFinalizar(registrada)
        }
    }

    fun actualizarResumenConProgreso() {

        // Evitar varias actualizaciones simultáneas.
        if (_cargando.value == true) {
            return
        }

        viewModelScope.launch {

            _cargando.value = true
            _mensajeCarga.value = "Actualizando registros..."

            try {
                // Tiempo de demostración para visualizar el indicador.
                delay(1200)

                // Consultar nuevamente los datos del repositorio.
                actualizarDatos()

            } finally {
                _cargando.value = false
                _mensajeCarga.value = ""
            }
        }
    }

    // ACTUALIZAR DATOS Y RESUMEN
    private fun actualizarDatos() {

        val listaDuenos = repositorio.obtenerDuenos()
        val listaMascotas = repositorio.obtenerMascotas()
        val listaConsultas = repositorio.obtenerConsultas()

        _duenos.value = listaDuenos
        _mascotas.value = listaMascotas
        _consultas.value = listaConsultas

        _resumen.value = ResumenVeterinaria(
            totalDuenos = listaDuenos.size,
            totalMascotas = listaMascotas.size,
            totalConsultas = listaConsultas.size,
            ultimoDueno = repositorio.obtenerUltimoDueno()
        )
    }
}
