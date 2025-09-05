package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

}
