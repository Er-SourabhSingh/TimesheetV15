package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class RoleFormPage extends BasePage {

    By txtRoleName = By.xpath("//input[@id='role_name']");

    By dropDownCopyWorkflow = By.xpath("//select[@id='copy_workflow_from']");

    By rightBtnProject = By.xpath("//fieldset[@id='module_project']//a");

    By checkBoxViewTimesheet = By.xpath("//input[@id='role_permissions_view_timesheet']");

    By rightBtnIssueTracking = By.xpath("//fieldset[@id='module_issue_tracking']//a");

    By checkboxViewIssue = By.xpath("//input[@id='role_permissions_view_issues']");

    By checkboxAddIssue = By.xpath("//input[@id='role_permissions_add_issues']");

    By checkboxEditIssue = By.xpath("//input[@id='role_permissions_edit_issues']");

    By rightBtnTimesheetApproval = By.xpath("//fieldset[@id='module_timesheet_approval']//a");

    By rightBtnTimeTracking = By.xpath("//fieldset[@id='module_time_tracking']//a");

    By checkboxViewSpentTime = By.xpath("//input[@id='role_permissions_view_time_entries']");

    By checkboxLogSpentTime = By.xpath("//input[@id='role_permissions_log_time']");

    By checkboxEditSpentTime = By.xpath("//input[@id='role_permissions_edit_time_entries']");

    By rightBtnTracker = By.xpath("//b[normalize-space()='All trackers']/preceding-sibling::a");

    By btnCreate = By.xpath("//input[@name='commit']");

    By checkboxManageTimesheet = By.xpath("//input[@id='role_permissions_manage_timesheet']");

    By checkboxApprovalTimesheet = By.xpath("//input[@id='role_permissions_approve_timesheet']");

    By checkboxViewIssuesAllTracker = By.xpath("//input[@id='role_permissions_all_trackers_view_issues']");

    By checkboxAddIssuesAllTracker = By.xpath("//input[@id='role_permissions_all_trackers_add_issues']");

    By checkboxEditIssuesAllTracker = By.xpath("//input[@id='role_permissions_all_trackers_edit_issues']");

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

    public void checkViewTimesheetCheckBox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkBoxViewTimesheet));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected()) {
            element.click();
        }
    }

    public void uncheckViewTimesheetCheckBox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkBoxViewTimesheet));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(element.isSelected()) {
            element.click();
        }
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

    public void uncheckTimesheetApprovalCheckbox() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxApprovalTimesheet));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if (element.isSelected()){
            element.click();
        }
    }

    public void checkManageTimesheetCheckbox() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxManageTimesheet));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if (!element.isSelected())
            element.click();
    }

    public void uncheckManageTimesheetCheckbox() {
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

    public void checkViewIssueCheckbox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxViewIssue));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected()) {
            element.click();
        }
    }

    public void checkAddIssueCheckbox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxAddIssue));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected()) {
            element.click();
        }
    }

    public void checkEditIssueCheckbox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxEditIssue));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected()) {
            element.click();
        }
    }

    public void clickOnRightBtnOfTimeTrackingField() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.rightBtnTimeTracking));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//fieldset[@id='module_time_tracking']")));
        element.click();
    }

    public void checkViewSpentTimeCheckbox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxViewSpentTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected()) {
            element.click();
        }
    }

    public void checkLogSpentTimeCheckbox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxLogSpentTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected()) {
            element.click();
        }
    }

    public void checkEditSpentTimeCheckbox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxEditSpentTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected()) {
            element.click();
        }
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

    public void checkViewIssuesAllTrackerCheckBox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxViewIssuesAllTracker));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected()) {
            element.click();
        }
    }

    public void checkAddIssuesAllTrackerCheckBox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxAddIssuesAllTracker));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected()) {
            element.click();
        }
    }

    public void checkEditIssuesAllTrackerCheckBox(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.checkboxEditIssuesAllTracker));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(!element.isSelected()) {
            element.click();
        }
    }
}
