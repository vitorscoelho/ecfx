package com.vitorcoelho.dimensionamentoTubulao;

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

    public double getVolumeFuste() {
        return this.secaoFuste.getVolume(comprimento - this.secaoBase.getAltura());
    }

    public double getVolumeBase() {
        return this.secaoBase.getVolume();
    }

    public double getVolumeTotal() {
        return (getVolumeFuste() + getVolumeBase());
    }
}
