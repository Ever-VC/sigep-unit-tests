package users_management;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class UsersManagement {
    protected WebDriver driver;
    protected String user = "Alberto Test Usuario";
    protected String email = "usuario.test@gmail.com";
    protected String user_updated = user + " actualizado";
    protected String email_updated = "usuario.test.actualizado@gmail.com";
    
    // Credenciales de administrador
    private static final String ADMIN_EMAIL = "elroble.ferreteria.sv@gmail.com";
    private static final String ADMIN_PASSWORD = "1234";
    private static final String USER_PASSWORD = "elhipopotamoAzul123$";
    private static final String BASE_URL = "https://sigep-el-roble-production.up.railway.app";
    private static final int WAIT_TIME = 1000;
    
    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL + "/");
    }
    
    /**
     * Método auxiliar para esperar la carga de la página
     */
    private void waitForPageLoad() {
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Método auxiliar para realizar login
     */
    private void performLogin(String email, String password) {
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Iniciar Sesión']")).click();
        waitForPageLoad();
    }
    
    /**
     * Método auxiliar para verificar que se cargó el panel de administración
     */
    private void verifyAdminPanel() {
        WebElement panelAdmin = driver.findElement(By.xpath("//h3[contains(text(),'Panel de Administración')]"));
        Assert.assertNotNull(panelAdmin);
        System.out.println("Se encontró el panel de administración: " + panelAdmin.getText());
    }
    
    /**
     * Método auxiliar para cerrar sesión
     */
    private void performLogout() {
        driver.findElement(By.xpath("//button[contains(.,'Cerrar sesión')]")).click();
        waitForPageLoad();
    }
    
    /**
     * Método auxiliar para navegar a una URL específica
     */
    private void navigateTo(String path) {
        driver.findElement(By.xpath("//a[@href='" + BASE_URL + path + "']")).click();
        waitForPageLoad();
    }
    
    @Test(priority = 1)
    public void adminLogin() {
        performLogin(ADMIN_EMAIL, ADMIN_PASSWORD);
        verifyAdminPanel();
    }
    
    @Test(priority = 2)
    public void usersView() {
        navigateTo("/users");
        
        WebElement panelUsuarios = driver.findElement(By.xpath("//h1[contains(text(),'Lista de Usuarios')]"));
        Assert.assertNotNull(panelUsuarios);
        System.out.println("Se encontró el panel de usuarios: " + panelUsuarios.getText());
    }
    
    @Test(priority = 3)
    public void createUser() {
        navigateTo("/users/create");
        
        // Llenado del formulario
        driver.findElement(By.xpath("//input[@placeholder='Nombre completo del usuario']")).sendKeys(user);
        driver.findElement(By.xpath("//input[@placeholder='Correo electrónico']")).sendKeys(email);
        driver.findElement(By.xpath("//input[@placeholder='Contraseña (mínimo 8 caracteres)']")).sendKeys(USER_PASSWORD);
        driver.findElement(By.xpath("//input[@placeholder='Confirmar contraseña']")).sendKeys(USER_PASSWORD);
        
        driver.findElement(By.xpath("//button[contains(.,'Guardar')]")).click();
        waitForPageLoad();
        
        // Verifica la creación del usuario
        WebElement usuarioCreado = driver.findElement(By.xpath("//div[contains(text(),'" + user + "')]"));
        Assert.assertNotNull(usuarioCreado);
    }
    
    @Test(priority = 4)
    public void logoutUser() {
        performLogout();
        
        WebElement loginPanel = driver.findElement(By.xpath("//h2[contains(text(),'Inicio de Sesión - SIGEP')]"));
        Assert.assertNotNull(loginPanel);
        System.out.println("Se encontró el panel de inicio de sesión: " + loginPanel.getText());
    }
    
    @Test(priority = 5)
    public void loginCreatedUser() {
        performLogin(email, USER_PASSWORD);
        verifyAdminPanel();
    }
    
    @Test(priority = 6)
    public void logoutCreatedUser() {
        performLogout();
        performLogin(ADMIN_EMAIL, ADMIN_PASSWORD);
        verifyAdminPanel();
        navigateTo("/users");
    }
    
    @Test(priority = 7)
    public void updateUser() {
        driver.findElement(By.xpath("//tr[td/div[contains(text(),'" + user + "')]]//a[contains(text(),'Editar')]")).click();
        waitForPageLoad();
        
        // Actualiza el nombre
        WebElement nameField = driver.findElement(By.xpath("//input[@placeholder='Nombre completo del usuario']"));
        nameField.clear();
        nameField.sendKeys(user_updated);
        
        // Actualiza el email
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='correo@ejemplo.com']"));
        emailField.clear();
        emailField.sendKeys(email_updated);
        
        driver.findElement(By.xpath("//button[contains(.,'Actualizar Usuario')]")).click();
        waitForPageLoad();
        
        // Verifica que se haya actualizado el usuario
        WebElement usuarioActualizado = driver.findElement(By.xpath("//div[contains(text(),'" + user_updated + "')]"));
        Assert.assertNotNull(usuarioActualizado);
    }
    
    @Test(priority = 8)
    public void deactivateUser() {
        driver.findElement(By.xpath("//tr[td/div[contains(text(),'" + user_updated + "')]]//a[contains(text(),'Estado')]")).click();
        waitForPageLoad();
        
        // Cambia el estado a Inactivo
        WebElement statusSelect = driver.findElement(By.id("status"));
        statusSelect.click();
        driver.findElement(By.xpath("//option[@value='inactive']")).click();
        
        driver.findElement(By.xpath("//button[contains(.,'Actualizar Estado')]")).click();
        waitForPageLoad();
        
        // Verifica que sí se haya desactivado
        WebElement usuarioDesactivado = driver.findElement(By.xpath("//tr[td/div[contains(text(),'" + user_updated + "')]]//span[contains(text(),'Inactivo')]"));
        Assert.assertNotNull(usuarioDesactivado);
        System.out.println("El usuario ha sido desactivado correctamente: " + usuarioDesactivado.getText());
    }
    
    @AfterClass
    public void tearDown() {
    	// Si el dirver NO es null, cierra el navegador (lo hace al finalizar las pruebas con el AfterClass)
        if (driver != null) {
            driver.quit();
        }
    }
}