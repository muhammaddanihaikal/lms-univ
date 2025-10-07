package tests.student;

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
import utils.Utils;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdmissionTest extends env_target {
    private WebDriverWait wait;
    private String applicationId;
    private String studentFullName;
    private String studentEmail;
    private String studentId;

    @BeforeEach
    void setUp() {
        // setup driver chrome
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\drivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito", "--disable-save-password-bubble");
        driver = new ChromeDriver(options);

        // buka browser
        driver.manage().window().maximize();
        driver.get(baseUrl);

        // inisialisasi explicit wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // set zoom ke 75%
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='70%'");
    }

    @AfterEach
    void tearDown() {
        // tutup browser
         if (driver != null) driver.quit();
    }

    @Test
    void admissionApproveCandidate(){
        createStudentData();
        approveStudent();
    }

    @Test
    void admissionRejectCandidate(){
        createStudentData();
        rejectStudent();
    }

    @Test
    void getEmailStudent(){
        createStudentAccount();
    }

    // ----------METHOD----------
    // membuat data mahasiswa
    void createStudentData(){
        // klik menu admission
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Admission"))).click();

        // pastikan halaman admission siap
        wait.until(ExpectedConditions.urlToBe(baseUrl+"admission"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));

        // --- personal information ---
        // full name
        driver.findElement(By.id("field-:r5:")).sendKeys("Ahmad Sahroni"+ Utils.randomNumber(2));
        // simpan fullName
        this.studentFullName = driver.findElement(By.id("field-:r5:")).getAttribute("value");
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
        // simpan email
        this.studentEmail = driver.findElement(By.id("field-:rj:")).getAttribute("value");
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
        String pathTestFilePdf = "C:\\Users\\acer\\IdeaProjects\\lms-univ\\src\\test\\resources\\test-files\\test-pdf.pdf";
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
        this.applicationId = matcher.find() ? matcher.group() : null;

        // hapus toast
        Utils.closeAllToasts(driver, wait);

        // kembali ke login
        driver.findElement(By.xpath("//button[normalize-space()='Back to Login']")).click();

        // validasi kalo udah di halaman login
        wait.until(ExpectedConditions.urlToBe(baseUrl+"login"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));
    }

    // login admin
    void loginAsAdmin() {
        // tunggu input username siap diketik
        WebElement username = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("username"))
        );

        // tunggu input password tampil
        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("password"))
        );

        // isi username admin
        username.clear();
        username.sendKeys("newadmin");

        // isi password admin
        password.clear();
        password.sendKeys("admin123");

        // klik tombol Login
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Login']")
        )).click();

        // hapus toast
        Utils.closeAllToasts(driver, wait);

        // pastikan sudah masuk ke halaman Admin Office
        wait.until(ExpectedConditions.urlToBe(baseUrl + "E-Campus/Adminoffice"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));
    }

    // approve mahasiswa
    void approveStudent() {
        // login admin
        loginAsAdmin();

        // buka menu Admission > Candidate List
        By admissionHeader = By.xpath("//p[normalize-space()='Admission']/ancestor::div[contains(@class,'css-1lekzkb')]");
        wait.until(ExpectedConditions.elementToBeClickable(admissionHeader)).click();

        By candidateList = By.xpath("//p[normalize-space()='Candidate List']/ancestor::a");
        wait.until(ExpectedConditions.elementToBeClickable(candidateList)).click();

        // tangkap row berdasarkan Application ID
        By rowLocator = By.xpath("//tr[td[normalize-space()='" + this.applicationId + "']]");
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));

        // klik tombol actions di dalam row
        WebElement actionBtn = wait.until(ExpectedConditions.elementToBeClickable(
                row.findElement(By.xpath(".//button[@aria-label='Actions']"))
        ));
        actionBtn.click();

        // buka popup update status > approve
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("css-1u2cvaz")));
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[normalize-space()='Update Status']]")
        )).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[normalize-space()='Approve Application']]")
        )).click();

        // hapus toast
        Utils.closeAllToasts(driver, wait);

        // tunggu row hilang (DOM berubah)
        wait.until(ExpectedConditions.stalenessOf(row));

        // buka Master > Master Student
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[normalize-space()='Master']"))).click();
        WebElement masterStudent = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[.//p[normalize-space()='Master Student']]")
        ));
        Utils.clickForce(driver, masterStudent);

        // validasi halaman Master Student
        wait.until(ExpectedConditions.urlContains("/E-Campus/Adminoffice/master/student"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Master Student']")));

        // pastikan nama mahasiswa muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[normalize-space()='" + this.studentFullName + "']")));

        // ambil Student ID
        this.studentId = driver.findElement(
                By.xpath("//p[normalize-space()='" + this.studentFullName + "']/ancestor::tr//td[2]//span")
        ).getText();

    }

    // reject mahasiswa
    void rejectStudent(){
        loginAsAdmin();

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
                "//tr[td[normalize-space()='" + this.applicationId + "']]//button[@aria-label='Actions']"
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

        // tunggu sampe statusnya berubah
        wait.until(ExpectedConditions.textToBe(By.xpath(
                "//tr[td[normalize-space()='"+this.applicationId+"']]//td[6]//span"
        ), "REJECTED"));

        // validasi reject application
        String statusText = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//tr[td[normalize-space()='" + this.applicationId + "']]//td[6]//span")
                )
        ).getText();
        assertEquals("REJECTED", statusText);
    }

    // membuat akun / get-email
    void createStudentAccount(){
        admissionApproveCandidate();

        // logout
        driver.findElement(By.xpath("//p[text()='Admin Office']/ancestor::button")).click();
        // tunggu sampai container menu list muncul
        WebElement menuContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@role='menu' and contains(@class,'chakra-menu__menu-list')]")
                )
        );

        // setelah container visible, klik tombol Sign Out
        menuContainer.findElement(By.xpath(".//button[normalize-space()='Sign Out']")).click();

        // validasi kalo udah di halaman login
        wait.until(ExpectedConditions.urlToBe(baseUrl+"login"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));

        // klik button get your email
        wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Get Your Email']"))
        ).click();

        // validasi kalo udah di halaman get email
        wait.until(ExpectedConditions.urlToBe(baseUrl+"get-email"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));

        // isi Personal Email
        WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@type='email' and contains(@placeholder,'personal email')]")
                )
        );
        emailField.sendKeys(studentEmail);

        // isi Student ID
        WebElement nimField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@type='text' and contains(@placeholder,'student ID')]")
                )
        );
        nimField.sendKeys(studentId);

        // hapus toast
        Utils.closeAllToasts(driver, wait);

        // klik Verify Student Record
        WebElement verifyBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[normalize-space()='Verify Student Record']")
                )
        );
        verifyBtn.click();

        // isi Username
        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@type='text' and contains(@placeholder,'desired username')]")
                )
        );
        usernameField.sendKeys(studentFullName.toLowerCase().replaceAll("\\s+", "") + "123");

        // isi Password
        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@type='password' and contains(@placeholder,'secure password')]")
                )
        );
        passwordField.sendKeys("12345678");

        // isi Confirm Password
        WebElement confirmPasswordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@type='password' and contains(@placeholder,'Confirm your password')]")
                )
        );
        confirmPasswordField.sendKeys("12345678");


        // klik tombol Create Campus Account
        WebElement submitBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[normalize-space()='Create Campus Account']")
                )
        );
        submitBtn.click();

        // tunggu sampai alert sukses muncul
        WebElement successAlert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[normalize-space()='Registration Successful!']")
                )
        );
        // validasi judul alert
        assertEquals("Registration Successful!", successAlert.getText());

        // kurang validasi kalo emailnya itu bener
        // misal "nama@university.edu.ac.id" bukan "username@univesity.edu.ac.id"



    }
}
