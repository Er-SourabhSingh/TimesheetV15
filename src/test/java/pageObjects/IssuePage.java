package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class IssuePage extends BasePage {

    /*@FindBy(xpath = "//select[@id='add_filter_select']")
    WebElement dropdownAddFiler;

    @FindBy(xpath = "//legend[normalize-space()='Options']")
    WebElement expandCollapsedOptions;

    @FindBy(xpath = "//select[@id='available_c']")
    WebElement availableColumn;

    @FindBy(xpath = "//input[@value='→']")
    WebElement moveBtnToSelectedCol;

    @FindBy(xpath = "//a[normalize-space()='Apply']")
    WebElement btnApply;

    @FindBy(xpath = "//a[@class='icon icon-reload']")
    WebElement btnClear;

    @FindBy(xpath = "//a[@class='icon icon-add new-issue']")
    WebElement btnAddNewIssue;

    @FindBy(xpath = "//tr//td[@class='subject']//a")
    List<WebElement> subjectColumn;*/


    By dropdownAddFiler = By.xpath("//select[@id='add_filter_select']");

    By expandCollapsedOptions = By.xpath("//legend[normalize-space()='Options']");

    By availableColumn = By.xpath("//select[@id='available_c']");

    By moveBtnToSelectedCol = By.xpath("//input[@value='→']");

    By btnApply = By.xpath("//a[normalize-space()='Apply']");

    By btnClear = By.xpath("//a[@class='icon icon-reload']");

    By btnAddNewIssue = By.xpath("//a[@class='icon icon-add new-issue']");

    By subjectColumn = By.xpath("//tr//td[@class='subject']//a");

    public IssuePage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickOnNewIssueBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnAddNewIssue));
        element.click();
    }

    public void selectFilter(String filterName) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(this.dropdownAddFiler));
        Select select = new Select(dropdown);
        select.selectByVisibleText(filterName);
    }

    public void selectOperatorAndAuthor(String operator, String authorName) {
        // Select the operator (either "is" or "is not")
        WebElement operatorDropdown = driver.findElement(By.id("operators_author_id"));
        Select operatorSelect = new Select(operatorDropdown);

        // Determine the operator value and select it
        if (operator.equalsIgnoreCase("is")) {
            operatorSelect.selectByValue("="); // "is"
        } else if (operator.equalsIgnoreCase("is not")) {
            operatorSelect.selectByValue("!"); // "is not"
        } else {
            System.out.println("Invalid operator provided.");
            return;
        }

        // Select the author name from the value dropdown
        WebElement authorDropdown = driver.findElement(By.id("values_author_id_1"));
        Select authorSelect = new Select(authorDropdown);
        if (authorName.equalsIgnoreCase("me")) {
            authorSelect.selectByValue("me"); // Select << me >> option
        } else {
            // Select other author names from the list
            authorSelect.selectByVisibleText(authorName); // Select the given author name
        }
    }

    public void clickOnExpandCollapsedOption() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.expandCollapsedOptions));
        element.click();
    }


    public void clickApply() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnApply));
        element.click();
    }

    public void clickClear() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnClear));
        element.click();
    }

    public List<String> getSubjectNamesOfIssuesList(){
            By projectLocator = By.xpath("//table[contains(@class,'list issues')]//tbody//td[6]//a");
            List<String> projectNames = new ArrayList<>();

            if (!driver.findElements(By.xpath("//p[@class='nodata']")).isEmpty()) {
                return projectNames; // No data
            }

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(projectLocator));
            int size = driver.findElements(projectLocator).size();

            for (int i = 0; i < size; i++) {
                int attempts = 0;
                while (attempts < 3) {
                    try {
                        List<WebElement> elements = driver.findElements(projectLocator);
                        WebElement element = elements.get(i);

                        if (element.isDisplayed()) {
                            projectNames.add(element.getText().trim());
                            break;
                        }
                    } catch (StaleElementReferenceException e) {
                        attempts++;
                        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(projectLocator));
                    }
                }
            }
            return projectNames;
    }
}
