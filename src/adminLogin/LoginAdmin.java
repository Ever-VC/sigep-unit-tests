package adminLogin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginAdmin {
protected WebDriver driver;
	
	@BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://sigep-el-roble-production.up.railway.app/");
    }
	
	@Test(priority = 1)
    public void adminLogin() {
        driver.findElement(By.id("email")).sendKeys("elroble.ferreteria.sv@gmail.com");
        driver.findElement(By.id("password")).sendKeys("1234");
        driver.findElement(By.xpath("//input[@value='Iniciar Sesión']")).click();

        //Espera 1 para que cargue la página después de iniciar sesión (por el tiempo de respuesta del servidor)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Accede al elemento H3 que contiene el título del panel (si lo encuentra es porque pudo entrar)
        WebElement panelEmpleado = driver.findElement(By.xpath("//h3[contains(text(),'Panel de Administración')]"));
        Assert.assertNotNull(panelEmpleado);
        System.out.println("Se encontró el panel de empleado: " + panelEmpleado.getText());
    }

}
