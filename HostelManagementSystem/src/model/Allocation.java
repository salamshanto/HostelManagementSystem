package model;

import java.io.Serializable;
import java.util.Date;

public class Allocation implements Serializable {
    private static final long serialVersionUID = 1L;

    private int allocationId;
    private int studentId;
    private int roomId;
    private Date startDate;
    private Date endDate;

    public Allocation(int allocationId, int studentId, int roomId, Date startDate, Date endDate) {
        this.allocationId = allocationId;
        this.studentId = studentId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Allocation " + allocationId + " (Student " + studentId + ", Room " + roomId + ")";
    }
}


