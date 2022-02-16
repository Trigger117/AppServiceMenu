package com.omicron;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import com.model.MenuContent;
import com.model.MenuNode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.google.gson.Gson;

public class ServiceMenu {
    public static void main(String[] args) {

        //lettura e caricamento json fornito
        try {
            Properties aProp = new Properties();

            //settaggio path file properties
            aProp.load(new FileInputStream("config.properties"));
            FileReader reader = new FileReader(aProp.getProperty("path"));
            Gson gson = new Gson();

            //Deserializzazione
            MenuContent menuContent = gson.fromJson(reader, MenuContent.class);
            List<MenuNode> lista = menuContent.getNodes();
            Files.createDirectories(Paths.get(aProp.getProperty("outputFolderPath")));
            int i = 1;
            int depth = 0;
            int mD = 0;
            proccessJson(mD, menuContent, lista, 0);
            int maxDepth = maxDepth(mD, menuContent, 0);
            System.out.println(maxDepth);


            //settaggio path json
            File excelFile = new File(aProp.getProperty("filePath"));
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(menuContent.getVersion());
            Row rowHeader = sheet.createRow(0);
            String[] title = {"nodeId", "nodeName", "nodeType", "groupType", "flowType", "resourceId"};
            XSSFCellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            createHeader(maxDepth, style, title, rowHeader);

            //creazione tabella excel
            for (MenuNode node : menuContent.getCleanNodes()) {
                if (depth < node.getDepth()) {
                    depth = node.getDepth();

                }
                Row row = sheet.createRow(i);

                if (node.getNodeType().equals("service")) {
                    row.createCell(maxDepth + 1).setCellValue(node.getNodeId());

                }
                row.createCell(maxDepth + 2).setCellValue(node.getNodeName());
                row.createCell(maxDepth + 3).setCellValue(node.getNodeType());
                row.createCell(maxDepth + 4).setCellValue(node.getGroupType());
                row.createCell(maxDepth + 5).setCellValue(node.getFlowType());

                if (node.getResource() != null) {
                    row.createCell(maxDepth + 6).setCellValue(node.getResource().getId());
                }

                row.createCell(node.getDepth()).setCellValue("X");
                i++;
                sheet.autoSizeColumn(maxDepth + 7);

            }
            int l = depth;
            for (int k = 0; k <= l; k++) {
                rowHeader.createCell(k).setCellValue(k);
            }
            //Creazione e inserimento dati tramite properties sulla prima riga

            int columns = rowHeader.getLastCellNum();
            for (int j = 0; j < columns; j++) {
                sheet.autoSizeColumn(j);
            }
            //chiusura stream
            FileOutputStream out = new FileOutputStream(excelFile);
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ignored) {

        }
    }

    public static void proccessJson(int maxDepth, MenuContent m, List<MenuNode> nodes, int depth) {
        if (depth > maxDepth) {
            maxDepth = depth;
        }

        for (MenuNode node : nodes) {
            m.setCleanNodes(node);
            node.setDepth(depth);
            if (node.getNodes() != null && !node.getNodes().isEmpty()) {
                proccessJson(maxDepth, m, node.getNodes(), depth + 1);
            }
        }
    }

    public static int maxDepth(int maxDepth, MenuContent m, int depth) {
        for (MenuNode node : m.getCleanNodes()) {
            if (depth < node.getDepth()) {
                depth = node.getDepth();
                maxDepth = depth;
            }

        }
        return maxDepth;
    }

    public static void createHeader(int maxDepth, XSSFCellStyle style, String[] headerCells, Row rowHeader) {
        int l = maxDepth;
        Cell cell;
        for (String s : headerCells) {
            l++;
            cell = rowHeader.createCell(l);
            cell.setCellValue(s);
            cell.setCellStyle(style);

        }

    }
}