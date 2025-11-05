package employee_login;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginEmployee {
	
	protected WebDriver driver;
	
	@BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://sigep-el-roble-production.up.railway.app/");
    }
	
	@Test(priority = 1)
    public void employeeLogin() {
        driver.findElement(By.id("email")).sendKeys("evervc.dev@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Horno#32");
        driver.findElement(By.xpath("//input[@value='Iniciar Sesión']")).click();

        //Accede al H2 que dice: Panel de Empleado, lo que significa que si ha accedido
        WebElement panelEmpleado = driver.findElement(By.xpath("//h2[contains(text(),'Panel del Empleado')]"));
        Assert.assertNotNull(panelEmpleado);
        System.out.println("Se encontró el panel de empleado: " + panelEmpleado.getText());
    }

}
