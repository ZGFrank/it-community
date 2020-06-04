export default function getFriendlyTime(time) {
  let currentTime = Date.parse(new Date());
  let dateTime = time; //后台传递来的时间
  let d_day = Date.parse(new Date(dateTime));
  let day = Math.abs(parseInt((d_day - currentTime) / 1000 / 3600 / 24)); //计算日期
  let hour = Math.abs(parseInt((d_day - currentTime) / 1000 / 3600)); //计算小时
  let minutes = Math.abs(parseInt((d_day - currentTime) / 1000 / 60)); //计算分钟
  let seconds = Math.abs(parseInt((d_day - currentTime)/1000));//计算秒
  if (parseInt(time.split("-")[0]) !== new Date().getFullYear()) {
    dateTime = dateTime.split(" ")[0];
  } else if (day > 7) {
    let arr = time.split(" ")[0].split("-");
    dateTime = arr[1] + "-" + arr[2];
  } else if (day === 7) {
    dateTime = "一周前"
  } else if (day >= 2 && day < 7) {
    dateTime = parseInt(day) + "天前"
  } else if (day > 0 && day < 2) {
    dateTime = "昨天";
  } else if (hour > 0 && hour < 24) {
    dateTime = parseInt(hour) + "小时前";
  } else if (minutes > 0 && minutes < 60) {
    dateTime = parseInt(minutes) + "分钟前";
  }else if (seconds > 0 && seconds < 60) {
    dateTime = "刚刚";
  }
  return dateTime;
}