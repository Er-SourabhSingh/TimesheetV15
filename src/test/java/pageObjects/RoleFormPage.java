package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class RoleFormPage extends BasePage {

    /*@FindBy(xpath = "//input[@id='role_name']")
    WebElement txtRoleName;

    @FindBy(xpath = "//select[@id='copy_workflow_from']")
    WebElement dropDownCopyWorkflow;

    @FindBy(xpath = "//fieldset[@id='module_project']//a")
    WebElement rightBtnProject;

    @FindBy(xpath = "//fieldset[@id='module_issue_tracking']//a")
    WebElement rightBtnIssueTracking;

    @FindBy(xpath = "//fieldset[@id='module_timesheet_approval']//a")
    WebElement rightBtnTimesheetApproval;

    @FindBy(xpath = "//fieldset[@id='module_time_tracking']//a")
    WebElement rightBtnTimeTracking;

    @FindBy(xpath = "//b[normalize-space()='All trackers']/preceding-sibling::a")
    WebElement rightBtnTracker;

    @FindBy(xpath = "//input[@name='commit']")
    WebElement btnCreate;

    @FindBy(xpath = "//input[@id='role_permissions_manage_timesheet']")
    WebElement checkboxManageTimesheet;

    @FindBy(xpath = "//input[@id='role_permissions_approve_timesheet']")
    WebElement checkboxApprovalTimesheet;*/

    By txtRoleName = By.xpath("//input[@id='role_name']");

    By dropDownCopyWorkflow = By.xpath("//select[@id='copy_workflow_from']");

    By rightBtnProject = By.xpath("//fieldset[@id='module_project']//a");

    By rightBtnIssueTracking = By.xpath("//fieldset[@id='module_issue_tracking']//a");

    By rightBtnTimesheetApproval = By.xpath("//fieldset[@id='module_timesheet_approval']//a");

    By rightBtnTimeTracking = By.xpath("//fieldset[@id='module_time_tracking']//a");

    By rightBtnTracker = By.xpath("//b[normalize-space()='All trackers']/preceding-sibling::a");

    By btnCreate = By.xpath("//input[@name='commit']");

    By checkboxManageTimesheet = By.xpath("//input[@id='role_permissions_manage_timesheet']");

    By checkboxApprovalTimesheet = By.xpath("//input[@id='role_permissions_approve_timesheet']");

    public RoleFormPage(WebDriver driver) {
        super(driver);
    }

    public void setTxtRoleName(String roleName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtRoleName));
        element.sendKeys(roleName);
    }

    public void selectWorkflow() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.dropDownCopyWorkflow));
        Select select = new Select(element);
        select.selectByValue("3");
    }

    public void clickOnRightBtnOfProjectField() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.rightBtnProject));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnRightBtnTimesheetApproval() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.rightBtnTimesheetApproval));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void checkCheckboxOfTimesheetApproval() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxApprovalTimesheet));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if (!element.isSelected()){
            element.click();
        }
    }

    public void uncheckCheckboxOfTimesheetApproval() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxApprovalTimesheet));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if (element.isSelected()){
            element.click();
        }
    }

    public void uncheckManageTimesheet() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxManageTimesheet));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if (element.isSelected())
            element.click();
    }

    public void clickOnRightBtnOfIssueTrackingField() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.rightBtnIssueTracking));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//fieldset[@id='module_issue_tracking']")));
        element.click();
    }

    public void clickOnRightBtnOfTimeTrackingField() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.rightBtnTimeTracking));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//fieldset[@id='module_time_tracking']")));
        element.click();
    }

    public void clickOnRightBtnTracker() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.rightBtnTracker));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnCreateBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnCreate));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }
}
