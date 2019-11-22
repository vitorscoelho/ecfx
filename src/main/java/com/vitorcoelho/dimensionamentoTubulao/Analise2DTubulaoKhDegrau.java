package com.vitorcoelho.dimensionamentoTubulao;

import com.vitorcoelho.auxiliarMath.Funcao;
import com.vitorcoelho.dimensionamentoEstrutural.Solicitacao;

import static java.lang.StrictMath.*;

public strictfp class Analise2DTubulaoKhDegrau implements Analise2DTubulao {

    private Tubulao tubulao;
    private SecaoFuste secaoFuste;
    private SecaoBase secaoBase;
    private double comprimento;
    private double normal;
    private double forcaHorizontal;
    private double momento;
    private double gamaN;

    private double kv;
    private double k1;
    private double kh1;
    private double kh2;
    private double l1;

    private double coesao;
    private double anguloDeAtrito;
    private double pesoEspecificoSolo;
    private double ka;
    private double kp;
    private double tensaoVAdmissivel;

    //Construtores
    public Analise2DTubulaoKhDegrau(Tubulao tubulao,
                                    double normal, double forcaHorizontal, double momento, double gamaN, double k1, double kv,
                                    double coesao, double anguloDeAtritoGraus, double pesoEspecificoSolo, double tensaoVAdmissivel) {
        this.tubulao = tubulao;
        this.secaoFuste = tubulao.getSecaoFuste();
        this.secaoBase = tubulao.getSecaoBase();
        this.comprimento = tubulao.getComprimento();
        this.normal = normal;
        this.forcaHorizontal = forcaHorizontal;
        this.momento = momento;
        this.gamaN = gamaN;

        this.kv = kv;
        this.k1 = k1;
        this.kh2 = k1 / this.secaoFuste.getDimensaoX();
        this.kh1 = 0.5 * kh2;

        double moduloDeElasticidade = this.secaoFuste.getSecaoConcretoArmado().getConcreto().getModuloDeDeformacaoSecante();
        double inercia = this.secaoFuste.getSecaoConcretoArmado().getInercia();
        this.l1 = 0.4 * pow(moduloDeElasticidade * inercia / this.k1, 0.25);

        this.coesao = coesao;
        this.anguloDeAtrito = toRadians(anguloDeAtritoGraus);
        this.pesoEspecificoSolo = pesoEspecificoSolo;
        this.tensaoVAdmissivel = tensaoVAdmissivel;

        this.ka = pow(tan(toRadians(45) - toRadians(anguloDeAtritoGraus / 2)), 2);
        this.kp = 1 / this.ka;

        /*
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
         */
    }

    @Override
    public double limiteComprimentoTubulaoCurto() {
        double moduloDeElasticidade = this.secaoFuste.getSecaoConcretoArmado().getConcreto().getModuloDeDeformacaoSecante();
        double inercia = this.secaoFuste.getSecaoConcretoArmado().getInercia();
        double limite = 4 * pow(moduloDeElasticidade * inercia / this.k1, 0.25);
        return limite;
    }

    @Override
    public boolean isCurto() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEstavel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getAlpha() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getDeslocamentoHxTopo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getDeslocamentoVTopo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Solicitacao getSolicitacaoTopo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getTensaoVAdmissivel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getKa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getKp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getAnguloDeAtrito() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getCoeficienteReacaoFuste(double profundidade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getTensaoFuste(double profundidade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getTensaoTensaoHorizontalAdmissivel(double profundidade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getCoeficienteSegurancaEstabilidade(double profundidade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getReacaoHorizontalFuste(double profundidade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getCortanteFuste(double profundidade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMomentoFuste(double profundidade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Funcao getFuncaoCoeficienteReacaoFuste() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Funcao getFuncaoTensaoFuste() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Funcao getFuncaoTensaoHorizontalAdmissivel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Funcao getFuncaoCoeficienteSegurancaEstabilidade() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Funcao getFuncaoReacaoFuste() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Funcao getFuncaoCortanteFuste() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Funcao getFuncaoMomentoFuste() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getTensaoMaximaBase() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getTensaoMinimaBase() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tubulao getTubulao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //Funções
    private static class FuncaoCoeficienteReacaoFuste {

        public FuncaoCoeficienteReacaoFuste() {
        }
    }

    private static class FuncaoTensaoFuste {

        public FuncaoTensaoFuste() {
        }
    }

}
