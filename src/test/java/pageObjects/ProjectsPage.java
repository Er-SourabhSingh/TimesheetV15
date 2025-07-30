package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class ProjectsPage extends BasePage {
    /*@FindBy(xpath = "//a[@class='icon icon-add']")
    WebElement btnAddProject;

    @FindBy(xpath = "//select[@id='add_filter_select']")
    WebElement dropdownAddFilter;

    @FindBy(xpath = "//legend[normalize-space()='Options']")
    WebElement expandCollapsedOption;

    @FindBy(xpath = "//input[@id='display_type_list']")
    WebElement radioBtnList;

    @FindBy(xpath = "//select[@id='available_c']")
    WebElement availableColumn;

    @FindBy(xpath = "//input[@value='→']")
    WebElement moveBtnToSelectedCol;

    @FindBy(xpath = "//a[@class='icon icon-checked']")
    WebElement btnApply;

    @FindBy(xpath = "//a[@class='icon icon-reload']")
    WebElement btnClear;

    @FindBy(xpath = "//select[@id='values_id_1']")
    WebElement dropdownProject;*/



    By btnAddProject = By.xpath("//a[@class='icon icon-add']");

    By dropdownAddFilter = By.xpath("//select[@id='add_filter_select']");

    By expandCollapsedOption = By.xpath("//legend[normalize-space()='Options']");

    By radioBtnList = By.xpath("//input[@id='display_type_list']");

    By availableColumn  = By.xpath("//select[@id='available_c']");

    By moveBtnToSelectedCol = By.xpath("//input[@value='→']");

    By btnApply = By.xpath("//a[@class='icon icon-checked']");

    By btnClear = By.xpath("//a[@class='icon icon-reload']");

    By dropdownProject = By.xpath("//select[@id='values_id_1']");

    public ProjectsPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnAddProjectBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnAddProject));
        element.click();
    }

    public void selectFilter(String filterName) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(this.dropdownAddFilter));
            Select select = new Select(dropdown);
            boolean isAlreadySelected = select.getFirstSelectedOption().getText().equals(filterName);

            if (!isAlreadySelected) {
                select.selectByVisibleText(filterName);
            }
        } catch (Exception e) {
            return;
        }
    }

    public void clickOnExpandCollapsedOption() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.expandCollapsedOption));
        element.click();
    }

    public void clickOnListRadioBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.radioBtnList));
        element.click();
    }

    public void selectColumnFromAvailableColumn(String column) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(this.availableColumn));
        Select option = new Select(dropdown);
        option.selectByVisibleText(column);
    }

    public void clickOnAddColumnBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.moveBtnToSelectedCol));
        element.click();
    }

    public void clickOnApplyBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnApply));
        element.click();
    }

    public void clickOnClear() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnClear));
        element.click();
    }

    public List<String> getProjectsName() {
        By emptyStateLocator = By.xpath("//p[@class='nodata']");
        List<WebElement> projects = new ArrayList<>();
        if(!driver.findElements(emptyStateLocator).isEmpty()){
            return new ArrayList<>();
        }
        By projectsLocator = By.xpath("//div//li//a[@class='project root leaf public']");

        int attempts = 0;
        while (attempts < 3){
            try{
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(projectsLocator));
                projects = driver.findElements(projectsLocator);
                break;
            }catch (StaleElementReferenceException | NoSuchElementException e){
                attempts++;
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(projectsLocator));
            }
        }
        List<String> texts = new ArrayList<>();
        for (WebElement el: projects){
            String text = el.getText().trim();
            if(!text.isEmpty())texts.add(text);
        }
        return texts;

    }


    public void selectProject(String projectName) throws Exception {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(this.dropdownProject));
        Select select = new Select(dropdown);
        select.selectByVisibleText(projectName);
    }

    public void clickOnProjectName(String projectName) {
        WebElement projectLnk = wait.until(ExpectedConditions.elementToBeClickable( By.xpath("//a[contains(text(),'" + projectName + "')]")));
        projectLnk.click();
    }

}
