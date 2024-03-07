import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.github.javafaker.Faker;


public class Part4Test {

    WebDriver driverForCreateUser;
    WebDriver driverForTest;
    Faker faker;

    String firstName, lastName, password, email;

    @BeforeClass
    public void setUp() throws InterruptedException {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=selenium");
        driverForTest = new ChromeDriver(options);
        driverForCreateUser = new ChromeDriver(options);
        faker = new Faker();
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        createUser();
    }



    @AfterClass
    public void tearDown() {
        driverForTest.quit();
        driverForCreateUser.quit();
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
        Thread.sleep(3000);
        driverForCreateUser.findElement(By.id("register-button")).click();
        driverForCreateUser.findElement(By.cssSelector("input.button-1.register-continue-button")).click();
        driverForCreateUser.close();
    }
    @Test
    private void login() throws InterruptedException {
        driverForTest.get("https://demowebshop.tricentis.com/");
        driverForTest.findElement(By.className("ico-login")).click();
        driverForTest.findElement(By.id("Email")).sendKeys(email);
        driverForTest.findElement(By.id("Password")).sendKeys(password);
        Thread.sleep(3000);
        driverForTest.findElement(By.cssSelector("input[value='Log in']")).click();
    }

    private void buyProducts(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                // Прочитать товары из файла и добавить их в корзину
                // Например: driver.findElement(By.linkText(line)).click();
                // И так далее
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkout() {
        driverForTest.findElement(By.linkText("Shopping cart")).click();
        driverForTest.findElement(By.id("termsofservice")).click();
        driverForTest.findElement(By.id("checkout")).click();
        // Дополнительные шаги для оформления заказа
    }
}
