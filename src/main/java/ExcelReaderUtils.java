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
    //需要跳过的TOP N行
    private int skipSize;
    //一次读取数量
    private int batchSize;

    private FileInputStream inputStream;
    private Sheet sheet;

    //标题
    private List<String> title;

    //实际已读取的行数
    private int index = 0;

    public ExcelReaderUtils(File file,int sheetIndex,int skipSize){
        new ExcelReaderUtils(file,sheetIndex,skipSize,100);
    }

    public ExcelReaderUtils(File file,int sheetIndex,int skipSize,int batchSize){
        this.file = file;
        this.sheetIndex = sheetIndex;
        this.skipSize = skipSize;
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
        sheet = workbook.getSheetAt(sheetIndex);
    }


    public List<Map<String, Object>> getLines(){
        // 跳过前面的行数
        while (index < skipSize) {
            index++;
        }

        //读取标题
        if(title == null){
            title = new LinkedList<String>();
            for(Row row :sheet){
                for(int titleIndex = row.getFirstCellNum();
                    titleIndex <= row.getLastCellNum();
                    titleIndex++){

                    title.add(String.valueOf(row.getCell(titleIndex)));
                }
                //只遍历第一行
                break;
            }
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
                line.put(title.get(idx),row.getCell(idx+startNum));
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

    public void cleanUp() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {

            }
        }
    }

    public List<String> getTitle() {
        return title;
    }

    /**
     * CELL_TYPE_NUMERIC　　数值型　　0
     * CELL_TYPE_STRING　　 字符串型　1
     * CELL_TYPE_FORMULA　　公式型   2
     * CELL_TYPE_BLANK　　  空值　　　3
     * CELL_TYPE_BOOLEAN　　布尔型　　4
     * CELL_TYPE_ERROR　　  错误　　　5
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
                case 0:
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
                case 1:
                    value = cell.getStringCellValue();
                    break;
                case 2:
                    value = cell.toString();
                    break;
                case 3:
                    value = "";
                    break;
                case 4:
                    value = cell.getBooleanCellValue();
            }
            return value;
        }
    }

}
