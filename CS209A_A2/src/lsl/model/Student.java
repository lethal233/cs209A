package lsl.model;

import javafx.beans.property.*;

import java.text.ParseException;
import java.util.Date;

import lsl.util.ValidatorUtils;
import org.apache.commons.csv.CSVRecord;

/**
 * @author Shilong Li (Lori)
 * @project CS209A_A2
 * @filename Student
 * @date 2021/4/15 21:06
 */
public class Student {
    private final StringProperty ID;
    private final StringProperty name;
    private final StringProperty gender;
    private final StringProperty department;
    private final DoubleProperty gpa;
    private final IntegerProperty creditEarned;
    private final ObjectProperty<Date> birthday;

    public Student() throws ParseException {
        this(null, null, "MALE", "CSE", 0.0, 0,
                "2000-01-01");
    }

    public Student(String ID,
                   String name,
                   String gender,
                   String department,
                   double gpa,
                   int creditEarned,
                   String birthday) throws ParseException {
        this.ID = new SimpleStringProperty(ID);
        this.name = new SimpleStringProperty(name);
        this.gender = new SimpleStringProperty(gender);
        this.department = new SimpleStringProperty(department);
        this.gpa = new SimpleDoubleProperty(gpa);
        this.creditEarned = new SimpleIntegerProperty(creditEarned);
        this.birthday = new SimpleObjectProperty<>(ValidatorUtils.sdf.parse(birthday));
    }

    public Student(CSVRecord record) throws ParseException {
        this(record.get("ID"),
                record.get("Name"),
                record.get("Gender"),
                record.get("Department"),
                Double.parseDouble(record.get("GPA")),
                Integer.parseInt(record.get("Credit Earned")),
                record.get("Birthday"));
    }

    public String getID() {
        return ID.get();
    }

    public StringProperty IDProperty() {
        return ID;
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getDepartment() {
        return department.get();
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public double getGpa() {
        return gpa.get();
    }

    public DoubleProperty gpaProperty() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa.set(gpa);
    }

    public int getCreditEarned() {
        return creditEarned.get();
    }

    public IntegerProperty creditEarnedProperty() {
        return creditEarned;
    }

    public void setCreditEarned(int creditEarned) {
        this.creditEarned.set(creditEarned);
    }

    public Date getBirthday() {
        return birthday.get();
    }

    public ObjectProperty<Date> birthdayProperty() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday.set(birthday);
    }

    public int hasContains(String a) {
        int count = 0;
        if (getID().contains(a)) count++;
        if (getName().contains(a)) count++;
        if (getGender().contains(a)) count++;
        if (getDepartment().contains(a)) count++;
        if (String.valueOf(getGpa()).contains(a)) count++;
        if (String.valueOf(getCreditEarned()).contains(a)) count++;
        if (ValidatorUtils.sdf.format(getBirthday()).contains(a)) count++;
        return count;
    }

    public int hasEquals(String a) {
        int count = 0;
        if (getID().equals(a)) count++;
        if (getName().equals(a)) count++;
        if (getGender().equals(a)) count++;
        if (getDepartment().equals(a)) count++;
        if (String.valueOf(getGpa()).equals(a)) count++;
        if (String.valueOf(getCreditEarned()).equals(a)) count++;
        if (ValidatorUtils.sdf.format(getBirthday()).equals(a)) count++;
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Student) {
            Student obj = (Student) o;
            if (this.getID()==null || this.getName() == null ) return false;
            return this.getID().equals(obj.getID())
                    && this.getGender().equals(obj.getGender())
                    && this.getDepartment().equals(obj.getDepartment())
                    && this.getName().equals(obj.getName())
                    && this.getGpa() == obj.getGpa()
                    && this.getCreditEarned() == obj.getCreditEarned()
                    && this.getBirthday().equals(obj.getBirthday());
        } else return false;
    }
}

