package com.sampleData;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Issue_Log {

    static WebDriver driver;
    // Read Excel File
    File src = new File("C:\\AMIGO Selenium Excel Sheet.xlsx");
    FileInputStream input = new FileInputStream(src);
    XSSFWorkbook workbook = new XSSFWorkbook(input);
    XSSFSheet sheet = workbook.getSheetAt(0);

    public Issue_Log() throws IOException, IOException {
    }

    @BeforeTest
    public void Setup() throws IOException {
        //get WebDriver Path
        String webDriverPath = sheet.getRow(3).getCell(2).getStringCellValue();
        System.out.println(webDriverPath);

        //get UserName & Password
        String username = sheet.getRow(1).getCell(2).getStringCellValue();
        System.out.println(username);
        String password = sheet.getRow(2).getCell(2).getStringCellValue();
        System.out.println(password);

        //Open Chrome & go to Salesforce login page
        System.setProperty("webdriver.chrome.driver", webDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        //all other arguments(if need then add)
        //options.addArguments("--always-authorize-plugins"); options.addArguments("--no-sandbox"); options.addArguments("--disable-dev-shm-usage"); options.addArguments("--aggressive-cache-discard"); options.addArguments("--disable-cache"); options.addArguments("--disable-application-cache"); options.addArguments("--disable-offline-load-stale-cache"); options.addArguments("--disk-cache-size=0"); options.addArguments("--headless"); options.addArguments("--disable-gpu"); options.addArguments("--dns-prefetch-disable"); options.addArguments("--no-proxy-server"); options.addArguments("--log-level=3"); options.addArguments("--silent"); options.addArguments("--disable-browser-side-navigation"); options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.get("https://login.salesforce.com");

        //give UserName & Password & Click to Login
        driver.findElement(By.xpath("//input[@id='username']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
        driver.findElement(By.id("Login")).click();
        driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);

        //get sObject URL
        String sObject = sheet.getRow(18).getCell(2).getStringCellValue();
        System.out.println(sObject);

        //redirect to sObject
        driver.get(sObject);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    @Test(priority = 0)
    public void CreateIssueLog() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);

        //click New Button
        WebElement myDynamicElement = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title=\"New\"]")));
        myDynamicElement.click();

        //Associated Organization
        String OrgName = sheet.getRow(11).getCell(3).getStringCellValue();
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@title=\"Search Organization\"]")));
        myDynamicElement.sendKeys(OrgName);
        Thread.sleep(2000);
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title=\""+OrgName+"\"]")));
        myDynamicElement.click();

        //Associated portfolio
        String PortName = sheet.getRow(12).getCell(3).getStringCellValue();
        driver.findElement(By.xpath("//input[@title=\"Search Portfolios\"]")).sendKeys(PortName);
        Thread.sleep(2000);
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title=\""+PortName+"\"]")));
        myDynamicElement.click();

        //Associated Program
        String ProgName = sheet.getRow(13).getCell(3).getStringCellValue();
        driver.findElement(By.xpath("//input[@title=\"Search Programs\"]")).sendKeys(ProgName);
        Thread.sleep(2000);
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title=\""+ProgName+"\"]")));
        myDynamicElement.click();

        //Issue Log Name
        String ILName = sheet.getRow(18).getCell(3).getStringCellValue();
        driver.findElement(By.xpath("//input[@class=\" input\" and @maxlength=\"80\"]")).sendKeys(ILName);

        //Priority
        driver.findElement(By.xpath("(//a[@class=\"select\"])")).click();
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"3-Low\"]")));
        myDynamicElement.click();

        //Risk Register
        String RiskRegName = sheet.getRow(32).getCell(3).getStringCellValue();
        driver.findElement(By.xpath("//input[@title=\"Search Risk Register\"]")).sendKeys(RiskRegName);
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@title=\""+RiskRegName+"\"]")).click();

        //Due Date
        driver.findElement(By.xpath("//a[@class=\"datePicker-openIcon display\"]")).click();
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='17']")));
        myDynamicElement.click();

        //Issue Coordinate
        String userName = sheet.getRow(37).getCell(4).getStringCellValue();
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@class=\" default input uiInput uiInputTextForAutocomplete uiInput--default uiInput--input uiInput uiAutocomplete uiInput--default uiInput--lookup\" and @title=\"Search People\"])[1]")));
        myDynamicElement.sendKeys(userName);
        Thread.sleep(3000);
        myDynamicElement.sendKeys(Keys.ARROW_DOWN);
        myDynamicElement.sendKeys(Keys.ENTER);

        //Issue Impacts
        driver.findElement(By.xpath("(//a[@class=\"select\"])[2]")).click();
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title=\"The business\"]")));
        myDynamicElement.click();

        //Issue Owner
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@class=\" default input uiInput uiInputTextForAutocomplete uiInput--default uiInput--input uiInput uiAutocomplete uiInput--default uiInput--lookup\" and @title=\"Search People\"])[2]")));
        myDynamicElement.sendKeys(userName);
        Thread.sleep(3000);
        myDynamicElement.sendKeys(Keys.ARROW_DOWN);
        myDynamicElement.sendKeys(Keys.ENTER);

        //Associted Change Request
        String CRName = sheet.getRow(13).getCell(3).getStringCellValue();
        driver.findElement(By.xpath("//input[@placeholder=\"Search Change Request Log...\"]")).sendKeys(CRName);
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title=\""+CRName+"\"]")));
        myDynamicElement.click();
        Thread.sleep(2000);

        //issue Signoff Resolution
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@placeholder=\"Search People...\"])[3]")));
        myDynamicElement.sendKeys(userName);
        Thread.sleep(3000);
        myDynamicElement.sendKeys(Keys.ARROW_DOWN);
        myDynamicElement.sendKeys(Keys.ENTER);

        //Issue Description
        driver.findElement(By.xpath("//div[@class=\"ql-editor ql-blank slds-rich-text-area__content slds-text-color_weak slds-grow\"]")).sendKeys("ok");

        //Final Resolution
        driver.findElement(By.xpath("//div[@class=\"ql-editor ql-blank slds-rich-text-area__content slds-text-color_weak slds-grow\"]")).sendKeys("ok");

        //Recommended Resolution
        driver.findElement(By.xpath("//div[@class=\"ql-editor ql-blank slds-rich-text-area__content slds-text-color_weak slds-grow\"]")).sendKeys("ok");

        //Historical Comment
        driver.findElement(By.xpath("//div[@class=\"ql-editor ql-blank slds-rich-text-area__content slds-text-color_weak slds-grow\"]")).sendKeys("test");

        //Save IssueLog
        driver.findElement(By.xpath("//button[@title=\"Save\"]")).click();
        Thread.sleep(5000);

        //get Toast Message
        myDynamicElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class=\"toastMessage slds-text-heading--small forceActionsText\"]")));
        String ToastMessage = myDynamicElement.getAttribute("innerHTML");

        //Expected Toast Message Value Set
        String ExpectedValue = "Issue Log \""+ILName+"\" was created.";

        //Check
        Assert.assertEquals(ToastMessage,ExpectedValue);

        Thread.sleep(5000);
    }

    @AfterTest
    //Closing the Chrome
    public void terminateBrowser(){
        driver.close();
    }
}