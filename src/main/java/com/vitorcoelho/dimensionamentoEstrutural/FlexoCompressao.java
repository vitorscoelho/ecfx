package com.vitorcoelho.dimensionamentoEstrutural;

import com.vitorcoelho.auxiliarMath.Funcao;
import com.vitorcoelho.auxiliarMath.Interpolacao;
import com.vitorcoelho.auxiliarMath.Quadratura;

import static java.lang.StrictMath.*;

public strictfp class FlexoCompressao {

    //Todas as unidades desta classe deverão estar em kN e cm.
    //Declaração da variáveis de instância
    private static final double eStu = -10.0; //Alongamento máximo (deformação específica mínima) permitido para o aço no concreto armado
    private static int nPontosGaussParabola = 10;
    private static int nPontosGaussRetangulo = 10;
    private static final double deltaNtolerancia = 0.0001;
    private static double porcentagemToleranciaMRdMSd = 0.0;
    private static double porcentagemAsMin = 0.5;
    private static double porcentagemAsMax = 8;

    //Construtores
    private FlexoCompressao() {
    }

    //Métodos públicos
    public static int nBarrasNecessario(Secao secao, Solicitacao solicitacao, int nBarrasMin, int nBarrasMax) {
        int nBarras = nBarrasMin - 1;
        double mSd = abs(solicitacao.getMd()) * (1.0 - FlexoCompressao.porcentagemToleranciaMRdMSd / 100.0);
        double mRd = 0.0;

        while ((mRd < mSd) && (nBarras <= nBarrasMax)) {
            nBarras++;
            mRd = mRd(secao, solicitacao, nBarras);
        }

        if (mRd < mSd) {
            return -999999;
        }
        return nBarras;
    }

    public static int nBarrasAdotado(Secao secao, Solicitacao solicitacao, int nBarrasNecessario) {
        double asMin = asMin(secao, solicitacao);
        int nBarrasMinima = (int) (asMin / secao.getBarraLongitudinal().getArea()) + 1;

        return (max(nBarrasNecessario, nBarrasMinima));
    }

    public static double mRd(Secao secao, Solicitacao solicitacao, int nBarras) {
        double nSd = solicitacao.getNd();
        BarraAco barra = secao.getBarraLongitudinal();
        double asTotal = nBarras * barra.getArea();
        Concreto concreto = secao.getConcreto();
        Aco aco = barra.getAco();

        double mRd = 0.0;

        //Testando se ocorre ruptura por compressão
        double eCGMax = secao.getConcreto().geteC2();
        double nRdMax = concreto.tensao(eCGMax) * (secao.getArea() - asTotal) + aco.tensao(eCGMax) * asTotal;

        if (nSd > nRdMax) {
            ///CRIAR EXCEPTION???
            return (-9999999.0);
        }

        //Início do "for" para avaliar duas situações de posicionamento das barras
        for (int i = 0; i <= 1; i++) {
            double rUtil = secao.getDiametro() / 2.0 - secao.getdLinha();
            double alphaInicial = 1.5 * PI + PI * i / nBarras;
            double[] yBarras = espalhamentoDeBarras(alphaInicial, rUtil, nBarras);

            //Encontrando yMáximo e yMínimo
            double yMax = 0.0;
            double yMin = 0.0;
            for (double y : yBarras) {
                if (y > yMax) {
                    yMax = y;
                }
                if (y < yMin) {
                    yMin = y;
                }
                yMin = abs(yMin);
            }

            //Testando se ocorre ruptura por tração
            double d = yMin + secao.getDiametro() / 2.0;
            double eSmaxAdot = max(aco.geteYd(), FlexoCompressao.eStu);
            double eCGMin = yMax * (FlexoCompressao.eStu + eSmaxAdot) / (yMax + yMin) - eSmaxAdot;
            double nRdMin = nRd(eCGMin, secao, yBarras, d);

            if (nSd < nRdMin) {
                ///CRIAR EXCEPTION???
                return (-100000.0);
            }

            FuncaoDeltaN funcaoDeltaN = new FuncaoDeltaN(nSd, d, secao, yBarras);
            double eCG = Interpolacao.raizMetodoPegaso(eCGMin, eCGMax, nSd - nRdMin, nSd - nRdMax, funcaoDeltaN, FlexoCompressao.deltaNtolerancia);

            //Pegando o menor valor para Mrd entre as duas situações analisadas de posicionamento de barras
            if (i == 0) {
                mRd = mRd(eCG, secao, yBarras, d);
            } else {
                mRd = min(mRd, mRd(eCG, secao, yBarras, d));
            }
        }
        return mRd;
    }

    public static double[] x_d_nRdMin_nRdMax_mRd_nBarrasNecessarias_AsCalculado(Secao secao, Solicitacao solicitacao, int nBarrasMin, int nBarrasMax) {
        double mSd = abs(solicitacao.getMd()) * (1.0 - FlexoCompressao.porcentagemToleranciaMRdMSd / 100.0);

        double x = -999.0;
        double d = -999.0;
        double nRdMin = -999.0;
        double nRdMax = -999.0;
        double mRd = 0.0;
        int nBarras = nBarrasMin - 1;

        double[] resposta = new double[7];

        while ((mRd < mSd) && (nBarras <= nBarrasMax)) {
            nBarras++;
            double[] respostaParcial = x_d_nRdMin_nRdMax_mRd(secao, solicitacao, nBarras);
            x = respostaParcial[0];
            d = respostaParcial[1];
            nRdMin = respostaParcial[2];
            nRdMax = respostaParcial[3];
            mRd = respostaParcial[4];
            System.out.println("nBarras= " + nBarras);
            System.out.println("Mrd= " + mRd);
        }

        if (mRd < mSd) {
            for (int i = 1; i < 7; i++) {
                resposta[i] = -99999.0;
            }
            return resposta;
        }
        resposta[0] = x;
        resposta[1] = d;
        resposta[2] = nRdMin;
        resposta[3] = nRdMax;
        resposta[4] = mRd;
        resposta[5] = nBarras;
        resposta[6] = nBarras * secao.getBarraLongitudinal().getArea();
        System.out.println("Área= " + resposta[6]);

        return resposta;
    }

    public static double[] x_d_nRdMin_nRdMax_mRd(Secao secao, Solicitacao solicitacao, int nBarras) {
        double nSd = solicitacao.getNd();
        BarraAco barra = secao.getBarraLongitudinal();
        double asTotal = nBarras * barra.getArea();
        Concreto concreto = secao.getConcreto();
        Aco aco = barra.getAco();

        double yLn = -999999.0;
        double dMin = 0.0;
        double nRdMin = 0;
        double mRd = 0.0;
        double[] resposta = new double[5];

        //Testando se ocorre ruptura por compressão
        double eCGMax = secao.getConcreto().geteC2();
        double nRdMax = concreto.tensao(eCGMax) * (secao.getArea() - asTotal) + aco.tensao(eCGMax) * asTotal;

        if (nSd > nRdMax) {
            ///CRIAR EXCEPTION???
            yLn = dMin = mRd = -9999999.0;
        } else {
            //Início do "for" para avaliar duas situações de posicionamento das barras
            for (int i = 0; i <= 1; i++) {
                double rUtil = secao.getDiametro() / 2.0 - secao.getdLinha();
                double alphaInicial = 1.5 * PI + PI * i / nBarras;
                double[] yBarras = espalhamentoDeBarras(alphaInicial, rUtil, nBarras);

                //Encontrando yMáximo e yMínimo
                double yMax = 0.0;
                double yMin = 0.0;
                for (double y : yBarras) {
                    if (y > yMax) {
                        yMax = y;
                    }
                    if (y < yMin) {
                        yMin = y;
                    }
                    yMin = abs(yMin);
                }

                //Testando se ocorre ruptura por tração
                double d = yMin + secao.getDiametro() / 2.0;
                double eSmaxAdot = max(aco.geteYd(), FlexoCompressao.eStu);
                double eCGMin = yMax * (FlexoCompressao.eStu + eSmaxAdot) / (yMax + yMin) - eSmaxAdot;
                nRdMin = nRd(eCGMin, secao, yBarras, d);

                if (nSd < nRdMin) {
                    ///CRIAR EXCEPTION???
                    yLn = dMin = mRd = -1000.0;
                } else {
                    FuncaoDeltaN funcaoDeltaN = new FuncaoDeltaN(nSd, d, secao, yBarras);
                    double eCG = Interpolacao.raizMetodoPegaso(eCGMin, eCGMax, nSd - nRdMin, nSd - nRdMax, funcaoDeltaN, FlexoCompressao.deltaNtolerancia);

                    //Pegando o menor valor para Mrd entre as duas situações analisadas de posicionamento de barras
                    if (i == 0) {
                        dMin = d;
                        yLn = -eCG / curvatura(eCG, dMin, secao);
                        mRd = mRd(eCG, secao, yBarras, d);
                    } else {
                        double mRdAtual = mRd(eCG, secao, yBarras, d);
                        if (mRdAtual < mRd) {
                            dMin = d;
                            yLn = -eCG / curvatura(eCG, dMin, secao);
                            mRd = mRdAtual;
                        }
                    }
                }
            }
        }

        resposta[0] = secao.getDiametro() / 2 - yLn;
        resposta[1] = dMin;
        resposta[2] = nRdMin;
        resposta[3] = nRdMax;
        resposta[4] = mRd;
        return resposta;
    }

    public static double asMin(Secao secao, Solicitacao solicitacao) {
        double asMin = 0.15 * solicitacao.getNd() / secao.getBarraLongitudinal().getAco().getFyd();
        return (max(asMin, secao.getArea() * FlexoCompressao.porcentagemAsMin / 100.0));
    }

    public static double asMax(Secao secao) {
        return (secao.getArea() * FlexoCompressao.porcentagemAsMax / 100.0);
    }

    //Método públicos intermediários
    public static double[] espalhamentoDeBarras(double alphaInicial, double rUtil, int nBarras) {
        double deltaAlpha = 2.0 * PI / nBarras;
        double alpha = alphaInicial;

        double[] yBarras = new double[nBarras];

        for (int i = 0; i < nBarras; i++) {
            yBarras[i] = rUtil * sin(alpha);
            alpha = alpha + deltaAlpha;
        }

        return yBarras;
    }

    public static double curvatura(double eCG, double d, Secao secao) {
        double D = secao.getDiametro();

        //Região I
        double eCu = secao.getConcreto().geteCu();
        double eC2 = secao.getConcreto().geteC2();
        double x5 = (eCu - eC2) * D / eCu;
        double curvatura1 = (eC2 - eCG) / (0.5 * D - x5);

        //Região II
        double curvatura2 = (eCu - eCG) * 2.0 / D;

        //Região III
        double curvatura3 = (eCG - FlexoCompressao.eStu) / (d - 0.5 * D);

        return (min(curvatura1, min(curvatura2, curvatura3)));
    }

    public static double deformacao(double eCG, double curvatura, double y) {
        return (y * curvatura + eCG);
    }

    public static double bw(double y, Secao secao) {
        double bw = 2 * sqrt(0.25 * pow(secao.getDiametro(), 2.0) - pow(y, 2.0));
        return bw;
    }

    public static double nRd(double eCG, Secao secao, double[] yBarras, double d) {
        //Parcela da normal resistida pelo aço
        Aco aco = secao.getBarraLongitudinal().getAco();
        Concreto concreto = secao.getConcreto();

        double curvatura = curvatura(eCG, d, secao);
        double nRdAco = 0.0;

        for (double y : yBarras) {
            double deformacao = deformacao(eCG, curvatura, y);
            double tensaoAco = aco.tensao(deformacao);
            double tensaoConcreto = concreto.tensao(deformacao);
            double tensaoAdotada = tensaoAco - tensaoConcreto;
            nRdAco += tensaoAdotada;
        }

        nRdAco *= secao.getBarraLongitudinal().getArea();

        //Parcela da normal resistida pelo concreto
        double yLn = -eCG / curvatura;
        double yC2 = (concreto.geteC2() - eCG) / curvatura;

        double y3 = 0.5 * secao.getDiametro();

        double y1;
        double y2;
        if (curvatura == 0) {
            y1 = -y3;
            y2 = -y3;
        } else {
            y1 = max(-y3, min(yLn, y3));
            y2 = max(-y3, min(yC2, y3));
        }

        FuncaoNRdiConcreto funcaoNrdiConcreto = new FuncaoNRdiConcreto(eCG, curvatura, secao);
        double nRdConcretoParabola = Quadratura.integral(y1, y2, funcaoNrdiConcreto, FlexoCompressao.nPontosGaussParabola);
        double nRdConcretoRetangulo = Quadratura.integral(y2, y3, funcaoNrdiConcreto, FlexoCompressao.nPontosGaussRetangulo);

        double nRdd = nRdAco + nRdConcretoParabola + nRdConcretoRetangulo;

        return nRdd;
    }

    public static double mRd(double eCG, Secao secao, double[] yBarras, double d) {
        //Parcela da normal resistida pelo aço
        Aco aco = secao.getBarraLongitudinal().getAco();
        Concreto concreto = secao.getConcreto();

        double curvatura = curvatura(eCG, d, secao);
        double mRdAco = 0.0;

        for (double y : yBarras) {
            double deformacao = deformacao(eCG, curvatura, y);
            double tensaoAco = aco.tensao(deformacao);
            double tensaoConcreto = concreto.tensao(deformacao);
            double tensaoAdotada = tensaoAco - tensaoConcreto;
            mRdAco += tensaoAdotada * y;
        }

        mRdAco *= secao.getBarraLongitudinal().getArea();

        //Parcela da normal resistida pelo concreto
        double yLn = -eCG / curvatura;
        double yC2 = (concreto.geteC2() - eCG) / curvatura;

        double y3 = secao.getDiametro() / 2.0;

        double y1;
        double y2;
        if (curvatura == 0) {
            y1 = -y3;
            y2 = -y3;
        } else {
            y1 = max(-y3, min(yLn, y3));
            y2 = max(-y3, min(yC2, y3));
        }

        FuncaoMRdiConcreto funcaoMrdiConcreto = new FuncaoMRdiConcreto(eCG, curvatura, secao);
        double mRdConcretoParabola = Quadratura.integral(y1, y2, funcaoMrdiConcreto, nPontosGaussParabola);
        double mRdConcretoRetangulo = Quadratura.integral(y2, y3, funcaoMrdiConcreto, nPontosGaussRetangulo);

        double mRdd = mRdAco + mRdConcretoParabola + mRdConcretoRetangulo;

        return mRdd;
    }

    public static String domínio(double x, double d, Secao secao, Concreto concreto, Aco aco) {
        if (x <= 0) {
            return "1";
        }

        double eCu = concreto.geteCu();
        double xLim = eCu * d / (eCu - FlexoCompressao.eStu);
        if (x <= xLim) {
            return "2";
        }

        xLim = eCu * d / (eCu + aco.geteYd());
        if (x <= xLim) {
            return "3";
        }

        xLim = d;
        if (x <= xLim) {
            return "4";
        }

        xLim = secao.getDiametro();
        if (x <= xLim) {
            return "4a";
        }

        return "5";

    }

    //Classes internas
    public strictfp static class FuncaoNRdiConcreto extends Funcao {

        private static double eCG;
        private static double curvatura;
        private static Secao secao;

        private FuncaoNRdiConcreto(double eCG, double curvatura, Secao secao) {
            FuncaoNRdiConcreto.eCG = eCG;
            FuncaoNRdiConcreto.curvatura = curvatura;
            FuncaoNRdiConcreto.secao = secao;
        }

        @Override
        public double getFx(double y) {
            double nRdX = secao.getConcreto().tensao(deformacao(eCG, curvatura, y)) * bw(y, secao);
            return nRdX;
        }
    }

    public static class FuncaoMRdiConcreto extends Funcao {

        private static double eCG;
        private static double curvatura;
        private static Secao secao;

        private FuncaoMRdiConcreto(double eCG, double curvatura, Secao secao) {
            FuncaoMRdiConcreto.eCG = eCG;
            FuncaoMRdiConcreto.curvatura = curvatura;
            FuncaoMRdiConcreto.secao = secao;
        }

        @Override
        public double getFx(double y) {
            double mRdX = secao.getConcreto().tensao(deformacao(eCG, curvatura, y)) * bw(y, secao) * y;
            return mRdX;
        }
    }

    public static class FuncaoDeltaN extends Funcao {

        private static double nSd;
        private static double d;
        private static Secao secao;
        private static double[] yBarras;

        private FuncaoDeltaN(double nSd, double d, Secao secao, double[] yBarras) {
            FuncaoDeltaN.nSd = nSd;
            FuncaoDeltaN.d = d;
            FuncaoDeltaN.secao = secao;
            FuncaoDeltaN.yBarras = yBarras;
        }

        @Override
        public double getFx(double eCG) {
            double nRd = nRd(eCG, secao, yBarras, d);
            double resultado = nSd - nRd;
            return resultado;
        }
    }

    //Getters e setters
    public static double getPorcentagemAsMin() {
        return porcentagemAsMin;
    }

    public static double getPorcentagemAsMax() {
        return porcentagemAsMax;
    }

    public static void setPorcentagemAsMin(double aPorcentagemAsMin) {
        porcentagemAsMin = aPorcentagemAsMin;
    }

    public static void setPorcentagemAsMax(double aPorcentagemAsMax) {
        porcentagemAsMax = aPorcentagemAsMax;
    }
}
