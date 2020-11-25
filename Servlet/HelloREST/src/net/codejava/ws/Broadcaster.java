package net.codejava.ws;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;


@Singleton
@Path("/events")
public class Broadcaster{
	public static SseBroadcaster broadcaster;
	public static Sse sse;
	
	@GET
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void register(@Context Sse sse, @Context SseEventSink eventSink) {
		Broadcaster.sse = sse;
		if (broadcaster == null) {
			Broadcaster.broadcaster = sse.newBroadcaster();
		}
		Broadcaster.broadcaster.register(eventSink);
		
		Broadcaster.broadcaster.broadcast(Broadcaster.sse.newEvent("MANDEI no register"));
	}
	
	
	public void send (){
		System.out.println("...");
		this.broadcaster.broadcast(this.sse.newEvent("MANDEI " + LocalTime.now().toString()));
	}
}
