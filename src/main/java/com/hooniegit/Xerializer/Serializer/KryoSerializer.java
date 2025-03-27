package com.hooniegit.Xerializer.Serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 객체의 직렬화 및 역직렬화를 static 하게 수행하는 클래스입니다.<br>
 * - Apache Commons Pool2 기반으로 객체 풀을 구성해 인스턴스를 재사용합니다.<br>
 * - Esoteric Software Kryo 를 기반으로 객체의 직렬화/역직렬화를 수행합니다.
 */
public class KryoSerializer {

    private static final ObjectPool<Kryo> kryoPool;

    /**
     * 클래스 내에 Kryo 객체 풀을 구성합니다.
     */
    static {
        GenericObjectPoolConfig<Kryo> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10); // 객체 풀 최대 사이즈 조정

        kryoPool = new GenericObjectPool<>(new BasePooledObjectFactory<Kryo>() {
            @Override
            public Kryo create() {
                Kryo kryo = new Kryo();
                kryo.setClassLoader(Thread.currentThread().getContextClassLoader()); // 현재 실행중인 스레드의 ClassLoader 을 대신 호출 (Kryo 자체 ClassLoader 대체)
                return kryo;
            }

            @Override
            public PooledObject<Kryo> wrap(Kryo kryo) {
                return new DefaultPooledObject<>(kryo);
            }
        }, config);
    }

    /**
     * 특정 타입의 객체를 byte[] 데이터로 직렬화합니다.
     * @param <T>
     * @param object 객체 데이터
     * @return 직렬화 데이터
     * @throws Exception
     */
    public static <T> byte[] serialize(T object) throws Exception {
        Kryo kryo = null;
        try {
            kryo = kryoPool.borrowObject();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream);

            kryo.writeClassAndObject(output, object);

            output.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Serialization failed", e);
        } finally {
            if (kryo != null) {
                kryoPool.returnObject(kryo);
            }
        }
    }

    /**
     * byte[] 데이터를 역직렬화 후 원래 타입으로 캐스팅합니다.
     * @param <T> 
     * @param bytes byte[] 데이터 
     * @return 역직렬화 데이터
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes) throws Exception {
        Kryo kryo = null;
        try {
            kryo = kryoPool.borrowObject();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Input input = new Input(byteArrayInputStream);

            return (T) kryo.readClassAndObject(input);
        } catch (Exception e) {
            throw new RuntimeException("Deserialization failed", e);
        } finally {
            if (kryo != null) {
                kryoPool.returnObject(kryo);
            }
        }
    }


}