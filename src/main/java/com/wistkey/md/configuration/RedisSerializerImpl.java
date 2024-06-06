package com.wistkey.md.configuration;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class RedisSerializerImpl implements RedisSerializer<Long> {

    @Override
    public byte[] serialize(Long l) throws SerializationException {
        byte[] result = new byte[Long.BYTES];
        for (int i = Long.BYTES - 1; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= Byte.SIZE;
        }
        return result;
    }

    @Override
    public Long deserialize(byte[] b) throws SerializationException {
        if (b == null) {
            return null;
        }
        long result = 0;
        for (int i = 0; i < Long.BYTES; i++) {
            result <<= Byte.SIZE;
            result |= (b[i] & 0xFF);
        }
        return result;
    }
}
