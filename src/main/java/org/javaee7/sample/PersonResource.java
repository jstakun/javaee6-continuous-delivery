package org.javaee7.sample;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@RequestScoped
@Path("persons")
public class PersonResource {

    PersonDatabase database;

    public PersonResource() {
    	database = new PersonDatabase();
    	database.init();
    }
    
    @GET
    @Produces("application/json")
    public Person[] get() {
        return database.currentList();
    }

    @GET
    @Path("{id}")  
    @Produces("application/json")
    public Person get(@PathParam("id") int id) throws Exception {
        return database.getPerson(id);
    }
}
