package demo.restfulapi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;
    List<String> result = new ArrayList<>();

    @BeforeMethod
    public void setUp() {
        System.out.println(">> Opening method!!!");
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173/login");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(groups = "testLogin", description = "Test Case login thành công")
    public void testLoginSuccess() {
        try {
            WebElement usernameField = driver.findElement(By.cssSelector("#username"));
            WebElement passwordField = driver.findElement(By.cssSelector("#password"));
            WebElement logBtn = driver.findElement(By.id("login-btn"));

            usernameField.sendKeys("hung");
            passwordField.sendKeys("123");
            logBtn.click();

            boolean loginFailed = driver.findElements(By.id("username")).isEmpty();
            Assert.assertFalse(loginFailed, ">> Người dùng đăng nhập thành công.");

            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".notyf__message")));

            System.out.println("Notyf message: " + successMessage.getText());

            Assert.assertTrue(successMessage.getText().contains("Đăng nhập thành công."));
            result.add("Test login success!");
        } catch (Exception e) {
            result.add("Test login failed: " + e.getMessage());
        }
    }

    @Test(groups = "testLogin", description = "Test Case login không hợp lệ")
    public void testLoginFailure() {
        try {
            WebElement usernameField = driver.findElement(By.cssSelector("#username"));
            WebElement passwordField = driver.findElement(By.cssSelector("#password"));
            WebElement logBtn = driver.findElement(By.id("login-btn"));

            usernameField.sendKeys("4444444");
            passwordField.sendKeys("123123123123");
            logBtn.click();

            boolean loginFailed = driver.findElements(By.id("logout")).isEmpty();
            Assert.assertTrue(loginFailed, ">> Người dùng đăng nhập thất bại.");

            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".notyf__message")));

            System.out.println("Notyf message: " + successMessage.getText());

            Assert.assertTrue(successMessage.getText().contains("Đăng nhập thất bại."));
            result.add("Test login failed as expected!");
        } catch (Exception e) {
            result.add("Test case failed due to error: " + e.getMessage());
        }
    }

    @Test(groups = "testLogin", description = "Test Case login tài khoản không tồn tại")
    public void testLoginNoAccount() {
        try {
            WebElement usernameField = driver.findElement(By.cssSelector("#username"));
            WebElement passwordField = driver.findElement(By.cssSelector("#password"));
            WebElement logBtn = driver.findElement(By.id("login-btn"));

            usernameField.sendKeys("hungquocdanawd");
            passwordField.sendKeys("123");
            logBtn.click();

            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".notyf__message")));

            System.out.println("Notyf message: " + successMessage.getText());

            Assert.assertTrue(successMessage.getText().contains("Đăng nhập thất bại."));

            result.add("Test login with non-existing account passed!");
        } catch (Exception e) {
            result.add("Test login with non-existing account failed: " + e.getMessage());
        }
    }

    @Test(groups = "testLogin", description = "Test Case login trống tài khoản")
    public void testLoginEmptyUsername() {
        try {
            WebElement usernameField = driver.findElement(By.cssSelector("#username"));
            WebElement passwordField = driver.findElement(By.cssSelector("#password"));
            WebElement logBtn = driver.findElement(By.id("login-btn"));

            usernameField.sendKeys("");
            passwordField.sendKeys("123123123");
            logBtn.click();

            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".notyf__message")));

            System.out.println("Notyf message: " + successMessage.getText());

            Assert.assertTrue(successMessage.getText().contains("Đăng nhập thất bại."));

            result.add("Test login with non-existing account passed!");
        } catch (Exception e) {
            result.add("Test login with non-existing account failed: " + e.getMessage());
        }
    }

    @Test(groups = "testLogin", description = "Test Case login trống mật khẩu")
    public void testLoginEmptyPassword() {
        try {
            WebElement usernameField = driver.findElement(By.cssSelector("#username"));
            WebElement passwordField = driver.findElement(By.cssSelector("#password"));
            WebElement logBtn = driver.findElement(By.id("login-btn"));

            usernameField.sendKeys("hung");
            passwordField.sendKeys("");
            logBtn.click();

            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".notyf__message")));

            System.out.println("Notyf message: " + successMessage.getText());

            Assert.assertTrue(successMessage.getText().contains("Đăng nhập thất bại."));

            result.add("Test login with non-existing account passed!");
        } catch (Exception e) {
            result.add("Test login with non-existing account failed: " + e.getMessage());
        }
    }

    @Test(groups = "testLogin", description = "Test Case login username có kí tự đặc biệt")
    public void testLoginUsernameHasSpecialChar() {
        try {
            WebElement usernameField = driver.findElement(By.cssSelector("#username"));
            WebElement passwordField = driver.findElement(By.cssSelector("#password"));
            WebElement logBtn = driver.findElement(By.id("login-btn"));

            usernameField.sendKeys("hung!@#$%^^&*");
            passwordField.sendKeys("123");
            logBtn.click();

            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".notyf__message")));

            System.out.println("Notyf message: " + successMessage.getText());

            Assert.assertTrue(successMessage.getText().contains("Đăng nhập thất bại."));

            result.add("Test login with non-existing account passed!");
        } catch (Exception e) {
            result.add("Test login with non-existing account failed: " + e.getMessage());
        }
    }

    @Test(groups = "testLogin", description = "Test Case login password có kí tự đặc biệt")
    public void testLoginPasswordHasSpecialChar() {
        try {
            WebElement usernameField = driver.findElement(By.cssSelector("#username"));
            WebElement passwordField = driver.findElement(By.cssSelector("#password"));
            WebElement logBtn = driver.findElement(By.id("login-btn"));

            usernameField.sendKeys("hung");
            passwordField.sendKeys("333@@@");
            logBtn.click();

            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".notyf__message")));

            System.out.println("Notyf message: " + successMessage.getText());

            Assert.assertTrue(successMessage.getText().contains("Đăng nhập thất bại."));

            result.add("Test login with non-existing account passed!");
        } catch (Exception e) {
            result.add("Test login with non-existing account failed: " + e.getMessage());
        }
    }

    @AfterSuite
    public void tearDown() throws IOException {
        System.out.println("Closing browser!!!");
        System.out.println("Result: " + result);

        if (driver != null) {
            driver.quit();
        }
    }
}
