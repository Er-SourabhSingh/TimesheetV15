package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class UserFormPage extends BasePage {

    /*@FindBy(xpath = "//input[@id='user_login']")
    WebElement txtUserLoginName;

    @FindBy(xpath = "//input[@id='user_firstname']")
    WebElement txtUserFirstName;

    @FindBy(xpath = "//input[@id='user_lastname']")
    WebElement txtUserLastName;

    @FindBy(xpath = "//input[@id='user_mail']")
    WebElement txtUserEmail;

    @FindBy(xpath = "//input[@id='user_password']")
    WebElement txtUserPassword;

    @FindBy(xpath = "//input[@id='user_password_confirmation']")
    WebElement txtConfirmationPassword;

    @FindBy(xpath = "//input[@name='commit']")
    WebElement btnCreate;

    @FindBy(xpath = "//h2[1]//a[1]")
    WebElement lnkUsersPage;

    @FindBy(xpath = "//div[@id='flash_notice']")
    WebElement successMsg;*/

    By txtUserLoginName = By.xpath("//input[@id='user_login']");

    By txtUserFirstName = By.xpath("//input[@id='user_firstname']");

    By txtUserLastName = By.xpath("//input[@id='user_lastname']");

    By txtUserEmail = By.xpath("//input[@id='user_mail']");

    By txtUserPassword = By.xpath("//input[@id='user_password']");

    By txtConfirmationPassword = By.xpath("//input[@id='user_password_confirmation']");

    By btnCreate = By.xpath("//input[@name='commit']");

    By lnkUsersPage = By.xpath("//h2[1]//a[1]");

    By successMsg = By.xpath("//div[@id='flash_notice']");

    public UserFormPage(WebDriver driver) {
        super(driver);
    }

    public void setTxtUserLoginName(String txtUserLoginName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtUserLoginName));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",element);
        element.clear();
        element.sendKeys(txtUserLoginName);
    }

    public void setTxtUserFirstName(String txtUserFirstName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtUserFirstName));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.clear();
        element.sendKeys(txtUserFirstName);
    }

    public void setTxtUserLastName(String txtUserLastName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtUserLastName));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.clear();
        element.sendKeys(txtUserLastName);
    }

    public void setTxtUserEmail(String txtUserEmail) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtUserEmail));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.clear();
        element.sendKeys(txtUserEmail);
    }

    public void setTxtUserPassword(String txtUserPassword) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtUserPassword));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.clear();
        element.sendKeys(txtUserPassword);
    }

    public void setTxtConfirmationPassword(String txtConfirmationPassword) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtConfirmationPassword));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.clear();
        element.sendKeys(txtConfirmationPassword);
    }

    public void clickOnCreate() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.btnCreate));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnUsersPage() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.lnkUsersPage));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public String getSuccessMessage() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.successMsg));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return element.getText();
    }
}
