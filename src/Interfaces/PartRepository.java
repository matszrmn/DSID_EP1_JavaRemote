package Interfaces;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.rmi.RemoteException;


public interface PartRepository extends Remote {
	public boolean inserirNovaPeca(Registry registry, Part peca, String[] repositorios) throws RemoteException;
	public boolean inserirCopiaDePeca(Part peca) throws RemoteException;
	
	public Part criarNovaPeca(int codigo, String nome, String descricao) throws RemoteException;
	public Part buscarPeca(int codigo) throws RemoteException;
	public LinkedList<Part> listaDePecas() throws RemoteException;
}