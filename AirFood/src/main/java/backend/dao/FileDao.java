package backend.dao;

import backend.model.*;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.ws.rs.core.Response;
import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileDao {

    //private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C://IdeaProjects//airfoodFiles//";
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/var/cat/docs/";

    public XSSFWorkbook getExcelById(Long id) {
        OrderPreview order = new OrderDao().getOrderPreviewById(id);

        ArrayList<Ration>[] directRations = new ArrayList[4];
        directRations[0] = order.getRationDirectBusinessList();
        directRations[1] = order.getRationDirectEconomList();
        directRations[2] = order.getRationDirectCrewList();
        directRations[3] = order.getRationDirectSpecialList();
        ArrayList<Ration>[] reverseRations = new ArrayList[4];
        reverseRations[0] = order.getRationReverseBusinessList();
        reverseRations[1] = order.getRationReverseEconomList();
        reverseRations[2] = order.getRationReverseCrewList();
        reverseRations[3] = order.getRationReverseSpecialList();
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Order #" + id);
            Row row = null;
            Cell cell = null;

            //styles
            CellStyle styleBold = workbook.createCellStyle();
            Font fontBold = workbook.createFont();
            fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
            styleBold.setFont(fontBold);

            CellStyle styleAlign = workbook.createCellStyle();
            styleAlign.setAlignment(CellStyle.ALIGN_LEFT);
            styleAlign.setVerticalAlignment(CellStyle.VERTICAL_TOP);

            CellStyle styleDate = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            styleDate.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("Аэропорт вылета");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue(order.getDepAirportName());
            cell = row.createCell(3);
            cell.setCellValue("Время прилета");
            cell.setCellStyle(styleBold);
            cell = row.createCell(4);
            if(order.getArriveDateTime()==null){
                cell.setCellValue("");
            }else{
                cell.setCellValue(createDate(order.getArriveDateTime()));
                cell.setCellStyle(styleDate);
            }

            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue("Аэропорт прилета");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue(order.getArrAirportName());
            cell = row.createCell(3);
            cell.setCellValue("Время вылета");
            cell.setCellStyle(styleBold);
            cell = row.createCell(4);
            if(order.getDepartureDateTime()==null){
                cell.setCellValue("");
            }else{
                cell.setCellValue(createDate(order.getDepartureDateTime()));
                cell.setCellStyle(styleDate);
            }


            row = sheet.createRow(2);
            cell = row.createCell(0);
            cell.setCellValue("Номер рейса пр.");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue(order.getFlightNameDirect());
            cell = row.createCell(3);
            cell.setCellValue("Время готовности");
            cell.setCellStyle(styleBold);
            cell = row.createCell(4);
            if(order.getReadyDateTime()==null){
                cell.setCellValue("");
            }else{
                cell.setCellValue(createDate(order.getReadyDateTime()));
                cell.setCellStyle(styleDate);
            }

            row = sheet.createRow(3);
            cell = row.createCell(0);
            cell.setCellValue("Номер рейса обр.");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue(order.getFlightNameReverse());
            cell = row.createCell(3);
            cell.setCellValue("Время досмотра");
            cell.setCellStyle(styleBold);
            cell = row.createCell(4);
            if(order.getInspectionDateTime()==null){
                cell.setCellValue("");
            }else{
                cell.setCellValue(createDate(order.getInspectionDateTime()));
                cell.setCellStyle(styleDate);
            }

            row = sheet.createRow(4);
            cell = row.createCell(0);
            cell.setCellValue("Бортовой номер");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue(order.getBoardNumber());
            cell = row.createCell(3);
            cell.setCellValue("Под бортом");
            cell.setCellStyle(styleBold);
            cell = row.createCell(4);
            if(order.getWorkDateTime()==null){
                cell.setCellValue("");
            }else{
                cell.setCellValue(createDate(order.getWorkDateTime()));
                cell.setCellStyle(styleDate);
            }

            row = sheet.createRow(5);
            cell = row.createCell(0);
            cell.setCellValue("Тип ВС");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue(order.getAircraftType());

            row = sheet.createRow(8);
            cell = row.createCell(0);
            cell.setCellValue((String) "Прямой пролет");
            cell.setCellStyle(styleBold);

            row = sheet.createRow(10);
            cell = row.createCell(0);
            cell.setCellValue("Бизнес");
            cell.setCellStyle(styleBold);
            cell = row.createCell(3);
            cell.setCellValue("Эконом");
            cell.setCellStyle(styleBold);
            cell = row.createCell(6);
            cell.setCellValue("Экипаж");
            cell.setCellStyle(styleBold);
            cell = row.createCell(9);
            cell.setCellValue((String) "Специальное");
            cell.setCellStyle(styleBold);

            int amountDirectRationsRows = max(order.getRationDirectBusinessList(), order.getRationDirectEconomList(),
                    order.getRationDirectSpecialList(), order.getRationDirectCrewList());
            int currentRow = 11;
            int amountRations = 1;
            for (int i = 0; i < amountDirectRationsRows; i++) {
                row = sheet.createRow(currentRow);
                writeRationsInExcel(directRations, cell, row, amountRations, i);
                amountRations++;
                currentRow++;
            }

            currentRow = currentRow + amountDirectRationsRows - 1;

            row = sheet.createRow(currentRow);
            cell = row.createCell(0);
            cell.setCellValue((String) "Напитки по матрице:");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue((String) order.getDrinkDirect());

            currentRow = currentRow + 2;

            row = sheet.createRow(currentRow);
            cell = row.createCell(0);
            cell.setCellValue((String) "Комментарий:");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue((String) order.getDirectComment());
            cell.setCellStyle(styleAlign);
            sheet.addMergedRegion(new CellRangeAddress(
                    currentRow, //first row (0-based)
                    currentRow + 1, //last row  (0-based)
                    1, //first column (0-based)
                    10  //last column  (0-based)
            ));

            currentRow = currentRow + 3;

            row = sheet.createRow(currentRow);
            cell = row.createCell(0);
            cell.setCellValue((String) "Обратный пролет");
            cell.setCellStyle(styleBold);

            currentRow = currentRow + 2;

            row = sheet.createRow(currentRow);
            cell = row.createCell(0);
            cell.setCellValue((String) "Бизнес");
            cell.setCellStyle(styleBold);
            cell = row.createCell(3);
            cell.setCellValue((String) "Эконом");
            cell.setCellStyle(styleBold);
            cell = row.createCell(6);
            cell.setCellValue((String) "Экипаж");
            cell.setCellStyle(styleBold);
            cell = row.createCell(9);
            cell.setCellValue((String) "Специальное");
            cell.setCellStyle(styleBold);

            int amountReverseRationsRows = max(order.getRationReverseBusinessList(), order.getRationReverseEconomList(),
                    order.getRationReverseSpecialList(), order.getRationReverseCrewList());
            currentRow++;
            amountRations = 1;
            for (int i = 0; i < amountReverseRationsRows; i++) {
                row = sheet.createRow(currentRow);
                writeRationsInExcel(reverseRations, cell, row, amountRations, i);
                amountRations++;
                currentRow++;
            }

            currentRow = currentRow + amountDirectRationsRows - 1;

            row = sheet.createRow(currentRow);
            cell = row.createCell(0);
            cell.setCellValue((String) "Напитки по матрице:");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue((String) order.getDrinkReverse());

            currentRow = currentRow + 2;

            row = sheet.createRow(currentRow);
            cell = row.createCell(0);
            cell.setCellValue((String) "Комментарий:");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue((String) order.getReverseComment());
            cell.setCellStyle(styleAlign);
            sheet.addMergedRegion(new CellRangeAddress(
                    currentRow, //first row (0-based)
                    currentRow + 1, //last row  (0-based)
                    1, //first column (0-based)
                    10  //last column  (0-based)
            ));
            sheet.autoSizeColumn((short) 0);
            sheet.autoSizeColumn((short) 1);
            sheet.setColumnWidth(2, 1000);
            sheet.autoSizeColumn((short) 3);
            sheet.setColumnWidth(4, 4500);
            sheet.setColumnWidth(5, 1000);
            sheet.setColumnWidth(6, sheet.getColumnWidth(3));
            sheet.setColumnWidth(7, sheet.getColumnWidth(4));
            sheet.setColumnWidth(8, 1000);
            sheet.setColumnWidth(9, sheet.getColumnWidth(3));
            sheet.setColumnWidth(10, sheet.getColumnWidth(4));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    public XSSFWorkbook getExcelOrders(String from, String to, int airportId, Integer userId) {
        OrderShortResponse allOrders = new OrderDao().getShortOrders(from, to, userId);
        OrderShortResponse orders = new OrderShortResponse();
        String airportName = null;
        if (airportId == 0) {
            orders = allOrders;
            airportName = "All";
        } else {
            airportName = new AirportDao().getAirportNameById(airportId);
            for (OrderShort order :
                    allOrders.getOrders()) {
                if (order.getDepAirportId() == airportId) {
                    orders.getOrders().add(order);
                }
            }
        }

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Orders");
            Row row = null;
            Cell cell = null;

            //styles
            CellStyle styleBold = workbook.createCellStyle();
            Font fontBold = workbook.createFont();
            fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
            styleBold.setFont(fontBold);

            CellStyle styleAlign = workbook.createCellStyle();
            styleAlign.setAlignment(CellStyle.ALIGN_CENTER);

            CellStyle styleDate = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            styleDate.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

            row = sheet.createRow(0);
//            cell = row.createCell(0);
//            cell.setCellValue((String) "Аэропорт");
//            cell.setCellStyle(styleBold);
//            cell = row.createCell(1);
//            cell.setCellValue((String) airportName);
            cell = row.createCell(3);
            cell.setCellValue((String) "За период:");
            cell.setCellStyle(styleBold);
            cell = row.createCell(4);
            cell.setCellValue(createDateSecond(from));
            cell.setCellStyle(styleDate);
            cell = row.createCell(5);
            cell.setCellValue((String) "-");
            cell.setCellStyle(styleAlign);
            cell = row.createCell(6);
            cell.setCellValue(createDateSecond(to));
            cell.setCellStyle(styleDate);

            sheet.addMergedRegion(new CellRangeAddress(
                    1, //first row (0-based)
                    1, //last row  (0-based)
                    0, //first column (0-based)
                    6  //last column  (0-based)
            ));

            row = sheet.createRow(2);
            cell = row.createCell(0);
            cell.setCellValue((String) "Dep.");
            cell.setCellStyle(styleBold);
            cell = row.createCell(1);
            cell.setCellValue((String) "Arr.");
            cell.setCellStyle(styleBold);
            cell = row.createCell(2);
            cell.setCellValue((String) "Время прилета");
            cell.setCellStyle(styleBold);
            cell = row.createCell(3);
            cell.setCellValue((String) "Время вылета");
            cell.setCellStyle(styleBold);
            cell = row.createCell(4);
            cell.setCellValue((String) "Время готовности");
            cell.setCellStyle(styleBold);
            cell = row.createCell(5);
            cell.setCellValue((String) "Время досмотра");
            cell.setCellStyle(styleBold);
            cell = row.createCell(6);
            cell.setCellValue((String) "Под бортом");
            cell.setCellStyle(styleBold);
            cell = row.createCell(7);
            cell.setCellValue((String) "Тип ВС");
            cell.setCellStyle(styleBold);
            cell = row.createCell(8);
            cell.setCellValue((String) "Номер рейса");
            cell.setCellStyle(styleBold);
            cell = row.createCell(9);
            cell.setCellValue((String) "Бортовой номер");
            cell.setCellStyle(styleBold);

            int i = 3;
            for (OrderShort order :
                    orders.getOrders()) {
                row = sheet.createRow(i);
                cell = row.createCell(0);
                cell.setCellValue((String) order.getDepAirportName());
                cell = row.createCell(1);
                cell.setCellValue((String) order.getArrAirportName());
                cell = row.createCell(2);
                cell.setCellValue(createDateSecond(order.getArriveDateTime()));
                cell.setCellStyle(styleDate);
                cell = row.createCell(3);
                cell.setCellValue(createDateSecond(order.getDepartureDateTime()));
                cell.setCellStyle(styleDate);
                cell = row.createCell(4);
                cell.setCellValue(createDateSecond(order.getReadyDateTime()));
                cell.setCellStyle(styleDate);
                cell = row.createCell(5);
                cell.setCellValue(createDateSecond(order.getInspectionDateTime()));
                cell.setCellStyle(styleDate);
                cell = row.createCell(6);
                cell.setCellValue(createDateSecond(order.getWorkDateTime()));
                cell.setCellStyle(styleDate);
                cell = row.createCell(7);
                cell.setCellValue((String) order.getAircraftType());
                cell = row.createCell(8);
                cell.setCellValue((String) order.getFlightNumberDirect());
                cell = row.createCell(9);
                cell.setCellValue((String) order.getBoardNumber());

                i++;
            }
//            for (int j = 0; j < 7; j++) {
//                sheet.autoSizeColumn((short) j);
//            }
//            sheet.setColumnWidth(4, sheet.getColumnWidth(6));

            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 2000);
            sheet.setColumnWidth(2, 4300);
            sheet.setColumnWidth(3, 4300);
            sheet.setColumnWidth(4, 4300);
            sheet.setColumnWidth(5, 4300);
            sheet.setColumnWidth(6, 4300);
            sheet.setColumnWidth(7, 4300);
            sheet.setColumnWidth(8, 4300);
            sheet.setColumnWidth(9, 4300);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    public XSSFWorkbook getExcelOrdersForReview(String from, String to, int airportId, Integer idByToken) {
        OrderDao orderDao = new OrderDao();
        ArrayList<OrderPreview> orders = new ArrayList<OrderPreview>();
        OrderShortResponse orderShortResponse = orderDao.getShortOrders(from, to, idByToken);
        for (OrderShort o :
                orderShortResponse.getOrders()) {
            orders.add(orderDao.getOrderPreviewById(o.getId()));
        }

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("OrdersForReview");
            Row row = null;
            Cell cell = null;

            //fonts

            Font fontCalibriBoldULine = workbook.createFont();
            fontCalibriBoldULine.setFontName("Calibri");
            fontCalibriBoldULine.setFontHeightInPoints((short) (18));
            fontCalibriBoldULine.setBoldweight(Font.BOLDWEIGHT_BOLD);
            fontCalibriBoldULine.setUnderline(Font.U_SINGLE);

            Font fontCalibriBold9 = workbook.createFont();
            fontCalibriBold9.setFontName("Calibri");
            fontCalibriBold9.setFontHeightInPoints((short) (9));
            fontCalibriBold9.setBoldweight(Font.BOLDWEIGHT_BOLD);

            Font fontCalibri9 = workbook.createFont();
            fontCalibriBold9.setFontName("Calibri");
            fontCalibriBold9.setFontHeightInPoints((short) (9));

            Font fontCalibriBold14 = workbook.createFont();
            fontCalibriBold14.setFontName("Calibri");
            fontCalibriBold14.setFontHeightInPoints((short) (14));
            fontCalibriBold14.setBoldweight(Font.BOLDWEIGHT_BOLD);

            //styles

            CellStyle styleDate = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            styleDate.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
            styleDate.setAlignment(CellStyle.ALIGN_CENTER);
            styleDate.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            CellStyle styleHeaderOrderText = workbook.createCellStyle();
            styleHeaderOrderText.setAlignment(CellStyle.ALIGN_CENTER);
            styleHeaderOrderText.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styleHeaderOrderText.setFont(fontCalibriBoldULine);

            CellStyle styleTableText = workbook.createCellStyle();
            styleTableText.setAlignment(CellStyle.ALIGN_CENTER);
            styleTableText.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styleTableText.setFont(fontCalibri9);

            CellStyle styleTableHeaderText = workbook.createCellStyle();
            styleTableHeaderText.setAlignment(CellStyle.ALIGN_CENTER);
            styleTableHeaderText.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styleTableHeaderText.setBorderLeft(CellStyle.BORDER_MEDIUM);
            styleTableHeaderText.setBorderBottom(CellStyle.BORDER_MEDIUM);
            styleTableHeaderText.setBorderRight(CellStyle.BORDER_MEDIUM);
            styleTableHeaderText.setBorderTop(CellStyle.BORDER_MEDIUM);
            styleTableHeaderText.setFont(fontCalibriBold9);

            CellStyle styleRationText = workbook.createCellStyle();
            styleRationText.setAlignment(CellStyle.ALIGN_CENTER);
            styleRationText.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styleRationText.setFont(fontCalibriBold9);

            CellStyle styleLeftBorderText = workbook.createCellStyle();
            styleLeftBorderText.setAlignment(CellStyle.ALIGN_CENTER);
            styleLeftBorderText.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styleLeftBorderText.setBorderLeft(CellStyle.BORDER_MEDIUM);
            styleLeftBorderText.setBorderBottom(CellStyle.BORDER_THIN);
            styleLeftBorderText.setBorderRight(CellStyle.BORDER_THIN);
            styleLeftBorderText.setFont(fontCalibriBold9);

            CellStyle styleCenterNotBorderText = workbook.createCellStyle();
            styleCenterNotBorderText.setAlignment(CellStyle.ALIGN_CENTER);
            styleCenterNotBorderText.setBorderBottom(CellStyle.BORDER_THIN);
            styleCenterNotBorderText.setBorderLeft(CellStyle.BORDER_THIN);
            styleCenterNotBorderText.setBorderRight(CellStyle.BORDER_THIN);
            styleCenterNotBorderText.setFont(fontCalibriBold14);

            CellStyle styleRightBorderText = workbook.createCellStyle();
            styleRightBorderText.setAlignment(CellStyle.ALIGN_CENTER);
            styleRightBorderText.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styleRightBorderText.setBorderRight(CellStyle.BORDER_MEDIUM);
            styleRightBorderText.setBorderBottom(CellStyle.BORDER_THIN);
            styleRightBorderText.setBorderLeft(CellStyle.BORDER_THIN);
            styleRightBorderText.setFont(fontCalibriBold14);

            CellStyle styleLeftBottomBorderText = workbook.createCellStyle();
            styleLeftBottomBorderText.setAlignment(CellStyle.ALIGN_CENTER);
            styleLeftBottomBorderText.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styleLeftBottomBorderText.setBorderBottom(CellStyle.BORDER_MEDIUM);
            styleLeftBottomBorderText.setBorderLeft(CellStyle.BORDER_MEDIUM);
            styleLeftBottomBorderText.setBorderTop(CellStyle.BORDER_THIN);
            styleLeftBottomBorderText.setBorderRight(CellStyle.BORDER_THIN);
            styleLeftBottomBorderText.setFont(fontCalibriBold14);

            CellStyle styleBottomBorderText = workbook.createCellStyle();
            styleBottomBorderText.setAlignment(CellStyle.ALIGN_CENTER);
            styleBottomBorderText.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styleBottomBorderText.setBorderBottom(CellStyle.BORDER_MEDIUM);
            styleBottomBorderText.setBorderTop(CellStyle.BORDER_THIN);
            styleBottomBorderText.setBorderLeft(CellStyle.BORDER_THIN);
            styleBottomBorderText.setBorderRight(CellStyle.BORDER_THIN);
            styleBottomBorderText.setFont(fontCalibriBold14);

            CellStyle styleRightBottomBorderText = workbook.createCellStyle();
            styleRightBottomBorderText.setAlignment(CellStyle.ALIGN_CENTER);
            styleRightBottomBorderText.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styleRightBottomBorderText.setBorderBottom(CellStyle.BORDER_MEDIUM);
            styleRightBottomBorderText.setBorderRight(CellStyle.BORDER_MEDIUM);
            styleRightBottomBorderText.setBorderTop(CellStyle.BORDER_THIN);
            styleRightBottomBorderText.setBorderLeft(CellStyle.BORDER_THIN);
            styleRightBottomBorderText.setFont(fontCalibriBold14);

            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("Заказ на поставку бортового питания");
            cell.setCellStyle(styleHeaderOrderText);

            CellRangeAddress region = new CellRangeAddress(
                    0, //first row (0-based)
                    1, //last row  (0-based)
                    0, //first column (0-based)
                    7  //last column  (0-based)
            );
            sheet.addMergedRegion(region);

            cell = row.createCell(8);
            cell.setCellValue("№");
            cell.setCellStyle(styleLeftBorderText);
            cell = row.createCell(9);
            cell.setCellStyle(styleCenterNotBorderText);
            cell = row.createCell(10);
            cell.setCellStyle(styleRightBorderText);

            row = sheet.createRow(1);
            cell = row.createCell(8);
            cell.setCellStyle(styleLeftBottomBorderText);
            cell = row.createCell(9);
            cell.setCellStyle(styleBottomBorderText);
            cell = row.createCell(10);
            cell.setCellStyle(styleRightBottomBorderText);

            row = sheet.createRow(3);
            row.setHeight((short) 1000);
            cell = row.createCell(0);
            cell.setCellValue("Время вылета");
            cell.setCellStyle(styleTableHeaderText);
            cell = row.createCell(1);
            cell.setCellValue("А/к");
            cell.setCellStyle(styleTableHeaderText);
            cell = row.createCell(2);
            cell.setCellValue("№ рейса");
            cell.setCellStyle(styleTableHeaderText);
            cell = row.createCell(3);
            cell.setCellValue("Вид рейса");
            cell.setCellStyle(styleTableHeaderText);
            cell = row.createCell(4);
            cell.setCellValue("Тип ВС");
            cell.setCellStyle(styleTableHeaderText);
            cell = row.createCell(5);
            cell.setCellValue("Направление");
            cell.setCellStyle(styleTableHeaderText);
            cell = row.createCell(6);
            cell.setCellValue("Время прилета");
            cell.setCellStyle(styleTableHeaderText);
            cell = row.createCell(7);
            cell.setCellValue("Время готовности");
            cell.setCellStyle(styleTableHeaderText);
            cell = row.createCell(8);
            mergeHorizontalStyleCells(sheet, row, cell, styleTableHeaderText, "Бизнес", 8, 9);
            sheet.addMergedRegion(new CellRangeAddress(
                    3, //first row (0-based)
                    3, //last row  (0-based)
                    8, //first column (0-based)
                    9  //last column  (0-based)
            ));

            cell = row.createCell(10);
            mergeHorizontalStyleCells(sheet, row, cell, styleTableHeaderText, "Эконом", 10, 11);
            sheet.addMergedRegion(new CellRangeAddress(
                    3, //first row (0-based)
                    3, //last row  (0-based)
                    10, //first column (0-based)
                    11  //last column  (0-based)
            ));
            cell = row.createCell(12);
            mergeHorizontalStyleCells(sheet, row, cell, styleTableHeaderText, "Экипаж", 12, 13);
            sheet.addMergedRegion(new CellRangeAddress(
                    3, //first row (0-based)
                    3, //last row  (0-based)
                    12, //first column (0-based)
                    13  //last column  (0-based)
            ));
            cell = row.createCell(14);
            mergeHorizontalStyleCells(sheet, row, cell, styleTableHeaderText, "Спец питание", 14, 15);
            sheet.addMergedRegion(new CellRangeAddress(
                    3, //first row (0-based)
                    3, //last row  (0-based)
                    14, //first column (0-based)
                    15  //last column  (0-based)
            ));
            cell = row.createCell(16);
            cell.setCellValue("Примечание");
            cell.setCellStyle(styleTableHeaderText);

            int currentRow = 4;

            for (OrderPreview o :
                    orders) {
                Order order = new OrderDao().getOrderById(o.getId());
                Airline airline = new AirlineDao().getAirlineById(new FlightDao().getFlightById(order.getFlightIdDirect()).getAirlineId());
                String airlineName = airline.getName();

                int directRows = max(o.getRationDirectBusinessList(), o.getRationDirectEconomList(),
                        o.getRationDirectCrewList(), o.getRationDirectSpecialList());
                if (directRows == 0) {
                    directRows = 1;
                }
                int reverseRows = max(o.getRationReverseBusinessList(), o.getRationReverseEconomList(),
                        o.getRationReverseCrewList(), o.getRationReverseSpecialList());
                if (reverseRows == 0) {
                    reverseRows = 1;
                }

                row = sheet.createRow(currentRow);

                cell = row.createCell(0);
                cell.setCellValue(createDateSecond(o.getDepartureDateTime()));
                cell.setCellStyle(styleDate);

                sheet.addMergedRegion(new CellRangeAddress(
                        currentRow, //first row (0-based)
                        currentRow + directRows + reverseRows - 1, //last row  (0-based)
                        0, //first column (0-based)
                        0  //last column  (0-based)
                ));

                cell = row.createCell(1);
                cell.setCellValue(airlineName);
                cell.setCellStyle(styleTableText);

                sheet.addMergedRegion(new CellRangeAddress(
                        currentRow, //first row (0-based)
                        currentRow + directRows + reverseRows - 1, //last row  (0-based)
                        1, //first column (0-based)
                        1  //last column  (0-based)
                ));

                cell = row.createCell(2);
                cell.setCellValue(o.getFlightNameDirect());
                cell.setCellStyle(styleTableText);

                if (directRows > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(
                            currentRow, //first row (0-based)
                            currentRow + directRows - 1, //last row  (0-based)
                            2, //first column (0-based)
                            2  //last column  (0-based)
                    ));
                }

                cell = row.createCell(3);
                cell.setCellValue("прямой");
                cell.setCellStyle(styleTableText);

                if (directRows > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(
                            currentRow, //first row (0-based)
                            currentRow + directRows - 1, //last row  (0-based)
                            3, //first column (0-based)
                            3  //last column  (0-based)
                    ));
                }

                cell = row.createCell(4);
                cell.setCellValue(o.getAircraftType());
                cell.setCellStyle(styleTableText);

                sheet.addMergedRegion(new CellRangeAddress(
                        currentRow, //first row (0-based)
                        currentRow + directRows + reverseRows - 1, //last row  (0-based)
                        4, //first column (0-based)
                        4  //last column  (0-based)
                ));

                cell = row.createCell(5);
                cell.setCellValue(o.getArrAirportName());
                cell.setCellStyle(styleTableText);

                sheet.addMergedRegion(new CellRangeAddress(
                        currentRow, //first row (0-based)
                        currentRow + directRows + reverseRows - 1, //last row  (0-based)
                        5, //first column (0-based)
                        5  //last column  (0-based)
                ));

                cell = row.createCell(6);
                if (o.getArriveDateTime() == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(createDateSecond(o.getArriveDateTime()));
                    cell.setCellStyle(styleDate);
                }

                sheet.addMergedRegion(new CellRangeAddress(
                        currentRow, //first row (0-based)
                        currentRow + directRows + reverseRows - 1, //last row  (0-based)
                        6, //first column (0-based)
                        6  //last column  (0-based)
                ));

                cell = row.createCell(7);
                if (o.getReadyDateTime() == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(createDateSecond(o.getReadyDateTime()));
                    cell.setCellStyle(styleDate);
                }

                sheet.addMergedRegion(new CellRangeAddress(
                        currentRow, //first row (0-based)
                        currentRow + directRows + reverseRows - 1, //last row  (0-based)
                        7, //first column (0-based)
                        7  //last column  (0-based)
                ));

                //int currentDirectRow = currentRow;

                ArrayList<Ration>[] directRations = new ArrayList[4];
                directRations[0] = o.getRationDirectBusinessList();
                directRations[1] = o.getRationDirectEconomList();
                directRations[2] = o.getRationDirectCrewList();
                directRations[3] = o.getRationDirectSpecialList();
                ArrayList<Ration>[] reverseRations = new ArrayList[4];
                reverseRations[0] = o.getRationReverseBusinessList();
                reverseRations[1] = o.getRationReverseEconomList();
                reverseRations[2] = o.getRationReverseCrewList();
                reverseRations[3] = o.getRationReverseSpecialList();

                int amountRations = 1;
                for (int i = 0; i < directRows; i++) {
                    writeRationsInExcelForReview(directRations, cell, row, amountRations, i, styleRationText);
                    amountRations++;
                    currentRow++;
                    row = sheet.createRow(currentRow);
                }

                //currentRow = currentRow + directRows - 1;

                row = sheet.createRow(currentRow);

                cell = row.createCell(2);
                cell.setCellValue(o.getFlightNameReverse());
                cell.setCellStyle(styleTableText);

                if (reverseRows > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(
                            currentRow, //first row (0-based)
                            currentRow + reverseRows - 1, //last row  (0-based)
                            2, //first column (0-based)
                            2  //last column  (0-based)
                    ));
                }

                cell = row.createCell(3);
                cell.setCellValue("обратный");
                cell.setCellStyle(styleTableText);

                if (reverseRows > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(
                            currentRow, //first row (0-based)
                            currentRow + reverseRows - 1, //last row  (0-based)
                            3, //first column (0-based)
                            3  //last column  (0-based)
                    ));
                }

                amountRations = 1;
                for (int i = 0; i < reverseRows; i++) {
                    writeRationsInExcelForReview(reverseRations, cell, row, amountRations, i, styleRationText);
                    amountRations++;
                    currentRow++;
                    row = sheet.createRow(currentRow);
                }
            }

            sheet.setColumnWidth(0, 4500);
            sheet.setColumnWidth(1, 3000);
            sheet.setColumnWidth(2, 3000);
            sheet.setColumnWidth(3, 3000);
            sheet.setColumnWidth(4, 3000);
            sheet.setColumnWidth(5, 4500);
            sheet.setColumnWidth(6, 4500);
            sheet.setColumnWidth(7, 4500);
            sheet.setColumnWidth(8, 4500);
            sheet.setColumnWidth(9, 2000);
            sheet.setColumnWidth(10, 4500);
            sheet.setColumnWidth(11, 2000);
            sheet.setColumnWidth(12, 4500);
            sheet.setColumnWidth(13, 2000);
            sheet.setColumnWidth(14, 4500);
            sheet.setColumnWidth(15, 2000);
            sheet.setColumnWidth(16, 4500);
            sheet.setColumnWidth(17, 2000);
            sheet.setColumnWidth(18, 4500);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /////////////////////

    private void mergeHorizontalStyleCells(XSSFSheet sheet, Row row, Cell cell, CellStyle style, String title, int ii, int c) {
        for (int i = ii; i <= c; ++i) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            if ((i == ii)) {
                cell.setCellValue(title);
            }
        }
    }

    private int max(ArrayList<Ration> list1, ArrayList<Ration> list2, ArrayList<Ration> list3, ArrayList<Ration> list4) {
        int max = 0;
        if (list1.size() >= list2.size()) {
            max = list1.size();
        } else {
            max = list2.size();
        }
        if (list3.size() > max) {
            max = list3.size();
        }
        if (list4.size() > max) {
            max = list4.size();
        }
        return max;
    }

    private void writeRationsInExcel(ArrayList<Ration>[] list, Cell cell, Row row, int amountRations, int i) {
        int k = -1;
        for (int j = 0; j < list.length; j++) {
            if (list[j].size() >= amountRations) {
                cell = row.createCell(++k);
                cell.setCellValue((String) list[j].get(i).getCode());
                cell = row.createCell(++k);
                cell.setCellValue((String) list[j].get(i).getAmount());
                ++k;
            } else {
                k = k + 3;
            }
        }
    }

    private void writeRationsInExcelForReview(ArrayList<Ration>[] list, Cell cell, Row row, int amountRations, int i, CellStyle style) {
        int k = 7;
        for (int j = 0; j < list.length; j++) {
            if (list[j].size() >= amountRations) {
                cell = row.createCell(++k);
                if(list[j].get(i).getId()!=null) {
                    cell.setCellValue((String) list[j].get(i).getCode());
                    cell.setCellStyle(style);
                    cell = row.createCell(++k);
                    cell.setCellValue((String) list[j].get(i).getAmount());
                    cell.setCellStyle(style);
                }
            } else {
                k = k + 2;
            }
        }
    }

    //work with files

    public void deleteFileFromDBAndServer(int id) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        try {
            String sql = "SELECT files.orderid, files.filename, files.filetype " +
                    "FROM airfood.files WHERE files.fileid = ?";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setLong(1, id);
            ResultSet rs = psql.executeQuery(sql);
            int orderId = 0;
            String name = null;
            String type = null;
            while (rs.next()) {
                orderId = rs.getInt("files.orderid");
                name = rs.getString("files.filename");
                type = rs.getString("files.filetype");
            }
            sql = "UPDATE airfood.files " +
                    "SET files.isdeleted = 1 " +
                    "WHERE files.fileid = ?";
            psql = connection.prepareStatement(sql);
            psql.setLong(1, id);
            psql.executeQuery(sql);
            rs.close();
            psql.close();
            connection.close();
            String prefix = name.substring(name.length() - 4, name.length());
            removeFileFromServer(id, orderId, type, prefix);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long setNewFileInDB(Long id, String name, String type) {
        Connection connection = ConnectionToMySQLDB.getConnaction();
        Long fileId = null;
        try {
            String sql = "INSERT INTO airfood.files (files.orderid, files.filename, files.filetype) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement psql = connection.prepareStatement(sql);
            psql.setLong(1, id);
            psql.setString(2, name);
            psql.setString(3, type);
            psql.executeUpdate();
            sql = "SELECT MAX(files.fileid) FROM airfood.files";
            psql = connection.prepareStatement(sql);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                fileId = rs.getLong("MAX(files.fileid)");
            }
            rs.close();
            psql.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileId;
    }

    private void removeFileFromServer(int id, int orderId, String type, String prefix) {
        //File file = new File(SERVER_UPLOAD_LOCATION_FOLDER + orderId + "//" + type + "/" + id + prefix);
        File file = new File(SERVER_UPLOAD_LOCATION_FOLDER + orderId +"/"+ type + "/" + id + prefix);
        boolean deleted = file.delete();
    }

    //work with date

    private Date createDate(String inputString) {
        if (inputString == null) {
            return null;
        }
        String s = inputString.substring(0, 2) + "." + inputString.substring(3, 5) + "." + inputString.substring(6, 10) + " " + inputString.substring(11, 13) + ":" + inputString.substring(14, 16);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        Date date = null;
        try {
            date = dateFormat.parse(s);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date;
    }

    private Date createDateSecond(String inputString) {
        if (inputString == null) {
            return null;
        }
        inputString = inputString.substring(0, 10).replaceAll("-", ".") + inputString.substring(10, 16);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        Date date = null;
        try {
            date = dateFormat.parse(inputString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date;
    }
}
