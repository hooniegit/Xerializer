package com.hooniegit.Xerializer.Tester;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hooniegit.Xerializer.Serializer.KryoSerializer;

import jakarta.annotation.PostConstruct;

@Service
public class XerializeTest {

    @PostConstruct
    private void test() {
        List<String> hobby = new ArrayList<>();
        hobby.add("Football");
        hobby.add("Video Game");

        Sample<Profile> sample = new Sample("KDH", 30, hobby, new Profile("Hooniegit", 0));
        try {
            byte[] b = KryoSerializer.serialize(sample);
            Sample<Profile> s = KryoSerializer.deserialize(b);
            System.out.println(s.getName() + " : " + s.getAge() + " : " + s.getT().getNickname());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
