package com.vitorcoelho.dimensionamentoEstrutural;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.min;

public strictfp final class Aco {

    //Todas as unidades desta classe deverão estar em kN e cm.
    //Declaração da variáveis de instância
    private final double fyk;
    private final double gamaS;
    private final double moduloDeDeformacao;
    private final double fyd;
    private final double fywd;

    private final double eYd; //Deformação específica de cálculo do aço no início do patamar plástico

    //Construtores
    public Aco(double fyk, double gamaS, double moduloDeDeformacao) {
        this.fyk = fyk;
        this.gamaS = gamaS;
        this.moduloDeDeformacao = moduloDeDeformacao;

        this.fyd = this.fyk / this.gamaS;
        this.fywd = min(fyk / gamaS, 43.5);
        this.eYd = 1000 * this.fyd / this.moduloDeDeformacao;
    }

    public Aco(double fyk, double gamaS) {
        this(fyk, gamaS, 21000.0);
    }

    public Aco(double fyk) {
        this(fyk, 1.15, 21000.0);
    }

    //Métodos públicos
    public double tensao(double deformacao) {
        if (abs(deformacao) < this.eYd) {
            return (this.fyd * deformacao / this.eYd);
        }
        return (this.fyd * deformacao / abs(deformacao));
    }

    @Override
    public String toString() {
        return ("CA-" + (int) fyk);
    }

    //Métodos getters e setters da classe
    public double getFyk() {
        return fyk;
    }

    public double getFyd() {
        return fyd;
    }

    public double getFywd() {
        return fywd;
    }

    public double geteYd() {
        return eYd;
    }
}
