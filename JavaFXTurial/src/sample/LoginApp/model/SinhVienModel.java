package sample.LoginApp.model;


public class SinhVienModel {
    private String IdSv,HoSv,TenSv,NgaysinhSv,SexSv,PhoneSv,DiachiSv,Lopsv;

    public SinhVienModel(String idSv, String hoSv, String tenSv, String ngaysinhSv, String sexSv, String phoneSv, String diachiSv, String Lopsv) {
        this.IdSv = idSv;
        this.HoSv = hoSv;
        this.TenSv = tenSv;
        this.NgaysinhSv = ngaysinhSv;
        this.SexSv = sexSv;
        this.PhoneSv = phoneSv;
        this.DiachiSv = diachiSv;
        this.Lopsv = Lopsv;
    }

    public String getIdSv() {
        return IdSv;
    }

    public void setIdSv(String idSv) {
        this.IdSv = idSv;
    }

    public String getHoSv() {
        return HoSv;
    }

    public void setHoSv(String hoSv) {
        this.HoSv = hoSv;
    }

    public String getTenSv() {
        return TenSv;
    }

    public void setTenSv(String tenSv) {
        this.TenSv = tenSv;
    }

    public String getNgaysinhSv() {
        return NgaysinhSv;
    }

    public void setNgaysinhSv(String ngaysinhSv) {
        this.NgaysinhSv = ngaysinhSv;
    }

    public String getSexSv() {
        return SexSv;
    }

    public void setSexSv(String sexSv) {
        this.SexSv = sexSv;
    }

    public String getPhoneSv() {
        return PhoneSv;
    }

    public void setPhoneSv(String phoneSv) {
        this.PhoneSv = phoneSv;
    }

    public String getDiachiSv() {
        return DiachiSv;
    }

    public void setDiachiSv(String diachiSv) {
        this.DiachiSv = diachiSv;
    }

    public String getLopsv() {
        return Lopsv;
    }

    public void setLopsv(String lopsv) {
        Lopsv = lopsv;
    }
}
