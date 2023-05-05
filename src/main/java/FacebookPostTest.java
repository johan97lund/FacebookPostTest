/**
 * @author Johan Lund
 * @project FacebookTests
 * @date 2023-04-26
 */

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class FacebookPostTest {

    static Logger logger = LoggerFactory.getLogger(FacebookPostTest.class);

    public static void main(String[] args) throws Exception {
        logger.info("Logback initialized");
        logger.info("Starting the test");


        String password = null;
        String email = null;
        try {
            logger.info("Reading email and password from config.json");
            // Read the email and password from config.json
            Gson gson = new Gson();
            JsonElement config = gson.fromJson(new FileReader("config.json"), JsonElement.class);
            email = config.getAsJsonObject().get("email").getAsString();
            password = config.getAsJsonObject().get("password").getAsString();
            logger.info("Read successfully!");
        } catch (IOException e) {
            // Handle any exceptions that might occur while reading the file
            logger.error("Could not read config.json", e);
            System.exit(1);
        }


        WebDriver driver = null;
        try {
            logger.info("Instantiating and launching ChromeDriver from my local machine.");
            // Set the path to the ChromeDriver executable
            System.setProperty("/users/johanlund/Downloads/Chromedriver_mac64\\chromedriver", "path/to/chromedriver");

            // Creates an instance of ChromeOptions and add the desired option
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            options.addArguments("--remote-debugging-port=9222");

            // Launch ChromeDriver
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
            logger.info("Launch successful!");
        } catch (WebDriverException e) {
            // Handle any exceptions that might occur while launching the ChromeDriver
            logger.error("Could not launch ChromeDriver", e);
            System.exit(1);
        }


        // Go to the Facebook login page
        logger.info("Attempting to log in.");
        driver.get("https://www.facebook.com/login.php");


        WebElement button = driver.findElement(By.xpath("//button[@data-testid='cookie-policy-manage-dialog-accept-button']"));
        button.click();

        // Enter email address
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(email);

        // Enter password
        WebElement passwordField = driver.findElement(By.id("pass"));
        passwordField.sendKeys(password);

        // Click the "Log In" button
        WebElement loginButton = driver.findElement(By.name("login"));
        loginButton.click();

        // Wait for the log in process to finish.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("Thread was interrupted", e);
            System.exit(1);
        }

        //Create post test

        // Click on the "Most Recent" menu
        WebElement mostRecentClick = driver.findElement(new By.ByXPath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div/div[1]/div[1]/div/div[1]/div/div/div[1]/div/div/div[1]/div[1]/ul/li[2]/div/a/div[1]/div[2]/div/div/div/div/span/span"));
        mostRecentClick.click();
        Thread.sleep(2000);
        // Click on the "What's on your mind?" post box.
        WebElement createPostBox = driver.findElement(new By.ByXPath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div/div[1]/div[1]/div/div[2]/div/div/div/div[4]/div/div[2]/div/div/div/div[1]/div"));
        createPostBox.click();
        Thread.sleep(2000);
        // Writes the string "Hello World!".
        WebElement writePost = driver.findElement(new By.ByXPath("/html/body/div[1]/div/div[1]/div/div[4]/div/div/div[1]/div/div[2]/div/div/div/form/div/div[1]/div/div/div/div[2]/div[1]/div[1]/div[1]/div/div/div[1]/p"));
        writePost.sendKeys("Hello World!");
        Thread.sleep(2000);
        // Click the button "Post".
        WebElement clickPost = driver.findElement(new By.ByXPath("/html/body/div[1]/div/div[1]/div/div[4]/div/div/div[1]/div/div[2]/div/div/div/form/div/div[1]/div/div/div/div[3]/div[2]/div"));
        clickPost.click();
        Thread.sleep(3000);
        //Update the site to check for "Hello World
        try {
            int i = 0;
            boolean foundPost = false;
            while (i < 4 && !foundPost) {
                driver.navigate().refresh();
                Thread.sleep(1000);
                if (driver.getPageSource().contains("Hello World")) {
                    foundPost = true;
                    logger.info("The post was found!");
                }
                i++;

            }
            if (!foundPost) {
                logger.info("The post was not found!");
            }
        } catch (Exception e) {
            logger.error("The post was not found", e);
            System.exit(1);
        }
        Thread.sleep(4000);


        // Close the browser
        logger.info("Test successful!");
        driver.quit();
    }
}
