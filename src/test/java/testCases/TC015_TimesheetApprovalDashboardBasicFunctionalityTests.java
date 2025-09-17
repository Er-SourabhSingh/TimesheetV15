package testCases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pageObjects.HeaderPage;
import pageObjects.ProjectsPage;
import pageObjects.TimesheetApprovalPage;
import pageObjects.TimesheetPage;
import testBase.BaseClass;

import java.util.Arrays;
import java.util.List;

class TC015_TimesheetApprovalDashboardBasicFunctionalityTests extends BaseClass {

    String[] approvalUsers = new String[]{
            "aurora.wren", "autumn.grace", "briar.sunset", "celeste.dawn", "daisy.skye"
    };

    String[] users = new String[]{
            "opal.sparrow", "sage.willow", "selene.frost", "serenity.bloom",
            "summer.rain"
    };

    String[] projectNames = {
            "Project A Default-Schema",
            "Project B 1-Level-Schema",
            "Project C 2-Level-Schema",
            "Project D 3-Level-Schema",
            "Project E 4-Level-Schema",
            "Project F 5-Level-Schema"
    };

    @AfterMethod(alwaysRun = true)
    public void logOutAfterTest() {
        try {
            HeaderPage headerPage = new HeaderPage(driver);
            headerPage.clickOnLogout();
            logger.info("Logged out successfully");
        } catch (Exception e) {
            logger.warn("Logout skipped or failed: " + e.getMessage());
        }
    }

    @Test(priority = 1, groups = {"Master","Sanity"})
    public void testSearchFunctionalityAsAdmin(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        logger.info("Test Case 1: Verify the search functionality in current date range and " + this.projectNames[0] + " as admin");
        try{
            logger.info("step 1: login as admin");
            login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));

            logger.info("step 2: go to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("step 3: go to Timesheet Approval Dashboard module");
            timesheetPage.clickOnTimesheetApprovalDashboard();

            logger.info("step 4: search for " + this.users[0]);
            timesheetApprovalPage.setTextSearch(toFullName(this.users[0]));
            timesheetApprovalPage.clickEnterBtn();

            logger.info("step 5: select " + this.projectNames[0]);
            timesheetApprovalPage.selectProject(this.projectNames[0]);

            logger.info("step 6: Check Filter Data");
            List<String> filterUsers = timesheetApprovalPage.getAllUser();

            for(String user: filterUsers){
                Assert.assertTrue(user.equals(toFullName(this.users[0])));
            }


        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 2, groups = {"Master","Sanity"})
    public void testSearchFunctionalityAsAdminInPastWeek(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        logger.info("Test Case 2: Verify the search functionality in Past date range and " +this.projectNames[1] + "  as admin");
        try{
            logger.info("step 1: login as admin");
            login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));

            logger.info("step 2: go to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("step 3: go to Timesheet Approval Dashboard module");
            timesheetPage.clickOnTimesheetApprovalDashboard();

            logger.info("step 4: go to date Range 08 September 2025 - 14 September 2025");
            timesheetApprovalPage.navigateToTargetDateRange("8 Sep 25","14 Sep 25");

            logger.info("step 5: Search for " + this.users[1]);
            timesheetApprovalPage.setTextSearch(toFullName(this.users[1]));
            timesheetApprovalPage.clickEnterBtn();

            logger.info("step 6: select " + this.projectNames[1]);
            timesheetApprovalPage.selectProject(this.projectNames[1]);

            logger.info("step 6: Check Filter Data");
            List<String> filterUsers = timesheetApprovalPage.getAllUser();

            for(String user: filterUsers){
                Assert.assertTrue(user.equals(toFullName(this.users[1])));
            }

            logger.info("step 7: Check Date range after filtered data");
            boolean result = timesheetApprovalPage.verifyDateRange("8 Sep 25","14 Sep 25");

            Assert.assertTrue(result);


        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 3, groups = {"Master","Sanity"})
    public void testSearchFunctionalityDifferntProjectAsAdmin(){

        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        logger.info("Test Case 3: Verify the search functionality in current date range and accross different project as admin");
        try{
            logger.info("step 1: login as admin");
            login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));

