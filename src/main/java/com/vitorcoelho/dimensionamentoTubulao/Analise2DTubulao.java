/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.dimensionamentoTubulao;

import com.vitorcoelho.dimensionamentoEstrutural.Solicitacao;
import com.vitorcoelho.auxiliarMath.Funcao;

/**
 *
 * @author Vítor
 */
public strictfp interface Analise2DTubulao {

    public double limiteComprimentoTubulaoCurto();

    public boolean isCurto();

    public boolean isEstavel();

    public double getAlpha();

    public double getDeslocamentoHxTopo();

    public double getDeslocamentoVTopo();

    public Solicitacao getSolicitacaoTopo();

    public double getTensaoVAdmissivel();

    public double getKa();

    public double getKp();

    public double getAnguloDeAtrito();

    //Getters de dados dada a profundidade
    public double getCoeficienteReacaoFuste(double profundidade);

    public double getTensaoFuste(double profundidade);

    public double getTensaoTensaoHorizontalAdmissivel(double profundidade);

    public double getCoeficienteSegurancaEstabilidade(double profundidade);

    public double getReacaoHorizontalFuste(double profundidade);

    public double getCortanteFuste(double profundidade);

    public double getMomentoFuste(double profundidade);

    //Getters de funções
    public Funcao getFuncaoCoeficienteReacaoFuste();

    public Funcao getFuncaoTensaoFuste();

    public Funcao getFuncaoTensaoHorizontalAdmissivel();

    public Funcao getFuncaoCoeficienteSegurancaEstabilidade();

    public Funcao getFuncaoReacaoFuste();

    public Funcao getFuncaoCortanteFuste();

    public Funcao getFuncaoMomentoFuste();

    public double getTensaoMaximaBase();

    public double getTensaoMinimaBase();

    public Tubulao getTubulao();

}
