package batalhanavalrmi.rede;

import batalhanavalrmi.util.RectangleCoordenado;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.StringTokenizer;

public class Comunicacao extends UnicastRemoteObject implements ComunicacaoRMI{

    private int jogador1Estado;
    private int jogador2Estado;
    
    public static RectangleCoordenado[][] jogador1Campo;
    public static RectangleCoordenado[][] jogador2Campo;
    
    public static final int DESCONECTADO = 0;
    public static final int CONECTADO = 1;
    public static final int PRONTO = 2;
    public static final int VEZ_DO_JOGADOR = 3;
    public static final int GANHOU = 4;
    
    public Comunicacao() throws RemoteException {
        this.jogador1Estado = DESCONECTADO;
        this.jogador2Estado = DESCONECTADO;
    }
    
    @Override
    public void conectar(int nJogador) throws RemoteException {
        if (nJogador == 1) {
            jogador1Estado = CONECTADO;
            System.out.println("Jogador 1 conectado");
        } else {
            jogador2Estado = CONECTADO;
            System.out.println("Jogador 2 conectado");
        }
    }

    @Override
    public void desconectar(int nJogador) throws RemoteException {
        if (nJogador == 1) {
            jogador1Estado = DESCONECTADO;
        } else {
            jogador2Estado = DESCONECTADO;
        }
    }

    @Override
    public void pronto(int nJogador) throws RemoteException {
        if (nJogador == 1) {
            jogador1Estado = VEZ_DO_JOGADOR;
        } else {
            jogador2Estado = PRONTO;
        }
    }

    @Override
    public String jogada(int nJogador, String jogada) throws RemoteException {
        StringTokenizer st = new StringTokenizer(jogada, "&");
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        
        if (nJogador == 1) {
            jogador1Estado = PRONTO;
            jogador2Estado = VEZ_DO_JOGADOR;
            if (jogador2Campo[x][y].isOcupado()) {
                return "a";
            } else {
                return "e";
            }
        } else {
            jogador2Estado = PRONTO;
            jogador1Estado = VEZ_DO_JOGADOR;
            if (jogador1Campo[x][y].isOcupado()) {
                return "a";
            } else {
                return "e";
            }
        }
    }

    @Override
    public int getJogador1Estado() {
        return jogador1Estado;
    }

    @Override
    public int getJogador2Estado() {
        return jogador2Estado;
    }

    public void setJogador1Estado(int jogador1Estado) {
        this.jogador1Estado = jogador1Estado;
    }

    public void setJogador2Estado(int jogador2Estado) {
        this.jogador2Estado = jogador2Estado;
    }

    @Override
    public RectangleCoordenado[][] getJogador1Campo() {
        return jogador1Campo;
    }

    @Override
    public RectangleCoordenado[][] getJogador2Campo() {
        return jogador2Campo;
    }

    @Override
    public void setJogador1Campo(RectangleCoordenado[][] jogador1Campo) {
        Comunicacao.jogador1Campo = jogador1Campo;
    }

    @Override
    public void setJogador2Campo(RectangleCoordenado[][] jogador2Campo) {
        Comunicacao.jogador2Campo = jogador2Campo;
    }
}
