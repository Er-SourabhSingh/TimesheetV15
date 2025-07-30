package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TimesheetApprovalSchema extends BasePage {

    /*@FindBy(xpath = "//a[@class='icon icon-add new-approval-schema']")
    WebElement btnAddNewSchema;

     @FindBy(xpath = "//input[@id='approval_schema_name']")
    WebElement txtSchemaName;

    @FindBy(xpath = "//textarea[@id='approval_schema_description']")
    WebElement txtSchemaDescription;

    @FindBy(xpath = "//button[@id='btn_cancel_schema']")
    WebElement btnCancelSchema;

    @FindBy(xpath = "//button[@id='btn_create_approval_schema']")
    WebElement btnCreateSchema;

    @FindBy(xpath = "//a[@class='icon icon-add new-team']")
    WebElement btnAddApprovalLevel;

    @FindBy(xpath = "//input[@id='approval_level_name']")
    WebElement txtApprovalLevelName;

    @FindBy(xpath = "//select[@id='approval_level_role']")
    WebElement dropdownApprovalRole;

    @FindBy(xpath = "//select[@id='approval_level_level']")
    WebElement dropdownApprovalLevel;

    @FindBy(xpath = "//input[@id='auto_approval']")
    WebElement checkBoxAutoApproval;

    @FindBy(xpath = "//button[@id='btn_cancel_level']")
    WebElement btnApprovalLevelCancel;

    @FindBy(xpath = "//button[@id='btn_create_level']")
    WebElement btnApprovalLevelCreate;*/

    By btnAddNewSchema = By.xpath("//a[@class='icon icon-add new-approval-schema']");

    By txtSchehmaName = By.xpath("//input[@id='approval_schema_name']");

    By txtSchemaDescription = By.xpath("//textarea[@id='approval_schema_description']");

    By btnCancelSchema = By.xpath("//button[@id='btn_cancel_schema']");

    By btnCreateSchema = By.xpath("//button[@id='btn_create_approval_schema']");

    By btnAddApprovalLevel = By.xpath("//a[@class='icon icon-add new-team']");

    By txtApprovalLevelName = By.xpath("//input[@id='approval_level_name']");

    By dropdownApprovalRole = By.xpath("//select[@id='approval_level_role']");

    By dropdownApprovalLevel = By.xpath("//select[@id='approval_level_level']");

    By checkBoxAutoApproval = By.xpath("//input[@id='auto_approval']");

    By btnApprovalLevelCancel = By.xpath("//button[@id='btn_cancel_level']");

    By btnApprovalLevelCreate = By.xpath("//button[@id='btn_create_level']");

    public TimesheetApprovalSchema(WebDriver driver){
        super(driver);
   }

    public void clickOnAddNewSchemaBTn(){
        /*this.btnAddNewSchema.click();*/
        wait.until(ExpectedConditions.elementToBeClickable(this.btnAddNewSchema)).click();
    }

    public void setSchemaName(String name){
        /*this.txtSchemaName.clear();
        this.txtSchemaName.sendKeys(name);*/

        wait.until(ExpectedConditions.presenceOfElementLocated(txtSchehmaName)).clear();
        wait.until(ExpectedConditions.presenceOfElementLocated(txtSchehmaName)).sendKeys(name);
    }

    public void setSchemaDescription(String description){
        /*this.txtSchemaDescription.clear();
        this.txtSchemaDescription.sendKeys(description);*/

        wait.until(ExpectedConditions.presenceOfElementLocated(this.txtSchemaDescription)).clear();
        wait.until(ExpectedConditions.presenceOfElementLocated(this.txtSchemaDescription)).sendKeys(description);
    }

    public void clickOnCancelSchema(){
        /*this.btnCancelSchema.click();*/
        wait.until(ExpectedConditions.elementToBeClickable(this.btnCancelSchema)).click();
    }

    public void clickOnCreateSchema(){
       /* this.btnCreateSchema.click();*/
        wait.until(ExpectedConditions.elementToBeClickable(this.btnCreateSchema)).click();
    }

    public void clickOnSchemaToOpen(String schemaName){
        By schemaXpath = By.xpath("//table[@class='approval-schemas-table list']//tbody//td[2]//a[text()='" + schemaName + "']");
        int attempts = 0;

        while (attempts < 3) {
            try {
                // Re-fetch fresh element inside loop
                WebElement locator = wait.until(ExpectedConditions.elementToBeClickable(schemaXpath));
                locator.click();
                break;
            } catch (StaleElementReferenceException | NoSuchElementException | ElementClickInterceptedException e) {
                attempts++;
                try {
                    Thread.sleep(200); // slight wait between retries
                } catch (InterruptedException ignored) {}
            } catch (WebDriverException e) {
                // Fallback for weird inspector errors
                if (e.getMessage().contains("Node with given id does not belong to the document")) {
                    attempts++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ignored) {}
                } else {
                    throw e;
                }
            }
        }
    }

    public void clickOnEditIconOfSchema(String schemaName){
        WebElement edit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@class='approval-schemas-table list']//tbody//td[2]//a[text()='"+schemaName+"']/ancestor::tr//td[4]//a[@title='Edit']")));
        edit.click();
    }

    public void clickOnDeleteIconOfSchema(String schemaName){
        WebElement delete = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@class='approval-schemas-table list']//tbody//td[2]//a[text()='"+schemaName+"']/ancestor::tr//td[4]//a[@title='Delete']")));
        delete.click();
    }

    public void acceptSchemaDeletion(){
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    public void dissmissSchemaDeletion(){
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }

    public void clickOnAddApprovalLevelBtn(){
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnAddApprovalLevel));
                element.click();
                break;
            }catch (StaleElementReferenceException | ElementClickInterceptedException | NoSuchElementException e){
                attempts++;
                wait.until(ExpectedConditions.presenceOfElementLocated(this.btnAddApprovalLevel));
            }catch (WebDriverException e) {
                if (e.getMessage().contains("Node with given id does not belong to the document")) {
                    attempts++;
                    wait.until(ExpectedConditions.presenceOfElementLocated(this.btnAddApprovalLevel));
                } else {
                    throw e; // some other WebDriver error
                }
            }
        }
    }

    public void setApprovalLevelName(String name){
        /*this.txtApprovalLevelName.sendKeys(name);*/
        wait.until(ExpectedConditions.presenceOfElementLocated(this.txtApprovalLevelName)).clear();
        wait.until(ExpectedConditions.presenceOfElementLocated(this.txtApprovalLevelName)).sendKeys(name);

    }

    public void selectApprovalRole(String role){
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(this.dropdownApprovalRole));
        Select select = new Select(dropdown);
        select.selectByVisibleText(role);
    }

    public void selectApprovalLevel(String level){
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(this.dropdownApprovalLevel));
        Select select = new Select(dropdown);
        select.selectByValue(level);
    }

    public void checkCheckboxAutoApproval(){
        if(!driver.findElement(this.checkBoxAutoApproval).isSelected())
            driver.findElement(this.checkBoxAutoApproval).click();
    }

    public void uncheckCheckboxAutoApproval(){
        if(driver.findElement(this.checkBoxAutoApproval).isSelected())
            driver.findElement(this.checkBoxAutoApproval).click();
    }

    public void clickOnApprovalLevelCancel(){
        /*this.btnApprovalLevelCancel.click();*/
        wait.until(ExpectedConditions.elementToBeClickable(this.btnApprovalLevelCancel)).click();
    }

    public void clickOnApprovalLevelCreate(){
        /*this.btnApprovalLevelCreate.click();*/
        wait.until(ExpectedConditions.elementToBeClickable(this.btnApprovalLevelCreate)).click();
    }

    public void clickOnEditIconOfApprovalLevel(String level){
        WebElement edit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@class='list']//tbody//td[3][text()='"+level+"']/ancestor::tr//td//a[@class='icon-only icon-edit']")));
        edit.click();
    }

    public void clickOnDeleteIconOfApprovalLevel(String level){
        WebElement delete = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@class='list']//tbody//td[3][text()='"+level+"']/ancestor::tr//td//a[@class='icon-only icon-del']")));
        delete.click();
    }

    public void confirmLevelDeletion(){
        WebElement delete = driver.findElement(By.xpath("//input[@id='btn_delete_level']"));
        delete.click();
    }

    public void dissmissLevelDeletion(){
        WebElement cancel = driver.findElement(By.xpath("//button[@id='btn_cancel_level']"));
        cancel.click();
    }

    public List<String> getSchemaList() {
        By schemaLocator = By.xpath("//table[@class='approval-schemas-table list']//tbody//td[2]//a");
        List<String> schemaTexts = new ArrayList<>();

        // Wait for elements to be present initially
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(schemaLocator));
        int size = driver.findElements(schemaLocator).size();

        for (int i = 0; i < size; i++) {
            int attempts = 0;
            while (attempts < 3) {
                try {
                    // Re-fetch the list to get a fresh reference every time
                    List<WebElement> elements = driver.findElements(schemaLocator);
                    WebElement element = elements.get(i);

                    // Trigger interaction to check validity (optional but helps)
                    if (element.isDisplayed()) {
                        schemaTexts.add(element.getText().trim());
                        break;
                    }
                } catch (StaleElementReferenceException e) {
                    attempts++;
                    // Small wait before retrying
                    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(schemaLocator));
                }
            }
        }
        return schemaTexts;
    }

    public List<String> getListOfLevels() {
        By levelLocator = By.xpath("//table[@class='list']//tbody//td[3]");
        List<String> levelTexts = new ArrayList<>();

        if (!driver.findElements(By.xpath("//p[@class='nodata']")).isEmpty()) {
            return levelTexts; // No data
        }

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(levelLocator));
        int size = driver.findElements(levelLocator).size();

        for (int i = 0; i < size; i++) {
            int attempts = 0;
            while (attempts < 3) {
                try {
                    List<WebElement> elements = driver.findElements(levelLocator);
                    WebElement element = elements.get(i);

                    if (element.isDisplayed()) {
                        levelTexts.add(element.getText().trim());
                        break;
                    }
                } catch (StaleElementReferenceException e) {
                    attempts++;
                    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(levelLocator));
                }
            }
        }

        return levelTexts;
    }


}
