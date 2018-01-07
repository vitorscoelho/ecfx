/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.dimensionamentoEstrutural;

/**
 *
 * @author Vítor
 */
public strictfp final class Solicitacao {

    //Todas as unidades desta classe deverão estar em kN e cm.
    //Declaração da variáveis de instância
    private double nk;
    private double nd;
    private double vd;
    private double md;

    //Construtores
    public Solicitacao(double nk, double vk, double mk, double gamaN) {
        this.nk = nk;
        this.nd = nk * gamaN;
        this.vd = vk * gamaN;
        this.md = mk * gamaN;
    }

    //Getters e setters
    public double getNk() {
        return nk;
    }

    public double getNd() {
        return nd;
    }

    public double getVd() {
        return vd;
    }

    public double getMd() {
        return md;
    }
}
