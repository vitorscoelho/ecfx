package com.vitorcoelho.dimensionamentoEstrutural;

import static java.lang.StrictMath.*;

public strictfp final class BarraAco {

    //Todas as unidades desta classe deverão estar em kN e cm.
    //Declaração da variáveis de instância
    private double bitola;
    private Aco aco;
    private double area;
    private double eta1;
    private double eta3;
    private Rugosidade rugosidade;

    //Enums
    public enum Rugosidade {

        Lisa("Lisa", 1),
        Entalhada("Entalhada", 1.4),
        Nervurada("Nervurada", 2.25);

        private final String nome;
        private final double eta1;

        private Rugosidade(String nome, double eta1) {
            this.nome = nome;
            this.eta1 = eta1;
        }

        private String getNome() {
            return nome;
        }

        private double getEta1() {
            return eta1;
        }
    }

    public enum Situacao {

        BoaAderencia(1.0),
        MaAderencia(0.7);

        private final double eta2;

        private Situacao(double eta2) {
            this.eta2 = eta2;
        }

        private double getEta2() {
            return eta2;
        }

    }

    public enum Gancho {

        SemGancho(1.0),
        ComGancho(0.7),
        ComBarraSoldada(0.7),
        ComGanchoEBarraSoldada(0.5);

        private final double alpha;

        private Gancho(double alpha) {
            this.alpha = alpha;
        }

        public double getAlpha() {
            return alpha;
        }
    }

    //Construtores
    public BarraAco(double bitola, Aco aco, Rugosidade rugosidade) {
        this.bitola = bitola;
        this.aco = aco;
        this.area = PI * pow(bitola, 2) / 4;
        this.eta1 = rugosidade.getEta1();
        this.rugosidade = rugosidade;

        if (bitola < 32) {
            this.eta3 = 1;
        } else {
            this.eta3 = (13.2 - bitola) / 10;
        }
    }

    public BarraAco(double bitola, Aco aco) {
        this(bitola, aco, Rugosidade.Nervurada);
    }

    //Métodos públicos
    public double lb(Concreto concreto, Situacao SituacaoDeAderencia) {
        double lb = this.bitola * this.aco.getFyd() / (4.0 * fbd(concreto, SituacaoDeAderencia));

        return max(lb, 25.0 * this.bitola);
    }

    public double lbNec(Concreto concreto, Situacao SituacaoDeAderencia, Gancho gancho, double aSCalc, double aSEfet) {
        double lb = lb(concreto, SituacaoDeAderencia);
        double lbNec = gancho.getAlpha() * lb * aSCalc / aSEfet;
        double lbMin1 = 0.3 * lb;
        double lbMin2 = 10.0 * this.bitola;
        double lbMin3 = 10;
        double lbMin = max(lbMin1, max(lbMin2, lbMin3));

        return max(lbNec, lbMin);
    }

    @Override
    public String toString() {
        String resultado = "#" + this.bitola * 10 + "mm  CA-" + this.getAco().getFyk();
        return resultado;
    }

    //Métodos privados
    private double fbd(Concreto concreto, Situacao situacaoDeAderencia) {
        return (this.eta1 * situacaoDeAderencia.getEta2() * this.eta3 * concreto.getFctd());
    }

    //Getters e setters
    public double getBitola() {
        return bitola;
    }

    public Aco getAco() {
        return aco;
    }

    public double getArea() {
        return area;
    }

    public Rugosidade getRugosidade() {
        return rugosidade;
    }
}
