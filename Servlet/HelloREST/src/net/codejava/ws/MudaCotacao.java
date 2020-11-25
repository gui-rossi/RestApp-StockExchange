package net.codejava.ws;

import java.rmi.RemoteException;


public class MudaCotacao implements Runnable{
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				Server.serv.mudaCotacaoGlobal();
			} catch (InterruptedException e) {} catch (RemoteException e) {}
			
		}
	}
}
