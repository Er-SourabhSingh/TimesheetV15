package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class IssueDetailPage extends BasePage {
    
    /*@FindBy(xpath = "//span[contains(@class,'badge badge-status')]")
    WebElement issueStatus;

    @FindBy(xpath = "//div[@id='flash_notice']")
    WebElement successMessage;

    @FindBy(xpath = "//div[@id='errorExplanation']")
    WebElement btnEdit;*/

    By issueStatus = By.xpath("//span[contains(@class,'badge badge-status')]");

    By successMessage = By.xpath("//div[@id='flash_notice']");

    By errorMessage = By.xpath("//div[@id='errorExplanation']");

    By btnEdit = By.xpath("//a[@accesskey='e']");


    public IssueDetailPage(WebDriver driver) {
        super(driver);
       // wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public String getIssueStatus() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.issueStatus));
            return element.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getSuccessMessage() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.successMessage));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            return element.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getErrorMessage(){
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.errorMessage));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            return element.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void clickEditIssueBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnEdit));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }
}

