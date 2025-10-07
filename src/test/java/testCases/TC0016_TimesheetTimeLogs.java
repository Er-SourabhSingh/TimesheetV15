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
            //"aurora.wren",
            "autumn.grace",   // 2Approval
            /*"briar.sunset",   // 3Approval
            "celeste.dawn",   // 4Approval
            "daisy.skye",     // 5Approval

            "ember.lilac", "harmony.rose", "isla.moon", "ivy.skylark",
            "luna.meadow", "marigold.rayne", "nova.starling",
            "opal.sparrow", "sage.willow", "selene.frost", "serenity.bloom",
            "summer.rain" , "luna.blossom", "violet.ember", "willow.belle"*/
    };


    String startDate = "11/01/2025", endDate = "11/30/2025";

    Map<String, Map<String, Map<String,Map<String, Map<String, Double>>>>> userDateProjectIssueActivityHours = new HashMap<>();


    @Test(priority = 1)
    public void logTimesOnTimesheet(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 1: Verify submitterUser can submit multiple project's timesheet and Approved");
        try{
            for(String user : this.users) {
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
                logger.info("Step 5: Logging time entries for each day from Monday to friday");

                //System.out.println(days.size());

                if (timesheetPage.isSubmitTimesheetBtnEnabled()) {

                    for (String day : days) {
                        logger.info("---- Logging entry for: " + day);
                        timesheetPage.clickOnTopRowDateCell(day);
                        String project = super.getRandomProject();
                        logger.info("------ Selecting project: " + project);
                        timesheetPage.selectProjectForLogTime(project);

                        String issueId = super.getRandomIssue();
                        logger.info("------ Selecting issue: "+ issueId);
                        timesheetPage.selectIssueForLogTime(issueId);

                        String activity = super.getRandomActivity();
                        logger.info("------ Selecting activity: " + activity);
                        timesheetPage.selectActivityForLogTime(activity);

                        String hours = getRandomTime();
                        logger.info("------ Entering hours: " + hours);
                        timesheetPage.setSpentTimeForLogTime(hours);

                        logger.info("------ Clicking Log Time button");
                        timesheetPage.clickOnLogTimeBtnForLogTime();



                        Double hoursVal = Double.parseDouble(hours);
                        this.userDateProjectIssueActivityHours.putIfAbsent(user, new HashMap<>());
                        this.userDateProjectIssueActivityHours.get(user).putIfAbsent(day, new HashMap<>());
                        this.userDateProjectIssueActivityHours.get(user).get(day).putIfAbsent(project, new HashMap<>());
                        this.userDateProjectIssueActivityHours.get(user).get(day).get(project).putIfAbsent(issueId, new HashMap<>());
                        Map<String, Double> activityMap = this.userDateProjectIssueActivityHours.get(user).get(day).get(project).get(issueId);
                        activityMap.put(activity, activityMap.getOrDefault(activity, 0.0) + hoursVal);
                    }

                }
                logger.info("Step 6: Logging out submitterUser");
                headerPage.clickOnLogout();
            }

            List<String[]> rows = new ArrayList<>();

            for (String user : this.userDateProjectIssueActivityHours.keySet()) {
                Map<String, Map<String, Map<String, Map<String, Double>>>> dates = this.userDateProjectIssueActivityHours.get(user);

                for (String day : dates.keySet()) {
                    Map<String, Map<String, Map<String, Double>>> projects = dates.get(day);

                    for (String project : projects.keySet()) {
                        Map<String, Map<String, Double>> issues = projects.get(project);

                        for (String issue : issues.keySet()) {
                            Map<String, Double> activities = issues.get(issue);

                            for (String activity : activities.keySet()) {
                                Double hours = activities.get(activity);

                                // Save as row (you could also directly write to Excel here)
                                rows.add(new String[]{user, day, project, issue, activity, hours.toString()});
                            }
                        }
                    }
                }
            }


            System.out.printf("%-15s %-12s %-12s %-12s %-15s %-5s%n", "User", "Day", "Project", "Issue", "Activity", "Hours");
            for (String[] row : rows) {
                System.out.printf("%-15s %-12s %-12s %-12s %-15s %-5s%n", row[0], row[1], row[2], row[3], row[4], row[5]);
            }

            try {
                // Ensure testData folder exists
                File folder = new File(".\\testData\\");
                if (!folder.exists()) {
                    folder.mkdir();
                }

                String path = ".\\testData\\TimesheetLogs.xlsx";
                ExcelUtility excel = new ExcelUtility(path);
                String sheetName = "Logs";

                // Write header row
                excel.setCellData(sheetName, 0, 0, "User");
                excel.setCellData(sheetName, 0, 1, "Day");
                excel.setCellData(sheetName, 0, 2, "Project");
                excel.setCellData(sheetName, 0, 3, "Issue");
                excel.setCellData(sheetName, 0, 4, "Activity");
                excel.setCellData(sheetName, 0, 5, "Hours");

                // Write each logged row to Excel
                int rowNum = 1;
                for (String[] row : rows) {
                    for (int colNum = 0; colNum < row.length; colNum++) {
                        excel.setCellData(sheetName, rowNum, colNum, row[colNum]);
                    }
                    rowNum++;
                }

                System.out.println("Timesheet data written successfully to: " + path);

            } catch (Exception e) {
                System.err.println("Failed to write data to Excel: " + e.getMessage());
                e.printStackTrace();
            }


        }catch (Exception e){
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


            String targetDate = "2025-10-06"; // can also parameterize this later

            // Step 1: Read all rows from Excel
            /*List<String> days = super.getMonthDates(this.startDate,this.endDate);

                int rowCount = excel.getRowCount(sheetName);
                boolean foundInExcel = false;
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
                        Double actualHours = super.convertTimeStringToDouble(timesheetPage.getLoggedHours(super.toFullName(excelUser), excelDate, excelIssue, excelActivity));*/
                        Double actualHours = super.convertTimeStringToDouble(timesheetPage.getLoggedHours(super.toFullName("autumn.grace"), "#214", "Design","2025-11-15"));

                        System.out.println( " | "+ actualHours);

                        // Validate
                       /* Assert.assertEquals(actualHours, expectedHours,
                                "Mismatch for user " + excelUser + " on " + excelDate +
                                        " (" + excelProject + " | " + excelIssue + " | " + excelActivity + ")");*/
                    /*}*/


                // If date not found in Excel for that user, you can log it



        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }


}

//div[contains(@class,'task-content-inner') and ancestor::div[contains(@class,'zt-gantt-task-cell') and @zt-gantt-cell-date='2025-10-06'] and ancestor::div[contains(@class,'zt-gantt-row-item') and .//div[contains(@class,'zt-gantt-cell-data') and normalize-space(text())='Redmine Admin']] and ancestor::div[contains(@class,'zt-gantt-child-row') and @zt-gantt-task-id='i10_Design' and .//div[@data-column-index='1' and normalize-space(text())='Design']]]
