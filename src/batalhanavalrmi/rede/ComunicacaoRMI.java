 package batalhanavalrmi.rede;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ComunicacaoRMI extends Remote{
    
    public void conectar() throws RemoteException;
    public void desconectar() throws RemoteException;
    public boolean jogada(int x, int y) throws RemoteException;
    
    public int getEstadoJogador() throws RemoteException;
    public void setEstadoJogador(int estadoJogador) throws RemoteException;
    public void setIpOponente(String ip) throws RemoteException;
    public String getNickName() throws RemoteException;
    public void setNickNameAdversario(String nickNameAdversario) throws RemoteException;
}
