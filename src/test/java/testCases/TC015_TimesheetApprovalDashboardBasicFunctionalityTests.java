package testCases;

import org.testng.annotations.Test;
import pageObjects.HeaderPage;
import pageObjects.ProjectsPage;
import pageObjects.TimesheetPage;
import testBase.BaseClass;

class TC015_TimesheetApprovalDashboardBasicFunctionalityTests extends BaseClass {
    @Test (priority = 1)
    public void verifySearchUser(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        try{

        }catch (Exception e){

        }

    }
}
