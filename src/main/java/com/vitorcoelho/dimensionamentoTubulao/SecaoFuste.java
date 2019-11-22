package com.vitorcoelho.dimensionamentoTubulao;

import com.vitorcoelho.dimensionamentoEstrutural.Secao;

public strictfp class SecaoFuste {

    //Variáveis de instância

    private Secao secaoConcretoArmado;
    private double dimensaoContatoSoloX;
    private double dimensaoContatoSoloY;

    //Construtores
    public SecaoFuste(Secao secaoConcretoArmado, double dimensaoContatoSoloX, double dimensaoContatoSoloY) {
        this.secaoConcretoArmado = secaoConcretoArmado;
        this.dimensaoContatoSoloX = dimensaoContatoSoloX;
        this.dimensaoContatoSoloY = dimensaoContatoSoloY;
    }

    //Getters
    public Secao getSecaoConcretoArmado() {
        return this.secaoConcretoArmado;
    }

    public double getDimensaoX() {
        return this.dimensaoContatoSoloX;
    }

    public double getDimensaoY() {
        return this.dimensaoContatoSoloY;
    }

    public double getVolume(double comprimentoSemBase) {
        double volume = this.secaoConcretoArmado.getArea() * comprimentoSemBase;
        return volume;
    }

}
