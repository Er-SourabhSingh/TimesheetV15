package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class IssueFormPage extends BasePage {
    /*@FindBy(xpath = "//select[@id='issue_tracker_id']")
    WebElement dropdownTracker;

    @FindBy(xpath = "//input[@id='issue_subject']")
    WebElement txtSubject;

    @FindBy(xpath = "//input[@name='commit']")
    WebElement btnCreate;*/

    By dropDownProject = By.xpath("//select[@id='issue_project_id']");

    By dropdownTracker = By.xpath("//select[@id='issue_tracker_id']");

    By txtSubject = By.xpath("//input[@id='issue_subject']");

    By btnCreate = By.xpath("//input[@name='commit']");

    By btnCreateAndAddAnother = By.xpath("//input[@name='continue']");


    public IssueFormPage(WebDriver driver) {
        super(driver);
    }

    public void selectProject(String projectName){
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(this.dropDownProject));
        Select select = new Select(dropdown);
        select.selectByVisibleText(projectName);
    }

    public void selectTracker(String trackerName) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(this.dropdownTracker));
        Select select = new Select(dropdown);
        select.selectByVisibleText(trackerName);
    }

    public void setTxtSubject(String subject) {
        int retries = 0;
        while (retries < 3) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.txtSubject));

                // Scroll into view if needed
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

                // Optional: Ensure it's not readonly or disabled
                if (element.isEnabled() && element.isDisplayed()) {
                    element.clear(); // In case of pre-filled template
                    element.sendKeys(subject);
                    break;
                } else {
                    throw new ElementNotInteractableException("Subject field is not enabled or visible");
                }

            } catch (StaleElementReferenceException | ElementNotInteractableException e) {
                retries++;
            }
        }

    }

    public void clickOnCreateBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnCreate));
        element.click();
    }

    public void clickOnCreateAndAddAnother(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnCreateAndAddAnother));
        element.click();
    }

}
