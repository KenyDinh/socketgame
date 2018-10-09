/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.common;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author KenyDinh
 */
public class CommonMethod {

    private static final GraphicsDevice GD = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static Map<String, String> mapFormatChat = new HashMap<>();

    public static void setLocationFrame(JFrame frame) {
        int monitor_height = GD.getDisplayMode().getHeight();
        int monitor_width = GD.getDisplayMode().getWidth();
        int frame_width = frame.getWidth();
        int frame_height = frame.getHeight();
        int location_width = (int) (monitor_width - frame_width) / 2;
        int location_height = (int) (monitor_height - frame_height) / 2;
        frame.setLocation(location_width, location_height);
    }

    public static String getContentFile(String pathFile) {
        StringBuilder sb = new StringBuilder();
        File file = new File(pathFile);
        if (file.exists() && file.isFile()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("##") || line.isEmpty()) {
                        continue;
                    }
                    sb.append(line);
                    sb.append(CommonDefine.BREAK_LINE);
                }
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }
        return sb.toString().trim();
    }

    public static boolean createFileWithContent(String pathFile, String content) {
        if (content.isEmpty()) {
            return true;
        }
        File file = new File(pathFile);
        boolean successed = false;
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            byte[] byte_content = content.getBytes("UTF-8");
            output.write(byte_content);
            output.flush();
            output.close();
            successed = true;
        } catch (IOException e) {
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                }
            }
        }
        return successed;
    }

    public static boolean storeClienData(Map<String, Map<String, String>> mapData) {
        StringBuilder sb = new StringBuilder();

        for (Entry<String, Map<String, String>> entry : mapData.entrySet()) {
            sb.append(entry.getKey()).append(">");//key
            for (Entry<String, String> en : entry.getValue().entrySet()) {
                sb.append(en.getKey()).append(CommonDefine.SEPARATOR_KEY_VALUE).append(getEncryptContent(en.getValue()));
                sb.append(CommonDefine.COMMA);
            }
            sb.append(CommonDefine.BREAK_LINE);
        }
        return createFileWithContent(CommonDefine.DATA_FILE_NAME, getEncryptContent(sb.toString().trim()));
    }

    public static Map<String, Map<String, String>> getMapClientData() {
        String content = getDecryptContent(getContentFile(CommonDefine.DATA_FILE_NAME));
        Map<String, Map<String, String>> mapData = new HashMap<>();
        for (String line : content.split(CommonDefine.BREAK_LINE)) {
            if (line.contains(">")) {
                String key = line.substring(0, line.indexOf(">"));
                Map<String, String> value = new HashMap<>();
                for (String s : line.substring(line.indexOf(">") + 1, line.length()).split(CommonDefine.COMMA)) {
                    value.put(s.split(CommonDefine.SEPARATOR_KEY_VALUE)[0], getDecryptContent(s.split(CommonDefine.SEPARATOR_KEY_VALUE)[1]));
                }
                if (!value.isEmpty()) {
                    mapData.put(key, value);
                }
            }
        }
        return mapData;
    }

    public static Map<String, Map<Integer, Integer>> getMapItemOwn() {
        String content = getDecryptContent(getContentFile(CommonDefine.ITEM_OWN_FILE_NAME));
        Map<String, Map<Integer, Integer>> mapItemOwn = new HashMap<>();
        for (String line : content.split(CommonDefine.BREAK_LINE)) {
            if (line.length() > 0 && line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                if (arr.length == 2) {
                    String key = arr[0].trim();
                    String val = arr[1].trim();
                    Map<Integer, Integer> value = new HashMap<>();
                    for (String s : val.split(CommonDefine.COMMA)) {
                        String array[] = s.split("-");
                        if (array.length == 2) {
                            value.put(Integer.parseInt(array[0].trim()), Integer.parseInt(array[1].trim()));
                        }
                    }
                    if (!value.isEmpty()) {
                        mapItemOwn.put(key, value);
                    }
                }
            }
        }
        return mapItemOwn;
    }

    public static boolean storeItemData(Map<String, Map<Integer, Integer>> mapItem) {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Map<Integer, Integer>> entry : mapItem.entrySet()) {
            sb.append(entry.getKey()).append(CommonDefine.SEPARATOR_KEY_VALUE);
            for (Entry<Integer, Integer> ent : entry.getValue().entrySet()) {
                sb.append(ent.getKey()).append("-").append(ent.getValue());

            }
            sb.append(CommonDefine.BREAK_LINE);
        }

        return createFileWithContent(CommonDefine.ITEM_OWN_FILE_NAME, getEncryptContent(sb.toString().trim()));
    }

    public static String getMapDXHData() {
        return getDecryptContent(getContentFile(CommonDefine.MAP_HASH_FILE_NAME));
    }

    public static Map<Integer, String> getBoomMap() {
        Map<Integer, String> mapData = new HashMap<>();
        File file = new File("data\\BoomMap.txt");
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder boomData = new StringBuilder();
                int index = 0;
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.matches("-+")) {
                        ++index;
                        mapData.put(index, boomData.toString());
                        boomData = new StringBuilder();
                    } else {
                        boomData.append(line);
                    }
                }
                log("Number of map boom: " + index);
                br.close();
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
        }
        return mapData;
    }

    public static String getEncryptContent(String content) {
        return EncriptString.vigenereEncrypt(EncriptString.getEncodeBase64(content));
    }

    public static String getDecryptContent(String content) {
        return EncriptString.getDecodeBase64(EncriptString.vigenereDecrypt(content));
    }

    public static Map<String, String> getConfigKeyValue() {
        String content = getDecryptContent(getContentFile(CommonDefine.CONFIG_FILE_NAME));
        Map<String, String> mapValue = new HashMap<>();
        if (content.contains(CommonDefine.BREAK_LINE)) {
            for (String line : content.split(CommonDefine.BREAK_LINE)) {
                if (line.length() > 0 && line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                    String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                    if (arr.length == 2) {
                        String key = arr[0].trim();
                        String value = arr[1].trim();
                        if (key.length() > 0 && value.length() > 0) {
                            mapValue.put(key, value);
                        }
                    }
                }
            }
        }
        return mapValue;
    }

    public static String formatMapToContentFile(Map<String, String> mapValue) {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : mapValue.entrySet()) {
            sb.append(entry.getKey()).append(CommonDefine.SEPARATOR_KEY_VALUE).append(CommonDefine.SPACE).append(entry.getValue());
            sb.append(CommonDefine.BREAK_LINE);
        }
        return getEncryptContent(sb.toString().trim());
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        str = str.trim();
        return str.matches("-*\\+*[0-9]+");
    }
    
    public static boolean isValidNumber(String str){
        if (str == null || str.length() == 0) {
            return false;
        }
        return str.matches("[0-9]+");
    }

    public static String getDateFormatStringNow() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(CommonDefine.TIME_FORMAT_PATERN);
        return sdf.format(c.getTime());
    }
    
    public static String getFullDateFormatString(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(c.getTime());
    }
    
    public static String formatMessageServer(String mess) {
        return "[" + getDateFormatStringNow() + "] <em>" + CommonDefine.SERVER_MESSAGE + ":</em> <span style=\"color:#EE3316;\">" + mess + "</span>";
    }

    public static String formatPrivateMessageTo(String from, String mess) {
        return "[" + getDateFormatStringNow() + "][Private From " + from + "]: <span style=\"color:#B712E4;\">" + mess + "</span>";//pink
    }

    public static String formatPrivateMessageFrom(String to, String mess) {
        return "[" + getDateFormatStringNow() + "][Private To " + to + "]: <span style=\"color:#B712E4;\">" + mess + "</span>";//pink
    }

    public static String formatMessageClient(String name, String mess) {
        return "[" + getDateFormatStringNow() + "] " + name + ": " + mess;
    }

    public static String formatClientRename(String old_name, String new_name) {
        return "[" + getDateFormatStringNow() + "] <em>" + CommonDefine.SERVER_MESSAGE + ":</em> <span style=\"color:#EE3316;\">" + old_name + "'ve rename -> [" + new_name + "]</span>";//red
    }

    public static String formatMessageInGame(String name, String mess) {
        return "[" + getDateFormatStringNow() + "][In Game] " + name + ": <span style=\"color:#22C46C;\">" + mess + "</span>";//green
    }

    public static String formatHTMLMessClient(String mess) {

        return "";
    }

    public static String getChooseTextString(int choice) {
        switch (choice) {
            case CommonDefine.BAUCUA_VALUE_OF_BAU:
                return CommonDefine.BAUCUA_STR_OF_BAU;
            case CommonDefine.BAUCUA_VALUE_OF_CUA:
                return CommonDefine.BAUCUA_STR_OF_CUA;
            case CommonDefine.BAUCUA_VALUE_OF_CA:
                return CommonDefine.BAUCUA_STR_OF_CA;
            case CommonDefine.BAUCUA_VALUE_OF_TOM:
                return CommonDefine.BAUCUA_STR_OF_TOM;
            case CommonDefine.BAUCUA_VALUE_OF_GA:
                return CommonDefine.BAUCUA_STR_OF_GA;
            case CommonDefine.BAUCUA_VALUE_OF_HUOU:
                return CommonDefine.BAUCUA_STR_OF_HUOU;
            default:
                return "";
        }
    }

    public static String getChooseImgString(int choice) {
        switch (choice) {
            case CommonDefine.BAUCUA_VALUE_OF_BAU:
                return "bau";
            case CommonDefine.BAUCUA_VALUE_OF_CUA:
                return "cua";
            case CommonDefine.BAUCUA_VALUE_OF_CA:
                return "ca";
            case CommonDefine.BAUCUA_VALUE_OF_TOM:
                return "tom";
            case CommonDefine.BAUCUA_VALUE_OF_GA:
                return "ga";
            case CommonDefine.BAUCUA_VALUE_OF_HUOU:
                return "huou";
            default:
                return "";
        }
    }

    public static void log(String content) {
        String filename = "server_log.log";
        createFileWithContent(filename, getContentFile(filename) + CommonDefine.BREAK_LINE + getFullDateFormatString() + " " + content);
    }

    public static String getMACAddress() {
        StringBuilder sb = new StringBuilder();
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(ip);
            byte[] mac = ni.getHardwareAddress();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    public static void initPropChat() {
        String filename = "prop\\chathelp.properties";
        Properties propChat = new Properties();
        File file = new File(filename);
        if (file.exists()) {
            try {
                propChat.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                for (Entry<Object, Object> entry : propChat.entrySet()) {
                    mapFormatChat.put(entry.getKey().toString(), entry.getValue().toString());
                }
            } catch (IOException ex) {

            }
        }
    }

    public static String getShopData() {
        return getContentFile(CommonDefine.ITEM_INFO_FILE_NAME);
    }

}
