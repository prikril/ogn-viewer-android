package com.meisterschueler.ognviewer;

public class FlarmMessage {
    private int AlarmLevel;      // decimal 0-3: 0 == no alarm, 1 == 13-18s to impact, 2 == 9-12s to impact, 3 == 0-8s to impact
    private int RelativeNorth;   // Decimal integer value. Range: from -32768 to 32767. Relative position in meters true north from own position.
    private int RelativeEast;    // Decimal integer value. Range: from -32768 to 32767. Relative position in meters true east from own position.
    private int RelativeVertical; // Decimal integer value. Range: from -32768 to 32767. Relative vertical separation in meters above own position.
    private int IDType;          // Decimal integer value. Range: from 0 to 3. 1 == official ICAO 24-bit aircraft address, 2 == stable FLARM ID (chosen by FLARM), 3 = anonymous ID
    private String ID;           // 6-digit hexadecimal value
    private int Track;           // Decimal integer value. Range: from 0 to 359.
    private int GroundSpeed;     // <GroundSpeed> Decimal integer value. Range: from 0 to 32767.
    private float ClimbRate;     // Decimal fixed point number with one digit after the radix. Range: from -32.7 to 32.7.
    private String AcftType;     // Hexadecimal value. Range: from 0 to F.

    public String toString() {
        return "PFLAA," + AlarmLevel + "," + RelativeNorth + "," + RelativeEast + "," + RelativeVertical + "," + IDType + "," + ID + "," + Track + "," + GroundSpeed + "," + ClimbRate + "," + AcftType;
        // PFLAA,<AlarmLevel>,<RelativeNorth>,<RelativeEast>,
        // <RelativeVertical>,<IDType>,<ID>,<Track>,<TurnRate>,<GroundSpeed>,
        // <ClimbRate>,<AcftType>
    }

    public int getAlarmLevel() {
        return AlarmLevel;
    }

    public void setAlarmLevel(int alarmLevel) {
        AlarmLevel = alarmLevel;
    }

    public int getRelativeNorth() {
        return RelativeNorth;
    }

    public void setRelativeNorth(int relativeNorth) {
        RelativeNorth = relativeNorth;
    }

    public int getRelativeEast() {
        return RelativeEast;
    }

    public void setRelativeEast(int relativeEast) {
        RelativeEast = relativeEast;
    }

    public int getRelativeVertical() {
        return RelativeVertical;
    }

    public void setRelativeVertical(int relativeVertical) {
        RelativeVertical = relativeVertical;
    }

    public int getIDType() {
        return IDType;
    }

    public void setIDType(int IDType) {
        this.IDType = IDType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getTrack() {
        return Track;
    }

    public void setTrack(int track) {
        Track = track;
    }

    public int getGroundSpeed() {
        return GroundSpeed;
    }

    public void setGroundSpeed(int groundSpeed) {
        GroundSpeed = groundSpeed;
    }

    public float getClimbRate() {
        return ClimbRate;
    }

    public void setClimbRate(float climbRate) {
        ClimbRate = climbRate;
    }

    public String getAcftType() {
        return AcftType;
    }

    public void setAcftType(String acftType) {
        AcftType = acftType;
    }
}