from apscheduler.schedulers.blocking import BlockingScheduler
import lfm.lfm as lfm


def do_job():
    scheduler = BlockingScheduler()
    #定时任务每天0点更新用户推荐列表
    scheduler.add_job(lfm.model_train_process, 'cron', hour=0, id='getAndSaveRecommend')
    scheduler.start()


do_job()
