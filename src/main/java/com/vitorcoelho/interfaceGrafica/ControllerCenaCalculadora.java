package com.vitorcoelho.interfaceGrafica;

import com.vitorcoelho.dimensionamentoEstrutural.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.StrictMath.round;

public strictfp class ControllerCenaCalculadora extends ControllerCenaPadrao implements Initializable {

    private int nRamos = 2;
    private int nBarrasMin = 6;
    private int nBarrasMax = 999;

    @FXML
    private TextField textFieldFck;
    @FXML
    private TextField textFieldGamaC;
    @FXML
    private TextField textFieldFykLong;
    @FXML
    private TextField textFieldGamaSLong;
    @FXML
    private TextField textFieldEsLong;
    @FXML
    private TextField textFieldBitolaLong;
    @FXML
    private TextField textFieldFykTransv;
    @FXML
    private TextField textFieldGamaSTransv;
    @FXML
    private TextField textFieldBitolaTransv;
    @FXML
    private TextField textFieldDiametro;
    @FXML
    private TextField textFieldGamaN;
    @FXML
    private TextField textFieldCobrimento;
    @FXML
    private TextField textFieldNkDim;
    @FXML
    private TextField textFieldVkDim;
    @FXML
    private TextField textFieldMkDim;
    @FXML
    private TextField textFieldNkVerCis;
    @FXML
    private TextField textFieldMkVerCis;
    @FXML
    private TextField textFieldEspacamento;
    @FXML
    private TextField textFieldNkVerFlex;
    @FXML
    private TextField textFieldNBarras;

    @FXML
    private TextArea textAreaDim;
    @FXML
    private TextArea textAreaVerCis;
    @FXML
    private TextArea textAreaVerFlex;
    @FXML
    private TextArea textAreaConsole;

    @FXML
    private Line line1;
    @FXML
    private Line line2;
    @FXML
    private Line line3;
    @FXML
    private Line line4;
    @FXML
    private Line line5;
    @FXML
    private Line line6;
    @FXML
    private Line line7;
    @FXML
    private Line line8;
    @FXML
    private Circle circleEstribo;
    @FXML
    private Label labelNBarras;
    @FXML
    private Label labelBitola;
    @FXML
    private Label labelCotaDiametro;
    @FXML
    private Label labelEstribos;
    @FXML
    private HBox hBoxLongitudinal;
    @FXML
    private HBox hBoxTransversal;
    Node[] imagensLongitudinal = new Node[9];
    Node[] imagensTransversal = new Node[2];

    PrintStream outPadrao = System.out;
    PrintStream outTextArea;

    private void aplicarTooltips() {
        this.textFieldFck.setTooltip(new Tooltip("Resistência característica à compressão do concreto."));
        this.textFieldGamaC.setTooltip(new Tooltip("Coeficiente de ponderação da resistência do concreto."));
        this.textFieldFykLong.setTooltip(new Tooltip("Resistência característica ao escoamento do aço da armadura longitudinal."));
        this.textFieldGamaSLong.setTooltip(new Tooltip("Coeficiente de ponderação da resistência do aço da armadura longitudinal."));
        this.textFieldEsLong.setTooltip(new Tooltip("Módulo de deformação do aço da armadura longitudinal."));
        this.textFieldBitolaLong.setTooltip(new Tooltip("Bitola da barra utilizada na armadura longitudinal."));
        this.textFieldFykTransv.setTooltip(new Tooltip("Resistência característica ao escoamento do aço da armadura transversal."));
        this.textFieldGamaSTransv.setTooltip(new Tooltip("Coeficiente de ponderação da resistência do aço da armadura transversal."));
        this.textFieldBitolaTransv.setTooltip(new Tooltip("Bitola da barra utilizada na armadura transversal."));
        this.textFieldDiametro.setTooltip(new Tooltip("Diâmetro da seção transversal de concreto."));
        this.textFieldGamaN.setTooltip(new Tooltip("Majorador de esforços para ELU."));
        this.textFieldCobrimento.setTooltip(new Tooltip("Cobrimento do estribo."));
        this.textFieldNkDim.setTooltip(new Tooltip("Esforço normal característico atuante no topo da estaca."));
        this.textFieldVkDim.setTooltip(new Tooltip("Esforço horizontal característico atuante no topo da estaca."));
        this.textFieldMkDim.setTooltip(new Tooltip("Esforço de momento fletor característico atuante no topo da estaca."));
        this.textFieldNkVerCis.setTooltip(new Tooltip("Esforço normal característico atuante no topo da estaca."));
        this.textFieldMkVerCis.setTooltip(new Tooltip("Esforço de momento fletor característico atuante no topo da estaca."));
        this.textFieldEspacamento.setTooltip(new Tooltip("Espaçamento entre estribos."));
        this.textFieldNkVerFlex.setTooltip(new Tooltip("Esforço normal característico atuante no topo da estaca."));
        this.textFieldNBarras.setTooltip(new Tooltip("Quantidade de barras longitudinais na seção transversal."));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        imagensLongitudinal[0] = this.line1;
        imagensLongitudinal[1] = this.line2;
        imagensLongitudinal[2] = this.line3;
        imagensLongitudinal[3] = this.line4;
        imagensLongitudinal[4] = this.line5;
        imagensLongitudinal[5] = this.line6;
        imagensLongitudinal[6] = this.line7;
        imagensLongitudinal[7] = this.line8;
        imagensLongitudinal[8] = this.hBoxLongitudinal;

        imagensTransversal[0] = this.circleEstribo;
        imagensTransversal[1] = this.hBoxTransversal;

        ativarImagensLong(false);
        ativarImagensTransv(false);
        this.labelCotaDiametro.setText(arredondarToString(Double.parseDouble(this.textFieldDiametro.getText()), 2));

        //Criando stream para transformar a caixa de texto em console pro System.println
        TextAreaOutputStream textOut = new TextAreaOutputStream(this.textAreaConsole);
        outTextArea = new PrintStream(textOut, true);

        this.aplicarTooltips();
    }

    @FXML
    void onActionDimensionar(ActionEvent event) {
        this.textAreaConsole.setText("Memória de cálculo:\r\n");
        System.setOut(outTextArea);

        double fck = Double.parseDouble(this.textFieldFck.getText()) / 10;
        double gamaC = Double.parseDouble(this.textFieldGamaC.getText());
        Concreto concreto = new Concreto(fck, gamaC);
        double fykLong = Double.parseDouble(this.textFieldFykLong.getText()) / 10;
        double gamaSLong = Double.parseDouble(this.textFieldGamaSLong.getText());
        double esLong = Double.parseDouble(this.textFieldEsLong.getText());
        double bitolaLong = Double.parseDouble(this.textFieldBitolaLong.getText()) / 10;
        Aco aco = new Aco(fykLong, gamaSLong, esLong);
        BarraAco barraLong = new BarraAco(bitolaLong, aco);

        double fykTransv = Double.parseDouble(this.textFieldFykTransv.getText()) / 10;
        double gamaSTransv = Double.parseDouble(this.textFieldGamaSTransv.getText());
        double bitolaTransv = Double.parseDouble(this.textFieldBitolaTransv.getText()) / 10;
        Aco acoTransv = new Aco(fykTransv, gamaSTransv);
        BarraAco barraTransv = new BarraAco(bitolaTransv, acoTransv);

        double diametro = Double.parseDouble(this.textFieldDiametro.getText());
        double cobrimento = Double.parseDouble(this.textFieldCobrimento.getText());
        Secao secao = new Secao(diametro, concreto, cobrimento, barraTransv, barraLong);

        double nk = Double.parseDouble(this.textFieldNkDim.getText());
        double vk = Double.parseDouble(this.textFieldVkDim.getText());
        double mk = Double.parseDouble(this.textFieldMkDim.getText()) * 100;
        double gamaN = Double.parseDouble(this.textFieldGamaN.getText());
        Solicitacao solicitacao = new Solicitacao(nk, vk, mk, gamaN);

        double nd = solicitacao.getNd();
        double vd = solicitacao.getVd();
        double md = solicitacao.getMd();

        //Dimensionamento ao cisalhamento
        double vrd2 = Cisalhamento.vrd2(secao);
        double vc0 = Cisalhamento.vc0(secao, solicitacao);
        double vc = Cisalhamento.vc(vc0, secao, solicitacao);
        boolean isViga = Cisalhamento.isViga(solicitacao, secao, vc);
        double aswMinima = Cisalhamento.aswMinima(isViga, secao, this.nRamos);
        double aswCalculada = Cisalhamento.aswCalculada(isViga, secao, solicitacao, vc);
        double aswAdotada = Cisalhamento.aswAdotada(aswMinima, aswCalculada);
        double espacamentoMaximo = Cisalhamento.espacamentoLongitudinalMaximo(secao, isViga, solicitacao, vrd2);
        double espacamentoCalculado = Cisalhamento.espacamentoLongitudinalCalculado(secao, aswAdotada, this.nRamos);
        double espacamentoAdotado = Cisalhamento.espacamentoLongitudinalAdotado(secao, espacamentoMaximo, espacamentoCalculado);

        String tVrd2 = "Vrd2= " + arredondar(vrd2, 2) + " kN\r\n";
        String tVc0 = "Vc0= " + arredondar(vc0, 2) + " kN\r\n";
        String tVc = "Vc= " + arredondar(vc, 2) + " kN\r\n";
        String tVigaOuPilar = "Dimensionado de acordo com o item " + (isViga ? "17.4.2.2" : "17.4.1.1.2.c)") + " da NBR6118:2014\r\n";
        String tAswCalculado = "(Asw/s)calculado= " + arredondar(aswCalculada * 100, 2) + " cm²/m\r\n";
        String tAswMinima = "(Asw/s)minima= " + arredondar(aswMinima * 100, 2) + " cm²/m\r\n";
        String tAsw = "Asw/s= " + arredondar(aswAdotada * 100, 2) + " cm²/m\r\n";
        String tEspacamentoCalculado = "s(calculado)= " + arredondar(espacamentoCalculado, 2) + " cm\r\n";
        String tEspacamentoMaximo = "s(máximo)= " + arredondar(espacamentoMaximo, 2) + " cm";
        String tArmaduraTransversal = "Transversal: #" + arredondar(secao.getBarraTransversal().getBitola() * 10, 1) + " mm c/" + arredondar(espacamentoAdotado, 1) + " cm\r\n";

        //Dimensionamento à flexo-compressão
        double[] flexoCompressao = FlexoCompressao.x_d_nRdMin_nRdMax_mRd_nBarrasNecessarias_AsCalculado(secao, solicitacao, this.nBarrasMin, this.nBarrasMax);
        double x = flexoCompressao[0];
        double d = flexoCompressao[1];
        double nRdMin = flexoCompressao[2];
        double nRdMax = flexoCompressao[3];
        double mRd = flexoCompressao[4];
        int nBarrasNecessario = (int) round(flexoCompressao[5]);
        int nBarrasAdotado = FlexoCompressao.nBarrasAdotado(secao, solicitacao, nBarrasNecessario);
        double asCalculado = flexoCompressao[6];
        String dominio = FlexoCompressao.domínio(x, d, secao, concreto, aco);

        String tX = "x= " + arredondar(x, 2) + " cm\r\n";
        String tD = "d= " + arredondar(d, 2) + " cm\r\n";
        String tNrdMin = "Nrd,mín= " + arredondar(nRdMin, 2) + " kN (tração pura máxima)\r\n";
        String tNrdMax = "Nrd,máx= " + arredondar(nRdMax, 2) + " kN (compressão pura máxima)\r\n";
        String tMrd = "Mrd= " + arredondar(mRd / 100.0, 2) + " kN.m (para Nsd= " + arredondar(nd, 2) + " kN)\r\n";
        String tArmaduraLongitudinal = "Longitudinal: " + nBarrasAdotado + " #" + arredondar(secao.getBarraLongitudinal().getBitola() * 10, 2) + " mm\r\n";
        String tAsCalculado = "Asl= " + arredondar(asCalculado, 2) + " cm²\r\n";
        String tDominio = "Domínio " + dominio + "\r\n";

        //Resultados
        String tNsd = "Nsd= " + arredondar(nd, 2) + " kN\r\n";
        String tVsd = "Vsd= " + arredondar(vd, 2) + "kN\r\n";
        String tMsd = "Msd= " + arredondar(md / 100, 2) + " kN.m\r\n";

        if (vd > vrd2) {
            this.textAreaDim.setText("Resultados:\r\n" + tNsd + tVsd + tMsd + "\r\n" + tVrd2 + "RUPTURA por compressão da diagonal de concreto.");
        } else if (asCalculado < 0.0) {
            this.textAreaDim.setText("Resultados:\r\n" + tNsd + tVsd + tMsd + "\r\n" + "NÃO foi encontrada solução para a\r\nquantidade de barras necessárias para\r\nresistir à flexo-compressão.\r\n");
        } else {
            this.textAreaDim.setText("Resultados (Flexão):\r\n" + tAsCalculado + tArmaduraLongitudinal
                    + "\r\nResultados (Cisalhamento):\r\n" + tAsw + tArmaduraTransversal
                    + "\r\nResultados intermediários:\r\n" + tNsd + tVsd + tMsd
                    + "\r\nResultados intermediários (Flexo-compressão):\r\nPara nBarras= " + nBarrasAdotado + ":\r\n" + tNrdMin + tNrdMax + tMrd + tD + tDominio
                    + "\r\nResultados intermediários (Cisalhamento):\r\n" + tVrd2 + tVc0 + tVc + tVigaOuPilar + tAswCalculado + tAswMinima + tEspacamentoCalculado + tEspacamentoMaximo);
        }

        imagensDim(secao, nBarrasAdotado, espacamentoAdotado);

        System.setOut(outPadrao);
    }

    @FXML
    void onActionVerificarCisalhamento(ActionEvent event) {
        this.textAreaConsole.setText("Memória de cálculo:\r\n");
        System.setOut(outTextArea);

        double fck = Double.parseDouble(this.textFieldFck.getText()) / 10;
        double gamaC = Double.parseDouble(this.textFieldGamaC.getText());
        Concreto concreto = new Concreto(fck, gamaC);

        double fyk = Double.parseDouble(this.textFieldFykLong.getText()) / 10;
        double gamaS = Double.parseDouble(this.textFieldGamaSLong.getText());
        double bitola = Double.parseDouble(this.textFieldBitolaLong.getText()) / 10;
        Aco aco = new Aco(fyk, gamaS);
        BarraAco barraLong = new BarraAco(bitola, aco);

        double fykTransv = Double.parseDouble(this.textFieldFykTransv.getText()) / 10;
        double gamaSTransv = Double.parseDouble(this.textFieldGamaSTransv.getText());
        double bitolaTransv = Double.parseDouble(this.textFieldBitolaTransv.getText()) / 10;
        Aco acoTransv = new Aco(fykTransv, gamaSTransv);
        BarraAco barraTransv = new BarraAco(bitolaTransv, acoTransv);

        double diametro = Double.parseDouble(this.textFieldDiametro.getText());
        double cobrimento = Double.parseDouble(this.textFieldCobrimento.getText());
        Secao secao = new Secao(diametro, concreto, cobrimento, barraTransv, barraLong);

        double nk = Double.parseDouble(this.textFieldNkVerCis.getText());
        double mk = Double.parseDouble(this.textFieldMkVerCis.getText()) * 100;
        double gamaN = Double.parseDouble(this.textFieldGamaN.getText());
        Solicitacao solicitacao = new Solicitacao(nk, 0, mk, gamaN);

        double espacamento = Double.parseDouble(this.textFieldEspacamento.getText());
        double asw = Cisalhamento.asw(secao, espacamento, this.nRamos);

        double nsd = nk * gamaN;
        double msd = mk * gamaN;
        double vrd2 = Cisalhamento.vrd2(secao);
        double vc0 = Cisalhamento.vc0(secao, solicitacao);
        double vc = Cisalhamento.vc(vc0, secao, solicitacao);
        double vsw = Cisalhamento.vsw(asw, secao);
        double vrd = Cisalhamento.vRd(vrd2, vc, vsw);

        String tNsd = "Nsd= " + arredondar(nsd, 2) + " kN\r\n";
        String tMsd = "Msd= " + arredondar(msd / 100, 2) + " kN.m\r\n";
        String tVrd = "Vrd= " + arredondar(vrd, 2) + "kN\r\n";
        String tBw = "bw,equi= " + arredondar(secao.getBwEqui(), 2) + " cm\r\n";
        String tD = "dequi= " + arredondar(secao.getdEqui(), 2) + " cm\r\n";
        String tVrd2 = "Vrd2= " + arredondar(vrd2, 2) + "kN\r\n";
        String tVc0 = "Vc0= " + arredondar(vc0, 2) + "kN\r\n";
        String tVc = "Vc= " + arredondar(vc, 2) + "kN\r\n";
        String tVsw = "Vsw= " + arredondar(vsw, 2) + "kN";
        String tAsw = "Asw/s= " + arredondar(asw * 100, 2) + "cm²/m\r\n";

        this.textAreaVerCis.setText("Resultados:\r\n" + tNsd + tMsd + tVrd + "\r\nResultados intermediários:\r\n" + tAsw + tBw + tD + tVrd2 + tVc0 + tVc + tVsw);

        imagensVerCis(secao, espacamento);

        System.setOut(outPadrao);
    }

    @FXML
    void onActionVerificarFlexoCompressao(ActionEvent event) {
        this.textAreaConsole.setText("Memória de cálculo:\r\n");
        System.setOut(outTextArea);

        double fyk = Double.parseDouble(this.textFieldFykLong.getText()) / 10;
        double gamaS = Double.parseDouble(this.textFieldGamaSLong.getText());
        double eS = Double.parseDouble(this.textFieldEsLong.getText()) / 10;
        double bitola = Double.parseDouble(this.textFieldBitolaLong.getText()) / 10;
        Aco aco = new Aco(fyk, gamaS, eS);
        BarraAco barraLong = new BarraAco(bitola, aco);

        double bitolaTransv = Double.parseDouble(this.textFieldBitolaTransv.getText()) / 10;
        BarraAco barraTransv = new BarraAco(bitolaTransv, aco);

        double fck = Double.parseDouble(this.textFieldFck.getText()) / 10;
        double gamaC = Double.parseDouble(this.textFieldGamaC.getText());
        Concreto concreto = new Concreto(fck, gamaC);

        double diametro = Double.parseDouble(this.textFieldDiametro.getText());
        double cobrimento = Double.parseDouble(this.textFieldCobrimento.getText());
        Secao secao = new Secao(diametro, concreto, cobrimento, barraTransv, barraLong);

        double nK = Double.parseDouble(this.textFieldNkVerFlex.getText());
        double gamaN = Double.parseDouble(this.textFieldGamaN.getText());
        Solicitacao solicitacao = new Solicitacao(nK, 0, 0, gamaN);

        int nBarras = Integer.parseInt(this.textFieldNBarras.getText());
        double[] resposta = FlexoCompressao.x_d_nRdMin_nRdMax_mRd(secao, solicitacao, nBarras);
        double ksi = resposta[0] / resposta[1];
        String dominio = FlexoCompressao.domínio(resposta[0], resposta[1], secao, concreto, aco);

        String tNsd = "Nsd= " + arredondar(nK * gamaN, 2) + " kN\r\n";
        String tNrdmin = "Nrd,mín= " + arredondar(resposta[2], 2) + " kN (tração pura máxima)\r\n";
        String tNrdmax = "Nrd,máx= " + arredondar(resposta[3], 2) + " kN (compressão pura máxima)\r\n";

        if (resposta[4] < 0.0) {
            if (nK < 0.0) {
                this.textAreaVerFlex.setText("Resultados:\r\n" + tNsd + tNrdmin + "RUPTURA por tração simples!");
            } else {
                this.textAreaVerFlex.setText("Resultados:\r\n" + tNsd + tNrdmax + "RUPTURA por compressão simples!");
            }
        } else {
            String tX = "x= " + arredondar(resposta[0], 2) + " cm\r\n";
            String tD = "d= " + arredondar(resposta[1], 2) + " cm\r\n";
            String tXD = "x/d= " + arredondar(ksi, 4) + "\r\n";
            String tDominio = "Domínio " + dominio;
            String tMrd = "Mrd= " + arredondar(resposta[4] / 100, 2) + " kN.m\r\n";
            this.textAreaVerFlex.setText("Resultados:\r\n" + tNsd + tMrd + "\r\nResultados intermediários\r\n" + tNrdmin + tNrdmax + tX + tD + tXD + tDominio);
        }

        imagensVerFlex(secao, nBarras);

        System.setOut(outPadrao);
    }

    void imagensDim(Secao secao, int nBarras, double espacamento) {
        this.labelCotaDiametro.setText(arredondarToString(secao.getDiametro(), 2));
        this.labelNBarras.setText("" + nBarras);
        this.labelBitola.setText(arredondarToString(secao.getBarraLongitudinal().getBitola() * 10, 1) + "mm");

        this.labelEstribos.setText(arredondar(secao.getBarraTransversal().getBitola() * 10, 2) + "mm c/" + arredondar(espacamento, 1) + " cm");

        ativarImagensLong(true);
        ativarImagensTransv(true);
    }

    void imagensVerCis(Secao secao, double espacamento) {
        this.labelCotaDiametro.setText(arredondarToString(secao.getDiametro(), 2));

        this.labelEstribos.setText(arredondar(secao.getBarraTransversal().getBitola() * 10, 2) + "mm c/" + arredondar(espacamento, 1) + " cm");

        ativarImagensLong(false);
        ativarImagensTransv(true);
    }

    void imagensVerFlex(Secao secao, int nBarras) {
        this.labelCotaDiametro.setText(arredondarToString(secao.getDiametro(), 2));
        this.labelNBarras.setText("" + nBarras);
        this.labelBitola.setText(arredondarToString(secao.getBarraLongitudinal().getBitola() * 10, 1) + "mm");

        ativarImagensLong(true);
        ativarImagensTransv(false);
    }

    void ativarImagensLong(boolean isAtivo) {
        for (int i = 0; i < imagensLongitudinal.length; i++) {
            imagensLongitudinal[i].setVisible(isAtivo);
        }
    }

    void ativarImagensTransv(boolean isAtivo) {
        for (int i = 0; i < imagensTransversal.length; i++) {
            imagensTransversal[i].setVisible(isAtivo);
        }
    }

    void ativarLeituraConsole(boolean isAtivo) {
        OutputStream saida = new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        PrintStream p = new PrintStream(saida);
        System.setOut(p);
    }

    void setDadosCalculadora() {
        ControllerCenaInicial cenaInicial = Principal.getControllerCenaInicial();
        textFieldFck.setText(cenaInicial.textFieldFck.getText());
        textFieldGamaC.setText(cenaInicial.textFieldGamaC.getText());
        textFieldFykLong.setText(cenaInicial.textFieldFykLong.getText());
        textFieldGamaSLong.setText(cenaInicial.textFieldGamaSLong.getText());
        textFieldEsLong.setText(cenaInicial.textFieldEsLong.getText());
        textFieldBitolaLong.setText(cenaInicial.textFieldBitolaLong.getText());
        textFieldFykTransv.setText(cenaInicial.textFieldFykTransv.getText());
        textFieldGamaSTransv.setText(cenaInicial.textFieldGamaSTransv.getText());
        textFieldBitolaTransv.setText(cenaInicial.textFieldBitolaTransv.getText());
        textFieldDiametro.setText(cenaInicial.textFieldDiametroFuste.getText());
        textFieldGamaN.setText(cenaInicial.textFieldGamaN.getText());
        textFieldCobrimento.setText(cenaInicial.textFieldCobrimento.getText());

        textAreaConsole.clear();
        textAreaDim.clear();
        textAreaVerCis.clear();
        textAreaVerFlex.clear();

        for (int i = 0; i < imagensLongitudinal.length; i++) {
            imagensLongitudinal[i].setVisible(false);
        }
        imagensTransversal[0].setVisible(false);
        imagensTransversal[1].setVisible(false);

        this.labelCotaDiametro.setText(arredondarToString(Double.parseDouble(this.textFieldDiametro.getText()), 2));

    }
}
