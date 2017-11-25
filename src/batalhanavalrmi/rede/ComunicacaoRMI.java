 package batalhanavalrmi.rede;

import batalhanavalrmi.util.RectangleCoordenado;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ComunicacaoRMI extends Remote{
    
    public void conectar() throws RemoteException;
    public void desconectar() throws RemoteException;
    public void pronto(int nJogador) throws RemoteException;
    public String jogada(String jogada) throws RemoteException;
    public String getIP() throws RemoteException;
    
    public int getEstadoJogador() throws RemoteException;
    public void setEstadoJogador(int estadoJogador) throws RemoteException;
    public RectangleCoordenado[][] getCampoJogador() throws RemoteException;
    public void setCampoJogador(RectangleCoordenado[][] campoJogador) throws RemoteException;
    public void setIpOponente(String ip) throws RemoteException;
    public String getIpOponente() throws RemoteException;
    public boolean isOponenteConectado() throws RemoteException;
}
