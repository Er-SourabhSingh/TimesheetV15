package utilities;

import org.testng.annotations.DataProvider;

public class DataProviders {
    @DataProvider(name = "schemaFlows")
    public Object[][] getSchemaFlows() {
        return new Object[][] {
                {"Project A Default-Schema", "Current Week", "user1", new String[]{"admin"}},
                {"Project A Default-Schema", "Current Week", "2Approval", new String[]{"admin"}},
                {"Project B 1-Level-Schema", "Current Week", "user2", new String[]{"1Approval"}},
                {"Project B 1-Level-Schema", "Current Week", "1Approval", new String[]{"admin"}},
                {"Project C 2-Level-Schema", "Current Week", "user3", new String[]{"1Approval", "2Approval"}},
                {"Project F 5-Level-Schema", "Current Week", "user4", new String[]{"1Approval", "2Approval", "3Approval", "4Approval", "5Approval"}},
                {"Project F 5-Level-Schema", "Current Week", "3Approval", new String[]{"4Approval", "5Approval"}},
                {"Project F 5-Level-Schema", "Current Week", "5Approval", new String[]{"admin"}}
        };
    }

    @DataProvider(name = "rejectionFlow")
    public Object[][] getRejectionFlowData() {
        return new Object[][] {
                // 2-Level schema: Reject at level 1
                {"Project C 2-Level-Schema", "Last Week", "user3", "1Approval", new String[]{"1Approval", "2Approval"}},

                // 5-Level schema: Reject at level 3
                {"Project F 5-Level-Schema", "Last Week", "user4", "3Approval", new String[]{"1Approval", "2Approval", "3Approval", "4Approval", "5Approval"}},

                // 5-Level schema: Reject at level 5
                {"Project F 5-Level-Schema", "Last Week", "3Approval", "5Approval", new String[]{"4Approval", "5Approval"}},

                // 1-Level schema: Reject at only level
                {"Project B 1-Level-Schema", "Last Week", "user2", "1Approval", new String[]{"1Approval"}},

                // Default schema: Approver rejects
                {"Project A Default-Schema", "Last Week", "2Approval", "admin", new String[]{"admin"}},

                // 5-Level schema: Final level (5Approval) submits; only admin can reject
                {"Project F 5-Level-Schema", "Last Week", "5Approval", "admin", new String[]{"admin"}}
        };
    }


}
