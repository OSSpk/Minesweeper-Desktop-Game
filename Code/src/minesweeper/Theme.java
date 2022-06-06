package minesweeper;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.*;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.ImageIcon;

public class Theme {
    private HashMap<String, ImageIcon> icons;

    private Color[] numberColors = new Color[8];

    private Color labelBoxColor;
    private Color labelFontColor;
    private Color tileBorderColor;
    private Color tileButtonColor;

    public Theme(String theme) {
        icons = new HashMap<String, ImageIcon>();
        JSONParser parser = new JSONParser();
        JSONArray jsonArray;
        try {
            FileReader reader = new FileReader(
                    new File(Theme.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath()
                            + "/themes/" + theme + ".json");
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject jsonNumberColorsList = (JSONObject) jsonObject.get("numberColors");
            for (int i = 0; i < 8; i++) {
                jsonArray = (JSONArray) jsonNumberColorsList.get((i + 1) + "");
                numberColors[i] = getColorFromJsonArray(jsonArray);
            }
            jsonArray = (JSONArray) jsonObject.get("labelBoxColor");
            labelBoxColor = getColorFromJsonArray(jsonArray);

            jsonArray = (JSONArray) jsonObject.get("labelFontColor");
            labelFontColor = getColorFromJsonArray(jsonArray);

            jsonArray = (JSONArray) jsonObject.get("tileBorderColor");
            tileBorderColor = getColorFromJsonArray(jsonArray);

            jsonArray = (JSONArray) jsonObject.get("tileButtonColor");
            tileButtonColor = getColorFromJsonArray(jsonArray);

            JSONObject themeData;
            String[] keys = { "background", "tile", "flag", "mine", "red_mine", "clock", "question" };
            for (String key : keys) {
                themeData = (JSONObject) jsonObject.get(key);
                icons.put(key, getImageIconFromJSONObject(themeData));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error Location: Reading Theme File");
        }
    }

    private ImageIcon getImageIconFromJSONObject(JSONObject jsonObject) {
        String themeDataType = (String) jsonObject.get("type");
        JSONObject data = (JSONObject) jsonObject.get("data");
        if (themeDataType.equalsIgnoreCase("image")) {
            return new ImageIcon(getClass().getResource((String) data.get("path")));
        } else {
            return createColorIcon(get2DVectorFromJSONArray((JSONArray) data.get("size")),
                    getColorFromJsonArray((JSONArray) data.get("color")));
        }
    }

    private int[] get2DVectorFromJSONArray(JSONArray arr) {
        int[] v = { Integer.valueOf(arr.get(0).toString()), Integer.valueOf(arr.get(1).toString()) };
        return v;
    }

    private Color getColorFromJsonArray(JSONArray arr) {
        int r = Integer.valueOf(arr.get(0).toString());
        int g = Integer.valueOf(arr.get(1).toString());
        int b = Integer.valueOf(arr.get(2).toString());
        return new Color(r, g, b);
    }

    protected static ImageIcon createColorIcon(int[] size, Color color) {
        BufferedImage img = new BufferedImage(size[0], size[1], BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, size[0], size[1]);
        g.dispose();

        return new ImageIcon(img);
    }

    public HashMap<String, ImageIcon> getIcons() {
        return icons;
    }

    public ImageIcon getIcon(String iconName) {
        return icons.get(iconName);
    }

    /**
     * @return ImageIcon return the backgroundIcon
     */
    public ImageIcon getBackgroundIcon() {
        return icons.get("background");
    }

    /**
     * @return ImageIcon return the tile
     */
    public ImageIcon getTileIcon() {
        return icons.get("tile");
    }

    /**
     * @return ImageIcon return the flag
     */
    public ImageIcon getFlagIcon() {
        return icons.get("flag");
    }

    /**
     * @return ImageIcon return the mine
     */
    public ImageIcon getMineIcon() {
        return icons.get("mine");
    }

    /**
     * @return ImageIcon return the redMine
     */
    public ImageIcon getRedMineIcon() {
        return icons.get("red_mine");
    }

    /**
     * @return ImageIcon return the clock
     */
    public ImageIcon getClockIcon() {
        return icons.get("clock");
    }

    /**
     * @return ImageIcon return the questionMark
     */
    public ImageIcon getQuestionMarkIcon() {
        return icons.get("question");
    }

    /**
     * @return Color[] return the numberColors
     */
    public Color[] getNumberColors() {
        return numberColors;
    }

    /**
     * @param index
     * @return Color return the sRGB given the index
     */
    public Color getNumberColors(int index) {
        return numberColors[index];
    }

    /**
     * @return Color return the labelBoxColor
     */
    public Color getLabelBoxColor() {
        return labelBoxColor;
    }

    /**
     * @return Color return the labelFontColor
     */
    public Color getLabelFontColor() {
        return labelFontColor;
    }

    /**
     * @return Color return the tileBorderColor
     */
    public Color getTileBorderColor() {
        return tileBorderColor;
    }

    /**
     * @return Color return the tileButtonColor
     */
    public Color getTileButtonColor() {
        return tileButtonColor;
    }

}