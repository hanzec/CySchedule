package edu.iastate.coms309.cyschedulebackend;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class GsonObjectSerializer implements RedisSerializer<Object> {

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return null;
    }
}
