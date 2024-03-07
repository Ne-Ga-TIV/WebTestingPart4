import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.javafaker.Faker;


public class Part4Test {

    WebDriver driverForCreateUser;

    String path = "src/test/resources/";
    WebDriver driverForTest;
    Faker faker;
    WebDriverWait wait;
    String firstName, lastName, password, email;

    @BeforeClass
    public void setUp() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driverForCreateUser = new ChromeDriver();
        faker = new Faker();
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        createUser();
    }

    @Test
    public void test1() throws InterruptedException {
        driverForTest = new ChromeDriver();
        login();
        buyProducts("data1.txt");
        checkout();
        fillAddress();
        endOrder();
        checkOrder();
        driverForTest.quit();
    }

    @Test
    public void test2() throws InterruptedException {
        driverForTest = new ChromeDriver();
        login();
        buyProducts("data2.txt");
        checkout();
        endOrder();
        checkOrder();
    }
    private void createUser() throws InterruptedException {
        driverForCreateUser.get("https://demowebshop.tricentis.com/");
        driverForCreateUser.findElement(By.className("ico-login")).click();
        driverForCreateUser.findElement(By.cssSelector("input.button-1.register-button")).click();
        driverForCreateUser.findElement(By.id("gender-male")).click(); // Выбор мужского пола
        driverForCreateUser.findElement(By.id("FirstName")).sendKeys(firstName);
        driverForCreateUser.findElement(By.id("LastName")).sendKeys(lastName);
        driverForCreateUser.findElement(By.id("Email")).sendKeys(email);
        driverForCreateUser.findElement(By.id("Password")).sendKeys(password);
        driverForCreateUser.findElement(By.id("ConfirmPassword")).sendKeys(password);
        driverForCreateUser.findElement(By.id("register-button")).click();
        driverForCreateUser.findElement(By.cssSelector("input.button-1.register-continue-button")).click();
        driverForCreateUser.close();
    }
    private void login() throws InterruptedException {
        driverForTest.get("https://demowebshop.tricentis.com/");
        driverForTest.findElement(By.className("ico-login")).click();
        driverForTest.findElement(By.id("Email")).sendKeys(email);
        driverForTest.findElement(By.id("Password")).sendKeys(password);
        driverForTest.findElement(By.cssSelector("input[value='Log in']")).click();
    }

    private void buyProducts(String fileName) throws InterruptedException {
        driverForTest.findElement(By.linkText("Digital downloads")).click();
        List<WebElement> products = driverForTest.findElements(By.className("details"));
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path + fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                for (WebElement product : products) {
                    WebElement productTitle = product.findElement(By.className("product-title"));
                    if (productTitle.getText().contains(line)) {
                        product.findElement(By.className("product-box-add-to-cart-button")).click();
                        Thread.sleep(1000);
                        break;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkout() throws InterruptedException {
        driverForTest.findElement(By.linkText("Shopping cart")).click();
        driverForTest.findElement(By.id("termsofservice")).click();
        driverForTest.findElement(By.id("checkout")).click();

        Thread.sleep(4000);
    }


    private void fillAddress() throws InterruptedException {
        Select select = new Select(driverForTest.findElement(By.id("BillingNewAddress_CountryId")));
        select.selectByVisibleText("Lithuania");
        driverForTest.findElement(By.id("BillingNewAddress_City")).sendKeys("Vilnius");
        driverForTest.findElement(By.id("BillingNewAddress_Address1")).sendKeys("Didlaukio 79");
        driverForTest.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("LT-3155");
        driverForTest.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("888003535");

    }

    private void endOrder(){
        wait = new WebDriverWait(driverForTest, Duration.ofSeconds(10));
        driverForTest.findElement(By.cssSelector("input[value='Continue']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".tab-section.allow.active input[value='Continue']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".tab-section.allow.active input[value='Continue']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".tab-section.allow.active input[value='Confirm']"))).click();

    }

    private void checkOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".section.order-completed")));
        Assert.assertEquals("Your order has been successfully processed!",driverForTest.findElement(By.className("title")).getText());
    }

    @AfterClass
    public void tearDown() {
        driverForTest.quit();
        driverForCreateUser.quit();
    }
}
