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

        Sample sample = new Sample("KDH", 30, hobby);
        try {
            byte[] b = KryoSerializer.serialize(sample);
            Sample s = KryoSerializer.<Sample>deserialize(b);
            System.out.println(s.getName() + " : " + s.getAge());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        

    }

}
