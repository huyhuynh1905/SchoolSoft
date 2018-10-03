package sample.LoginApp.model;

public class GiaoVienModel {
    private String IdGv,HoGv,TenGv,NgaysinhGv,SexGv,PhoneGv,DiachiGv,KhoaGv;

    public GiaoVienModel(String idGv, String hoGv, String tenGv, String ngaysinhGv, String sexGv, String phoneGv, String diachiGv, String khoaGv) {
        IdGv = idGv;
        HoGv = hoGv;
        TenGv = tenGv;
        NgaysinhGv = ngaysinhGv;
        SexGv = sexGv;
        PhoneGv = phoneGv;
        DiachiGv = diachiGv;
        KhoaGv = khoaGv;
    }

    public String getIdGv() {
        return IdGv;
    }

    public void setIdGv(String idGv) {
        IdGv = idGv;
    }

    public String getHoGv() {
        return HoGv;
    }

    public void setHoGv(String hoGv) {
        HoGv = hoGv;
    }

    public String getTenGv() {
        return TenGv;
    }

    public void setTenGv(String tenGv) {
        TenGv = tenGv;
    }

    public String getNgaysinhGv() {
        return NgaysinhGv;
    }

    public void setNgaysinhGv(String ngaysinhGv) {
        NgaysinhGv = ngaysinhGv;
    }

    public String getSexGv() {
        return SexGv;
    }

    public void setSexGv(String sexGv) {
        SexGv = sexGv;
    }

    public String getPhoneGv() {
        return PhoneGv;
    }

    public void setPhoneGv(String phoneGv) {
        PhoneGv = phoneGv;
    }

    public String getDiachiGv() {
        return DiachiGv;
    }

    public void setDiachiGv(String diachiGv) {
        DiachiGv = diachiGv;
    }

    public String getKhoaGv() {
        return KhoaGv;
    }

    public void setKhoaGv(String khoaGv) {
        KhoaGv = khoaGv;
    }
}
