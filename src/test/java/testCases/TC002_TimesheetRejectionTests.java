package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.*;
import testBase.BaseClass;
import utilities.DataProviders;

public class TC002_TimesheetRejectionTests extends BaseClass {

    @Test(priority = 1, dataProvider = "rejectionFlow", dataProviderClass = DataProviders.class)
    public void testTimesheetRejectionFlow(String projectName, String dateRange, String submitter, String rejectBy, String []approvers){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        try{
            logger.info("Step 1: Logging in as user " + submitter);
            super.login(submitter,"12345678");

            logger.info("Step 2: Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("Step 3: Expanding timesheet view");
            timesheetPage.selectExpand();

            logger.info("Step 4: Selecting date range filter as 'Last Week'");
            timesheetPage.clickOnFilterDateRange();
            timesheetPage.selectDateRangeOption(dateRange);

            String [] dateRanges = timesheetPage.getSelectedDateRange();

            String days[] = getLastWeekDayNumbers();
            logger.info("Step 5: Logging time entries for each day from Monday to Friday");
            for(String day : days) {
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

            logger.info("Step 7: Logging out user");
            headerPage.clickOnLogout();


            logger.info("Step 8: Rejection process begins");
            for(String approver : approvers){
                logger.info("---- Logging in as approver: " + approver);
                super.login(approver, "12345678");

                logger.info("------ Navigating to project: " + projectName);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(projectName);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0],dateRanges[1]);

                if(approver.equals(rejectBy)){
                    logger.info("------ Rejecting timesheet for user: " + submitter);
                    timesheetApprovalPage.clickOnRejectBtn(submitter);
                    timesheetApprovalPage.setRejectionText("Rejected by -------- " +approver);
                    timesheetApprovalPage.clickOnSubmitBtnOfRejection();
                    logger.info("------ Verifying that timesheet is marked as 'Rejected'");
                    Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(submitter),"Rejected");

                    logger.info("------ Logging out approver: " + approver);
                    headerPage.clickOnLogout();
                    break;
                }

                logger.info("------ Approving timesheet for user: " + submitter);
                timesheetApprovalPage.clickOnApproveBtn(submitter);

                timesheetApprovalPage.setApprovalText("Approved by -------- " +approver);
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                logger.info("------ Logging out approver: " + approver);
                headerPage.clickOnLogout();
            }

            logger.info("Step 9: Verifying that timesheet is marked as 'Rejected' for all approvers.");
            for(String approver : approvers){
                logger.info("---- Logging in as approver: " + approver);
                super.login(approver, "12345678");

                logger.info("------ Navigating to project: " + projectName);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(projectName);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Sumbmission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0],dateRanges[1]);

                logger.info("------ Verifying that timesheet is marked as 'Rejected' for approval "+approver);
                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(submitter),"Rejected");

                logger.info("------ Logging out approver: " + approver);
                headerPage.clickOnLogout();
            }

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 2, dataProvider = "rejectionFlow", dataProviderClass = DataProviders.class)
    public void testResubmitTimesheetForRejectedProject(String projectName, String dateRange, String submitter, String rejectBy, String[] approvers) {
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);

        try {
            logger.info("Step 1: Login as user " + submitter + " to resubmit timesheet");
            super.login(submitter, "12345678");

            logger.info("Step 2: Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("Step 3: Selecting date range filter: " + dateRange);
            timesheetPage.clickOnFilterDateRange();
            timesheetPage.selectDateRangeOption(dateRange);

            logger.info("Step 4: Approval flow begins after resubmission");
            String[] dateRanges = timesheetPage.getSelectedDateRange(); // capture selected range

            logger.info("Step 5: Resubmitting the rejected timesheet");
            Assert.assertTrue(timesheetPage.isSubmitTimesheetBtnEnabled(), "Submit button should be enabled for resubmission.");
            timesheetPage.clickOnSubmitTimesheetBtn();

            logger.info("Step 6: Logging out submitter");
            headerPage.clickOnLogout();

            logger.info("Step 7: Sequential approval process begins");
            for (String approver : approvers) {
                logger.info("---- Logging in as approver: " + approver);
                super.login(approver, "12345678");

                logger.info("------ Navigating to project: " + projectName);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(projectName);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

                logger.info("------ Approving resubmitted timesheet for user: " + submitter);
                timesheetApprovalPage.clickOnApproveBtn(submitter);
                timesheetApprovalPage.setApprovalText("Approved after resubmission by -------- " + approver);
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                logger.info("------ Verifying that timesheet is now marked as 'Approved'");
                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(submitter), "Approved");

                logger.info("------ Logging out approver: " + approver);
                headerPage.clickOnLogout();
            }

            logger.info("Step 8: Login as user " + submitter);
            super.login(submitter, "12345678");

            logger.info("Step 9: Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("Step 10: Selecting date range filter: " + dateRange);
            timesheetPage.clickOnFilterDateRange();
            timesheetPage.selectDateRangeOption(dateRange);

            logger.error("Step 11: Check Submitted timesheet Btn text converted into Timesheet Approved");
            Assert.assertFalse(timesheetPage.isSubmitTimesheetBtnEnabled());
            Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(),"Timesheet Approved");

            headerPage.clickOnLogout();

        } catch (Exception e) {
            logger.error("Exception in resubmission test: ", e);
            Assert.fail();
        }
    }




}
