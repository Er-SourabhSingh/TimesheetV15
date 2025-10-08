package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.HeaderPage;
import pageObjects.TimesheetPage;
import testBase.BaseClass;
import utilities.ExcelUtility;

import java.io.File;
import java.util.*;

public class TC0016_TimesheetTimeLogs extends BaseClass {
    String[] users = {
            "aurora.wren",
            //"autumn.grace",   // 2Approval
            /*"briar.sunset",   // 3Approval
            "celeste.dawn",   // 4Approval
            "daisy.skye",     // 5Approval

            "ember.lilac", "harmony.rose", "isla.moon", "ivy.skylark",
            "luna.meadow", "marigold.rayne", "nova.starling",
            "opal.sparrow", "sage.willow", "selene.frost", "serenity.bloom",
            "summer.rain" , "luna.blossom", "violet.ember", "willow.belle"*/
    };


    String startDate = "11/01/2025", endDate = "11/30/2025";

    @Test(priority = 1)
    public void logTimesOnTimesheet() {
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 1: Verify submitterUser can submit multiple project's timesheet and Approved");

        String path = ".\\testData\\TimesheetLogs.xlsx";
        String sheetName = "Logs";
        ExcelUtility excel = new ExcelUtility(path);

        try {
            // Ensure testData folder exists
            File folder = new File(".\\testData\\");
            if (!folder.exists()) folder.mkdir();

            // Create header row only once
            excel.setCellData(sheetName, 0, 0, "User");
            excel.setCellData(sheetName, 0, 1, "Day");
            excel.setCellData(sheetName, 0, 2, "Project");
            excel.setCellData(sheetName, 0, 3, "Issue");
            excel.setCellData(sheetName, 0, 4, "Activity");
            excel.setCellData(sheetName, 0, 5, "Hours");

            int rowNum = 1; // Excel row counter

            for (String user : this.users) {
                logger.info("Step 1: Logging in as " + user);
                super.login(user, "12345678");

                logger.info("Step 2: Navigating to Timesheet module");
                headerPage.clickOnTimesheet();

                logger.info("Step 3: Expanding timesheet view");
                timesheetPage.selectExpand();

                logger.info("Step 4: Selecting date range filter as 'Custom Range'");
                timesheetPage.clickOnFilterDateRange();
                timesheetPage.selectDateRangeOption("Custom Range");
                timesheetPage.selectStartDate(this.startDate);
                timesheetPage.selectEndDate(this.endDate);
                timesheetPage.clickOnDateRangeApplyBtn();

                List<String> days = getMonthDates(this.startDate, this.endDate);
                logger.info("Step 5: Logging time entries for each day from Monday to Friday");

                if (timesheetPage.isSubmitTimesheetBtnEnabled()) {
                    for (String day : days) {
                        logger.info("---- Logging entry for: " + day);
                        timesheetPage.clickOnTopRowDateCell(day);

                        String project = super.getRandomProject();
                        logger.info("------ Selecting project: " + project);
                        timesheetPage.selectProjectForLogTime(project);

                        String issueId = super.getRandomIssue();
                        logger.info("------ Selecting issue: " + issueId);
                        timesheetPage.selectIssueForLogTime(issueId);

                        String activity = super.getRandomActivity();
                        logger.info("------ Selecting activity: " + activity);
                        timesheetPage.selectActivityForLogTime(activity);

                        String hours = getRandomTime();
                        logger.info("------ Entering hours: " + hours);
                        timesheetPage.setSpentTimeForLogTime(hours);

                        logger.info("------ Clicking Log Time button");
                        timesheetPage.clickOnLogTimeBtnForLogTime();

                        // ✅ Directly write to Excel
                        excel.setCellData(sheetName, rowNum, 0, user);
                        excel.setCellData(sheetName, rowNum, 1, day);
                        excel.setCellData(sheetName, rowNum, 2, project);
                        excel.setCellData(sheetName, rowNum, 3, issueId);
                        excel.setCellData(sheetName, rowNum, 4, activity);
                        excel.setCellData(sheetName, rowNum, 5, hours);

                        rowNum++;
                    }
                }

                logger.info("Step 6: Logging out submitterUser");
                headerPage.clickOnLogout();
            }

            System.out.println("✅ Timesheet data written directly to Excel: " + path);

        } catch (Exception e) {
            logger.error(e);
            Assert.fail();
        }
    }


    @Test(priority = 2)
    public void checkTimeEntriesOnTimesheet(){

        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 1: Verify As Approval can view timesheet of user of a month");
        try{

            super.login(properties.getProperty("adminUser"),properties.getProperty("adminPassword"));

            logger.info("Step 2: Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("Step 3: Expanding timesheet view");
            timesheetPage.selectExpand();

            logger.info("Step 4: Selecting date range filter as 'Custom Range'");
            timesheetPage.clickOnFilterDateRange();
            timesheetPage.selectDateRangeOption("Custom Range");
            timesheetPage.selectStartDate(this.startDate);
            timesheetPage.selectEndDate(this.endDate);
            timesheetPage.clickOnDateRangeApplyBtn();

            logger.info("Step 5: Selecting date range filter as 'Custom Range'");
            timesheetPage.selectTimesheetView("Team 1");

            String filePath = ".\\testData\\TimesheetLogs.xlsx";
            String sheetName = "Logs";

            ExcelUtility excel = new ExcelUtility(filePath);




            // Step 1: Read all rows from Excel
            List<String> days = super.getMonthDates(this.startDate,this.endDate);
                int rowCount = excel.getRowCount(sheetName);
                for (int i = 1; i <= rowCount; i++) {
                    String excelUser = excel.getCellData(sheetName, i, 0);
                    String excelDate = excel.getCellData(sheetName, i, 1);
                    String excelProject = excel.getCellData(sheetName, i, 2);
                    String excelIssue = excel.getCellData(sheetName, i, 3);
                    String excelActivity = excel.getCellData(sheetName, i, 4);
                    String excelHoursStr = excel.getCellData(sheetName, i, 5);

                    // Match user and date

                    double expectedHours = Double.parseDouble(excelHoursStr.trim());

                    // Get actual hours from UI via page object
                    double actualHours = super.convertTimeStringToDouble(timesheetPage.getLoggedHours(super.toFullName(excelUser), excelIssue, excelActivity, excelDate));
                    //double actualHours = super.convertTimeStringToDouble(timesheetPage.getLoggedHours(super.toFullName("autumn.grace"), "#214", "Design","2025-11-15"));

                    System.out.println(expectedHours+" | "+ actualHours);

                    // Validate
                    Assert.assertEquals(actualHours, expectedHours,
                            "Mismatch for user " + excelUser + " on " + excelDate +
                                    " (" + excelProject + " | " + excelIssue + " | " + excelActivity + ")");
                    }

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }


}