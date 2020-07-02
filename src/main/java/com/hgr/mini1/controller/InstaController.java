package com.hgr.mini1.controller;

import com.hgr.mini1.model.PostVO;
import com.hgr.mini1.repository.PostRepository;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class InstaController {

    @Autowired
    PostRepository pr;

    @GetMapping("/insta")
    public String crawler(){

        System.setProperty("webdriver.chrome.driver", "C:/dev/Workspace/IdeaProjects/hgr/mini1/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-default-apps");

        ChromeDriver driver = new ChromeDriver(options);


        WebElement data =null;
        WebElement user = null;
        List<WebElement> innerdata = null;
        WebElement img = null;
        List<WebElement> like = null;

        int scroll = 1;
        while(true){
            for(int i=1;i<=5;i++) {
                for(int j=1;j<=3;j++){
                    driver.get("https://www.instagram.com/explore/tags/%EA%BD%83%EC%A7%91/");
                    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                    data = driver.findElementByXPath("//*[@id=\"react-root\"]/section/main/article/div[2]/div/div["+i+"]/div["+j+"]/a");
                    String url = data.getAttribute("href");
                    driver.get(url);
                    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                    System.out.println("두번째 화면 접속 완료!");

                    user = driver.findElementByXPath("//*[@id=\"react-root\"]/section/main/div/div/article/div[2]/div[1]/ul/div/li/div/div/div[2]/h2/div/a");
                    String userId = user.getText();
                    System.out.println("user 아이디 크롤링 완료!");
                    System.out.println(userId);

                    innerdata = driver.findElementsByXPath("//*[@id=\"react-root\"]/section/main/div/div/article/div[2]/div[1]/ul/div/li/div/div/div[2]/span");
                    String postContent = innerdata.get(0).getText();
                    System.out.println("post 내용 크롤링 완료!");




                    img = driver.findElementByXPath("//*[@id=\"react-root\"]/section/main/div/div/article/div[1]/div");
                    String postPicture = img.findElement(By.tagName("img")).getAttribute("src");
                    System.out.println("post 사진 크롤링 완료!");
                    System.out.println(postPicture);


                    PostVO pv = new PostVO();
                    pv.setPostContent(postContent);
                    pv.setUserId(userId);
                    pv.setPostPicture(postPicture);


                    pr.save(pv);

                }
            }
            driver.executeScript("window.scrollTo(0, "+4000*scroll+")");
            driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
            scroll += 1;
        }
    }

    public void xlsxWiter(List<PostVO> list) {
        // 워크북 생성
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 워크시트 생성
        XSSFSheet sheet = workbook.createSheet();
        // 행 생성
        XSSFRow row = sheet.createRow(0);
        // 쎌 생성
        XSSFCell cell;

        // 헤더 정보 구성
        cell = row.createCell(0);
        cell.setCellValue("아이디");

        cell = row.createCell(1);
        cell.setCellValue("이름");

        cell = row.createCell(2);
        cell.setCellValue("나이");

        cell = row.createCell(3);
        cell.setCellValue("이메일");

        // 리스트의 size 만큼 row를 생성
        PostVO vo;
        for(int rowIdx=0; rowIdx < list.size(); rowIdx++) {
            vo = list.get(rowIdx);

            // 행 생성
            row = sheet.createRow(rowIdx+1);

            cell = row.createCell(0);
            cell.setCellValue(vo.getUserId());

            cell = row.createCell(1);
            cell.setCellValue(vo.getPostContent());

            cell = row.createCell(2);
            cell.setCellValue(vo.getPostPicture());

        }

        // 입력된 내용 파일로 쓰기
        File file = new File("C:\\excel\\testWrite.xlsx");
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(workbook!=null) workbook.close();
                if(fos!=null) fos.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}


