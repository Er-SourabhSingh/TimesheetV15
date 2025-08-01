package testBase;

import org.testng.Assert;
import org.testng.annotations.*;
import pageObjects.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class SetupPreconditions extends BaseClass{
    String []approvalRoles = new String[]{"1Approval","2Approval","3Approval","4Approval","5Approval"};
    String []userRoles = new String[]{"Employee","Developer","Support"};
    String [] approvalUsers = new String[]{"1Approval","2Approval","3Approval","4Approval","5Approval"};
    String []users = new String[]{"user1","user2","user3","user4","user5","user6","user7","user8"};
    String []schemas = new String[]{"1 Level Schema", "2 Level Schema", "3 Level Schema", "4 Level Schema", "5 Level Schema"};
    String []projects = new String[]{"Project A Default-Schema", "Project B 1-Level-Schema", "Project C 2-Level-Schema", "Project D 3-Level-Schema"
                                        ,"Project E 4-Level-Schema", "Project F 5-Level-Schema"};
    @BeforeTest (groups = {"Sanity"})
    @Parameters({"os","browser"})
    public void setUpUserAndRoles(String os, String br) throws IOException{
        super.launch(os,br);
        super.login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));
        this.createRoles();
        this.createUsers();
        this.enabledIssueLogTimeForOtherUser();
        this.createSchemas();
        this.createLevelInsideSchemas();
        this.createDummyProjectsAndSelectSchema();
        this.selectMemberAndTheirRoleInsideProject();
        this.createDummyIssues();
        driver.quit();
    }

    public void createRoles(){
        HeaderPage headerPage = new HeaderPage(driver);
        AdministrationPage administrationPage = new AdministrationPage(driver);
        RolesAndPermissionsPage rolesAndPermissionsPage = new RolesAndPermissionsPage(driver);
        RoleFormPage roleFormPage = new RoleFormPage(driver);

        try{

            headerPage.clickOnAdministrator();
            administrationPage.clickOnRolesAndPermissions();

            for(String r : this.approvalRoles){
                if(rolesAndPermissionsPage.getRoleList().containsAll(Arrays.asList(this.approvalRoles)))
                    break;

                if(!rolesAndPermissionsPage.getRoleList().contains(r)) {
                    rolesAndPermissionsPage.clickCreateRoleBtn();
                    roleFormPage.setTxtRoleName(r);
                    roleFormPage.selectWorkflow();
                    roleFormPage.clickOnRightBtnOfProjectField();
                    roleFormPage.clickOnRightBtnOfIssueTrackingField();
                    roleFormPage.clickOnRightBtnOfTimeTrackingField();
                    roleFormPage.clickOnRightBtnTimesheetApproval();
                    roleFormPage.clickOnRightBtnTracker();
                    roleFormPage.clickOnCreateBtn();
                }
            }

            for(String r : this.userRoles){
                if(rolesAndPermissionsPage.getRoleList().containsAll(Arrays.asList(this.userRoles)))
                    break;

                if(!rolesAndPermissionsPage.getRoleList().contains(r)) {
                    rolesAndPermissionsPage.clickCreateRoleBtn();
                    roleFormPage.setTxtRoleName(r);
                    roleFormPage.selectWorkflow();
                    roleFormPage.clickOnRightBtnOfProjectField();
                    //roleFormPage.uncheckManageTimesheet();
                    roleFormPage.clickOnRightBtnOfIssueTrackingField();
                    roleFormPage.clickOnRightBtnOfTimeTrackingField();
                    roleFormPage.clickOnRightBtnTracker();
                    roleFormPage.clickOnCreateBtn();
                }
            }

        } catch (Exception e) {
            logger.error(e);
            Assert.fail();
        }
    }

    public void createUsers(){
        HeaderPage headerPage = new HeaderPage(driver);
        AdministrationPage administrationPage = new AdministrationPage(driver);
        UsersPage usersPage = new UsersPage(driver);
        UserFormPage userFormPage = new UserFormPage(driver);

        try{
            headerPage.clickOnAdministrator();
            administrationPage.clickOnUsers();

            for(String user : this.approvalUsers){
                if(usersPage.getUserList().containsAll(Arrays.asList(this.approvalUsers)))
                    break;

                if(!usersPage.getUserList().contains(user)) {
                    usersPage.clickOnAddUserBtn();
                    userFormPage.setTxtUserLoginName(user);
                    userFormPage.setTxtUserFirstName(user);
                    userFormPage.setTxtUserLastName(user);
                    userFormPage.setTxtUserEmail(user+"@gmail.com");
                    userFormPage.setTxtUserPassword("12345678");
                    userFormPage.setTxtConfirmationPassword("12345678");
                    userFormPage.clickOnCreate();
                    userFormPage.clickOnUsersPage();
                }
            }

            for(String user : this.users){
                if(usersPage.getUserList().containsAll(Arrays.asList(this.users)))
                    break;

                if(!usersPage.getUserList().contains(user)) {
                    usersPage.clickOnAddUserBtn();
                    userFormPage.setTxtUserLoginName(user);
                    userFormPage.setTxtUserFirstName(user);
                    userFormPage.setTxtUserLastName(user);
                    userFormPage.setTxtUserEmail(user+"@gmail.com");
                    userFormPage.setTxtUserPassword("12345678");
                    userFormPage.setTxtConfirmationPassword("12345678");
                    userFormPage.clickOnCreate();
                    userFormPage.clickOnUsersPage();
                }
            }

        } catch (Exception e) {
            logger.error(e);
            Assert.fail();
        }
    }

    public void enabledIssueLogTimeForOtherUser(){
        HeaderPage headerPage = new HeaderPage(driver);
        AdministrationPage administrationPage = new AdministrationPage(driver);
        PluginsPage pluginsPage = new PluginsPage(driver);
        TimesheetConfiguration configuration = new TimesheetConfiguration(driver);

        try {
            headerPage.clickOnAdministrator();
            administrationPage.clickOnPlugins();
            pluginsPage.clickOnConfigureOfTimesheetPlugin();
            configuration.checkCheckboxUnassignedUser();
            configuration.clickOnApplyBtn();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSchemas(){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalSchema approvalSchema = new TimesheetApprovalSchema(driver);
        logger.info("----- Create schemas -----");
        try {
            logger.info("1: Click on timesheet module in header");
            headerPage.clickOnTimesheet();
            logger.info("2: Click on timesheet approval schema sub module inside Timesheet module");
            timesheetPage.clickOnTimesheetApprovalSchema();
            for(String schema : this.schemas) {
                logger.info("3: Check that schema is alrady created");
                System.out.println(approvalSchema.getSchemaList());
                if(approvalSchema.getSchemaList().containsAll(Arrays.asList(this.schemas)))
                    break;
                if(!approvalSchema.getSchemaList().contains(schema)) {
                    logger.info("Click on add new schema btn");
                    approvalSchema.clickOnAddNewSchemaBTn();
                    logger.info("Write schema name");
                    approvalSchema.setSchemaName(schema);
                    logger.info("Click on Create schema");
                    approvalSchema.clickOnCreateSchema();
                }
            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    public void createLevelInsideSchemas(){
        Map<Integer,String> levels = new HashMap<>();
        levels.put(1, "Level One"); levels.put(2, "Level Two"); levels.put(3,"Level Three"); levels.put(4,"Level Four"); levels.put(5,"Level Five");
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalSchema approvalSchema = new TimesheetApprovalSchema(driver);
        logger.info("----- Create levels inside schema -----");
        try {
            for(int i=1; i<=this.schemas.length; i++) {
                logger.info("Open schema "+schemas[i-1]);
                approvalSchema.clickOnSchemaToOpen(schemas[i-1]);
                for(int j=1; j<=i; j++){
                    if(approvalSchema.getListOfLevels().contains(levels.get(i)))
                        break;
                    if(!approvalSchema.getListOfLevels().contains(levels.get(j))) {
                        logger.info("Click on Add Approval level btn");
                        approvalSchema.clickOnAddApprovalLevelBtn();
                        logger.info("Set level name Level " + Integer.toString(j));
                        approvalSchema.setApprovalLevelName("Level " + Integer.toString(j));
                        logger.info("Select Approval Role: " + this.approvalRoles[j - 1]);
                        approvalSchema.selectApprovalRole(this.approvalRoles[j - 1]);
                        logger.info("Select Approval Level: " + Integer.toString(j));
                        approvalSchema.selectApprovalLevel(Integer.toString(j));
                        logger.info("Click on Create Approval Level");
                        approvalSchema.clickOnApprovalLevelCreate();
                    }
                }
                logger.info("go back page of approval schema level");
                driver.navigate().back();
            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    public void createDummyProjectsAndSelectSchema(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        ProjectFormPage projectFormPage = new ProjectFormPage(driver);
        try{
            int i = 0;
            headerPage.clickOnProjects();
            if(!projectsPage.getProjectsName().containsAll(Arrays.asList(this.projects))){
                projectsPage.clickOnAddProjectBtn();
                for(String project : this.projects){
                    projectFormPage.setTxtProjectName(project);
                    if(project.equals("Project A Default-Schema")) {
                        projectFormPage.selectTimesheetSchema("Default Approval Schema");
                    }else {
                        projectFormPage.selectTimesheetSchema(this.schemas[i++]);
                    }
                    projectFormPage.checkCheckBoxTimesheetApproval();
                    projectFormPage.clickOnCreateAndAddAnother();
                }
            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    public void selectMemberAndTheirRoleInsideProject(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        ProjectFormPage projectFormPage = new ProjectFormPage(driver);
        try{
            headerPage.clickOnProjects();
            for(String project : this.projects){
                projectsPage.clickOnProjectName(project);
                headerPage.clickOnProjectSetting();
                projectFormPage.clickOnMemberTab();
                if(!projectFormPage.getMembersList().containsAll(Arrays.asList(this.approvalUsers))) {
                    for (int i = 0; i < this.approvalUsers.length; i++) {
                        projectFormPage.clickOnNewMemberBtn();
                        projectFormPage.selectMember(this.approvalUsers[i]);
                        projectFormPage.selectRole(this.approvalRoles[i]);
                        projectFormPage.clickOnAddBtnOfMember();
                    }
                }
                if(!projectFormPage.getMembersList().containsAll(Arrays.asList(this.users))) {
                    projectFormPage.clickOnNewMemberBtn();
                    for (int i = 0; i < users.length; i++) {
                        projectFormPage.selectMember(users[i]);
                    }
                    projectFormPage.selectRole("Developer");
                    projectFormPage.clickOnAddBtnOfMember();
                }

                if(!projectFormPage.getMembersList().contains("Redmine Admin")){
                    projectFormPage.clickOnNewMemberBtn();
                    projectFormPage.selectMember("Redmine Admin");
                    projectFormPage.selectRole("Manager");
                    projectFormPage.clickOnAddBtnOfMember();
                    headerPage.clickOnProjects();
                }
            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    public void createDummyIssues(){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        IssuePage issuePage = new IssuePage(driver);
        IssueFormPage issueFormPage = new IssueFormPage(driver);
        try{
            headerPage.clickOnProjects();

            for(String project : this.projects){
                projectsPage.clickOnProjectName(project);
                headerPage.clickOnIssues();

                if(!issuePage.getSubjectNamesOfIssuesList().contains("Test")) {
                    issuePage.clickOnNewIssueBtn();
                    issueFormPage.setTxtSubject("Test");
                    issueFormPage.clickOnCreateBtn();
                }

                headerPage.clickOnProjects();
            }

        } catch (Exception e) {
            logger.error(e);
            Assert.fail();
        }
    }

}
