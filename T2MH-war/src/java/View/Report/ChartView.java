/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Report;

import Entity.Branch;
import Entity.Category;
import Entity.Groups;
import SessionBean.BranchFacadeLocal;
import SessionBean.CategoryFacadeLocal;
import SessionBean.GroupsFacadeLocal;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

@Named(value = "chartView")
@ManagedBean
@SessionScoped
public class ChartView implements Serializable {

    @PushEndpoint("/browser")
    public class BrowserStatsResource {

        @OnMessage(encoders = {JSONEncoder.class})
        public Map onMessage(Map data) {
            return data;
        }
    }

    @EJB
    private BranchFacadeLocal branchF;
    @EJB
    private CategoryFacadeLocal categoryF;

    @EJB
    private GroupsFacadeLocal groupsF;

    private PieChartModel pieModel;
    private ExternalContext externalContext;
    private boolean isShow;
    private String typeReport;
    private String typeOutput;
    private String typeCalculator;
    private Date firstDate;
    private Date secondDate;
    private Date dateCreate;
    private List<Category> AllCategory;
    private List<Groups> AllGroups;
    private int[] listChooseCategory;
    private int[] listChooseGroups;
    private int lengthOfLCC;
    private int lengthOfLGC;
    private Branch branch;
    private int branchIDInput;
    private int inputBranchID;
    private Map<String, Long> hmNameValue;
    //getter and setter

    public String getTypeCalculator() {
        return typeCalculator;
    }

    public void setTypeCalculator(String typeCalculator) {
        this.typeCalculator = typeCalculator;
    }

    public String getTypeOutput() {
        return typeOutput;
    }

    public void setTypeOutput(String typeOutput) {
        this.typeOutput = typeOutput;
    }

    public List<Groups> getAllGroups() {
        AllGroups = new ArrayList<>();
        for (int i = 0; i < listChooseCategory.length; i++) {
            AllGroups.addAll(categoryF.find(listChooseCategory[i]).getGroupsCollection());
        }
        return AllGroups;
    }

    public void setAllGroups(List<Groups> AllGroups) {
        this.AllGroups = AllGroups;
    }

    public int[] getListChooseGroups() {
        return listChooseGroups;
    }

    public void setListChooseGroups(int[] listChooseGroups) {
        this.listChooseGroups = listChooseGroups;
    }

    public long returnTotalAmount(Map<String, Long> hmNameValue) {
        if (hmNameValue == null) {
            hmNameValue = this.hmNameValue;
        }
        long totalAmount = 0;
        for (Map.Entry<String, Long> entry : hmNameValue.entrySet()) {
            totalAmount += entry.getValue();
        }
        return totalAmount;
    }

    public int getBranchIDInput() {
        return branchIDInput;
    }

    public void setBranchIDInput(int branchIDInput) {
        this.branchIDInput = branchIDInput;
    }

    public Map<String, Long> getHmNameValue() {
        return hmNameValue;
    }

    public void setHmNameValue(Map<String, Long> hmNameValue) {
        this.hmNameValue = hmNameValue;
    }

    public int getLengthOfLCC() {
        lengthOfLCC = 0;
        for (int i : listChooseCategory) {
            if (i > 0) {//category id must be greater than 0
                lengthOfLCC++;
            }
        }
        return lengthOfLCC;
    }

    public int getLengthOfLGC() {
        lengthOfLGC = 0;
        for (int i : listChooseGroups) {
            if (i > 0) {//category id must be greater than 0
                lengthOfLGC++;
            }
        }
        return lengthOfLGC;
    }

    public String getCateNameByID(int cateID) {
        try {
            return categoryF.find(cateID).getCateName();
        } catch (Exception e) {
            return "";
        }
    }

    public String getGroupsNameByID(int groupsID) {
        try {
            return groupsF.find(groupsID).getGroupName();
        } catch (Exception e) {
            return "";
        }
    }

    public int[] getListChooseCategory() {
        return listChooseCategory;
    }

    public void setListChooseCategory(int[] listChooseCategory) {
        this.listChooseCategory = listChooseCategory;
    }

    public List<Category> getAllCategory() {
        return AllCategory;
    }

