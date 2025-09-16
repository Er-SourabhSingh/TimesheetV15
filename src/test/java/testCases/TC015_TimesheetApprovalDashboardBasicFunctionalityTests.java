package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.HeaderPage;
import pageObjects.ProjectsPage;
import pageObjects.TimesheetApprovalPage;
import pageObjects.TimesheetPage;
import testBase.BaseClass;

import java.util.List;

class TC015_TimesheetApprovalDashboardBasicFunctionalityTests extends BaseClass {
    String[] projects= new String[]{
            "Project A Default-Schema",
            "Project B 1-Level-Schema"
    };

    String[] users = new String[]{
            "opal.sparrow", "sage.willow", "selene.frost", "serenity.bloom",
            "summer.rain"
    };

    @Test(priority = 1, groups = {"Master","Sanity"})
    public void verifySearchFunctionalityAsAdmin(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        logger.info("Test Case 1: Verify the search functionality in current date range as admin");
        try{
            logger.info("step 1: login as admin");
            login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));

            logger.info("step 2: go to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("step 3: go to Timesheet Approval Dashboard module");
            timesheetPage.clickOnTimesheetApprovalDashboard();

            logger.info("step 4: Search for " + users[0]);
            timesheetApprovalPage.setTextSearch(toFullName(users[0]));
            timesheetApprovalPage.clickEnterBtn();

            logger.info("step 5: Check Filter Data");
            List<String> filterUsers = timesheetApprovalPage.getAllUser();

            for(String user: filterUsers){
                Assert.assertTrue(user.equals(toFullName(users[0])));
            }

            logger.info("step 6: Logging out as admin");
            headerPage.clickOnLogout();

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    @Test(priority = 2, groups = {"Master","Sanity"})
    public void verifySearchFunctionalityAsAdminInPastWeek(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalPage timesheetApprovalPage = new TimesheetApprovalPage(driver);
        logger.info("Test Case 1: Verify the search functionality in Past date range as admin");
        try{
            logger.info("step 1: login as admin");
            login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));

            logger.info("step 2: go to Timesheet module");
            headerPage.clickOnTimesheet();

            logger.info("step 3: go to Timesheet Approval Dashboard module");
            timesheetPage.clickOnTimesheetApprovalDashboard();

            logger.info("step 4: go to date Range 08 September 2025 - 14 September 2025");
            timesheetApprovalPage.navigateToTargetDateRange("8 Sep 25","14 Sep 25");

            logger.info("step 5: Search for " + users[1]);
            timesheetApprovalPage.setTextSearch(toFullName(users[1]));
            timesheetApprovalPage.clickEnterBtn();

            logger.info("step 6: Check Filter Data");
            List<String> filterUsers = timesheetApprovalPage.getAllUser();

            for(String user: filterUsers){
                Assert.assertTrue(user.equals(toFullName(users[1])));
            }

            logger.info("step 7: Check Date range after filtered data");
            boolean result = timesheetApprovalPage.verifyDateRange("8 Sep 25","14 Sep 25");

            Assert.assertTrue(result);

            logger.info("step 8: Logging out as admin");
            headerPage.clickOnLogout();

        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }




}
