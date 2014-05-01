package br.datamaio.fly.rs.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import br.datamaio.fly.web.Datastore;

@Path("register")
public class Register {

	@GET
	public void register(@QueryParam("id") final String id){
	    Datastore.register(id);	
	}

	@DELETE
	public void unregister(@QueryParam("id") final String id){
		Datastore.unregister(id);	
	}

}
