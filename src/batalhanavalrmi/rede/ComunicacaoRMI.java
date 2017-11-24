 package batalhanavalrmi.rede;

import batalhanavalrmi.util.RectangleCoordenado;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ComunicacaoRMI extends Remote{
    
    public void conectar(int nJogador) throws RemoteException;
    public void desconectar() throws RemoteException;
    public void pronto(int nJogador) throws RemoteException;
    public String jogada(String jogada) throws RemoteException;
    
    public int getEstadoJogador() throws RemoteException;
    public void setEstadoJogador(int estadoJogador) throws RemoteException;
    public RectangleCoordenado[][] getCampoJogador() throws RemoteException;
    public void setCampoJogador(RectangleCoordenado[][] campoJogador) throws RemoteException;
    
    public int getJogadoresConectados() throws RemoteException;
}
