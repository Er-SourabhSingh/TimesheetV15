package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.HeaderPage;
import pageObjects.TimesheetPage;
import testBase.BaseClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TC005_PartiallyRejectTimesheetForProjects extends BaseClass {
    String []projects = new String[]{"Project A Default-Schema", "Project C 2-Level-Schema", "Project D 3-Level-Schema"};
    String [] approvalUsers = new String[]{"1Approval","2Approval","3Approval","4Approval","5Approval"};
    String [] dateRanges = new String[2];
    String startDate = "06/30/2025", endDate = "07/06/2025";
    String user = "user7";

    Map<String , Map<String, Double>> projectActivityHours = new HashMap<>();

    @Test(priority = 1)
    public void testSubmitTimesheetForProjectsACD() {
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 1: Verify Multiple Project Rejection");

        try {
            logger.info("Step 1: Logging in as user4");
            super.login(this.user, "12345678");

            logger.info("Step 2: Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("Step 3: Expanding timesheet view");
            timesheetPage.selectExpand();

            logger.info("Step 4: Selecting date range filter as 'Last Week'");
            timesheetPage.clickOnFilterDateRange();
            timesheetPage.selectDateRangeOption("Custom Range");
            timesheetPage.selectStartDate(this.startDate);
            timesheetPage.selectEndDate(this.endDate);
            timesheetPage.clickOnDateRangeApplyBtn();

            this.dateRanges = timesheetPage.getSelectedDateRange();

            List<String> days = getFirstFiveDay(this.startDate, this.endDate);
            logger.info("Step 5: Logging time entries for each day from Monday to friday");

            if (timesheetPage.isSubmitTimesheetBtnEnabled()) {
                for (String project : this.projects) {
                    for (String day : days) {
                        logger.info("---- Logging entry for: " + day);
                        timesheetPage.clickOnTopRowDateCell(day);

                        logger.info("------ Selecting project: " + project);
                        timesheetPage.selectProjectForLogTime(project);

                        logger.info("------ Selecting issue: Test");
                        timesheetPage.selectIssueForLogTime("Test");

                        String activity = getRandomActivity();
                        logger.info("------ Selecting activity: " + activity);
                        timesheetPage.selectActivityForLogTime(activity);

                        String hours = getRandomTime();
                        logger.info("------ Entering hours: " + hours);
                        timesheetPage.setSpentTimeForLogTime(hours);

                        logger.info("------ Clicking Log Time button");
                        timesheetPage.clickOnLogTimeBtnForLogTime();

                        Double hoursVal = Double.parseDouble(hours);
                        projectActivityHours.putIfAbsent(project, new HashMap<>());
                        Map<String, Double> activityMap = projectActivityHours.get(project);
                        activityMap.put(activity, activityMap.getOrDefault(activity,0.0)+hoursVal);
                    }
                }

                logger.info("Step 6: Submitting the timesheet");
                timesheetPage.clickOnSubmitTimesheetBtn();

                for (String project : projects) {
                    logger.info("------ Verifying that the " + project + " status is 'Waiting for Approval'");
                    Assert.assertTrue(timesheetPage.hasPendingTimesheet(project));
                }
            }

            logger.info("Step 8: Logging out user");
            headerPage.clickOnLogout();
        } catch (Exception e) {
            logger.error(e);
            Assert.fail();
        }
    }
}
