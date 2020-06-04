import numpy as np
import sys
import threading

sys.path.append('../utils')
import util.read as read
import util.redisUtil as my_redis
import operator


def lfm_train(train_data, F, alpha, beta, max_iter):
    """
    Args:
        train_data: 训练集
        F: 用户和文章的隐特征纬度
        alpha:正则化系数
        beta: 学习率
        max_iter: 迭代次数
    Return:
        dict: key art_id, value:np.ndarray
        dict: key user_id, value:np.ndarray
    """
    user_vec = {}
    article_vec = {}
    for iter_index in range(max_iter):
        for data_instance in train_data:
            user_id, art_id, label = data_instance
            if user_id not in user_vec:
                user_vec[user_id] = init_model(F)
            if art_id not in article_vec:
                article_vec[art_id] = init_model(F)
            delta = label - model_predict(user_vec[user_id], article_vec[art_id])
            for index in range(F):
                user_vec[user_id][index] += beta * (
                            delta * article_vec[art_id][index] - alpha * user_vec[user_id][index])
                article_vec[art_id][index] += beta * (
                            delta * user_vec[user_id][index] - alpha * article_vec[art_id][index])
        beta = beta * 0.9
    return user_vec, article_vec


def init_model(vector_len):
    """
    Args:
        vector_len: 向量纬度值
    Return:
         a ndarray
    """
    return np.random.randn(vector_len)


def model_predict(user_vector, article_vector):
    """
    计算用户向量和文章向量的余弦距离
    Args:
        user_vector: 用户向量
        article_vector: 文章项向量
    Return:
         距离值
    """
    res = np.dot(user_vector, article_vector) / (np.linalg.norm(user_vector) * np.linalg.norm(article_vector))
    return res


def model_train_process():
    train_data = read.get_train_data(read.get_rating_data())
    user_vec, article_vec = lfm_train(train_data, 50, 0.01, 0.1, 50)
    # print(user_vec["1"])
    # recom_result = give_recom_result(user_vec, article_vec, '24')
    # print(recom_result)
    for user_id in user_vec:
        threading.Thread(target=give_recom_result, args=(user_vec, article_vec, user_id)).start()


def give_recom_result(user_vec, article_vec, user_id):
    """
    获取用户推荐数据并保存到Redis数据库中
    """
    fix_num = 15
    if user_id not in user_vec:
        return []
    record = {}
    recom_list = []
    user_vector = user_vec[user_id]
    for art_id in article_vec:
        article_vector = article_vec[art_id]
        res = np.dot(user_vector, article_vector) / (np.linalg.norm(user_vector) * np.linalg.norm(article_vector))
        record[art_id] = res
    for zuhe in sorted(record.items(), key=operator.itemgetter(1), reverse=True)[:fix_num]:
        art_id = zuhe[0]
        score = round(zuhe[1], 3)
        recom_list.append((art_id, score))
    # 向redis中保存推荐数据
    my_redis.save_recommend(user_id, ','.join([str(item[0]) for item in recom_list]))


if __name__ == "__main__":
    model_train_process()
