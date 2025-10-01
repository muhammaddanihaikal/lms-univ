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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        driver.findElement(By.xpath("//button[normalize-space()='Submit Application']")).click();

        // validasi muncul notif sukses
        String toastTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast-1-title"))).getText();
        assertEquals("Application Submitted Successfully!", toastTitle);

        // ambil deskripsi toast
        String toastDesc = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("toast-1-description"))
        ).getText();

        // ekstrak Application ID pakai regex (REG + digit)
        Pattern pattern = Pattern.compile("\\bREG\\d+\\b");
        Matcher matcher = pattern.matcher(toastDesc);

        // simpan Applicatoin ID
        String applicationId = matcher.find() ? matcher.group() : null;

        // hapus notifikasi
        // pastikan toast muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast-1")));

        // cari tombol close
        WebElement closeBtn = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[@id='toast-1']//button[@aria-label='Close']")
                )
        );

        // klik pakai JS
        Utils.clickForce(driver, closeBtn);

        // validasi toast udah hilang
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("toast-1")));

        // kembali ke login
        driver.findElement(By.xpath("//button[normalize-space()='Back to Login']")).click();

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

        // klik menu admission
        By admissionHeader = By.xpath(
                "//p[normalize-space()='Admission']/ancestor::div[contains(@class,'css-1lekzkb')]"
        );
        wait.until(ExpectedConditions.elementToBeClickable(admissionHeader)).click();

        // klik sub menu candidate list
        By candidateList = By.xpath(
                "//p[normalize-space()='Candidate List']/ancestor::a"
        );
        wait.until(ExpectedConditions.elementToBeClickable(candidateList)).click();

        // klik button titik 3
        By actionButton = By.xpath(
                "//tr[td[normalize-space()='" + "REG2025225108" + "']]//button[@aria-label='Actions']"
        );
        wait.until(ExpectedConditions.elementToBeClickable(actionButton)).click();

        // tunggu sampe pop up muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("css-1u2cvaz")
        ));

        // klik button update status
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[normalize-space()='Update Status']]")
        )).click();

        // klik button reject application
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[normalize-space()='Reject Application']]")
        )).click();


    }
}
