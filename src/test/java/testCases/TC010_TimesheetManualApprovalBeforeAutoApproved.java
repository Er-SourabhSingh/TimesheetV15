package testCases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pageObjects.*;
import testBase.BaseClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TC010_TimesheetManualApprovalBeforeAutoApproved extends  BaseClass{

    String[] approvalUsers = {
            "aurora.wren",    // 1Approval
            "autumn.grace",   // 2Approval
            "briar.sunset",   // 3Approval
            "celeste.dawn",   // 4Approval
            "daisy.skye"      // 5Approval
    };
    String submitterUser = "marigold.rayne"; // user8 replaced
    String project = "New Project 5-Level-Schema";
    String startDate = "08/18/2025", endDate = "08/24/2025";
    String [] dateRanges = new String[2];
    Map<String , Map<String, Double>> projectActivityHours = new HashMap<>();

    @Test (priority = 1, groups = {"Master","Regression"})
    public void testSubmitTimesheetOfMarigoldRayneForNewProject(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 1: Verify use can submit timesheet for new project");

        try {
            logger.info("Step 1: Logging in as " + this.submitterUser);
            this.login(this.submitterUser, "12345678");

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

                    logger.info("------ Selecting project: " + this.project);
                    timesheetPage.selectProjectForLogTime(this.project);

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


                logger.info("Step 6: Submitting the timesheet");
                timesheetPage.clickOnSubmitTimesheetBtn();


                logger.info("------ Verifying that the " + project + " status is 'Waiting for Approval'");
                Assert.assertTrue(timesheetPage.hasPendingTimesheet(project));

            }

            logger.info("Step 8: Logging out submitterUser");
            headerPage.clickOnLogout();
        } catch (Exception e) {
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 2, groups = {"Master","Regression"} , dependsOnMethods = {"testSubmitTimesheetOfMarigoldRayneForNewProject"})
    public void testApproveUserTimesheetBeforeAutoApproved(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        try{
            logger.info("----- Verify That Approver can Approve Timesheet Before Auto Approver get Trigger");
            for(int i = 0; i < this.approvalUsers.length; i++) {
                logger.info("---- Logging in as Approver:" + this.approvalUsers[i]);
                this.login(this.approvalUsers[i], "12345678");

                logger.info("------ Navigating to project: " + this.project);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(this.project);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(this.dateRanges[0], this.dateRanges[1]);

                logger.info("------ Verify 'Design' and 'Development' Activity hours ");
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.submitterUser))), projectActivityHours.get(this.project).getOrDefault("Design",0.0));
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.submitterUser))), projectActivityHours.get(this.project).getOrDefault("Development",0.0));

                logger.info("------ Approving timesheet for "+ this.submitterUser);
                timesheetApprovalPage.clickOnApproveBtn(toFullName(this.submitterUser));
                timesheetApprovalPage.setApprovalText("Approved by -------- "+ toFullName(this.approvalUsers[i]));
                timesheetApprovalPage.clickOnSubmitBtnOfApproval();

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.submitterUser)),"Approved");

                logger.info("------ Logging out approver: " + this.approvalUsers[i]);
                headerPage.clickOnLogout();

            }

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 3, groups = {"Master", "Regression"}, dependsOnMethods = {"testApproveUserTimesheetBeforeAutoApproved"})
    public void verifyHistoryOfManualApproval(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        HistoryApprovalPage historyApprovalPage = new HistoryApprovalPage(driver);
        try{
            logger.info("----- Verify That History of Manual approver");
            for(int i = 0; i < this.approvalUsers.length ; i++) {
                logger.info("---- Logging in as Approver:" + this.approvalUsers[i]);
                this.login(approvalUsers[i], "12345678");

                logger.info("------ Navigating to project: " + this.project);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(this.project);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(this.dateRanges[0], this.dateRanges[1]);

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.submitterUser)),"Approved");

                //checking histroy
                logger.info("------ Clicking on show btn of : " + this.submitterUser + "Timesheet History");
                timesheetApprovalPage.clickOnShowHistoryOfUserTimesheet(toFullName(this.submitterUser));
                List<String> history = new ArrayList<>();
                history.add("Project Name : " +this.project);
                history.add("Status : Approved");
                history.add("Submitted By : " + toFullName(this.submitterUser));
                history.add("Approved By : " + toFullName(this.approvalUsers[i]));
                history.add("Approval Comment : Approved by -------- "  + toFullName(this.approvalUsers[i]));
                Assert.assertTrue(historyApprovalPage.getLastUpdatedHistoryByUser(toFullName(this.approvalUsers[i])).containsAll(history));

                if(i == this.approvalUsers.length - 1){
                    Thread.sleep(10 * 60 * 1000); // 10 mint
                    Assert.assertTrue(historyApprovalPage.getLastUpdatedHistory().containsAll(history));
                }

                logger.info("------ Logging out approver: " + this.approvalUsers[i]);
                headerPage.clickOnLogout();

            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }



}
