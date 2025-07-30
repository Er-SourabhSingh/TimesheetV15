package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HeaderPage extends BasePage {

    /*@FindBy(xpath = "//a[contains(@class,'timesheets')]")
    WebElement headerTimesheetTab;*/

    /*@FindBy(xpath = "//a[@class='administration']")
    WebElement headerAdministrationTab;*/

    /*@FindBy(xpath = "//div[@id='top-menu']//a[normalize-space()='Projects']")
    WebElement headerProjectsTab;*/

    /*@FindBy(xpath = "//a[@class='logout']")
    WebElement linkLogout;*/

    /*@FindBy(xpath = "//a[@class='issues' or @class='issues selected']")
    WebElement subMenuIssuesTab;*/

    /*@FindBy(xpath = "//a[@class='time-entries' or @class='time-entries selected']")
    WebElement subMenuSpentTimeTab;*/

    /*@FindBy(xpath = "//a[@class='settings' or @class='settings selected']")
    WebElement subMenuProjectsSettingTab;*/

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
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.headerProjectsTab));
        element.click();
    }

    public void clickOnIssues() {
        /*this.subMenuIssuesTab.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.subMenuIssuesTab));
        element.click();
    }

    public void clickOnSpentTime() {
        /*this.subMenuSpentTimeTab.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.subMenuSpentTimeTab));
        element.click();
    }

    public void clickOnAdministrator() {
        /*this.headerAdministrationTab.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.headerAdministrationTab));
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
        element.click();
    }

    public void clickOnTimesheet(){
        /*this.headerTimesheetTab.click();*/
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.headerTimesheetTab));
        element.click();
    }

    public void clickOnTimesheetApproval(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.subMenuTimesheetApprovalTab));
        element.click();
    }


}
