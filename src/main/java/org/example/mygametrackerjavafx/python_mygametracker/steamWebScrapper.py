from selenium import webdriver

from urllib.request import urlopen
from bs4 import BeautifulSoup

import sys

from time import sleep

options = webdriver.ChromeOptions()
options.add_argument("--disable-blink-features=AutomationControlled")
options.add_argument("--headless")
driver = webdriver.Chrome(options=options)

game_name = sys.argv[1].lower()

def title_verifier_via_steam(game_titles):
    count = 0
    for game_title in game_titles:
        if game_name in game_title.text.lower():
            return True
        count += 1
        if count == 5:
            break
def title_verifier_via_google():
    driver.get(f"https://www.google.com/search?q={game_name}")
    sleep(2)

    html = driver.page_source

    bs = BeautifulSoup(html, "html.parser")

    allData = bs.find("div", {"class": "dURPMd"}).find_all("div", {"class": "Ww4FFb"})

    element = {}

    data = []

    for i in range(0, len(allData)):
        try:
            element["link"] = allData[i].find("a").get("href")
        except:
            element["link"] = None

        if "https://store.steampowered.com/app/" in element["link"]:
            data.append(element)
        element = {}

    return data

def scrapper():
    driver.get(f"https://store.steampowered.com/search/?term={game_name.lower().replace(' ', '+').strip().rstrip()}")

    html = urlopen(driver.current_url)

    bs = BeautifulSoup(html, "html.parser")

    game_titles = bs.find_all("div", {"class": "col search_name ellipsis"})
    title_verified = title_verifier_via_steam(game_titles)
    if title_verified:
        print(f"{title_verified}")
        print("steam")
    else:
        title_verified = False
        print(f"{title_verified}")
        print("steam")

    title_verified_google = title_verifier_via_google()
    if title_verified_google != None:
        title_verified_google = True
        print(f"{title_verified_google}")
        print("google")
    else:
        title_verified_google = False
        print(title_verified_google)
        print("google")
scrapper()
