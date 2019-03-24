package com.example.jake.university.profile;

import com.example.jake.university.API.postReq;
import com.example.jake.university.scholarships.Scholarships;
import com.example.jake.university.exams.ExamItem;
import com.example.jake.university.timetable.WeekSchedule;
import com.example.jake.university.timetable.scheduleServClasses.Day;
import com.example.jake.university.timetable.scheduleServClasses.Lesson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Singleton {

    /*Поля класса*/
    String nrec;
    BigInteger longNrec;
    String bigNrec;
    JSONArray arrExams;
    JSONObject jobj;
    postReq comand;
    ArrayList<ExamItem> arrears;
    ArrayList<ExamItem> upcomingExams;
    ArrayList<ExamItem> passedExams;
    ArrayList<Scholarships> scholarships;
    ProfileInfo profileInfo;
    WeekSchedule schedule;
    HashSet<String> lecturersList;;

    boolean parity;

    /* Конструктор */
    private Singleton(String nrec) throws ExecutionException, InterruptedException, JSONException {
        this.nrec = nrec;
            longNrec = new BigInteger(nrec.replaceFirst("0x8", ""), 16);
        bigNrec = longNrec.toString();
        setTimetable();
        setExamsList();
        setArrears();
        setPassedExams();
        setUpcomingExams();
        setProfileInfo();
        setScholarships();
    }

    private void setArrears() throws JSONException, ExecutionException, InterruptedException {
        arrears = new ArrayList();
        for (int i=0; i<arrExams.length(); i++) {
            jobj = arrExams.getJSONObject(i);
            if (jobj.getString("Положит").equals("0")) {
                arrears.add(new ExamItem(jobj.getString("Наименование дисциплины"),
                        jobj.getString("Тип"), jobj.getString("Семестр")));
            }
        }
    }

    private void setExamsList() throws ExecutionException, InterruptedException {
        jobj = new JSONObject();
        comand = new postReq();
        comand.execute("10", "A_LKS_GetMarks", nrec).get();
        arrExams = comand.getjARRAY();
    }

    private void setUpcomingExams() throws JSONException {
        upcomingExams = new ArrayList();
        for (int i=0; i<arrExams.length(); i++) {
            jobj = arrExams.getJSONObject(i);
            if (jobj.getString("Оценка").equals("нет оценки") || jobj.getString("Оценка").equals("")) {
                upcomingExams.add(new ExamItem(jobj.getString("Наименование дисциплины"),
                        jobj.getString("Тип"),  jobj.getString("Семестр")));
            }
        }
    }

    private void setPassedExams() throws JSONException {
        passedExams = new ArrayList();
        for (int i=0; i<arrExams.length(); i++) {
            jobj = arrExams.getJSONObject(i);
            if (jobj.getString("Оценка") != "" && jobj.getString("Оценка") != "нет оценки") {
                passedExams.add(new ExamItem(jobj.getString("Наименование дисциплины"),
                        jobj.getString("Тип"),  jobj.getString("Семестр"),
                        jobj.getString("Оценка")));
            }
        }
    }

    private void setScholarships() throws ExecutionException, InterruptedException, JSONException {
        JSONObject jobj = new JSONObject();
        postReq comand = new postReq();
        comand.execute("10","A_LKS_GetBonuses",nrec).get();
        JSONArray arr = comand.getjARRAY();
        scholarships = new ArrayList<>();
        for (int i=0; i<arr.length(); i++)
        {
            jobj=arr.getJSONObject(i);
            scholarships.add(new Scholarships(jobj.getString("Тип"), jobj.getString("Дата начала"),
                    jobj.getString("Дата окончания"), jobj.getString("Сумма")));
        }
    }

    private void setProfileInfo() throws JSONException, ExecutionException, InterruptedException {
        JSONObject obj = new JSONObject();
        postReq comand = new postReq();
        comand.execute("10","A_LKS_GetStudentInfo_Mobile",nrec).get();
        JSONArray arr = comand.getjARRAY();
        obj = arr.getJSONObject(0);
        profileInfo = new ProfileInfo(obj.getString("ФИО"), obj.getString("Факультет"),
                obj.getString("Источник финансирования обучения"),
                obj.getString("Форма обучения"), obj.getString("Группа"),
                obj.getString("Специальность"), "fdsf", "01.01.2018", obj.getString("Номер контактного телефона"));
    }
    private void setTimetable() throws JSONException, ExecutionException, InterruptedException
    {
        postReq comand = new postReq();
        comand.execute("10", "[dbo].[_A_SCD_StudSchedule]", "0," +bigNrec+ ",0,0,-1,2018,0,-1").get();
        JSONArray arr = comand.getjARRAY();
        schedule = new WeekSchedule(arr);
    }

    public ArrayList<ExamItem> getArrears(){return arrears;}

    public ArrayList<ExamItem> getUpcomingExams(){return upcomingExams;}

    public ArrayList<ExamItem> getPassedExams() {return passedExams;}

    public ArrayList<Scholarships> getScholarships() {return scholarships;}

    public ProfileInfo getProfileInfo() {return profileInfo;}

    public WeekSchedule getTimetable() {return schedule;}

    public boolean isParity() {
        return parity;
    }

    public void setParity() {

    }
    public HashSet<String> getLecturersList() {
        return lecturersList;
    }

    public void setLecturersList() {
        lecturersList = new HashSet<>();
        for (Day day : schedule.getSchedule()) {
            for (Lesson l: day.getEven()) {
                lecturersList.add(l.getTeacherName());
            }
            for (Lesson l: day.getOdd()){
                lecturersList.add(l.getTeacherName());
            }
        }
    }

    private static Singleton instance;

    /*Реализация Singleton*/
    public static Singleton getInstance(String nrec) throws ExecutionException, InterruptedException, JSONException {
        if(instance == null)
        {
            instance = new Singleton(nrec);
        }
        return instance;
    }
    public static Singleton getter() {return instance;}
}
