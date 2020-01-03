/**
 * @Description
 * @auther denny
 * @create 2020-01-02 16:59
 */
public class TestDemo {
    public static void main(String[] args) {
        ExcelWriteUtils excelUtils = new ExcelWriteUtils();
        String path = "D:\\tool\\src\\main\\resources\\" + "test.xlsx";
        String[] title = new String[]{"t11","t22"};
//        excelUtils.createExcel(path,"Sheet1",title);

        String[][] values = new String[2][2];
        values[0][0] = "t101";
        values[0][1] = "t111";
        values[1][0] = "t201";
        values[1][1] = "t211";

//        excelUtils.appendToExcel(path,"Sheet1",values);

        int num = excelUtils.createSheet(path,"Sheet2",title,values);
        values[0][0] = "t1012";
        values[0][1] = "t1112";
        values[1][0] = "t2012";
        values[1][1] = "t2112";
        excelUtils.appendToExcel(path,"Sheet2",values,num);
    }
}
