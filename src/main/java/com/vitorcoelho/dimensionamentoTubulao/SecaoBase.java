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
public strictfp interface SecaoBase {
    public double getAltura();
    public double getRodape();
    public double getDimensaoX();
    public double getDimensaoY();
    public double getArea();
    public double getModuloFlexao();
    public double getVolume();
}
