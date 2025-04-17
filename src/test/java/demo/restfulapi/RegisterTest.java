package demo.restfulapi;

import demo.restfulapi.repository.AccountRepository;
import demo.restfulapi.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class RegisterTest {

    @LocalServerPort
    private int port;
    WebDriver driver;
    WebDriverWait wait;
    List<String> result = new ArrayList<>();

    @BeforeMethod
    public void setUp() {
        System.out.println(">> Opening method!!!");
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173/register");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(groups = "testRegister", description = "Tạo tài khoản thành công và đăng nhập vào hệ thống")
    public void registerSuccess() {
        driver.get("http://localhost:5173/register");

        String username = "kasist" + System.currentTimeMillis();

        driver.findElement(By.id("fullname")).sendKeys("Đặng Quốc Huy");
        driver.findElement(By.id("email")).sendKeys("huydqps41316@gmail.com");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys("dr3333");
        driver.findElement(By.id("register")).click();

        // wait login
        wait.until(ExpectedConditions.urlContains("/login"));
        Assertions.assertEquals("http://localhost:5173/login", driver.getCurrentUrl());

        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys("dr3333");
        driver.findElement(By.id("login-btn")).click();

        // wait home
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/"));
        assertTrue(driver.getTitle().equalsIgnoreCase("Trang chủ"));
    }

    @Test(groups = "testRegister", description = "Tạo tài khoản đã tồn tại (username đã tồn tại trong db)")
    public void registerFailure() {
        WebElement fullnameField = driver.findElement(By.cssSelector("#fullname"));
        WebElement emailField = driver.findElement(By.cssSelector("#email"));
        WebElement usernameField = driver.findElement(By.cssSelector("#username"));
        WebElement passwordField = driver.findElement(By.cssSelector("#password"));
        WebElement registerBtn = driver.findElement(By.cssSelector("#register"));

        String fullname = "Thien Hung";
        String email = "hungfddz@gmail.com";
        String username = "hung";
        String password = "123456";

        fullnameField.sendKeys(fullname);
        emailField.sendKeys(email);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        registerBtn.click();

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".notyf__message")));

        System.out.println("Notyf message: " + successMessage.getText());

        Assert.assertTrue(successMessage.getText().contains("Tạo tài khoản thất bại!"));
    }

    @Test(groups = "testRegister", description = "Tạo tài khoản trống họ tên")
    public void emptyFullname() {
        WebElement fullnameField = driver.findElement(By.cssSelector("#fullname"));
        WebElement emailField = driver.findElement(By.cssSelector("#email"));
        WebElement usernameField = driver.findElement(By.cssSelector("#username"));
        WebElement passwordField = driver.findElement(By.cssSelector("#password"));
        WebElement registerBtn = driver.findElement(By.cssSelector("#register"));

        // Empty full name
        String fullname = "";
        String email = "hungfddz@gmail.com";
        String username = "hung";
        String password = "123456";

        fullnameField.sendKeys(fullname);
        emailField.sendKeys(email);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        registerBtn.click();

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".notyf__message")));

        System.out.println("Notyf message: " + successMessage.getText());

        Assert.assertTrue(successMessage.getText().contains("Vui lòng điền đầy đủ thông tin."));
    }

    @Test(groups = "testRegister", description = "Tạo tài khoản trống email")
    public void emptyEmail() {
        WebElement fullnameField = driver.findElement(By.cssSelector("#fullname"));
        WebElement emailField = driver.findElement(By.cssSelector("#email"));
        WebElement usernameField = driver.findElement(By.cssSelector("#username"));
        WebElement passwordField = driver.findElement(By.cssSelector("#password"));
        WebElement registerBtn = driver.findElement(By.cssSelector("#register"));

        // Empty full name
        String fullname = "Trần Thiên Hùng";
        String email = "";
        String username = "hung";
        String password = "123456";

        fullnameField.sendKeys(fullname);
        emailField.sendKeys(email);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        registerBtn.click();

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".notyf__message")));

        System.out.println("Notyf message: " + successMessage.getText());

        Assert.assertTrue(successMessage.getText().contains("Vui lòng điền đầy đủ thông tin."));
    }

    @Test(groups = "testRegister", description = "Tạo tài khoản trống tên tài khoản")
    public void emptyUsername() {
        WebElement fullnameField = driver.findElement(By.cssSelector("#fullname"));
        WebElement emailField = driver.findElement(By.cssSelector("#email"));
        WebElement usernameField = driver.findElement(By.cssSelector("#username"));
        WebElement passwordField = driver.findElement(By.cssSelector("#password"));
        WebElement registerBtn = driver.findElement(By.cssSelector("#register"));

        // Empty full name
        String fullname = "Thiên Hùng";
        String email = "hungfddz@gmail.com";
        String username = "";
        String password = "123456";

        fullnameField.sendKeys(fullname);
        emailField.sendKeys(email);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        registerBtn.click();

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".notyf__message")));

        System.out.println("Notyf message: " + successMessage.getText());

        Assert.assertTrue(successMessage.getText().contains("Vui lòng điền đầy đủ thông tin."));
    }

    @Test(groups = "testRegister", description = "Tạo tài khoản trống mật khẩu")
    public void emptyPassword() {

        WebElement fullnameField = driver.findElement(By.cssSelector("#fullname"));
        WebElement emailField = driver.findElement(By.cssSelector("#email"));
        WebElement usernameField = driver.findElement(By.cssSelector("#username"));
        WebElement passwordField = driver.findElement(By.cssSelector("#password"));
        WebElement registerBtn = driver.findElement(By.cssSelector("#register"));

        // Empty full name
        String fullname = "Trần Thiên Hùng";
        String email = "hungfddz@gmail.com";
        String username = "hung";
        String password = "";

        fullnameField.sendKeys(fullname);
        emailField.sendKeys(email);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        registerBtn.click();

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".notyf__message")));

        System.out.println("Notyf message: " + successMessage.getText());

        Assert.assertTrue(successMessage.getText().contains("Vui lòng điền đầy đủ thông tin."));
    }

    @Test(groups = "testRegister", description = "Tạo tài khoản tên tài khoản có kí tự đặc biệt")
    public void usernameHasSpecialCharacters() {
        String usernameClone = "kasist" + System.currentTimeMillis();

        WebElement fullnameField = driver.findElement(By.cssSelector("#fullname"));
        WebElement emailField = driver.findElement(By.cssSelector("#email"));
        WebElement usernameField = driver.findElement(By.cssSelector("#username"));
        WebElement passwordField = driver.findElement(By.cssSelector("#password"));
        WebElement registerBtn = driver.findElement(By.cssSelector("#register"));

        String fullname = "Đặng Quốc Huy !!!!!";
        String email = "hungfddz@gmail.com";
        String password = "123";

        fullnameField.sendKeys(fullname);
        emailField.sendKeys(email);
        usernameField.sendKeys(usernameClone);
        passwordField.sendKeys(password);

        registerBtn.click();

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".notyf__message")));

        System.out.println("Notyf message: " + successMessage.getText());

        Assert.assertTrue(successMessage.getText().contains("Tạo tài khoản thành công!"));

        wait.until(ExpectedConditions.titleIs("Đăng nhập"));

        assertEquals("Đăng nhập", driver.getTitle());
    }

    @Test(groups = "testRegister", description = "Tạo tài khoản sai cú pháp Email")
    public void wrongEmail() {
        String usernameClone = "kasist" + System.currentTimeMillis();

        WebElement fullnameField = driver.findElement(By.cssSelector("#fullname"));
        WebElement emailField = driver.findElement(By.cssSelector("#email"));
        WebElement usernameField = driver.findElement(By.cssSelector("#username"));
        WebElement passwordField = driver.findElement(By.cssSelector("#password"));
        WebElement registerBtn = driver.findElement(By.cssSelector("#register"));

        String fullname = "Đặng Quốc Huy";
        String email = "123@123.com";
        String password = "123";

        fullnameField.sendKeys(fullname);
        emailField.sendKeys(email);
        usernameField.sendKeys(usernameClone);
        passwordField.sendKeys(password);

        registerBtn.click();

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".notyf__message")));

        System.out.println("Notyf message: " + successMessage.getText());

        Assert.assertTrue(successMessage.getText().contains("Email không hợp lệ."));
    }

}