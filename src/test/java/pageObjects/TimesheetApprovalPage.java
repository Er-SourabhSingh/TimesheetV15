package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimesheetApprovalPage extends BasePage{
    By lnkPrevious = By.xpath("//a[@id='previous_link']");

    By lnkNext = By.xpath("//a[@id='next_link']");

    By txtSearch = By.xpath("//input[@id='searchUser']");

    By dropDownProject = By.xpath("//select[@id='tree-select-input-project']");

    By dateRange = By.xpath("//div[@class='links-left']//span");

    By noDataXpath = By.xpath("//p[@class='nodata']");

    By txtRejectionComment = By.xpath("//input[@id='rejection-comment']");

    By btnRejectionCancel = By.xpath("//button[@class='reject-modal-cancel']");

    By btnRejectionSubmit = By.xpath("//button[@id='reject-modal-submit']");

    By txtApprovalComment = By.xpath("//input[@id='approval-comment']");

    By btnApprovalCancel = By.xpath("//button[@class='approve-modal-cancel']");

    By btnApprovalSubmit = By.xpath("//button[@id='approve-modal-submit']");

    By checkBoxSelectAll = By.xpath("//input[@id='select_all_checkbox']");

    public TimesheetApprovalPage(WebDriver driver){
        super(driver);
    }


    public boolean isNoDataDisplay(){
        WebElement noDataMsd = driver.findElement(this.noDataXpath);
        return noDataMsd.isDisplayed() && noDataMsd.getText().equals("No data to display");
    }

    public void setTextSearch(String userName){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.txtSearch));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.clear();
        element.sendKeys(userName);
    }

    public void clickEnterBtn(){
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).perform();
    }

    public List<String> getAllUser(){
        List<String> users = new ArrayList<>();
        By usersXpath = By.xpath("//table[@class='list issues']//tbody//tr//td[3]");

        List<WebElement> userList = driver.findElements(usersXpath);

        for(WebElement user : userList){
            users.add(user.getText());
        }

        return users;
    }

    public void selectProject(String projectName){
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(this.dropDownProject));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);
        Select select = new Select(dropdown);
        select.selectByVisibleText(projectName);
    }

    public List<String> getProjectOptions(){
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(this.dropDownProject));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);
        Select select = new Select(dropdown);
        List<String> optionList = new ArrayList<>();
        List<WebElement> options = select.getOptions();

        for(WebElement option : options){
            optionList.add(option.getText().trim());
        }

        return optionList;
    }

    public boolean isProjectSelected(String projectName){
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(this.dropDownProject));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);
        Select select = new Select(dropdown);

        String selectedText = select.getFirstSelectedOption().getText().trim();

        return selectedText.equalsIgnoreCase(projectName);
    }


    public void selectAllUser(){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(this.checkBoxSelectAll));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(element.isEnabled() && !element.isSelected()){
            element.click();
        }
    }

    public void selectUserCheckBoxInTable(String userName){
        By checkBox = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'"+userName+"')]/ancestor::tr//td[2]//input");

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(checkBox));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if(element.isEnabled() && !element.isSelected()){
            element.click();
        }
    }


    public String getDesignHoursOfUser(String userName){
        //By designHours = By.xpath("//table[@class='list issues']//tbody//tr//td[3]//a[contains(text(),'" + userName + "')]/ancestor::tr//td[4]");
        By designHours = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'" + userName + "')]/ancestor::tr//td[4]");

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(designHours));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return element.getText();
    }

    public String getDevelopmentHoursOfUser(String userName){
        //By developmentHours = By.xpath("//table[@class='list issues']//tbody//tr//td[3]//a[contains(text(),'" + userName + "')]/ancestor::tr//td[5]");
        By developmentHours = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'" + userName + "')]/ancestor::tr//td[5]");
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(developmentHours));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return element.getText();
    }

    public String getTotalHoursOfUser(String userName){
        //By totalHours = By.xpath("//table[@class='list issues']//tbody//tr//td[3]//a[contains(text(),'" + userName + "')]/ancestor::tr//td[6]");
        By totalHours = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'" + userName + "')]/ancestor::tr//td[6]");
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(totalHours));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return element.getText();
    }

    public String getTimePeriodOfUser(String userName){
        //By timePeriod = By.xpath("//table[@class='list issues']//tbody//tr//td[3]//a[contains(text(),'" + userName + "')]/ancestor::tr//td[7]");
        By timePeriod = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'" + userName + "')]/ancestor::tr//td[7]");
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(timePeriod));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return element.getText();
    }

    public String getStatusValueOfUserTimesheet(String userName){
        //By statusValue = By.xpath("//table[@class='list issues']//tbody//tr//td[3]//a[contains(text(),'" + userName + "')]/ancestor::tr//td[8]//input[1]");
        By statusValue = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'" + userName + "')]/ancestor::tr//td[8]//input[1]");
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(statusValue));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return element.getAttribute("value");
    }

    public String getTextStatusOfUserTimesheet(String userName) {
        By textStatus = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'"
                + userName + "')]/ancestor::tr//td[8]");
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(textStatus));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return element.getText().trim();
    }


    public Boolean isApproveBtnEnabled(String userName){
        // By btnApprove = By.xpath("//table[@class='list issues']//tbody//tr//td[3]//a[contains(text(),'" + userName + "')]/ancestor::tr//td[8]//a[.='Approve']");
        By btnApprove = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'" + userName + "')]/ancestor::tr//td[8]//a[.='Approve']");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(btnApprove));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return element.isEnabled();
    }

    public Boolean isRejectBtnEnabled(String userName){
        //By btnReject = By.xpath("//table[@class='list issues']//tbody//tr//td[3]//a[contains(text(),'" + userName + "')]/ancestor::tr//td[8]//a[.='Reject']");
        By btnReject = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'" + userName + "')]/ancestor::tr//td[8]//a[.='Reject']");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(btnReject));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return element.isEnabled();
    }

    public void clickOnApproveBtn(String userName){
       // By btnApprove = By.xpath("//table[@class='list issues']//tbody//tr//td[3]//a[contains(text(),'" + userName + "')]/ancestor::tr//td[8]//a[.='Approve']");
        By btnApprove = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'" + userName + "')]/ancestor::tr//td[8]//a[.='Approve']");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(btnApprove));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnRejectBtn(String userName){
        //By btnReject = By.xpath("//table[@class='list issues']//tbody//tr//td[3]//a[contains(text(),'" + userName + "')]/ancestor::tr//td[8]//a[.='Reject']");
        By btnReject = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'" + userName + "')]/ancestor::tr//td[8]//a[.='Reject']");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(btnReject));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }

    public void clickOnShowHistoryOfUserTimesheet(String userName){
        //By lnkShow = By.xpath("//table[@class='list issues']//tbody//tr//td[3]//a[contains(text(),'" + userName + "')]/ancestor::tr//td[9]");
        By lnkShow = By.xpath("//table[@class='list issues']//tbody//tr//td[3][contains(text(),'" + userName + "')]/ancestor::tr//td[9]");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(lnkShow));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }


    public void setRejectionText(String comment){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(txtRejectionComment));
        element.sendKeys(comment);
    }

    public void clickOnCancelBtnOfRejection(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(btnRejectionCancel));
        element.click();
    }

    public void clickOnSubmitBtnOfRejection(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(btnRejectionSubmit));
        element.click();
        driver.navigate().refresh();// remove
    }

    public void setApprovalText(String comment){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(txtApprovalComment));
        element.sendKeys(comment);
    }

    public void clickOnCancelBtnOfApproval(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(btnApprovalCancel));
        element.click();
    }

    public void clickOnSubmitBtnOfApproval(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(btnApprovalSubmit));
        element.click();
    }

    public void clickOnPreviousLnk(){
        WebElement previousLnk = wait.until(ExpectedConditions.elementToBeClickable(this.lnkPrevious));
        previousLnk.click();
    }

    public void clickOnNextLnk(){
        WebElement nextLnk = wait.until(ExpectedConditions.elementToBeClickable(this.lnkNext));
        nextLnk.click();
    }

    public void navigateToTargetDateRange(String expectedStartDate, String expectedEndDate) throws InterruptedException{
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMM yy", Locale.ENGLISH);
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);

        LocalDate expectedStart = LocalDate.parse(expectedStartDate.trim(), inputFormatter);
        LocalDate expectedEnd = LocalDate.parse(expectedEndDate.trim(), inputFormatter);

        String expectedRange = expectedStart.format(fullFormatter)+" - "+expectedEnd.format(fullFormatter);

        while (true){
            String fullText = wait.until(ExpectedConditions.visibilityOfElementLocated(this.dateRange)).getText().trim();

            String cleanedText = fullText.replace("Previous","").replace("Next","").trim();

            if (cleanedText.equals(expectedRange)) {
                //System.out.println("Reached target range: " + cleanedText);
                break;
            }

            String currentStartStr = cleanedText.split(" - ")[0].trim();
            LocalDate currentStart = LocalDate.parse(currentStartStr,fullFormatter);

            if(currentStart.isAfter(expectedStart)){
                this.clickOnPreviousLnk();
            }
            else{
                this.clickOnNextLnk();
            }
            Thread.sleep(1000);
        }
    }

    public boolean verifyDateRange(String expectedStartDate, String expectedEnDate){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMM yy", Locale.ENGLISH);
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);

        LocalDate expectedStart = LocalDate.parse(expectedStartDate.trim(),inputFormatter);
        LocalDate expectedEnd = LocalDate.parse(expectedEnDate.trim(), inputFormatter);

        String expectedRange = expectedStart.format(fullFormatter) + " - " +expectedEnd.format(fullFormatter);

        String fullText = wait.until(ExpectedConditions.visibilityOfElementLocated(this.dateRange)).getText().trim();

        String cleanedText = fullText.replaceAll("\\s*Previous\\s*|\\s*Next\\s*", "").trim();

        return cleanedText.equals(expectedRange);
    }
}
