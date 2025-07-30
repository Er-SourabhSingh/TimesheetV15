package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PluginsPage extends BasePage {

    /*@FindBy(xpath = "//tr[@id='plugin-redmineflux_timesheet']//td//a[normalize-space()='Configure']")
    WebElement lnkTimesheetConfigure;*/

    By lnkTimesheetConfigure = By.xpath("//tr[@id='plugin-redmineflux_timesheet']//td//a[normalize-space()='Configure']");

    public PluginsPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnConfigureOfTimesheetPlugin() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.lnkTimesheetConfigure));
        element.click();
    }
}
