/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.auxiliarMath;

/**
 *
 * @author Vítor
 */
public strictfp class Utils {

    public static double minimo(double... valor) {
        double minimo = valor[0];
        for (int i = 1; i < valor.length; i++) {
            minimo=StrictMath.min(minimo, valor[i]);
        }
        return minimo;
    }
    
    public static double minimo(String... valor) {
        double minimo = Double.parseDouble(valor[0]);
        for (int i = 1; i < valor.length; i++) {
            minimo=StrictMath.min(minimo, Double.parseDouble(valor[i]));
        }
        return minimo;
    }

    public static double arredondar(double valor, int nCasas) {
        double x = (int) Math.pow(10, nCasas);
        double N = (int) (valor * x);
        N = (double) (N / x);
        return N;
    }

    public static String arredondarToString(double valor, int nCasas) {
        double resposta = arredondar(valor, nCasas);
        return (Double.toString(resposta));
    }

    public static String arredondarToString(String valor, int nCasas) {
        double valorD = Double.parseDouble(valor);
        double resposta = arredondar(valorD, nCasas);
        return (Double.toString(resposta));
    }
}
