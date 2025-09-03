package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ProjectFormPage extends BasePage {

    /*@FindBy(xpath = "//input[@id='project_name']")
    WebElement txtProjectName;

    @FindBy(xpath = "//select[@id='project_approval_schema_id']")
   WebElement dropdownTimesheetSchema;

    @FindBy(xpath = "//input[@name='commit']")
    WebElement btnCreate;

    @FindBy(xpath = "//form[contains(@id,'edit_project')]//input[@name='commit']")
    WebElement btnSaveProject;

    @FindBy(xpath = "//input[@name='continue']")
    WebElement btnCreateAndAddAnother;

    @FindBy(xpath = "//input[@id='project_enabled_module_names_issue_tracking']")
    WebElement checkBoxIssueTracking;

    @FindBy(xpath = "//input[@id='project_enabled_module_names_time_tracking']")
    WebElement checkBoxTimeTracking;

    @FindBy(xpath = "//input[@id='project_enabled_module_names_timesheet_approval']")
    WebElement checkBoxTimesheetApproval;

    @FindBy(xpath = "//a[@id='tab-members']")
    WebElement tabMember;

    @FindBy(xpath = "//a[normalize-space()='New member']")
    WebElement btnNewMember;

    @FindBy(xpath = "//input[@id='member-add-submit']")
    WebElement btnAddMember;*/

    By txtProjectName = By.xpath("//input[@id='project_name']");

    By dropdownTimesheetSchema = By.xpath("//select[@id='project_approval_schema_id']");

    By btnCreate = By.xpath("//input[@name='commit']");

    By btnSaveProject = By.xpath("//form[contains(@id,'edit_project')]//input[@name='commit']");

    By btnCreateAndAddAnother = By.xpath("//input[@name='continue']");

    By checkBoxIssueTracking = By.xpath("//input[@id='project_enabled_module_names_issue_tracking']");

    By checkBoxTimeTracking = By.xpath("//input[@id='project_enabled_module_names_time_tracking']");

    By checkBoxTimesheetApproval = By.xpath("//input[@id='project_enabled_module_names_timesheet_approval']");

    By tabMember = By.xpath("//a[@id='tab-members']");

    By btnNewMember = By.xpath("//a[normalize-space()='New member']");

    By btnAddMember = By.xpath("//input[@id='member-add-submit']");


    public ProjectFormPage(WebDriver driver) {
        super(driver);
    }


    public void setTxtProjectName(String projectName) {
        /*this.txtProjectName.sendKeys(projectName);*/
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtProjectName));
        element.sendKeys(projectName);
    }

    public void checkCheckBoxIssueTracking() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.checkBoxIssueTracking));
        if (!element.isSelected())
            element.click();
    }

    public void checkCheckBoxTimeTracking() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.checkBoxTimeTracking));
        if (!element.isSelected())
            element.click();
    }

    public void checkCheckBoxTimesheetApproval() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.checkBoxTimesheetApproval));
        if (!element.isSelected())
            element.click();
    }

    public void clickOnCreate() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnCreate));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnCreateAndAddAnother() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnCreateAndAddAnother));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnMemberTab() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.tabMember));
        element.click();
    }

    public void clickOnNewMemberBtn() {
        int attempts = 0;
        while(attempts < 3) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnNewMember));
                element.click();
                break;
            }catch (StaleElementReferenceException | NoSuchElementException e){
                attempts++;
                wait.until(ExpectedConditions.elementToBeClickable(this.btnNewMember));
            }
        }
    }

    public void selectRole(String roleName) {
        By roleLocator = By.xpath("//div[@class='roles-selection']//label[text()=' " + roleName + "']//input");
        WebElement input = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(roleLocator));
        input.click();
    }

    public void selectMember(String userName) {
        By memberLoctor = By.xpath("//div[@id='principals_for_new_member']//label[contains(text(),'" + userName + "')]//input");
        WebElement input = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(memberLoctor));
        input.click();
    }

    public void clickOnAddBtnOfMember() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.btnAddMember));
        element.click();
    }

    public List<String> getMembersList() {
        By emptyStateLocator = By.xpath("//p[@class='nodata']");

        try {
            List<String> texts = new ArrayList<>();
            By memberLoctor = By.xpath("//table[@class='list members']//td[1]//a");
            List<WebElement> members = new ArrayList<>();
            int attempts = 0;
            while (attempts < 3) {
                try {
                    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(memberLoctor));
                    members = driver.findElements(memberLoctor);
                    break;
                } catch (StaleElementReferenceException | NoSuchElementException e) {
                    attempts++;
                }
            }

            for (WebElement el : members) {
                String text = el.getText().trim();
                if (!text.isEmpty()) texts.add(text);
            }
            return texts;
        }catch (Exception exception) {
          return new ArrayList<>() ;
        }

    }

    public void clickOnProjectSaveBtn() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.btnSaveProject));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void selectTimesheetSchema(String schemaName){
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(this.dropdownTimesheetSchema));
        Select select = new Select(dropdown);
        select.selectByVisibleText(schemaName);
    }
}
