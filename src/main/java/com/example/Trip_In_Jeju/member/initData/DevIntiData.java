package com.example.Trip_In_Jeju.member.initData;


import com.example.Trip_In_Jeju.member.entity.MemberRole;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevIntiData implements BeforeIntiData {
    @Bean
    CommandLineRunner initData(MemberService memberService) {
        return args -> {
            beforeInit();

            // member init
            String password = "1234"; // noop을 해주면 난수처럼 들어감
            memberService.signup2("admin","관리자",password,"admin@test.com","테마", MemberRole.ADMIN);
            memberService.signup2("user1","필써니",password,"user1@test.com","관광지",MemberRole.MEMBER);
            memberService.signup2("user2","송햄지",password,"user2@test.com","축제",MemberRole.MEMBER);
            memberService.signup2("user3","근혀기",password,"user3@test.com","음식",MemberRole.MEMBER);




        };
    }
}
