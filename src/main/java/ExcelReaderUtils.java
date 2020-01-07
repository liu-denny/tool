import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** 仅支持xlsx(2007版)
 * @Description
 * @auther denny
 * @create 2020-01-03 14:54
 */
public class ExcelReaderUtils {
    private static DecimalFormat df = new DecimalFormat("0");

    //excel文件
    private File file;
    //表序号
    private int sheetIndex;
    //需要跳过的行数(标题为第一行)
    private int skipSize;

    //一次读取数量
    private int batchSize;

    private FileInputStream inputStream;
    private Sheet sheet;

    //标题
    private LinkedList<String> title;

    //实际已读取的行数
    private int index = 0;

    public ExcelReaderUtils(File file){
        this(file,0,100);
    }

    public ExcelReaderUtils(File file,int sheetIndex){
        this(file,sheetIndex,100);
    }

    public ExcelReaderUtils(File file,int sheetIndex,int batchSize){
        this.file = file;
        this.sheetIndex = sheetIndex;
        this.batchSize = batchSize;

        //open excel file
        try{
            inputStream = new FileInputStream(file);
        }catch (FileNotFoundException e){
            //处理文件不存在
            System.out.println("open file for stream error, path: " + file.getAbsolutePath());
            inputStream = null;
        }

        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(1000)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(204800)     // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(inputStream);
        this.sheet = workbook.getSheetAt(sheetIndex);
    }

    public void setSkipSize(int skipSize) {
        this.skipSize = skipSize;
    }

    public List<Map<String, Object>> getLines(){


        //读取标题
        if(title == null){
            this.addTitle();
        }

        while (index < skipSize){
            // 读取并跳过一行
            for (Row row : sheet) {
                break;
            }
            index++;
        }

        //文件结尾
        if(index > sheet.getLastRowNum()) {
            return null;
        }
        int bachIdx = 0;
        List<Map<String, Object>> lines = new LinkedList<>();
        for(Row row:sheet){

            //行转map
            Map<String,Object> line = new HashMap<>();
            int startNum = row.getFirstCellNum();
            for(int idx =0;idx < title.size();idx++){
                line.put(title.get(idx),getValue(row.getCell(idx+startNum)));
            }
            lines.add(line);
            index++;
            bachIdx++;

            if(bachIdx >= batchSize) {
                break;
            }
        }
        return lines;

    }

    public void addTitle() {
        //读取标题
        if(title == null){
            title = new LinkedList<>();

            for(Row row :sheet){
                for(int titleIndex = row.getFirstCellNum();
                    titleIndex < row.getLastCellNum();
                    titleIndex++){

                    title.add(String.valueOf(getValue(row.getCell(titleIndex))));
                }
                //只遍历第一行
                break;
            }
            index++;
        }
    }

    public LinkedList<String> getTitle() {
        if(title == null){
            this.addTitle();
        }
        return title;
    }

    /**
     * 获取当前读到第几行（包括标题）
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取当前Sheet的名字
     * @return
     */
    public String getSheetName(){
        return sheet.getSheetName();
    }

    /**
     * 回收资源
     */
    public void cleanUp() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {

            }
        }
    }

    /**
     * NUMERIC　　数值型　　
     * STRING　　 字符串型　
     * FORMULA　　公式型
     * BLANK　　  空值　　　
     * BOOLEAN　　布尔型　　
     * @param cell
     * @return
     */
    public Object getValue(Cell cell){
        Object value = null;
        if(cell == null){
            return value;
        }else {
            String style = cell.getCellStyle().getDataFormatString();
            switch (cell.getCellType()) {
                case NUMERIC:
                    double numeric = cell.getNumericCellValue();
                    if("@".equals(style)){
                        value =  df.format(numeric);
                    } else if ("General".equals(style)) {
                        value = numeric;
                    } else if("0.00%".equals(style)) {
                        value = numeric;
                    } else {
                        value = numeric;
                    }
                    break;
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case FORMULA:
                    value = cell.toString();
                    break;
                case BLANK:
                    value = "";
                    break;
                case BOOLEAN:
                    value = cell.getBooleanCellValue();
            }
            return value;
        }
    }

}
