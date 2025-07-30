package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class UsersPage extends BasePage {
    /*@FindBy(xpath = "//a[@class='icon icon-add']")
    WebElement btnNewUser;

    @FindBy(xpath = "//input[@id='name']")
    WebElement searchUser;

    @FindBy(xpath = "//input[@value='Apply']")
    WebElement btnApply;

    @FindBy(xpath = "//a[@class='icon icon-reload']")
    WebElement btnClear;

    @FindBy(xpath = "//span[@class='items']")
    WebElement userPagination;*/

    By btnNewUser = By.xpath("//a[@class='icon icon-add']");

    By searchUser = By.xpath("//input[@id='name']");

    By btnApply = By.xpath("/input[@value='Apply']");

    By btnClear = By.xpath("//a[@class='icon icon-reload']");

    By userPagination = By.xpath("//span[@class='items']");


    public UsersPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnAddUserBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnNewUser));
        element.click();
    }

    public void setSearchUser(String userName) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.searchUser));
        element.sendKeys(userName);
    }

    public void clickOnApplyBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnApply));
        element.click();
    }

    public void clickOnClearBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnClear));
        element.click();
    }


    public List<String> getUserList() {
        List<String> users = new ArrayList<>();
        while (true) {
            List<WebElement> listUsers = driver.findElements(
                    By.xpath("//table[@class='list users']//tr[@class='user active']//td[@class='username']//a"));
            String pagination = driver.findElement(this.userPagination).getText();
            pagination = pagination.replace("(","").replace(")","");
            String []parts = pagination.split("/");
            String range = parts[0];
            String total = parts[1];

            String []rangeParts = range.split("-");

            int end = Integer.parseInt(rangeParts[1].trim());
            int totalItems = Integer.parseInt(total.trim());

            for (WebElement u : listUsers) {
                users.add(u.getText());
            }
            if(end>=totalItems)
                break;

            driver.findElement(By.partialLinkText("Next")).click();
        }
        return users;
    }
}
