/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.dimensionamentoEstrutural;

import static java.lang.StrictMath.*;

/**
 *
 * @author Vítor
 */
public strictfp final class Concreto {

    //Todas as unidades desta classe deverão estar em kN e cm.
    //Declaração da variáveis de instância
    private final double fck;
    private final double gamaC;
    private final AgregadoGraudo agregadoGraudo;
    private double moduloDeDeformacaoInicial;
    private double moduloDeDeformacaoSecante;

    private final double K_MOD = 0.85; //Fator de redução para resistência a longa duração do concreto. kMod=kMod1*kMod2*kMod3=0.95*1.20*0.75=0.855
    private final double alphaC; //Parâmetro para o diagrama retangular de compressão
    private final double lambida; //Parâmetro para o diagrama retangular de compresão
    private final double n; //Parâmetro para o diagrama "parábola"-retângulo de compressão
    private final double eC2; //Deformação específica de encurtamento do concreto no início do patamar plástico (por mil)
    private final double eCu; //Deformação específica de encurtamento do concreto na ruptura (por mil)

    private final double fcd;
    private final double fctd;
    private final double fctm;
    private final double fctkInf;
    private final double fctkSup;
    private final double alphaV2;     //Parâmetro utilizado para verificação da biela no cálculo do esforço cortante

    //Enums
    public enum AgregadoGraudo {

        Basalto("Basalto", 1.2),
        Diabasio("Diabásio", 1.2),
        Granito("Granito", 1.0),
        Gnaisse("Gnaisse", 1.0),
        Calcario("Calcário", 0.9),
        Arenito("Arenito", 0.7);

        private String nome;
        private double alphaE;  //Coeficiente para cálculo do módulo de deformação de acordo com a NBR6118:2014

        private AgregadoGraudo(String nome, double alphaE) {
            this.nome = nome;
            this.alphaE = alphaE;
        }

        public double getAlphaE() {
            return alphaE;
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    //Construtores
    public Concreto(double fck, double gamaC, double moduloDeDeformacaoInicial, double moduloDeDeformacaoSecante, AgregadoGraudo agregadoGraudo) {
        this.fck = fck;
        this.gamaC = gamaC;
        this.agregadoGraudo = agregadoGraudo;
        this.moduloDeDeformacaoInicial = moduloDeDeformacaoInicial;
        this.moduloDeDeformacaoSecante = moduloDeDeformacaoSecante;
        this.fcd = this.fck / this.gamaC;

        if (this.fck <= 5.) {
            this.alphaC = this.K_MOD;
            this.lambida = 0.8;
            this.n = 2.0;
            this.eC2 = 2.0;
            this.eCu = 3.5;
            this.fctm = 0.03 * pow(this.fck * 10.0, 2.0 / 3.0);
        } else {
            this.alphaC = this.K_MOD * (1. - (this.fck - 5.0) / 20.0);
            this.lambida = 0.8 - (this.fck - 5.0) / 40.0;
            this.n = 1.4 + 23.4 * pow(((9.0 - this.fck) / 10.0), 4.0);
            this.fctm = 0.212 * log(1.0 + 0.11 * this.fck * 10.0);
            this.eC2 = 2.0 + 0.085 * pow((this.fck * 10.0 - 50.0), 0.53);
            this.eCu = 2.6 + 35.0 * pow((9.0 - this.fck) / 10.0, 4.0);
        }

        this.fctkInf = 0.7 * this.fctm;
        this.fctkSup = 1.3 * this.fctm;
        this.alphaV2 = (1.0 - (this.fck * 10.0) / 250.0);
        this.fctd = this.fctkInf / this.gamaC;
    }

    public Concreto(double fck, double gamaC, AgregadoGraudo agregadoGraudo) {
        this(fck, gamaC, 0, 0, agregadoGraudo);
        calculaModuloDeDeformacao(fck, agregadoGraudo);
    }

    public Concreto(double fck, double gamaC) {
        this(fck, gamaC, 0, 0, AgregadoGraudo.Arenito);
        calculaModuloDeDeformacao(fck, agregadoGraudo);
    }

    public Concreto(double fck) {
        this(fck, 1.4);
        calculaModuloDeDeformacao(fck, agregadoGraudo);
    }

    public Concreto() {
        this(25);
    }

    //Métodos privados
    private void calculaModuloDeDeformacao(double fck, AgregadoGraudo agregadoGraudo) {
        if (this.fck <= 5) {
            this.moduloDeDeformacaoInicial = agregadoGraudo.getAlphaE() * 560 * sqrt(this.fck * 10);
        } else {
            this.moduloDeDeformacaoInicial = agregadoGraudo.getAlphaE() * 2150 * pow((fck + 1.25), (1D / 3D));
        }

        double alphaI = min((0.8 + 0.2 * this.fck / 8), 1);
        this.moduloDeDeformacaoSecante = alphaI * this.moduloDeDeformacaoInicial;
    }

    //Métodos públicos
    public double tensao(double deformacao) {
        if (deformacao <= 0) {
            return 0;
        }
        if (deformacao < this.eC2) {
            return (this.K_MOD * this.fcd * (1 - pow((1 - deformacao / this.eC2), this.n)));
        }
        return (this.K_MOD * this.fcd);
    }
    
    public static double moduloDeDeformacaoInicial(double fck, AgregadoGraudo agregadoGraudo) {
        if (fck <= 5) {
            return (agregadoGraudo.getAlphaE() * 560 * sqrt(fck * 10));
        } else {
            return (agregadoGraudo.getAlphaE() * 2150 * pow((fck + 1.25), (1D / 3D)));
        }
    }
    
    public static double moduloDeDeformacaoSecante(double fck, double moduloDeDeformacaoInicial) {
        double alphaI = min((0.8 + 0.2 * fck / 8), 1);
        return (alphaI * moduloDeDeformacaoInicial);
    }

    //Getters e setters
    public double getFck() {
        return fck;
    }

    public double getModuloDeDeformacaoInicial() {
        return moduloDeDeformacaoInicial;
    }

    public double getModuloDeDeformacaoSecante() {
        return moduloDeDeformacaoSecante;
    }

    public double getAlphaC() {
        return alphaC;
    }

    public double getLambida() {
        return lambida;
    }

    public double geteC2() {
        return eC2;
    }

    public double geteCu() {
        return eCu;
    }

    public double getFcd() {
        return fcd;
    }

    public double getFctd() {
        return fctd;
    }

    public double getFctm() {
        return fctm;
    }

    public double getFctkInf() {
        return fctkInf;
    }

    public double getFctkSup() {
        return fctkSup;
    }

    public double getAlphaV2() {
        return alphaV2;
    }
    
    public double getTwrdSimples(double tensaoMediaConcretoComprimido){
        double parcelaNormal=1+3*tensaoMediaConcretoComprimido/this.fck;
        parcelaNormal=max(1,min(parcelaNormal,2));
        
        double resistente=0.25*this.fctd*parcelaNormal;   //Já está considerando que, para concreto simples, deve-se multiplicar o gamac por 1,2
        
        return resistente;
    }
}
