/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.dimensionamentoTubulao;

import com.vitorcoelho.dimensionamentoEstrutural.Cisalhamento;
import com.vitorcoelho.dimensionamentoEstrutural.FlexoCompressao;
import com.vitorcoelho.dimensionamentoEstrutural.Secao;
import com.vitorcoelho.dimensionamentoEstrutural.Solicitacao;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;

/**
 *
 * @author Vítor
 */
public class dimensionamentoEstruturalTubulao {

    public static double tensaoMediaMaximaConcretoSimples = 0.5;
    public static double comprimentoMinimoArmadura = 300;

    //Construtores
    private dimensionamentoEstruturalTubulao() {
    }

    //Métodos públicos
    //Retorna a quantidade de barras longitudinais necessária em uma dada seção
    public static int nBarrasLongitudinais(Tubulao tubulao, Solicitacao solicitacao, int nBarrasMin, int nBarrasMax, double profundidade) {
        if (isArmado(tubulao, solicitacao, profundidade)) {
            Secao secao = tubulao.getSecaoFuste().getSecaoConcretoArmado();
            int nBarras = FlexoCompressao.nBarrasAdotado(secao, solicitacao, FlexoCompressao.nBarrasNecessario(secao, solicitacao, nBarrasMin, nBarrasMax));
            return nBarras;
        }
        return 0;
    }

    public static double espacamentoEstribo(Tubulao tubulao, Solicitacao solicitacao, int nRamos, double profundidade) {
        if (isArmado(tubulao, solicitacao, profundidade)) {
            Secao secao = tubulao.getSecaoFuste().getSecaoConcretoArmado();
            return Cisalhamento.espacamentoLongitudinalAdotado(secao, solicitacao, nRamos);
        }
        return -1;
    }

    private static boolean isArmado(Tubulao tubulao, Solicitacao solicitacao, double profundidade) {
        if (profundidade <= comprimentoMinimoArmadura) {
            return true;
        }

        double tensaoMedia = solicitacao.getNd() / tubulao.getSecaoFuste().getSecaoConcretoArmado().getArea();
        if (tensaoMedia > dimensionamentoEstruturalTubulao.tensaoMediaMaximaConcretoSimples) {
            return true;
        }

        double tensaoMinima = tensaoMedia - solicitacao.getMd() / tubulao.getSecaoFuste().getSecaoConcretoArmado().getW0();
        double tracaoLimite = -(tubulao.getSecaoFuste().getSecaoConcretoArmado().getConcreto().getFctd() / 1.2);
        if (tensaoMinima < tracaoLimite) {
            return true;
        }

        double cisalhamentoAtuante = 4 * solicitacao.getVd() / (3 * tubulao.getSecaoFuste().getSecaoConcretoArmado().getArea());
        double cisalhamentoMaximo = tubulao.getSecaoFuste().getSecaoConcretoArmado().getConcreto().getTwrdSimples(tensaoMedia);

        if (cisalhamentoAtuante > cisalhamentoMaximo) {
            return true;
        }

        return false;
    }

    //Enums
    public enum TipoEstaca {

        EstacaPersonalizada("Estaca personalizada", 2, 1.8, 1.15, 400, 0.6, true, false),
        TubulaoPersonalizado("Tubulão personalizado", 2, 1.8, 1.15, 300, 0.5, true, true),
        Helice("Estaca hélice", 2, 1.8, 1.15, 400, 0.6, false, false),
        EscavadaSemFluido("Estaca escavada sem fluido", 1.5, 1.9, 1.15, 200, 0.5, false, false),
        EscavadaComFluido("Estaca escavada com fluido", 2, 1.8, 1.15, 400, 0.6, false, false),
        Strauss("Estaca Strauss", 1.5, 1.9, 1.15, 200, 0.5, false, false),
        Franki("Estaca Franki", 2, 1.8, 1.15, -100, 0, false, false),
        Tubulao("Tubulão não encamisado", 2, 1.8, 1.15, 300, 0.5, false, true),
        Raiz("Estaca raiz", 2, 1.6, 1.15, -100, 0, false, false),
        MicroEstaca("Microestaca", 2, 1.8, 1.15, -100, 0, false, false),
        EstacaTradoVazado("Estaca trado vazado segmetado", 2, 1.8, 1.15, -100, 0, false, false);

        private String descricaoTipoEstaca;
        private double fck;
        private double gamaC;
        private double gamaS;
        private double comprimentoMinimoArmadura;
        private double tensaoMediaLimiteSemArmadura;
        private boolean isPersonalizada;
        private boolean isTubulao;

        private TipoEstaca(String descricaoTipoEstaca, double fck, double gamaC, double gamaS, double comprimentoMinimoArmadura, double tensaoMediaLimiteSemArmadura, boolean isPersonalizada, boolean isTubulao) {
            this.descricaoTipoEstaca = descricaoTipoEstaca;
            this.fck = fck;
            this.gamaC = gamaC;
            this.gamaS = gamaS;
            this.comprimentoMinimoArmadura = comprimentoMinimoArmadura;
            this.tensaoMediaLimiteSemArmadura = tensaoMediaLimiteSemArmadura;
            this.isPersonalizada = isPersonalizada;
            this.isTubulao = isTubulao;
        }

        public double getFck() {
            return fck;
        }

        public double getGamaC() {
            return gamaC;
        }

        public double getGamaS() {
            return gamaS;
        }

        public double getComprimentoMinimoArmadura(Spinner<Double> spinnerComComprimentoMinimo) {
            if(this.isPersonalizada){
                return (spinnerComComprimentoMinimo.getValueFactory().getValue()*100);
            }
            return comprimentoMinimoArmadura;
        }

        public double getTensaoMediaLimiteSemArmadura(Spinner<Double> spinnerComValorDaTensao) {
            if(this.isPersonalizada){
                return tensaoMediaLimiteSemArmadura;
            }
            return tensaoMediaLimiteSemArmadura;
        }

        public boolean isPersonalizada() {
            return isPersonalizada;
        }

        public boolean isTubulao() {
            return isTubulao;
        }

        @Override
        public String toString() {
            return descricaoTipoEstaca;
        }
    }
}
