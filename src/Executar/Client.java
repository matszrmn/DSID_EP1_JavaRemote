package Executar;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import Util.Sistema;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.Scanner;
import Interfaces.Part;
import Interfaces.PartRepository;
import Interfaces.Subcomponente;


public class Client {
    private Client() {}
    
    public static Part inserirPecaRepoAtual(Scanner sc, String[] repositorios, PartRepository currentRepo, 
    										Registry registry, int atualRepo,
    										String nomeRepoAtual, String codPecaAtual) {
    	String comando;
    	
    	while(true) {
			System.out.print(nomeRepoAtual+"\\  Peca:"+codPecaAtual+"\\  Sublista:> Digite o codigo da peca: ");
			
			try {
				comando = sc.nextLine();
				if(comando.trim().equals("cancel")) {
					System.out.println("Insercao cancelada\n");
					return null;
				}
				
				int codigo = Integer.parseInt(comando);
				int pecaExisteNoRepoAtual = Sistema.pecaExisteNoRepositorioAtual(currentRepo, codigo);
				if(pecaExisteNoRepoAtual == 1)
					System.out.println("Esta peca ja foi inserida no repositorio atual\n");
				else if(pecaExisteNoRepoAtual == -1) { //Servidor caiu
					registry.unbind(nomeRepoAtual); //Pode ser modificado pelo programador
					return null;
				}
				else {
					Part pecaResp = Sistema.buscarPecaEmOutroRepositorio(registry, repositorios, repositorios[atualRepo], codigo);
					
					if(pecaResp != null) {
						currentRepo.inserirCopiaDePeca(pecaResp);
						System.out.println("Peca inserida em outro servidor... copia concluida!\n");
						return null;
					}
					else {
						String nome;
						System.out.print(nomeRepoAtual+"\\  Peca:"+codPecaAtual+"\\  Sublista:> Digite o nome da peca: ");
						nome = sc.nextLine();
						if(nome.trim().equals("cancel")) {
							System.out.println("Insercao cancelada\n");
							return null;
						}
						
						String descricao;
						System.out.print(nomeRepoAtual+"\\  Peca:"+codPecaAtual+"\\  Sublista:> Digite a descricao da peca: ");
						descricao = sc.nextLine();
						if(descricao.trim().equals("cancel")) {
							System.out.println("Insercao cancelada\n");
							return null;
						}
						
						System.out.println("Digite 'terminate' para concluir a insercao da peca ou inclua mais subpecas\n");
						return currentRepo.criarNovaPeca(codigo, nome, descricao);
					}
				}
				return null;
			}
			catch(Exception e) {
				System.out.println("Digite apenas valores numericos para o codigo da peca ou 'cancel' para cancelar a insercao\n");
			}
		}
    }
    public static int buscarRepoPeloNome(String[] repositorios, String busca) {
    	busca = busca.toLowerCase();
    	for(int i=0; i<repositorios.length; i++) {
    		if(repositorios[i].toLowerCase().equals(busca)) return i;
    	}
    	return -1;
    }
    public static int numeroRepo(String comando, String[] repositorios) {
    	Scanner scBind = new Scanner(comando);
		int atualRepoAux = 0;
		scBind.next();
		if(scBind.hasNext()) atualRepoAux = buscarRepoPeloNome(repositorios, scBind.next());
		else atualRepoAux = -1;
		scBind.close();
		
		return atualRepoAux;
    }
    public static Part getPart(String comando, PartRepository currentRepo, Registry registry, String nomeRepoAtual) {
    	Scanner scGetp = new Scanner(comando);
		scGetp.next();
		if(!scGetp.hasNext()) System.out.println("Digite um codigo valido da peca procurada apos o comando 'getp'\n");
		else {
			try {
				int codigoGetp = Integer.parseInt(scGetp.next());
				Part pecaAtualAux = currentRepo.buscarPeca(codigoGetp);
				if(pecaAtualAux != null) {
					System.out.println("Peca recuperada!\n");
					scGetp.close();
					return pecaAtualAux;
				}
				else System.out.println("Peca inexistente no repositorio atual\n");
				scGetp.close();
				return null;
			}
			catch(NumberFormatException e) { 
				System.out.println("Digite apenas um numero inteiro apos o comando 'getp'\n");
				scGetp.close();
				return null;
			}
			catch(RemoteException e) {
				System.out.println("Referencia indisponivel\n");
				scGetp.close();
				return null;
			}
		}
		scGetp.close();
		return null;
    }
    public static Part getSubpart(String comando, Part pecaAtual) {
    	Scanner scGetsub = new Scanner(comando);
		scGetsub.next();
		if(pecaAtual == null) System.out.println("Nao existe uma peca atual\n");
		else if(!scGetsub.hasNext()) System.out.println("Digite um codigo valido da subpeca procurada apos o comando 'getsubpart'\n");
		else {
			try {
				int codigoGetsub = Integer.parseInt(scGetsub.next());
				Part pecaAtualAux = pecaAtual.buscarSubcomponente(codigoGetsub).getPeca();
				System.out.println("Peca recuperada!\n");
				scGetsub.close();
				return pecaAtualAux;
			}
			catch(NumberFormatException e) { System.out.println("Digite apenas um numero inteiro apos o comando 'getp'\n");}
			catch(NullPointerException e) { System.out.println("Subcomponente inexistente na peca atual\n"); }
			catch(RemoteException e) { System.out.println("Referencia indisponivel\n"); }
		}
		scGetsub.close();
		return null;
    }
    public static void addSubpart(String comando, Part pecaInsercaoAtual, Part pecaAtual, Registry registry) {
    	
    	Scanner scAddsubpart = new Scanner(comando);
		if(!scAddsubpart.hasNext()) System.out.println("Digite uma quantidade valida da peca apos o comando 'addsubpart'\n");
		else {
			try {
				scAddsubpart.next();
				if(!scAddsubpart.hasNext()) System.out.println("Digite uma quantidade apos o comando 'addsubpart'\n");
				else if(pecaInsercaoAtual.inserirSubcomponente(pecaAtual, Integer.parseInt(scAddsubpart.next())))
					System.out.println("Peca(s) adicionada(s)!\n");
				else System.out.println("Insira apenas valores positivos para a quantidade de subcomponentes\n");
			}
			catch(NumberFormatException e) {System.out.println("Digite apenas um numero inteiro apos o comando 'addsubpart'\n");}
			catch(RemoteException e) {System.out.println("Referencia indisponivel\n");}
		}
		scAddsubpart.close();
    }
    public static void listParts(PartRepository currentRepo, Registry registry, String nomeRepoAtual) {
    	try {
			int totalPecas = 0;
			LinkedList<Part> pecasRepoAtual = currentRepo.listaDePecas();
			for(Part peca : pecasRepoAtual) {
				try {
					System.out.println("Codigo: " + peca.getCodigo()+"  Nome: " + peca.getNome()+"  Descricao: " 
				    					+ peca.getDescricao());
					totalPecas++;
				}
				catch(RemoteException e) {continue;}
			}
			System.out.println("Total de pecas: " + totalPecas);
		}
		catch(RemoteException e) {
			System.out.println("Repositorio inativo");
			try {registry.unbind(nomeRepoAtual);} catch(Exception e2) {}
		}
		System.out.println();
    }
    public static void showPart(Part pecaAtual) {
    	if(pecaAtual == null) System.out.println("Nao existe uma peca atual\n");
		else {
			try {
				System.out.println("Codigo: "+pecaAtual.getCodigo()+"  Nome: "+pecaAtual.getNome()+"  Descricao:"+pecaAtual.getDescricao());
				if(pecaAtual.getSubcomponentes().size() == 0) System.out.println("Peca primitiva\n");
				else {
					System.out.println("Peca agregada, subcomponentes:");
					Part peca;
					for(Subcomponente comp : pecaAtual.getSubcomponentes()) {
						try {
							peca = comp.getPeca();
							System.out.println("    Codigo: "+peca.getCodigo()+"  Nome: "+peca.getNome()+"  Descricao: "+peca.getDescricao()+"  [Quantidade = "+comp.getQuantidade()+"]");
						}
						catch(RemoteException e) {continue;}
					}
					System.out.println();
				}
			}
			catch(RemoteException e) { System.out.println("Peca indisponivel\n"); }
		}
    }
    
    
    public static void main(String[] args) {
        try {
        	Scanner sc = new Scanner(System.in);
        	Registry registry = LocateRegistry.getRegistry(1099);
        	String[] repositorios = registry.list();
        	PartRepository currentRepo = (PartRepository) registry.lookup(repositorios[0]);
            
            int repoAtual = 0;
            PartRepository repoInsercao = null;
            Part pecaAtual = null;
            Part pecaInsercaoAtual = null;
            
            String nomeRepoAtual = repositorios[0];
            String codPecaAtual = "";
            String codSublistaAtual = "";
        	
        	System.out.print(nomeRepoAtual+"\\  Peca:"+codPecaAtual+"\\  Sublista:"+codSublistaAtual+"> ");
        	String comando;
        	
        	while(true) {
        		comando = sc.nextLine();
        		
        		if(comando.trim().equals("quit")) {
                	sc.close();
                	System.out.print("Bye");
                	break;
        		}
        		else if(comando.contains("bind")) {
        			repositorios = registry.list();
        			int atualRepoAux = numeroRepo(comando, repositorios);
        			if(atualRepoAux == -1) System.out.println("Repositorio inexistente\n");
        			else {
        				try {
        					currentRepo = (PartRepository) registry.lookup(repositorios[atualRepoAux]);
        					repoAtual = atualRepoAux;
        					nomeRepoAtual = repositorios[atualRepoAux];
        				}
        				catch(RemoteException e) {System.out.println("Repositorio inexistente\n");}
        			}
        		}
        		else if(comando.trim().equals("addp")) {
        			if(!codSublistaAtual.equals("")) System.out.println("Insercao da peca de codigo "+codSublistaAtual+" cancelada.\n");
        			pecaInsercaoAtual = null;
        			repoInsercao = null;
        			codSublistaAtual = "";
        			
        			pecaInsercaoAtual = inserirPecaRepoAtual(sc, repositorios, currentRepo, registry, repoAtual, 
        													 nomeRepoAtual, codPecaAtual);
        			if(pecaInsercaoAtual != null) {
        				repoInsercao = (PartRepository) registry.lookup(repositorios[repoAtual]); 
        				codSublistaAtual = String.valueOf(pecaInsercaoAtual.getCodigo());
        			}
        		}
        		else if(comando.trim().equals("terminate")) {
        			if(pecaInsercaoAtual == null) System.out.println("Nao existe uma peca para ser inserida\n");
        			else {
        				try {
        					if(repoInsercao.inserirNovaPeca(registry, pecaInsercaoAtual, repositorios)) System.out.println("Insercao concluida!\n");
        					else System.out.println("Uma peca com o mesmo codigo foi inserida antes\n");
        					pecaInsercaoAtual = null;
        					repoInsercao = null;
        					codSublistaAtual = "";
        				}
        				catch(RemoteException e) {System.out.println("Repositorio indisponivel\n");}
        			}
        		}
        		else if(comando.trim().equals("cancel")) {
        			if(codSublistaAtual.equals("")) System.out.println("Nao existem insercoes pendentes.\n");
        			else {
        				pecaInsercaoAtual = null;
            			repoInsercao = null;
            			codSublistaAtual = "";
            			System.out.println("Insercao cancelada\n");
        			}
        		}
        		else if(comando.trim().equals("clearlist")) {
        			if(pecaInsercaoAtual != null) {
        				try {
        					pecaInsercaoAtual.clearList();
        					System.out.println("Lista limpa!\n");
        				}
        				catch(RemoteException e) {System.out.println("Referencia indisponivel");}
        			}
        			else System.out.println("Nao existe uma lista atual\n");
        		}
        		else if(comando.contains("getp")) {
        			Part pecaAtualAux = getPart(comando, currentRepo, registry, nomeRepoAtual);
        			if(pecaAtualAux != null) {
        				pecaAtual = pecaAtualAux;
        				codPecaAtual = String.valueOf(pecaAtual.getCodigo());
        			}
        		}
        		else if(comando.contains("getsubpart")) {
        			Part pecaAtualAux = getSubpart(comando, pecaAtual);
        			if(pecaAtualAux != null) {
        				pecaAtual = pecaAtualAux;
        				codPecaAtual = String.valueOf(pecaAtual.getCodigo());
        			}
        		}
        		else if(comando.contains("addsubpart")) {
        			if(pecaAtual != null && pecaInsercaoAtual != null) {
        				addSubpart(comando, pecaInsercaoAtual, pecaAtual, registry);
        			}
        			else if(pecaAtual == null) System.out.println("Nao existe uma peca para inserir na lista\n");
        			else System.out.println("Nao existe uma lista atual para incluir uma peca\n");
        		}
        		else if(comando.trim().equals("listp")) {
        			listParts(currentRepo, registry, nomeRepoAtual);
        		}
        		else if(comando.trim().equals("showp")) {
        			showPart(pecaAtual);
        		}
        		else if(comando.trim().equals("listrepos")) {
        			repositorios = registry.list();
        			System.out.println();
        			for(int i=0; i<repositorios.length; i++) {
        				System.out.println(repositorios[i]);
        			}
        			System.out.println();
        		}
        		else System.out.println("O comando nao foi reconhecido\n");
        		
        		System.out.print(nomeRepoAtual+"\\  Peca:"+codPecaAtual+"\\  Sublista:"+codSublistaAtual+"> ");
        	}
        } catch (Exception e) {
        	System.out.println("Referencia(s) nao disponivel(is)\n");
        }
    }
}