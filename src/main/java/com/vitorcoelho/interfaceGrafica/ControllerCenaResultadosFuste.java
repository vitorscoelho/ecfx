/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.interfaceGrafica;

import com.vitorcoelho.auxiliarMath.Funcao;
import com.vitorcoelho.dimensionamentoTubulao.Analise2DTubulao;
import com.vitorcoelho.dimensionamentoTubulao.Tubulao;
import com.vitorcoelho.interfaceGrafica.Resultados.GraficoTubulao;
import static java.lang.StrictMath.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

/**
 * FXML Controller class
 *
 * @author Vítor
 */
public strictfp class ControllerCenaResultadosFuste extends ControllerCenaPadrao implements Initializable {

    @FXML Line lineGrafico;
    @FXML TextField textFieldProfundidade;

    @FXML ComboBox<TipoGrafico> comboBoxGrafico1;
    @FXML TextArea textAreaGrafico1;
    @FXML Label labelGrafico1;
    @FXML Label labelValorMouseGrafico1;
    private Funcao funcaoGrafico1;
    private TipoGrafico tipoGrafico1;
    @FXML private StackedAreaChart<?, ?> areaChart1;

    @FXML ComboBox<TipoGrafico> comboBoxGrafico2;
    @FXML TextArea textAreaGrafico2;
    @FXML Label labelGrafico2;
    @FXML Label labelValorMouseGrafico2;
    private Funcao funcaoGrafico2;
    private TipoGrafico tipoGrafico2;
    @FXML private StackedAreaChart<?, ?> areaChart2;

    @FXML ComboBox<TipoGrafico> comboBoxGrafico3;
    @FXML TextArea textAreaGrafico3;
    @FXML Label labelGrafico3;
    @FXML Label labelValorMouseGrafico3;
    private Funcao funcaoGrafico3;
    private TipoGrafico tipoGrafico3;
    @FXML private StackedAreaChart<?, ?> areaChart3;

    @FXML ComboBox<TipoGrafico> comboBoxGrafico4;
    @FXML TextArea textAreaGrafico4;
    @FXML Label labelGrafico4;
    @FXML Label labelValorMouseGrafico4;
    private Funcao funcaoGrafico4;
    private TipoGrafico tipoGrafico4;
    @FXML private StackedAreaChart<?, ?> areaChart4;

    private double profundidadeTubulao = 1;
    private final double Y_MINIMO = 181;
    private final double Y_MAXIMO = 616;
    private final double X_MINIMO = 0;
    private final double X_MAXIMO = 250;
    private final double DEFASAGEM_LINHA_MOUSE = 142;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Alimentando ComboBox
        ObservableList listaTipoGrafico = FXCollections.observableArrayList(
                TipoGrafico.CoeficienteDeReacao,
                TipoGrafico.TensaoHorizontal,
                TipoGrafico.Reacao,
                TipoGrafico.TensaoHorizontalResistente,
                TipoGrafico.SegurancaEstabilidade,
                TipoGrafico.Vk,
                TipoGrafico.Mk,
                TipoGrafico.ArmaduraLongitudinal,
                TipoGrafico.ArmaduraTransversal
        );

        this.comboBoxGrafico1.getItems().addAll(listaTipoGrafico);
        this.comboBoxGrafico2.getItems().addAll(listaTipoGrafico);
        this.comboBoxGrafico3.getItems().addAll(listaTipoGrafico);
        this.comboBoxGrafico4.getItems().addAll(listaTipoGrafico);

        //Adicionando listener ao ComboBox
        this.comboBoxGrafico1.valueProperty().addListener(getListenerComboBox(this.areaChart1, this.textAreaGrafico1, this.labelGrafico1));
        this.comboBoxGrafico2.valueProperty().addListener(getListenerComboBox(this.areaChart2, this.textAreaGrafico2, this.labelGrafico2));
        this.comboBoxGrafico3.valueProperty().addListener(getListenerComboBox(this.areaChart3, this.textAreaGrafico3, this.labelGrafico3));
        this.comboBoxGrafico4.valueProperty().addListener(getListenerComboBox(this.areaChart4, this.textAreaGrafico4, this.labelGrafico4));

        //Adicionando listener que será acionado quando o TextField de escolha da profundidade analisada perder o foco
        this.textFieldProfundidade.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == false) {
                atualizarProfundidade(textFieldProfundidade);
            }
        });

    }

    public void getGrafico(AreaChart graficoOndeSeraColocado) {
        /*NumberAxis eixoY = (NumberAxis) graficoOndeSeraColocado.getXAxis();
         NumberAxis eixoX = (NumberAxis) graficoOndeSeraColocado.getYAxis();
         double maximoY = lista.get(lista.size() - 1);
         double maximoX;
         double minimoX;

         eixoY.setLowerBound(0);
         eixoY.setUpperBound(maximoY);
         eixoX.setLowerBound(minimoX - 10);
         eixoX.setUpperBound(maximoX - 10);*/
    }

    @FXML
    void onActionAbrirCalculadora(ActionEvent event){
        Principal.abrirCalculadora();
    }
    
    @FXML
    private void movendoMouse(MouseEvent event) {
        double xPane = event.getY();
        double yPane = event.getX() + this.DEFASAGEM_LINHA_MOUSE;

        double profundidade = this.profundidadeTubulao * (yPane - this.Y_MINIMO) / (this.Y_MAXIMO - this.Y_MINIMO);

        if ((yPane >= this.Y_MINIMO && yPane <= this.Y_MAXIMO) && (xPane >= this.X_MINIMO && xPane <= this.X_MAXIMO)) {
            this.lineGrafico.setLayoutY(yPane);
        } else if (yPane >= this.Y_MAXIMO || yPane <= this.Y_MINIMO) {
            profundidade = max(min(profundidade, this.profundidadeTubulao), 0);
        }

        this.labelValorMouseGrafico1.setText(arredondar(this.funcaoGrafico1.getFx(profundidade * 100), 2) + this.tipoGrafico1.getUnidade() + " em " + arredondar(profundidade, 2) + "m");
        this.labelValorMouseGrafico2.setText(arredondar(this.funcaoGrafico2.getFx(profundidade * 100), 2) + this.tipoGrafico2.getUnidade() + " em " + arredondar(profundidade, 2) + "m");
        this.labelValorMouseGrafico3.setText(arredondar(this.funcaoGrafico3.getFx(profundidade * 100), 2) + this.tipoGrafico3.getUnidade() + " em " + arredondar(profundidade, 2) + "m");
        this.labelValorMouseGrafico4.setText(arredondar(this.funcaoGrafico4.getFx(profundidade * 100), 2) + this.tipoGrafico4.getUnidade() + " em " + arredondar(profundidade, 2) + "m");
        this.textFieldProfundidade.setText("" + arredondar(profundidade, 2));

    }

    @FXML
    private void mouseSaiuDoGrafico(MouseEvent event) {
        double profundidade = Double.parseDouble(this.textFieldProfundidade.getText());
        double yPane = profundidade * (this.Y_MAXIMO - this.Y_MINIMO) / this.profundidadeTubulao + this.Y_MINIMO;
        this.lineGrafico.setLayoutY(yPane);
    }

    @FXML
    protected void onKeyPressed1(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            TextField textField = (TextField) event.getTarget();
            atualizarProfundidade(textField);
        }
    }

    protected void atualizarProfundidade(TextField textField) {
        double profundidade = Double.parseDouble(textField.getText());

        if (profundidade > this.profundidadeTubulao) {
            profundidade = this.profundidadeTubulao;
        }

        textField.setText("" + arredondarToString(profundidade, 2));

        double yPane = profundidade * (this.Y_MAXIMO - this.Y_MINIMO) / this.profundidadeTubulao + this.Y_MINIMO;

        this.lineGrafico.setLayoutY(yPane);

        this.labelValorMouseGrafico1.setText(arredondar(this.funcaoGrafico1.getFx(profundidade * 100), 2) + this.tipoGrafico1.getUnidade() + " em " + arredondar(profundidade, 2) + "m");
        this.labelValorMouseGrafico2.setText(arredondar(this.funcaoGrafico2.getFx(profundidade * 100), 2) + this.tipoGrafico2.getUnidade() + " em " + arredondar(profundidade, 2) + "m");
        this.labelValorMouseGrafico3.setText(arredondar(this.funcaoGrafico3.getFx(profundidade * 100), 2) + this.tipoGrafico3.getUnidade() + " em " + arredondar(profundidade, 2) + "m");
        this.labelValorMouseGrafico4.setText(arredondar(this.funcaoGrafico4.getFx(profundidade * 100), 2) + this.tipoGrafico4.getUnidade() + " em " + arredondar(profundidade, 2) + "m");

    }

    //Setters
    private void setDados(StackedAreaChart graficoOndeSeraColocado, GraficoTubulao dadosDeGraficos, TipoGrafico tipoGrafico) {
        NumberAxis eixoY = (NumberAxis) graficoOndeSeraColocado.getXAxis();

        double maximoY = dadosDeGraficos.getComprimentTubulaoMetros();

        eixoY.setLowerBound(0);
        eixoY.setUpperBound(maximoY);

        graficoOndeSeraColocado.getData().remove(graficoOndeSeraColocado.getData());
        graficoOndeSeraColocado.getData().add(dadosDeGraficos.getListaValores());

        this.profundidadeTubulao = maximoY;

        if (graficoOndeSeraColocado.equals(this.areaChart1)) {
            this.funcaoGrafico1 = dadosDeGraficos.getFuncao();
            this.tipoGrafico1 = tipoGrafico;
        } else if (graficoOndeSeraColocado.equals(this.areaChart2)) {
            this.funcaoGrafico2 = dadosDeGraficos.getFuncao();
            this.tipoGrafico2 = tipoGrafico;
        } else if (graficoOndeSeraColocado.equals(this.areaChart3)) {
            this.funcaoGrafico3 = dadosDeGraficos.getFuncao();
            this.tipoGrafico3 = tipoGrafico;
        } else if (graficoOndeSeraColocado.equals(this.areaChart4)) {
            this.funcaoGrafico4 = dadosDeGraficos.getFuncao();
            this.tipoGrafico4 = tipoGrafico;
        }
    }

    //Listeners
    private ChangeListener getListenerComboBox(StackedAreaChart grafico, TextArea textAreaDescricao, Label labelMaximos) {
        ChangeListener listener = (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) -> {
            grafico.getData().clear();
            if (!newValue.equals(oldValue)) {
                TipoGrafico tipoGrafico = (TipoGrafico) newValue;
                textAreaDescricao.setText(tipoGrafico.getDescricao());
                labelMaximos.setText(tipoGrafico.getMaximoOuMinimo() + ": " + " " + tipoGrafico.getUnidade() + " em " + "m");

                if (tipoGrafico.equals(TipoGrafico.CoeficienteDeReacao)) {
                    GraficoTubulao graficoTubulao = Principal.getResultadosAnalise().getGraficoCoeficienteReacao();

                    setDados(grafico, graficoTubulao, tipoGrafico);
                    labelMaximos.setText(tipoGrafico.getMaximoOuMinimo() + ": " + " " + arredondar(graficoTubulao.getValorMaximo(), 2) + tipoGrafico.getUnidade() + " em " + arredondar(graficoTubulao.getProfundidadeValorMaximo() / 100, 2) + "m");
                } else if (tipoGrafico.equals(TipoGrafico.TensaoHorizontal)) {
                    GraficoTubulao graficoTubulao = Principal.getResultadosAnalise().getGraficoTensao();

                    setDados(grafico, graficoTubulao, tipoGrafico);
                    labelMaximos.setText(tipoGrafico.getMaximoOuMinimo() + ": " + " " + arredondar(graficoTubulao.getValorMaximo(), 2) + tipoGrafico.getUnidade() + " em " + arredondar(graficoTubulao.getProfundidadeValorMaximo() / 100, 2) + "m");
                } else if (tipoGrafico.equals(TipoGrafico.Reacao)) {
                    GraficoTubulao graficoTubulao = Principal.getResultadosAnalise().getGraficoReacao();

                    setDados(grafico, graficoTubulao, tipoGrafico);
                    labelMaximos.setText(tipoGrafico.getMaximoOuMinimo() + ": " + " " + arredondar(graficoTubulao.getValorMaximo(), 2) + tipoGrafico.getUnidade() + " em " + arredondar(graficoTubulao.getProfundidadeValorMaximo() / 100, 2) + "m");
                } else if (tipoGrafico.equals(TipoGrafico.TensaoHorizontalResistente)) {
                    GraficoTubulao graficoTubulao = Principal.getResultadosAnalise().getGraficoTensaoHorizontalResistente();

                    setDados(grafico, graficoTubulao, tipoGrafico);
                    labelMaximos.setText(tipoGrafico.getMaximoOuMinimo() + ": " + " " + arredondar(graficoTubulao.getValorMaximo(), 2) + tipoGrafico.getUnidade() + " em " + arredondar(graficoTubulao.getProfundidadeValorMaximo() / 100, 2) + "m");
                } else if (tipoGrafico.equals(TipoGrafico.SegurancaEstabilidade)) {
                    GraficoTubulao graficoTubulao=Principal.getResultadosAnalise().getGraficoCoeficienteSegurancaEstabilidade();
                    
                    setDados(grafico, graficoTubulao, tipoGrafico);
                    labelMaximos.setText(tipoGrafico.getMaximoOuMinimo() + ": " + " " + arredondar(graficoTubulao.getValorMaximo(), 2) + tipoGrafico.getUnidade() + " em " + arredondar(graficoTubulao.getProfundidadeValorMaximo() / 100, 2) + "m");
                } else if (tipoGrafico.equals(TipoGrafico.Vk)) {
                    GraficoTubulao graficoTubulao = Principal.getResultadosAnalise().getGraficoCortante();

                    setDados(grafico, graficoTubulao, tipoGrafico);
                    labelMaximos.setText(tipoGrafico.getMaximoOuMinimo() + ": " + " " + arredondar(graficoTubulao.getValorMaximo(), 2) + tipoGrafico.getUnidade() + " em " + arredondar(graficoTubulao.getProfundidadeValorMaximo() / 100, 2) + "m");
                } else if (tipoGrafico.equals(TipoGrafico.Mk)) {
                    GraficoTubulao graficoTubulao = Principal.getResultadosAnalise().getGraficoMomento();

                    setDados(grafico, graficoTubulao, tipoGrafico);
                    labelMaximos.setText(tipoGrafico.getMaximoOuMinimo() + ": " + " " + arredondar(graficoTubulao.getValorMaximo(), 2) + tipoGrafico.getUnidade() + " em " + arredondar(graficoTubulao.getProfundidadeValorMaximo() / 100, 2) + "m");
                } else if (tipoGrafico.equals(TipoGrafico.ArmaduraLongitudinal)) {
                    GraficoTubulao graficoTubulao = Principal.getResultadosAnalise().getGraficoNBarrasLongitudinais();

                    setDados(grafico, graficoTubulao, tipoGrafico);
                    labelMaximos.setText(tipoGrafico.getMaximoOuMinimo() + ": " + " " + arredondar(graficoTubulao.getValorMaximo(), 2) + tipoGrafico.getUnidade() + " em " + arredondar(graficoTubulao.getProfundidadeValorMaximo() / 100, 2) + "m");
                } else if (tipoGrafico.equals(TipoGrafico.ArmaduraTransversal)) {
                    GraficoTubulao graficoTubulao = Principal.getResultadosAnalise().getGraficoEspacamentoEstribos();

                    setDados(grafico, graficoTubulao, tipoGrafico);
                    labelMaximos.setText(tipoGrafico.getMaximoOuMinimo() + ": " + " " + arredondar(graficoTubulao.getValorMaximo(), 2) + tipoGrafico.getUnidade() + " em " + arredondar(graficoTubulao.getProfundidadeValorMaximo() / 100, 2) + "m");
                }
            }
        };
        return listener;
    }

    //Enums
    private enum TipoGrafico {

        CoeficienteDeReacao("kh (kN/m³)", "Máximo", "kN/m³", "Descrição:\r\nCoeficiente de reação horizontal ao longo do fuste em kN/m³."),
        TensaoHorizontal("TensaoH (kN/m²)", "Máxima", "kN/m²", "Descrição:\r\nTensão horizontal atuante característica no fuste em kN/m²."),
        Reacao("Reação (kN/m)", "Máxima", "kN/m", "Descrição:\r\nReação horizontal característica do solo no fuste em kN/m."),
        TensaoHorizontalResistente("TensãoH Adm (kN/m²)", "Máxima", "kN/m²", "Descrição:\r\nTensão horizontal admissível  do solo em kN/m².\r\nOBS.: No gráfico, não está sendo considerado o coeficiente de segurança."),
        SegurancaEstabilidade("C.S. Estabilidade", "Mínimo", "", "Descrição:\r\nCoeficiente de segurança da estabilidade horizontal do fuste.\r\nIgual a:\r\n(Tensão admissível)/(Tensão atuante)\r\nOBS.: No gráfico, o coeficiente de segurança está limitado a 20 para facilitar a apresentação dos resultados. Ou seja, mesmo que em um determinado ponto o coeficiente de segurança seja maior que 20, será apresentado como se fosse igual a 20."),
        Vk("Cortante (kN)", "Máximo", "kN", "Descrição:\r\nEsforço cortante característico no fuste em kN."),
        Mk("Momento Fletor (kN.m)", "Máximo", "kN.m", "Descrição:\r\nMomento fletor característico no fuste em kN.m."),
        ArmaduraLongitudinal("N° de barras longitudinais", "Máximo", "unid", "Descrição:\r\nQuantidade de barras necessárias na armadura longitudinal.\r\nOBS.1: Não considera comprimento de ancoragem e nem a decalagem do diagrama de momento fletor.\r\nOBS.2: O gráfico serve mais como referência qualitativa, pois, devido às incertezas existentes na escolha dos parâmetros do solo e na aplicação do método, é recomendável que se use, em todo o fuste do tubulão, a armadura máxima encontrada (a não ser que recaia no caso de armadura mínima apenas nos três primeiros metros de profundidade)"),
        ArmaduraTransversal("Espaçamento estribo (cm)", "Mínimo", "cm", "Descrição:\r\nEspaçamento necessário entre as barras da armadura transversal em cm.\r\nOBS.1: Quando apresentado espaçamento igual a -1, significa que não é necessária a colocação de estribos.");

        private String nome;
        private String maximoOuMinimo;
        private String unidade;
        private String descricao;

        private TipoGrafico(String nome, String maximoOuMinimo, String unidade, String descricao) {
            this.nome = nome;
            this.maximoOuMinimo = maximoOuMinimo;
            this.unidade = unidade;
            this.descricao = descricao;
        }

        @Override
        public String toString() {
            return this.nome;
        }

        public String getMaximoOuMinimo() {
            return this.maximoOuMinimo;
        }

        public String getUnidade() {
            return this.unidade;
        }

        public String getDescricao() {
            return this.descricao;
        }

    }

    public void resetarGraficosIniciais() {
        //Mudança apenas para, de fato, resetar o grafico
        this.comboBoxGrafico1.getSelectionModel().select(TipoGrafico.CoeficienteDeReacao);
        this.comboBoxGrafico2.getSelectionModel().select(TipoGrafico.CoeficienteDeReacao);
        this.comboBoxGrafico3.getSelectionModel().select(TipoGrafico.CoeficienteDeReacao);
        this.comboBoxGrafico4.getSelectionModel().select(TipoGrafico.CoeficienteDeReacao);

        //Setando os graficos iniciais
        this.comboBoxGrafico1.getSelectionModel().select(TipoGrafico.TensaoHorizontal);
        this.comboBoxGrafico1.getSelectionModel().select(TipoGrafico.CoeficienteDeReacao);
        this.comboBoxGrafico2.getSelectionModel().select(TipoGrafico.TensaoHorizontal);
        this.comboBoxGrafico3.getSelectionModel().select(TipoGrafico.Vk);
        this.comboBoxGrafico4.getSelectionModel().select(TipoGrafico.Mk);
    }
    
}
