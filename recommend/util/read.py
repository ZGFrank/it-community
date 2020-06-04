import pymysql


def get_rating_data():
    """
    从数据库中获取行为数据
    :return: [[user_id,art_id,rating],[]]
    """
    conn = pymysql.connect(host="192.168.56.110", user="root", password="123456", database="it-community",
                           charset="utf8")
    cursor = conn.cursor()
    # 在用户行为表中获取用户对文章的评分数据
    sql = "SELECT u_id, art_id, rating from ic_access_behavior;"
    cursor.execute(sql)
    ret = cursor.fetchall()
    cursor.close()
    conn.close()
    if len(ret) < 10000:
        return []
    # 将二维元组转化为二维列表
    rating_data = list(list(items) for items in list(ret))
    return rating_data


def get_ave_score(rating_data):
    """
    返回所有用户对文章评分的平均值字典
    :param rating_data: 用户行为数据
    :return: {art_id:avg()}
    """
    if len(rating_data) == 0:
        return {}
    record_dict = {}
    score_dict = {}
    for line in rating_data:
        user_id, art_id, rating = line[0], line[1], float(line[2])
        if art_id not in record_dict:
            record_dict[art_id] = [0, 0]
        record_dict[art_id][0] += 1
        record_dict[art_id][1] += rating
    for art_id in record_dict:
        score_dict[art_id] = round(record_dict[art_id][1] / record_dict[art_id][0], 3)
    return score_dict


def get_train_data(rating_data):
    """
    获取训练集
    :param rating_data: 用户行为数据
    :return: [正样本,负样本] =>[user_id,art_id,label] label正样本取1，负样本取0
    """
    if len(rating_data) == 0:
        return []
    score_dict = get_ave_score(rating_data)
    neg_dict = {}
    pos_dict = {}
    train_data = []
    score_thr = 7.0
    for line in rating_data:
        user_id, art_id, rating = line[0], line[1], float(line[2])
        if user_id not in pos_dict:
            pos_dict[user_id] = []
        if user_id not in neg_dict:
            neg_dict[user_id] = []
        if rating >= score_thr:
            pos_dict[user_id].append((art_id, 1))
        else:
            score = score_dict.get(art_id, 0)
            neg_dict[user_id].append((art_id, score))
    for user_id in pos_dict:
        data_num = min(len(pos_dict[user_id]), len(neg_dict.get(user_id, [])))
        if data_num > 0:
            train_data += [(user_id, zuhe[0], zuhe[1]) for zuhe in pos_dict[user_id]][:data_num]
        else:
            continue
        sorted_neg_list = sorted(neg_dict[user_id], key=lambda element: element[1], reverse=True)[:data_num]
        train_data += [(user_id, zuhe[0], 0) for zuhe in sorted_neg_list]
    return train_data
