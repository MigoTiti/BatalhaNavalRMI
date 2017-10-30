package batalhanavalrmi.rede;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ComunicacaoRMI extends Remote{
    
    public void conectar(int nJogador) throws RemoteException;
    public void desconectar(int nJogador) throws RemoteException;
    public void pronto(int nJogador) throws RemoteException;
    public String jogada(int nJogador, String jogada) throws RemoteException;
    public int getJogador1Estado() throws RemoteException;
    public int getJogador2Estado() throws RemoteException;
}
