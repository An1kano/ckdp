
if redis.call('EXISTS', KEYS[1]) == 1 then
    return 1
end

local count = redis.call('DECR', KEYS[2])
if count < 0 then
    redis.call('INCR', KEYS[2]) -- 恢复库存
    return 0
end

redis.call('SET', KEYS[1], 1)
return 2