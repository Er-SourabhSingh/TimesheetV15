package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class TimesheetPage extends BasePage {

    By tabTimesheetDashboard = By.xpath("//a[@id='timesheet_main_dashboard_icon']");

    By optionExpandCollapse = By.xpath("//select[@id='detail']");

    By filterDateRange = By.xpath("//div[@id='reportrange']");

    By tabTimesheetApprovalDashboard = By.xpath("//a[@id='timesheet_report_icon']");

    By tabTimesheetApprovalSchema = By.xpath("//a[@id='approval_level_dashboard']");

    By btnDateFilterApply = By.xpath("//button[contains(text(),'Apply')]");

    By dateRangeAndFilter = By.xpath("//div[@class='daterange']//span");

    By btnLogTime = By.xpath("//span[@class='button_issue']");

    By btnSubmitTimesheet = By.xpath("//button[contains(@class,'submit-timesheet')]");

    By projectDropdownOfLogTime = By.id("project-drop");

    By issueDropdownOfLogTime = By.id("i-div");

    By activityDropdownOfLogTime = By.id("select_activity");

    By dateOfLogTime = By.id("log_date");

    By spentTimeOfLogTime = By.id("spent_hour");

    By commentOfLogTime = By.xpath("//textarea[@placeholder=' Comment']");

    By btnLogTimeOfLogTime = By.xpath("//div[@class='modal']//div//button[@class='button-plan'][normalize-space()='Log time']");

    By btnCancelOfLogTime = By.xpath("//button[@class='button-cross']");

    public TimesheetPage(WebDriver driver) {
        super(driver);
        //wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickOnSubmitTimesheetBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(this.btnSubmitTimesheet)).click();
    }

    public boolean isSubmitTimesheetBtnEnabled() {
        return driver.findElement(this.btnSubmitTimesheet).isEnabled();
    }

    public String getSubmitTimesheetBtnText() {
        return driver.findElement(this.btnSubmitTimesheet).getText().trim();
    }

    public void clickOnTimesheetDashboard() {
        /*this.tabTimesheetDashboard.click();*/
        wait.until(ExpectedConditions.elementToBeClickable(this.tabTimesheetDashboard)).click();
    }

    public void clickOnTimesheetApprovalDashboard() {
        /*this.tabTimesheetApprovalDashboard.click();*/
        wait.until(ExpectedConditions.elementToBeClickable(this.tabTimesheetApprovalDashboard)).click();
    }

    public void clickOnTimesheetApprovalSchema() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.tabTimesheetApprovalSchema));
                element.click();
                break;
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                attempts++;
                System.out.println("Attempt " + attempts + " failed: " + e.getMessage());
            }
        }
    }

    public void clickOnFilterDateRange() {
        /*this.filterDateRange.click();*/
        wait.until(ExpectedConditions.elementToBeClickable(this.filterDateRange)).click();
    }

    public String[] getSelectedDateRange() {
        WebElement dateRangeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(this.dateRangeAndFilter));
        String dateRangeText = dateRangeElement.getText().trim();
        String[] dateParts = dateRangeText.split("-");
        return dateParts;
    }

    public void selectDateRangeOption(String optionText) {
        WebElement rangeOption = this.driver.findElement(By.xpath("//li[text()='" + optionText + "']"));
        rangeOption.click();
    }

    public void selectStartDate(String start) {
        selectMonthAndYear(start);
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate targetDate = LocalDate.parse(start, inputFormat);
        String day = String.valueOf(targetDate.getDayOfMonth());
        WebElement date = driver.findElement(By.xpath("//div[@class='drp-calendar right']//td[contains(@class,'available') and not(contains(@class,'ends')) and text()='" + day + "']"));
        date.click();
    }

    public void selectEndDate(String end) {
        selectMonthAndYear(end);
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate targetDate = LocalDate.parse(end, inputFormat);
        String day = String.valueOf(targetDate.getDayOfMonth());
        WebElement date = driver.findElement(By.xpath("//div[@class='drp-calendar right']//td[contains(@class,'available') and not(contains(@class,'ends')) and text()='" + day + "']"));
        date.click();
    }

    public void clickOnDateRangeApplyBtn() {
        /*this.btnDateFilterApply.click();*/
        driver.findElement(this.btnDateFilterApply).click();
    }

    public void selectMonthAndYear(String targetDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate targetDate = LocalDate.parse(targetDateStr, formatter);
        String targetMonth = targetDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase(); // "JUN"
        int targetYear = targetDate.getYear();

        // XPath for calendar elements
        String headerXPath = "//div[@class='drp-calendar right']//th[@class='month']";
        String nextBtnXPath = "//th[@class='next available']//span";
        String prevBtnXPath = "//th[@class='prev available']//span";

        while (true) {
            // Get current calendar header text: e.g. "JUN 2025"
            String headerText = driver.findElement(By.xpath(headerXPath)).getText(); // "JUN 2025"
            String[] parts = headerText.trim().split(" ");
            String currentMonth = parts[0].toUpperCase();
            int currentYear = Integer.parseInt(parts[1]);

            if (currentYear == targetYear && currentMonth.equals(targetMonth)) {
                break; // We are on the right month/year
            }

            if (currentYear < targetYear || (currentYear == targetYear && compareMonths(currentMonth, targetMonth) < 0)) {
                driver.findElement(By.xpath(nextBtnXPath)).click(); // Click Next
            } else {
                driver.findElement(By.xpath(prevBtnXPath)).click(); // Click Previous
            }

            // Optional: Small wait for calendar to update
            try {
                Thread.sleep(300); // Or better: use WebDriverWait if needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int compareMonths(String currentMonth, String targetMonth) {
        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("JAN", 1);
        monthMap.put("FEB", 2);
        monthMap.put("MAR", 3);
        monthMap.put("APR", 4);
        monthMap.put("MAY", 5);
        monthMap.put("JUN", 6);
        monthMap.put("JUL", 7);
        monthMap.put("AUG", 8);
        monthMap.put("SEP", 9);
        monthMap.put("OCT", 10);
        monthMap.put("NOV", 11);
        monthMap.put("DEC", 12);

        return monthMap.get(currentMonth) - monthMap.get(targetMonth);
    }

    public void selectExpand() {
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(this.optionExpandCollapse));
        Select select = new Select(option);
        select.selectByValue("expand");
    }

    public void selectCollapse() {
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(this.optionExpandCollapse));
        Select select = new Select(option);
        select.selectByValue("collapse");
    }


    public void clickOnLogTimeBtn() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.btnLogTime));
        element.click();
    }

    public void clickOnTopRowDateCell(String day) {
        By cellDate = By.xpath("//div[contains(@zt-gantt-cell-date,'" + day + "')]");
        By logTimeModal = By.xpath("//div[@class='modal' and not(contains(@style, 'display: none'))]");
        int attempts = 0;
        while (attempts < 3) {
            try {
                ((JavascriptExecutor) driver).executeScript(
                        "let todayMarker = document.getElementById('zt-gantt-marker-today');" +
                                "if (todayMarker) todayMarker.style.display = 'none';"
                );
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(cellDate));
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'nearest', inline: 'center'});", element
                );
                new Actions(driver).moveToElement(element).pause(Duration.ofMillis(500)).click().perform();

                wait.until(ExpectedConditions.visibilityOfElementLocated(logTimeModal));
                break;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                System.out.println("Retrying due to click failure on date cell: " + e.getMessage());
                attempts++;
                try {
                    Thread.sleep(1000); // slight wait before retry
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (TimeoutException e) {
                System.out.println("Log Time popup did not open after clicking date cell.");
                attempts++;
            }
        }
        if (attempts == 3) {
            throw new RuntimeException("Failed to click on top row date cell or log time popup did not appear after multiple attempts for day: " + day);
        }
    }

    public void selectProjectForLogTime(String desiredProject) {
        WebElement projectDropdown = wait.until(ExpectedConditions.elementToBeClickable(this.projectDropdownOfLogTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", projectDropdown);
        projectDropdown.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#project-drop .list ul")));

        List<WebElement> options = driver.findElements(By.cssSelector("#project-drop .list ul li.option"));

        for (WebElement option : options) {
            if (option.getText().trim().equals(desiredProject)) {
                option.click();
                break;
            }
        }
    }

    public List<String> getProjectOptionsForLogTime(){
        WebElement projectDropdown = wait.until(ExpectedConditions.elementToBeClickable(this.projectDropdownOfLogTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", projectDropdown);
        projectDropdown.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#project-drop .list ul")));
        List<WebElement> options = driver.findElements(By.cssSelector("#project-drop .list ul li.option"));
        List<String> projects = new ArrayList<>();
        for (WebElement op : options) {
            projects.add(op.getText().trim());
        }
        projectDropdown.click();
        return projects;
    }

    public void selectIssueForLogTime(String desiredIssue) {
        WebElement issueDropdown = wait.until(ExpectedConditions.elementToBeClickable(this.issueDropdownOfLogTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", issueDropdown);
        issueDropdown.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#issue-drop .list ul")));

        List<WebElement> options = driver.findElements(By.cssSelector("#issue-drop .list ul li.option"));

        for (WebElement option : options) {
            String idText = option.findElement(By.cssSelector(".task_id")).getText().trim();
            if (idText.equals(desiredIssue)) {
                option.click();
                break;
            }
        }
    }

    public List<String> getIssueIDForLogTime(){
        WebElement issueDropdown = wait.until(ExpectedConditions.elementToBeClickable(this.issueDropdownOfLogTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", issueDropdown);  // need to implement to get project ids and also implement to select issue on the basis of ids
        issueDropdown.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#issue-drop .list ul")));
        List<WebElement> options = driver.findElements(By.cssSelector("#issue-drop .list ul li.option"));
        List<String> issueIds = new ArrayList<>();
        for (WebElement op : options) {
            String idText = op.findElement(By.cssSelector(".task_id")).getText().trim();
            issueIds.add(idText);
        }
        issueDropdown.click();
        return issueIds;
    }

    public void selectActivityForLogTime(String desiredActivity) {
        WebElement activityDropdown = wait.until(ExpectedConditions.elementToBeClickable(this.activityDropdownOfLogTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", activityDropdown);
        Select select = new Select(activityDropdown);
        select.selectByVisibleText(desiredActivity);
    }

    public List<String> getActivitiesOpetionForLogTime() {
        WebElement activityDropdown = wait.until(ExpectedConditions.elementToBeClickable(this.activityDropdownOfLogTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", activityDropdown);
        Select select = new Select(activityDropdown);
        List<WebElement> options = select.getOptions();
        List<String> activities = new ArrayList<>();
        for (WebElement op : options) {
            activities.add(op.getText().trim());
        }
        return activities;

    }

    public void setDateForLogTime(String date) {
        WebElement calendar = wait.until(ExpectedConditions.elementToBeClickable(this.dateOfLogTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", calendar);
        calendar.click();
    }

    public void setSpentTimeForLogTime(String hours) {
        WebElement spentTime = wait.until(ExpectedConditions.presenceOfElementLocated(this.spentTimeOfLogTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", spentTime);
        spentTime.clear();
        spentTime.sendKeys(hours);
    }

    public void clickOnLogTimeBtnForLogTime() {
        WebElement btnLogTime = wait.until(ExpectedConditions.elementToBeClickable(this.btnLogTimeOfLogTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnLogTime);
        btnLogTime.click();
    }

    public void clickOnCancelBTnForLogTime() {
        WebElement btnCancel = wait.until(ExpectedConditions.elementToBeClickable(this.btnCancelOfLogTime));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnCancel);
        btnCancel.click();
    }


    public Map<String, String> getTimesheetStatusesByProject(String projectName) {

        Map<String, String> timesheetStatuses = new HashMap<>();

        List<WebElement> projectRows = driver.findElements(By.xpath(
                "//div[@class='project_name']/b[text()='"+projectName+"']" +
                        "/ancestor::div[contains(@class, 'zt-gantt-row-item')]"
        ));

        for (WebElement row : projectRows) {
            String taskId = row.getAttribute("zt-gantt-task-id");

            if (taskId != null && !taskId.isEmpty()) {
                // Now get status
                String statusXpath = "//div[@zt-gantt-task-id='" + taskId + "']" +
                        "/div[@data-column-index='r-0']" +
                        "//div[contains(@class, 'zt-gantt-cell-data')]";
                try {
                    WebElement statusElement = driver.findElement(By.xpath(statusXpath));
                    String status = statusElement.getText().trim();
                    //System.out.println("project: "+projectName+" and taskid: "+taskId+" and status: "+status);
                    timesheetStatuses.put(taskId, status);
                }catch (NoSuchElementException e){
                    timesheetStatuses.put(taskId, "Status Not Found");
                }
            }
        }

        return timesheetStatuses;
    }

    public boolean hasPendingTimesheet(String projectName){
        Map<String, String> statuses = getTimesheetStatusesByProject(projectName);
        return statuses.values().stream().allMatch(status-> status.equalsIgnoreCase("Waiting for Approval"));
    }

    public boolean hasApprovedTimesheet(String projectName){
        Map<String, String> statuses = getTimesheetStatusesByProject(projectName);
        return statuses.values().stream().allMatch(status-> status.equalsIgnoreCase("Approved"));
    }

    public boolean hasRejectedTimesheet(String projectName){
        Map<String, String> statuses = getTimesheetStatusesByProject(projectName);
        return statuses.values().stream().allMatch(status-> status.equalsIgnoreCase("Rejected"));
    }

}
