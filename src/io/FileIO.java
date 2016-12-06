/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import core.Database;
import core.Profile;
import core.ScheduleTimes;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.TreeMap;
import scheduleData.LectionData;
import utils.Time;
import static core.ScheduleTimes.DAYS;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import utils.Dialogs;

/**
 *
 * Speichern und Laden der Schüler- und Lection-Daten über JSON
 */
public class FileIO {

    private Database database;
    public static final String VALID_EXTENSION = "stdpl";
    private GsonBuilder gsonBuilder;
    private Gson gson;
    private ArrayList dataList;
    private ScheduleTimes scheduleTimes;
    private ArrayList<Profile> studentDataList;
    private ArrayList<TreeMap<Time, LectionData>> lectionMaps;
    private ArrayList<String> studentJournals;
    private HashMap<Integer, ArrayList<Integer>> attendanceFieldLists;

    public FileIO(Database database) {
        this.database = database;
        initGson();
        dataList = new ArrayList();
        lectionMaps = new ArrayList<>();
        studentJournals = new ArrayList<>();
        attendanceFieldLists = new HashMap<>();
    }

    private void initGson() {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.enableComplexMapKeySerialization();
        gson = gsonBuilder.create();
    }

    public void save(File file) {
        dataList.clear();
        dataList.add(database.getScheduleTimes());
        dataList.add(database.getStudentDataList());
        dataList.add(database.getStudentJournals());
        dataList.add(database.getAttendanceFieldLists());
        for (TreeMap<Time, LectionData> lectionMap : database.getLectionMaps()) {
            dataList.add(lectionMap);
        }
        String path = file.getPath();
        String suffix = "." + VALID_EXTENSION;
        if (!path.toLowerCase().endsWith(suffix)) {
            file = new File(path + suffix);
        }
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"))) {
            String json = gson.toJson(dataList);
            writer.write(json);
        } catch (Exception ex) {
            Dialogs.showSaveFileErrorMessage();
        }
    }

    public void load(File file) {
        lectionMaps.clear();
        try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(reader).getAsJsonArray();
            scheduleTimes = gson.fromJson(array.get(0), ScheduleTimes.class);
            studentDataList = gson.fromJson(array.get(1), new TypeToken<ArrayList<Profile>>() {
            }.getType());
            studentJournals = gson.fromJson(array.get(2), new TypeToken<ArrayList<String>>() {
            }.getType());
            attendanceFieldLists = gson.fromJson(array.get(3), new TypeToken<HashMap<Integer, ArrayList<Integer>>>() {
            }.getType());
            for (int i = 0; i < DAYS; i++) {
                TreeMap<Time, LectionData> lectionMap = gson.fromJson(array.get(i + 4), new TypeToken<TreeMap<Time, LectionData>>() {
                }.getType());
                lectionMaps.add(lectionMap);
            }
        } catch (Exception ex) {
            Dialogs.showLoadFileErrorMessage();
        }
    }

    public boolean isValidExtension(File file) {
        String fileExtension = "";
        String fileName = file.getName();
        int i = file.getName().lastIndexOf(".");
        if (i > 0 && i < fileName.length() - 1) {
            fileExtension = fileName.substring(i + 1).toLowerCase();
        }
        return fileExtension.equals(VALID_EXTENSION);
    }

    public ScheduleTimes getScheduleTimes() {
        return scheduleTimes;
    }

    public ArrayList<Profile> getStudentDataList() {
        return studentDataList;
    }

    public ArrayList<TreeMap<Time, LectionData>> getLectionMaps() {
        return lectionMaps;
    }

    public ArrayList<String> getStudentJournals() {
        return studentJournals;
    }

    public HashMap<Integer, ArrayList<Integer>> getAttendanceFieldLists() {
        return attendanceFieldLists;
    }
}
