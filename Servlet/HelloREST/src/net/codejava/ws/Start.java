package net.codejava.ws;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


//Main class, responsavel por iniciar o codigo
public class Start implements ServletContextListener {
	
	public static void main(String[] args) throws InterruptedException {    
        System.out.println("Main executed");
	}
	
	//contexto do codigo em execucao
	@Override
    public void contextInitialized(ServletContextEvent arg0) {
		try {
	        ServImpl referenciaServidor = new ServImpl();
	        Server.serv = referenciaServidor;
	        
	        /*Coloco pra rodar a thread que fica sempre verificando os pedidos de compra e venda*/
	        Sistema sis = new Sistema(Server.serv);
	        Thread t_Sis = new Thread(sis);
	        t_Sis.start();
	        //Coloco pra rodar a thread que fica sempre mudando as cotacoes
	        MudaCotacao muda = new MudaCotacao();
	        Thread t_Muda = new Thread(muda);
	        t_Muda.start();
	        
		}catch(Exception e) {System.out.println(e.getMessage());}
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }
}
