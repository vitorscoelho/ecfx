package vitorscoelho.ecfx.utils

import vitorscoelho.ecfx.dimensionamento.estrutural.Aco
import vitorscoelho.ecfx.dimensionamento.estrutural.Concreto
import vitorscoelho.utils.measure.toDoubleSU
import javax.measure.Quantity
import javax.measure.quantity.Pressure

fun criarAco(fyk: Quantity<Pressure>, gamaS: Double, moduloDeDeformacao: Quantity<Pressure>) = Aco(
    fyk = fyk.toDoubleSU(), gamaS = gamaS, moduloDeDeformacao = moduloDeDeformacao.toDoubleSU()
)

fun criarConcreto(fck: Quantity<Pressure>, gamaC: Double) = Concreto(
    fck = fck.toDoubleSU(), gamaC = gamaC
)

