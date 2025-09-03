package testBase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import pageObjects.LoginPage;
import pageObjects.TimesheetPage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BaseClass {
    public static WebDriver driver;
    public static Logger logger;
    public static Properties properties;

    @BeforeClass(groups = {"Sanity","Master","Regression"})
    @Parameters({"os","browser"})
    public void setup(String os, String br) throws IOException{
      launch(os,br);
    }

    public void launch(String os, String br) throws IOException{
        logger = LogManager.getLogger(this.getClass());
        FileReader fileReader = new FileReader("./src//test//resources//config.properties");
        properties = new Properties();
        properties.load(fileReader);

        if (properties.getProperty("execution_env").equalsIgnoreCase("remote")) {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            if (os.equalsIgnoreCase("window")) {
                capabilities.setPlatform(Platform.WIN11);
            } else if (os.equalsIgnoreCase("mac")) {
                capabilities.setPlatform(Platform.MAC);
            } else {
                System.out.println("os is invalid");
                return;
            }

            switch (br.toLowerCase()) {
                case "chrome":
                    capabilities.setBrowserName("chrome");
                    break;
                case "edge":
                    capabilities.setBrowserName("edge");
                    break;
                case "firefox":
                    capabilities.setBrowserName("firefox");
                    break;
                default:
                    System.out.println("invalid browser");
                    return;
            }
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
        } else if (properties.getProperty("execution_env").equalsIgnoreCase("local")) {
            switch (br.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    //chromeOptions.addArguments("--headless");//Enable headless mode
                    //chromeOptions.addArguments("--window-size=1980,1080");//Set Screen Size
                    //chromeOptions.addArguments("--disable-gpu");//Disable GPU acceleration (some time needed for headless)
                    chromeOptions.setExperimentalOption("prefs", Map.of(
                            "credentials_enable_service", false,
                            "profile.password_manager_enabled", false
                    ));

                    // Prevent browser automation detection
                    chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    chromeOptions.setExperimentalOption("useAutomationExtension", false);

                    // Disable annoying UI popups
                    chromeOptions.addArguments("--disable-notifications");
                    chromeOptions.addArguments("--disable-features=AutofillServerCommunication");
                    chromeOptions.addArguments("--disable-save-password-bubble");
                    chromeOptions.addArguments("--disable-sync");

                    // Run in incognito to avoid cached login/profile warnings
                    chromeOptions.addArguments("--incognito");
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    //edgeOptions.addArguments("--headless"); // Run in headless mode
                    //edgeOptions.addArguments("--disable-gpu"); // Disable GPU for better performance
                    edgeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    edgeOptions.setExperimentalOption("useAutomationExtension", false);
                    WebDriver driver = new EdgeDriver(edgeOptions);
                    break;
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    //firefoxOptions.addArguments("--headless");// Run in headless mode
                    //firefoxOptions.addArguments("--disable-gpu"); // Disable GPU for better performance
                    firefoxOptions.addPreference("dom.webdriver.enabled", false);
                    firefoxOptions.addPreference("useAutomationExtension", false);
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                default:
                    System.out.println("invalid browser");
                    return;
            }
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(properties.getProperty("url1"));
        driver.manage().window().maximize();
    }

    @AfterClass(groups = {"Sanity", "Master", "Regression"})
    public void tearDown() {
        driver.quit();
    }

    public String captureScreen(String tName) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

        String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tName + "_" + timeStamp + ".png";
        File targetFile = new File(targetFilePath);

        sourceFile.renameTo(targetFile);

        return targetFilePath;
    }

    public void login(String userName, String userPassword){
        LoginPage loginPage = new LoginPage(driver);
        try {
            loginPage.setTxtUserName(userName);
            loginPage.setTxtUserPassword(userPassword);
            loginPage.clickBtnLogin();
        }catch (Exception e){
            Assert.fail();
        }
    }

    public String[] getCurrentWeekDayNumbers(){
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String[] dayNumbers = new String[5];

        for (int i = 0; i < 5 ; i++){
            dayNumbers[i] = monday.plusDays(i).format(dateTimeFormatter);
        }
        return dayNumbers;
    }

    public String[] getLastWeekDayNumbers(){
        LocalDate today = LocalDate.now();
        LocalDate lastWeekMonday = today.with(DayOfWeek.MONDAY).minusWeeks(1);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String[] dayNumbers = new String[5];

        for (int i = 0; i < 5 ; i++){
            dayNumbers[i] = lastWeekMonday.plusDays(i).format(dateTimeFormatter);
        }
        return dayNumbers;
    }

    public List<String> getFirstFiveDay(String start, String end){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(start, inputFormatter);
        LocalDate endDate = LocalDate.parse(end, inputFormatter);

        int count = 0;
        List<String> days = new ArrayList<>();

        while(!startDate.isAfter(endDate) && count < 5){
            days.add(startDate.format(dayFormatter));
            startDate = startDate.plusDays(1);
            count++;
        }
        return days;
    }

    public String getRandomActivity() throws Exception{
            TimesheetPage timesheetPage = new TimesheetPage(driver);

            List<String> activities = timesheetPage.getActivitiesOfLogTime();

            // Optional: remove placeholder options like "-- Please Select --"
            activities.removeIf(activity -> activity.trim().isEmpty() || activity.toLowerCase().contains("select"));

            if (activities.isEmpty()) {
                throw new IllegalStateException("No valid activities found in the dropdown.");
            }

            int index = ThreadLocalRandom.current().nextInt(activities.size());
            return activities.get(index);

    }


    public static String getRandomTime(){
        Random random = new Random();
        int hours = random.nextInt(4)+6;
        return Integer.toString(hours);
    }


    public static double convertTimeToDecimal(String time){
        if(time == null || !time.contains(":")) return 0.0;
        String [] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        return hours + (minutes/60.0);
    }

    public List<String> toFullNames(String[] loginNames) {
        List<String> fullNames = new ArrayList<>();
        for (String login : loginNames) {
            String[] parts = login.split("\\.");
            String first = capitalize(parts[0]);
            String last = capitalize(parts[1]);
            fullNames.add(first + " " + last);
        }
        return fullNames;
    }

    public String capitalize(String str) {
        return str == null || str.isEmpty()
                ? str
                : str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
