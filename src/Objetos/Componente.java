package Objetos;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import Interfaces.Part;
import Interfaces.Subcomponente;


public class Componente extends UnicastRemoteObject implements Subcomponente {
	private static final long serialVersionUID = -3211398903677653776L;
	
	public Part peca;
	public int quantidade;
	
	public Part getPeca() throws RemoteException { return this.peca; }
	public int getQuantidade() throws RemoteException { return this.quantidade; }
	public void setPeca(Part peca) throws RemoteException { this.peca = peca; }
	public void setQuantidade(int quantidade) throws RemoteException { this.quantidade = quantidade; }
	
	public Componente(Part peca, int quantidade) throws RemoteException {
		this.peca = peca;
		this.quantidade = quantidade;
	}
}
