package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.HeaderPage;
import pageObjects.ProjectsPage;
import pageObjects.TimesheetApprovalPage;
import pageObjects.TimesheetPage;
import testBase.BaseClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TC005_PartiallyRejectTimesheetForProjects extends BaseClass {
    String []projects = new String[]{"Project A Default-Schema", "Project C 2-Level-Schema", "Project D 3-Level-Schema"};
    String[] approvalUsers = new String[]{
            "aurora.wren",    // 1Approval
            "autumn.grace",   // 2Approval
            "briar.sunset",   // 3Approval
            "celeste.dawn",   // 4Approval
            "daisy.skye"      // 5Approval
    };
    String [] dateRanges = new String[2];
    String startDate = "06/30/2025", endDate = "07/06/2025";
    String user = "serenity.bloom";

   /* Map<String , Map<String, Double>> projectActivityHours = new HashMap<>();*/

    @Test(priority = 1, groups = {"Master", "Sanity", "Regression"})
    public void testSubmitTimesheetForProjectsACD() {
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 1: Verify Multiple Project Rejection");

        try {
            logger.info("Step 1: Logging in as " + this.user);
            super.login(this.user, "12345678");

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

                        /*Double hoursVal = Double.parseDouble(hours);
                        projectActivityHours.putIfAbsent(project, new HashMap<>());
                        Map<String, Double> activityMap = projectActivityHours.get(project);
                        activityMap.put(activity, activityMap.getOrDefault(activity,0.0)+hoursVal);*/
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

    @Test(priority = 2, groups = {"Master", "Regression"}, dependsOnMethods = {"testSubmitTimesheetForProjectsACD"})
    public void testRejectProjectCAtLevel1(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 2: Verify Rejection of Project C Timesheet");
        try {
            logger.info("Step 1: Logging in as " + approvalUsers[0]);
            super.login(approvalUsers[0], "12345678");

            logger.info("Step 2: Navigate to project: " + projects[1]);
            headerPage.clickOnProjects();
            projectsPage.clickOnProjectName(projects[1]);

            logger.info("Step 3: Navigate to Timesheet Approval");
            headerPage.clickOnTimesheetApproval();

            logger.info("Step 4: Go to Date Range of Submission");
            timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

            /*logger.info("Step 5: Verify 'Design' and 'Development' Activity hours ");
            Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.user))), projectActivityHours.get(projects[1]).getOrDefault("Design", 0.0));
            Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.user))), projectActivityHours.get(projects[1]).getOrDefault("Development", 0.0));*/

            logger.info("Step 6: Reject timesheet for: " + user);
            timesheetApprovalPage.clickOnRejectBtn(toFullName(this.user));
            timesheetApprovalPage.setRejectionText("Rejected by --------- " + approvalUsers[0]);
            timesheetApprovalPage.clickOnSubmitBtnOfRejection();

            Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.user)), "Rejected");

            logger.info("Step 7: Logging out as " + approvalUsers[0]);
            headerPage.clickOnLogout();


            logger.info("Step 8: Logging in as " + user);
            super.login(user, "12345678");

            logger.info("Step 9: Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("Step 10: Expanding timesheet view");
            timesheetPage.selectExpand();

            logger.info("Step 11: Selecting date range filter as 'Custom Range'");
            timesheetPage.clickOnFilterDateRange();
            timesheetPage.selectDateRangeOption("Custom Range");
            timesheetPage.selectStartDate(startDate);
            timesheetPage.selectEndDate(endDate);
            timesheetPage.clickOnDateRangeApplyBtn();

            Assert.assertTrue(timesheetPage.hasRejectedTimesheet(projects[1]));
            Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Submit Timesheet");

            logger.info("Step 12: Logging out " + user);
            headerPage.clickOnLogout();
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 3, groups = {"Master", "Regression"}, dependsOnMethods = {"testSubmitTimesheetForProjectsACD"})
    public void testApproveProjectAByAdmin(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 3: Verify Approval of Project A Timesheet");
        try{
            logger.info("Step 1: Logging in as admin");
            super.login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));

            logger.info("Step 2: Navigating to project: " + projects[0]);
            headerPage.clickOnProjects();
            projectsPage.clickOnProjectName(projects[0]);

            logger.info("Step 3: Navigating to Timesheet Approval");
            headerPage.clickOnTimesheetApproval();

            logger.info("Step 4: Go to Date Range of Submission");
            timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

            /*logger.info("Step 5: Verify 'Design' and 'Development' Activity hours ");
            Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.user))),projectActivityHours.get(projects[0]).getOrDefault("Design",0.0));
            Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.user))),projectActivityHours.get(projects[0]).getOrDefault("Development",0.0));*/

            logger.info("Step 6: Approve timesheet for: "+user);
            timesheetApprovalPage.clickOnApproveBtn(toFullName(this.user));
            timesheetApprovalPage.setApprovalText("Approved by --------- Admin");
            timesheetApprovalPage.clickOnSubmitBtnOfApproval();

            Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.user)),"Approved");

            logger.info("Step 7: Logging out as Admin");
            headerPage.clickOnLogout();

            logger.info("Step 8: Logging in as "+user);
            super.login(user,"12345678");

            logger.info("Step 9: Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("Step 10: Expanding timesheet view");
            timesheetPage.selectExpand();

            logger.info("Step 11: Selecting date range filter as 'Custom Range'");
            timesheetPage.clickOnFilterDateRange();
            timesheetPage.selectDateRangeOption("Custom Range");
            timesheetPage.selectStartDate(startDate);
            timesheetPage.selectEndDate(endDate);
            timesheetPage.clickOnDateRangeApplyBtn();

            Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[0]));

            logger.info("Step 12: Logging out "+user);
            headerPage.clickOnLogout();

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 4, groups = {"Master", "Regression"}, dependsOnMethods = {"testSubmitTimesheetForProjectsACD"})
    public void testCheckDesignAndDevelopementTimeInProjectD(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 4: Verify Approval of Project A Timesheet");
        try{
            logger.info("Step 1: Logging in as "+ approvalUsers[0]);
            super.login(approvalUsers[0], "12345678");

            logger.info("Step 2: Navigating to project: " + projects[2]);
            headerPage.clickOnProjects();
            projectsPage.clickOnProjectName(projects[2]);

            logger.info("Step 3: Navigating to Timesheet Approval");
            headerPage.clickOnTimesheetApproval();

            logger.info("Step 4: Go to Date Range of Submission");
            timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

            /*logger.info("Step 5: Verify 'Design' and 'Development' Activity hours ");
            Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.user))),projectActivityHours.get(projects[2]).getOrDefault("Design",0.0),"Design");
            Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.user))),projectActivityHours.get(projects[2]).getOrDefault("Development",0.0),"Development");*/

            logger.info("Step 6: Check approve and reject btn is enabled for : "+user);
            Assert.assertTrue(timesheetApprovalPage.isApproveBtnEnabled(toFullName(this.user)));
            Assert.assertTrue(timesheetApprovalPage.isRejectBtnEnabled(toFullName(this.user)));

            logger.info("Step 7: Logging out "+user);
            headerPage.clickOnLogout();

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test (priority = 5,  groups = {"Master", "Regression"}, dependsOnMethods = {"testRejectProjectCAtLevel1","testApproveProjectAByAdmin","testCheckDesignAndDevelopementTimeInProjectD"})
    public void testVerifyTimesheetStatusApprovedRejectedPending(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 5: Verify status of Project A C D");
        try {
            logger.info("Step 1: Logging in as" +user);
            super.login(this.user, "12345678");

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

            logger.info("Step 5: Verifying that the " + projects[0] + " status is 'Approved'");
            Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[0]));

            logger.info("Step 6: Verifying that the " + projects[1] + " status is 'Rejected'");
            Assert.assertTrue(timesheetPage.hasRejectedTimesheet(projects[1]));

            logger.info("Step 7: Verifying that the " + projects[2] + " status is 'Waiting for Approval'");
            Assert.assertTrue(timesheetPage.hasPendingTimesheet(projects[2]));

        } catch (Exception e) {
            logger.error(e);
            Assert.fail();
        }
    }

    @Test (priority = 6, groups = {"Master", "Regression"}, dependsOnMethods = {"testVerifyTimesheetStatusApprovedRejectedPending"})
    public void testResubmitRejectedProjectCTimesheet(){
        logger.info("Test case 6: Resubmitting rejected timesheet for a Rejected Project's Timesheet after updating");
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        try {
            List<String> days = getFirstFiveDay(this.startDate, this.endDate);
            logger.info("Step 1: log time for project C");
            String[] activities = new String[]{"Design", "Development"};
            if (timesheetPage.isSubmitTimesheetBtnEnabled()) {
                for (String activity : activities) {
                    Random random = new Random();
                    String day = days.get(random.nextInt(days.size()));
                    logger.info("---- Logging entry for: " + day);
                    timesheetPage.clickOnTopRowDateCell(day);
                    logger.info("------ Selecting project: " + projects[1]);
                    timesheetPage.selectProjectForLogTime(projects[1]);

                    logger.info("------ Selecting issue: Test");
                    timesheetPage.selectIssueForLogTime("Test");

                    logger.info("------ Selecting activity: " + activity);
                    timesheetPage.selectActivityForLogTime(activity);

                    String hours = getRandomTime();
                    logger.info("------ Entering hours: " + hours);
                    timesheetPage.setSpentTimeForLogTime(hours);

                    logger.info("------ Clicking Log Time button");
                    timesheetPage.clickOnLogTimeBtnForLogTime();

                    /*Double hoursVal = Double.parseDouble(hours);
                    projectActivityHours.putIfAbsent(projects[1], new HashMap<>());
                    Map<String, Double> activityMap = projectActivityHours.get(projects[1]);
                    activityMap.put(activity, activityMap.getOrDefault(activity, 0.0) + hoursVal);*/
                }
            }
            logger.info("Step 2: Resubmitting the timesheet");
            timesheetPage.clickOnSubmitTimesheetBtn();

            logger.info("Step 5: Verifying that the " + projects[0] + " status is 'Approved'");
            Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[0]));

            logger.info("Step 6: Verifying that the " + projects[1] + " status is 'Waiting for Approval'");
            Assert.assertTrue(timesheetPage.hasPendingTimesheet(projects[1]));

            logger.info("Step 7: Verifying that the " + projects[2] + " status is 'Waiting for Approval'");
            Assert.assertTrue(timesheetPage.hasPendingTimesheet(projects[2]));

            logger.info("Step 3: Logging out submitterUser");
            headerPage.clickOnLogout();
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }

    }

    @Test(priority = 7, groups = {"Master", "Regression"}, dependsOnMethods = {"testResubmitRejectedProjectCTimesheet"})
    public void testFinalApprovalVerificationForAllProjects(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 7: Approving all resubmitted timesheets through levels");
        try {
            logger.info("Step 1: Logging in as admin");
            super.login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));

            logger.info("Step 2: Navigating to project: " + projects[0]);
            headerPage.clickOnProjects();
            projectsPage.clickOnProjectName(projects[0]);

            logger.info("Step 3: Navigating to Timesheet Approval");
            headerPage.clickOnTimesheetApproval();

            logger.info("Step 4: Go to Date Range of Submission");
            timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

            /*logger.info("Step 5: Verify 'Design' and 'Development' Activity hours ");
            Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.user))),projectActivityHours.get(projects[0]).getOrDefault("Design",0.0));
            Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.user))),projectActivityHours.get(projects[0]).getOrDefault("Development",0.0));*/

            logger.info("Step 7: Verified timesheet of: " + user +" should be already in approved state");

            Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.user)), "Approved");

            logger.info("Step 8: Logging out as Admin");
            headerPage.clickOnLogout();

            logger.info("Step 9: Logging in as " + user);
            super.login(user, "12345678");

            logger.info("Step 10: Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("Step 11: Expanding timesheet view");
            timesheetPage.selectExpand();

            logger.info("Step 12: Selecting date range filter as 'Custom Range'");
            timesheetPage.clickOnFilterDateRange();
            timesheetPage.selectDateRangeOption("Custom Range");
            timesheetPage.selectStartDate(startDate);
            timesheetPage.selectEndDate(endDate);
            timesheetPage.clickOnDateRangeApplyBtn();

            Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[0]));
            Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Waiting for Approval");

            logger.info("Step 13: Logging out " + user);
            headerPage.clickOnLogout();

            logger.info("----- Verify That Approver can Approve Timesheet Project C");
            for (int i = 0; i < approvalUsers.length - 3; i++) {
                logger.info("---- Logging in as Approver:" + approvalUsers[i]);
                super.login(approvalUsers[i], "12345678");

                logger.info("------ Navigating to project: " + projects[1]);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(projects[1]);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

                /*logger.info("------ Verify 'Design' and 'Development' Activity hours ");
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.user))),projectActivityHours.get(projects[1]).getOrDefault("Design",0.0));
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.user))),projectActivityHours.get(projects[1]).getOrDefault("Development",0.0));*/

                logger.info("------ Approving timesheet for " + user);
                timesheetApprovalPage.clickOnApproveBtn(toFullName(this.user));
                timesheetApprovalPage.setApprovalText("Approved by --------  "+approvalUsers[i]);
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.user)), "Approved");

                logger.info("------ Logging out approver: " + approvalUsers[i]);
                headerPage.clickOnLogout();


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

                if (i == 0 && !(i < 0)) {
                    logger.info("------ Verifying that the " + projects[1] + " status is 'Waiting for Approval' after approval at level " + (i + 1));
                    Assert.assertTrue(timesheetPage.hasPendingTimesheet(projects[1]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Waiting for Approval");
                }
                if (i == 1) {
                    logger.info("------ Verifying that the " + projects[1] + " status is 'Approved' after approval at level " + (i + 1));
                    Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[1]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Waiting for Approval");
                }

                logger.info("------ Logging out " + user);
                headerPage.clickOnLogout();

            }
            logger.info("----- Verify That Approver can Approve Timesheet Project D");
            for(int i = 0; i < approvalUsers.length - 2; i++) {
                logger.info("---- Logging in as Approver:" + approvalUsers[i]);
                super.login(approvalUsers[i], "12345678");

                logger.info("------ Navigating to project: " + projects[2]);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(projects[2]);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

                /*logger.info("------ Verify 'Design' and 'Development' Activity hours ");
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.user))), projectActivityHours.get(projects[2]).getOrDefault("Design", 0.0));
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.user))), projectActivityHours.get(projects[2]).getOrDefault("Development", 0.0));*/

                logger.info("------ Approving timesheet for " + user);
                timesheetApprovalPage.clickOnApproveBtn(toFullName(this.user));
                timesheetApprovalPage.setApprovalText("Approved by -------- "+approvalUsers[i]);
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.user)), "Approved");

                logger.info("------ Logging out approver: " + approvalUsers[i]);
                headerPage.clickOnLogout();


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

                if (i <= 1 && !(i < 0)) {
                    logger.info("------ Verifying that the " + projects[2] + " status is 'Waiting for Approval' after approval at level " + (i + 1));
                    Assert.assertTrue(timesheetPage.hasPendingTimesheet(projects[2]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Waiting for Approval");
                }
                if (i == 2) {
                    logger.info("------ Verifying that the " + projects[2] + " status is 'Approved' after approval at level " + (i + 1));
                    Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[2]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(), "Timesheet Approved");
                }

                logger.info("------ Logging out " + user);
                headerPage.clickOnLogout();
            }
        } catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 8, groups = {"Master", "Regression"}, dependsOnMethods = {"testFinalApprovalVerificationForAllProjects"})
    public void testFinalVerificationStatusAllApproved() {
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 8: Final verification of all timesheet statuses");
        try{
            logger.info("------ Logging in as "+user);
            super.login(user,"12345678");

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
