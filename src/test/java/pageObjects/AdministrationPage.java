package pageObjects;

import org.apache.xmlbeans.impl.xb.xsdschema.FieldDocument;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AdministrationPage extends BasePage {

    /*@FindBy(xpath = "//a[@class='icon icon-projects projects']")
    WebElement lnkProject;*/

    /*@FindBy(xpath = "//a[@class='icon icon-user users']")
    WebElement lnkUsers;*/

    /*@FindBy(xpath = "//a[@class='icon icon-roles roles']")
    WebElement lnkRolesAndPermission;*/

    /*@FindBy(xpath = "//a[@class='icon icon-plugins plugins']")
    WebElement lnkPlugins;*/

    By lnkProject = By.xpath("//a[@class='icon icon-projects projects']");

    By lnkUsers = By.xpath("//a[@class='icon icon-user users']");

    By lnkRolesAndPermission = By.xpath("//a[@class='icon icon-roles roles']");

    By lnkPlugins = By.xpath("//a[@class='icon icon-plugins plugins']");

    public AdministrationPage(WebDriver driver) {
        super(driver);
    }


    public void clickOnProjects() {
        /*this.lnkProject.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.lnkProject));
        element.click();
    }

    public void clickOnUsers() {
        /*this.lnkUsers.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.lnkUsers));
        element.click();
    }

    public void clickOnRolesAndPermissions() {
        /*this.lnkRolesAndPermission.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.lnkRolesAndPermission));
        element.click();
    }

    public void clickOnPlugins() {
        /*this.lnkPlugins.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.lnkPlugins));
        element.click();
    }
}
