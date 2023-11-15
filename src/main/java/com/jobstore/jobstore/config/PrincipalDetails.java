package com.jobstore.jobstore.config;
import com.jobstore.jobstore.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class PrincipalDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

//    private Admin admin;
//    private User user;
    private Member member;
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    //해당 유저의 권한 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collector = new ArrayList<>();
        collector.add(() -> { return member.getRole();}); // 람다식

        return collector;
    }

    //비밀번호
    @Override
    public String getPassword() {
        return member.getPassword();
    }
    //아이디
    @Override
    public String getUsername() {
        return member.getMemberid();
    }
    //계정 만료 여부 true: 만료 안됨, false : 만료
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //계정 잠김 여부 true: 잠기지 않음
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //비밀번호 만료 여부 true: 만료 안됨
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //사용자 활성화 여부 true: 활성화
    @Override
    public boolean isEnabled() {
        return true;
    }
}