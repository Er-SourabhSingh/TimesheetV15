package testCases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObjects.*;
import testBase.BaseClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TC007_TimesheetSubmissionAndApprovalForTwoProjectsSameSchema extends BaseClass {
    String[] newApprovalUsers = {
            "ivy.skylark",    // New 1ApproverNew
            "luna.meadow",    // New 2ApproverNew
            "marigold.rayne"  // New 3ApproverNew
    };

    String[] approvalUsers = {
            "aurora.wren",    // 1Approval
            "autumn.grace",   // 2Approval
            "briar.sunset",   // 3Approval
            "celeste.dawn",   // 4Approval
            "daisy.skye"      // 5Approval
    };

    String[] projects = {
            "Project D 3-Level-Schema",
            "New Project 3-Level-Schema"
    };

    String[] approvalRoles = {
            "1Approval",
            "2Approval",
            "3Approval",
            "4Approval",
            "5Approval"
    };

    String[] users = {
            "ember.lilac",
            "harmony.rose",
            "isla.moon",
            "nova.starling",
            "opal.sparrow",
            "sage.willow",
            "selene.frost",
            "serenity.bloom",
            "summer.rain"
    };


    String submitterUser = "opal.sparrow"; // user8 replaced

    String startDate = "08/04/2025", endDate = "08/10/2025";
    String [] dateRanges = new String[2];
    Map<String , Map<String, Double>> projectActivityHours = new HashMap<>();

    @Test(priority = 1, groups = {"Sanity", "Master", "Regression"})
    public void testSubmitTimesheetForProjectsDAndNewProject(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 1: Verify approval flow in different projects which have same Schema");

        try {
            logger.info("Step 1: Logging in as " + this.submitterUser);
            super.login(this.submitterUser, "12345678");

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

            logger.info("Step 8: Logging out submitterUser");
            headerPage.clickOnLogout();
        } catch (Exception e) {
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 2, groups = {"Master", "Regression"}, dependsOnMethods = {"testSubmitTimesheetForProjectsDAndNewProject"})
    public void testApproveTimesheetofProjectD(){
        logger.info("TC002: Verify That Approver can Approve Timesheet Project D");
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        try {
            for (int i = 0; i < approvalUsers.length - 2; i++) {
                logger.info("---- Logging in as Approver:" + approvalUsers[i]);
                super.login(approvalUsers[i], "12345678");

                logger.info("------ Navigating to project: " + projects[0]);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(projects[0]);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

                logger.info("------ Verify 'Design' and 'Development' Activity hours ");
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.submitterUser))), projectActivityHours.get(projects[0]).getOrDefault("Design", 0.0));
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.submitterUser))), projectActivityHours.get(projects[0]).getOrDefault("Development", 0.0));

                logger.info("------ Approving timesheet for " + submitterUser);
                timesheetApprovalPage.clickOnApproveBtn(toFullName(this.submitterUser));
                timesheetApprovalPage.setApprovalText("Approved by -------- " + approvalUsers[i]);
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.submitterUser)), "Approved");

                logger.info("------ Logging out approver: " + approvalUsers[i]);
                headerPage.clickOnLogout();


                logger.info("------ Logging in as " + submitterUser);
                super.login(submitterUser, "12345678");

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

                if (i <= 1) {
                    logger.info("------ Verifying that the " + projects[0] + " status is 'Waiting for Approval' after approval at level " + (i + 1));
                    Assert.assertTrue(timesheetPage.hasPendingTimesheet(projects[0]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Waiting for Approval");
                }
                if (i == 2) {
                    logger.info("------ Verifying that the " + projects[0] + " status is 'Approved' after approval at level " + (i + 1));
                    Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[0]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Waiting for Approval");
                }

                logger.info("------ Logging out " + submitterUser);
                headerPage.clickOnLogout();
            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 3, groups = {"Master", "Regression"}, dependsOnMethods = {"testSubmitTimesheetForProjectsDAndNewProject"})
    public void testApproveTimesheetofNewProject(){
        logger.info("TC003: Verify That Approver can Approve Timesheet New Project");
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        try {
            for (int i = 0; i < newApprovalUsers.length; i++) {
                logger.info("---- Logging in as Approver:" + newApprovalUsers[i]);
                super.login(newApprovalUsers[i], "12345678");

                logger.info("------ Navigating to project: " + projects[1]);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(projects[1]);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

                logger.info("------ Verify 'Design' and 'Development' Activity hours ");
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.submitterUser))), projectActivityHours.get(projects[1]).getOrDefault("Design", 0.0));
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.submitterUser))), projectActivityHours.get(projects[1]).getOrDefault("Development", 0.0));

                logger.info("------ Approving timesheet for " + submitterUser);
                timesheetApprovalPage.clickOnApproveBtn(toFullName(this.submitterUser));
                timesheetApprovalPage.setApprovalText("Approved by -------- " + newApprovalUsers[i]);
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.submitterUser)), "Approved");

                logger.info("------ Logging out approver: " + newApprovalUsers[i]);
                headerPage.clickOnLogout();


                logger.info("------ Logging in as " + submitterUser);
                super.login(submitterUser, "12345678");

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

                if (i <= 1) {
                    logger.info("------ Verifying that the " + projects[1] + " status is 'Waiting for Approval' after approval at level " + (i + 1));
                    Assert.assertTrue(timesheetPage.hasPendingTimesheet(projects[1]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Waiting for Approval");
                }
                if (i == 2) {
                    logger.info("------ Verifying that the " + projects[1] + " status is 'Approved' after approval at level " + (i + 1));
                    Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[1]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Timesheet Approved");
                }

                logger.info("------ Logging out " + submitterUser);
                headerPage.clickOnLogout();
            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 4, groups = {"Master", "Regression"}, dependsOnMethods = {"testApproveTimesheetofProjectD","testApproveTimesheetofNewProject"})
    public void testFinalVerificationStatusAllApproved() {
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 8: Final verification of all timesheet statuses");
        try{
            logger.info("------ Logging in as "+ submitterUser);
            super.login(submitterUser,"12345678");

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

            for(String project : projects){
                logger.info("------ Verifying that the " + project + " status is 'Approved'");
                Assert.assertTrue(timesheetPage.hasApprovedTimesheet(project));
            }
            Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(),"Timesheet Approved");

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }
}
