package com.vitorcoelho.dimensionamentoEstrutural;

import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.pow;

public strictfp class Secao {

    //Todas as unidades desta classe deverão estar em kN e cm.
    //Declaração da variáveis de instância
    private double diametro;

    private double bwEqui;  //bw equivalente para o cálculo da armadura para cisalhamento
    private double dEqui;   //altura útil equivalente para o cálculo da armadura para cisalhamento
    private double dLinha;

    private double area;
    private double inercia;
    private double yCG;
    private double w0;

    private Concreto concreto;
    private BarraAco barraTransversal;
    private BarraAco barraLongitudinal;

    //Construtores
    public Secao(double diametro, Concreto concreto, double cobrimento, BarraAco barraTransversal, BarraAco barraLongitudinal) {
        this.diametro = diametro;
        this.dLinha = cobrimento + barraTransversal.getBitola() + barraLongitudinal.getBitola() / 2.0;
        this.bwEqui = diametro;
        this.dEqui = 0.72 * diametro;
        this.area = PI * pow(diametro, 2.0) / 4.0;
        this.inercia = PI * pow(diametro, 4.0) / 64.0;
        this.yCG = diametro / 2.0;
        this.w0 = this.inercia / this.yCG;
        this.concreto = concreto;
        this.barraTransversal = barraTransversal;
        this.barraLongitudinal = barraLongitudinal;
    }

    //Getters e setters
    public double getDiametro() {
        return diametro;
    }

    public double getBwEqui() {
        return bwEqui;
    }

    public double getdEqui() {
        return dEqui;
    }

    public double getdLinha() {
        return dLinha;
    }

    public double getArea() {
        return area;
    }

    public double getInercia() {
        return inercia;
    }

    public double getW0() {
        return w0;
    }

    public Concreto getConcreto() {
        return concreto;
    }

    public BarraAco getBarraTransversal() {
        return barraTransversal;
    }

    public BarraAco getBarraLongitudinal() {
        return barraLongitudinal;
    }
}
