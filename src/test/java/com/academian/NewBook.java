package com.academian;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class NewBook {

	private static final String CITY = "Bengaluru";
	private static final String EMAIL = "padma@YOPmail.com";
	private static final String BOOKMYSHOW_HOME = "https://in.bookmyshow.com/explore/home/";

	public static void main(String[] args) throws InterruptedException {

		ChromeDriver driver = initDriver();

		// Open bookMyShow URL
		
		driver.get(BOOKMYSHOW_HOME);
		driver.manage().window().maximize();
		
		// Select city 
		WebElement place = driver.findElement(By.xpath("//input[@placeholder='Search for your city']"));
		place.clear();
		place.sendKeys(CITY);

	
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		List<WebElement> list = driver.findElements(By.xpath("//div/div/div/div/div/div/div/ul/li/span/strong"));

		// select an option from list
		for (int i = 0; i < list.size(); i++) {
			String text = list.get(i).getText();
			if (text.equalsIgnoreCase(CITY)) {
				list.get(i).click();
				break;
			}
		}

		// Sign in page

		driver.findElement(By.xpath("//div[@class='bwc__sc-1nbn7v6-14 khhVFa']")).click();
		
		// continue with email click
		driver.findElement(By.xpath(" //div[contains(text(),'Continue with Email')]")).click();

		// email signin and clicking on continue
		WebElement emailfield = driver.findElement(By.xpath("//input[@id='emailId']"));
		emailfield.clear();
		emailfield.sendKeys(EMAIL);
		driver.findElement(By.xpath("//button[normalize-space()='Continue']")).click();
		
		
		// open new tab in same browser window
		driver.switchTo().newWindow(WindowType.TAB);
		
		//open yop mail.com
		driver.get("https://yopmail.com/");

		// find windows ids for swtiching between tabs
		Set<String> win_ids = driver.getWindowHandles();
		List<String> listwinids = new ArrayList(win_ids);
		String Parentwindowid = listwinids.get(0);
		String childwindowid = listwinids.get(1);

		// Switching to child window[yopmail]
		driver.switchTo().window(childwindowid);

		driver.findElement(By.xpath("//input[@id='login']")).sendKeys(EMAIL);
		
		driver.findElement(By.xpath("//i[@class='material-icons-outlined f36']")).click();
		
		// Finding inner frame of mailbox of yop mail
		WebElement fr = driver.findElement(By.xpath("//iframe[@id='ifinbox']"));
		driver.switchTo().frame(fr);

		// 
		WebElement email = driver
				.findElement(By.xpath("/html/body/div[2]/div[@class='m'][1]/button/div[@class='lms']"));

		char[] otpAsChar = findOTP(email);

		driver.switchTo().window(Parentwindowid);

		// Fill OTP in OTP fields
		fillOTP(driver, otpAsChar);
		
		// Submit the form
		driver.findElement(By.xpath("//button[@class='bwc__sc-dh558f-43 bIAzbS']")).click();

	}

	private static void fillOTP(ChromeDriver driver, char[] otpAsChar) {
		WebElement first_textbox = driver.findElement(By.xpath("//div[@id='modal-root']//input[1]"));
		first_textbox.sendKeys(String.valueOf(otpAsChar[0]));

		WebElement second_textbox = driver.findElement(By.xpath("//div[@id='modal-root']//input[2]"));
		second_textbox.sendKeys(String.valueOf(otpAsChar[1]));

		WebElement third_textbox = driver.findElement(By.xpath("//div[@id='modal-root']//input[3]"));
		third_textbox.sendKeys(String.valueOf(otpAsChar[2]));

		WebElement forth_textbox = driver.findElement(By.xpath("//div[@id='modal-root']//input[4]"));
		forth_textbox.sendKeys(String.valueOf(otpAsChar[3]));

		WebElement fifth_textbox = driver.findElement(By.xpath("//div[@id='modal-root']//input[5]"));
		fifth_textbox.sendKeys(String.valueOf(otpAsChar[4]));

		WebElement six_textbox = driver.findElement(By.xpath("//div[@id='modal-root']//input[6]"));
		six_textbox.sendKeys(String.valueOf(otpAsChar[5]));
	}

	private static char[] findOTP(WebElement email) {
		String codetotal = email.getText();
		
		String code = codetotal.substring(0, 6);
		code.substring(0, 0);
		int codenum = Integer.parseInt(code);

		char[] otpAsChar = ("" + codenum).toCharArray();
		return otpAsChar;
	}

	private static ChromeDriver initDriver() {

		ChromeOptions options = new ChromeOptions();

		//Driver options were initialized to stop bookmyshow server from detecting bot requests for login. 
		
		initDriverOptions(options);

		WebDriverManager.chromedriver().setup();
		ChromeDriver driver = new ChromeDriver(options);
		driver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

		return driver;
	}

	private static ChromeOptions initDriverOptions(ChromeOptions options) {

		options.addArguments("--disable-blink-features=AutomationControlled", "start-maximized", "--disable-extensions",
				"--disable-plugins-discovery"
		// "--incognito"
		// "--remote-allow-origins=*"
		);
		options.setExperimentalOption("useAutomationExtension", false);
		String[] switeches = { "enable-automation" };
		options.setExperimentalOption("excludeSwitches", switeches);
		return options;
	}

}
