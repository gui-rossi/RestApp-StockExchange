package net.codejava.ws;

public class CompraVenda implements Runnable{
	private int id_usuario;
	private String empresa;
	private String tipo_negocio;
	private int tempo;
	private ServImpl refServ;
	
	CompraVenda(int id_usuario, String empresa, String tipo_negocio, int tempo, ServImpl refServ){
		this.id_usuario = id_usuario;
		this.empresa = empresa;
		this.tipo_negocio = tipo_negocio;
		this.tempo = tempo;
		this.refServ = refServ;
	}
	
	@Override
	public void run() {
		
		try {
			Thread.sleep(1000*this.tempo);
		}catch(Exception e) {}
		
		try {
			this.refServ.encerrarNegocio(this.id_usuario, this.empresa, this.tipo_negocio);
		}catch(Exception e) {}
	}
}
