package net.codejava.ws;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Sistema implements Runnable {
	ServImpl servRef;
	
	Sistema(ServImpl referenciaServidor) throws RemoteException{
	        this.servRef = referenciaServidor;
	}
	
	/*Thread que fica sempre rodando checando se rolou venda e compra*/
	@Override
	public void run() {
		while (true) {
			try {
				/*checo se tem alguem comprando ou vendendo acoes e realizo o negocio*/
				Server.serv.checaNegocios();
				/*checo se preciso notificar alguem sobre ganho ou perda*/
				Server.serv.checaNotificacoes();
				//adiciono na fila de envios para vendedor e comprador
				if (Server.hadDeal) {
					FeedHistoryVendedores feed_v = new FeedHistoryVendedores();
					feed_v.id_vendedor = Server.id_vendedor;
					feed_v.message_vendedor = Server.message_vendedor;
					//coloco na fila de vendas a venda que aconteceu agora
					Server.feed_V.add(feed_v);
					Server.id_vendedor = "";
					Server.message_vendedor = "";
					
					FeedHistoryCompradores feed_c = new FeedHistoryCompradores();
					feed_c.id_comprador = Server.id_comprador;
					feed_c.message_comprador = Server.message_comprador;
					//coloco na fila de compras a compra que aconteceu agora
					Server.feed_C.add(feed_c);
					Server.id_comprador = "";
					Server.message_comprador = "";
					
					Server.hadDeal = false;
				}
				//se tenho alguma notificacao pra acontecer
				if (Server.hadNot) {
					FeedHistoryNotificacoes feed_n = new FeedHistoryNotificacoes();
					feed_n.id_notificar = Server.id_notificar;
					feed_n.message_notificar = Server.message_notificar;
					//coloco na fila de notificacoes a notificacao
					Server.feed_N.add(feed_n);
					Server.id_notificar = "";
					Server.message_notificar = "";
					
					Server.hadNot = false;
				}
				
			}catch(Exception e) {}
		}
	}
}
