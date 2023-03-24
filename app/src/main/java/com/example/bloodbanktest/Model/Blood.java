package com.example.bloodbanktest.Model;

public class Blood {
    String ap, an, bp, bn, abp, abn, op, on;

    public Blood() { }

    public Blood (String ap, String an, String bp, String bn, String abp, String abn, String op, String on){
        this.ap = ap;
        this.an = an;
        this.bp = bp;
        this.bn = bn;
        this.abp = abp;
        this.abn = abn;
        this.op = op;
        this.on = on;
    }

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getAbp() {
        return abp;
    }

    public void setAbp(String abp) {
        this.abp = abp;
    }

    public String getAbn() {
        return abn;
    }

    public void setAbn(String abn) {
        this.abn = abn;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }
}
