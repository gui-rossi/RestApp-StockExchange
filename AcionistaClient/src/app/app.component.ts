import { HttpClient } from '@angular/common/http';
import { Component, Input, SystemJsNgModuleLoader } from '@angular/core';
import * as $ from 'jquery';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
/*classe unica do projeto*/
export class AppComponent {
  public id: number;
  name_functions: boolean; 
  name;
  cotacoes: any;
  events;

  title = 'AcionistaClient';

  constructor(private http: HttpClient){

  }

  //funcao chamada ao inicializar a pagina, como se fosse um construtor, mas uma funcao
  ngOnInit(){
    //faco o id do cliente ser a porta em que ele esta usando
    this.name_functions = true;
    this.id = parseInt(window.location.port);
    console.log("id de acionista gerado: ", this.id);
  }

  //funcao nao utilizada
  async delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }
  //funcao nao utilizada
  async loop(){
    while (true){
      this.keepListening();
     await this.delay(((Math.random() * 7) + 4) * 1000);
    }
  }
  //funcao chamada ao aperta o botao de update, crio um event listener para o endpoint get do servidor, passando
  //meu id como parametro
  keepListening (){
    const eventSource = new EventSource("http://localhost:1099/HelloREST/rest/event?id=" + window.location.port);
    eventSource.addEventListener(window.location.port, function(event) {
      console.log(event['data']);
      if (event['data'] != "nada a mostrar"){    
        $("#events").empty();
        $("#events").text(event['data']);
      }
    });
  }

  //funcao chamada ao clicar no botao de adicionar acionista, pego o que foi digitado no campo e faco o http request
  //para um post enviado um json no corpo
  adicionarAcionista(){
    var nome = $("#nome").val();
    $("#nome").val('');

    this.name = nome;
    this.name_functions = false;

    this.http.post<any>("http://localhost:1099/HelloREST/rest/funcs/adicionarAcionista", { "id": this.id, "nome": nome })
    .subscribe((data) => {
      console.log("Post request sent for 'adicionar Acionista'");
    });
  }

  //funcao chamada ao clicar no botao de adicionar interesse em cotacao, pego o que foi digitado no campo e faco o http request
  //para um post enviado um json no corpo
  inserirInteresseCotacao(){
    var interesse = $("#inserirInteresse").val();
    $("#inserirInteresse").val('');

    this.http.post<any>("http://localhost:1099/HelloREST/rest/funcs/inserirInteresse", { "id": this.id, "empresa": interesse })
    .subscribe((data) => {
      console.log("Post request sent for 'inserir Interesse Cotacao'");
    })
  }

  //funcao chamada ao clicar no botao de remover interesse em cotacao, pego o que foi digitado no campo e faco o http request
  //para um post enviado um json no corpo
  removerInteresseCotacao(){
    var interesse = $("#removerInteresse").val();
    $("#removerInteresse").val('');

    this.http.post<any>("http://localhost:1099/HelloREST/rest/funcs/removerInteresse", { "id": this.id, "empresa": interesse })
    .subscribe((data) => {
      console.log("Post request sent for 'remover Interesse Cotacao'");
    })
  }

  //funcao chamada ao clicar no botao de adicionar notificacao, pego o que foi digitado nos campos e faco o http request
  //para um post enviado um json no corpo
  adicionarNotificacao(){
    var notificacaoEmpresa = $("#notificacaoEmpresa").val();
    $("#notificacaoEmpresa").val('');
    var notificacaoLimite = $("#notificacaoLimite").val();
    $("#notificacaoLimite").val('');
    var notificacaoTipo = $("#notificacaoTipo").val();
    $("#notificacaoTipo").val('');

    this.http.post<any>("http://localhost:1099/HelloREST/rest/funcs/adicionarNotificacao", { "id": this.id, "empresa": notificacaoEmpresa, "tipo": notificacaoTipo, "limite": notificacaoLimite })
    .subscribe((data) => {
      console.log("Post request sent for 'adicionar Notificacao'");
    })
  }

  //funcao chamada ao clicar no botao de pedido de compra, pego o que foi digitado nos campos e faco o http request
  //para um post enviado um json no corpo
  pedidoCompra(){
    var compraQtde = $("#compraQtde").val();
    $("#compraQtde").val('');
    var compraPrecoMax = $("#compraPrecoMax").val();
    $("#compraPrecoMax").val('');
    var compraEmpresa = $("#compraEmpresa").val();
    $("#compraEmpresa").val('');
    var compraTempo = $("#compraTempo").val();
    $("#compraTempo").val('');

    this.http.post<any>("http://localhost:1099/HelloREST/rest/funcs/pedidoCompra", { "id": this.id, "empresa": compraEmpresa, "qtde": compraQtde, "precoMax": compraPrecoMax, "tempo": compraTempo })
    .subscribe((data) => {
      console.log("Post request sent for 'pedido Compra'");
    });
  }

  //funcao chamada ao clicar no botao de pedido de venda, pego o que foi digitado nos campos e faco o http request
  //para um post enviado um json no corpo
  pedidoVenda(){
    var vendaQtde = $("#vendaQtde").val();
    $("#vendaQtde").val('');
    var vendaPrecoMin = $("#vendaPrecoMin").val();
    $("#vendaPrecoMin").val('');
    var vendaEmpresa = $("#vendaEmpresa").val();
    $("#vendaEmpresa").val('');
    var vendaTempo = $("#vendaTempo").val();
    $("#vendaTempo").val('');

    this.http.post<any>("http://localhost:1099/HelloREST/rest/funcs/pedidoVenda", { "id": this.id, "empresa": vendaEmpresa, "qtde": vendaQtde, "precoMin": vendaPrecoMin, "tempo": vendaTempo })
    .subscribe((data) => {
      console.log("Post request sent for 'pedido Venda'");
    });
  }

  //funcao chamada ao clicar no botao de obter cotacao, faco o http request
  //pego o texto retornado do servidor e coloco na tela usando jquery
  obterCotacoes(){
    this.http.get<any>("http://localhost:1099/HelloREST/rest/funcs/obterCotacao?id=" + this.id)
    .subscribe((data) => {
      $("#obterCotacoes").find('p').remove();
      this.cotacoes = data;
      for (var i = 0; i < this.cotacoes.linha.length; i++){
        $("#obterCotacoes").append("<p></p>").find('p').eq(i).text("Empresa: " + this.cotacoes.linha[i].acao + ", Preco: " + this.cotacoes.linha[i].preco + " reais");
      }
    })
  }
}
