package com.shamlatech.pojo;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

public class PojoAttendanceKeyPair {
    String slot;
    String time;

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return  time;
    }
}
