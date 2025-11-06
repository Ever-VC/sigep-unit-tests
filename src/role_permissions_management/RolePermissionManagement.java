package role_permissions_management;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RolePermissionManagement {
    protected WebDriver driver;
    private static final String BASE_URL = "https://sigep-el-roble-production.up.railway.app";
    private static final int WAIT_TIME = 1000;
    private static final int MODAL_WAIT_TIME = 2000;
    private static final String ROLE_NAME = "Rol de Prueba";
    private static final String ROLE_NAME_UPDATED = ROLE_NAME + " Actualizado";
    
    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL + "/");
    }
    
    private void waitForPageLoad() {
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void waitForModalBug() {
        try {
            Thread.sleep(MODAL_WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void navigateTo(String path) {
        driver.findElement(By.xpath("//a[@href='" + BASE_URL + path + "']")).click();
        waitForPageLoad();
    }
    
    private void searchAndAddPermission(String permissionName) {
        WebElement searchInput = driver.findElement(By.xpath("//input[@placeholder='Buscar permisos...']"));
        searchInput.clear();
        searchInput.sendKeys(permissionName);
        waitForPageLoad();
        
        driver.findElement(By.xpath("//li[contains(@class, 'permission-item')]//span[contains(text(), '" + permissionName + "')]/following-sibling::div//button[contains(text(), 'Agregar')]")).click();
        waitForPageLoad();
    }
    
    @Test(priority = 1)
    public void adminLogin() {
        driver.findElement(By.id("email")).sendKeys("elroble.ferreteria.sv@gmail.com");
        driver.findElement(By.id("password")).sendKeys("1234");
        driver.findElement(By.xpath("//input[@value='Iniciar Sesión']")).click();
        waitForPageLoad();
        
        WebElement panelAdmin = driver.findElement(By.xpath("//h3[contains(text(),'Panel de Administración')]"));
        Assert.assertNotNull(panelAdmin);
        System.out.println("Se encontró el panel de administración: " + panelAdmin.getText());
    }
    
    @Test(priority = 2)
    public void rolePermissionsView() {
        navigateTo("/admin/roles");
        waitForModalBug();
        
        WebElement listaRoles = driver.findElement(By.xpath("//h1[contains(text(),'Lista de roles')]"));
        Assert.assertNotNull(listaRoles);
        System.out.println("Se encontró el panel de roles y permisos: " + listaRoles.getText());
    }
    
    @Test(priority = 3)
    public void clickNewRole() {
        driver.findElement(By.xpath("//button[contains(text(), 'Nuevo Rol')]")).click();
        waitForPageLoad();
        
        WebElement createTitle = driver.findElement(By.xpath("//h2[contains(text(), 'Crear nuevo rol')]"));
        Assert.assertNotNull(createTitle);
        System.out.println("Se encontró la vista de crear rol: " + createTitle.getText());
    }
    
    @Test(priority = 4)
    public void fillRoleName() {
        WebElement roleNameInput = driver.findElement(By.xpath("//input[@placeholder='Ej. administrador, RRHH']"));
        roleNameInput.sendKeys(ROLE_NAME);
        waitForPageLoad();
    }
    
    @Test(priority = 5)
    public void addDirectPermissions() {
        driver.findElement(By.xpath("//li[contains(@class, 'permission-item')]//span[text()='ver planillas']/following-sibling::div//button[contains(text(), 'Agregar')]")).click();
        waitForPageLoad();
        
        driver.findElement(By.xpath("//li[contains(@class, 'permission-item')]//span[text()='ver asistencias']/following-sibling::div//button[contains(text(), 'Agregar')]")).click();
        waitForPageLoad();
        
        driver.findElement(By.xpath("//li[contains(@class, 'permission-item')]//span[text()='ver empleados']/following-sibling::div//button[contains(text(), 'Agregar')]")).click();
        waitForPageLoad();
        
        driver.findElement(By.xpath("//li[contains(@class, 'permission-item')]//span[text()='generar planillas']/following-sibling::div//button[contains(text(), 'Agregar')]")).click();
        waitForPageLoad();
        
        System.out.println("Se agregaron 4 permisos directamente de la lista");
    }
    
    @Test(priority = 6)
    public void addPermissionsBySearch() {
        searchAndAddPermission("registrar asistencias");
        System.out.println("Se agregó el permiso: registrar asistencias");
        
        searchAndAddPermission("modificar asistencias");
        System.out.println("Se agregó el permiso: modificar asistencias");
    }
    
    @Test(priority = 7)
    public void removePermission() {
        WebElement searchInput = driver.findElement(By.xpath("//input[@placeholder='Buscar permisos...']"));
        searchInput.clear();
        waitForPageLoad();
        
        driver.findElement(By.xpath("//span[text()='modificar asistencias']/following-sibling::button[contains(text(), 'Quitar')]")).click();
        waitForPageLoad();
        
        System.out.println("Se quitó el permiso: modificar asistencias");
    }
    
    @Test(priority = 8)
    public void saveRole() {
        driver.findElement(By.xpath("//button[@type='submit' and contains(text(), 'Guardar rol')]")).click();
        waitForPageLoad();
        waitForModalBug();
        
        WebElement successMessage = driver.findElement(By.xpath("//div[contains(text(), 'Rol creado exitosamente.')]"));
        Assert.assertNotNull(successMessage);
        System.out.println("Rol creado exitosamente: " + successMessage.getText());
        
        WebElement roleCard = driver.findElement(By.xpath("//h3[contains(text(), '" + ROLE_NAME + "')]"));
        Assert.assertNotNull(roleCard);
        System.out.println("Se encontró la tarjeta del rol creado: " + roleCard.getText());
    }
    
    @Test(priority = 9)
    public void editRole() {
        driver.findElement(By.xpath("//h3[contains(text(), '" + ROLE_NAME + "')]/ancestor::div[contains(@class, 'group')]//button[contains(text(), 'Editar')]")).click();
        waitForPageLoad();
        
        WebElement editTitle = driver.findElement(By.xpath("//h2[contains(text(), 'Editar rol')]"));
        Assert.assertNotNull(editTitle);
        System.out.println("Se encontró la vista de editar rol: " + editTitle.getText());
    }
    
    @Test(priority = 10)
    public void createNewPermission() {
        WebElement newPermissionInput = driver.findElement(By.xpath("//input[@placeholder='Ej. editar empleados']"));
        newPermissionInput.sendKeys("permiso prueba");
        
        driver.findElement(By.xpath("//input[@placeholder='Ej. editar empleados']/following-sibling::button[contains(text(), 'Guardar')]")).click();
        waitForPageLoad();
        
        System.out.println("Se creó el permiso: permiso prueba");
    }
    
    @Test(priority = 11)
    public void editNewPermission() {
        WebElement searchInput = driver.findElement(By.xpath("//input[@placeholder='Buscar permisos...']"));
        searchInput.clear();
        searchInput.sendKeys("permiso prueba");
        waitForPageLoad();
        
        List<WebElement> actionButtons = driver.findElements(By.xpath("//li[contains(@class, 'permission-item')]//span[contains(text(), 'permiso prueba')]/following-sibling::div//button"));
        
        if (actionButtons.size() >= 3) {
            actionButtons.get(0).click();
        } else {
            driver.findElement(By.xpath("//li[contains(@class, 'permission-item')]//span[contains(text(), 'permiso prueba')]/following-sibling::div/button[1]")).click();
        }
        waitForPageLoad();
        
        WebElement newPermissionInput = driver.findElement(By.xpath("//input[@placeholder='Ej. editar empleados']"));
        newPermissionInput.clear();
        newPermissionInput.sendKeys("editar prueba");
        
        driver.findElement(By.xpath("//input[@placeholder='Ej. editar empleados']/following-sibling::button[contains(text(), 'Guardar')]")).click();
        waitForPageLoad();

        // Refresca la página para evitar problemas de visualización
        driver.navigate().refresh();
        
        System.out.println("Se editó el permiso de 'permiso prueba' a 'editar prueba'");
    }
    
    @Test(priority = 12)
    public void deleteNewPermission() {
        WebElement searchInput = driver.findElement(By.xpath("//input[@placeholder='Buscar permisos...']"));
        searchInput.clear();
        searchInput.sendKeys("editar prueba");
        waitForPageLoad();
        
        List<WebElement> actionButtons = driver.findElements(By.xpath("//li[contains(@class, 'permission-item')]//span[contains(text(), 'editar prueba')]/following-sibling::div//button"));
        
        if (actionButtons.size() >= 3) {
            actionButtons.get(1).click();
        } else {
            driver.findElement(By.xpath("//li[contains(@class, 'permission-item')]//span[contains(text(), 'editar prueba')]/following-sibling::div/button[2]")).click();
        }
        waitForPageLoad();
        
        driver.findElement(By.xpath("//button[contains(text(), 'Eliminar')]")).click();
        waitForPageLoad();
        
        System.out.println("Se eliminó el permiso: editar prueba");
    }
    
    @Test(priority = 13)
    public void verifyPermissionDeleted() {
        WebElement searchInput = driver.findElement(By.xpath("//input[@placeholder='Buscar permisos...']"));
        searchInput.clear();
        searchInput.sendKeys("editar prueba");
        waitForPageLoad();
        
        List<WebElement> permissionItems = driver.findElements(By.xpath("//li[contains(@class, 'permission-item')]//span[contains(text(), 'editar prueba')]"));
        Assert.assertTrue(permissionItems.isEmpty(), "El permiso debería estar eliminado");
        
        System.out.println("Se verificó que el permiso fue eliminado correctamente");
    }

    @Test(priority = 14)
    public void updateRoleName() {
        WebElement roleNameInput = driver.findElement(By.xpath("//input[@placeholder='Ej. administrador, RRHH']"));
        roleNameInput.clear();
        roleNameInput.sendKeys(ROLE_NAME_UPDATED);
        waitForPageLoad();
        
        System.out.println("Se actualizó el nombre del rol a: " + ROLE_NAME_UPDATED);
    }
    
    @Test(priority = 15)
    public void saveUpdatedRole() {
        WebElement searchInput = driver.findElement(By.xpath("//input[@placeholder='Buscar permisos...']"));
        searchInput.clear();
        waitForPageLoad();
        
        driver.findElement(By.xpath("//button[@type='submit' and contains(text(), 'Guardar rol')]")).click();
        waitForPageLoad();
        waitForModalBug();
        
        WebElement successMessage = driver.findElement(By.xpath("//div[contains(text(), 'Rol actualizado exitosamente.')]"));
        Assert.assertNotNull(successMessage);
        System.out.println("Rol actualizado exitosamente: " + successMessage.getText());
        
        WebElement updatedRoleCard = driver.findElement(By.xpath("//h3[contains(text(), '" + ROLE_NAME_UPDATED + "')]"));
        Assert.assertNotNull(updatedRoleCard);
        System.out.println("Se encontró la tarjeta del rol actualizado: " + updatedRoleCard.getText());
    }

    @Test(priority = 16)
    public void deleteRole() {
        driver.findElement(By.xpath("//h3[contains(text(), '" + ROLE_NAME_UPDATED + "')]/ancestor::div[contains(@class, 'group')]//button[contains(text(), 'Eliminar')]")).click();
        waitForPageLoad();
        
        driver.findElement(By.xpath("//div[contains(@class, 'fixed inset-0')]//button[contains(@class, 'bg-red-600') and contains(text(), 'Eliminar')]")).click();
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        List<WebElement> deletedRoleCard = driver.findElements(By.xpath("//h3[contains(text(), '" + ROLE_NAME_UPDATED + "')]"));
        Assert.assertTrue(deletedRoleCard.isEmpty(), "El rol debería estar eliminado");
        
        System.out.println("El rol fue eliminado correctamente");
    }
    
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}