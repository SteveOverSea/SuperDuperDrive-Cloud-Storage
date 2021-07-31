package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private static String firstname = "Hans";
    private static String lastname = "Friedl";
    private static String username = "HansHans75";
    private static String password = "admin1234";

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testUnauthorizedPagesAccess() {

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testUserSignUpAndLogoutAccess() {

        signupAndLogin(firstname, lastname, username, password);

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement logoutButton = driver.findElement(By.cssSelector("#logoutDiv button"));
        logoutButton.click();

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testCreateNote() throws InterruptedException {
        String noteTitle = "New Note";
        String noteDescription = "Go buy milk!";
        createNote(noteTitle, noteDescription);

        Thread.sleep(1000);

        WebElement newNoteTitle = driver.findElement(By.cssSelector("#nav-notes tbody th"));
        WebElement newNoteDescription = driver.findElement(By.cssSelector("#nav-notes tbody th + td"));

        Assertions.assertEquals(noteTitle, newNoteTitle.getText());
        Assertions.assertEquals(noteDescription, newNoteDescription.getText());
    }

    @Test
    public void testEditNote() throws InterruptedException {
        createNote("Notiz an mich", "Alles fein hier.");

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tbody button.btn-success"))).click();

        String newTitle = "Bitte nicht.";
        String newDescription = "Aufhören";

        Thread.sleep(500);

        WebElement inputTitle = driver.findElement(By.id("note-title"));
        WebElement inputDescription = driver.findElement(By.id("note-description"));

        inputTitle.clear();
        inputDescription.clear();

        inputTitle.sendKeys(newTitle);
        inputDescription.sendKeys(newDescription);
        inputDescription.submit();

        Thread.sleep(500);
        driver.get("http://localhost:" + this.port + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();

        Thread.sleep(1000);

        WebElement newNoteTitle = driver.findElement(By.cssSelector("#nav-notes tbody th"));
        WebElement newNoteDescription = driver.findElement(By.cssSelector("#nav-notes tbody th + td"));

        Assertions.assertEquals(newTitle, newNoteTitle.getText());
        Assertions.assertEquals(newDescription, newNoteDescription.getText());
    }

    @Test
    public void testDeleteNote() throws InterruptedException {
        createNote("Notiz an mich", "Alles fein hier.");

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tbody a.btn-danger"))).click();

        Thread.sleep(500);
        driver.get("http://localhost:" + this.port + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();

        Thread.sleep(1000);

        List<WebElement> tableRows = driver.findElements(By.cssSelector("#nav-notes tbody tr"));
        Assertions.assertEquals(0, tableRows.size());
    }

    @Test
    public void testCreateCredential() throws InterruptedException {
        String url = "udacity.com";
        String username = "steveOC";
        String password = "123";
        createCredential(url, username, password);

        Thread.sleep(1000);

        WebElement newUrl = driver.findElement(By.cssSelector("#credentialTable tbody th"));
        WebElement newUsername = driver.findElement(By.cssSelector("#credentialTable tbody th + td"));
        WebElement newPassword = driver.findElement(By.cssSelector("#credentialTable tbody td + td"));

        Assertions.assertEquals(url, newUrl.getText());
        Assertions.assertEquals(username, newUsername.getText());
        Assertions.assertNotEquals(password, newPassword.getText());
    }

    @Test
    public void testEditCredentials() throws InterruptedException {
        String password = "password";
        createCredential("google.com", "karli", password);

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tbody button.btn-success"))).click();

        String newUrl = "duckduckgo.com";
        String newUsername = "private";
        String newPassword = "admin1234";

        Thread.sleep(1000);

        WebElement inputUrl = driver.findElement(By.id("credential-url"));
        WebElement inputUsername = driver.findElement(By.id("credential-username"));
        WebElement inputPassword = driver.findElement(By.id("credential-password"));

        Assertions.assertEquals(password, inputPassword.getAttribute("value"));

        Thread.sleep(1000);

        inputUrl.clear();
        inputUsername.clear();
        inputPassword.clear();

        inputUrl.sendKeys(newUrl);
        inputUsername.sendKeys(newUsername);
        inputPassword.sendKeys(newPassword);
        inputPassword.submit();

        Thread.sleep(500);
        driver.get("http://localhost:" + this.port + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();

        Thread.sleep(1000);

        WebElement newUrlEl = driver.findElement(By.cssSelector("#credentialTable tbody th"));
        WebElement newUsernameEl = driver.findElement(By.cssSelector("#credentialTable tbody th + td"));
        WebElement newPasswordEl = driver.findElement(By.cssSelector("#credentialTable tbody td + td"));

        Assertions.assertEquals(newUrl, newUrlEl.getText());
        Assertions.assertEquals(newUsername, newUsernameEl.getText());
        Assertions.assertNotEquals(newPassword, newPasswordEl.getText());
    }

    @Test
    public void testDeleteCredential() throws InterruptedException {
        createCredential("udemy.com", "hirnkack", "hihi");

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tbody a.btn-danger"))).click();

        Thread.sleep(500);
        driver.get("http://localhost:" + this.port + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();

        Thread.sleep(1000);

        List<WebElement> tableRows = driver.findElements(By.cssSelector("#credentialTable tbody tr"));
        Assertions.assertEquals(0, tableRows.size());
    }

    // HELPERS

    public void signupUser(String firstname, String lastname, String username, String password) {
        driver.get("http://localhost:" + this.port + "/signup");

        WebElement inputFirstname = driver.findElement(By.id("inputFirstName"));
        WebElement inputLastname = driver.findElement(By.id("inputLastName"));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));

        inputFirstname.sendKeys(firstname);
        inputLastname.sendKeys(lastname);
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        inputPassword.submit();
    }

    public void loginUser(String username, String password) {
        //driver.get("http://localhost:" + this.port + "/login");

        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));

        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        inputPassword.submit();
    }

    public void signupAndLogin(String firstname, String lastname, String username, String password) {
        signupUser(firstname, lastname, username, password);
        loginUser(username, password);
    }

    public void createNote(String noteTitle, String noteDescription) throws InterruptedException {
        signupAndLogin("Heinz", "Krieger", "Vespakarl", "martina");
        driver.get("http://localhost:" + this.port + "/home");

        WebElement navTab = driver.findElement(By.id("nav-notes-tab"));
        navTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#nav-notes > button"))).click();

        Thread.sleep(500);

        WebElement inputTitle = driver.findElement(By.id("note-title"));
        WebElement inputDescription = driver.findElement(By.id("note-description"));

        inputTitle.sendKeys(noteTitle);
        inputDescription.sendKeys(noteDescription);
        inputDescription.submit();

        Thread.sleep(500);
        driver.get("http://localhost:" + this.port + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
    }

    public void createCredential(String url, String username, String password) throws InterruptedException {
        signupAndLogin("Heinz", "Krieger", "Vespakarl", "martina");
        driver.get("http://localhost:" + this.port + "/home");

        WebElement navTab = driver.findElement(By.id("nav-credentials-tab"));
        navTab.click();

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#nav-credentials > button"))).click();

        Thread.sleep(500);

        WebElement inputUrl = driver.findElement(By.id("credential-url"));
        WebElement inputUsername = driver.findElement(By.id("credential-username"));
        WebElement inputPassword = driver.findElement(By.id("credential-password"));

        inputUrl.sendKeys(url);
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        inputPassword.submit();

        Thread.sleep(500);
        driver.get("http://localhost:" + this.port + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
    }

}
