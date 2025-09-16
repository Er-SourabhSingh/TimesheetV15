package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.*;
import testBase.BaseClass;

import java.util.*;

public class TC014_TimesheetApprovalMemberManualRejectionTests extends BaseClass {
    String[] approvalUsers = {
            "aurora.wren",    // 1Approval
            "autumn.grace",   // 2Approval
            "briar.sunset",   // 3Approval
            "celeste.dawn",   // 4Approval
            "daisy.skye"      // 5Approval
    };
    String[] approvalRoles = {
            "1Approval",
            "2Approval",
            "3Approval",
            "4Approval",
            "5Approval"
    };
    String submitterUser = "briar.sunset"; // user8 replaced
    String project = "New Project 5-Level-Schema";
    String startDate = "08/18/2025", endDate = "08/24/2025";
    String [] dateRanges = new String[2];
    Map<String , Map<String, Double>> projectActivityHours = new HashMap<>();
    Stack<List<String>> allHistory = new Stack<>();
    @Test(priority = 1, groups = {"Master","Regression"})
    public void testSubmitTimesheetOfBriarSunsetForNewProject(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        logger.info("Test Case 1: Verify user can submit timesheet for new project");

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

    @Test(priority = 2, groups = {"Master","Regression"}, dependsOnMethods = {"testSubmitTimesheetOfBriarSunsetForNewProject"})
    public void testRejectSubmittedTimesheetOfBriarSunset(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        HistoryApprovalPage historyApprovalPage = new HistoryApprovalPage(driver);
        logger.info("Test Case 2: Verify Rejection of Before Auto Approval");
        try{
            logger.info("Step 1: Logging in as "+ approvalUsers[3]);
            super.login(approvalUsers[3], "12345678");

            logger.info("Step 2: Navigate to project: " + project);
            headerPage.clickOnProjects();
            projectsPage.clickOnProjectName(project);

            logger.info("Step 3: Navigate to Timesheet Approval");
            headerPage.clickOnTimesheetApproval();

            logger.info("Step 4: Go to Date Range of Submission");
            timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

            logger.info("Step 5: Verify 'Design' and 'Development' Activity hours ");
            Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.submitterUser))),projectActivityHours.get(this.project).getOrDefault("Design",0.0));
            Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.submitterUser))),projectActivityHours.get(this.project).getOrDefault("Development",0.0));

            logger.info("Step 6: Reject timesheet for: "+this.submitterUser);
            timesheetApprovalPage.clickOnRejectBtn(toFullName(this.submitterUser));
            timesheetApprovalPage.setRejectionText("Rejected by --------- "+toFullName(approvalUsers[3]));
            timesheetApprovalPage.clickOnSubmitBtnOfRejection();

            Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.submitterUser)),"Rejected");

            //checking history
            logger.info("------ Clicking on show btn of : " + this.submitterUser + "Timesheet History");
            timesheetApprovalPage.clickOnShowHistoryOfUserTimesheet(toFullName(this.submitterUser));
            List<String> history = new ArrayList<>();
            history.add("Project Name : " +this.project);
            history.add("Status : Rejected");
            history.add("Level : Level 4 (" + this.approvalRoles[3]+")");
            history.add("Submitted By : " + toFullName(this.submitterUser));
            history.add("Rejected By : " + toFullName(this.approvalUsers[3]));
            history.add("Comment : Rejected by --------- "  + toFullName(this.approvalUsers[3]));
            this.allHistory.push(history);
            Assert.assertTrue(historyApprovalPage.getLastUpdatedHistory().containsAll(history));

            logger.info("Step 7: Logging out as "+ approvalUsers[3]);
            headerPage.clickOnLogout();

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }



    @Test(priority = 3, groups = {"Master", "Regression"}, dependsOnMethods = {"testRejectSubmittedTimesheetOfBriarSunset"})
    public void testAllHistoryOfApproval(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        HistoryApprovalPage historyApprovalPage = new HistoryApprovalPage(driver);
        try{
            logger.info("----- Verify That auto approval functionality");

            logger.info("---- Logging in as Admin");
            super.login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));

            logger.info("------ Navigating to project: " + this.project);
            headerPage.clickOnProjects();
            projectsPage.clickOnProjectName(this.project);

            logger.info("------ Navigating to Timesheet Approval");
            headerPage.clickOnTimesheetApproval();

            logger.info("------ Go to Date Range of Submission");
            timesheetApprovalPage.navigateToTargetDateRange(this.dateRanges[0], this.dateRanges[1]);

            Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.submitterUser)),"Rejected");

            logger.info("------ Clicking on show btn of : " + this.submitterUser + "Timesheet History");
            timesheetApprovalPage.clickOnShowHistoryOfUserTimesheet(toFullName(this.submitterUser));

            //checking histroy
            List<List<String>> changelogDetails = historyApprovalPage.getTimesheetApprovalDetails();
            Assert.assertTrue(changelogDetails.size() == this.allHistory.size());

            for(List<String> block : changelogDetails) {
                if(!this.allHistory.isEmpty()) {
                    List<String> history = this.allHistory.peek();
                    Assert.assertTrue(block.containsAll(history));
                    this.allHistory.pop();
                }
            }

            headerPage.clickOnLogout();

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }
}
