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
 * Data Serialization Class
 * - Kryo: For Rapid Data Serialization
 * - Netty: For Buffering
 * - Pool2: For Instance Re-Using
 * 
 * <Usage>
 * - Serialize T To ByteBuf
 * - De-Serialize ByteBuf To T
 */
public class KryoSerializer {

    private static final ObjectPool<Kryo> kryoPool;

    static {
        GenericObjectPoolConfig<Kryo> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10);
        kryoPool = new GenericObjectPool<>(new BasePooledObjectFactory<Kryo>() {
            @Override
            public Kryo create() {
                Kryo kryo = new Kryo();
                kryo.setClassLoader(Thread.currentThread().getContextClassLoader());
                return kryo;
            }
    
            @Override
            public PooledObject<Kryo> wrap(Kryo kryo) {
                return new DefaultPooledObject<>(kryo);
            }
        }, config);
    }

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