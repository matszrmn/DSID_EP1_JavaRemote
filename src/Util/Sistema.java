package Util;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import Interfaces.Part;
import Interfaces.PartRepository;

public class Sistema {
	
    public static int pecaExisteNoRepositorioAtual(PartRepository currentRepo, int codigo) {
    	try {
    		if(currentRepo.buscarPeca(codigo) != null) return 1;
    		return 0;
    	}
    	catch(RemoteException e) {
    		System.out.println("Repositorio inativo\n");
    		return -1;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return -1;
    	}
    }
    public static boolean pecaExisteNoSistema(Registry registry, String[] repositorios, int codigo) {
    	try {
    		PartRepository repositorioAtualLista;
    		Part busca = null;
    		
    		for(int i=0; i<repositorios.length; i++) {
    			try {
    				repositorioAtualLista = (PartRepository) registry.lookup(repositorios[i]);
    				busca = repositorioAtualLista.buscarPeca(codigo);
    				if(busca != null) return true;
    			}
    			catch(RemoteException e) {continue;}
    		}
    		return false;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
	}
	public static Part buscarPecaEmOutroRepositorio(Registry registry, String[] repositorios, String repositorioAtual, int codigo) {
    	try {
    		PartRepository repositorioAtualLista;
    		Part resposta = null;
    		
    		for(int i=0; i<repositorios.length; i++) {
    			if(!repositorios[i].equals(repositorioAtual)) { //Repositorio diferente do utilizado atualmente pelo cliente
    				try {
    					repositorioAtualLista = (PartRepository) registry.lookup(repositorios[i]);
    					resposta = repositorioAtualLista.buscarPeca(codigo);
    					if(resposta != null) return resposta;
    				}
    				catch(RemoteException e) {continue;}
    			}
    		}
    		return null;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
}
