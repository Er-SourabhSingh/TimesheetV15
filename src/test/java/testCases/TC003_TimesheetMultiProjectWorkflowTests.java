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

public class TC003_TimesheetMultiProjectWorkflowTests extends BaseClass {
    String []projects = new String[]{"Project C 2-Level-Schema", "Project D 3-Level-Schema", "Project F 5-Level-Schema"};
    String [] approvalUsers = new String[]{"1Approval","2Approval","3Approval","4Approval","5Approval"};
    String [] dateRanges = new String[2];

    String startDate = "07/14/2025", endDate = "07/20/2025";
    String user = "user4";

    Map<String , Map<String, Double>> projectActivityHours = new HashMap<>();

    @Test (priority = 1)
    public void testSubmitTimesheetForProjectsCDF(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 1: Verify user can submit multiple project's timesheet and Approved");
        try{
            logger.info("Step 1: Logging in as "+user);
            super.login(user,"12345678");

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

            if(timesheetPage.isSubmitTimesheetBtnEnabled()) {
                for (String project : this.projects) {
                    for (String day : days) {
                        logger.info("---- Logging entry for: " + day);
                        timesheetPage.clickOnTopRowDateCell(day);
                        Random random = new Random();
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

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test (priority = 2)
    public void testApproveTimesheetProjectC_2LevelSchema(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        try{
            logger.info("----- Verify That Approver can Approve Timesheet Project C");
            for(int i = 0; i < approvalUsers.length - 3; i++) {
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
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(user)),projectActivityHours.get(projects[0]).getOrDefault("Design",0.0));
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(user)),projectActivityHours.get(projects[0]).getOrDefault("Development",0.0));

                logger.info("------ Approving timesheet for "+user);
                timesheetApprovalPage.clickOnApproveBtn(user);
                timesheetApprovalPage.setApprovalText("Approved by -------- "+approvalUsers[i]);
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(user),"Approved");

                logger.info("------ Logging out approver: " + approvalUsers[i]);
                headerPage.clickOnLogout();

                logger.info("------ Logging in as "+user);
                super.login(user,"12345678");

                logger.info("------ Navigating to Timesheet module");
                headerPage.clickOnTimesheet();

                logger.info("------ Expanding timesheet view");
                timesheetPage.selectExpand();

                logger.info("------ Selecting date range filter as 'Last Week'");
                timesheetPage.clickOnFilterDateRange();
                timesheetPage.selectDateRangeOption("Custom Range");
                timesheetPage.selectStartDate(startDate);
                timesheetPage.selectEndDate(endDate);
                timesheetPage.clickOnDateRangeApplyBtn();

                if(i==0){
                    logger.info("------ Verifying that the " + projects[0] + " status is 'Waiting for Approval' after approval at level " + (i+1));
                    Assert.assertTrue(timesheetPage.hasPendingTimesheet(projects[0]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(),"Waiting for Approval");
                }
                if(i==1){
                    logger.info("------ Verifying that the " + projects[0] + " status is 'Approved' after approval at level " + (i+1));
                    Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[0]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(),"Waiting for Approval");
                }

                logger.info("------ Logging out "+user);
                headerPage.clickOnLogout();
            }

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test (priority = 3)
    public void testApproveTimesheetProjectD_3LevelSchema(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        try{
            logger.info("----- Verify That Approver can Approve Timesheet Project D");
            for(int i = 0; i < approvalUsers.length - 2; i++) {
                logger.info("---- Logging in as Approver:" + approvalUsers[i]);
                super.login(approvalUsers[i], "12345678");

                logger.info("------ Navigating to project: " + projects[1]);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(projects[1]);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

                logger.info("----- Verify 'Design' and 'Development' Activity hours ");
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(user)),projectActivityHours.get(projects[1]).getOrDefault("Design",0.0));
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(user)),projectActivityHours.get(projects[1]).getOrDefault("Development",0.0));

                logger.info("------ Approving timesheet for "+user);
                timesheetApprovalPage.clickOnApproveBtn(user);
                timesheetApprovalPage.setApprovalText("Approved by -------- "+approvalUsers[i]);
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(user),"Approved");

