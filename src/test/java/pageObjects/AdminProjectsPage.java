package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AdminProjectsPage extends BasePage {

    /*@FindBy(xpath = "//input[@id='name']")
    WebElement txtSearchProject;

    @FindBy(xpath = "//input[@value='Apply']")
    WebElement btnApply;

    @FindBy(xpath = "//input[@id='confirm']")
    WebElement txtConfirmField;

    @FindBy(xpath = "//input[@name='commit']")
    WebElement btnConfirmDelete;*/

    By txtSearchProject = By.xpath("//input[@id='name']");

    By btnApply = By.xpath("//input[@value='Apply']");

    By txtConfirmField = By.xpath("//input[@id='confirm']");

    By btnConfirmDelete = By.xpath("//input[@name='commit']");

    By warningTxt = By.xpath("//div[@class='warning']//p[3]");

    public AdminProjectsPage(WebDriver driver) {
        super(driver);
    }

    private By getDeleteBtnXpath(String pName) {
        return By.xpath("//tbody//tr//td//span[contains(.,'" + pName + "')]/ancestor::tr//td[4]//a[text()='Delete']");
    }

    public void clickOnDeleteIconOnProjectList(String pName) {
        this.searchProjectByName(pName);
        this.clickOnApply();
        WebElement btnDelete = wait.until(ExpectedConditions.elementToBeClickable(getDeleteBtnXpath(pName)));
        btnDelete.click();
    }

    private void searchProjectByName(String pName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtSearchProject));
        element.sendKeys(pName);
    }

    private void clickOnApply() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnApply));
        element.click();
    }


    private void setTxtConfirmField() {
        WebElement warning = wait.until(ExpectedConditions.visibilityOfElementLocated(this.warningTxt));
        String text = warning.getText();
        String identifier = text.substring(text.indexOf("(") + 1, text.indexOf(")"));
        WebElement filedElement = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtConfirmField));
        filedElement.sendKeys(identifier);
    }

    public void clickOnConfirmDeleteBTn() {
        this.setTxtConfirmField();
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnConfirmDelete));
        element.click();
    }
}
