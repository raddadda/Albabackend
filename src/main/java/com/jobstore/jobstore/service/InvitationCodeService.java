package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.StoreDto;
import com.jobstore.jobstore.entity.Store;
import com.jobstore.jobstore.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

//companynumber 찾아서 일치 하였을 때 초대코드 발급
@Service
public class InvitationCodeService {
    @Autowired
    StoreRepository storeRepository;
    public String generateInvitationCode(StoreDto storeDto) {
        String companynumber = storeDto.getCompanynumber();

        Store existingStore = storeRepository.findByCompanynumber(companynumber);
        if (existingStore != null) {
            // 초대코드 생성
            String code = generateCodeFromCompanyName(companynumber);
            existingStore.setInvitecode(code); // 초대코드 설정

            storeRepository.save(existingStore); // 변경된 초대코드를 DB에 저장

            return code;
        } else {
            return null;
        }
    }

    //초대 코드 발급 로직
    //아직은 고정값 X->추후에 고정 값이 전달 되게 로직 수정함
    private String generateCodeFromCompanyName(String companynumber) {
        // companyname을 이용하여 코드 생성
        String fixedValue = "Alba";
        String codeData = companynumber + fixedValue;
        //하나의 companynumber에 대한 고정된 하나의 초대 코드가 발급 된다
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeData.getBytes());
    }
}