                logger.info("------ Logging out approver: " + approvalUsers[i]);
                headerPage.clickOnLogout();


                logger.info("------ Logging in as "+user);
                super.login(user,"12345678");

                logger.info("------ Navigating to Timesheet module");
                headerPage.clickOnTimesheet();

                logger.info("------ Expanding timesheet view");
                timesheetPage.selectExpand();

                logger.info("------ Selecting date range filter as 'Last Week'");
                timesheetPage.clickOnFilterDateRange();
                timesheetPage.selectDateRangeOption("Custom Range");
                timesheetPage.selectStartDate(startDate);
                timesheetPage.selectEndDate(endDate);
                timesheetPage.clickOnDateRangeApplyBtn();

                if(i <= 1 && !(i < 0)){
                    logger.info("------ Verifying that the " + projects[1] + " status is 'Waiting for Approval' after approval at level " + (i+1));
                    Assert.assertTrue(timesheetPage.hasPendingTimesheet(projects[1]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(),"Waiting for Approval");
                }
                if(i==2){
                    logger.info("------ Verifying that the " + projects[1] + " status is 'Approved' after approval at level " + (i+1));
                    Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[1]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(),"Waiting for Approval");
                }

                logger.info("------ Logging out "+user);
                headerPage.clickOnLogout();
            }

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test (priority = 4)
    public void testApproveTimesheetProjectF_5LevelSchema(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        try{
            logger.info("----- Verify That Approver can Approve Timesheet Project F");
            for(int i = 0; i < approvalUsers.length; i++) {
                logger.info("---- Logging in as Approver:" + approvalUsers[i]);
                super.login(approvalUsers[i], "12345678");

                logger.info("------ Navigating to project: " + projects[2]);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(projects[2]);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

                logger.info("------ Verify 'Design' and 'Development' Activity hours ");
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(user)), projectActivityHours.get(projects[2]).getOrDefault("Design",0.0));
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(user)), projectActivityHours.get(projects[2]).getOrDefault("Development",0.0));


                logger.info("------ Approving timesheet for "+user);
                timesheetApprovalPage.clickOnApproveBtn(user);
                timesheetApprovalPage.setApprovalText("Approved by -------- "+approvalUsers[i]);
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(user),"Approved");

                logger.info("------ Logging out approver: " + approvalUsers[i]);
                headerPage.clickOnLogout();


                logger.info("------ Logging in as "+user);
                super.login(user,"12345678");

                logger.info("------ Navigating to Timesheet module");
                headerPage.clickOnTimesheet();

                logger.info("------ Expanding timesheet view");
                timesheetPage.selectExpand();

                logger.info("------ Selecting date range filter as 'Last Week'");
                timesheetPage.clickOnFilterDateRange();
                timesheetPage.selectDateRangeOption("Custom Range");
                timesheetPage.selectStartDate(startDate);
                timesheetPage.selectEndDate(endDate);
                timesheetPage.clickOnDateRangeApplyBtn();

                if(i <= 3 && !(i < 0)){
                    logger.info("------ Verifying that the " + projects[2] + " status is 'Waiting for Approval' after approval at level " + (i+1));
                    Assert.assertTrue(timesheetPage.hasPendingTimesheet(projects[2]));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(),"Waiting for Approval");
                }
                if(i==4){
                    logger.info("------ Verifying that the " + projects[2] + " status is 'Approved' after approval at level" + (i+1));
                    Assert.assertTrue(timesheetPage.hasApprovedTimesheet(projects[2]));
                }

                logger.info("------ Logging out "+user);
                headerPage.clickOnLogout();
            }

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 5)
    public void testFinalTimesheetStatusOfUser(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("------ Verify that Projects status are 'Approved'");
        try{
            logger.info("------ Logging in as "+user);
            super.login(user,"12345678");

            logger.info("------ Navigating to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("------ Expanding timesheet view");
            timesheetPage.selectExpand();

            logger.info("------ Selecting date range filter as 'Last Week'");
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
