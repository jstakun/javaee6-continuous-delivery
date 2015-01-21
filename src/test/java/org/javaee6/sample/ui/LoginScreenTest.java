package org.javaee6.sample.ui;

import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
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

/**
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 * @author <a href="http://community.jboss.org/people/kpiwko">Karel Piwko</a>
 */
@RunWith(Arquillian.class)
public class LoginScreenTest {
    private static final String WEBAPP_SRC = "src/main/webapp";
    
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
        
        browser.type("id=loginForm:username", "user1");
        browser.type("id=loginForm:password", "demo");
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
    	usernameInput.sendKeys("user1");
    	passwordInput.sendKeys("demo");
    	loginButton.click();
    	Assert.assertTrue("User is logged in.", webDriver.findElement(By.xpath("//li[contains(text(),'Welcome!')]")).isDisplayed());
    }
}
