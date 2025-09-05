package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HeaderPage extends BasePage {

    By headerTimesheetTab = By.xpath("//a[contains(@class,'timesheets')]");

    By headerAdministrationTab = By.xpath("//a[@class='administration']");

    By headerProjectsTab = By.xpath("//div[@id='top-menu']//a[normalize-space()='Projects']");

    By linkLogout = By.xpath("//a[@class='logout']");

    By subMenuIssuesTab = By.xpath("//a[@class='issues' or @class='issues selected']");

    By subMenuSpentTimeTab = By.xpath("//a[@class='time-entries' or @class='time-entries selected']");

    By subMenuTimesheetApprovalTab = By.xpath("//a[contains(@class,'timesheet-approvals')]");

    By subMenuProjectsSettingTab = By.xpath("//a[@class='settings' or @class='settings selected']");

    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnProjects() {
        /*this.headerProjectsTab.click();*/
        int attempts = 0;
        while(attempts < 3) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.headerProjectsTab));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                element.click();
                break;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
    }

    public void clickOnIssues() {
        /*this.subMenuIssuesTab.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.subMenuIssuesTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnSpentTime() {
        /*this.subMenuSpentTimeTab.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.subMenuSpentTimeTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnAdministrator() {
        /*this.headerAdministrationTab.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.headerAdministrationTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnLogout() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(this.linkLogout));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", logoutLink);
                logoutLink.click();
                break;
            }catch (StaleElementReferenceException e){
                attempts++;
            }
        }
    }

    public void clickOnProjectSetting() {
        /*this.subMenuProjectsSettingTab.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.subMenuProjectsSettingTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnTimesheet(){
        /*this.headerTimesheetTab.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.headerTimesheetTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnTimesheetApproval(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.subMenuTimesheetApprovalTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }


}
