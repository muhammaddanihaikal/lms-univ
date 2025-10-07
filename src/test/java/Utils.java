import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

public class Utils {

    public static String randomNumber(int length) {
        String digits = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(digits.charAt(random.nextInt(digits.length())));
        }

        return sb.toString();
    }

    // klik pakai JS kalau klik biasa suka gagal (misalnya ketutup animasi/overlay)
    public static void clickForce(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Tutup semua toast (chakra UI) yang sedang muncul.
     * Ada print log supaya kelihatan prosesnya.
     */
    public static void closeAllToasts(WebDriver driver, WebDriverWait wait) {
        By toastSel = By.cssSelector("div[id^='toast-'][data-status]");
        By closeSel = By.xpath("//div[starts-with(@id,'toast-')]//button[@aria-label='Close']");

        List<WebElement> toasts = driver.findElements(toastSel);
        if (toasts.isEmpty()) {
            System.out.println("🟢 Tidak ada toast yang aktif saat ini.");
            return;
        }

        System.out.println("🔔 Ditemukan " + toasts.size() + " toast aktif, mulai menutup...");

        long deadline = System.currentTimeMillis() + 10_000; // batas 10 detik
        int loop = 1;

        while (System.currentTimeMillis() < deadline) {
            System.out.println("⏳ Iterasi ke-" + loop++);

            List<WebElement> closeBtns = driver.findElements(closeSel);

            if (closeBtns.isEmpty()) {
                System.out.println("⚪ Tidak ada tombol close, mungkin toast auto-hide...");
                if (driver.findElements(toastSel).isEmpty()) {
                    System.out.println("✅ Semua toast sudah hilang (auto-hide).");
                    break;
                }
            } else {
                try {
                    System.out.println("👉 Klik tombol close toast (" + closeBtns.size() + " ditemukan)");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeBtns.get(0));
                } catch (StaleElementReferenceException e) {
                    System.out.println("♻️ Toast berubah (stale), ulangi loop...");
                } catch (ElementClickInterceptedException e) {
                    System.out.println("🚧 Toast tertutup elemen lain, akan coba lagi...");
                } catch (Exception e) {
                    System.out.println("❌ Gagal klik tombol close: " + e.getMessage());
                }
            }

            try {
                Thread.sleep(150);
            } catch (InterruptedException ignored) {}

            if (driver.findElements(toastSel).isEmpty()) {
                System.out.println("✅ Semua toast berhasil ditutup.");
                break;
            }
        }

        try {
            wait.until(ExpectedConditions.numberOfElementsToBe(toastSel, 0));
            System.out.println("🎉 Konfirmasi: tidak ada toast tersisa di layar.");
        } catch (TimeoutException e) {
            System.out.println("⚠️ Timeout: Masih ada toast yang belum hilang setelah 10 detik.");
        }
    }

    /**
     * Menunggu semua toast auto-hide tanpa menutupnya.
     */
    public static void waitToastsGone(WebDriver driver, WebDriverWait wait) {
        By toastSel = By.cssSelector("div[id^='toast-'][data-status]");
        System.out.println("🕓 Menunggu semua toast hilang otomatis...");
        wait.until(ExpectedConditions.numberOfElementsToBe(toastSel, 0));
        System.out.println("✅ Semua toast sudah hilang.");
    }
}
