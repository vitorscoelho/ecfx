/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.dimensionamentoTubulao;

/**
 *
 * @author Vítor
 */
public strictfp class Tubulao {
    //Variáveis de instância
    private SecaoFuste secaoFuste;
    private SecaoBase secaoBase;
    private double comprimento;


    //Construtores
    public Tubulao(SecaoFuste secaoFuste, SecaoBase secaoBase, double comprimento) {
        this.secaoFuste = secaoFuste;
        this.secaoBase = secaoBase;
        this.comprimento = comprimento;
    }

    //Getters
    public SecaoFuste getSecaoFuste() {
        return secaoFuste;
    }

    public SecaoBase getSecaoBase() {
        return secaoBase;
    }

    public double getComprimento() {
        return comprimento;
    }
    
    public double getVolumeFuste(){
        return this.secaoFuste.getVolume(comprimento-this.secaoBase.getAltura());
    }
    
    public double getVolumeBase(){
        return this.secaoBase.getVolume();
    }
    
    public double getVolumeTotal(){
        return (getVolumeFuste()+getVolumeBase());
    }
}
