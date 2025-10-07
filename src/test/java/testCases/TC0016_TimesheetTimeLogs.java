package testCases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.HeaderPage;
import pageObjects.TimesheetPage;
import testBase.BaseClass;

import java.util.*;

public class TC0016_TimesheetTimeLogs extends BaseClass {
    String[] users = {
            "aurora.wren",
            "autumn.grace",   // 2Approval
            "briar.sunset",   // 3Approval
            "celeste.dawn",   // 4Approval
            "daisy.skye",     // 5Approval

            "ember.lilac", "harmony.rose", "isla.moon", "ivy.skylark",
            "luna.meadow", "marigold.rayne", "nova.starling",
            "opal.sparrow", "sage.willow", "selene.frost", "serenity.bloom",
            "summer.rain" , "luna.blossom", "violet.ember", "willow.belle"
    };


    String startDate = "11/01/2025", endDate = "11/30/2025";

    Map<String, Map<String, Map<String,Map<String, Map<String, Double>>>>> userDayProjectIssueActivityHours = new HashMap<>();


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
                        this.userDayProjectIssueActivityHours.putIfAbsent(user, new HashMap<>());
                        this.userDayProjectIssueActivityHours.get(user).putIfAbsent(day, new HashMap<>());
                        this.userDayProjectIssueActivityHours.get(user).get(day).putIfAbsent(project, new HashMap<>());
                        this.userDayProjectIssueActivityHours.get(user).get(day).get(project).putIfAbsent(issueId, new HashMap<>());
                        Map<String, Double> activityMap = this.userDayProjectIssueActivityHours.get(user).get(day).get(project).get(issueId);
                        activityMap.put(activity, activityMap.getOrDefault(activity, 0.0) + hoursVal);
                    }

                }
                logger.info("Step 6: Logging out submitterUser");
                headerPage.clickOnLogout();
            }

            List<String[]> rows = new ArrayList<>();

            for (String user : this.userDayProjectIssueActivityHours.keySet()) {
                Map<String, Map<String, Map<String, Map<String, Double>>>> days = this.userDayProjectIssueActivityHours.get(user);

                for (String day : days.keySet()) {
                    Map<String, Map<String, Map<String, Double>>> projects = days.get(day);

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


        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }


}

//div[contains(@class,'task-content-inner') and ancestor::div[contains(@class,'zt-gantt-task-cell') and @zt-gantt-cell-date='2025-10-06'] and ancestor::div[contains(@class,'zt-gantt-row-item') and .//div[contains(@class,'zt-gantt-cell-data') and normalize-space(text())='Redmine Admin']] and ancestor::div[contains(@class,'zt-gantt-child-row') and @zt-gantt-task-id='i10_Design' and .//div[@data-column-index='1' and normalize-space(text())='Design']]]
