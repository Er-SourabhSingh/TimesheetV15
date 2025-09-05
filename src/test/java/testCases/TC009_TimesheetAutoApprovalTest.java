package testCases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObjects.*;
import testBase.BaseClass;

import java.sql.Time;
import java.util.*;

public class TC009_TimesheetAutoApprovalTest extends BaseClass {
    String[] approvalUsers = {
            "aurora.wren",    // 1Approval
            "autumn.grace",   // 2Approval
            "briar.sunset",   // 3Approval
            "celeste.dawn",   // 4Approval
            "daisy.skye"      // 5Approval
    };

    String project = "New Project 5-Level-Schema";

    String schema = "Schema 5 auto-approval";

    String[] approvalRoles = {
            "1Approval",
            "2Approval",
            "3Approval",
            "4Approval",
            "5Approval"
    };



    String submitterUser = "opal.sparrow"; // user8 replaced

    String startDate = "08/04/2025", endDate = "08/10/2025";
    String [] dateRanges = new String[2];
    Map<String , Map<String, Double>> projectActivityHours = new HashMap<>();

    @BeforeClass
    public void setupPreconditions() {
        HeaderPage headerPage = new HeaderPage(driver);
        AdministrationPage administrationPage = new AdministrationPage(driver);
        PluginsPage pluginsPage = new PluginsPage(driver);
        TimesheetConfiguration configuration = new TimesheetConfiguration(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        ProjectFormPage projectFormPage = new ProjectFormPage(driver);
        IssuePage issuePage = new IssuePage(driver);
        IssueFormPage issueFormPage = new IssueFormPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalSchema approvalSchema = new TimesheetApprovalSchema(driver);
        logger.info("Precondition: Create Approval schema and 5 levels, create new project with same schema, and add members.");
        try {

            logger.info("Step 1: Logging in as Admin");
            super.login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));
            logger.info("Step 2: Click on Timesheet Module");
            headerPage.clickOnTimesheet();

            logger.info("3: Click on timesheet module in header");
            headerPage.clickOnTimesheet();
            logger.info("4: Click on timesheet approval schema sub module inside Timesheet module");
            timesheetPage.clickOnTimesheetApprovalSchema();

            logger.info("5: Click on timesheet approval schema sub module inside Timesheet module");
            timesheetPage.clickOnTimesheetApprovalSchema();

            logger.info("6: Check that schema is already created");

            if(!approvalSchema.getSchemaList().contains(this.schema)) {
                logger.info("Click on add new schema btn");
                approvalSchema.clickOnAddNewSchemaBTn();
                logger.info("Write schema name");
                approvalSchema.setSchemaName(this.schema);
                logger.info("Click on Create schema");
                approvalSchema.clickOnCreateSchema();
            }

            logger.info("7: Open schema: " + this.schema);
            approvalSchema.clickOnSchemaToOpen(this.schema);
            Map<Integer,String> levels = new HashMap<>();
            levels.put(1, "Level One"); levels.put(2, "Level Two"); levels.put(3,"Level Three"); levels.put(4,"Level Four"); levels.put(5,"Level Five");

            for (int j = 1; j <= 5; j++) {
                if (!approvalSchema.getListOfLevels().contains(levels.get(j))) {
                    logger.info("Click on Add Approval level btn");
                    approvalSchema.clickOnAddApprovalLevelBtn();
                    logger.info("Set level name Level " + Integer.toString(j));
                    approvalSchema.setApprovalLevelName("Level " + Integer.toString(j));
                    logger.info("Select Approval Role: " + this.approvalRoles[j - 1]);
                    approvalSchema.selectApprovalRole(this.approvalRoles[j - 1]);
                    logger.info("Select Approval Level: " + Integer.toString(j));
                    approvalSchema.selectApprovalLevel(Integer.toString(j));
                    logger.info("Check Auto Approval");
                    approvalSchema.checkCheckboxAutoApproval();
                    logger.info("Click on Create Approval Level");
                    approvalSchema.clickOnApprovalLevelCreate();
                }
            }

            logger.info("8: go back page of approval schema level");
            driver.navigate().back();


            logger.info("9: Create project: " + this.project + " and add members");
            headerPage.clickOnProjects();
            if(!projectsPage.getProjectsName().contains(this.project)){
                projectsPage.clickOnAddProjectBtn();
                projectFormPage.setTxtProjectName(this.project);
                projectFormPage.selectTimesheetSchema(this.schema);
                projectFormPage.checkCheckBoxTimesheetApproval();
                projectFormPage.clickOnCreate();

                projectFormPage.clickOnMemberTab();
                if(!projectFormPage.getMembersList().containsAll(Arrays.asList(this.approvalUsers))) {
                    for (int i = 0; i < this.approvalUsers.length; i++) {
                        projectFormPage.clickOnNewMemberBtn();
                        projectFormPage.selectMember(toFullName(this.approvalUsers[i]));
                        projectFormPage.selectRole(this.approvalRoles[i]);
                        projectFormPage.clickOnAddBtnOfMember();
                    }
                }
                if(!projectFormPage.getMembersList().contains(this.submitterUser)) {
                    projectFormPage.clickOnNewMemberBtn();
                    projectFormPage.selectMember(toFullName(this.submitterUser));
                    projectFormPage.selectRole("Developer");
                    projectFormPage.clickOnAddBtnOfMember();
                }


                headerPage.clickOnIssues();

                if(!issuePage.getSubjectNamesOfIssuesList().contains("Test")) {
                    issuePage.clickOnNewIssueBtn();
                    issueFormPage.setTxtSubject("Test");
                    issueFormPage.clickOnCreateBtn();
                }
            }

            logger.info("9: Set time for Auto approval");
            headerPage.clickOnAdministrator();
            administrationPage.clickOnPlugins();
            pluginsPage.clickOnConfigureOfTimesheetPlugin();
            configuration.setTxtAutoApprovalTime("0.03");
            configuration.clickOnApplyBtn();

            logger.info("Step 10: Logging out");
            headerPage.clickOnLogout();


        } catch (Exception e) {
            logger.error("Setup failed: " + e.getMessage());
            Assert.fail();
        }
    }

    @Test(priority = 1, groups = {"Sanity", "Master", "Regression"})
    public void testSubmitTimesheetForNewProject(){
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

    @Test(priority = 2, groups = {"Master", "Regression"}, dependsOnMethods = {"testSubmitTimesheetForNewProject"})
    public void testAutoApproval(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        HistoryApprovalPage historyApprovalPage = new HistoryApprovalPage(driver);
        try{
            logger.info("----- Verify That Approver can Approve Timesheet Project F");
            for(int i = 0; i < approvalUsers.length; i++) {
                Thread.sleep(150000); // 120,000 ms = 2 minutes
                logger.info("---- Logging in as Approver:" + approvalUsers[i]);
                super.login(approvalUsers[i], "12345678");

                logger.info("------ Navigating to project: " + this.project);
                headerPage.clickOnProjects();
                projectsPage.clickOnProjectName(this.project);

                logger.info("------ Navigating to Timesheet Approval");
                headerPage.clickOnTimesheetApproval();

                logger.info("------ Go to Date Range of Submission");
                timesheetApprovalPage.navigateToTargetDateRange(dateRanges[0], dateRanges[1]);

                logger.info("------ Verify 'Design' and 'Development' Activity hours ");
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDesignHoursOfUser(toFullName(this.submitterUser))), projectActivityHours.get(this.project).getOrDefault("Design",0.0));
                Assert.assertEquals(convertTimeToDecimal(timesheetApprovalPage.getDevelopmentHoursOfUser(toFullName(this.submitterUser))), projectActivityHours.get(this.project).getOrDefault("Development",0.0));

                Assert.assertEquals(timesheetApprovalPage.getStatusValueOfUserTimesheet(toFullName(this.submitterUser)),"Approved");

                //checking histroy
                logger.info("------ Clicking on show btn of : " + this.submitterUser + "Timesheet History");
                timesheetApprovalPage.clickOnShowHistoryOfUserTimesheet(toFullName(this.submitterUser));
                List<String> history = new ArrayList<>();
                history.add("Project Name : " +this.project);
                history.add("Status : Approved");
                history.add("Submitted By : " + toFullName(this.submitterUser));
                history.add("Approved By : Auto Approved" );
                history.add("Approval Comment : Auto Approved");
                Assert.assertTrue(historyApprovalPage.getLastUpdatedHistory().containsAll(history));

                logger.info("------ Logging out approver: " + approvalUsers[i]);
                headerPage.clickOnLogout();


                logger.info("------ Logging in as "+this.submitterUser);
                super.login(this.submitterUser,"12345678");

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

                if(i <= 3 && !(i < 0)){
                    logger.info("------ Verifying that the " + this.project + " status is 'Waiting for Approval' after approval at level " + (i+1));
                    Assert.assertTrue(timesheetPage.hasPendingTimesheet(this.project));
                    Assert.assertEquals(timesheetPage.getSubmitTimesheetBtnText(),"Waiting for Approval");
                }
                if(i==4){
                    logger.info("------ Verifying that the " + this.project + " status is 'Approved' after approval at level" + (i+1));
                    Assert.assertTrue(timesheetPage.hasApprovedTimesheet(this.project));
                }

                logger.info("------ Logging out "+this.submitterUser);
                headerPage.clickOnLogout();
            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }
}
