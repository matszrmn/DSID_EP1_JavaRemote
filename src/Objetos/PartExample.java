package Objetos;
import java.util.LinkedList;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import Interfaces.Part;
import Interfaces.Subcomponente;

public class PartExample extends UnicastRemoteObject implements Part {
	private static final long serialVersionUID = 1981404131057934615L;
	
	public int codigo = -1;
	public String nome = "";
	public String descricao = "";
	public LinkedList<Subcomponente> subcomponentes = new LinkedList<Subcomponente>();
	
	public int getCodigo() throws RemoteException { return this.codigo; }
	public String getNome() throws RemoteException { return this.nome; }
	public String getDescricao() throws RemoteException { return this.descricao; }
	public LinkedList<Subcomponente> getSubcomponentes() throws RemoteException { return this.subcomponentes; }
	
	public void setCodigo(int codigo) throws RemoteException { this.codigo = codigo; }
	public void setNome(String nome) throws RemoteException { this.nome = nome; }
	public void setDescricao(String descricao) throws RemoteException { this.descricao = descricao; }
	public void setSubcomponentes(LinkedList<Subcomponente> subcomponentes) throws RemoteException { this.subcomponentes = subcomponentes; }
	
	
	public PartExample(int codigo, String nome, String descricao) throws RemoteException {
		this.codigo = codigo;
		this.nome = nome;
		this.descricao = descricao;
	}
	public boolean inserirSubcomponente(Part peca, int quant) throws RemoteException {
		if(quant <= 0) return false;
		
		int codigoNovo = peca.getCodigo();
		boolean modificou = false;
		for(Subcomponente subcomponente : this.subcomponentes) {
			if(subcomponente.getPeca().getCodigo() == codigoNovo) {
				subcomponente.setQuantidade(subcomponente.getQuantidade() + quant);
				modificou = true;
			}
		}
		if(modificou == false) {
			Subcomponente novo = new Componente(peca, quant);
			this.subcomponentes.add(novo);
		}
		return true;
	}
	public Subcomponente buscarSubcomponente(int codigo) throws RemoteException {
		for(Subcomponente subcomponente : this.subcomponentes) {
			if(subcomponente.getPeca().getCodigo() == codigo) return subcomponente;
		}
		return null;
	}
	public void clearList() throws RemoteException { this.subcomponentes.clear(); }
}
