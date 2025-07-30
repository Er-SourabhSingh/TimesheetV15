package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    /*@FindBy(xpath = "//input[@id='username']")
    WebElement txtUserName;
    @FindBy(xpath = "//input[@id='password']")
    WebElement txtUserPassword;
    @FindBy(xpath = "//input[@id='login-submit']")
    WebElement btnLogin;*/

    By txtUserName = By.xpath("//input[@id='username']");

    By txtUserPassword = By.xpath("//input[@id='password']");

    By btnLogin = By.xpath("//input[@id='login-submit']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void clickBtnLogin() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnLogin));
        element.click();
    }

    public void setTxtUserName(String userName) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.txtUserName));
        element.sendKeys(userName);
    }

    public void setTxtUserPassword(String pwd) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.txtUserPassword));
        element.sendKeys(pwd);
    }

}
