import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Excel工具类
 * @auther denny
 * @create 2019-12-10 14:32
 */
@Data
public class ExcelWriterUtils {
    String filepath;
    String sheetName;

    //标题
    private LinkedList<String> title;

    FileOutputStream fos;
    FileInputStream fis;
    Workbook workbook;


    public ExcelWriterUtils(String filepath, String sheetName, LinkedList<String> title) {
        try{
            fos = new FileOutputStream(new File(filepath));
            fis = new FileInputStream(filepath);

            String[] split = filepath.split("\\.");
            if("xls".equals(split[1])){

            }else if("xlsx".equals(split[1])){

            }else {
                //可以修改成打印日志
                System.out.println("文件类型错误");
                return;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
