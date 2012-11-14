/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;
import com.sun.media.sound.AuFileReader;
import org.apache.http.auth.AuthScope;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 *
 * @author david
 */
public class ActiveComponentTest {


    public static final String PAGE_PROPERTIES_COUNT = "6";
    public static final String DESIGN_PROPERTIES_COUNT = "7";
    public static final String RESOURCE_PROPERTIES_COUNT = "8";
    public static final String EXTRA_RESOURCE_PROPERTIES_COUNT = "6";

    public static final String DESIGN_PATH = "/etc/designs/testing-activecq-api";
    public static final String PAGE_PATH = "/content/testing-activecq-api/activecomponent";
    public static final String RESOURCE_PATH = PAGE_PATH + "/jcr:content/par/active-component";
    public static final String EXTRA_RESOURCE_PATH = PAGE_PATH + "/jcr:content/par/extra";

    private HtmlUnitDriver driver;

    public ActiveComponentTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private String makeURL(String selector) {
        final String url = "http://localhost:5502" + RESOURCE_PATH + "." + selector + ".html?wcmmode=disabled";
        System.out.println(url);
        return url;
    }

    @Before
    public void setUp() {
        driver = new HtmlUnitDriver() {
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                client.addRequestHeader("Authorization", "Basic YWRtaW46YWRtaW4=");
                return client;
            }
        };
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void getComponent() throws Exception {
        final String expResult = "active-component";

        driver.get(makeURL("getComponent"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getComponentContext() throws Exception {
        final String expResult = "active-component";

        driver.get(makeURL("getComponentContext"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getDesign() throws Exception {
        final String expResult = DESIGN_PATH;

        driver.get(makeURL("getDesign"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getDesigner() throws Exception {
        final String expResult = "true";

        driver.get(makeURL("getDesigner"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getDesignProperties() throws Exception {
        final String expResult = DESIGN_PROPERTIES_COUNT;

        driver.get(makeURL("getDesignProperties"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getDesignProperty() throws Exception {
        driver.get(makeURL("getDesignProperty"));

        String text = driver.findElement(By.className("plain-text")).getText();
        Assert.assertEquals("Design: Plain text", text);

        text = driver.findElement(By.className("rich-text")).getText();
        Assert.assertEquals("Design Resource: Rich text", text);

        text = driver.findElement(By.className("rich-text")).findElement(By.tagName("strong")).getText();
        Assert.assertEquals("Rich text", text);

        text = driver.findElement(By.className("double")).getText();
        Assert.assertEquals("4.4444", text);

        text = driver.findElement(By.className("long")).getText();
        Assert.assertEquals("4444444444", text);

        text = driver.findElement(By.className("boolean")).getText();
        Assert.assertEquals("true", text);

        text = driver.findElement(By.className("default-value")).getText();
        Assert.assertEquals("This is the default design value", text);

        text = driver.findElement(By.className("str-array")).getText();
        Assert.assertEquals("{design-val-1, design-val-2}", text);
    }

    @Test
    public void getPropertyExtraResource() throws Exception {
        driver.get(makeURL("getPropertyResource"));

        String text = driver.findElement(By.className("plain-text")).getText();
        Assert.assertEquals("Extra Resource: Plain text", text);

        text = driver.findElement(By.className("rich-text")).getText();
        Assert.assertEquals("Extra Resource: Rich text", text);

        text = driver.findElement(By.className("rich-text")).findElement(By.tagName("strong")).getText();
        Assert.assertEquals("Rich text", text);

        text = driver.findElement(By.className("double")).getText();
        Assert.assertEquals("9.999", text);

        text = driver.findElement(By.className("long")).getText();
        Assert.assertEquals("9999999999", text);

        text = driver.findElement(By.className("boolean")).getText();
        Assert.assertEquals("false", text);

        text = driver.findElement(By.className("default-value")).getText();
        Assert.assertEquals("This is the extra default value", text);

        text = driver.findElement(By.className("str-array")).getText();
        Assert.assertEquals("{extra-resource-val-1, extra-resource-val-2}", text);
    }

    @Test
    public void getEditContext() throws Exception {
        final String expResult = "true";

        driver.get(makeURL("getEditContext"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getNode() throws Exception {
        final String expResult = RESOURCE_PATH;

        driver.get(makeURL("getNode"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getPage() throws Exception {
        final String expResult = PAGE_PATH;

        driver.get(makeURL("getPage"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getPageManager() throws Exception {
        final String expResult = "true";

        driver.get(makeURL("getPageManager"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getPageProperties() throws Exception {
        final String expResult = PAGE_PROPERTIES_COUNT;

        driver.get(makeURL("getPageProperties"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getProperties() throws Exception {
        final String expResult = RESOURCE_PROPERTIES_COUNT;

        driver.get(makeURL("getProperties"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getProperty() throws Exception {
        driver.get(makeURL("getProperty"));

        String text = driver.findElement(By.className("plain-text")).getText();
        Assert.assertEquals("Content Resource: Plain text", text);

        text = driver.findElement(By.className("rich-text")).getText();
        Assert.assertEquals("Content Resource: Rich text", text);

        text = driver.findElement(By.className("rich-text")).findElement(By.tagName("strong")).getText();
        Assert.assertEquals("Rich text", text);

        text = driver.findElement(By.className("double")).getText();
        Assert.assertEquals("3.333", text);

        text = driver.findElement(By.className("long")).getText();
        Assert.assertEquals("3333333333", text);

        text = driver.findElement(By.className("boolean")).getText();
        Assert.assertEquals("true", text);

        text = driver.findElement(By.className("default-value")).getText();
        Assert.assertEquals("This is the content resource default value", text);

        text = driver.findElement(By.className("str-array")).getText();
        Assert.assertEquals("{content-resource-val-1, content-resource-val-2}", text);
    }

    @Test
    public void getQueryBuilder() throws Exception {
        final String expResult = "true";

        driver.get(makeURL("getQueryBuilder"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getRequest() throws Exception {
        final String expResult = RESOURCE_PATH + ".getRequest.html";

        driver.get(makeURL("getRequest"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getRequestDesign() throws Exception {
        final String expResult = DESIGN_PATH;

        driver.get(makeURL("getRequestDesign"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getRequestDesignProperties() throws Exception {
        final String expResult = DESIGN_PROPERTIES_COUNT;

        driver.get(makeURL("getRequestDesignProperties"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getRequestPage() throws Exception {
        final String expResult = PAGE_PATH;

        driver.get(makeURL("getRequestPage"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getRequestPageProperties() throws Exception {
        final String expResult = PAGE_PROPERTIES_COUNT;

        driver.get(makeURL("getRequestPageProperties"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getRequestStyle() throws Exception {
        final String expResult = DESIGN_PATH + "/jcr:content/test-page/par/active-component";

        driver.get(makeURL("getRequestStyle"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getResource() throws Exception {
        final String expResult = RESOURCE_PATH;

        driver.get(makeURL("getResource"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getResourceDesign() throws Exception {
        final String expResult = DESIGN_PATH;

        driver.get(makeURL("getResourceDesign"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getResourceDesignProperties() throws Exception {
        final String expResult = DESIGN_PROPERTIES_COUNT;

        driver.get(makeURL("getResourceDesignProperties"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getResourcePage() throws Exception {
        final String expResult = PAGE_PATH;

        driver.get(makeURL("getResourcePage"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getResourcePageProperties() throws Exception {
        final String expResult = PAGE_PROPERTIES_COUNT;

        driver.get(makeURL("getResourcePageProperties"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getResourceResolver() throws Exception {
        final String expResult = "true";

        driver.get(makeURL("getResourceResolver"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getResourceStyle() throws Exception {
        final String expResult = DESIGN_PATH + "/jcr:content/test-page/par/active-component";

        driver.get(makeURL("getResourceStyle"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getResponse() throws Exception {
        final String expResult = "true";

        driver.get(makeURL("getResponse"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getService() throws Exception {
        final String expResult = "true";

        driver.get(makeURL("getService"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void getStyle() throws Exception {
        final String expResult = DESIGN_PATH + "/jcr:content/test-page/par/active-component";

        driver.get(makeURL("getStyle"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    public void hasNode() throws Exception {
        final String expResult = "true";

        driver.get(makeURL("hasNode"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    public void jstl() throws Exception {
        final String expResult = "active-component";

        driver.get(makeURL("jstl"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    /** PLUGINS **/
    @Test
    public void xssProtect1() throws Exception {
        final String expResult = "This is safe and so is this.";

        driver.get(makeURL("plugins.xss.protect-1"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }

    @Test
    public void translateFr() throws Exception {
        final String expResult = "Annuler";

        driver.get(makeURL("plugins.i18n.translate"));
        final String text = driver.findElement(By.tagName("body")).getText();
        Assert.assertEquals(expResult, text);
    }
}
