package org.javaee6.sample.ui;

import static org.junit.Assert.assertTrue;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.thoughtworks.selenium.DefaultSelenium;



@RunWith(Arquillian.class)
public class LoginScreenTest {
    private static final String WEBAPP_SRC = "src/main/webapp";
    private static final String USER = "user1";
    private static final String PASSWORD = "demo";
    
    @ArquillianResource
    URL deploymentUrl;
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "login.war")
            .addClasses(LoginController.class, User.class, Credentials.class)
            // .addAsWebResource(new File(WEBAPP_SRC), "login.xhtml")
            // .addAsWebResource(new File(WEBAPP_SRC), "home.xhtml")
            .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                "/", Filters.include(".*\\.xhtml$"))
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource(
                new StringAsset("<faces-config version=\"2.0\"/>"),
                "faces-config.xml");
    }
    
    /*/uncomment selenium extension in arquillian.xml
      
    @Drone
    DefaultSelenium browser;
    
    
    @Test
    public void should_login_with_valid_credentials() {
    	
    	browser.open(deploymentUrl.toString().replaceFirst("/$", "") + "/login.jsf");
        
        browser.type("id=loginForm:username", USER);
        browser.type("id=loginForm:password", PASSWORD);
        browser.click("id=loginForm:login");
        browser.waitForPageToLoad("15000");

        Assert.assertTrue("User should be logged in!",
            browser.isElementPresent("xpath=//li[contains(text(),'Welcome')]"));
    }
    
    */
    
    @Drone
    WebDriver webDriver;
    
    @FindBy(id="loginForm:username")
    private WebElement usernameInput;
    @FindBy(id="loginForm:password")
    private WebElement passwordInput;
    @FindBy(id="loginForm:login")
    private WebElement loginButton;
    
    @Test
    public void with_web_driver() {
    	webDriver.get(deploymentUrl.toString().replaceFirst("/$", "") + "/login.jsf");
    	usernameInput.sendKeys(USER);
    	passwordInput.sendKeys(PASSWORD);
    	loginButton.click();
    	Assert.assertTrue("User is logged in.", webDriver.findElement(By.xpath("//li[contains(text(),'Welcome!')]")).isDisplayed());
    	String expression = "//p[contains(text(),'You are signed in as " + USER + ".')]";
    	Assert.assertTrue("User is logged in.", webDriver.findElement(By.xpath(expression)).isDisplayed());
    }
    
    @FindBy(tagName = "li")
    private WebElement facesMessage;
    
    @FindByJQuery("p:visible")                 
    private WebElement signedAs;
    
    @Test
    public void with_graphene() {
    	webDriver.get(deploymentUrl.toString().replaceFirst("/$", "") + "/login.jsf");
    	usernameInput.sendKeys(USER);
    	passwordInput.sendKeys(PASSWORD);
    	guardHttp(loginButton).click();
    	
    	waitModel().until().element(facesMessage).is().present();    
    	Assert.assertEquals("Welcome!", facesMessage.getText().trim());
    	assertTrue(signedAs.getText().contains(USER));
    }
}