    public void setAllCategory(List<Category> AllCategory) {
        this.AllCategory = AllCategory;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public Date getSecondDate() {
        return secondDate;
    }

    public void setSecondDate(Date secondDate) {
        this.secondDate = secondDate;
    }

    public String getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(String typeReport) {
        this.typeReport = typeReport;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public boolean isIsShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        if (initData().isEmpty()) {
            this.isShow = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data Is Empty, <br/>Cannot Render"));
        } else {
            this.isShow = isShow;
            pieModel.setData((Map) initData());
        }

    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public int getInputBranchID() {
        return inputBranchID;
    }

    public void setInputBranchID(int inputBranchID) {
        this.inputBranchID = inputBranchID;
    }

    //end getter and setter
    @PostConstruct
    public void init() {
        isShow = false;
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        dateCreate = new Date();
        pieModel = new PieChartModel();
        AllCategory = categoryF.findAll();
        AllGroups = groupsF.findAll();
        listChooseCategory = new int[AllCategory.size()];
        listChooseGroups = new int[AllGroups.size()];
        lengthOfLCC = 0;
        inputBranchID = 0;
        hmNameValue = new HashMap<String, Long>();
        pieModel.setTitle("Category Stats");
        pieModel.setShowDataLabels(true);
        pieModel.setLegendPosition("w");
        //autoset
        {
            firstDate = new Date();
            secondDate = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendarStartOfDate = Calendar.getInstance();
            calendarStartOfDate.setTime(firstDate);
            calendarStartOfDate.add(Calendar.MONTH, -1);
            calendarStartOfDate.set(Calendar.HOUR_OF_DAY, 0);
            calendarStartOfDate.set(Calendar.MINUTE, 0);
            calendarStartOfDate.set(Calendar.SECOND, 0);
            firstDate = calendarStartOfDate.getTime();
            //autosetListChoose
            listChooseCategory = new int[]{1, 2, 3, 4, 5, 6};
            //autosetyptreport
            typeReport = "system";
            if (typeReport.equalsIgnoreCase("branch")) {
                //auto set id
                inputBranchID = 1100;
                branch = branchF.find(inputBranchID);
            }
            typeOutput="cate";
            typeCalculator="no";
        }
    }

    public String returnMaxDate() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public void reloadPage() {
        isShow = false;
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        pieModel = new PieChartModel();
//        pieModel.setTitle("Category Stats");
        pieModel.setShowDataLabels(true);
        pieModel.setLegendPosition("w");
    }

    private Map<String, Long> initData() {
        if (typeReport.equalsIgnoreCase("branch") && branch == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data Is Empty, <br/>Branch Not Found"));
        }
        Map<Integer, Long> hmValueTypeReport = new HashMap<>();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (firstDate.after(secondDate)) {
            Date date = new Date();
            date = secondDate;
            secondDate = firstDate;
            firstDate = date;
        }
        try {
            firstDate = formatter.parse(formatter.format(firstDate));
            secondDate = formatter.parse(formatter.format(secondDate));
        } catch (Exception e) {
        }
        if (typeReport.equalsIgnoreCase("system")) {
            hmValueTypeReport = categoryF.getReport(listChooseCategory, listChooseGroups, typeReport, typeOutput, typeCalculator, -1, firstDate, secondDate);
        } else if (typeReport.equalsIgnoreCase("branch")) {
            hmValueTypeReport = categoryF.getReport(listChooseCategory, listChooseGroups, typeReport, typeOutput, typeCalculator, branch.getBranchID(), firstDate, secondDate);
        }
        List<Integer> listTypeOutput = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : hmValueTypeReport.entrySet()) {
            listTypeOutput.add(entry.getKey());
        }
        //sort
        for (int i = 0; i < listTypeOutput.size(); i++) {
            for (int j = i + 1; j < listTypeOutput.size(); j++) {
                if (hmValueTypeReport.get(listTypeOutput.get(i)) < hmValueTypeReport.get(listTypeOutput.get(j))) {
                    //index of smaller
                    int minValue = listTypeOutput.get(i);
                    int maxValue = listTypeOutput.get(j);

                    listTypeOutput.set(i, maxValue);
                    listTypeOutput.set(j, minValue);
                }
            }

        }
        //listCate.get(i) will return ID of HashMap
        for (Iterator<Integer> iterator = listTypeOutput.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            System.out.println(hmValueTypeReport.get(next));
        }
        hmNameValue = new HashMap<String, Long>();//name=groupsName or cateName depend typeOutput
        DecimalFormat DF = new DecimalFormat("#,###");
        if (hmValueTypeReport.size() < 5) {
            for (Map.Entry<Integer, Long> entry : hmValueTypeReport.entrySet()) {
                Integer key = entry.getKey();
                Long value = entry.getValue();
                if (typeOutput.equalsIgnoreCase("cate")) {
                    hmNameValue.put(categoryF.find(key).getCateName() + " [" + DF.format(value) + "]", value);
                } else {
                    hmNameValue.put(groupsF.find(key).getGroupName() + " [" + DF.format(value) + "]", value);
                }

            }
        } else if (hmValueTypeReport.size() >= 5) {
            //calculator of 4 first category
            long valueOther = 0;
            for (int i = 0; i < listTypeOutput.size(); i++) {
                if (i < 5) {
                    Long value = hmValueTypeReport.get(listTypeOutput.get(i));
                    if (typeOutput.equalsIgnoreCase("cate")) {
                        hmNameValue.put(categoryF.find(listTypeOutput.get(i)).getCateName() + " [" + DF.format(value) + "]", value);
                    } else {
                        hmNameValue.put(groupsF.find(listTypeOutput.get(i)).getGroupName() + " [" + DF.format(value) + "]", value);
                    }
                } else {
                    valueOther += hmValueTypeReport.get(listTypeOutput.get(i));
                }
            }
            if (typeOutput.equalsIgnoreCase("cate")) {
                hmNameValue.put("All Of Others Category" + " [" + DF.format(valueOther) + "]", valueOther);
            } else {
                hmNameValue.put("All Of Others Groups" + " [" + DF.format(valueOther) + "]", valueOther);
            }

        }
        return hmNameValue;
    }

    public void chooseTypeOutputListener(ValueChangeEvent event) {
        reloadPage();
        typeOutput = (String) event.getNewValue();
    }

    public void chooseTypeReportListener(ValueChangeEvent event) {
        reloadPage();
        typeReport = (String) event.getNewValue();
    }

    public void chooseTypeCalculatorListener(ValueChangeEvent event) {
        reloadPage();
        typeCalculator = (String) event.getNewValue();
    }

    public void onFirstDateSelect(SelectEvent event) {
        reloadPage();
        firstDate = (Date) event.getObject();
    }

    public void onCategoryChange() {
        reloadPage();
        List<Category> listCate = new ArrayList<>();
        AllGroups = new ArrayList<>();

        for (int j = 0; j < listChooseCategory.length; j++) {
            Category category = categoryF.find(listChooseCategory[j]);
            listCate.add(category);
            AllGroups.addAll(category.getGroupsCollection());
        }

        List<Integer> newList = new ArrayList<>();
        if (listChooseGroups != null) {
            for (int i = 0; i < listChooseGroups.length; i++) {
                Category category = groupsF.find(listChooseGroups[i]).getCateID();
                if (listCate.contains(groupsF.find(listChooseGroups[i]).getCateID()) == true) {
                    newList.add(listChooseGroups[i]);
                }
            }
            listChooseGroups = new int[newList.size()];
            for (int i = 0; i < newList.size(); i++) {
                listChooseGroups[i] = newList.get(i);
            }
        }
    }

    public void onGroupChange() {
        reloadPage();
    }

    public void onSecondDateSelect(SelectEvent event) {
        reloadPage();
        secondDate = (Date) event.getObject();
    }

    public void checkInputBrachID() {
        try {
            branch = branchF.find(inputBranchID);
            if (branch != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Load Infor Success"
                        + "<br/>Branch Name: " + branch.getBranchName()));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data Is Empty, <br/>Branch Not Found"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data Is Empty, <br/>Branch Not Found"));
        }
    }

    public String outputFirstDate(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendarStartOfDate = Calendar.getInstance();
        calendarStartOfDate.setTime(date);
        calendarStartOfDate.set(Calendar.HOUR_OF_DAY, 0);
        calendarStartOfDate.set(Calendar.MINUTE, 0);
        calendarStartOfDate.set(Calendar.SECOND, 0);
        date = calendarStartOfDate.getTime();
        return formatter.format(date);
    }

    public String outputSecondDate(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendarEndOfDate = Calendar.getInstance();
        calendarEndOfDate.setTime(date);
        calendarEndOfDate.set(Calendar.HOUR_OF_DAY, 23);
        calendarEndOfDate.set(Calendar.MINUTE, 59);
        calendarEndOfDate.set(Calendar.SECOND, 59);
        date = calendarEndOfDate.getTime();
        return formatter.format(date);
    }

    public String outputDateCreate() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return formatter.format(dateCreate);
    }

    public List<Integer> returnIdOfListCateChoose(int[] listChooseCategory) {
        List<Integer> listCategory = new ArrayList<>();
        for (int i = 0; i < listChooseCategory.length; i++) {
            listCategory.add(listChooseCategory[i]);
        }
        return listCategory;
    }

    public List<Integer> returnIdOfListGroupsChoose(int[] listChooseGroups) {
        List<Integer> listGroups = new ArrayList<>();
        for (int i = 0; i < listChooseGroups.length; i++) {
            listGroups.add(listChooseGroups[i]);
        }
        return listGroups;
    }

    public List<Groups> getListGroupsByCateID(int cateID) {
        return (List<Groups>) categoryF.find(cateID).getGroupsCollection();
    }
}
