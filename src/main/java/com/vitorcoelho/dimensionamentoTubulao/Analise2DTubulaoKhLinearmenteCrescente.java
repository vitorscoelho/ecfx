/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.dimensionamentoTubulao;

import com.vitorcoelho.dimensionamentoEstrutural.Solicitacao;
import com.vitorcoelho.auxiliarMath.Funcao;
import com.vitorcoelho.interfaceGrafica.Resultados;

import static java.lang.StrictMath.*;

/**
 * @author Vítor
 */
public strictfp class Analise2DTubulaoKhLinearmenteCrescente implements Analise2DTubulao {

    //Variaveis de instância
    private Tubulao tubulao;
    private SecaoFuste secaoFuste;
    private SecaoBase secaoBase;
    private double comprimento;
    private double normal;
    private double forcaHorizontal;
    private double momento;
    private double gamaN;

    private double rotacao;
    private double deltaH;
    private double deltaV;
    private double nh;
    private double kh;
    private double kv;
    private double coesao;
    private double anguloDeAtrito;
    private double pesoEspecificoSolo;
    private double ka;
    private double kp;
    private double tensaoVAdmissivel;

    private FuncaoCoeficienteReacaoFuste funcaoCoeficienteReacaoFuste;
    private FuncaoTensaoFuste funcaoTensaoFuste;
    private FuncaoTensaoHorizontalAdmissivel funcaoTensaoHorizontalAdmissivel;
    private FuncaoCoeficienteSegurancaEstabilidade funcaoCoeficienteSegurancaEstabilidade;
    private FuncaoReacaoHorizontalFuste funcaoReacaoHorizontalFuste;
    private FuncaoCortanteFuste funcaoCortanteFuste;
    private FuncaoMomentoFuste funcaoMomentoFuste;

    //Construtores
    public Analise2DTubulaoKhLinearmenteCrescente(Tubulao tubulao,
                                                  double normal, double forcaHorizontal, double momento, double gamaN, double nh, double kv,
                                                  double coesao, double anguloDeAtritoGraus, double pesoEspecificoSolo, double tensaoVAdmissivel) {
        this.tubulao = tubulao;
        this.secaoFuste = tubulao.getSecaoFuste();
        this.secaoBase = tubulao.getSecaoBase();
        this.comprimento = tubulao.getComprimento();
        this.normal = normal;
        this.forcaHorizontal = forcaHorizontal;
        this.momento = momento;
        this.gamaN = gamaN;
        this.nh = nh;
        this.kv = kv;
        this.coesao = coesao;
        this.anguloDeAtrito = toRadians(anguloDeAtritoGraus);
        this.pesoEspecificoSolo = pesoEspecificoSolo;
        this.tensaoVAdmissivel = tensaoVAdmissivel;

        this.ka = pow(tan(toRadians(45) - toRadians(anguloDeAtritoGraus / 2)), 2);
        this.kp = 1 / this.ka;

        this.kh = nh * comprimento / secaoFuste.getDimensaoX();

        this.rotacao = (3 * momento + 2 * forcaHorizontal * comprimento) / (3 * (this.kh * secaoFuste.getDimensaoX() * pow(comprimento, 3) / 36 + kv * secaoBase.getDimensaoX() * secaoBase.getModuloFlexao() / 2));
        this.deltaH = 2 * forcaHorizontal / (this.kh * secaoFuste.getDimensaoX() * comprimento) + 2 * this.rotacao * comprimento / 3;
        this.deltaV = normal / (kv * secaoBase.getArea());

        funcaoCoeficienteReacaoFuste = new FuncaoCoeficienteReacaoFuste();
        funcaoTensaoFuste = new FuncaoTensaoFuste();
        funcaoTensaoHorizontalAdmissivel = new FuncaoTensaoHorizontalAdmissivel();
        funcaoCoeficienteSegurancaEstabilidade = new FuncaoCoeficienteSegurancaEstabilidade();
        funcaoReacaoHorizontalFuste = new FuncaoReacaoHorizontalFuste();
        funcaoCortanteFuste = new FuncaoCortanteFuste();
        funcaoMomentoFuste = new FuncaoMomentoFuste();
    }

    //Getters
    @Override
    public double limiteComprimentoTubulaoCurto() {
        double moduloDeElasticidade = this.secaoFuste.getSecaoConcretoArmado().getConcreto().getModuloDeDeformacaoSecante();
        double inercia = this.secaoFuste.getSecaoConcretoArmado().getInercia();
        double limite = 4 * pow(moduloDeElasticidade * inercia / this.nh, 0.2);
        return limite;
    }

    @Override
    public boolean isCurto() {
        return (this.comprimento <= limiteComprimentoTubulaoCurto());
    }

    @Override
    public boolean isEstavel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getAlpha() {
        return this.rotacao;
    }

    @Override
    public double getDeslocamentoHxTopo() {
        return this.deltaH;
    }

    @Override
    public double getDeslocamentoVTopo() {
        return this.deltaV;
    }

    @Override
    public Solicitacao getSolicitacaoTopo() {
        return (new Solicitacao(normal, forcaHorizontal, momento, gamaN));
    }

    @Override
    public double getKa() {
        return this.ka;
    }

    @Override
    public double getKp() {
        return this.kp;
    }

    @Override
    public double getAnguloDeAtrito() {
        return this.anguloDeAtrito;
    }

    @Override
    public double getCoeficienteReacaoFuste(double profundidade) {
        return (this.kh * profundidade / this.comprimento);
    }

    @Override
    public double getTensaoFuste(double profundidade) {
        return (this.funcaoTensaoFuste.getFx(profundidade));
    }

    @Override
    public double getTensaoTensaoHorizontalAdmissivel(double profundidade) {
        return (this.funcaoTensaoHorizontalAdmissivel.getFx(profundidade));
    }

    @Override
    public double getCoeficienteSegurancaEstabilidade(double profundidade) {
        return this.funcaoCoeficienteSegurancaEstabilidade.getFx(profundidade);
    }

    @Override
    public double getReacaoHorizontalFuste(double profundidade) {
        return (getTensaoFuste(profundidade) * this.secaoFuste.getDimensaoX());
    }

    @Override
    public double getCortanteFuste(double profundidade) {
        return (this.funcaoCortanteFuste.getFx(profundidade));
    }

    @Override
    public double getMomentoFuste(double profundidade) {
        return (this.funcaoMomentoFuste.getFx(profundidade));
    }

    @Override
    public double getTensaoMaximaBase() {
        return (this.normal / secaoBase.getArea() + this.kv * this.rotacao * this.secaoBase.getDimensaoX() / 2);
    }

    @Override
    public double getTensaoMinimaBase() {
        return (this.normal / secaoBase.getArea() - this.kv * this.rotacao * this.secaoBase.getDimensaoX() / 2);
    }

    @Override
    public double getTensaoVAdmissivel() {
        return tensaoVAdmissivel;
    }

    @Override
    public Tubulao getTubulao() {
        return this.tubulao;
    }

    @Override
    public FuncaoCoeficienteReacaoFuste getFuncaoCoeficienteReacaoFuste() {
        return funcaoCoeficienteReacaoFuste;
    }

    @Override
    public FuncaoTensaoFuste getFuncaoTensaoFuste() {
        return funcaoTensaoFuste;
    }

    @Override
    public FuncaoTensaoHorizontalAdmissivel getFuncaoTensaoHorizontalAdmissivel() {
        return funcaoTensaoHorizontalAdmissivel;
    }

    @Override
    public FuncaoCoeficienteSegurancaEstabilidade getFuncaoCoeficienteSegurancaEstabilidade() {
        return funcaoCoeficienteSegurancaEstabilidade;
    }

    @Override
    public FuncaoReacaoHorizontalFuste getFuncaoReacaoFuste() {
        return funcaoReacaoHorizontalFuste;
    }

    @Override
    public FuncaoCortanteFuste getFuncaoCortanteFuste() {
        return funcaoCortanteFuste;
    }

    @Override
    public FuncaoMomentoFuste getFuncaoMomentoFuste() {
        return funcaoMomentoFuste;
    }

    //Classes internas (Funções)
    public class FuncaoCoeficienteReacaoFuste extends Funcao {

        @Override
        public double getFx(double x) {
            return (kh * x / comprimento);
        }

    }

    public class FuncaoTensaoFuste extends Funcao {

        private double Z1;

        private FuncaoTensaoFuste() {
            this.Z1 = kh / comprimento;
        }

        @Override
        public double getFx(double x) {
            return (-Z1 * x * (deltaH - rotacao * x));
        }
    }

    public class FuncaoTensaoHorizontalAdmissivel extends Funcao {

        private double Z1;
        private double Z2;

        private FuncaoTensaoHorizontalAdmissivel() {
            this.Z1 = 0.5 * kp - ka;
            this.Z2 = 2 * coesao * (0.5 * sqrt(kp) + sqrt(ka));
        }

        @Override
        public double getFx(double x) {
            return (pesoEspecificoSolo * x * this.Z1 + this.Z2);
        }

    }

    public class FuncaoCoeficienteSegurancaEstabilidade extends Funcao {

        private double Z1;
        private double Z2;
        private double Z3;

        private FuncaoCoeficienteSegurancaEstabilidade() {
            this.Z1 = kh / comprimento;
            this.Z2 = 0.5 * kp - ka;
            this.Z3 = 2 * coesao * (0.5 * sqrt(kp) + sqrt(ka));
        }

        @Override
        public double getFx(double x) {
            double tensaoAdmissivel = pesoEspecificoSolo * x * this.Z2 + this.Z3;
            double tensaoAtuante = Z1 * x * (deltaH - rotacao * x);
            if (tensaoAdmissivel <= 0.0001) {//PARA EVITAR UNDERFLOW
                return Resultados.coeficienteSegurancaMaximo;
            }
            return min(Resultados.coeficienteSegurancaMaximo, abs(tensaoAdmissivel / tensaoAtuante));
        }
        //VERIFICAR O ERRO QUE ESTÁ DANDO QUE NÃO APARECE NADA NO GRÁFICO. APESAR DE O GETTER ESTAR FUNCIONANDO
        //JÁ QUE É APRESENTADO O VALOR CORRETO ONDE PASSA O MOUSE
    }

    public class FuncaoReacaoHorizontalFuste extends Funcao {

        private double Z1;

        private FuncaoReacaoHorizontalFuste() {
            this.Z1 = kh / comprimento;
        }

        @Override
        public double getFx(double x) {
            return (secaoFuste.getDimensaoX() * Z1 * x * (deltaH - rotacao * x));
        }

    }

    public class FuncaoCortanteFuste extends Funcao {

        private double H;
        private double Z1;
        private double Z2;
        private double Z3;

        private FuncaoCortanteFuste() {
            this.H = forcaHorizontal;
            this.Z1 = kh * secaoFuste.getDimensaoX() / comprimento;
            this.Z2 = rotacao / 3;
            this.Z3 = deltaH / 2;
        }

        @Override
        public double getFx(double x) {
            return (H + (Z1 * x * x) * (Z2 * x - Z3));
        }

    }

    public class FuncaoMomentoFuste extends Funcao {

        private double M;
        private double H;
        private double Z1;
        private double Z2;
        private double Z3;

        private FuncaoMomentoFuste() {
            this.M = momento;
            this.H = forcaHorizontal;
            this.Z1 = kh * secaoFuste.getDimensaoX() / (6 * comprimento);
            this.Z2 = rotacao / 2;
            this.Z3 = deltaH;
        }

        @Override
        public double getFx(double x) {
            return (M + H * x + (Z1 * x * x * x) * (Z2 * x - Z3));
        }

    }
}
