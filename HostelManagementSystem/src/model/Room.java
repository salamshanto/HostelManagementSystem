package model;

import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private int roomId;
    private RoomType roomType;
    private double monthlyFee;
    private boolean cafeteriaIncluded;
    private boolean available;

    public Room(int roomId, RoomType roomType, double monthlyFee, boolean cafeteriaIncluded, boolean available) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.monthlyFee = monthlyFee;
        this.cafeteriaIncluded = cafeteriaIncluded;
        this.available = available;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public boolean isCafeteriaIncluded() {
        return cafeteriaIncluded;
    }

    public void setCafeteriaIncluded(boolean cafeteriaIncluded) {
        this.cafeteriaIncluded = cafeteriaIncluded;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return roomId + " - " + roomType;
    }
}


