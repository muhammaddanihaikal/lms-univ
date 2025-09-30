import config.env_target;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdmissionTest extends env_target {
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        // setup driver chrome
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\drivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito", "--disable-save-password-bubble");
        driver = new ChromeDriver(options);

        // buka browser
        driver.manage().window().maximize();
        driver.get(baseUrl);

        // inisialisasi explicit wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // set zoom ke 75%
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='80%'");
    }

    @AfterEach
    void tearDown() {
        // tutup browser
        // if (driver != null) driver.quit();
    }

    @Test
    void admission(){
        // klik menu admission
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Admission"))).click();

        // pastikan halaman admission siap
        wait.until(ExpectedConditions.urlToBe(baseUrl+"admission"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));

        // --- personal information ---
        // full name
        driver.findElement(By.id("field-:r5:")).sendKeys("Ahmad Sahroni"+ Utils.randomNumber(2));
        // nisn
        driver.findElement(By.id("field-:r7:")).sendKeys( Utils.randomNumber(8));
        // gender
        new Select(driver.findElement(By.id("field-:r9:"))).selectByVisibleText("Male");
        // religion
        new Select(driver.findElement(By.id("field-:rb:"))).selectByVisibleText("Islam");
        // place of birth
        driver.findElement(By.id("field-:rd:")).sendKeys("DKI Jakarta");
        // date of birth
        driver.findElement(By.id("field-:rf:")).sendKeys("01-01-2000");
        // complete address
        driver.findElement(By.id("field-:rh:")).sendKeys("Jalan Pesanggrahan Timur No.17 Cengkareng Barat, Cengkareng, Jakarta Barat, DKI Jakarta, 57128");
        // email
        driver.findElement(By.id("field-:rj:")).sendKeys("ahmadsahroni"+ Utils.randomNumber(2)+"@gmail.com");
        // phone number
        driver.findElement(By.id("field-:rl:")).sendKeys("08"+ Utils.randomNumber(10));

        // --- parent/guardian information ---
        // name
        driver.findElement(By.id("field-:rn:")).sendKeys("Parent Ahmad Sahroni");
        // phone
        driver.findElement(By.id("field-:rp:")).sendKeys("08"+Utils.randomNumber(10));
        // email
        driver.findElement(By.id("field-:rr:")).sendKeys("parentahmadsahroni@gmail.com");

        // --- emergency contact ---
        // name
        driver.findElement(By.id("field-:rt:")).sendKeys("Sibling Ahmad Sahroni");
        // phone
        driver.findElement(By.id("field-:rv:")).sendKeys("08"+Utils.randomNumber(10));
        // relationship
        new Select(driver.findElement(By.id("field-:r11:"))).selectByVisibleText("Sibling");

        // --- educational background ---
        // high scool name
        driver.findElement(By.id("field-:r13:")).sendKeys("Sibling Ahmad Sahroni");
        // graduation year
        new Select(driver.findElement(By.id("field-:r15:"))).selectByVisibleText("2023");
        // previous educational level
        new Select(driver.findElement(By.id("field-:r17:"))).selectByVisibleText("Senior High School (SMA/SMK)");

        // --- program selection ---
        new Select(driver.findElement(By.id("field-:r19:"))).selectByIndex(2);
        // class type
        new Select(driver.findElement(By.id("field-:r1b:"))).selectByIndex(1);
        // entry path
        new Select(driver.findElement(By.id("field-:r1d:"))).selectByIndex(1);

        //--- supporting documents ---
        String pathTestFilePdf = "C:\\Users\\acer\\IdeaProjects\\lms-univ\\src\\main\\resources\\test-files\\test-pdf.pdf";
        // academic transcript
        driver.findElement(By.id("field-:r1f:")).sendKeys(pathTestFilePdf);
        // diploma/graduation certificate
        driver.findElement(By.id("field-:r1h:")).sendKeys(pathTestFilePdf);
        // national id card/KTP
        driver.findElement(By.id("field-:r1j:")).sendKeys(pathTestFilePdf);
        // family card/KK
        driver.findElement(By.id("field-:r1l:")).sendKeys(pathTestFilePdf);

        // klik button submit
        // driver.findElement(By.xpath("//button[normalize-space()='Submit Application']")).click();

        // validasi muncul notif sukses
        //String toastTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast-1-title"))).getText();
        //assertEquals("Application Submitted Successfully!", toastTitle);

        // kembali ke login
        driver.findElement(By.xpath("//button[normalize-space()='Back to Login']")).click();

        // validasi kalo udah di halaman login
        wait.until(ExpectedConditions.urlToBe(baseUrl+"login"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));

        // login ke admin
        driver.findElement(By.id("username")).sendKeys("newadmin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Login']"))).click();

        // validasi kalo udah di halaman admin
        wait.until(ExpectedConditions.urlToBe(baseUrl+"E-Campus/Adminoffice"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));

        By admissionHeader = By.xpath(
                "//div[contains(@class,'css-1lekzkb')][.//p[normalize-space()='Admission']]"
        );
        wait.until(ExpectedConditions.elementToBeClickable(admissionHeader)).click();

        // ke menu candidate list
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[.//p[normalize-space()='Candidate List']]")
        )).click();

        // validasi kalo datanya masuk
        // klik button reject
        // validasi kalo reject berhasil

    }

    @Test
    void candidateList(){
        // validasi kalo udah di halaman login
        wait.until(ExpectedConditions.urlToBe(baseUrl+"login"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));

        // login ke admin
        driver.findElement(By.id("username")).sendKeys("newadmin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Login']"))).click();

        // validasi kalo udah di halaman admin
        wait.until(ExpectedConditions.urlToBe(baseUrl+"E-Campus/Adminoffice"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));

        By admissionHeader = By.xpath(
                "//div[contains(@class,'css-1lekzkb')][.//p[normalize-space()='Admission']]"
        );
        wait.until(ExpectedConditions.elementToBeClickable(admissionHeader)).click();

        // ke menu candidate list
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[.//p[normalize-space()='Candidate List']]")
        )).click();
    }
}
