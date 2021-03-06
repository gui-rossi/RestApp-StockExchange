package net.codejava.ws;

import java.util.ArrayList;
import java.util.List;

//CLASSE SERVER responsavel por manter a referencia global do servidor
//tambem possui variaveis globais de controle do fluxo do sistema
public class Server {
	public static ServImpl serv;
	
	//variaveis de controle da informacao que sera enviada ao client
	//se aconteceu algum par venda/compra, seto a booleana hadDeal para true e adiciono uma instancia de
	//FeedHistoryVendedores e FeedHistoryCompradores na fila feed_V e feed_C, respectivamente
	//removo dessas filas assim que envio ao client
	//mesma logica para as notificacoes
	public static boolean hadDeal = false;
	public static boolean hadNot = false;
	public static List<FeedHistoryVendedores> feed_V = new ArrayList<FeedHistoryVendedores>();
	public static List<FeedHistoryCompradores> feed_C = new ArrayList<FeedHistoryCompradores>();
	public static List<FeedHistoryNotificacoes> feed_N = new ArrayList<FeedHistoryNotificacoes>();
	public static String id_vendedor;
	public static String id_comprador;
	public static String id_notificar;
	public static String message_vendedor;
	public static String message_comprador;
	public static String message_notificar;
}

//classes auxiliares responsaveis por manter a fila de informacoes de vendas, compras e notificacoes
//a serem enviadas para os clients
class FeedHistoryVendedores {
	public String id_vendedor;
	public String message_vendedor;
}

class FeedHistoryCompradores {
	public String id_comprador;
	public String message_comprador;
}

class FeedHistoryNotificacoes {
	public String id_notificar;
	public String message_notificar;
}