package com.vitorcoelho.dimensionamentoTubulao;

import static java.lang.StrictMath.*;

public strictfp class SecaoBaseCircular implements SecaoBase {

    //Variáveis de instância
    private double diametro;
    private double altura;
    private double rodape;

    private double area;
    private double moduloFlexao;

    private double volume;

    //Construtores
    public SecaoBaseCircular(double diametro, double altura, double rodape, double diametroFuste) {
        this.diametro = diametro;
        this.altura = altura;
        this.rodape = rodape;

        double areaBase = PI * pow(diametro, 2) / 4;
        double areaFuste = PI * pow(diametroFuste, 2) / 4;
        this.volume = areaBase * rodape + (altura - rodape) * ((areaBase + areaFuste + sqrt(areaBase * areaFuste)) / 3);

        this.area = areaBase;
        this.moduloFlexao = PI * pow(diametro, 3) / 32;
    }

    //Getters
    @Override
    public double getAltura() {
        return this.altura;
    }

    @Override
    public double getRodape() {
        return this.rodape;
    }

    @Override
    public double getDimensaoX() {
        return this.diametro;
    }

    @Override
    public double getDimensaoY() {
        return this.diametro;
    }

    @Override
    public double getArea() {
        return this.area;
    }

    @Override
    public double getModuloFlexao() {
        return this.moduloFlexao;
    }

    @Override
    public double getVolume() {
        return this.volume;
    }

}
