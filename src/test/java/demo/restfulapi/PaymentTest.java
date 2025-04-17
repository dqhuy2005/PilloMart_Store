package demo.restfulapi;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        System.out.println(">> Opening method!!!");
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173/login");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void paymentTest() throws InterruptedException {

        // Login
        driver.findElement(By.id("username")).sendKeys("hung");
        driver.findElement(By.id("password")).sendKeys("123");
        driver.findElement(By.id("login-btn")).click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/"));
        Thread.sleep(2000);

        // Home page
        WebElement addToCartButton = driver.findElement(By.cssSelector("#add-to-cart"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartButton);

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".notyf__message")));
        Assert.assertTrue(successMessage.getText().contains("Đã thêm sản phẩm!"));

        driver.findElement(By.id("cart")).click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/cart"));
        Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:5173/cart");

        // Payment
        driver.findElement(By.id("address")).sendKeys("Khu phố 5 Bình Chiến");
        Thread.sleep(2000);
        driver.findElement(By.id("checkout")).click();

        boolean title = wait.until(ExpectedConditions.titleIs("Hóa đơn"));
        Assert.assertTrue(title);

    }

}
