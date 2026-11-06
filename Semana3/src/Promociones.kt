
import java.time.LocalDate
import kotlin.math.roundToInt

object Promociones {

    private val fechaInicio = LocalDate.of(2026, 11, 1)
    private val fechaTermino = LocalDate.of(2026, 11, 30)

    private const val DESCUENTO_PROMOCIONAL = 5

    // VERIFICAR SI LA FECHA ESTÁ DENTRO DEL RANGO
    fun estaVigente(fecha: LocalDate): Boolean {
        return fecha in fechaInicio..fechaTermino
    }

    // CALCULAR EL DESCUENTO POR FECHA
    fun calcularDescuento(fecha: LocalDate, monto: Int): Int {
        return if (estaVigente(fecha)) {
            (monto * DESCUENTO_PROMOCIONAL / 100.0).roundToInt()
        } else {
            0
        }
    }
}
