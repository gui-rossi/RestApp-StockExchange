package net.codejava.ws;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.websocket.OnMessage;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import com.sun.research.ws.wadl.Response;

//endpoints do servidor utilizados pelo client para enviar e receber informacoes
@Path("/funcs")
public class Interface {
	Registry refServ;
	
	//endpoint para que o client coloque interesse em receber cotacoes de determinada empresa
	@Path("/inserirInteresse")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void inserirInteresseFunc(String msg) {
		//tratamento do JSON, transformacao dos atributos do JSON em variavel e chamada do metodo de insercao
		JSONParser parser = new JSONParser(msg);
		try {
			LinkedHashMap<String, Object> json = parser.object();
			int id = Integer.parseInt(json.get("id").toString());
			String empresa = json.get("empresa").toString();
			Server.serv.inserirCotacaoInteressante(empresa, id);
			
		} catch (ParseException | RemoteException e){}
	}
	
	//endpoint para que o client remova interesse em receber cotacoes de determinada empresa
	@Path("/removerInteresse")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void removerInteresseFunc(String msg) {
		//tratamento do JSON, transformacao dos atributos do JSON em variavel e chamada do metodo de remocao
		JSONParser parser = new JSONParser(msg);
		try {
			LinkedHashMap<String, Object> json = parser.object();
			int id = Integer.parseInt(json.get("id").toString());
			String empresa = json.get("empresa").toString();
			Server.serv.removerCotacaoDesinteressante(empresa, id);
			
		} catch (ParseException | RemoteException e){}
	}
	
	//endpoint para que o client adicione notificacao de ganho ou perda de determinada empresa
	@Path("/adicionarNotificacao")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void adicionarNotificacao(String msg) {
		//tratamento do JSON, transformacao dos atributos do JSON em variavel e chamada do metodo de adicionar notificacao
		JSONParser parser = new JSONParser(msg);
		try {
			LinkedHashMap<String, Object> json = parser.object();
			int id = Integer.parseInt(json.get("id").toString());
			int limite = Integer.parseInt(json.get("limite").toString());
			String tipo = json.get("tipo").toString();
			String empresa = json.get("empresa").toString();
			Server.serv.inserirInteresseNotificacao(id, empresa, limite, tipo);
			
		} catch (ParseException | RemoteException e){}
	}
	
	//endpoint para que o client adicione pedido de compra de determinada empresa
	@Path("/pedidoCompra")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void pedidoCompra(String msg) {
		//tratamento do JSON, transformacao dos atributos do JSON em variavel e chamada do metodo de pedido de compra
		JSONParser parser = new JSONParser(msg);
		try {
			LinkedHashMap<String, Object> json = parser.object();
			int id = Integer.parseInt(json.get("id").toString());
			int qtde = Integer.parseInt(json.get("qtde").toString());
			int precoMax = Integer.parseInt(json.get("precoMax").toString());
			int tempo = Integer.parseInt(json.get("tempo").toString());
			String empresa = json.get("empresa").toString();
			Server.serv.pedidoCompra(qtde, precoMax, empresa, tempo, id);
			
		} catch (ParseException | RemoteException e){}
	}
	
	//endpoint para que o client adicione pedido de venda de determinada empresa
	@Path("/pedidoVenda")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void pedidoVenda(String msg) {
		//tratamento do JSON, transformacao dos atributos do JSON em variavel e chamada do metodo de pedido de venda
		JSONParser parser = new JSONParser(msg);
		try {
			LinkedHashMap<String, Object> json = parser.object();
			int id = Integer.parseInt(json.get("id").toString());
			int qtde = Integer.parseInt(json.get("qtde").toString());
			int precoMin = Integer.parseInt(json.get("precoMin").toString());
			int tempo = Integer.parseInt(json.get("tempo").toString());
			String empresa = json.get("empresa").toString();
			Server.serv.pedidoVenda(qtde, precoMin, empresa, tempo, id);
			
		} catch (ParseException | RemoteException e){}
	}
	
	//endpoint para cadastrar o acionista no servidor
	@Path("/adicionarAcionista")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void adicionarAcionista(String msg) {
		//tratamento do JSON, transformacao dos atributos do JSON em variavel e chamada do metodo de cadastro do acionista
		JSONParser parser = new JSONParser(msg);
		try {
			LinkedHashMap<String, Object> json = parser.object();
			int id = Integer.parseInt(json.get("id").toString());
			String nome = json.get("nome").toString();
			Server.serv.registrarInteresse(nome, id);
			
		} catch (ParseException | RemoteException e){}
	}
	
	//endpoint receber cotacoes de interesse do acionista
	@Path("/obterCotacao")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String obterCotacao(@QueryParam("id") int id) {
		//como é um metodo get não ha json a ser processado, apenas chamamos a função e criamos o TXT de envio
		try {
			return Server.serv.obterCotacoes(id);
			
		} catch (RemoteException e){}
		return null;
	}
	
	public String id;
	public String message = "nada a mostrar";
	
	//endpoint receber cotacoes de interesse do acionista
	@Path("/update")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getUpdates(@QueryParam("id") int idint) {
		String id = Integer.toString(idint);
		this.message = "{\"msg\":" + "\"" + "nada a mostrar" + "\"" + "}";
		//percorro a fila de compras realizadas para ver se tem algo a ser notificado ao ID do parametro
		for (FeedHistoryCompradores feed_c : Server.feed_C){
        	//usuario requisitou ver o feed, vejo se há alguma compra dele efetuada
        	if (id.contains(feed_c.id_comprador)) {
        		this.message = "{\"msg\":" + "\"" + feed_c.message_comprador + "\"" + "}";
        		//removo o feed e saio
        		Server.feed_C.remove(feed_c);
        		break;
        	}
        }
		//percorro a fila de vendas realizadas para ver se tem algo a ser notificado ao ID do parametro
		for (FeedHistoryVendedores feed_v : Server.feed_V){
        	//usuario requisitou ver o feed, vejo se há alguma Venda dele efetuada
        	if (id.contains(feed_v.id_vendedor)) {
        		this.message = "{\"msg\":" + "\"" + feed_v.message_vendedor + "\"" + "}";
        		//removo o feed e saio
        		Server.feed_V.remove(feed_v);
        		break;
        	}
        }
		//percorro a fila de notificacoes para ver se tem algo a ser notificado ao ID do parametro
		for (FeedHistoryNotificacoes feed_n : Server.feed_N){
        	//usuario requisitou ver o feed, vejo se há alguma notificacao para ele
        	if (id.contains(feed_n.id_notificar)) {
        		this.message = "{\"msg\":" + "\"" + feed_n.message_notificar + "\"" + "}";
        		//removo o feed e saio
        		Server.feed_N.remove(feed_n);
        		break;
        	}
        }
		
		return this.message;
	}
}
