package Interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface Part extends Remote {
	public int getCodigo() throws RemoteException;
	public String getNome() throws RemoteException;
	public String getDescricao() throws RemoteException;
	public LinkedList<Subcomponente> getSubcomponentes() throws RemoteException;
	
	public void setCodigo(int codigo) throws RemoteException;
	public void setNome(String nome) throws RemoteException;
	public void setDescricao(String descricao) throws RemoteException;
	public void setSubcomponentes(LinkedList<Subcomponente> subcomponentes) throws RemoteException;
	
	public boolean inserirSubcomponente(Part peca, int quant) throws RemoteException;
	public Subcomponente buscarSubcomponente(int codigo) throws RemoteException;
	public void clearList() throws RemoteException;
}