package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.*;
import testBase.BaseClass;
import utilities.DataProviders;

import java.util.ArrayList;
import java.util.List;

public class TC001_TimesheetApprovalTests extends BaseClass {

    @Test(priority = 1, groups = {"Data Driven"}, dataProvider = "schemaFlows", dataProviderClass = DataProviders.class)
    public void testTimesheetApprovalFlow(String projectName, String dateRange, String user, String approvers[]){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        HistoryApprovalPage historyApprovalPage = new HistoryApprovalPage(driver);
        try{
            logger.info("Step 1: Logging in as submitterUser " + user);
            super.login(user,"12345678");

            logger.info("Step 2: Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("Step 3: Expanding timesheet view");
            timesheetPage.selectExpand();

            logger.info("Step 4: Selecting date range filter as 'Current Week'");
            timesheetPage.clickOnFilterDateRange();
            timesheetPage.selectDateRangeOption(dateRange);
            String days[] = getCurrentWeekDayNumbers();
            if(timesheetPage.isSubmitTimesheetBtnEnabled()) {
                logger.info("Step 5: Logging time entries for each day from Monday to Friday");
                for (String day : days) {
                    logger.info("---- Logging entry for: " + day);
                    timesheetPage.clickOnTopRowDateCell(day);

                    logger.info("------ Selecting project: " + projectName);
                    timesheetPage.selectProjectForLogTime(projectName);

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
                }
                logger.info("Step 6: Submitting the timesheet");
                timesheetPage.clickOnSubmitTimesheetBtn();
            }

            logger.info("Step 7: Logging out submitterUser");
            headerPage.clickOnLogout();

            logger.info("Step 8: Sequential approval process begins");
            for(String approver : approvers){
                logger.info("---- Logging in as approver: " + approver);
                super.login(approver, "12345678");

                logger.info("------ Navigating to project: " + projectName);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(projectName);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Approving timesheet for submitterUser: " + user);
                timesheetApprovalPage.clickOnApproveBtn(toFullName(user));
                timesheetApprovalPage.setApprovalText("Approved by -------- " +approver);
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(user)),"Approved");

                //checking histroy
                logger.info("------ Clicking on show btn of : " + user + "Timesheet History");
                timesheetApprovalPage.clickOnShowHistoryOfUserTimesheet(toFullName(user));
                List<String> history = new ArrayList<>();
                history.add("Project Name : " +projectName);
                history.add("Status : Approved");
                history.add("Submitted By : " + toFullName(user));
                if(approver.equals("admin")){
                    history.add("Approved By : Redmine Admin");
                }else {
                    history.add("Approved By : " + toFullName(approver));
                }
                history.add("Approval Comment : Approved by -------- " +approver);
                Assert.assertTrue(historyApprovalPage.getLastUpdatedHistory().containsAll(history));

                logger.info("------ Logging out approver: " + approver);
                headerPage.clickOnLogout();
            }

            logger.info("Step 9: Logging in as submitterUser " + user);
            super.login(user,"12345678");

            logger.info("Step 10: Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("Step 11: Select Date Range : "+dateRange);
            timesheetPage.clickOnFilterDateRange();
            timesheetPage.selectDateRangeOption(dateRange);

            logger.info("Step 12: Expanding timesheet view");
            timesheetPage.selectExpand();

            logger.error("Step 13: Check Submitted timesheet Btn text converted into Timesheet Approved");
            Assert.assertFalse(timesheetPage.isSubmitTimesheetBtnEnabled());
            Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(),"Timesheet Approved");

            logger.info("Step 14: Logging out submitterUser");
            headerPage.clickOnLogout();
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }
}
