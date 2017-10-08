package backend.model;

import java.util.ArrayList;

public class Order {

    private Long id;
    private Long depAirportId;
    private Long arrAirportId;
    private Long boardId;
    private Long flightIdDirect;
    private Long flightIdReverse;
    private String arriveDateTime;
    private String departureDateTime;
    private String workDateTime;
    private String inspectionDateTime;
    private String readyDateTime;
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
    private Drink drinkDirect;
    private Drink drinkReverse;
    private ArrayList<UploadFile> files = new ArrayList<UploadFile>();

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepAirportId() {
        return depAirportId;
    }

    public void setDepAirportId(Long depAirportId) {
        this.depAirportId = depAirportId;
    }

    public Long getArrAirportId() {
        return arrAirportId;
    }

    public void setArrAirportId(Long arrAirportId) {
        this.arrAirportId = arrAirportId;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Long getFlightIdDirect() {
        return flightIdDirect;
    }

    public void setFlightIdDirect(Long flightIdDirect) {
        this.flightIdDirect = flightIdDirect;
    }

    public Long getFlightIdReverse() {
        return flightIdReverse;
    }

    public void setFlightIdReverse(Long flightIdReverse) {
        this.flightIdReverse = flightIdReverse;
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

    public String getWorkDateTime() {
        return workDateTime;
    }

    public void setWorkDateTime(String workDateTime) {
        this.workDateTime = workDateTime;
    }

    public String getInspectionDateTime() {
        return inspectionDateTime;
    }

    public void setInspectionDateTime(String inspectionDateTime) {
        this.inspectionDateTime = inspectionDateTime;
    }

    public String getReadyDateTime() {
        return readyDateTime;
    }

    public void setReadyDateTime(String readyDateTime) {
        this.readyDateTime = readyDateTime;
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

    public Drink getDrinkDirect() {
        return drinkDirect;
    }

    public void setDrinkDirect(Drink drinkDirect) {
        this.drinkDirect = drinkDirect;
    }

    public Drink getDrinkReverse() {
        return drinkReverse;
    }

    public void setDrinkReverse(Drink drinkReverse) {
        this.drinkReverse = drinkReverse;
    }

    public ArrayList<UploadFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<UploadFile> files) {
        this.files = files;
    }
}
