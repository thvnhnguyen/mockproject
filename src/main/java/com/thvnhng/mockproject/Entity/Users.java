package com.thvnhng.mockproject.Entity;

import com.thvnhng.mockproject.constant.SystemConstant;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
//@Data
@Getter
@Setter
public class Users extends AbstractEntity{

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "gender")
    private Character gender;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "one_time_password")
    private String oneTimePassword;

    @Column(name = "otp_requested_time")
    private Date otpRequestedTime;

    @Column(name = "course_name_permit")
    private String courseNamePermit;

    public boolean isOTPRequired() {
        if (this.getOneTimePassword() == null) {
            return false;
        }

        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis = this.otpRequestedTime.getTime();

        if (otpRequestedTimeInMillis + SystemConstant.OTP_VALID_DURATION < currentTimeInMillis) {
            // OTP expires
            return false;
        }

        return true;
    }

    @Transient
    public String getFullName() {
        StringBuilder name = new StringBuilder();
        name.append(firstName);
        name.append(" ");
        name.append(lastName);
        return name.toString();
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Roles> rolesList;

    @ManyToMany(mappedBy = "usersList")
    private List<Courses> coursesList;

    @OneToMany(mappedBy = "user")
    private List<Scores> scoresList;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subjects subjectUser;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(username, users.username)
                && Objects.equals(password, users.password)
                && Objects.equals(firstName, users.firstName)
                && Objects.equals(lastName, users.lastName)
                && Objects.equals(email, users.email)
                && Objects.equals(gender, users.gender)
                && Objects.equals(contactNumber, users.contactNumber)
                && Objects.equals(address, users.address)
                && Objects.equals(birthDate, users.birthDate)
                && Objects.equals(rolesList, users.rolesList)
                && Objects.equals(coursesList, users.coursesList)
                && Objects.equals(scoresList, users.scoresList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                username,
                password,
                firstName,
                lastName,
                email,
                gender,
                contactNumber,
                address,
                birthDate,
                oneTimePassword,
                otpRequestedTime,
                rolesList,
                coursesList,
                scoresList);
    }
}
