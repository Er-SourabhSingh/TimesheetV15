package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.HeaderPage;
import pageObjects.ProjectsPage;
import pageObjects.TimesheetApprovalPage;
import pageObjects.TimesheetPage;
import testBase.BaseClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TC008_BulkUsersTimesheetApprovalAndRejectionTests extends BaseClass {

    // Actual approval users
    String[] approvalUsers = new String[]{
            "aurora.wren",   // 1Approval
            "autumn.grace",  // 2Approval
            "briar.sunset",  // 3Approval
            "celeste.dawn",  // 4Approval
            "daisy.skye"     // 5Approval
    };

    // Regular (non-approval) users
    String[] users = new String[]{
            "ember.lilac",
            "harmony.rose",
            "isla.moon",
            "nova.starling",
            "opal.sparrow"
    };

    String project = "Project F 5-Level-Schema";
    String startDate = "08/11/2025", endDate = "08/17/2025";
    String [] dateRanges = new String[2];
    Map<String, Map<String , Map<String, Double>>> userProjectActivityHours = new HashMap<>();

    @Test(priority = 1, groups = {"Master", "Sanity", "Regression"})
    public void testSubmitTimesheetsForMultipleUsers() {
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 1: Verify Multiple User Timesheet Approval/Rejection");

        try {

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

                this.dateRanges = timesheetPage.getSelectedDateRange();

                List<String> days = getFirstFiveDay(this.startDate, this.endDate);
                //System.out.println(days);
                logger.info("Step 5: Logging time entries for each day from Monday to friday");

                if (timesheetPage.isSubmitTimesheetBtnEnabled()) {
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

                        userProjectActivityHours.putIfAbsent(user, new HashMap<>());

                        userProjectActivityHours.get(user).putIfAbsent(project, new HashMap<>());

                        Map<String, Double> activityMap = userProjectActivityHours.get(user).get(project);

                        activityMap.put(activity, activityMap.getOrDefault(activity, 0.0) + hoursVal);

                    }


                    logger.info("Step 6: Submitting the timesheet");
                    timesheetPage.clickOnSubmitTimesheetBtn();


                    logger.info("Step 7: Verifying that the " + project + " status is 'Waiting for Approval'");
                    Assert.assertTrue(timesheetPage.hasPendingTimesheet(project));

                }

                logger.info("Step 8: Logging out submitterUser");
                headerPage.clickOnLogout();
            }
        }catch(Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 2, groups = {"Master", "Regression"}, dependsOnMethods = {"testSubmitTimesheetsForMultipleUsers"})// approve 1 to 4
    public void testBulkApproveTimesheets(){
        logger.info("TC002: Verify That Approvers Can Bulk Approved");
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        try {
            for (int i = 0; i < approvalUsers.length - 1; i++) {
                logger.info("---- Logging in as Approver:" + approvalUsers[i]);
                super.login(approvalUsers[i], "12345678");

                logger.info("------ Navigating to project: " + project);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(project);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);
                for(String user : this.users) {
                    logger.info("------ Verify 'Design' and 'Development' Activity hours of user :" + toFullName(user));
                    Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(user))), userProjectActivityHours.get(user).get(project).getOrDefault("Design", 0.0));
                    Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(user))), userProjectActivityHours.get(user).get(project).getOrDefault("Development", 0.0));
                }

                logger.info("------ Approving timesheet for " + Arrays.asList(this.users));
                timesheetApprovalPage.selectAllUser();
                timesheetApprovalPage.clickOnApproveBtn(toFullName(this.users[0]));
                timesheetApprovalPage.setApprovalText("Approved by -------- " + approvalUsers[i]);

                timesheetApprovalPage.clickOnSubmitBtnOfApproval();
                for(String user : this.users) {
                    Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(user)), "Approved");
                }

                logger.info("------ Logging out approver: " + approvalUsers[i]);
                headerPage.clickOnLogout();

                for(String user : this.users) {
                    logger.info("------ Logging in as " + user);
                    super.login(user, "12345678");

                    logger.info("------ Navigating to Timesheet module");
                    headerPage.clickOnTimesheet();

                    logger.info("------ Expanding timesheet view");
                    timesheetPage.selectExpand();

                    logger.info("------ Selecting date range filter as 'Custom Range'");
                    timesheetPage.clickOnFilterDateRange();
                    timesheetPage.selectDateRangeOption("Custom Range");
                    timesheetPage.selectStartDate(startDate);
                    timesheetPage.selectEndDate(endDate);
                    timesheetPage.clickOnDateRangeApplyBtn();

                    if (i <= 4) {
                        logger.info("------ Verifying that the " + project + " status is 'Waiting for Approval' after approval at level " + (i + 1));
                        Assert.assertTrue(timesheetPage.hasPendingTimesheet(project));
                        Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Waiting for Approval");
                    }

                    logger.info("------ Logging out " + user);
                    headerPage.clickOnLogout();
                }


            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }


    @Test(priority = 3, groups = {"Master", "Regression"}, dependsOnMethods = {"testBulkApproveTimesheets"}) // reject at 5
    public void testBulkRejectTimesheets(){
        logger.info("TC003: Verify That Approver At 5 Level Can Bulk Rejected");
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        try {

            logger.info("---- Logging in as Approver:" + approvalUsers[4]);
            super.login(approvalUsers[4], "12345678");

            logger.info("------ Navigating to project: " + project);
            headerPage.clickOnProjects();
            projectsPage.clickOnProjectName(project);

            logger.info("------ Navigating to Timesheet Approval");
            headerPage.clickOnTimesheetApproval();

            logger.info("------ Go to Date Range of Submission");
            timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);
            for(String user : this.users) {
                logger.info("------ Verify 'Design' and 'Development' Activity hours ");
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(user))), userProjectActivityHours.get(user).get(project).getOrDefault("Design", 0.0));
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(user))), userProjectActivityHours.get(user).get(project).getOrDefault("Development", 0.0));
            }

            logger.info("------ Rejecting timesheet for " + Arrays.asList(this.users));
            timesheetApprovalPage.selectAllUser();
            timesheetApprovalPage.clickOnRejectBtn(toFullName(this.users[0]));
            timesheetApprovalPage.setRejectionText("Rejected by -------- " + approvalUsers[4]);

            timesheetApprovalPage.clickOnSubmitBtnOfRejection();
            for(String user : this.users) {
                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(user)), "Rejected");
            }

            logger.info("------ Logging out approver: " + approvalUsers[4]);
            headerPage.clickOnLogout();

            for(String user : this.users) {
                logger.info("------ Logging in as " + user);
                super.login(user, "12345678");

                logger.info("------ Navigating to Timesheet module");
                headerPage.clickOnTimesheet();

                logger.info("------ Expanding timesheet view");
                timesheetPage.selectExpand();

                logger.info("------ Selecting date range filter as 'Custom Range'");
                timesheetPage.clickOnFilterDateRange();
                timesheetPage.selectDateRangeOption("Custom Range");
                timesheetPage.selectStartDate(startDate);
                timesheetPage.selectEndDate(endDate);
                timesheetPage.clickOnDateRangeApplyBtn();


                logger.info("------ Verifying that the " + project + " status is 'Rejected' after Rejection");
                Assert.assertTrue(timesheetPage.hasRejectedTimesheet(project));
                Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Submit Timesheet");

                logger.info("------ Logging out " + user);
                headerPage.clickOnLogout();
            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }
}
