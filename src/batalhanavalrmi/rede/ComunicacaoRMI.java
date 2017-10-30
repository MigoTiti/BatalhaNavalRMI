 package batalhanavalrmi.rede;

import batalhanavalrmi.util.RectangleCoordenado;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ComunicacaoRMI extends Remote{
    
    public void conectar(int nJogador) throws RemoteException;
    public void desconectar(int nJogador) throws RemoteException;
    public void pronto(int nJogador) throws RemoteException;
    public String jogada(int nJogador, String jogada) throws RemoteException;
    
    public int getJogador1Estado() throws RemoteException;
    public int getJogador2Estado() throws RemoteException;
    
    public void setJogador1Campo(RectangleCoordenado[][] campo) throws RemoteException;
    public void setJogador2Campo(RectangleCoordenado[][] campo) throws RemoteException;
    public RectangleCoordenado[][] getJogador1Campo() throws RemoteException;
    public RectangleCoordenado[][] getJogador2Campo() throws RemoteException;
}
