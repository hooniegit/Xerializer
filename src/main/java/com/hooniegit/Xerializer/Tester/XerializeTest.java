package com.hooniegit.Xerializer.Tester;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import com.hooniegit.Xerializer.Serializer.KryoSerializer;

@Service
public class XerializeTest {

    @PostConstruct
    private void test() {
        // 객체 생성 예제
        List<String> hobby = new ArrayList<>();
        hobby.add("Football");
        hobby.add("Video Game");
        Sample<Profile> sample = new Sample("KDH", 30, hobby, new Profile("Hooniegit", 0));

        // 패키지 테스트 예제
        try {
            byte[] b = KryoSerializer.serialize(sample); // 직렬화
            Sample<Profile> s = KryoSerializer.deserialize(b); // 역직렬화
            System.out.println(s.getName() + " : " + s.getAge() + " : " + s.getT().getNickname());
            System.out.println(s.getHobby().size());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
