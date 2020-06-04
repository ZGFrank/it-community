import redis

# 连接池
pool = redis.ConnectionPool(host="192.168.56.110", port=6379, max_connections=1024)
conn = redis.Redis(connection_pool=pool)


def save_recommend(user_id, recom_list_str):
    conn.set(user_id, recom_list_str)
