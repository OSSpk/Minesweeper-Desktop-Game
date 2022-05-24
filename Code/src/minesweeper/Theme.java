package minesweeper;

import java.awt.Color;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.swing.ImageIcon;

public class Theme {
    private ImageIcon backgroundIcon;
    private ImageIcon tileIcon;
    private ImageIcon flagIcon;
    private ImageIcon mineIcon;
    private ImageIcon redMineIcon;
    private ImageIcon clockIcon;
    private ImageIcon questionMarkIcon;

    private Color[] numberColors = new Color[8];
    private Color labelBoxColor;
    private Color labelFontColor;

    private Color tileBorderColor;
    private Color tileButtonColor;

    public Theme(String theme) {
        backgroundIcon = new ImageIcon(getClass().getResource("/resources/" + theme + "/2.jpg"));
        tileIcon = new ImageIcon(getClass().getResource("/resources/" + theme + "/tile.png"));
        flagIcon = new ImageIcon(getClass().getResource("/resources/" + theme + "/flag.png"));
        mineIcon = new ImageIcon(getClass().getResource("/resources/" + theme + "/mine.png"));
        redMineIcon = new ImageIcon(getClass().getResource("/resources/" + theme + "/redmine.png"));
        clockIcon = new ImageIcon(getClass().getResource("/resources/" + theme + "/clock.png"));
        questionMarkIcon = new ImageIcon(getClass().getResource("/resources/" + theme + "/question.png"));

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Color getColorFromJsonArray(JSONArray arr) {
        int r = Integer.valueOf(arr.get(0).toString());
        int g = Integer.valueOf(arr.get(1).toString());
        int b = Integer.valueOf(arr.get(2).toString());
        return new Color(r, g, b);
    }

    /**
     * @return ImageIcon return the backgroundIcon
     */
    public ImageIcon getBackgroundIcon() {
        return backgroundIcon;
    }

    /**
     * @return ImageIcon return the tile
     */
    public ImageIcon getTileIcon() {
        return tileIcon;
    }

    /**
     * @return ImageIcon return the flag
     */
    public ImageIcon getFlagIcon() {
        return flagIcon;
    }

    /**
     * @return ImageIcon return the mine
     */
    public ImageIcon getMineIcon() {
        return mineIcon;
    }

    /**
     * @return ImageIcon return the redMine
     */
    public ImageIcon getRedMineIcon() {
        return redMineIcon;
    }

    /**
     * @return ImageIcon return the clock
     */
    public ImageIcon getClockIcon() {
        return clockIcon;
    }

    /**
     * @return ImageIcon return the questionMark
     */
    public ImageIcon getQuestionMarkIcon() {
        return questionMarkIcon;
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