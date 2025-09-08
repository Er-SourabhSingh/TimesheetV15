package testBase;

import org.testng.Assert;
import org.testng.annotations.*;
import pageObjects.*;

import java.io.IOException;
import java.util.*;


public class


SetupPreconditions extends BaseClass{



    @BeforeTest (groups = {"Sanity","Master","Regression"})
    @Parameters({"os", "browser", "approvalFlow"})
    public void preconditions(String os, String br, String flowType) throws IOException{
        super.launch(os,br);
        if(flowType != null) {
            switch(flowType.toLowerCase()) {
                case "manual":
                    setupManualFlow();
                    break;
                case "sameschema":
                    setupTwoProjectsSameSchema();
                    break;
               /* case "auto":
                    setupAutoFlow();
                    break;*/
                default:
                    throw new IllegalArgumentException("Unknown flow type: " + flowType);
            }
        }
    }


    private void setupManualFlow() {
        System.out.println("Running Manual Flow Preconditions...");
        String []approvalRoles = new String[]{"1Approval","2Approval","3Approval","4Approval","5Approval"};
        String []userRoles = new String[]{"Employee","Developer","Support"};

        String[] approvalUsers = new String[]{
                "aurora.wren", "autumn.grace", "briar.sunset", "celeste.dawn", "daisy.skye"
        };

        String[] users = new String[]{
                "ember.lilac", "harmony.rose", "isla.moon", "ivy.skylark",
                "luna.meadow", "marigold.rayne", "nova.starling",
                "opal.sparrow", "sage.willow", "selene.frost", "serenity.bloom",
                "summer.rain"
        };

        String []schemas = new String[]{"1 Level Schema", "2 Level Schema", "3 Level Schema", "4 Level Schema", "5 Level Schema"};
        String []projects = new String[]{"Project A Default-Schema", "Project B 1-Level-Schema", "Project C 2-Level-Schema", "Project D 3-Level-Schema"
                ,"Project E 4-Level-Schema", "Project F 5-Level-Schema"};

        super.login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));
        this.createRoles(approvalRoles, userRoles);
        this.createUsers(approvalUsers, users);
        this.enabledIssueLogTimeForOtherUser();
        this.createSchemas(schemas);
        this.createLevelInsideSchemas(schemas,approvalRoles);
        this.createDummyProjectsAndSelectSchema(projects,schemas);
        this.selectMemberAndTheirRoleInsideProject(projects, approvalUsers, approvalRoles, users);
        this.createDummyIssues(projects);
        driver.quit();
    }

    private void createRoles(String[] approvalRoles, String[] userRoles){
        HeaderPage headerPage = new HeaderPage(driver);
        AdministrationPage administrationPage = new AdministrationPage(driver);
        RolesAndPermissionsPage rolesAndPermissionsPage = new RolesAndPermissionsPage(driver);
        RoleFormPage roleFormPage = new RoleFormPage(driver);

        try{

            headerPage.clickOnAdministrator();
            administrationPage.clickOnRolesAndPermissions();

            for(String r : approvalRoles){
                if(rolesAndPermissionsPage.getRoleList().containsAll(Arrays.asList(approvalRoles)))
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

            for(String r : userRoles){
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
                }else{
                    rolesAndPermissionsPage.clickOnRoleName(r);
                    roleFormPage.checkViewTimesheetCheckBox();
                    roleFormPage.checkViewIssueCheckbox();
                    roleFormPage.checkAddIssueCheckbox();
                    roleFormPage.checkEditIssueCheckbox();
                    roleFormPage.checkViewSpentTimeCheckbox();
                    roleFormPage.checkLogSpentTimeCheckbox();
                    roleFormPage.checkEditSpentTimeCheckbox();
                    roleFormPage.checkViewIssuesAllTrackerCheckBox();
                    roleFormPage.checkAddIssuesAllTrackerCheckBox();
                    roleFormPage.checkEditIssuesAllTrackerCheckBox();
                    roleFormPage.clickOnCreateBtn();
                }
            }

        } catch (Exception e) {
            logger.error(e);
            Assert.fail();
        }
    }

    private void createUsers(String[] approvalUsers, String[] users){
        HeaderPage headerPage = new HeaderPage(driver);
        AdministrationPage administrationPage = new AdministrationPage(driver);
        UsersPage usersPage = new UsersPage(driver);
        UserFormPage userFormPage = new UserFormPage(driver);

        try{
            headerPage.clickOnAdministrator();
            administrationPage.clickOnUsers();

            for(String user : approvalUsers){
                if(usersPage.getUserList().containsAll(Arrays.asList(approvalUsers)))
                    break;

                if(!usersPage.getUserList().contains(user)) {
                    usersPage.clickOnAddUserBtn();
                    userFormPage.setTxtUserLoginName(user);
                    userFormPage.setTxtUserFirstName(capitalize(user.split("\\.")[0]));
                    userFormPage.setTxtUserLastName(capitalize(user.split("\\.")[1]));
                    userFormPage.setTxtUserEmail(user + "@zehntech.com");
                    userFormPage.setTxtUserPassword("12345678");
                    userFormPage.setTxtConfirmationPassword("12345678");
                    userFormPage.clickOnCreate();
                    userFormPage.clickOnUsersPage();
                }
            }

            for(String user : users){
                if(usersPage.getUserList().containsAll(Arrays.asList(users)))
                    break;

                if(!usersPage.getUserList().contains(user)) {
                    usersPage.clickOnAddUserBtn();
                    userFormPage.setTxtUserLoginName(user);
                    userFormPage.setTxtUserFirstName(capitalize(user.split("\\.")[0]));
                    userFormPage.setTxtUserLastName(capitalize(user.split("\\.")[1]));
                    userFormPage.setTxtUserEmail(user + "@zehntech.com");
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

    private void enabledIssueLogTimeForOtherUser(){
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

    private void createSchemas(String[] schemas){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalSchema approvalSchema = new TimesheetApprovalSchema(driver);
        logger.info("----- Create schemas -----");
        try {
            logger.info("1: Click on timesheet module in header");
            headerPage.clickOnTimesheet();
            logger.info("2: Click on timesheet approval schema sub module inside Timesheet module");
            timesheetPage.clickOnTimesheetApprovalSchema();
            for(String schema : schemas) {
                logger.info("3: Check that schema is already created");
                if(approvalSchema.getSchemaList().containsAll(Arrays.asList(schemas)))
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

    private void createLevelInsideSchemas(String[] schemas, String[] approvalRoles){
        Map<Integer,String> levels = new HashMap<>();
        levels.put(1, "Level One"); levels.put(2, "Level Two"); levels.put(3,"Level Three"); levels.put(4,"Level Four"); levels.put(5,"Level Five");
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalSchema approvalSchema = new TimesheetApprovalSchema(driver);
        logger.info("----- Create levels inside schema -----");
        try {
            for(int i=1; i<=schemas.length; i++) {
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
                        logger.info("Select Approval Role: " + approvalRoles[j - 1]);
                        approvalSchema.selectApprovalRole(approvalRoles[j - 1]);
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

    private void createDummyProjectsAndSelectSchema(String[] projects, String[] schemas){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        ProjectFormPage projectFormPage = new ProjectFormPage(driver);
        try{
            int i = 0;
            headerPage.clickOnProjects();


            for (String project : projects) {
                if(!projectsPage.getProjectsName().contains(project)){
                    projectsPage.clickOnAddProjectBtn();
                    projectFormPage.setTxtProjectName(project);
                    if (project.equals("Project A Default-Schema")) {
                        projectFormPage.selectTimesheetSchema("Default Approval Schema");
                    } else {
                        projectFormPage.selectTimesheetSchema(schemas[i++]);
                    }
                    projectFormPage.checkCheckBoxTimesheetApproval();
                    projectFormPage.clickOnCreate();
                    headerPage.clickOnProjects();
                }
            }


        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    private void selectMemberAndTheirRoleInsideProject(String[] projects, String[] approvalUsers, String[] approvalRoles, String[] users){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        ProjectFormPage projectFormPage = new ProjectFormPage(driver);
        try{
            headerPage.clickOnProjects();
            for(String project : projects){
                projectsPage.clickOnProjectName(project);
                headerPage.clickOnProjectSetting();
                projectFormPage.clickOnMemberTab();
                List<String> approvalUsersList = super.toFullNames(approvalUsers);
                if(!projectFormPage.getMembersList().containsAll(approvalUsersList)) {
                    for (int i = 0; i < approvalUsers.length; i++) {
                        projectFormPage.clickOnNewMemberBtn();
                        projectFormPage.selectMember(capitalize(approvalUsers[i].split("\\.")[0]) + " " + capitalize(approvalUsers[i].split("\\.")[1]));
                        projectFormPage.selectRole(approvalRoles[i]);
                        projectFormPage.clickOnAddBtnOfMember();
                    }
                }

                List<String> usersList = this.toFullNames(users);
                if(!projectFormPage.getMembersList().containsAll(usersList)) {
                    projectFormPage.clickOnNewMemberBtn();
                    for (int i = 0; i < users.length; i++) {
                        projectFormPage.selectMember(capitalize(users[i].split("\\.")[0]) + " " + capitalize(users[i].split("\\.")[1]));
                    }
                    projectFormPage.selectRole("Developer");
                    projectFormPage.clickOnAddBtnOfMember();
                }

                if(!projectFormPage.getMembersList().contains("Redmine Admin")){
                    projectFormPage.clickOnNewMemberBtn();
                    projectFormPage.selectMember("Redmine Admin");
                    projectFormPage.selectRole("Manager");
                    projectFormPage.clickOnAddBtnOfMember();
                }
                headerPage.clickOnProjects();
            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    private void createDummyIssues(String[] projects){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        IssuePage issuePage = new IssuePage(driver);
        IssueFormPage issueFormPage = new IssueFormPage(driver);
        try{
            headerPage.clickOnProjects();

            for(String project : projects){
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

    private void setupTwoProjectsSameSchema(){
        String[] newApprovalUsers = {
                "ivy.skylark",    // New 1ApproverNew
                "luna.meadow",    // New 2ApproverNew
                "marigold.rayne"  // New 3ApproverNew
        };

        /*String[] approvalUsers = {
                "aurora.wren",    // 1Approval
                "autumn.grace",   // 2Approval
                "briar.sunset",   // 3Approval
                "celeste.dawn",   // 4Approval
                "daisy.skye"      // 5Approval
        };*/

        String projects[] = {"New Project 3-Level-Schema"};
        String Schema = "3 Level Schema";

        String[] approvalRoles = {
                "1Approval",
                "2Approval",
                "3Approval",
                "4Approval",
                "5Approval"
        };

        String[] users = {
                "ember.lilac",
                "harmony.rose",
                "isla.moon",
                "nova.starling",
                "opal.sparrow",
                "sage.willow",
                "selene.frost",
                "serenity.bloom",
                "summer.rain"
        };

        String []schemas = new String[]{"3 Level Schema"};

        String []userRoles = new String[]{"Employee","Developer","Support"};

        super.login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));
        this.createRoles(approvalRoles, userRoles);
        this.createUsers(newApprovalUsers, users);
        this.enabledIssueLogTimeForOtherUser();
        this.createSchemas(schemas);
        this.createLevelInsideSchemas(schemas,approvalRoles);
        this.createDummyProjectsAndSelectSchema(projects,schemas);
        this.selectMemberAndTheirRoleInsideProject(projects, newApprovalUsers, approvalRoles, users);
        this.createDummyIssues(projects);
        driver.quit();
    }


}
