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



    public List<String> getLastUpdatedHistoryByUser(String name) {
        List<String> listHistory = new ArrayList<>();
        try {
            List<WebElement> userBlocks = driver.findElements(
                    By.xpath("//div[@class='changelog_list']//h4//a[text()='" + name + "']/ancestor::div[@class='changelog_list']")
            );

            if (!userBlocks.isEmpty()) {
                WebElement lastBlock = userBlocks.get(userBlocks.size() - 1);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", lastBlock);

                List<WebElement> dataList = lastBlock.findElements(By.tagName("li"));
                for (WebElement data : dataList) {
                    listHistory.add(data.getText().trim());
                }
            }
        } catch (StaleElementReferenceException e) {
            throw new StaleElementReferenceException("Stale");

        }
        return listHistory;
    }



}
