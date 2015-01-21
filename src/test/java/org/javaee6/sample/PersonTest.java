package org.javaee6.sample;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.javaee6.sample.Person;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PersonTest {

    //private WebTarget target;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage("org.javaee6.sample");
    }

    @ArquillianResource
    private URL base;

    @Before
    public void setUp() throws MalformedURLException {
        //Client client = ClientBuilder.newClient();
        final URI uri = URI.create(new URL(base, "resources/persons").toExternalForm());
        System.out.println("Endpoint address: " + uri);
        //target = client.target(uri);
        //target.register(Person.class);
    }

    /**
     * Test of get method, of class Person.
     * @throws Exception 
     */
    @Test
    public void testGetAll() throws Exception {
        ClientRequest personsRequest = new ClientRequest(new URL(base, "resources/persons").toExternalForm());
    	
    	ClientResponse<List<Person>> response = personsRequest.get(new GenericType<List<Person>>(){});
    	List<Person> persons = response.getEntity();
        
    	assertEquals(8, persons.size());

        assertEquals("Penny", persons.get(0).getName());
        assertEquals("Leonard", persons.get(1).getName());
        assertEquals("Sheldon", persons.get(2).getName());
        assertEquals("Amy", persons.get(3).getName());
        assertEquals("Howard", persons.get(4).getName());
        assertEquals("Bernadette", persons.get(5).getName());
        assertEquals("Raj", persons.get(6).getName());
        assertEquals("Priya", persons.get(7).getName());
    }

    /**
     * Test of get method, of class Person.
     * @throws Exception 
     */
    @Test
    public void testGetOne() throws Exception {
        ClientRequest personRequest = new ClientRequest(new URL(base, "resources/persons/0").toExternalForm());
        
        Person response = personRequest.get(Person.class).getEntity();
        
        assertEquals("Penny", response.getName());
        
        personRequest = new ClientRequest(new URL(base, "resources/persons/1").toExternalForm());
        
        response = personRequest.get(Person.class).getEntity();
        
        assertEquals("Leonard", response.getName());
    }
}
