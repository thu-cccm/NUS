package com.maple.common.util.excel.bean;

import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.awt.*;

public enum ExportExcelTheme {

    DEFAULT("DEFAULT",
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(0, 0, 0), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(0, 0, 0), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(156, 155, 155), new DefaultIndexedColorMap())
    ),

    BLUE("BLUE",
            new XSSFColor(new Color(91, 155, 213), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(221, 235, 247), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(189, 215, 238), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(0, 0, 0), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap())
    ),

    GREEN("GREEN",
            new XSSFColor(new Color(112, 173, 71), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(226, 239, 218), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(198, 224, 180), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(0, 0, 0), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap())
    ),

    ORANGE("ORANGE",
            new XSSFColor(new Color(237, 125, 49), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(252, 228, 214), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(248, 203, 173), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(0, 0, 0), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap())
    );

    private final String code;

    private final XSSFColor titleBackgroundColor;

    private final XSSFColor titleFontColor;

    private final XSSFColor contentOneColor;

    private final XSSFColor contentTwoColor;

    private final XSSFColor contentFrontColor;

    private final XSSFColor borderColor;

    ExportExcelTheme(String code, XSSFColor titleBackgroundColor, XSSFColor titleFontColor,
                     XSSFColor contentOneColor, XSSFColor contentTwoColor,
                     XSSFColor contentFrontColor, XSSFColor borderColor) {
        this.code = code;
        this.titleBackgroundColor = titleBackgroundColor;
        this.titleFontColor = titleFontColor;
        this.contentOneColor = contentOneColor;
        this.contentTwoColor = contentTwoColor;
        this.contentFrontColor = contentFrontColor;
        this.borderColor = borderColor;
    }

    public String getCode() {
        return code;
    }

    public XSSFColor getTitleBackgroundColor() {
        return titleBackgroundColor;
    }

    public XSSFColor getTitleFontColor() {
        return titleFontColor;
    }

    public XSSFColor getContentOneColor() {
        return contentOneColor;
    }

    public XSSFColor getContentTwoColor() {
        return contentTwoColor;
    }

    public XSSFColor getContentFrontColor() {
        return contentFrontColor;
    }

    public XSSFColor getBorderColor() {
        return borderColor;
    }
}
