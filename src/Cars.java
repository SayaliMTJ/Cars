import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cars {
    public static void main(String[] args) throws InterruptedException, IOException {

        System.setProperty("webdriver.chrome.driver", "/Users/sayalimammadova/Desktop/BrowserDrivers/chromedriver");
        WebDriver driver = new ChromeDriver();

        //1. Navigate to cars.com
        driver.navigate().to("https://www.cars.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4000));

        //2. Verify the default selected options for the dropdowns are the following

        WebElement stockType = driver.findElement(By.id("make-model-search-stocktype"));
        Select selectStockType = new Select(stockType);
        WebElement selectedStock = selectStockType.getFirstSelectedOption();
        Assert.assertEquals(selectedStock.getText(), "New & used cars");

        WebElement make = driver.findElement(By.id("makes"));
        Select selectMakeType = new Select(make);
        WebElement selectedMake = selectMakeType.getFirstSelectedOption();
        Assert.assertEquals(selectedMake.getText(), "All makes");

        WebElement model = driver.findElement(By.id("models"));
        Select selectModelType = new Select(model);
        WebElement selectedModel = selectModelType.getFirstSelectedOption();
        Assert.assertEquals(selectedModel.getText(), "All models");

        WebElement price = driver.findElement(By.id("make-model-max-price"));
        Select selectMaxPrice = new Select(price);
        WebElement selectedPrice = selectMaxPrice.getFirstSelectedOption();
        Assert.assertEquals(selectedPrice.getText(), "No max price");

        WebElement distance = driver.findElement(By.id("make-model-maximum-distance"));
        Select selectDistance = new Select(distance);
        WebElement selectedDistance = selectDistance.getFirstSelectedOption();
        Assert.assertEquals(selectedDistance.getText(), "20 miles");

        // 3. In the New/used dropdown box, verify that the expected values are the following:
        String[] expectedStockTypes = {"New & used cars", "New & certified cars", "New cars", "Used cars", "Certified cars"};
        List<WebElement> StockTypes = selectStockType.getOptions();
        for (int i = 0; i < expectedStockTypes.length; i++) {
            Assert.assertEquals((StockTypes.get(i)).getText(), expectedStockTypes[i]);

        }

        // 4. Choose Used Cars from New/used dropdown
        selectStockType.getFirstSelectedOption();

        // 5. Choose Tesla from Make dropdown
        selectMakeType.selectByVisibleText("Tesla");

        // 6. Verify Models dropdown contains the following:
        String[] expectedModels = {"All models", "Model 3", "Model S", "Model X", "Model Y", "Roadster"};
        List<WebElement> ModelsElement = selectModelType.getOptions();
        for (int i = 0; i < expectedModels.length; i++) {
            Assert.assertEquals((ModelsElement.get(i).getText()), expectedModels[i]);
        }


        // 7. Choose Model S from the dropdown
        selectModelType.selectByIndex(2);

        // 8. Choose $100.000 from Price dropdown
        selectMaxPrice.selectByValue("100000");

        // 9. Choose 50 miles fromDistance dropdown
        selectDistance.selectByValue("50");

        // 10. Enter 22182 for ZIP and click Search
        driver.findElement(By.id("make-model-zip")).clear();
        driver.findElement(By.id("make-model-zip")).sendKeys("22182");
        driver.findElement(By.xpath("//button[@data-searchtype='make']")).click();
        Thread.sleep(2000);

        // 11. In the next Search Results Page, verify that there are 19 results on the page and each search result title contains "Tesla Model S"
        List<WebElement> carResultsList = driver.findElements(By.xpath("//a[@class='vehicle-card-link js-gallery-click-link']"));
        Assert.assertEquals(carResultsList.size(), 20);
        for (int i = 0; i < carResultsList.size(); i++) {
            Assert.assertTrue(carResultsList.get(i).getText().contains("Tesla Model S"));
        }
        Thread.sleep(2000);

        // 12. Choose Lowest Price from Sort by dropdown and verify that all the results are sorted in ascending order of the price
        new Select(driver.findElement(By.id("sort-dropdown"))).selectByValue("list_price");
        Thread.sleep(2000);
        Thread.sleep(2000);

        List<Integer> prices = new ArrayList<>();
        List<WebElement> pricesList = driver.findElements(By.xpath("//span[@class='primary-price']"));
        for(WebElement eachPrice : pricesList){
            Integer formatPrice = Integer.valueOf(eachPrice.getText().split("\\$")[1].replace(",", ""));
            prices.add(formatPrice);
        }
        Collections.sort(prices);
        for(int i =0; i<prices.size();i++) {
            Assert.assertEquals(Integer.valueOf(pricesList.get(i).getText().split("\\$")[1].replace(",", "")),prices.get(i));
        }

        // 13.Choose Highest Mileage from Sort by dropdown and verify that all the results are sorted in descending order of the mileage:
        new Select(driver.findElement(By.id("sort-dropdown"))).selectByVisibleText("Highest mileage");
        Thread.sleep(2000);
        Thread.sleep(2000);
        List<Integer> mileage = new ArrayList<>();
        List<WebElement> sortedMileage = driver.findElements(By.xpath("//div[@class='vehicle-details']//div[@class='mileage']"));
        for(WebElement eachMileage : sortedMileage){
            Integer milesInteger = Integer.valueOf(eachMileage.getText().split(" ")[0].replace(",", ""));
            mileage.add(milesInteger);
        }
        mileage.sort(Collections.reverseOrder());
        Thread.sleep(2000);
        for(int i =0; i<mileage.size();i++) {
            Assert.assertEquals(Integer.valueOf(sortedMileage.get(i).getText().split(" ")[0].replace(",", "")),mileage.get(i));
        }
        Thread.sleep(2000);

        // 14. Choose Nearest location from Sort by dropdown and verify that all the results are sorted in ascending order of the proximity to the current zip (exclude online sellers)
        new Select(driver.findElement(By.id("sort-dropdown"))).selectByValue("distance");
        Thread.sleep(4000);

        List<Integer> location= new ArrayList<>();
        List<WebElement> sortedDistance = driver.findElements(By.xpath("//div[@data-qa='miles-from-user']"));
        for(WebElement eachDistance : sortedDistance){
            String strDistance = eachDistance.getText();
            location.add(Integer.valueOf(strDistance.split(" ")[0]));
        }
        Thread.sleep(1500);
        Collections.sort(location);
        Thread.sleep(2000);
        for(int i =0; i<location.size();i++) {
            String formatDist = sortedDistance.get(i).getText();
            Assert.assertEquals(Integer.valueOf(formatDist.split(" ")[0]),location.get(i));
        }

        // 15. Choose Oldest year from Sort by dropdown and verify that all the results are sorted in ascending order of the year
        new Select(driver.findElement(By.id("sort-dropdown"))).selectByVisibleText("Oldest year");
        Thread.sleep(2000);
        Thread.sleep(2000);

        List<Integer> year = new ArrayList<>();
        List<WebElement> sortedYear = driver.findElements(By.xpath("//h2[@class='title']"));

        for(WebElement years : sortedYear){
            Integer formatYears = Integer.valueOf(years.getText().split(" ")[0]);
            mileage.add(formatYears);
        }
        Collections.sort(year);


        for(int i =0; i<year.size();i++) {
            Assert.assertEquals(Integer.valueOf(sortedYear.get(i).getText().split(" ")[0]),year.get(i));
        }
        driver.close();
    }
}





