package com.springboot.peanut.data.entity;

import com.springboot.peanut.data.dto.signDto.AdditionalInfoDto;
import com.springboot.peanut.data.dto.signDto.KakaoResponseDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "USER_ID")
    private Long id;

    private String userName;

    private String phoneNumber;

    private String email;

    private String profileUrl;

    private String gender;

    private String birth;

    private String nickName;

    private String height;

    private String weight;

    private String password;

    private boolean verified = false;

    private String loginMethod;

    private LocalDateTime create_At;

    private LocalDateTime update_At;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>(List.of("MEMBER"));

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Intake> intakes = new ArrayList<>();


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Medicine> medicines = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Insulin> insulin = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MealInfo> mealInfos = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Community> communities = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CommunityLike> communityLikes = new ArrayList<>();

    // 환자의 보호자 관계들
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientGuardian> patientGuardians = new ArrayList<>();

    // 보호자의 환자 관계들
    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientGuardian> guardianPatients = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserName() {
        return this.userName;
    }
    public void saveProfile(String name, String gender, String birth, String nickname, String phoneNumber, String height, String weight, String encodedPassword) {
        this.userName = name;
        this.gender = gender;
        this.birth = birth;
        this.nickName = nickname;
        this.phoneNumber = phoneNumber;
        this.height = height;
        this.weight = weight;
        this.password = encodedPassword;
    }

    public static User createKakaoUser(KakaoResponseDto kakaoUserInfoResponse) {
        return User.builder()
                .email(kakaoUserInfoResponse.getEmail())
                .userName(kakaoUserInfoResponse.getUserName())
                .phoneNumber(kakaoUserInfoResponse.getPhoneNumber())
                .gender(kakaoUserInfoResponse.getGender())
                .birth(kakaoUserInfoResponse.getBirth())
                .profileUrl(kakaoUserInfoResponse.getProfileUrl())
                .loginMethod("Kakao")
                .create_At(LocalDateTime.now())
                .update_At(LocalDateTime.now())
                .build();
    }


    public void addKakaoAdditionalInfo(AdditionalInfoDto additionalInfoDto) {
        this.nickName = additionalInfoDto.getNickName();
        this.height = additionalInfoDto.getHeight();
        this.weight = additionalInfoDto.getWeight();
        this.update_At = LocalDateTime.now();
    }


    // 로그인 방법 설정 메서드
    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }

    // 타임스탬프 업데이트 메서드
    public void updateTimestamps(LocalDateTime currentTime) {
        this.create_At = currentTime;
        this.update_At = currentTime;
    }


}
