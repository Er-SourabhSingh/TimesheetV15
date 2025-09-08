package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class HistoryApprovalPage extends BasePage{

    By xpathOfHistoryLists = By.xpath("(//div[@class='timesheet_details']//div[contains(@class,'changelog_list')])[1]//li");

    public HistoryApprovalPage(WebDriver driver){
        super(driver);
    }

    public List<String> getLastUpdatedHistory(){
        List<String> listHistory = new ArrayList<>();
        try {
            //List<WebElement> dataList = driver.findElements(this.xpathOfHistoryLists);
            List<WebElement> dataList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(this.xpathOfHistoryLists));
            for (WebElement data : dataList) {
                listHistory.add(data.getText().trim());
            }
        } catch (StaleElementReferenceException e) {
            return new ArrayList<>();
        }

        return listHistory;
    }


    public List<String> getUpdatedHistoryByApproverUser(String name){
        List<String> listHistory = new ArrayList<>();
        try {
            By xpathOfUpdatedBy = By.xpath("//div[@class='changelog_list'][1]//h4//a[text()='"+name+"']/ancestor::div[@class='changelog_list']//li");
            List<WebElement> dataList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(xpathOfUpdatedBy));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//div[@class='changelog_list'][1]//h4//a[text()='"+name+"']/ancestor::div[@class='changelog_list']")));
            for (WebElement data : dataList) {
                listHistory.add(data.getText().trim());
            }
        } catch (StaleElementReferenceException e) {
            return new ArrayList<>();
        }

        return listHistory;
    }
}