            logger.info("step 2: go to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("step 3: go to Timesheet Approval Dashboard module");
            timesheetPage.clickOnTimesheetApprovalDashboard();

            logger.info("step 4: Search for " + this.users[3]);
            timesheetApprovalPage.setTextSearch(toFullName(this.users[3]));
            timesheetApprovalPage.clickEnterBtn();

            for(String project : this.projectNames) {
                logger.info("----- : select " + project);
                timesheetApprovalPage.selectProject(project);
                logger.info("----- : Check Filter Data");
                List<String> filterUsers = timesheetApprovalPage.getAllUser();

                for (String user : filterUsers) {
                    Assert.assertTrue(user.equals(toFullName(users[3])));
                }

            }


        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 4, groups = {"Master", "Sanity"})
    public void testProjectVisibilityForApprovalMemeber(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        logger.info("Test Case 4: Verify that an Approval member can see only projects in the project dropdown where they are a memeber");
        try{
            logger.info("step 1: login as Approval Memeber");
            login(approvalUsers[0], "12345678");

            logger.info("step 2: go to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("step 3: go to Timesheet Approval Dashboard module");
            timesheetPage.clickOnTimesheetApprovalDashboard();

            logger.info("step 4: Check project options");
            List<String> projectOptions = timesheetApprovalPage.getProjectOptions();

            for(String project : projectOptions){
                Assert.assertTrue(Arrays.asList(this.projectNames).contains(project));
            }


        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 5, groups = {"Master", "Sanity"})
    public void testSearchFunctioanlityAsApprovalMemeber(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        logger.info("Test Case 5: Verify that an Approval member can search user accross different project in current data range");
        try{
            logger.info("step 1: login as Approval Member");
            login(approvalUsers[0], "12345678");

            logger.info("step 2: go to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("step 3: go to Timesheet Approval Dashboard module");
            timesheetPage.clickOnTimesheetApprovalDashboard();

            logger.info("step 4: Search for " + this.users[3]);
            timesheetApprovalPage.setTextSearch(toFullName(this.users[3]));
            timesheetApprovalPage.clickEnterBtn();

            for(String project : this.projectNames){
                logger.info("----- : select " + project);
                timesheetApprovalPage.selectProject(project);
                logger.info("----- : Check Filter Data");
                List<String> filterUsers = timesheetApprovalPage.getAllUser();

                for (String user : filterUsers) {
                    Assert.assertTrue(user.equals(toFullName(users[3])));
                }
            }

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }


    @Test(priority = 6, groups = {"Master","Sanity"})
    public void testSearchFunctionalityAsApprovalInPastWeek(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        logger.info("Test Case 2: Verify the search functionality in Past date range and " +this.projectNames[1] + "  as Approval Member");
        try{
            logger.info("step 1: login as Approval Member");
            login(approvalUsers[1], "12345678");

            logger.info("step 2: go to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("step 3: go to Timesheet Approval Dashboard module");
            timesheetPage.clickOnTimesheetApprovalDashboard();

            logger.info("step 4: go to date Range 08 September 2025 - 14 September 2025");
            timesheetApprovalPage.navigateToTargetDateRange("8 Sep 25","14 Sep 25");

            logger.info("step 5: Search for " + this.users[1]);
            timesheetApprovalPage.setTextSearch(toFullName(this.users[1]));
            timesheetApprovalPage.clickEnterBtn();

            logger.info("step 6: select " + this.projectNames[1]);
            timesheetApprovalPage.selectProject(this.projectNames[1]);

            logger.info("step 6: Check Filter Data");
            List<String> filterUsers = timesheetApprovalPage.getAllUser();

            for(String user: filterUsers){
                Assert.assertTrue(user.equals(toFullName(this.users[1])));
            }

            logger.info("step 7: Check Date range after filtered data");
            boolean result = timesheetApprovalPage.verifyDateRange("8 Sep 25","14 Sep 25");

            Assert.assertTrue(result);

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 7, groups = {"Master","Sanity"})
    public void testNoUserFoundAsAdmin(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        logger.info("Test Case 2: Verify the search functionality in Past date range and " +this.projectNames[1] + "  as Approval Member");
        try{
            logger.info("step 1: login as admin");
            login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));

            logger.info("step 2: go to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("step 3: go to Timesheet Approval Dashboard module");
            timesheetPage.clickOnTimesheetApprovalDashboard();


            logger.info("step 4: Search for ABCDEF");
            timesheetApprovalPage.setTextSearch("ABCDEF");
            timesheetApprovalPage.clickEnterBtn();

            for(String project : this.projectNames){
                logger.info("----- : select " + project);
                timesheetApprovalPage.selectProject(project);
                logger.info("----- : Check no data to display");
                boolean result = timesheetApprovalPage.isNoDataDisplay();
                Assert.assertTrue(result);
            }



        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }
}
