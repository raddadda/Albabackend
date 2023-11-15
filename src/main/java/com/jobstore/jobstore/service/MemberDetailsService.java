package com.jobstore.jobstore.service;

<<<<<<< HEAD:src/main/java/com/jobstore/jobstore/service/AdminDetailsService.java
import com.jobstore.jobstore.repository.AdminRepository;
=======
import com.jobstore.jobstore.entity.Admin;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.repository.MemberRepository;
>>>>>>> chan2:src/main/java/com/jobstore/jobstore/service/MemberDetailsService.java
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService  implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberid) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(memberid)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
                });
        return new MemberService(member);
    }
}
