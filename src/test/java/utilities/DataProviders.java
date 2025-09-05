package utilities;

import org.testng.annotations.DataProvider;

public class DataProviders {
    @DataProvider(name = "schemaFlows")
    public Object[][] getSchemaFlows() {
        return new Object[][] {
                {"Project A Default-Schema", "Current Week", "ember.lilac", new String[]{"admin"}},
                {"Project A Default-Schema", "Current Week", "autumn.grace", new String[]{"admin"}},
                {"Project B 1-Level-Schema", "Current Week", "isla.moon", new String[]{"aurora.wren"}},
                {"Project B 1-Level-Schema", "Current Week", "aurora.wren", new String[]{"admin"}},
                {"Project C 2-Level-Schema", "Current Week", "ivy.skylark", new String[]{"aurora.wren", "autumn.grace"}},
                {"Project F 5-Level-Schema", "Current Week", "nova.starling", new String[]{"aurora.wren", "autumn.grace", "briar.sunset", "celeste.dawn", "daisy.skye"}},
                {"Project F 5-Level-Schema", "Current Week", "briar.sunset", new String[]{"celeste.dawn", "daisy.skye"}},
                {"Project F 5-Level-Schema", "Current Week", "daisy.skye", new String[]{"admin"}}
        };
    }

    @DataProvider(name = "rejectionFlow")
    public Object[][] getRejectionFlowData() {
        return new Object[][] {
                // 2-Level schema: Reject at level 1
                {"Project C 2-Level-Schema", "Last Week", "harmony.rose", "aurora.wren",
                        new String[]{"aurora.wren", "autumn.grace"}},

                // 5-Level schema: Reject at level 3
                {"Project F 5-Level-Schema", "Last Week", "isla.moon", "briar.sunset",
                        new String[]{"aurora.wren", "autumn.grace", "briar.sunset", "celeste.dawn", "daisy.skye"}},

                // 5-Level schema: Reject at level 5
                {"Project F 5-Level-Schema", "Last Week", "briar.sunset", "daisy.skye",
                        new String[]{"celeste.dawn", "daisy.skye"}},

                // 1-Level schema: Reject at only level
                {"Project B 1-Level-Schema", "Last Week", "ember.lilac", "aurora.wren",
                        new String[]{"aurora.wren"}},

                // Default schema: Approver rejects
                {"Project A Default-Schema", "Last Week", "autumn.grace", "admin",
                        new String[]{"admin"}},

                // 5-Level schema: Final level (5Approval) submits; only admin can reject
                {"Project F 5-Level-Schema", "Last Week", "daisy.skye", "admin",
                        new String[]{"admin"}}
        };
    }




}
