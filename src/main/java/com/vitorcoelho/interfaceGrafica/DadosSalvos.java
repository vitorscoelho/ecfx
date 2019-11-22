package com.vitorcoelho.interfaceGrafica;

import java.io.Serializable;

class DadosSalvos implements Serializable {
    //Concreto
    String fck;
    String gamaC;
    String eci;
    String ecs;
    int selecionadoComboBoxAgregado;
    boolean isSelecionadoCheckBoxEcAutomatico;

    //Armadura longitudinal
    String fyk;
    String gamaSLongitudinal;
    String esLongitudinal;
    String bitolaLongitudinal;

    //Armadura transversal
    String fywk;
    String gamaSTransversal;
    String bitolaTransversal;

    //Características Geométricas
    String cobrimento;
    String diametroFuste;
    String diametroBase;
    String hBase;
    String rodape;
    String profundidade;

    //Cargas no topo
    String nk;
    String hk;
    String mk;
    String gamaN;

    //Características do elemento de fundação
    int selecionadoComboBoxTipoFundacao;
    double comprimentoMinimoArmadura;
    double tensaoMediaConcretoSimples;

    //Tipo do solo
    boolean isAreiaSelecionado;

    //Características do solo
    String etaOuKh;
    String kv;
    String coesao;
    String anguloDeAtrito;
    String pesoEspecifico;
    String tensaoAdmissivel;

    //Parâmetros
    String coeficienteDeSegurancaAtual;
    String coeficienteDeSegurancaMaximoAtual;
    String majoradorFlexoCompressaoAtual;
    String taxaMinimaLongitudinalAtual;
    String taxaMaximaLongitudinalAtual;
}
