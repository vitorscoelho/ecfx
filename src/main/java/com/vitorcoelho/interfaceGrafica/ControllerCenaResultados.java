/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.interfaceGrafica;

import com.vitorcoelho.dimensionamentoEstrutural.Cisalhamento;
import com.vitorcoelho.dimensionamentoEstrutural.FlexoCompressao;
import com.vitorcoelho.dimensionamentoEstrutural.Secao;
import com.vitorcoelho.dimensionamentoTubulao.Analise2DTubulao;
import com.vitorcoelho.dimensionamentoTubulao.Tubulao;
import com.vitorcoelho.dimensionamentoTubulao.dimensionamentoEstruturalTubulao;
import static java.lang.StrictMath.toDegrees;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Vítor
 */
public strictfp class ControllerCenaResultados extends ControllerCenaPadrao implements Initializable {

    @FXML Label labelArmaduraAoLongoDoFuste;

    @FXML Label labelCotaDiametro;
    @FXML Label labelNBarras;
    @FXML Label labelBitolaLongitudinal;
    @FXML Label labelBitolaEstribo;
    @FXML Label labelEspacamentoEstribo;

    @FXML TextField textFieldRotacao;
    @FXML TextField textFieldTranslacaoH;
    @FXML TextField textFieldTranslacaoV;

    @FXML TextField textFieldTensaoMediaBaseAtuante;
    @FXML TextField textFieldTensaoMaximaBaseAtuante;
    @FXML TextField textFieldTensaoMinimaBaseAtuante;
    @FXML TextField textFieldTensaoMediaBaseAdmissivel;
    @FXML TextField textFieldTensaoMaximaBaseAdmissivel;

    @FXML TextField textFieldNormal;
    @FXML TextField textFieldCortante;
    @FXML TextField textFieldMomento;
    @FXML TextField textFieldTensaoHorizontal;

    @FXML TextField textFieldVolFuste;
    @FXML TextField textFieldVolBase;
    @FXML TextField textFieldVolTotal;

    @FXML TextField textFieldTensaoMediaAtuanteFuste;
    @FXML TextField textFieldTensaoMediaLimiteFuste;

    @FXML TextField textFieldKa;
    @FXML TextField textFieldKp;
    @FXML TextField textFieldAnguloDeAtrito;

    @FXML TextField textFieldAslMin;
    @FXML TextField textFieldAslMax;
    @FXML TextField textFieldAslAdot;
    @FXML TextField textFieldAstAdot;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void onActionButtonFuste(ActionEvent event) {
        Principal.getStageCenaResultadosFuste().show();
    }

    @FXML
    void onActionAbrirCalculadora(ActionEvent event) {
        Principal.abrirCalculadora();
    }

    //Métodos públicos
    public void setDadosResumo() {
        dimensionamentoEstruturalTubulao.TipoEstaca tipoEstaca = (dimensionamentoEstruturalTubulao.TipoEstaca) Principal.getControllerCenaInicial().comboBoxTipoFundacao.getSelectionModel().getSelectedItem();
        Resultados resultados = Principal.getResultadosAnalise();
        Analise2DTubulao analise = Principal.getAnaliseTubulao();
        Secao secaoFuste = analise.getTubulao().getSecaoFuste().getSecaoConcretoArmado();
        Tubulao tubulao = analise.getTubulao();

        if (resultados.isArmadoSomentePrimeirosMetros() && (tipoEstaca.getComprimentoMinimoArmadura(Principal.getControllerCenaInicial().spinnerComprimentoMinimoArmadura) > 0.0)) {
            this.labelArmaduraAoLongoDoFuste.setText("Armar somente nos primeiros " + arredondar(tipoEstaca.getComprimentoMinimoArmadura(Principal.getControllerCenaInicial().spinnerComprimentoMinimoArmadura) / 100, 2) + " metros de fuste.");
        } else {
            this.labelArmaduraAoLongoDoFuste.setText("Armar ao longo de todo o fuste.");
        }

        this.labelNBarras.setText(arredondarToString(resultados.getGraficoNBarrasLongitudinais().getValorMaximo(), 0));
        this.labelBitolaLongitudinal.setText(arredondarToString(secaoFuste.getBarraLongitudinal().getBitola() * 10, 2));
        this.labelBitolaEstribo.setText(arredondarToString(secaoFuste.getBarraTransversal().getBitola() * 10, 2));
        this.labelEspacamentoEstribo.setText(arredondarToString(resultados.getGraficoEspacamentoEstribos().getValorMaximo(), 2));
        this.labelCotaDiametro.setText(arredondarToString(secaoFuste.getDiametro(), 2));

        this.textFieldRotacao.setText(arredondarToString(Principal.getAnaliseTubulao().getAlpha(), 5));
        this.textFieldTranslacaoH.setText(arredondarToString(Principal.getAnaliseTubulao().getDeslocamentoHxTopo() * 10, 2));
        this.textFieldTranslacaoV.setText(arredondarToString(Principal.getAnaliseTubulao().getDeslocamentoVTopo() * 10, 2));

        this.textFieldTensaoMediaBaseAtuante.setText(arredondarToString(((Principal.getAnaliseTubulao().getTensaoMaximaBase() + Principal.getAnaliseTubulao().getTensaoMinimaBase()) / 2) * 10000, 2));
        this.textFieldTensaoMaximaBaseAtuante.setText(arredondarToString(Principal.getAnaliseTubulao().getTensaoMaximaBase() * 10000, 2));
        this.textFieldTensaoMinimaBaseAtuante.setText(arredondarToString(Principal.getAnaliseTubulao().getTensaoMinimaBase() * 10000, 2));
        this.textFieldTensaoMediaBaseAdmissivel.setText(arredondarToString(Principal.getAnaliseTubulao().getTensaoVAdmissivel() * 10000, 2));
        this.textFieldTensaoMaximaBaseAdmissivel.setText(arredondarToString(Principal.getAnaliseTubulao().getTensaoVAdmissivel() * Principal.getControllerCenaParametros().getMajoradorFlexoCompressao() * 10000, 2));

        this.textFieldNormal.setText(arredondarToString(resultados.analiseTubulao.getSolicitacaoTopo().getNk(), 2));
        this.textFieldCortante.setText(arredondarToString(resultados.getGraficoCortante().getValorMaximo(), 2));
        this.textFieldMomento.setText(arredondarToString(resultados.getGraficoMomento().getValorMaximo(), 2));
        this.textFieldTensaoHorizontal.setText(arredondarToString(resultados.getGraficoTensao().getValorMaximo(), 2));

        this.textFieldVolFuste.setText(arredondarToString(tubulao.getVolumeFuste() / 1000000, 2));
        this.textFieldVolBase.setText(arredondarToString(tubulao.getVolumeBase() / 1000000, 2));
        this.textFieldVolTotal.setText(arredondarToString(tubulao.getVolumeTotal() / 1000000, 2));

        this.textFieldTensaoMediaAtuanteFuste.setText(arredondarToString(10 * resultados.analiseTubulao.getSolicitacaoTopo().getNk() / secaoFuste.getArea(), 2));
        this.textFieldTensaoMediaLimiteFuste.setText(arredondarToString(Principal.getControllerCenaInicial().spinnerTensaoMaximaSemArmadura.getValue(), 2));

        this.textFieldKa.setText(arredondarToString(analise.getKa(), 2));
        this.textFieldKp.setText(arredondarToString(analise.getKp(), 2));
        this.textFieldAnguloDeAtrito.setText(arredondarToString(toDegrees(analise.getAnguloDeAtrito()), 2));

        this.textFieldAslMin.setText(arredondarToString(secaoFuste.getArea() * FlexoCompressao.getPorcentagemAsMin() / 100, 2));
        this.textFieldAslMax.setText(arredondarToString(secaoFuste.getArea() * FlexoCompressao.getPorcentagemAsMax() / 100, 2));
        this.textFieldAslAdot.setText(arredondarToString(Double.parseDouble(this.labelNBarras.getText()) * secaoFuste.getBarraLongitudinal().getArea(), 2));

        this.textFieldAstAdot.setText(arredondarToString(100*Cisalhamento.asw(secaoFuste, resultados.getGraficoEspacamentoEstribos().getValorMaximo(), 2), 2));

        Principal.getControllerCenaResultadosFuste().textFieldProfundidade.setText("0.00");
        Principal.getControllerCenaResultadosFuste().labelValorMouseGrafico1.setText("");
        Principal.getControllerCenaResultadosFuste().labelValorMouseGrafico2.setText("");
        Principal.getControllerCenaResultadosFuste().labelValorMouseGrafico3.setText("");
        Principal.getControllerCenaResultadosFuste().labelValorMouseGrafico4.setText("");
    }

}
