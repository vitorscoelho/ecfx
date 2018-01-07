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
public final class CisalhamentoBackup {

    private static Concreto concreto = new Concreto();
    private static Aco aco;

    private static Secao secao;

    private static Solicitacao solicitacao;
    private static boolean lNCortaSecao = true;
    private static double mr = 0;

    private static double aswNec = 0;
    private static double aswMin = 0;
    private static double aswAdot = 0;
    private static double vrd2 = 0;
    private static double vc = 0;
    private static double vsw = 0;
    private static double decalagem = 0;
    private static double stMax = 0;
    private static double razaoVsdVrd2 = 0;

    private CisalhamentoBackup() {
    }

    public static void calculaCisalhamento(Secao secao, Solicitacao solicitacao) {
        CisalhamentoBackup.concreto = secao.getConcreto();
        CisalhamentoBackup.aco = secao.getBarraTransversal().getAco();
        CisalhamentoBackup.secao = secao;
        CisalhamentoBackup.solicitacao = solicitacao;
        calculaCisalhamento(solicitacao);
    }

    public static void calculaCisalhamento(Solicitacao solicitacao) {
        CisalhamentoBackup.vrd2 = concreto.getAlphaV2() * concreto.getFcd() * secao.getBwEqui() * secao.getdEqui();
        CisalhamentoBackup.razaoVsdVrd2 = solicitacao.getVd() / CisalhamentoBackup.vrd2;

        if (lNCortaSecao) {
            double m0 = solicitacao.getNd() * secao.getW0() / secao.getArea();
            double vc0 = 0.6 * concreto.getFctd() * secao.getBwEqui() * secao.getdEqui();
            CisalhamentoBackup.vc = vc0 + m0 / solicitacao.getMd();
        } else {
            CisalhamentoBackup.vc = 0;
        }

        CisalhamentoBackup.mr = secao.getW0() * (solicitacao.getNd() / secao.getArea() + concreto.getFctkInf());

        if ((solicitacao.getVd() <= CisalhamentoBackup.vc) && (solicitacao.getMd() <= CisalhamentoBackup.mr)) {
            CisalhamentoBackup.vsw = 0;
            CisalhamentoBackup.aswNec = 0;
            CisalhamentoBackup.aswMin = 0;
            CisalhamentoBackup.aswAdot = 0;
            CisalhamentoBackup.stMax = secao.getDiametro();
        } else {
            CisalhamentoBackup.vsw = Math.max(solicitacao.getVd() - CisalhamentoBackup.vc, 0);
            CisalhamentoBackup.aswNec = CisalhamentoBackup.vsw / (0.9 * secao.getdEqui() * aco.getFywd());
            CisalhamentoBackup.aswMin = 0.2 * secao.getBwEqui() * concreto.getFctm() / aco.getFyk();

            if ((solicitacao.getVd() <= CisalhamentoBackup.vrd2)) {
                CisalhamentoBackup.aswAdot = Math.max(CisalhamentoBackup.aswNec, CisalhamentoBackup.aswMin);
            } else {
                CisalhamentoBackup.aswAdot = 999999999;
            }

            if (CisalhamentoBackup.razaoVsdVrd2 <= 0.2) {
                CisalhamentoBackup.stMax = Math.min(secao.getdEqui(), 80);
            } else {
                CisalhamentoBackup.stMax = Math.min(0.6 * secao.getdEqui(), 35);
            }
        }

        double d = secao.getDiametro() - secao.getdLinha();
        if (solicitacao.getVd() <= CisalhamentoBackup.vc) {
            CisalhamentoBackup.decalagem = d;
        } else {
            CisalhamentoBackup.decalagem = Math.max((d * solicitacao.getVd() / (2 * (solicitacao.getVd() - CisalhamentoBackup.vc))), d);
        }
    }

    public double getEspacamentoLongitudinal(Secao secao, int numeroDeRamos, BarraAco barraTransversal, BarraAco barraLongitudinal) {
        double espacamento;
        double multiplicador = 24 - 12 * (barraLongitudinal.getAco().getFyk() - 25) / 25;

        espacamento = Math.min(20, Math.min(secao.getDiametro(), multiplicador * barraLongitudinal.getBitola()));

        if ((solicitacao.getVd() > CisalhamentoBackup.vc) || (solicitacao.getMd() > CisalhamentoBackup.mr)) {
            double espacamentoCalculado = numeroDeRamos * barraTransversal.getArea() / CisalhamentoBackup.aswAdot;
            espacamento = Math.min(espacamento, espacamentoCalculado);
        }

        return espacamento;
    }
}
