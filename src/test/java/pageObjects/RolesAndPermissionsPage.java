package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class RolesAndPermissionsPage extends BasePage {

    /*@FindBy(xpath = "//a[@class='icon icon-add']")
    WebElement btnCreateRole;

    @FindBy(xpath = "//div[@id='flash_notice']")
    WebElement successMsg;*/

    By btnCreateRole = By.xpath("//a[@class='icon icon-add']");

    By successMsg = By.xpath("//div[@id='flash_notice']");

    public RolesAndPermissionsPage(WebDriver driver) {
        super(driver);
    }

    public void clickCreateRoleBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnCreateRole));
        element.click();
    }

    public void clickOnRoleName(String roleName){
        By xpathRole = By.xpath("//a[normalize-space()='"+roleName+"']");
        WebElement element = driver.findElement(xpathRole);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public String getSuccessMsg() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.successMsg));
        return element.getText();
    }

    public List<String> getRoleList() {
        By emptyStateLocator = By.xpath("//p[@class='nodata']");
        if(!driver.findElements(emptyStateLocator).isEmpty()){
            return new ArrayList<>();
        }
        By rolesLocator = By.xpath("//table[@class='list roles']//tbody//td[@class='name']//a");
        List<WebElement> roles = new ArrayList<>();
        int attempts = 0;
        while (attempts < 3){
            try{
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rolesLocator));
                roles = driver.findElements(rolesLocator);
                break;
            }catch (StaleElementReferenceException | NoSuchElementException e){
                attempts++;
            }
        }
        List<String> texts = new ArrayList<>();
        for (WebElement el: roles){
            String text = el.getText().trim();
            if(!text.isEmpty())texts.add(text);
        }
        return texts;
    }


}
