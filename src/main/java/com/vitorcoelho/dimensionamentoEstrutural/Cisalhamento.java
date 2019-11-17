/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.dimensionamentoEstrutural;

import static com.vitorcoelho.auxiliarMath.Utils.*;
import static java.lang.StrictMath.*;

/**
 *
 * @author Vítor
 */
public strictfp class Cisalhamento {
    //Todas as unidades desta classe deverão estar em kN e cm.
    //Declaração da variáveis de instância

    //Construtores
    private Cisalhamento() {
    }

    //Métodos públicos
    public static double asw(Secao secao, Solicitacao solicitacao, int nRamos) {
        double vd = solicitacao.getVd();

        double vrd2 = vrd2(secao);
        if (vd > vrd2) {
            return (-999.0);
        }

        double vc0 = vc0(secao, solicitacao);
        double vc = vc(vc0, secao, solicitacao);

        boolean isViga = isViga(solicitacao, secao, vc);

        double aswMinima = aswMinima(isViga, secao, nRamos);
        double aswCalculada = aswCalculada(isViga, secao, solicitacao, vc);
        
        System.out.println("Esforços solicitantes:");
        System.out.println("Nsk = " +arredondar(solicitacao.getNk(),2) + "kN");
        System.out.println("Nsd = " +arredondar(solicitacao.getNd(),2) + "kN");
        System.out.println("Vsd = " +arredondar(solicitacao.getVd(),2) + "kN");
        System.out.println("Msd = " +arredondar(solicitacao.getMd()/100,2) + "kN.m");
        System.out.println("Geometria:");
        System.out.println("bw,equi = " +secao.getBwEqui());
        System.out.println("d,equi = " +secao.getdEqui());
        System.out.println("\r\nVerificação da compressão diagonal do concreto:");
        System.out.println("Vrd2 = 0.27*alphaV2*fcd*bw*d = " +arredondar(solicitacao.getNk(),2) + "kN");
        
        return (aswAdotada(aswMinima, aswCalculada));
    }

    public static boolean isViga(Solicitacao solicitacao, Secao secao, double vc) {
        double vd = solicitacao.getVd();
        double nd = solicitacao.getNd();
        double md = solicitacao.getMd();
        double area = secao.getArea();
        double fctkInf = secao.getConcreto().getFctkInf();

        double mFissuracao = secao.getW0() * (nd / area + fctkInf);
        boolean isViga = !(vd <= vc && md <= mFissuracao && nd > 0.0);
        return isViga;
    }

    public static double vRd(Secao secao, Solicitacao solicitacao, double espacamento, int nRamos) {
        double asw = nRamos * secao.getBarraTransversal().getArea() / espacamento;

        double vRd2 = vrd2(secao);

        double vc0 = vc0(secao, solicitacao);
        double vc = vc(vc0, secao, solicitacao);
        double vsw = vsw(asw, secao);

        return (min(vRd2, vsw + vc));
    }

    public static double vRd(double vrd2, double vc, double vsw) {
        return (min(vrd2, vc + vsw));
    }

    public static double vrd2(Secao secao) {
        return (0.27 * secao.getConcreto().getAlphaV2() * secao.getConcreto().getFcd() * secao.getBwEqui() * secao.getdEqui());
    }

    public static double vc0(Secao secao, Solicitacao solicitacao) {
        return (0.6 * secao.getConcreto().getFctd() * secao.getBwEqui() * secao.getdEqui());
    }

    public static double vc(double vc0, Secao secao, Solicitacao solicitacao) {
        double diametro = secao.getDiametro();
        double area = secao.getArea();
        double w0 = secao.getW0();
        double nk = solicitacao.getNk();
        double nd = solicitacao.getNd();
        double md = solicitacao.getMd();

        //Cálculo do momento de descompressão
        double m0 = nk * w0 / area;

        //Verificação se a linha neutra corta a seção
        boolean isCompressao = (nd > 0.0);
        double excentricidade = md / nd;
        double raioNucleoCentral = diametro / 8;
        boolean isForaNucleoCentral = (excentricidade >= raioNucleoCentral);
        boolean lNCortaSecao = (isCompressao || isForaNucleoCentral);

        //Cálculo do Vc
        if (isCompressao) {
            return (vc0 * min((1 + m0 / md), 2));
        } else if (lNCortaSecao) {
            return (vc0);
        } else {
            return (0);
        }
    }

    public static double vsw(double asw, Secao secao) {
        return (asw * 0.9 * secao.getdEqui() * secao.getBarraTransversal().getAco().getFywd());
    }

    public static double aswMinima(boolean isViga, Secao secao, int nRamos) {
        if (isViga) {
            return (0.2 * secao.getBwEqui() * secao.getConcreto().getFctm() / secao.getBarraTransversal().getAco().getFyk());
        }

        double diametro = secao.getDiametro();

        double fywk = secao.getBarraTransversal().getAco().getFyk();
        double bitola = secao.getBarraTransversal().getBitola();
        double areaBarra = secao.getBarraTransversal().getArea();

        double bitolaLongitudinal = secao.getBarraLongitudinal().getBitola();
        double fykLongitudinal = secao.getBarraLongitudinal().getAco().getFyk();

        double multiplicador = 24 - 12 * (fykLongitudinal - 25) / 25;
        double espacamento = min(20, min(diametro, multiplicador * bitolaLongitudinal));

        if (bitola < bitolaLongitudinal / 4) {
            espacamento = min(espacamento, 9000 * (bitola * bitola / bitolaLongitudinal) / max(fywk, fykLongitudinal));
        }

        return (nRamos * areaBarra / espacamento);
    }

    public static double aswCalculada(boolean isViga, Secao secao, Solicitacao solicitacao, double vc) {
        if (isViga) {
            double asw = (solicitacao.getVd() - vc) / (0.9 * secao.getdEqui() * secao.getBarraTransversal().getAco().getFywd());
            return (max(asw, 0));
        }
        return 0.0;
    }

    public static double aswAdotada(double aswMinima, double aswCalculada) {
        return (max(aswMinima, aswCalculada));
    }

    public static double espacamentoLongitudinalMaximo(Secao secao, boolean isViga, Solicitacao solicitacao, double vrd2) {
        double diametro = secao.getDiametro();
        double fywk = secao.getBarraTransversal().getAco().getFyk();
        double bitola = secao.getBarraTransversal().getBitola();
        double bitolaLongitudinal = secao.getBarraLongitudinal().getBitola();
        double fykLongitudinal = secao.getBarraLongitudinal().getAco().getFyk();
        double multiplicador = 24 - 12 * (fykLongitudinal - 25) / 25;
        double espacamento = max(0,min(20, min(diametro, multiplicador * bitolaLongitudinal)));

        if (bitola < bitolaLongitudinal / 4) {
            espacamento = min(espacamento, 9000 * (bitola * bitola / bitolaLongitudinal) / max(fywk, fykLongitudinal));
        }
        
        if (isViga) {
            if (solicitacao.getVd() <= 0.67 * vrd2) {
                return (min(espacamento,min(0.6 * secao.getdEqui(), 30)));
            }
            return (min(espacamento,min(0.3 * secao.getdEqui(), 20)));
        }
        
        return espacamento;
    }

    public static double espacamentoLongitudinalCalculado(Secao secao, double aswAdotado, int nRamos) {
        return (nRamos * secao.getBarraTransversal().getArea() / aswAdotado);
    }

    public static double espacamentoLongitudinalAdotado(Secao secao, double espacamentoMaximo, double espacamentoCalculado) {
        return (min(espacamentoMaximo, espacamentoCalculado));
    }
    
    public static double espacamentoLongitudinalAdotado(Secao secao, Solicitacao solicitacao, int nRamos){
        double vrd2 = Cisalhamento.vrd2(secao);
        double vc0 = Cisalhamento.vc0(secao, solicitacao);
        double vc = Cisalhamento.vc(vc0, secao, solicitacao);
        boolean isViga = Cisalhamento.isViga(solicitacao, secao, vc);
        double aswMinima = Cisalhamento.aswMinima(isViga, secao, nRamos);
        double aswCalculada = Cisalhamento.aswCalculada(isViga, secao, solicitacao, vc);
        double aswAdotada = Cisalhamento.aswAdotada(aswMinima, aswCalculada);
        double espacamentoMaximo = Cisalhamento.espacamentoLongitudinalMaximo(secao, isViga, solicitacao, vrd2);
        double espacamentoCalculado = Cisalhamento.espacamentoLongitudinalCalculado(secao, aswAdotada, nRamos);
        double espacamentoAdotado = Cisalhamento.espacamentoLongitudinalAdotado(secao, espacamentoMaximo, espacamentoCalculado);
        
        return espacamentoAdotado;
    }

    public static double espacamentoTransversalMaximo(Secao secao, Solicitacao solicitacao, boolean isViga, double vsd, double vrd2) {
        if (isViga) {
            if (solicitacao.getVd() <= 0.2 * vrd2) {
                return (min(secao.getdEqui(), 80));
            }
            return (min(0.6 * secao.getdEqui(), 35));
        }
        return 0;
    }

    public static double asw(Secao secao, double espacamento, int nRamos) {
        return (nRamos * secao.getBarraTransversal().getArea() / espacamento);
    }
}
