package testBase;

import org.testng.Assert;
import org.testng.annotations.*;
import pageObjects.*;

import java.io.IOException;
import java.util.*;


public class


SetupPreconditions extends BaseClass{

    @BeforeTest (groups = {"Sanity","Master","Regression"},alwaysRun = true)
    @Parameters({"os", "browser", "approvalflow"})
    public void setupPreconditions(String os, String br, String flowType) throws IOException{
        super.launch(os,br);
        if(flowType != null) {
            switch(flowType.toLowerCase()) {
                case "manual":
                    setupManualFlow();
                    break;
                case "sameschema":
                    setupTwoProjectsSameSchema();
                    break;
                case "auto":
                    setupAutoFlow();
                    break;
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

        LinkedHashMap<String, Integer> schemaLevelMap = new LinkedHashMap<>(); // Keeps insertion order
        schemaLevelMap.put("1 Level Schema", 1);
        schemaLevelMap.put("2 Level Schema", 2);
        schemaLevelMap.put("3 Level Schema", 3);
        schemaLevelMap.put("4 Level Schema", 4);
        schemaLevelMap.put("5 Level Schema", 5);

        LinkedHashMap<String, String> projectSchemaMap = new LinkedHashMap<>();
        projectSchemaMap.put("Project A Default-Schema", "Default Approval Schema");
        projectSchemaMap.put("Project B 1-Level-Schema", "1 Level Schema");
        projectSchemaMap.put("Project C 2-Level-Schema", "2 Level Schema");
        projectSchemaMap.put("Project D 3-Level-Schema", "3 Level Schema");
        projectSchemaMap.put("Project E 4-Level-Schema", "4 Level Schema");
        projectSchemaMap.put("Project F 5-Level-Schema", "5 Level Schema");

        super.login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));
        this.createRoles(approvalRoles, userRoles);
        this.createUsers(approvalUsers, users);
        this.enabledIssueLogTimeForOtherUser();
        this.createSchemas(schemaLevelMap);
        this.createLevelInsideSchemas(schemaLevelMap,approvalRoles);
        this.createDummyProjectsAndSelectSchema(projectSchemaMap);
        this.selectMemberAndTheirRoleInsideProject(projectSchemaMap, approvalUsers, approvalRoles, users);
        this.createDummyIssues(projectSchemaMap);
        driver.quit();
    }

    private void setupTwoProjectsSameSchema(){
        String[] newApprovalUsers = {
                "ivy.skylark",    // New 1ApproverNew
                "luna.meadow",    // New 2ApproverNew
                "marigold.rayne"  // New 3ApproverNew
        };

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

        String []userRoles = new String[]{"Employee","Developer","Support"};

        LinkedHashMap<String, Integer> schemaLevelMap = new LinkedHashMap<>(); // Keeps insertion order
        schemaLevelMap.put("3 Level Schema", 3);

        LinkedHashMap<String, String> projectSchemaMap = new LinkedHashMap<>();
        projectSchemaMap.put("New Project 3-Level-Schema", "3 Level Schema");
        projectSchemaMap.put("Project D 3-Level-Schema", "3 Level Schema");

        super.login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));
        this.createRoles(approvalRoles, userRoles);
        this.createUsers(newApprovalUsers, users);
        this.enabledIssueLogTimeForOtherUser();
        this.createSchemas(schemaLevelMap);
        this.createLevelInsideSchemas(schemaLevelMap,approvalRoles);
        this.createDummyProjectsAndSelectSchema(projectSchemaMap);
        this.selectMemberAndTheirRoleInsideProject(projectSchemaMap, newApprovalUsers, approvalRoles, users);
        this.createDummyIssues(projectSchemaMap);
        driver.quit();
    }

    private void setupAutoFlow(){
        String[] approvalUsers = {
                "aurora.wren",    // 1Approval
                "autumn.grace",   // 2Approval
                "briar.sunset",   // 3Approval
                "celeste.dawn",   // 4Approval
                "daisy.skye"      // 5Approval
        };

        LinkedHashMap<String, Integer> schemaLevelMap = new LinkedHashMap<>(); // Keeps insertion order
        schemaLevelMap.put("Schema 5 auto-approval", 5);

        LinkedHashMap<String, String> projectSchemaMap = new LinkedHashMap<>();
        projectSchemaMap.put("New Project 5-Level-Schema", "Schema 5 auto-approval");


        String[] approvalRoles = {
                "1Approval",
                "2Approval",
                "3Approval",
                "4Approval",
                "5Approval"
        };

        String[] users = new String[]{
                "ember.lilac", "harmony.rose", "isla.moon", "ivy.skylark",
                "luna.meadow", "marigold.rayne", "nova.starling",
                "opal.sparrow", "sage.willow", "selene.frost", "serenity.bloom",
                "summer.rain"
        };

        String []userRoles = new String[]{"Employee","Developer","Support"};

        super.login(properties.getProperty("adminUser"), properties.getProperty("adminPassword"));
        this.createRoles(approvalRoles, userRoles);
        this.createUsers(approvalUsers, users);
        this.enabledIssueLogTimeForOtherUser();
        this.setAutApprovalTime("0.03");
        this.createSchemas(schemaLevelMap);
        this.createLevelInsideSchemas(schemaLevelMap,approvalRoles);
        this.createDummyProjectsAndSelectSchema(projectSchemaMap);
        this.selectMemberAndTheirRoleInsideProject(projectSchemaMap, approvalUsers, approvalRoles, users);
        this.createDummyIssues(projectSchemaMap);
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

    private void setAutApprovalTime(String time){
        HeaderPage headerPage = new HeaderPage(driver);
        AdministrationPage administrationPage = new AdministrationPage(driver);
        PluginsPage pluginsPage = new PluginsPage(driver);
        TimesheetConfiguration configuration = new TimesheetConfiguration(driver);

        try {
            headerPage.clickOnAdministrator();
            administrationPage.clickOnPlugins();
            pluginsPage.clickOnConfigureOfTimesheetPlugin();
            configuration.setTxtAutoApprovalTime(time);
            configuration.clickOnApplyBtn();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void createSchemas(LinkedHashMap<String, Integer> schemas){
        HeaderPage headerPage = new HeaderPage(driver);
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalSchema approvalSchema = new TimesheetApprovalSchema(driver);
        logger.info("----- Create schemas -----");
        try {
            logger.info("1: Click on timesheet module in header");
            headerPage.clickOnTimesheet();
            logger.info("2: Click on timesheet approval schema sub module inside Timesheet module");
            timesheetPage.clickOnTimesheetApprovalSchema();
            for(Map.Entry<String, Integer> entry : schemas.entrySet()) {
                String schemaName = entry.getKey();
                logger.info("3: Check that schema is already created");
                /*if(approvalSchema.getSchemaList().containsAll(Arrays.asList(schemas)))
                    break;*/
                if(!approvalSchema.getSchemaList().contains(schemaName)) {
                    logger.info("Click on add new schema btn");
                    approvalSchema.clickOnAddNewSchemaBTn();
                    logger.info("Write schema name");
                    approvalSchema.setSchemaName(schemaName);
                    logger.info("Click on Create schema");
                    approvalSchema.clickOnCreateSchema();
                }
            }
        }catch (Exception e){
            logger.error(e);
            Assert.fail();
        }
    }

    private void createLevelInsideSchemas(LinkedHashMap<String, Integer> schemaLevelMap, String[] approvalRoles){
        Map<Integer,String> levels = new HashMap<>();
        levels.put(1, "Level One"); levels.put(2, "Level Two"); levels.put(3,"Level Three"); levels.put(4,"Level Four"); levels.put(5,"Level Five");
        TimesheetPage timesheetPage = new TimesheetPage(driver);
        TimesheetApprovalSchema approvalSchema = new TimesheetApprovalSchema(driver);
        logger.info("----- Create levels inside schema -----");
        try {
            for(Map.Entry<String, Integer> entry : schemaLevelMap.entrySet()) {
                logger.info("Open schema "+entry.getKey());
                int levelCount = entry.getValue();
                approvalSchema.clickOnSchemaToOpen(entry.getKey());
                for(int j=0; j<levelCount; j++){
                    if(approvalSchema.getListOfLevels().contains(levels.get(levelCount)))
                        break;
                    if(!approvalSchema.getListOfLevels().contains(levels.get(j + 1))) {
                        logger.info("Click on Add Approval level btn");
                        approvalSchema.clickOnAddApprovalLevelBtn();
                        logger.info("Set level name Level " + Integer.toString(j));
                        approvalSchema.setApprovalLevelName("Level " + Integer.toString(j + 1));
                        logger.info("Select Approval Role: " + approvalRoles[j]);
                        approvalSchema.selectApprovalRole(approvalRoles[j]);
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

    private void createDummyProjectsAndSelectSchema(LinkedHashMap<String, String> projectSchemaMap){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        ProjectFormPage projectFormPage = new ProjectFormPage(driver);
        try{
            int i = 0;
            headerPage.clickOnProjects();
            for (Map.Entry<String, String> entry : projectSchemaMap.entrySet()) {
                String project = entry.getKey();
                String schema = entry.getValue();
                if(!projectsPage.getProjectsName().contains(project)){
                    projectsPage.clickOnAddProjectBtn();
                    projectFormPage.setTxtProjectName(project);
                    projectFormPage.selectTimesheetSchema(schema);
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

    private void selectMemberAndTheirRoleInsideProject(LinkedHashMap<String, String> projectSchemaMap, String[] approvalUsers, String[] approvalRoles, String[] users){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        ProjectFormPage projectFormPage = new ProjectFormPage(driver);
        try{
            headerPage.clickOnProjects();
            for(Map.Entry<String, String> entry : projectSchemaMap.entrySet()){
                String project = entry.getKey();
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

    private void createDummyIssues(LinkedHashMap<String, String> projectSchemaMap){
        HeaderPage headerPage = new HeaderPage(driver);
        ProjectsPage projectsPage = new ProjectsPage(driver);
        IssuePage issuePage = new IssuePage(driver);
        IssueFormPage issueFormPage = new IssueFormPage(driver);
        try{
            headerPage.clickOnProjects();

            for(Map.Entry<String, String> entry : projectSchemaMap.entrySet()){
                String project = entry.getKey();
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
