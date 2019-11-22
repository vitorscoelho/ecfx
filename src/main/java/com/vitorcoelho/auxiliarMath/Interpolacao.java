package com.vitorcoelho.auxiliarMath;

import static java.lang.StrictMath.abs;

public strictfp class Interpolacao {

    //Construtores
    private Interpolacao() {
    }

    //Métodos públicos
    public static final double raizMetodoSecantes(double x1, double x2, double fx1, double fx2, Funcao funcao, double tolerancia) {
        double delta1;
        double delta2;
        double x3;
        double fx3;

        int nIter = 1;
        do {
            delta1 = x2 - x1;
            delta2 = fx2 - fx1;
            x3 = x2 - fx2 * delta1 / delta2;
            fx3 = funcao.getFx(x3);

            /*System.out.println("Iteração: " + nIter);
            System.out.println("x1= " + x1);
            System.out.println("fx1= " + fx1);
            System.out.println("x2= " + x2);
            System.out.println("fx2= " + fx2);
            System.out.println("x3= " + x3);
            System.out.println("fx3= " + fx3);
            System.out.println("");*/

            x1 = x2;
            fx1 = fx2;
            x2 = x3;
            fx2 = fx3;

            nIter++;
        } while (abs(fx3) >= tolerancia);

        return x3;
    }

    public static final double raizMetodoSecantes(double x1, double x2, Funcao funcao, double tolerancia) {
        double fx1 = funcao.getFx(x1);
        double fx2 = funcao.getFx(x2);

        return raizMetodoSecantes(x1, x2, fx1, fx2, funcao, tolerancia);
    }

    public static final double raizMetodoPegaso(double x1, double x2, double fx1, double fx2, Funcao funcao, double tolerancia) {
        double delta1;
        double delta2;
        double x3;
        double fx3;

        int nIter = 1;
        do {
            delta1 = x2 - x1;
            delta2 = fx2 - fx1;
            x3 = x2 - fx2 * delta1 / delta2;
            fx3 = funcao.getFx(x3);

            /*System.out.println("Iteração: " + nIter);
            System.out.println("x1= " + x1);
            System.out.println("fx1= " + fx1);
            System.out.println("x2= " + x2);
            System.out.println("fx2= " + fx2);
            System.out.println("x3= " + x3);
            System.out.println("fx3= " + fx3);
            System.out.println("");*/

            if (fx3 * fx2 < 0.0) {
                x1 = x2;
                fx1 = fx2;
            } else {
                fx1 = fx1 * fx2 / (fx2 + fx3);
            }

            x2 = x3;
            fx2 = fx3;

            nIter++;
        } while (abs(fx3) >= tolerancia);

        return x3;
    }

    public static final double raizMetodoPegaso(double x1, double x2, Funcao funcao, double tolerancia) {
        double fx1 = funcao.getFx(x1);
        double fx2 = funcao.getFx(x2);

        return raizMetodoPegaso(x1, x2, fx1, fx2, funcao, tolerancia);
    }
}
