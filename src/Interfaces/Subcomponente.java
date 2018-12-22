package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Subcomponente extends Remote {
	public Part getPeca() throws RemoteException;
	public int getQuantidade() throws RemoteException;
	public void setPeca(Part peca) throws RemoteException;
	public void setQuantidade(int quantidade) throws RemoteException;
}
