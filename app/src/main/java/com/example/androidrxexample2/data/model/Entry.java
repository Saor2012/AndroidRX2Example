package com.example.androidrxexample2.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Objects;

@Entity(tableName = "entry_model2",primaryKeys = {"keyId"})
public class Entry {
    @NonNull
    private String keyId;

    private String value;
    private String date;

    public Entry() {
    }

    public Entry(@NonNull String keyId, String value) {
        this.keyId = keyId;
        this.value = value;
        this.date = "";
    }

    public Entry(@NonNull String keyId, String value, String date) {
        this.keyId = keyId;
        this.value = value;
        this.date = date;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "keyId='" + keyId + '\'' +
                ", value='" + value + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return keyId.equals(entry.keyId) &&
                Objects.equals(value, entry.value) &&
                Objects.equals(date, entry.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId, value, date);
    }
}
