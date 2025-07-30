package pageObjects;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TimesheetConfiguration extends BasePage{
    /*@FindBy (xpath = "//input[@id='settings_unassigned_issues']")
    WebElement checkboxUnassignedUser;

    @FindBy (xpath = "//input[@name='commit']")
    WebElement btnApply;*/

    By checkboxUnassignedUser = By.xpath("//input[@id='settings_unassigned_issues']");

    By btnApply = By.xpath("//input[@name='commit']");

    public TimesheetConfiguration(WebDriver driver){
        super(driver);
    }

    public void checkCheckboxUnassignedUser(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxUnassignedUser));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected())
            element.click();
    }

    public void clickOnApplyBtn(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnApply));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

}
