package com.hooniegit.Xerializer.Tester;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.hooniegit.Xerializer.DataClass.Complexed;
import com.hooniegit.Xerializer.DataClass.Specified;
import com.hooniegit.Xerializer.Serializer.KryoSerializer;

import jakarta.annotation.PostConstruct;

@Service
public class XerializeTest {

    private final Random random = new Random();

    @PostConstruct
    private void test() {


        while(true) {

			for (int i = 1; i <= 6000; i++) {
                // Define Header
                HashMap<String, Object> header = new HashMap<>();
                header.put("timestamp", LocalDateTime.now().toString());

                List<Specified> body = new ArrayList<>();

				for (int j = 1; j <= 10; j++) {
					int category = j + (i - 1) * 10;

					for (int k = 1; k <= 30; k++) {
						int id = k + (j - 1) * 30 + (i - 1) * 300;

						body.add(new Specified(id, 
                                               random.nextInt(), 
                                               null, 
                                               category,
                                               null, 
                                               null,
                                               null));
					}
				}

                Complexed<List<Specified>> outer = new Complexed<>(header, body);


                try {
                    byte[] b = KryoSerializer.serialize(outer);
                    Complexed<List<Specified>> c = KryoSerializer.deserialize(b);
                    System.out.println(c.getBody().size());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

			}
        }

    }

}
