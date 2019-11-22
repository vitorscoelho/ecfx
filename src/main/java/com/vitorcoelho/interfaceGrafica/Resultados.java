package com.vitorcoelho.interfaceGrafica;

import com.vitorcoelho.auxiliarMath.Funcao;
import com.vitorcoelho.dimensionamentoEstrutural.Solicitacao;
import com.vitorcoelho.dimensionamentoTubulao.Analise2DTubulao;
import com.vitorcoelho.dimensionamentoTubulao.Tubulao;
import com.vitorcoelho.dimensionamentoTubulao.dimensionamentoEstruturalTubulao;
import javafx.scene.chart.XYChart;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.StrictMath.*;

public strictfp class Resultados {

    private final double VARIACAO_PROFUNDIDADE = 1.0;
    private final double VARIACAO_PROFUNDIDADE_TRECHOS = 50.0;

    public static double coeficienteSegurancaMaximo = 20.0;

    Tubulao tubulao;
    Analise2DTubulao analiseTubulao;

    private GraficoTubulao graficoCoeficienteReacao;
    private GraficoTubulao graficoTensao;
    private GraficoTubulao graficoReacao;
    private GraficoTubulao graficoCortante;
    private GraficoTubulao graficoMomento;
    private GraficoTubulao graficoNBarrasLongitudinais;
    private GraficoTubulao graficoEspacamentoEstribos;
    private GraficoTubulao graficoTensaoHorizontalResistente;
    private GraficoTubulao graficoCoeficienteSegurancaEstabilidade;

    private boolean isArmadoSomentePrimeirosMetros;

    public Resultados(Analise2DTubulao analiseTubulao) {
        //Gerar os gráficos e resultados de armaduras tensões na base e resumo de resultados
        this.tubulao = analiseTubulao.getTubulao();
        this.analiseTubulao = analiseTubulao;
    }

    //Métodos públicos
    public void processarGraficos() {

        Thread t1 = new Thread(new GeraGrafico(1), "GraficoCoeficienteReacao");
        Thread t2 = new Thread(new GeraGrafico(2), "GraficoTensao");
        Thread t3 = new Thread(new GeraGrafico(3), "GraficoReacao");
        Thread t4 = new Thread(new GeraGrafico(4), "GraficoCortante");
        Thread t5 = new Thread(new GeraGrafico(5), "GraficoMomento");
        Thread t6 = new Thread(new GeraGrafico(6), "GraficoTensaoHorizontalResistente");
        Thread t7 = new Thread(new GeraGrafico(7), "GraficoCoeficienteSegurancaEstabilidade");

        long inicio = System.nanoTime();

        //Processamento dos gráficos de esforços
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Resultados.class.getName()).log(Level.SEVERE, null, ex);
        }


        /*t1.start();
         t1.join();
         t2.start();
         t2.join();
         t3.start();
         t3.join();
         t4.start();
         t4.join();
         t5.start();
         t5.join();
         t6.start();
         t6.join();
         t7.start();
         t7.join();*/
        //Processamento dos gráficos de armadura
        DadosGraficoArmadurasTubulao dadosGraficoArmaduras = new DadosGraficoArmadurasTubulao(this.graficoMomento.getTrechos(), this.graficoCortante.getTrechos());
        this.isArmadoSomentePrimeirosMetros = dadosGraficoArmaduras.isArmadoSomentePrimeirosMetros;
        this.graficoNBarrasLongitudinais = new GraficoArmadurasTubulao(tubulao, 1, dadosGraficoArmaduras, TipoGraficoArmadura.Longitudinal);
        this.graficoEspacamentoEstribos = new GraficoArmadurasTubulao(tubulao, 1, dadosGraficoArmaduras, TipoGraficoArmadura.Estribo);

        long termino = System.nanoTime();

        System.out.println((termino - inicio) / pow(10, 9) + " segundos");
    }

    //Nos getters, tive que criar um novo objeto por conta de um bug no gráfico.
    //Quando selecionava uma mesma série em mais de um gráfico, ela ficava fixada, mesmo que mudasse na comboBox.
    public GraficoTubulao getGraficoCoeficienteReacao() {
        graficoCoeficienteReacao.refreshGraficoTubulao();
        return graficoCoeficienteReacao;
    }

    public GraficoTubulao getGraficoTensao() {
        graficoTensao.refreshGraficoTubulao();
        return graficoTensao;
    }

    public GraficoTubulao getGraficoTensaoHorizontalResistente() {
        graficoTensaoHorizontalResistente.refreshGraficoTubulao();
        return graficoTensaoHorizontalResistente;
    }

    public GraficoTubulao getGraficoCoeficienteSegurancaEstabilidade() {
        graficoCoeficienteSegurancaEstabilidade.refreshGraficoTubulao();
        return graficoCoeficienteSegurancaEstabilidade;
    }

    public GraficoTubulao getGraficoReacao() {
        graficoReacao.refreshGraficoTubulao();
        return graficoReacao;
    }

    public GraficoTubulao getGraficoCortante() {
        graficoCortante.refreshGraficoTubulao();
        return graficoCortante;
    }

    public GraficoTubulao getGraficoMomento() {
        graficoMomento.refreshGraficoTubulao();
        return graficoMomento;
    }

    public GraficoTubulao getGraficoNBarrasLongitudinais() {
        graficoCortante.refreshGraficoTubulao();
        return this.graficoNBarrasLongitudinais;
    }

    public GraficoTubulao getGraficoEspacamentoEstribos() {
        graficoCortante.refreshGraficoTubulao();
        return this.graficoEspacamentoEstribos;
    }

    public boolean isArmadoSomentePrimeirosMetros() {
        return this.isArmadoSomentePrimeirosMetros;
    }

    //Classes internas
    public class GraficoTubulao {

        //Variáveis de instância
        private XYChart.Series listaValores = new XYChart.Series();
        private double valorMaximo = 0;
        private double comprimentoTubulao;

        private double profundidadeValorMaximo = 0;

        private double multiplicadorValores;

        private Funcao funcaoParaValores;

        private Tubulao tubulao;

        private TrechoGrafico[] trechos;

        private GraficoTubulao() {
        }

        public GraficoTubulao(Tubulao tubulao, Funcao funcaoParaValores, double multiplicadorValores, String minimoOuMaximo) {

            //System.out.println("Criou gráfico novo");

            this.funcaoParaValores = funcaoParaValores;
            this.multiplicadorValores = multiplicadorValores;
            this.tubulao = tubulao;

            int qtdTrechos = (int) (tubulao.getComprimento() / VARIACAO_PROFUNDIDADE_TRECHOS);
            if (qtdTrechos * VARIACAO_PROFUNDIDADE_TRECHOS < tubulao.getComprimento()) {
                qtdTrechos++;
            }

            //System.out.println(qtdTrechos);
            trechos = new TrechoGrafico[qtdTrechos];

            double profundidade = 0;

            ///Para máximos
            if (minimoOuMaximo.equals("máximo")) {
                for (int nTrecho = 1; nTrecho <= qtdTrechos; nTrecho++) {
                    double profInicial = profundidade;
                    double profFinal = min(profInicial + VARIACAO_PROFUNDIDADE_TRECHOS, tubulao.getComprimento());

                    double maximo = 0;
                    double profMaximo = 0;

                    for (profundidade = profInicial; profundidade <= profFinal; profundidade += VARIACAO_PROFUNDIDADE) {
                        double valor = funcaoParaValores.getFx(profundidade) * multiplicadorValores;
                        listaValores.getData().add(new XYChart.Data(profundidade / 100, valor));

                        if (abs(valor) > maximo) {
                            maximo = abs(valor);
                            profMaximo = profundidade;
                        }
                    }

                    if (maximo > this.valorMaximo) {
                        this.valorMaximo = maximo;
                        this.profundidadeValorMaximo = profMaximo;
                    }

                    TrechoGrafico trechoAtual = new TrechoGrafico(profInicial, profFinal, profMaximo, maximo);
                    trechos[nTrecho - 1] = trechoAtual;

                    //System.out.println("Trecho: " + nTrecho + "///ProfInicial= " + profInicial + "///ProfFinal= " + profFinal + "///ValorMaximo= " + maximo + "///ProfMaximo= " + profMaximo);
                    profundidade--;
                }
            } else {
                //Para mínimos
                double profInicial = 0;
                double profFinal = tubulao.getComprimento();

                double minimo = abs(funcaoParaValores.getFx(profundidade) * multiplicadorValores);
                double profMinimo = 0;

                for (profundidade = profInicial; profundidade <= profFinal; profundidade += VARIACAO_PROFUNDIDADE) {
                    double valor = funcaoParaValores.getFx(profundidade) * multiplicadorValores;
                    listaValores.getData().add(new XYChart.Data(profundidade / 100, valor));

                    if (abs(valor) < abs(minimo)) {
                        minimo = abs(valor);
                        profMinimo = profundidade;
                    }
                }

                this.valorMaximo = minimo;
                this.profundidadeValorMaximo = profMinimo;
            }

            comprimentoTubulao = tubulao.getComprimento();
        }

        //Getters
        public double getComprimentTubulaoMetros() {
            return comprimentoTubulao / 100;
        }

        public double getValorMaximo() {
            return valorMaximo;
        }

        public XYChart.Series getListaValores() {
            return listaValores;
        }

        public double getProfundidadeValorMaximo() {
            return profundidadeValorMaximo;
        }

        public Funcao getFuncao() {
            Funcao funcao = new Funcao() {
                @Override
                public double getFx(double x) {
                    return getFuncaoParaValores().getFx(x) * getMultiplicadorValores();
                }
            };

            return funcao;
        }

        public double getMultiplicadorValores() {
            return multiplicadorValores;
        }

        public Funcao getFuncaoParaValores() {
            return funcaoParaValores;
        }

        public Tubulao getTubulao() {
            return tubulao;
        }

        public TrechoGrafico[] getTrechos() {
            return this.trechos;
        }

        public void refreshGraficoTubulao() {
            this.listaValores = new XYChart.Series(this.listaValores.getData());
        }

    }

    public class GraficoArmadurasTubulao extends GraficoTubulao {

        //Variáveis de instância
        private XYChart.Series listaValores;
        private double valorMaximo = 0;
        private double comprimentoTubulao;

        private double profundidadeValorMaximo = 50.0;

        private double multiplicadorValores;

        private Funcao funcaoParaValores;

        private Tubulao tubulao;

        public GraficoArmadurasTubulao(Tubulao tubulao, double multiplicadorValores, DadosGraficoArmadurasTubulao dadosGraficoArmaduras, TipoGraficoArmadura tipoDoGrafico) {
            //System.out.println("Criou gráfico de armadura novo");
            this.comprimentoTubulao = tubulao.getComprimento();

            this.multiplicadorValores = multiplicadorValores;
            this.tubulao = tubulao;

            if (tipoDoGrafico.equals(TipoGraficoArmadura.Longitudinal)) {
                this.funcaoParaValores = dadosGraficoArmaduras.getFuncaoNBarrasLongitudinais();
                this.listaValores = dadosGraficoArmaduras.getListaNBarrasLongitudinais();
                this.valorMaximo = dadosGraficoArmaduras.getNBarrasLongitudinaisMaximo();
                this.profundidadeValorMaximo = dadosGraficoArmaduras.getProfundidadeNBarrasMaximo();
            } else if (tipoDoGrafico.equals(TipoGraficoArmadura.Estribo)) {
                this.funcaoParaValores = dadosGraficoArmaduras.getFuncaoEspacamentoEstribo();
                this.listaValores = dadosGraficoArmaduras.getListaEspacamentosEstribos();
                this.valorMaximo = dadosGraficoArmaduras.getEspacamentoMinimoEstribos();
                this.profundidadeValorMaximo = dadosGraficoArmaduras.getProfundidadeEspacamentoMinimoEstribo();
            }

        }

        //Getters
        @Override
        public double getComprimentTubulaoMetros() {
            return comprimentoTubulao / 100;
        }

        @Override
        public double getValorMaximo() {
            return valorMaximo;
        }

        @Override
        public XYChart.Series getListaValores() {
            return listaValores;
        }

        @Override
        public double getProfundidadeValorMaximo() {
            return profundidadeValorMaximo;
        }

        @Override
        public Funcao getFuncao() {
            return funcaoParaValores;
        }

        @Override
        public double getMultiplicadorValores() {
            return multiplicadorValores;
        }

        @Override
        public Funcao getFuncaoParaValores() {
            return funcaoParaValores;
        }

        @Override
        public Tubulao getTubulao() {
            return tubulao;
        }

        @Override
        public void refreshGraficoTubulao() {
            this.listaValores = new XYChart.Series(this.listaValores.getData());
        }
    }

    public class GeraGrafico implements Runnable {

        private int numeroGrafico;

        public GeraGrafico(int numeroGrafico) {
            this.numeroGrafico = numeroGrafico;
        }

        @Override
        public void run() {
            if (this.numeroGrafico == 1) {
                graficoCoeficienteReacao = new GraficoTubulao(tubulao, analiseTubulao.getFuncaoCoeficienteReacaoFuste(), 1000000, "máximo");
            } else if (this.numeroGrafico == 2) {
                graficoTensao = new GraficoTubulao(tubulao, analiseTubulao.getFuncaoTensaoFuste(), 10000, "máximo");
            } else if (this.numeroGrafico == 3) {
                graficoReacao = new GraficoTubulao(tubulao, analiseTubulao.getFuncaoReacaoFuste(), 100, "máximo");
            } else if (this.numeroGrafico == 4) {
                graficoCortante = new GraficoTubulao(tubulao, analiseTubulao.getFuncaoCortanteFuste(), 1, "máximo");
            } else if (this.numeroGrafico == 5) {
                graficoMomento = new GraficoTubulao(tubulao, analiseTubulao.getFuncaoMomentoFuste(), 0.01, "máximo");
            } else if (this.numeroGrafico == 6) {
                graficoTensaoHorizontalResistente = new GraficoTubulao(tubulao, analiseTubulao.getFuncaoTensaoHorizontalAdmissivel(), 10000, "máximo");
            } else if (this.numeroGrafico == 7) {
                graficoCoeficienteSegurancaEstabilidade = new GraficoTubulao(tubulao, analiseTubulao.getFuncaoCoeficienteSegurancaEstabilidade(), 1, "mínimo");
            }
        }
    }

    public class TrechoGrafico {

        private double profundidadeInicial;
        private double profundidadeFinal;
        private double valorMaximo;
        private double profundidadeValorMaximo;

        public TrechoGrafico(double profundidadeInicial, double profundidadeFinal, double profundidadeValorMaximo, double valorMaximo) {
            this.profundidadeInicial = profundidadeInicial;
            this.profundidadeFinal = profundidadeFinal;
            this.profundidadeValorMaximo = profundidadeValorMaximo;
            this.valorMaximo = valorMaximo;
        }

        public double getProfundidadeInicial() {
            return profundidadeInicial;
        }

        public double getProfundidadeFinal() {
            return profundidadeFinal;
        }

        public double getValorMaximo() {
            return valorMaximo;
        }

        public double getProfundidadeValorMaximo() {
            return profundidadeValorMaximo;
        }
    }

    public class DadosGraficoArmadurasTubulao {

        //Variáveis de instância
        private XYChart.Series listaNBarrasLongitudinais = new XYChart.Series();
        private XYChart.Series listaEspacamentosEstribos = new XYChart.Series();

        private FuncaoNBarrasLongitudinais funcaoNBarrasLongitudinais;
        private FuncaoEspacamentoEstribo funcaoEspacamentoEstribo;

        private int qtdTrechos;
        private double[] profundidadesIniciais;
        private double[] profundidadesFinais;
        private int[] nBarras;
        private double[] espacamentos;

        private double nBarrasMaximo = 0;
        private double espacamentoMinimoEstribo = 999999.9;
        private double profundidadeNBarrasMaximo = 0.0;
        private double profundidadeEspacamentoMinimoEstribo = 0.0;

        boolean isArmadoSomentePrimeirosMetros = true;

        public DadosGraficoArmadurasTubulao(TrechoGrafico[] trechosMomentos, TrechoGrafico[] trechoCortantes) {
            qtdTrechos = trechosMomentos.length;

            profundidadesIniciais = new double[qtdTrechos];
            profundidadesFinais = new double[qtdTrechos];
            nBarras = new int[qtdTrechos];
            espacamentos = new double[qtdTrechos];

            double normal = Principal.getAnaliseTubulao().getSolicitacaoTopo().getNk();
            double gamaN = Principal.getAnaliseTubulao().getSolicitacaoTopo().getNd() / normal;

            for (int i = 0; i < qtdTrechos; i++) {
                profundidadesIniciais[i] = trechoCortantes[i].getProfundidadeInicial();
                profundidadesFinais[i] = trechoCortantes[i].getProfundidadeFinal();
                double cortante = trechoCortantes[i].getValorMaximo();
                double momento = trechosMomentos[i].getValorMaximo();
                Solicitacao solicitacao = new Solicitacao(normal, cortante, momento * 100, gamaN);

                nBarras[i] = dimensionamentoEstruturalTubulao.nBarrasLongitudinais(tubulao, solicitacao, 6, 1000, profundidadesIniciais[i] + 0.01);//O valor 0.01 foi somado na profundidade inicial apenas para que um trecho que comece em cima do 300 de profundidade não seja armado
                espacamentos[i] = dimensionamentoEstruturalTubulao.espacamentoEstribo(tubulao, solicitacao, 2, profundidadesIniciais[i] + 0.01);//O valor 0.01 foi somado na profundidade inicial apenas para que um trecho que comece em cima do 300 de profundidade não seja armado

                if ((espacamentos[i] >= -0.1) & (profundidadesFinais[i] >= (dimensionamentoEstruturalTubulao.comprimentoMinimoArmadura + 5))) {
                    isArmadoSomentePrimeirosMetros = false;
                }

                listaNBarrasLongitudinais.getData().add(new XYChart.Data(profundidadesIniciais[i] / 100, nBarras[i]));
                listaNBarrasLongitudinais.getData().add(new XYChart.Data(profundidadesFinais[i] / 100, nBarras[i]));

                listaEspacamentosEstribos.getData().add(new XYChart.Data(profundidadesIniciais[i] / 100, espacamentos[i]));
                listaEspacamentosEstribos.getData().add(new XYChart.Data(profundidadesFinais[i] / 100, espacamentos[i]));

                if (nBarrasMaximo <= nBarras[i]) {
                    nBarrasMaximo = nBarras[i];
                    profundidadeNBarrasMaximo = profundidadesFinais[i];
                }

                if (espacamentos[i] >= 0 & espacamentoMinimoEstribo >= espacamentos[i]) {
                    espacamentoMinimoEstribo = espacamentos[i];
                    profundidadeEspacamentoMinimoEstribo = profundidadesFinais[i];
                }

            }

            funcaoNBarrasLongitudinais = new FuncaoNBarrasLongitudinais();
            funcaoEspacamentoEstribo = new FuncaoEspacamentoEstribo();
        }

        public double getProfundidadeNBarrasMaximo() {
            return profundidadeNBarrasMaximo;
        }

        public double getProfundidadeEspacamentoMinimoEstribo() {
            return profundidadeEspacamentoMinimoEstribo;
        }

        //Funções
        public class FuncaoNBarrasLongitudinais extends Funcao {

            @Override
            public double getFx(double x) {
                for (int i = 0; i < qtdTrechos; i++) {
                    if (x <= (profundidadesFinais[i] + 0.0001)) {
                        return nBarras[i];
                    }
                }
                return -450;
            }
        }

        public class FuncaoEspacamentoEstribo extends Funcao {

            @Override
            public double getFx(double x) {
                for (int i = 0; i < qtdTrechos; i++) {
                    if (x <= (profundidadesFinais[i] + 0.0001)) {
                        return espacamentos[i];
                    }
                }
                return -450;
            }
        }

        //Getters
        public XYChart.Series getListaNBarrasLongitudinais() {
            return this.listaNBarrasLongitudinais;
        }

        public XYChart.Series getListaEspacamentosEstribos() {
            return this.listaEspacamentosEstribos;
        }

        public FuncaoNBarrasLongitudinais getFuncaoNBarrasLongitudinais() {
            return funcaoNBarrasLongitudinais;
        }

        public FuncaoEspacamentoEstribo getFuncaoEspacamentoEstribo() {
            return funcaoEspacamentoEstribo;
        }

        public double getNBarrasLongitudinaisMaximo() {
            return this.nBarrasMaximo;
        }

        public double getEspacamentoMinimoEstribos() {
            return this.espacamentoMinimoEstribo;
        }

    }

    //Enums
    public enum TipoGraficoArmadura {

        Longitudinal(),
        Estribo();
    }

}
