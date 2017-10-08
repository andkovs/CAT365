package backend.model;

import java.util.ArrayList;

public class OrderPreview {

    private Long id;
    private String depAirportName;
    private String arrAirportName;
    private String flightNameDirect;
    private String flightNameReverse;
    private String aircraftType;
    private String boardNumber;
    private String arriveDateTime;
    private String departureDateTime;
    private String readyDateTime;
    private String inspectionDateTime;
    private String workDateTime;
    private String directComment;
    private String reverseComment;
    private ArrayList<Ration> rationDirectBusinessList = new ArrayList<Ration>();
    private ArrayList<Ration> rationReverseBusinessList = new ArrayList<Ration>();
    private ArrayList<Ration> rationDirectEconomList = new ArrayList<Ration>();
    private ArrayList<Ration> rationReverseEconomList = new ArrayList<Ration>();
    private ArrayList<Ration> rationDirectCrewList = new ArrayList<Ration>();
    private ArrayList<Ration> rationReverseCrewList = new ArrayList<Ration>();
    private ArrayList<Ration> rationDirectSpecialList = new ArrayList<Ration>();
    private ArrayList<Ration> rationReverseSpecialList = new ArrayList<Ration>();
    private String drinkDirect;
    private String drinkReverse;
    private ArrayList<UploadFile> files = new ArrayList<UploadFile>();
    private ArrayList<UploadFile> filesFrom = new ArrayList<UploadFile>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepAirportName() {
        return depAirportName;
    }

    public void setDepAirportName(String depAirportName) {
        this.depAirportName = depAirportName;
    }

    public String getArrAirportName() {
        return arrAirportName;
    }

    public void setArrAirportName(String arrAirportName) {
        this.arrAirportName = arrAirportName;
    }

    public String getFlightNameDirect() {
        return flightNameDirect;
    }

    public void setFlightNameDirect(String flightNameDirect) {
        this.flightNameDirect = flightNameDirect;
    }

    public String getFlightNameReverse() {
        return flightNameReverse;
    }

    public void setFlightNameReverse(String flightNameReverse) {
        this.flightNameReverse = flightNameReverse;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getBoardNumber() {
        return boardNumber;
    }

    public void setBoardNumber(String boardNumber) {
        this.boardNumber = boardNumber;
    }

    public String getArriveDateTime() {
        return arriveDateTime;
    }

    public void setArriveDateTime(String arriveDateTime) {
        this.arriveDateTime = arriveDateTime;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getReadyDateTime() {
        return readyDateTime;
    }

    public void setReadyDateTime(String readyDateTime) {
        this.readyDateTime = readyDateTime;
    }

    public String getInspectionDateTime() {
        return inspectionDateTime;
    }

    public void setInspectionDateTime(String inspectionDateTime) {
        this.inspectionDateTime = inspectionDateTime;
    }

    public String getWorkDateTime() {
        return workDateTime;
    }

    public void setWorkDateTime(String workDateTime) {
        this.workDateTime = workDateTime;
    }

    public String getDirectComment() {
        return directComment;
    }

    public void setDirectComment(String directComment) {
        this.directComment = directComment;
    }

    public String getReverseComment() {
        return reverseComment;
    }

    public void setReverseComment(String reverseComment) {
        this.reverseComment = reverseComment;
    }

    public ArrayList<Ration> getRationDirectBusinessList() {
        return rationDirectBusinessList;
    }

    public void setRationDirectBusinessList(ArrayList<Ration> rationDirectBusinessList) {
        this.rationDirectBusinessList = rationDirectBusinessList;
    }

    public ArrayList<Ration> getRationReverseBusinessList() {
        return rationReverseBusinessList;
    }

    public void setRationReverseBusinessList(ArrayList<Ration> rationReverseBusinessList) {
        this.rationReverseBusinessList = rationReverseBusinessList;
    }

    public ArrayList<Ration> getRationDirectEconomList() {
        return rationDirectEconomList;
    }

    public void setRationDirectEconomList(ArrayList<Ration> rationDirectEconomList) {
        this.rationDirectEconomList = rationDirectEconomList;
    }

    public ArrayList<Ration> getRationReverseEconomList() {
        return rationReverseEconomList;
    }

    public void setRationReverseEconomList(ArrayList<Ration> rationReverseEconomList) {
        this.rationReverseEconomList = rationReverseEconomList;
    }

    public ArrayList<Ration> getRationDirectCrewList() {
        return rationDirectCrewList;
    }

    public void setRationDirectCrewList(ArrayList<Ration> rationDirectCrewList) {
        this.rationDirectCrewList = rationDirectCrewList;
    }

    public ArrayList<Ration> getRationReverseCrewList() {
        return rationReverseCrewList;
    }

    public void setRationReverseCrewList(ArrayList<Ration> rationReverseCrewList) {
        this.rationReverseCrewList = rationReverseCrewList;
    }

    public ArrayList<Ration> getRationDirectSpecialList() {
        return rationDirectSpecialList;
    }

    public void setRationDirectSpecialList(ArrayList<Ration> rationDirectSpecialList) {
        this.rationDirectSpecialList = rationDirectSpecialList;
    }

    public ArrayList<Ration> getRationReverseSpecialList() {
        return rationReverseSpecialList;
    }

    public void setRationReverseSpecialList(ArrayList<Ration> rationReverseSpecialList) {
        this.rationReverseSpecialList = rationReverseSpecialList;
    }

    public String getDrinkDirect() {
        return drinkDirect;
    }

    public void setDrinkDirect(String drinkDirect) {
        this.drinkDirect = drinkDirect;
    }

    public String getDrinkReverse() {
        return drinkReverse;
    }

    public void setDrinkReverse(String drinkReverse) {
        this.drinkReverse = drinkReverse;
    }

    public ArrayList<UploadFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<UploadFile> files) {
        this.files = files;
    }

    public ArrayList<UploadFile> getFilesFrom() {
        return filesFrom;
    }

    public void setFilesFrom(ArrayList<UploadFile> filesFrom) {
        this.filesFrom = filesFrom;
    }
}
