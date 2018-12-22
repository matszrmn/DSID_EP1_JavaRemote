package Executar;
import java.util.LinkedList;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import Interfaces.Part;
import Interfaces.PartRepository;
import Objetos.PartExample;
import Util.Sistema;


public class Server implements PartRepository {
	public LinkedList<Part> pecas = new LinkedList<Part>();
	
	public boolean inserirNovaPeca(Registry registry, Part peca, String[] repositorios) throws RemoteException { //Verifica em todo o sistema
    	int codigoNovo = peca.getCodigo();
    	try {
    		if(Sistema.pecaExisteNoSistema(registry, repositorios, codigoNovo)) return false;
    		pecas.add(peca);
    		return true;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    public boolean inserirCopiaDePeca(Part peca) throws RemoteException { //Verifica apenas no servidor atual
    	int codigoNovo = peca.getCodigo();
    	try {
    		if(buscarPeca(codigoNovo) != null) return false;
    		pecas.add(peca);
    		return true;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    public Part criarNovaPeca(int codigo, String nome, String descricao) throws RemoteException {
    	Part nova = new PartExample(codigo, nome, descricao);
    	return nova;
    }
	public Part buscarPeca(int codigo) throws RemoteException {
		for(Part peca : pecas) {
			if(peca.getCodigo() == codigo) return peca;
		}
		return null;
	}
	public LinkedList<Part> listaDePecas() throws RemoteException {
		return this.pecas;
	}
	
    public static void main(String args[]) {
        try {
        	Server serv1 = new Server();
        	Server serv2 = new Server();
        	Server serv3 = new Server();
        	
        	PartRepository stub1 = (PartRepository) UnicastRemoteObject.exportObject(serv1, 0);
        	PartRepository stub2 = (PartRepository) UnicastRemoteObject.exportObject(serv2, 1);
        	PartRepository stub3 = (PartRepository) UnicastRemoteObject.exportObject(serv3, 2);
        	
        	Registry registry = LocateRegistry.getRegistry(1099);
        	registry.bind("Repo1", stub1);
        	registry.bind("Repo2", stub2);
        	registry.bind("Repo3", stub3);
        	
        	System.out.println("Servers ready");
        } catch (Exception e) {	System.out.println("O registro RMI nao foi encontrado ou os repositorios ja foram registrados"); }
    }
}