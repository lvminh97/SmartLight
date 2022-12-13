import requests
from datetime import datetime, timedelta
from random import randrange

# time = datetime(2022, 12, 12, 12, 0, 0)
# for i in range(100):
#     light = randrange(65, 68)
#     power = randrange(39, 42)
#     resp = requests.post("http://smartlight.c1.biz/?action=set_data", {
#         "id" : 1,
#         "time" : time.strftime("%Y-%m-%d %H:%M:%S"),
#         "light" : light,
#         "power" : power
#     })
#     print(resp.text)
#     time = time + timedelta(seconds = 30)

resp = requests.post("http://localhost/?action=generate", {
    "id" : 1,
    "start" : "2022-12-07 08:00:00",
    "min_light" : 65,
    "max_light" : 67,
    "min_power" : 39,
    "max_power" : 41
})
print(resp.text)